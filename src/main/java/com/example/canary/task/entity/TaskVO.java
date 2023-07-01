package com.example.canary.task.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 任务
 *
 * @ClassName TaskVO
 * @Description 任务
 * @Author zhaohongliang
 * @Date 2023-07-01 12:49
 * @Since 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TaskVO extends TaskBase {

    @Serial
    private static final long serialVersionUID = -6423730300144704373L;
}
