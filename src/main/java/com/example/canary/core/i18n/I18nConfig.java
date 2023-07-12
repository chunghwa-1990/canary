package com.example.canary.core.i18n;

import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Locale;

/**
 * 国际化配置
 *
 * @since 1.0
 * @author zhaohongliang
 */
@Configuration
class I18nConfig implements WebMvcConfigurer {

    @Autowired
    private MessageSource messageSource;


    /**
     * i18n 拦截器
     *
     * @return
     */
    @Bean
    public LocaleChangeInterceptor getLocaleChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName(I18nConstant.LANG);
        return localeChangeInterceptor;
    }

    /**
     * i18n 解析器
     * custom {@link MyLocalResolver}
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

    /**
     * 添加拦截器
     *
     * @param registry 拦截器注册器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 国际化
        InterceptorRegistration localChangeRegistration = registry.addInterceptor(getLocaleChangeInterceptor());
        localChangeRegistration.addPathPatterns("/**");
    }

    @Override
    public Validator getValidator() {
        LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
        factoryBean.setValidationMessageSource(messageSource);
        factoryBean.setProviderClass(HibernateValidator.class);
        return factoryBean;
    }

}
