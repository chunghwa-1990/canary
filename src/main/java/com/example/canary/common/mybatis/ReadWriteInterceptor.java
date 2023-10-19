package com.example.canary.common.mybatis;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 拦截器动态切换数据源
 *
 * @author zhaohongliang 2023-10-16 22:39
 * @since 1.0
 */
@Slf4j
@Intercepts({
    @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }),
    @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class }),
    @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class })
})
@Component
public class ReadWriteInterceptor implements Interceptor {

    @Autowired
    @Qualifier("masterDataSource")
    private DataSource masterDataSource;

    @Autowired
    @Qualifier("slave1DataSource")
    private DataSource slave1DataSource;

    private AtomicInteger index = new AtomicInteger(0);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        // boolean isMaster = mappedStatement.getId().toLowerCase(Locale.ENGLISH).contains(ReadWriteEnum.MASTER.getKey())
        boolean synchronizationActive = TransactionSynchronizationManager.isSynchronizationActive();
        // 获取写入操作数据源 key
        ReadWriteEnum writeDataSourceKey = getWriteDataSourceKey();

        // 当前是否处于事务同步活动状态
        if (!synchronizationActive) {
            if (mappedStatement.getSqlCommandType().equals(SqlCommandType.SELECT)) {
                // 负载均衡策略
                int currentIndex = Math.abs(index.getAndIncrement() % 2);
                ReadWriteEnum slaveKey = ReadWriteEnum.getSlaveValues().get(currentIndex);
                DataSourceContextHolder.setDataSourceKey(slaveKey);
            } else {
                DataSourceContextHolder.setDataSourceKey(writeDataSourceKey);
            }
        } else {
            DataSourceContextHolder.setDataSourceKey(writeDataSourceKey);
        }

        try {
            return invocation.proceed();
        } finally {
            // reset index
            if (index.get() == Integer.MAX_VALUE) {
                index = new AtomicInteger(0);
            }
            // clear
            DataSourceContextHolder.clearDataSourceKey();
        }
    }

    /**
     * 获取写入操作数据源 key
     *
     * @return
     */
    private ReadWriteEnum getWriteDataSourceKey() {
        Connection masterConn = null;
        Connection slave1Conn = null;
        try {
            masterConn = masterDataSource.getConnection();
            slave1Conn = slave1DataSource.getConnection();
            if (masterConn.isValid(5) && !slave1Conn.isValid(5)) {
                return ReadWriteEnum.MASTER;
            } else if (!masterConn.isValid(5) && slave1Conn.isValid(5)) {
                return ReadWriteEnum.SLAVE1;
            } else {
                return ReadWriteEnum.MASTER;
            }
        } catch (SQLException e) {
            log.error("数据源主备写入服务发生异常，异常信息：" + e.getMessage());
            throw new MyBatisSystemException(e.getCause());
        } finally {
            if (masterConn != null) {
                try {
                    masterConn.close();
                } catch (SQLException e) {
                    log.error("关闭主库连接发生异常，异常信息：" + e.getMessage());
                }
            }
            if (slave1Conn != null) {
                try {
                    slave1Conn.close();
                } catch (SQLException e) {
                    log.error("关闭备库连接发生异常，异常信息：" + e.getMessage());
                }
            }
        }
    }
}
