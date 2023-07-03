package com.example.canary.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Locale;

/**
 * 国际化配置
 *
 * @ClassName I18nConfig
 * @Description 国际化配置
 * @Author zhaohongliang
 * @Date 2023-07-03 14:10
 * @Since 1.0
 */
@Configuration
class I18nConfig {


    /**
     * i18n 拦截器
     *
     * @return
     */
    @Bean
    public LocaleChangeInterceptor getLocaleChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        return localeChangeInterceptor;
    }

    /**
     * i18n 解析器
     *
     * @return
     */
    @Bean
    public LocaleResolver localeResolver() {
        // cookie 区域解析器
        CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
        cookieLocaleResolver.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
        return cookieLocaleResolver;
    }

}
