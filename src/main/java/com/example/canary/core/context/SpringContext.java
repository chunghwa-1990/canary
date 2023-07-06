package com.example.canary.core.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * SpringBean 上下文
 *
 * @ClassName SpringContext
 * @Description SpringBean 上下文
 * @Author zhaohongliang
 * @Date 2023-06-29 17:13
 * @Since 1.0
 */
public class SpringContext implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }


    public static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }

    public static <T> T getBean(String beanName, Class<T> clazz) {
        return context.getBean(beanName, clazz);
    }

    public static Object getBean(String beanName) {
        return context.getBean(beanName);
    }
}
