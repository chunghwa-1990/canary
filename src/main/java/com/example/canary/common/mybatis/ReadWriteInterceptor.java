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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Map;
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
@ConditionalOnProperty(value = "spring.datasource.cluster.enabled")
@Component
public class ReadWriteInterceptor implements Interceptor {

    @Autowired
    private DataSourceHealthIndicator dataSourceHealthIndicator;

    private AtomicInteger index = new AtomicInteger(0);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        // boolean isMaster = mappedStatement.getId().toLowerCase(Locale.ENGLISH).contains(DataSourceEnum.MASTER.getKey())
        boolean synchronizationActive = TransactionSynchronizationManager.isSynchronizationActive();
        // 获取写入操作数据源 key
        // DataSourceEnum writeDataSourceKey = getWriteDataSourceKey();

        // 当前是否处于事务同步活动状态
        if (!synchronizationActive) {
            if (mappedStatement.getSqlCommandType().equals(SqlCommandType.SELECT)) {
                // 负载均衡策略
                int currentIndex = Math.abs(index.getAndIncrement() % 2);
                DataSourceEnum slaveKey = DataSourceEnum.getSlaveValues().get(currentIndex);
                DataSourceContextHolder.setDataSourceKey(slaveKey);
            } else {
                DataSourceContextHolder.setDataSourceKey(DataSourceEnum.MASTER);
            }
        } else {
            DataSourceContextHolder.setDataSourceKey(DataSourceEnum.MASTER);
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
    private DataSourceEnum getWriteDataSourceKey() {
        Health health = dataSourceHealthIndicator.health();
        // 获取健康状态的详细信息
        Map<String, Object> details = health.getDetails();
        if ("not healthy".equals(details.get("master"))) {
            // 主库不健康，选择备库
            return DataSourceEnum.SLAVE1;
        } else {
            // 主库健康，选择主库
            return DataSourceEnum.MASTER;
        }
    }

}
