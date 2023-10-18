package com.example.canary.common.mybatis;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 读写
 *
 * @author zhaohongliang 2023-10-18 01:44
 * @since 1.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataSource {
    @AliasFor("dataSourceName")
    ReadWriteEnum value() default ReadWriteEnum.MASTER;

    @AliasFor("value")
    ReadWriteEnum dataSourceName() default ReadWriteEnum.MASTER;
}
