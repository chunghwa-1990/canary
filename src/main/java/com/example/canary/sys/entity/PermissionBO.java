package com.example.canary.sys.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import java.io.Serial;

/**
 * 权限
 *
 * @author zhaohongliang 2023-10-10 11:15
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PermissionBO extends PermissionBase {

    @Serial
    private static final long serialVersionUID = 8674393638474865342L;

    /**
     * 菜单ID
     */
    private String menuId;

    public PermissionBO() {
    }

    public PermissionBO(PermissionVO permissionVo) {
        BeanUtils.copyProperties(permissionVo, this);
    }
}
