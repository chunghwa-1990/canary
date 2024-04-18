package com.example.canary.task.core;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * task properties
 *
 * @author zhaohongliang 2023-07-12 18:11
 * @since 1.0
 */
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "task.scheduling", ignoreUnknownFields = false)
public class TaskSchedulerProperties {

    private TaskSchedulerProperties() {}

    /**
     * 线程池
     */
    private final Pool pool = new Pool();

    /**
     * 监测任务
     */
    private final Monitor monitor = new Monitor();

    private final Shutdown shutdown = new Shutdown();

    /**
     * 是否自动执行, 默认: false
     */
    private Boolean enabled = false;

    /**
     * 线程池名称前缀
     */
    private String threadNamePrefix = "TaskExecutor-";

    @Setter
    @Getter
    public static class Pool {

        /**
         * Maximum allowed number of threads.
         */
        private int size = 1;

    }

    @Setter
    @Getter
    public static class Shutdown {

        /**
         * Whether the executor should wait for scheduled tasks to complete on shutdown.
         */
        private boolean awaitTermination;

        /**
         * Maximum time the executor should wait for remaining tasks to complete.
         */
        private Duration awaitTerminationPeriod;

    }

    /**
     * 监测任务
     */
    @Setter
    @Getter
    public static class Monitor {

        /**
         * 是否开启监测任务
         */
        private Boolean enabled = false;

        /**
         * 监测任务 cron, 默认: 每1小时执行一次
         */
        private String cron = "0 0 /1 * * ? ";
    }
}
