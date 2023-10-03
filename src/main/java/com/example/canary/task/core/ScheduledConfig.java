package com.example.canary.task.core;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.task.TaskSchedulingProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * 定时任务线程池
 * 实现 SchedulingConfigurer 接口，必须以@Bean创建ThreadPoolTaskScheduler，不可以使用配置文件自动装配
 * spring:
 *   task:
 *     scheduling:
 *       # 线程池名称前缀
 *       thread-name-prefix: TaskExecutor-
 *       pool:
 *         # 线程池大小,默认为 1
 *         size: 20
 *       shutdown:
 *         # 应用关闭时，是否等待任务执行完成，默认为 false
 *         await-termination: true
 *         # 等待任务完成的最大时长，默认为 0, 单位为秒
 *         await-termination-period: 60s
 *
 * @since 1.0
 * @author zhaohongliang
 */
@Slf4j
@EnableAsync
@Configuration
@EnableScheduling
@EnableConfigurationProperties({TaskProperties.class, TaskSchedulingProperties.class})
public class ScheduledConfig implements SchedulingConfigurer {

    @Resource
    private TaskSchedulingProperties taskSchedulingProperties;

    /**
     * 定时任务线程池
     *
     * @return
     */
    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(taskSchedulingProperties.getPool().getSize());
        taskScheduler.setThreadNamePrefix(taskSchedulingProperties.getThreadNamePrefix());
        taskScheduler.setWaitForTasksToCompleteOnShutdown(taskSchedulingProperties.getShutdown().isAwaitTermination());
        taskScheduler.setAwaitTerminationSeconds((int) taskSchedulingProperties.getShutdown().getAwaitTerminationPeriod().toSeconds());
        return taskScheduler;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setTaskScheduler(taskScheduler());
    }

}
