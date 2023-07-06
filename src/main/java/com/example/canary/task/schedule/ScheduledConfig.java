package com.example.canary.task.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * 定时任务线程池
 *
 * @ClassName ScheduledConfig
 * @Description 定时任务线程池
 * @Author zhaohongliang
 * @Date 2023-06-29 11:54
 * @Since 1.0
 */
@Slf4j
@EnableAsync
@Configuration
@EnableScheduling
public class ScheduledConfig implements SchedulingConfigurer {

    /**
     * 定时任务线程池
     *
     * @return
     */
    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        log.info("Creating ThreadPoolTaskScheduler ...");
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(20);
        taskScheduler.setThreadNamePrefix("TaskExecutor-");
        taskScheduler.setWaitForTasksToCompleteOnShutdown(true);
        taskScheduler.setAwaitTerminationSeconds(60);
        log.info("Successfully started");
        return taskScheduler;
    }

    /**
     * 监测线程
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public MonitorTask moniterTask() {
        MonitorTask monitorTask = new MonitorTask();
        monitorTask.setTaskName("Monitor Task");
        monitorTask.setCornExpression("0 * * * * ?");
        return monitorTask;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setTaskScheduler(taskScheduler());
    }


}
