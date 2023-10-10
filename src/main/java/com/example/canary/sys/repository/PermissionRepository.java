package com.example.canary.sys.repository;

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
     * list
     *
     * @return
     */
    List<PermissionVO> selectList();
}
