package com.example.canary.task.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 任务
 *
 * @since 1.0
 * @author zhaohongliang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TaskVO extends TaskBase {

    @Serial
    private static final long serialVersionUID = -6423730300144704373L;
}
