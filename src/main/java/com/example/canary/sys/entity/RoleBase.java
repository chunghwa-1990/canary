package com.example.canary.sys.entity;

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
 * 角色
 *
 * @author zhaohongliang 2023-08-03 21:16
 * @since 1.0
 */
@Data
public class RoleBase implements Serializable {

    @Serial
    private static final long serialVersionUID = -6832820254048501758L;

    /**
     * id
     */
    @NotBlank(groups = { ValidGroup.Edit.class })
    private String id;

    /**
     * 名称
     */
    @NotBlank(groups = { ValidGroup.Add.class, ValidGroup.Edit.class })
    @Length(min = 1, max = 20, groups = { ValidGroup.Add.class, ValidGroup.Edit.class })
    private String name;

    /**
     * 描述
     */
    @Length(max = 500, groups = { ValidGroup.Add.class, ValidGroup.Edit.class })
    private String description;

    /**
     * 是否禁用
     * @see com.example.canary.common.enums.StatusEnum.Disabled
     */
    @Range(min = 0, max = 1, groups = { ValidGroup.Add.class, ValidGroup.Edit.class })
    @TableField(value = "is_disabled")
    private Integer disabled;
}
