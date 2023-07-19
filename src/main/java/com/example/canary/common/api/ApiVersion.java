package com.example.canary.common.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Api 版本号自定义注解
 *
 * @since 1.0
 * @author zhaohongliang
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiVersion {

    /**
     * 版本号
     *
     * @return
     */
    String value() default "v1.0";

    /**
     * 版本描述
     *
     * @return
     */
    String description() default "";
}
