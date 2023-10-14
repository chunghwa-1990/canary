package com.example.canary.task.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.canary.common.api.ApiVersion;
import com.example.canary.common.exception.ResultEntity;
import com.example.canary.common.exception.ValidGroup;
import com.example.canary.task.entity.TaskAO;
import com.example.canary.task.entity.TaskQuery;
import com.example.canary.task.entity.TaskVO;
import com.example.canary.task.service.TaskService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 任务
 *
 * @since 1.0
 * @author zhaohongliang
 */
@ApiVersion
@Validated
@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    /**
     * pages
     *
     * @param query request query parameters
     * @return response page result
     */
    @GetMapping("/pages")
    public IPage<TaskVO> pagesTask(TaskQuery query) {
        return taskService.pagesTask(query);
    }

    /**
     * add
     *
     * @param taskAo request object
     * @return response result
     */
    @PostMapping("/add")
    public TaskVO addTask(@Validated({ ValidGroup.Add.class }) @RequestBody TaskAO taskAo) {
        return taskService.addTask(taskAo);
    }

    /**
     * edit
     *
     * @param taskAo request object
     * @return response result
     */
    @PutMapping("/edit")
    public TaskVO editTask(@Validated({ ValidGroup.Edit.class }) @RequestBody TaskAO taskAo) {
        return taskService.editTask(taskAo);
    }

    /**
     * delete
     *
     * @param taskId task primary key
     */
    @DeleteMapping("/delete")
    public void deleteTask(@NotBlank String taskId) {
        taskService.deleteTask(taskId);
    }

    /**
     * execute
     *
     * @param taskId task primary key
     */
    @GetMapping("/execute")
    public void executeTask(@NotBlank String taskId) {
        taskService.executeTask(taskId);
    }

    /**
     * start
     *
     * @param taskId task primary key
     */
    @GetMapping("/start")
    public void startTask(@NotBlank String taskId) {
        taskService.startTask(taskId);
    }

    /**
     * stop
     *
     * @param taskId task primary key
     */
    @GetMapping("/stop")
    public void stopTask(@NotBlank String taskId) {
        taskService.stopTask(taskId);
    }

}
