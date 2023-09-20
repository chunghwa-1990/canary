package com.example.canary.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 菜单权限关联关系
 *
 * @author zhaohongliang 2023-09-20 21:40
 * @since 1.0
 */
@Data
public class MenuPermissionBase implements Serializable {

    @Serial
    private static final long serialVersionUID = -7322244199416314691L;

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 菜单ID
     */
    private String menuId;

    /**
     * 权限ID
     */
    private String permissionId;

    public MenuPermissionBase() {
    }

    public MenuPermissionBase(String menuId, String permissionId) {
        this.menuId = menuId;
        this.permissionId = permissionId;
    }
}
