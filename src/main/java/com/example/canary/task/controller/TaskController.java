package com.example.canary.task.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.canary.common.entity.ResultEntity;
import com.example.canary.common.entity.ValidGroup;
import com.example.canary.task.entity.TaskAO;
import com.example.canary.task.entity.TaskQuery;
import com.example.canary.task.entity.TaskVO;
import com.example.canary.task.service.TaskService;
import jakarta.validation.constraints.NotNull;
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
 * @ClassName TaskController
 * @Description 任务
 * @Author zhaohongliang
 * @Date 2023-06-29 10:18
 * @Since 1.0
 */
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
    public ResultEntity<Page<TaskVO>> pagesTask(TaskQuery query) {
        return taskService.pagesTask(query);
    }

    /**
     * save
     *
     * @param taskAo request object
     * @return response result
     */
    @PostMapping("/save")
    @SuppressWarnings("rawtypes")
    public ResultEntity saveTask(@Validated({ ValidGroup.Add.class }) @RequestBody TaskAO taskAo) {
        return taskService.saveTask(taskAo);
    }

    /**
     * update
     *
     * @param taskAo request object
     * @return response result
     */
    @PutMapping("/update")
    @SuppressWarnings("rawtypes")
    public ResultEntity updateTask(@Validated({ ValidGroup.Edit.class }) @RequestBody TaskAO taskAo) {
        return taskService.updateTask(taskAo);
    }

    /**
     * delete
     *
     * @param taskId task primary key
     * @return response result
     */
    @DeleteMapping("/delete")
    @SuppressWarnings("rawtypes")
    public ResultEntity deleteTask(@NotNull String taskId) {
        return taskService.deleteTask(taskId);
    }

}
