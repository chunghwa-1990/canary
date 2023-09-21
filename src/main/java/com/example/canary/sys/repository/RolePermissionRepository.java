package com.example.canary.sys.repository;

import com.example.canary.sys.entity.RolePermissionPO;

import java.util.List;
import java.util.Set;

/**
 * 角色权限关联关系
 *
 * @author zhaohongliang 2023-09-21 10:28
 * @since 1.0
 */
public interface RolePermissionRepository {

    /**
     * insert
     *
     * @param rolePermissionPo
     * @return
     */
    int insert(RolePermissionPO rolePermissionPo);

    /**
     * batch insert
     *
     * @param list
     * @return
     */
    int batchInsert(List<RolePermissionPO> list);

    /**
     * delete by roleId
     *
     * @param roleId
     * @return
     */
    int deleteByRoleId(String roleId);
}
