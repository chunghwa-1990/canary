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
@ConfigurationProperties(prefix = "task", ignoreUnknownFields = false)
public class TaskProperties {

    private TaskProperties() {}

    /**
     * 是否自动执行, 默认: true
     */
    private Boolean autoExecute = true;

    /**
     * 监测任务
     */
    private final Monitor monitor = new Monitor();

    /**
     * 监测任务
     */
    @Setter
    @Getter
    public static class Monitor {

        /**
         * 是否开启监测任务
         */
        private Boolean enabled = true;

        /**
         * 监测任务 cron, 默认: 每1小时执行一次
         */
        private String cron = "0 0 /1 * * ? ";
    }
}
