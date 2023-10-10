package com.example.canary.sys.entity;

import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;

/**
 * 权限
 *
 * @author zhaohongliang 2023-10-10 11:15
 * @since 1.0
 */
@Data
public class PermissionDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 8674393638474865342L;

    /**
     * id
     */
    private String id;

    /**
     * name
     */
    private String name;

    /**
     * 图标
     */
    private String icon;

    /**
     * 路由
     */
    private String route;

    /**
     * 菜单ID
     */
    private String menuId;

    public PermissionDTO() {
    }

    public PermissionDTO(PermissionVO permissionVo) {
        BeanUtils.copyProperties(permissionVo, this);
    }

    public PermissionDTO(PermissionPO permissionPo) {
        BeanUtils.copyProperties(permissionPo, this);
    }

    public PermissionDTO(PermissionDTO permissionDTO) {
        BeanUtils.copyProperties(permissionDTO, this);
    }
}
