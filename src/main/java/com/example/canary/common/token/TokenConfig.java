package com.example.canary.common.token;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
     * {@link TokenProperties} tokenProperties.initializeTokenBuilder().build()
     * {@link TokenBuilder} new TokenBuilder("secret", Duration.ZERO).build()
     * TokenBuilder.create().secret("secret").timeout(Duration.ZERO).build()
     * TokenBuilder.create(tokenProperties).build()
     *
     * @param tokenProperties token 配置文件
     * @return tokenService
     */
    @Bean
    @ConditionalOnMissingBean(TokenService.class)
    public TokenService tokenService(TokenProperties tokenProperties) {
        return tokenProperties.initializeTokenBuilder().build();
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
