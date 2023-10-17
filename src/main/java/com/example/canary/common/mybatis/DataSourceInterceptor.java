package com.example.canary.common.mybatis;

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

/**
 * 拦截器动态切换数据源
 *
 * @author zhaohongliang 2023-10-16 22:39
 * @since 1.0
 */
@Intercepts({
    @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }),
    @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class }),
    @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class })
})
public class DataSourceInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        // boolean isMaster = mappedStatement.getId().toLowerCase(Locale.ENGLISH).contains(DataSourceEnum.MASTER.getKey())

        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        boolean synchronizationActive = TransactionSynchronizationManager.isSynchronizationActive();

        if (!synchronizationActive) {
            if (mappedStatement.getSqlCommandType().equals(SqlCommandType.SELECT)) {
                DataSourceContextHolder.setDataSourceKey(DataSourceEnum.SLAVE1);
            } else {
                DataSourceContextHolder.setDataSourceKey(DataSourceEnum.MASTER);
            }
        } else {
            DataSourceContextHolder.setDataSourceKey(DataSourceEnum.MASTER);
        }

        try {
            return invocation.proceed();
        } finally {
            DataSourceContextHolder.clearDataSourceKey();
        }
    }
}
