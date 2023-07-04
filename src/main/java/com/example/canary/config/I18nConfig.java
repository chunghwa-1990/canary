package com.example.canary.config;

import com.example.canary.common.constant.BaseConstant;
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
        localeChangeInterceptor.setParamName(BaseConstant.PARAM_KEY_LANG);
        return localeChangeInterceptor;
    }

    /**
     * i18n 解析器
     * custom {@link com.example.canary.resolver.MyLocalResolver}
     *
     * @return
     */
    @Bean
    public LocaleResolver localeResolver() {

        // accept

        // session

        // fixed

        // custom

        // cookie 区域解析器
        CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
        cookieLocaleResolver.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
        return cookieLocaleResolver;
    }

}
