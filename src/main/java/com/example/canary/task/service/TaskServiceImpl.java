package com.example.canary.task.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.canary.common.context.SpringContext;
import com.example.canary.common.exception.BusinessException;
import com.example.canary.common.exception.ResultEntity;
import com.example.canary.task.entity.TaskAO;
import com.example.canary.task.entity.TaskPO;
import com.example.canary.task.entity.TaskQuery;
import com.example.canary.task.entity.TaskVO;
import com.example.canary.task.repository.TaskRepository;
import com.example.canary.task.core.AbstractTask;
import com.example.canary.task.core.BusinessTask;
import com.example.canary.task.core.CronTaskRegistrar;
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
    public Page<TaskVO> pagesTask(TaskQuery query) {
        return null;
    }

    /**
     * add
     *
     * @param taskAo request object
     * @return response result
     */
    @Override
    public TaskVO addTask(TaskAO taskAo) {
        TaskPO taskPo = taskAo.convertToPo();
        taskRepository.insert(taskPo);
        return new TaskVO(taskPo);
    }

    /**
     * update
     *
     * @param taskAo request object
     * @return response result
     */
    @Override
    public TaskVO editTask(TaskAO taskAo) {
        TaskPO taskPo = taskAo.convertToPo();
        taskRepository.update(taskPo);
        return new TaskVO(taskPo);
    }

    /**
     * delete
     *
     * @param taskId task primary key
     */
    @Override
    public void deleteTask(String taskId) {
        taskRepository.deleteById(taskId);
    }

    /**
     * execute
     *
     * @param taskId task primary key
     */
    @Override
    public void executeTask(String taskId) {
        TaskPO taskPo = taskRepository.selectById(taskId);
        if (taskPo == null) {
            throw new BusinessException("任务不存在");
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
            throw new BusinessException("启动 " + taskPo.getName() + " 任务失败，原因：找不到" + taskPo.getClassName() + "类");
        } catch (NoSuchMethodException e) {
            log.error("启动 {} 任务失败，原因：找不到 {} 方法，异常信息：{}", taskPo.getName(), taskPo.getMethodName(), e.getMessage());
            throw  new BusinessException("启动 " + taskPo.getName() + " 任务失败，原因：找不到" + taskPo.getMethodName() + "方法");
        }

        AbstractTask task = new BusinessTask(taskPo.getName(), taskPo.getCronExpression(), object, method);
        cronTaskRegistrar.executeCronTask(task);

    }

    /**
     * start
     *
     * @param taskId task primary key
     */
    @Override
    public void startTask(String taskId) {
        TaskPO taskPo = taskRepository.selectById(taskId);
        if (taskPo == null) {
            throw new BusinessException("任务不存在");
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
            throw new BusinessException("启动 " + taskPo.getName() + " 任务失败，原因：找不到" + taskPo.getClassName() + "类");
        } catch (NoSuchMethodException e) {
            log.error("启动 {} 任务失败，原因：找不到 {} 方法，异常信息：{}", taskPo.getName(), taskPo.getMethodName(), e.getMessage());
            throw new BusinessException("启动 " + taskPo.getName() + " 任务失败，原因：找不到" + taskPo.getMethodName() + "方法");
        }

        AbstractTask task = new BusinessTask(taskPo.getName(), taskPo.getCronExpression(), object, method);
        cronTaskRegistrar.addCronTask(taskPo.getId(), task, task.getCornExpression());
    }

    /**
     * stop
     *
     * @param taskId task primary key
     */
    @Override
    public void stopTask(String taskId) {
        TaskPO taskPo = taskRepository.selectById(taskId);
        if (taskPo == null) {
            throw new BusinessException("任务不存在");
        }
        cronTaskRegistrar.removeCronTask(taskId);
    }

}
