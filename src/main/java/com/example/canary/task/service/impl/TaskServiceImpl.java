package com.example.canary.task.service.impl;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.canary.common.entity.ResultEntity;
import com.example.canary.task.entity.TaskAO;
import com.example.canary.task.entity.TaskPO;
import com.example.canary.task.entity.TaskQuery;
import com.example.canary.task.entity.TaskVO;
import com.example.canary.task.repository.TaskRepository;
import com.example.canary.task.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

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

}
