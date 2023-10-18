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
import org.springframework.transaction.support.TransactionSynchronizationManager;

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
public class ReadWriteInterceptor implements Interceptor {

    private AtomicInteger index = new AtomicInteger(0);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        // boolean isMaster = mappedStatement.getId().toLowerCase(Locale.ENGLISH).contains(ReadWriteEnum.MASTER.getKey())
        boolean synchronizationActive = TransactionSynchronizationManager.isSynchronizationActive();
        // 当前是否处于事务同步活动状态
        if (!synchronizationActive) {
            if (mappedStatement.getSqlCommandType().equals(SqlCommandType.SELECT)) {
                // 负载均衡策略
                int currentIndex = Math.abs(index.getAndIncrement() % 2);
                ReadWriteEnum slaveKey = ReadWriteEnum.getSlaveValues().get(currentIndex);
                DataSourceContextHolder.setDataSourceKey(slaveKey);
            } else {
                DataSourceContextHolder.setDataSourceKey(ReadWriteEnum.MASTER);
            }
        } else {
            DataSourceContextHolder.setDataSourceKey(ReadWriteEnum.MASTER);
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
}
