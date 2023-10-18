package com.example.canary.common.mybatis;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 只读
 *
 * @author zhaohongliang 2023-10-18 01:27
 * @since 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ReadOnly {

    /**
     * value
     *
     * @return
     */
    boolean value() default true;
}
