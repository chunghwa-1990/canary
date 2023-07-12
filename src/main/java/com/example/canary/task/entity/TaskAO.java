package com.example.canary.task.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import java.io.Serial;

/**
 * 任务
 *
 * @since 1.0
 * @author zhaohongliang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TaskAO extends TaskBase {

    @Serial
    private static final long serialVersionUID = 8270693936284012737L;

    public TaskPO convertToPo() {
        TaskPO taskPo = new TaskPO();
        BeanUtils.copyProperties(this, taskPo);
        return taskPo;
    }
}
