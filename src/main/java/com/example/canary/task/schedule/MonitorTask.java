package com.example.canary.task.schedule;

import com.example.canary.task.entity.TaskPO;
import com.example.canary.task.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

/**
 * 监测任务
 *
 * @ClassName MonitorTask
 * @Description 监测
 * @Author zhaohongliang
 * @Date 2023-06-30 17:51
 * @Since 1.0
 */
@Slf4j
@Component
public class MonitorTask {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private CronTaskRegistrar cronTaskRegistrar;

    @Autowired
    private ThreadPoolTaskScheduler scheduler;

    @Async
    @Scheduled(cron = "0 * * * * ?")
    protected void execute() {
        List<TaskPO> tasks = taskRepository.listEnableTask();
        Set<String> taskIds = tasks.stream().map(TaskPO::getId).collect(Collectors.toSet());
        Map<String, ScheduledTaskHolder> scheduledTaskHolderMap = cronTaskRegistrar.getScheduledTaskHolderMap();
        if (CollectionUtils.isEmpty(scheduledTaskHolderMap)) {
            tasks.forEach(task -> log.warn("taskId {}, taskName {} has stopped", task.getId(), task.getName()));
        } else {
            scheduledTaskHolderMap.forEach((k, v) -> {
                log.info("register taskId {}, taskName {} is running, corn expression {} ", k, v.getTask().getTaskName(), v.getTask().getCornExpression());
                if (!taskIds.contains(k)) {
                    log.warn("taskId {}, taskName {} has stopped", k, v.getTask().getTaskName());
                }
            });
        }
    }
}
