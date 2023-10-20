package com.example.canary.common.mybatis;

/**
 * 数据源上下文
 *
 * @author zhaohongliang 2023-10-16 20:39
 * @since 1.0
 */
public class DataSourceContextHolder {

    private DataSourceContextHolder() {}

    private static final ThreadLocal<DataSourceEnum> CONTEXT_HOLDER = new ThreadLocal<>();

    public static void setDataSourceKey(DataSourceEnum dataSourceKey) {
        CONTEXT_HOLDER.set(dataSourceKey);
    }

    public static DataSourceEnum getDataSourceKey() {
        return CONTEXT_HOLDER.get();
    }

    public static void clearDataSourceKey() {
        CONTEXT_HOLDER.remove();
    }
}
