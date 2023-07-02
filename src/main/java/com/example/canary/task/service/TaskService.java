package com.example.canary.task.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.canary.common.entity.ResultEntity;
import com.example.canary.task.entity.TaskAO;
import com.example.canary.task.entity.TaskQuery;
import com.example.canary.task.entity.TaskVO;

/**
 * 任务
 *
 * @ClassName TaskService
 * @Description 任务
 * @Author zhaohongliang
 * @Date 2023-06-29 10:19
 * @Since 1.0
 */
public interface TaskService {

    /**
     * pages
     *
     * @param query request query parameters
     * @return response page result
     */
    ResultEntity<Page<TaskVO>> pagesTask(TaskQuery query);

    /**
     * save
     *
     * @param taskAo request object
     * @return response result
     */
    @SuppressWarnings("rawtypes")
    ResultEntity saveTask(TaskAO taskAo);

    /**
     * update
     *
     * @param taskAo request object
     * @return response result
     */
    @SuppressWarnings("rawtypes")
    ResultEntity updateTask(TaskAO taskAo);

    /**
     * delete
     *
     * @param taskId task primary key
     * @return response result
     */
    @SuppressWarnings("rawtypes")
    ResultEntity deleteTask(String taskId);

    /**
     * execute
     *
     * @param taskId task primary key
     * @return response result
     */
    @SuppressWarnings("rawtypes")
    ResultEntity executeTask(String taskId);
}
