package com.example.canary.task.schedule;

import com.example.canary.core.context.SpringContext;
import com.example.canary.task.entity.TaskPO;
import com.example.canary.task.repository.TaskRepository;
import com.example.canary.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 启动执行任务
 *
 * @author zhaohongliang 2023-07-12 15:27
 * @since 1.0
 */
@Slf4j
@Component
@ConditionalOnProperty(value = "spring.task.auto-execute")
public class ScheduledTaskRunner implements ApplicationRunner {

    @Autowired
    private CronTaskRegistrar cronTaskRegistrar;

    @Autowired
    private TaskRepository taskRepository;

    /**
     * register task
     */
    private void registerCronTask() {

        // clear
        cronTaskRegistrar.getScheduledTaskHolderMap().clear();

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
                AbstractTask task = new BusinessTask(taskPo.getName(), taskPo.getCronExpression(), object, method);
                cronTaskRegistrar.addCronTask(taskPo.getId(), task, task.getCornExpression());
            } catch (ClassNotFoundException e) {
                log.error("启动 {} 任务失败，原因：找不到 {} 类，异常信息：{}", taskPo.getName(), taskPo.getClassName(), e.getMessage());
            } catch (NoSuchMethodException e) {
                log.error("启动 {} 任务失败，原因：找不到 {} 方法，异常信息：{}", taskPo.getName(), taskPo.getMethodName(), e.getMessage());
            }

        });
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("任务调度器开始执行...");
        this.registerCronTask();
        if (!CollectionUtils.isEmpty(cronTaskRegistrar.getScheduledTaskHolderMap())) {
            cronTaskRegistrar.getScheduledTaskHolderMap().forEach((k, v) ->
                    log.info("register taskId {}, taskName {} complete, cron expression {}",
                            k, v.getTask().getTaskName(), v.getTask().getCornExpression()));
        }
    }
}
