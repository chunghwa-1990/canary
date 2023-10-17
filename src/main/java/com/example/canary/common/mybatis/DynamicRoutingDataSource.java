package com.example.canary.common.mybatis;

import jakarta.annotation.Nonnull;
import lombok.Getter;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.Map;

/**
 * 动态数据源路由
 *
 * @author zhaohongliang 2023-10-16 20:24
 * @since 1.0
 */
@Getter
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {

    private Map<Object, Object> targetDataSources;

    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.getDataSourceKey();
    }

    @Override
    public void setTargetDataSources(@Nonnull Map<Object, Object> targetDataSources) {
        this.targetDataSources = targetDataSources;
        super.setTargetDataSources(targetDataSources);
        super.afterPropertiesSet();
    }


}
