package com.example.canary.task.repository;

import com.example.canary.task.entity.TaskPO;

import java.util.List;

/**
 * 任务
 *
 * @since 1.0
 * @author zhaohongliang
 */
public interface TaskRepository {

    /**
     * enable task list
     *
     * @return
     */
    List<TaskPO> listEnableTask();

    /**
     * insert
     *
     * @param taskPo
     * @return
     */
    int insert(TaskPO taskPo);

    /**
     * update
     *
     * @param taskPo
     * @return
     */
    int update(TaskPO taskPo);

    /**
     * delete
     *
     * @param taskId task primary key
     * @return response
     */
    int deleteById(String taskId);

    /**
     * select by primary key
     *
     * @param taskId task primary key
     * @return
     */
    TaskPO selectById(String taskId);
}
