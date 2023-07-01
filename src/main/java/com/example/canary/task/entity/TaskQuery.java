package com.example.canary.task.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 任务
 *
 * @ClassName TaskQuery
 * @Description 任务
 * @Author zhaohongliang
 * @Date 2023-07-01 15:27
 * @Since 1.0
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
