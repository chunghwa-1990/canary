package com.example.canary.sys.repository;

import com.example.canary.sys.entity.PermissionAO;
import com.example.canary.sys.entity.PermissionPO;

/**
 * 权限
 *
 * @author zhaohongliang 2023-09-13 18:59
 * @since 1.0
 */
public interface PermissionRepository {

    /**
     * insert
     *
     * @param permissionPo
     * @return
     */
    int insert(PermissionPO permissionPo);

    /**
     * update
     *
     * @param permissionPo
     * @return
     */
    int update(PermissionPO permissionPo);

    /**
     * delete
     *
     * @param permissionId
     * @return
     */
    int deleteById(String permissionId);
}
