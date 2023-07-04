package com.example.canary.core;

import com.example.canary.common.annotation.ApiVersion;
import jakarta.validation.constraints.NotNull;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

/**
 * api version handlerMapping
 *
 * @ClassName ApiVersionHandlerMapping
 * @Description api version handlerMapping
 * @Author zhaohongliang
 * @Date 2023-07-04 21:14
 * @Since 1.0
 */
public class ApiVersionHandlerMapping extends RequestMappingHandlerMapping {

    @Override
    protected RequestCondition<?> getCustomTypeCondition(@NotNull Class<?> handleType) {
        // 扫描类上@ApiVersion
        ApiVersion version = AnnotationUtils.findAnnotation(handleType, ApiVersion.class);
        return createRequestCondition(version);
    }

    @Override
    protected RequestCondition<?> getCustomMethodCondition(@NotNull Method method) {
        // 扫描方法上的@ApiVersion
        ApiVersion version = AnnotationUtils.findAnnotation(method, ApiVersion.class);
        return createRequestCondition(version);
    }

    private RequestCondition<ApiVersionCondition> createRequestCondition(ApiVersion version) {
        if (version == null) {
            return null;
        }
        // int value = Integer.valueOf(version.value());
        // Assert.isTrue(value >= 1, "Api Version Must be greater than or equal to 1");
        return new ApiVersionCondition(version.value());
    }
}
