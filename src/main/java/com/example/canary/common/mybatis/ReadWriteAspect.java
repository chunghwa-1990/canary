package com.example.canary.common.mybatis;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 读写分离数据源AOP切面
 *
 * @author zhaohongliang 2023-10-18 17:06
 * @since 1.0
 */
@Slf4j
@Aspect
@Component
public class ReadWriteAspect {

    /**
     * 在 @ReadOnly 方法执行执行前后操作数据源
     *
     * @param proceedingJoinPoint
     * @param readOnly
     * @return
     * @throws Throwable
     */
    @Around("@annotation(readOnly)")
    public Object proceed(ProceedingJoinPoint proceedingJoinPoint, ReadOnly readOnly) throws Throwable {
        if (readOnly.value()) {
            // 负载均衡策略
            DataSourceContextHolder.setDataSourceKey(ReadWriteEnum.SLAVE1);
        } else {
            DataSourceContextHolder.setDataSourceKey(ReadWriteEnum.MASTER);
        }

        try {
            // 执行目标方法
            return proceedingJoinPoint.proceed();
        } finally {
            DataSourceContextHolder.clearDataSourceKey();
        }
    }
}
