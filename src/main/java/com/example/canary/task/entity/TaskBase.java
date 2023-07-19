package com.example.canary.task.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.example.canary.common.exception.ValidGroup;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import java.io.Serial;
import java.io.Serializable;

/**
 * 任务
 *
 * @since 1.0
 * @author zhaohongliang
 */
@Data
public class TaskBase implements Serializable {

    @Serial
    private static final long serialVersionUID = -5765905120230979856L;

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @NotBlank(groups = { ValidGroup.Edit.class })
    private String id;

    /**
     * 名称
     */
    @NotBlank(groups = { ValidGroup.Add.class, ValidGroup.Edit.class })
    private String name;

    /**
     * 描述
     */
    @Length(max = 500, groups = { ValidGroup.Add.class, ValidGroup.Edit.class })
    private String description;

    /**
     * 表达式
     */
    @NotBlank(groups = { ValidGroup.Add.class, ValidGroup.Edit.class })
    private String cronExpression;

    /**
     * 类名
     */
    @NotBlank(groups = { ValidGroup.Add.class, ValidGroup.Edit.class })
    private String className;

    /**
     * 方法名
     */
    @NotBlank(groups = { ValidGroup.Add.class, ValidGroup.Edit.class })
    private String methodName;

    /**
     * 方法参数
     */
    private String methodParams;

    /**
     * 是否禁用
     * @see com.example.canary.common.enums.StatusEnum.Disabled
     */
    @Range(min = 0, max = 1, groups = { ValidGroup.Add.class, ValidGroup.Edit.class })
    @TableField(value = "is_disabled")
    private Integer disabled;

}
