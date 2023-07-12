package com.example.canary.core.context;

import jakarta.validation.constraints.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * SpringBean 上下文
 *
 * @since 1.0
 * @author zhaohongliang
 */
public class SpringContext implements ApplicationContextAware {

    /**
     * application context
     */
    private static ApplicationContext context;

    /**
     * 获取 application context
     *
     * @return
     */
    public static ApplicationContext getContext() {
        return context;
    }

    /**
     * 设置 application context
     *
     * @param context
     */
    public static void setContext(ApplicationContext context) {
        SpringContext.context = context;
    }

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        SpringContext.setContext(applicationContext);
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
