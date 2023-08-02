package com.example.canary.common.context;

import jakarta.annotation.Nullable;
import lombok.Getter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * SpringBean 上下文
 *
 * @since 1.0
 * @author zhaohongliang
 */
@Getter
@Component
public class SpringContext implements ApplicationContextAware {

    /**
     * application context
     */
    private static ApplicationContext context;

    /**
     * 设置 application context
     *
     * @param context application上下文
     */
    private static void setContext(ApplicationContext context) {
        SpringContext.context = context;
    }

    @Override
    public void setApplicationContext(@Nullable ApplicationContext applicationContext) throws BeansException {
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
