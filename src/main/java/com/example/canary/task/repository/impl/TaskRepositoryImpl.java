package com.example.canary.task.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.canary.common.enums.DisabledStatusEnum;
import com.example.canary.task.mapper.TaskMapper;
import com.example.canary.task.entity.TaskPO;
import com.example.canary.task.repository.TaskRepository;
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
        queryWrapper.eq(TaskPO::getDeleted, DisabledStatusEnum.TRUE.getCode());
        return taskMapper.selectList(queryWrapper);
    }
}
