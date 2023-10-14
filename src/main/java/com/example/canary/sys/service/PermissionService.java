package com.example.canary.sys.service;

import com.example.canary.sys.entity.MenuPermissionVO;
import com.example.canary.sys.entity.PermissionAO;
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
     * 根据用户id 查询菜单和权限
     *
     * @param userId
     * @return
     */
    List<MenuPermissionVO> queryPermissions(String userId);

    /**
     * list
     *
     * @return
     */
    List<MenuPermissionVO> listPermissions();

    /**
     * add
     *
     * @param permissionAo
     * @return
     */
    PermissionVO addPermission(PermissionAO permissionAo);

    /**
     * update
     *
     * @param permissionAo
     * @return
     */
    PermissionVO editPermission(PermissionAO permissionAo);

    /**
     * delete
     *
     * @param id
     */
    void deletePermission(String id);
}
