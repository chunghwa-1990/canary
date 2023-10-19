package com.example.canary.task.core;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * 线程任务
 *
 * @since 1.0
 * @author zhaohongliang
 */
@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
public class BusinessTask extends AbstractTask {

    /**
     * 类
     */
    private Object object;

    /**
     * 方法
     */
    private Method method;


    /**
     * 无参构造
     */
    public BusinessTask() {
    }

    /**
     * 全参构造
     *
     * @param taskName
     * @param cornExpression
     * @param object
     * @param method
     */
    public BusinessTask(String taskName, String cornExpression, Object object, Method method) {
        super(taskName, cornExpression);
        this.object = object;
        this.method = method;
    }

    /**
     * override execute method
     */
    @Override
    public void execute() {
        try {
            method.invoke(object);
        } catch (Exception e) {
            log.error("{} 执行失败，异常信息：{}", super.getTaskName(), e.getMessage());
        }
    }
}
