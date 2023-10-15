package com.example.canary.sys.repository;

import com.example.canary.sys.entity.MenuPermissionVO;
import com.example.canary.sys.entity.PermissionDTO;
import com.example.canary.sys.entity.PermissionPO;
import com.example.canary.sys.entity.PermissionVO;

import java.util.List;

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
     * @param id
     * @return
     */
    int deleteById(String id);

    /**
     * 根据 id 查询
     *
     * @param id
     * @return
     */
    PermissionPO selectById(String id);

    /**
     * 权限是否正在被用户使用
     *
     * @param permissionId
     * @return
     */
    boolean isBeingUsed(String permissionId);

    /**
     * 根据用户id 查询菜单权限
     *
     * @param userId
     * @return
     */
    List<PermissionDTO> selectByUserId(String userId);

    /**
     * list
     *
     * @return
     */
    List<PermissionVO> list();
}
