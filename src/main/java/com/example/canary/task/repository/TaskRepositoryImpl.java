package com.example.canary.task.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.canary.core.enums.BaseEnum;
import com.example.canary.core.enums.StatusEnum;
import com.example.canary.task.entity.TaskPO;
import com.example.canary.task.mapper.TaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 任务
 *
 * @ClassName TaskRepositoryImpl
 * @Description 任务
 * @Author zhaohongliang
 * @Date 2023-06-29 16:34
 * @Since 1.0
 */
@Service
public class TaskRepositoryImpl implements TaskRepository {

    @Autowired
    private TaskMapper taskMapper;

    /**
     * 启用的任务
     *
     * @return
     */
    @Override
    public List<TaskPO> listEnableTask() {
        LambdaQueryWrapper<TaskPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TaskPO::getDisabled, StatusEnum.Disabled.FALSE.getCode());
        queryWrapper.eq(TaskPO::getDeleted, StatusEnum.Deleted.FALSE.getCode());
        return taskMapper.selectList(queryWrapper);
    }

    /**
     * insert
     *
     * @param taskPo
     * @return
     */
    @Override
    public int insert(TaskPO taskPo) {
        return taskMapper.insert(taskPo);
    }

    /**
     * update
     *
     * @param taskPo
     * @return
     */
    @Override
    public int update(TaskPO taskPo) {
        return taskMapper.updateById(taskPo);
    }

    /**
     * delete
     *
     * @param taskId
     * @return
     */
    @Override
    public int deleteById(String taskId) {
        return taskMapper.deleteById(taskId);
    }

    /**
     * select by primary key
     *
     * @param taskId task primary key
     * @return
     */
    @Override
    public TaskPO selectById(String taskId) {
        return taskMapper.selectById(taskId);
    }
}
