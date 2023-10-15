package com.example.canary.sys.repository;

import com.example.canary.sys.entity.RolePermissionPO;
import com.example.canary.sys.mapper.RolePermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色权限关联关系
 *
 * @author zhaohongliang 2023-09-21 10:29
 * @since 1.0
 */
@Service
public class RolePermissionRepositoryImpl implements RolePermissionRepository {

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    /**
     * insert
     *
     * @param rolePermissionPo
     * @return
     */
    @Override
    public int insert(RolePermissionPO rolePermissionPo) {
        return rolePermissionMapper.insert(rolePermissionPo);
    }

    /**
     * batch insert
     *
     * @param list
     * @return
     */
    @Override
    public int batchInsert(List<RolePermissionPO> list) {
        return rolePermissionMapper.batchInsert(list);
    }

    /**
     * delete by roleId
     *
     * @param roleId
     * @return
     */
    @Override
    public int deleteByRoleId(String roleId) {
        return rolePermissionMapper.deleteByRoleId(roleId);
    }

    /**
     * delete by permissionId
     *
     * @param permissionId
     * @return
     */
    @Override
    public int deleteByPermissionId(String permissionId) {
        return rolePermissionMapper.deleteByPermissionId(permissionId);
    }

}
