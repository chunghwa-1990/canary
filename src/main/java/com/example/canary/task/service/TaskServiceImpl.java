package com.example.canary.task.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.canary.core.context.SpringContext;
import com.example.canary.core.exception.ResultEntity;
import com.example.canary.task.entity.TaskAO;
import com.example.canary.task.entity.TaskPO;
import com.example.canary.task.entity.TaskQuery;
import com.example.canary.task.entity.TaskVO;
import com.example.canary.task.repository.TaskRepository;
import com.example.canary.task.schedule.AbstractTask;
import com.example.canary.task.schedule.BusinessTask;
import com.example.canary.task.schedule.CronTaskRegistrar;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 任务
 *
 * @since 1.0
 * @author zhaohongliang
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
            taskRepository.insert(taskPo);
        } catch (Exception e) {
            Throwable cause = e.getCause();
            if (cause instanceof SQLIntegrityConstraintViolationException) {
                String errorMessage = cause.getMessage();
                if (StringUtils.hasText(errorMessage) && errorMessage.contains("udx_name_1")) {
                    return ResultEntity.fail("task name has exist");
                }
            }
            return ResultEntity.fail();
        }
        return ResultEntity.success();
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
            taskRepository.update(taskPo);
        } catch (Exception e) {
            Throwable cause = e.getCause();
            if (cause instanceof SQLIntegrityConstraintViolationException) {
                String errorMessage = cause.getMessage();
                if (StringUtils.hasText(errorMessage) && errorMessage.contains("udx_name_1")) {
                    return ResultEntity.fail("task name has exist");
                }
            }
            return ResultEntity.fail();
        }
        return ResultEntity.success();
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

        AbstractTask task = new BusinessTask(taskPo.getName(), taskPo.getCronExpression(), object, method);
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

        AbstractTask task = new BusinessTask(taskPo.getName(), taskPo.getCronExpression(), object, method);
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
