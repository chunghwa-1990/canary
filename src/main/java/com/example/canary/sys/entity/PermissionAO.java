package com.example.canary.sys.entity;

import com.example.canary.common.exception.ValidGroup;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import java.io.Serial;

/**
 * 权限
 *
 * @author zhaohongliang 2023-09-13 19:01
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PermissionAO extends PermissionBase {

    @Serial
    private static final long serialVersionUID = 5895257941977389229L;

    /**
     * 菜单id
     */
    @NotBlank(groups = { ValidGroup.Add.class, ValidGroup.Edit.class })
    private String menuId;


    public PermissionPO convertToPo() {
        PermissionPO permissionPo = new PermissionPO();
        BeanUtils.copyProperties(this, permissionPo);
        return permissionPo;
    }
}
