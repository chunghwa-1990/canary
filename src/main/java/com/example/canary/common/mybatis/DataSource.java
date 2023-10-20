package com.example.canary.common.mybatis;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据源
 *
 * @author zhaohongliang 2023-10-18 01:27
 * @since 1.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataSource {

    /**
     * value
     *
     * @return
     */
    @AliasFor("name")
    DataSourceEnum value() default DataSourceEnum.MASTER;

    /**
     * name
     *
     * @return
     */
    @AliasFor("value")
    DataSourceEnum name() default  DataSourceEnum.MASTER;

}
