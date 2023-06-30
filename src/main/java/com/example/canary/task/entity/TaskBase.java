package com.example.canary.task.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 任务
 *
 * @ClassName TaskBase
 * @Description 任务
 * @Author zhaohongliang
 * @Date 2023-06-26 16:16
 * @Since 1.0
 */
@Data
public class TaskBase implements Serializable {

    @Serial
    private static final long serialVersionUID = -5765905120230979856L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 名称（英文）
     */
    private String nameEn;

    /**
     * 描述
     */
    private String description;

    /**
     * 表达式
     */
    private String cronExpression;

    /**
     * 类名
     */
    private String className;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 方法参数
     */
    private String methodParams;

    /**
     * 是否禁用
     * @see com.example.canary.common.enums.DisabledStatusEnum
     */
    @TableField(value = "is_disabled")
    private Integer disabled;

}
