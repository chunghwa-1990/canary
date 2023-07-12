package com.example.canary.task.schedule;

import lombok.Data;

import java.util.concurrent.ScheduledFuture;

/**
 * 定时任务包装类
 *
 * @since 1.0
 * @author zhaohongliang
 */
@Data
public class ScheduledTaskHolder {

    /**
     * task
     */
    private AbstractTask task;

    private ScheduledFuture<?> scheduledFuture;

    public ScheduledTaskHolder() {
    }

    public ScheduledTaskHolder(AbstractTask task, ScheduledFuture<?> scheduledFuture) {
        this.task = task;
        this.scheduledFuture = scheduledFuture;
    }

    public boolean isCancelled() {
        return scheduledFuture.isCancelled();
    }
}
