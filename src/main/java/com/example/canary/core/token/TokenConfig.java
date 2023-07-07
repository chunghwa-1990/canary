package com.example.canary.core.token;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * token 配置
 *
 * @ClassName TokenConfig
 * @Description token 配置
 * @Author zhaohongliang
 * @Date 2023-07-06 13:19
 * @Since 1.0
 */
@Configuration
@EnableConfigurationProperties(TokenProperties.class)
public class TokenConfig implements WebMvcConfigurer {

    /**
     * token 拦截器
     *
     * @return
     */
    @Bean
    public TokenInterceptor getTokenInterceptor() {
        return new TokenInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration interceptorRegistration = registry.addInterceptor(getTokenInterceptor());
        interceptorRegistration.addPathPatterns("/**");
        interceptorRegistration.excludePathPatterns("/login");
    }
}
