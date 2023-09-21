package com.example.canary.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * 角色权限关联关系
 *
 * @author zhaohongliang 2023-09-21 00:11
 * @since 1.0
 */
@Data
public class RolePermissionBase implements Serializable {

    @Serial
    private static final long serialVersionUID = -793979626944722704L;

    /**
     * ID
     */
    private String id;

    /**
     * 角色ID
     */
    private String roleId;

    /**
     * 权限ID
     */
    private String permissionId;

    public RolePermissionBase() {
    }

    public RolePermissionBase(String roleId, String permissionId) {
        this.roleId = roleId;
        this.permissionId = permissionId;
    }
}
