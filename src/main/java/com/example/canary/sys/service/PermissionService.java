package com.example.canary.sys.service;

import com.example.canary.common.exception.ResultEntity;
import com.example.canary.sys.entity.PermissionAO;
import com.example.canary.sys.entity.PermissionQuery;
import com.example.canary.sys.entity.PermissionVO;

import java.util.List;

/**
 * 权限
 *
 * @author zhaohongliang 2023-09-13 18:57
 * @since 1.0
 */
public interface PermissionService {

    /**
     * list
     *
     * @return
     */
    ResultEntity<List<PermissionVO>> listPermission();

    /**
     * add
     *
     * @param permissionAo
     * @return
     */
    @SuppressWarnings("rawtypes")
    ResultEntity addPermission(PermissionAO permissionAo);

    /**
     * update
     *
     * @param permissionAo
     * @return
     */
    @SuppressWarnings("rawtypes")
    ResultEntity editPermission(PermissionAO permissionAo);

    /**
     * delete
     *
     * @param id
     * @return
     */
    @SuppressWarnings("rawtypes")
    ResultEntity deletePermission(String id);
}
