package com.example.canary.task.schedule;

import com.example.canary.util.DateUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.SimpleTriggerContext;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;

/**
 * 线程任务抽象类
 *
 * @since 1.0
 * @author zhaohongliang
 */
@Slf4j
@Data
public abstract class AbstractTask implements Runnable {

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 表达式
     */
    private String cornExpression;

    /**
     * 无参构造
     */
    protected AbstractTask() {
    }

    /**
     * 全参构造
     *
     * @param taskName
     * @param cornExpression
     */
    protected AbstractTask(String taskName, String cornExpression) {
        this.taskName = taskName;
        this.cornExpression = cornExpression;
    }

    /**
     * run method
     */
    @Override
    public void run() {
        LocalDateTime startTime = LocalDateTime.now();
        execute();
        LocalDateTime endTime = LocalDateTime.now();
        Duration duration = Duration.between(startTime, endTime);
        Instant instant = new CronTrigger(this.cornExpression).nextExecution(new SimpleTriggerContext());
        log.info("{} 执行完毕，开始时间：{}，结束时间：{}，执行耗时：{} ms，下次执行时间：{}", taskName, startTime, endTime, duration.toMillis(), DateUtils.toLocalDateTime(instant));
    }

    /**
     * execute method
     */
    public abstract void execute();
}
