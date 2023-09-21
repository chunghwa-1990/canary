package com.example.canary.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.example.canary.common.exception.ValidGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import java.io.Serial;
import java.io.Serializable;

/**
 * 菜单
 *
 * @author zhaohongliang 2023-09-08 20:37
 * @since 1.0
 */
@Data
public class MenuBase implements Serializable {

    @Serial
    private static final long serialVersionUID = 3277512367742736079L;

    /**
     * id
     */
    @NotBlank(groups = ValidGroup.Edit.class)
    private String id;

    /**
     * 父菜单id
     */
    private String parentId;

    /**
     * 名称
     */
    @NotBlank(groups = { ValidGroup.Add.class, ValidGroup.Edit.class })
    @Length(min = 1, max = 20, groups = { ValidGroup.Add.class, ValidGroup.Edit.class })
    private String name;

    /**
     * 图标
     */
    private String icon;

    /**
     * 路由
     */
    @NotBlank(groups = { ValidGroup.Add.class, ValidGroup.Edit.class })
    @Length(min = 1, max = 100, groups = { ValidGroup. Add.class, ValidGroup.Edit.class })
    private String route;

    /**
     * 菜单级别 [0,1]
     */
    @Range(min = 1, max = 2)
    private Integer level;

    /**
     * 是否禁用
     * @see com.example.canary.common.enums.StatusEnum.Disabled
     */
    @Range(min = 0, max = 1, groups = { ValidGroup.Add.class, ValidGroup.Edit.class })
    @TableField(value = "is_disabled")
    private Integer disabled;




}
