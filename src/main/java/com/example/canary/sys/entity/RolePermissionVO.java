package com.example.canary.sys.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 角色权限关联关系
 *
 * @author zhaohongliang 2023-09-21 00:16
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RolePermissionVO extends RolePermissionBase {

    @Serial
    private static final long serialVersionUID = 8232725740599676971L;

}
