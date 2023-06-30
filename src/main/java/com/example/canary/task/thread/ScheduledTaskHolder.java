package com.example.canary.task.thread;

import lombok.Data;

import java.util.concurrent.ScheduledFuture;

/**
 * 定时任务包装类
 *
 * @ClassName ScheduledTaskHolder
 * @Description 定时任务包装类
 * @Author zhaohongliang
 * @Date 2023-06-30 15:58
 * @Since 1.0
 */
@Data
public class ScheduledTaskHolder {

    /**
     * task
     */
    private ITask task;

    private ScheduledFuture<?> scheduledFuture;

    public ScheduledTaskHolder() {
    }

    public ScheduledTaskHolder(ITask task, ScheduledFuture<?> scheduledFuture) {
        this.task = task;
        this.scheduledFuture = scheduledFuture;
    }

    public boolean isCancelled() {
        return scheduledFuture.isCancelled();
    }
}
