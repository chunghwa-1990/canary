package com.example.canary.task.core;

import com.example.canary.util.DateUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.SimpleTriggerContext;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 线程任务抽象类
 *
 * @ClassName ITask
 * @Description 线程任务抽象类
 * @Author zhaohongliang
 * @Date 2023-06-29 23:54
 * @Since 1.0
 */
@Slf4j
@Data
public abstract class ITask implements Runnable {

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 表达式
     */
    private String cornExpression;

    protected ITask() {
    }

    protected ITask(String taskName, String cornExpression) {
        this.taskName = taskName;
        this.cornExpression = cornExpression;
    }

    @Override
    public void run() {
        LocalDateTime startTime = LocalDateTime.now();
        execute();
        LocalDateTime endTime = LocalDateTime.now();
        Duration duration = Duration.between(startTime, endTime);
        Instant instant = new CronTrigger(this.cornExpression).nextExecution(new SimpleTriggerContext());
        log.info("【{}】执行完毕，开始时间：{}，结束时间：{}，执行耗时：{} ms，下次执行时间：{}", taskName, startTime, endTime, duration.toMillis(), DateUtils.toLocalDateTime(instant));
    }

    public abstract void execute();
}
