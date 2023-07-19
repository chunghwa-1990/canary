package com.example.canary.task.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * 动态处理定时任务
 *
 * @since 1.0
 * @author zhaohongliang
 */
@Slf4j
@Component
public class CronTaskRegistrar {

    /**
     * business task
     */
    private final Map<String, ScheduledTaskHolder> scheduledTaskHolderMap = new ConcurrentHashMap<>(16);

    public Map<String, ScheduledTaskHolder> getScheduledTaskHolderMap() {
        return scheduledTaskHolderMap;
    }

    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;

    /**
     * execute task
     *
     * @param task
     */
    public void executeCronTask(AbstractTask task) {
        task.run();
    }

    /**
     * add task
     *
     * @param taskId
     * @param task
     * @param cronExpression
     */
    public void addCronTask(String taskId, AbstractTask task, String cronExpression) {
        ScheduledFuture<?> scheduledFuture = taskScheduler.schedule(task, new CronTrigger(cronExpression));
        ScheduledTaskHolder holder = new ScheduledTaskHolder(task, scheduledFuture);
        scheduledTaskHolderMap.put(taskId, holder);
    }

    /**
     * remove task
     *
     * @param taskId
     */
    public void removeCronTask(String taskId) {
        ScheduledTaskHolder holder = scheduledTaskHolderMap.remove(taskId);
        if (holder != null) {
            if (holder.isCancelled()) {
                return;
            }
            ScheduledFuture<?> scheduledFuture = holder.getScheduledFuture();
            scheduledFuture.cancel(true);
        }
    }

}
