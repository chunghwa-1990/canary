package com.example.canary.task.repository;

import com.example.canary.task.entity.TaskPO;

import java.util.List;

/**
 * 任务
 *
 * @ClassName TaskRepository
 * @Description 任务
 * @Author zhaohongliang
 * @Date 2023-06-29 16:33
 * @Since 1.0
 */
public interface TaskRepository {

    /**
     * 启用的任务
     *
     * @return
     */
    List<TaskPO> listEnableTask();
}
