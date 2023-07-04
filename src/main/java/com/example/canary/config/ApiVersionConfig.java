package com.example.canary.config;

import com.example.canary.core.ApiVersionHandlerMapping;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * api 版本配置
 *
 * @ClassName ApiVersionConfig
 * @Description api 版本配置
 * @Author zhaohongliang
 * @Date 2023-07-04 21:00
 * @Since 1.0
 */
@Configuration
public class ApiVersionConfig implements WebMvcRegistrations {

    @Override
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        return new ApiVersionHandlerMapping();
    }

}
