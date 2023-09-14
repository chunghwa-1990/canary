package com.example.canary.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.example.canary.common.exception.ValidGroup;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.io.Serializable;

/**
 * 权限
 *
 * @author zhaohongliang 2023-09-13 19:00
 * @since 1.0
 */
@Data
public class PermissionBase implements Serializable {

    @Serial
    private static final long serialVersionUID = 5669917258682365283L;

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @NotBlank(groups = ValidGroup.Edit.class)
    private String id;

    /**
     * name
     */
    @NotBlank(groups = { ValidGroup.Add.class, ValidGroup.Edit.class })
    @Length(min = 1, max = 20, groups = { ValidGroup.Add.class, ValidGroup.Edit.class })
    private String name;



}
