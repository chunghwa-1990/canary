package com.example.canary.task.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 任务
 *
 * @since 1.0
 * @author zhaohongliang
 */
@Data
public class TaskQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = -2895721913560813382L;

    /**
     * keywords
     */
    private String keywords;
}
