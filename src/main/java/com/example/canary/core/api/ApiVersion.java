package com.example.canary.core.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Api 版本号自定义注解</p>
 *
 * @ClassName ApiVersion
 * @Description Api 版本号自定义注解
 * @Author zhaohongliang
 * @Date 2023-07-04 21:17
 * @Since 1.0
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
