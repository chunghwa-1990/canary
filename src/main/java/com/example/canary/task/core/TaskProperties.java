package com.example.canary.task.core;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * task properties
 *
 * @author zhaohongliang 2023-07-12 18:11
 * @since 1.0
 */
@Setter
@Getter
@ConfigurationProperties("spring.task")
public class TaskProperties {

    private TaskProperties() {}

    /**
     * 是否自动执行
     */
    private Boolean autoExecute;

    /**
     * 监测任务 cron
     */
    private String monitorCron;
}
