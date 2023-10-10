package com.example.canary.sys.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 权限
 *
 * @author zhaohongliang 2023-09-13 19:03
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PermissionVO extends PermissionBase {

    @Serial
    private static final long serialVersionUID = 8553002464871711739L;

    /**
     * 菜单ID
     */
    private String menuId;


}
