package com.example.canary.task.schedule;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * 线程任务
 *
 * @since 1.0
 * @author zhaohongliang
 */
@Slf4j
public class BusinessTask extends AbstractTask {

    private Object object;

    private Method method;


    public BusinessTask() {
    }

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
            log.error("{} 执行失败,异常信息：{}", super.getTaskName(), e.getMessage());
        }
    }
}
