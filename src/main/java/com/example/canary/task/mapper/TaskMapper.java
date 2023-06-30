package com.example.canary.task.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.canary.task.entity.TaskPO;
import org.springframework.stereotype.Repository;

/**
 * 任务
 *
 * @ClassName TaskMapper
 * @Description 任务
 * @Author zhaohongliang
 * @Date 2023-06-29 16:50
 * @Since 1.0
 */
@Repository
public interface TaskMapper extends BaseMapper<TaskPO> {

}
