package com.example.canary.core.token;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * token 配置
 *
 * @since 1.0
 * @author zhaohongliang
 */
@Configuration
@ConditionalOnClass(TokenService.class)
@EnableConfigurationProperties(TokenProperties.class)
public class TokenConfig implements WebMvcConfigurer {

    /**
     * tokenService
     *
     * @param tokenProperties
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(TokenService.class)
    public TokenService tokenService(TokenProperties tokenProperties) {
        return TokenBuilder.create(tokenProperties).build();
    }

    /**
     * token 拦截器
     *
     * @return TokenInterceptor
     */
    @Bean
    public TokenInterceptor getTokenInterceptor() {
        return new TokenInterceptor();
    }

    /**
     * 添加拦截器
     *
     * @param registry 拦截器注册器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration interceptorRegistration = registry.addInterceptor(getTokenInterceptor());
        interceptorRegistration.addPathPatterns("/**");
        interceptorRegistration.excludePathPatterns("/sys/login");
    }

    /**
     * 添加跨域配置
     *
     * @param registry 跨域注册器
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedHeaders("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD")
                .maxAge(3600);
    }
}
