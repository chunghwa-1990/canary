package com.example.canary.task.schedule;

import com.example.canary.core.context.SpringContext;
import com.example.canary.task.entity.TaskPO;
import com.example.canary.task.repository.TaskRepository;
import com.example.canary.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * 动态处理定时任务
 *
 * @ClassName CronTaskRegistrar
 * @Description 动态处理定时任务
 * @Author zhaohongliang
 * @Date 2023-06-29 16:29
 * @Since 1.0
 */
@Slf4j
@Component
public class CronTaskRegistrar implements InitializingBean {

    /**
     * system task
     */
    private final Map<String, ScheduledFuture<?>> scheduledFutureMap = new ConcurrentHashMap<>(16);

    /**
     * business task
     */
    private final Map<String, ScheduledTaskHolder> scheduledTaskHolderMap = new ConcurrentHashMap<>(16);

    public Map<String, ScheduledTaskHolder> getScheduledTaskHolderMap() {
        return scheduledTaskHolderMap;
    }

    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;

    @Autowired
    private TaskRepository taskRepository;

    @Lazy
    @Autowired
    private MonitorTask monitorTask;


    /**
     * execute task
     *
     * @param task
     */
    public void executeCronTask(ITask task) {
        task.run();
    }

    /**
     * add task
     *
     * @param taskId
     * @param task
     * @param cronExpression
     */
    public void addCronTask(String taskId, ITask task, String cronExpression) {
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

    /**
     * register task
     */
    private void registerCronTask() {

        // clear
        scheduledFutureMap.clear();

        // moniter task
        ScheduledFuture<?> scheduledFuture = taskScheduler.schedule(monitorTask, new CronTrigger(monitorTask.getCornExpression()));
        scheduledFutureMap.put("0", scheduledFuture);

        // dynamic task
        List<TaskPO> tasks = taskRepository.listEnableTask();
        if (CollectionUtils.isEmpty(tasks)) {
            return;
        }

        tasks.forEach(taskPo -> {
            try {
                Class<?> clazz = Class.forName(taskPo.getClassName());
                String beanName = StringUtils.toLowerCamelCase(clazz.getSimpleName());
                Object object = SpringContext.getBean(beanName);
                Method method = clazz.getMethod(taskPo.getMethodName());
                ITask task = new BusinessTask(taskPo.getName(), taskPo.getCronExpression(), object, method);
                this.addCronTask(taskPo.getId(), task, task.getCornExpression());
            } catch (ClassNotFoundException e) {
                log.error("启动 {} 任务失败，原因：找不到 {} 类，异常信息：{}", taskPo.getName(),  taskPo.getClassName(), e.getMessage());
            } catch (NoSuchMethodException e) {
                log.error("启动 {} 任务失败，原因：找不到 {} 方法，异常信息：{}", taskPo.getName(), taskPo.getMethodName(), e.getMessage());
            }

        });
    }

    /**
     * 初始化完成bean执行
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() {
        log.info("任务调度器开始执行...");
        this.registerCronTask();
        if (!CollectionUtils.isEmpty(scheduledTaskHolderMap)) {
            scheduledTaskHolderMap.forEach((k, v) ->
                    log.info("register taskId {}, taskName {} complete, cron expression {}",
                            k, v.getTask().getTaskName(), v.getTask().getCornExpression()));
        }
    }
}
