package com.example.canary.task.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.canary.common.entity.ResultEntity;
import com.example.canary.context.SpringContext;
import com.example.canary.task.entity.TaskAO;
import com.example.canary.task.entity.TaskPO;
import com.example.canary.task.entity.TaskQuery;
import com.example.canary.task.entity.TaskVO;
import com.example.canary.task.repository.TaskRepository;
import com.example.canary.task.service.TaskService;
import com.example.canary.task.thread.BusinessTask;
import com.example.canary.task.thread.CronTaskRegistrar;
import com.example.canary.task.thread.ITask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 任务
 *
 * @ClassName TaskServiceImpl
 * @Description 任务
 * @Author zhaohongliang
 * @Date 2023-06-29 10:22
 * @Since 1.0
 */
@Slf4j
@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private CronTaskRegistrar cronTaskRegistrar;


    /**
     * pages
     *
     * @param query request query parameters
     * @return response page result
     */
    @Override
    public ResultEntity<Page<TaskVO>> pagesTask(TaskQuery query) {
        return null;
    }

    /**
     * save
     *
     * @param taskAo request object
     * @return response result
     */
    @Override
    @SuppressWarnings("rawtypes")
    public ResultEntity saveTask(TaskAO taskAo) {
        TaskPO taskPo = taskAo.convertToPo();
        try {
            int result = taskRepository.insert(taskPo);
            if (result > 0) {
                return ResultEntity.success();
            }
        } catch (Exception e) {
            Throwable cause = e.getCause();
            if (cause instanceof SQLIntegrityConstraintViolationException) {
                String errorMessage = cause.getMessage();
                if (StringUtils.hasText(errorMessage) && errorMessage.contains("udx_name_1")) {
                    return ResultEntity.fail("task name has exist");
                }
            }

        }
        return ResultEntity.fail();
    }

    /**
     * update
     *
     * @param taskAo request object
     * @return response result
     */
    @Override
    @SuppressWarnings("rawtypes")
    public ResultEntity updateTask(TaskAO taskAo) {
        TaskPO taskPo = taskAo.convertToPo();
        try {
            int result = taskRepository.update(taskPo);
            if (result > 0) {
                return ResultEntity.success();
            }
        } catch (Exception e) {
            Throwable cause = e.getCause();
            if (cause instanceof SQLIntegrityConstraintViolationException) {
                String errorMessage = cause.getMessage();
                if (StringUtils.hasText(errorMessage) && errorMessage.contains("udx_name_1")) {
                    return ResultEntity.fail("task name has exist");
                }
            }

        }
        return ResultEntity.fail();
    }

    /**
     * delete
     *
     * @param taskId task primary key
     * @return response result
     */
    @Override
    @SuppressWarnings("rawtypes")
    public ResultEntity deleteTask(String taskId) {
        taskRepository.deleteById(taskId);
        return ResultEntity.success();
    }

    /**
     * execute
     *
     * @param taskId task primary key
     * @return response result
     */
    @Override
    @SuppressWarnings("rawtypes")
    public ResultEntity executeTask(String taskId) {
        TaskPO taskPo = taskRepository.selectById(taskId);
        if (taskPo == null) {
            return ResultEntity.fail();
        }

        Object object = null;
        Method method = null;
        try {
            Class<?> clazz = Class.forName(taskPo.getClassName());
            String beanName = com.example.canary.util.StringUtils.toLowerCamelCase(clazz.getSimpleName());
            object = SpringContext.getBean(beanName);
            method = clazz.getMethod(taskPo.getMethodName());
        } catch (ClassNotFoundException e) {
            log.error("启动 {} 任务失败，原因：找不到 {} 类，异常信息：{}", taskPo.getName(),  taskPo.getClassName(), e.getMessage());
            return ResultEntity.fail("启动 " + taskPo.getName() + " 任务失败，原因：找不到" + taskPo.getClassName() + "类");
        } catch (NoSuchMethodException e) {
            log.error("启动 {} 任务失败，原因：找不到 {} 方法，异常信息：{}", taskPo.getName(), taskPo.getMethodName(), e.getMessage());
            return ResultEntity.fail("启动 " + taskPo.getName() + " 任务失败，原因：找不到" + taskPo.getMethodName() + "方法");
        }

        ITask task = new BusinessTask(taskPo.getName(), taskPo.getCronExpression(), object, method);
        cronTaskRegistrar.executeCronTask(task);

        return ResultEntity.success();
    }

    /**
     * start
     *
     * @param taskId task primary key
     * @return response result
     */
    @Override
    @SuppressWarnings("rawtypes")
    public ResultEntity startTask(String taskId) {
        TaskPO taskPo = taskRepository.selectById(taskId);
        if (taskPo == null) {
            return ResultEntity.fail();
        }

        Object object = null;
        Method method = null;
        try {
            Class<?> clazz = Class.forName(taskPo.getClassName());
            String beanName = com.example.canary.util.StringUtils.toLowerCamelCase(clazz.getSimpleName());
            object = SpringContext.getBean(beanName);
            method = clazz.getMethod(taskPo.getMethodName());
        } catch (ClassNotFoundException e) {
            log.error("启动 {} 任务失败，原因：找不到 {} 类，异常信息：{}", taskPo.getName(),  taskPo.getClassName(), e.getMessage());
            return ResultEntity.fail("启动 " + taskPo.getName() + " 任务失败，原因：找不到" + taskPo.getClassName() + "类");
        } catch (NoSuchMethodException e) {
            log.error("启动 {} 任务失败，原因：找不到 {} 方法，异常信息：{}", taskPo.getName(), taskPo.getMethodName(), e.getMessage());
            return ResultEntity.fail("启动 " + taskPo.getName() + " 任务失败，原因：找不到" + taskPo.getMethodName() + "方法");
        }

        ITask task = new BusinessTask(taskPo.getName(), taskPo.getCronExpression(), object, method);
        cronTaskRegistrar.addCronTask(taskPo.getId(), task, task.getCornExpression());

        return ResultEntity.success();
    }

    /**
     * stop
     *
     * @param taskId task primary key
     * @return response result
     */
    @Override
    @SuppressWarnings("rawtypes")
    public ResultEntity stopTask(String taskId) {
        TaskPO taskPo = taskRepository.selectById(taskId);
        if (taskPo == null) {
            return ResultEntity.fail();
        }
        cronTaskRegistrar.removeCronTask(taskId);
        return ResultEntity.success();
    }

}
