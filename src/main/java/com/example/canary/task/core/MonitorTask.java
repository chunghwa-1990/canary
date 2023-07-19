package com.example.canary.task.core;

import com.example.canary.task.entity.TaskPO;
import com.example.canary.task.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 监测任务
 *
 * @since 1.0
 * @author zhaohongliang
 */
@Slf4j
@Component
@ConditionalOnProperty(value = "spring.task.auto-execute")
public class MonitorTask {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private CronTaskRegistrar cronTaskRegistrar;

    @Async
    @Scheduled(cron = "${spring.task.monitor-cron}")
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
