package com.example.canary.task.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import java.io.Serial;

/**
 * 任务
 *
 * @ClassName TaskAO
 * @Description 任务
 * @Author zhaohongliang
 * @Date 2023-06-29 10:10
 * @Since 1.0
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
