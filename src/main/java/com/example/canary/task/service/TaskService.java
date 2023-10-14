package com.example.canary.task.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.canary.task.entity.TaskAO;
import com.example.canary.task.entity.TaskQuery;
import com.example.canary.task.entity.TaskVO;

/**
 * 任务
 *
 * @since 1.0
 * @author zhaohongliang
 */
public interface TaskService {

    /**
     * pages
     *
     * @param query request query parameters
     * @return response page result
     */
    IPage<TaskVO> pagesTask(TaskQuery query);

    /**
     * add
     *
     * @param taskAo request object
     * @return response result
     */
    TaskVO addTask(TaskAO taskAo);

    /**
     * update
     *
     * @param taskAo request object
     * @return response result
     */
    TaskVO editTask(TaskAO taskAo);

    /**
     * delete
     *
     * @param taskId task primary key
     */
    void deleteTask(String taskId);

    /**
     * execute
     *
     * @param taskId task primary key
     */
    void executeTask(String taskId);

    /**
     * start
     *
     * @param taskId task primary key
     */
    void startTask(String taskId);

    /**
     * stop
     *
     * @param taskId task primary key
     * @return response result
     */
    void stopTask(String taskId);
}
