package com.example.canary.common.mybatis;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

/**
 * 数据源AOP切面
 *
 * @author zhaohongliang 2023-10-17 21:27
 * @since 1.0
 */
@Slf4j
@Aspect
@Component
public class DataSourceAspect {


    /**
     * 切点
     */
    @Pointcut("@annotation(com.example.canary.common.mybatis.DataSource) || @within(com.example.canary.common.mybatis.DataSource)")
    public void pointcut() {
    }


    @Around("pointcut()")
    public Object proceed(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();

        // 查找方法上面的注解
        DataSource dataSource = AnnotationUtils.findAnnotation(methodSignature.getMethod(), DataSource.class);
        if (dataSource == null) {
            // 查找类上面的注解
            dataSource = AnnotationUtils.findAnnotation(methodSignature.getDeclaringType(), DataSource.class);
        }

        if (dataSource != null) {
            // 数据源名称
            ReadWriteEnum readWriteEnum = dataSource.value();
            DataSourceContextHolder.setDataSourceKey(readWriteEnum);
        }
        // 执行目标方法
        return proceedingJoinPoint.proceed();
    }

    @Before("pointcut()")
    public void before() {
        // TODO document why this method is empty
    }

    @After("pointcut()")
    public void after() {
        DataSourceContextHolder.clearDataSourceKey();
    }


}
