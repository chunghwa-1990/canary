package com.example.canary.sys.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.canary.common.exception.ResultEntity;
import com.example.canary.sys.entity.PermissionAO;
import com.example.canary.sys.entity.PermissionQuery;
import com.example.canary.sys.entity.PermissionVO;

/**
 * 权限
 *
 * @author zhaohongliang 2023-09-13 18:57
 * @since 1.0
 */
public interface PermissionService {

    /**
     * pages
     *
     * @param query
     * @return
     */
    ResultEntity<Page<PermissionVO>> pagesPermission(PermissionQuery query);

    /**
     * save
     *
     * @param permissionAo
     * @return
     */
    @SuppressWarnings("rawtypes")
    ResultEntity savePermission(PermissionAO permissionAo);

    /**
     * update
     *
     * @param permissionAo
     * @return
     */
    @SuppressWarnings("rawtypes")
    ResultEntity updatePermission(PermissionAO permissionAo);

    /**
     * delete
     *
     * @param permissionId
     * @return
     */
    @SuppressWarnings("rawtypes")
    ResultEntity deletePermission(String permissionId);
}
