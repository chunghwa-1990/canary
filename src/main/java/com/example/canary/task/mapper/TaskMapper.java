package com.example.canary.task.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.canary.task.entity.TaskPO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * 任务
 *
 * @since 1.0
 * @author zhaohongliang
 */
@Repository
public interface TaskMapper extends BaseMapper<TaskPO> {

    @Update("UPDATE t_task SET is_deleted = #{taskId} WHERE id = #{taskId}")
    int deleteById(@Param("taskId") String taskId);
}
