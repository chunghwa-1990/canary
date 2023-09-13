package com.example.canary.sys.repository;

import com.example.canary.sys.entity.PermissionAO;
import com.example.canary.sys.entity.PermissionPO;
import com.example.canary.sys.mapper.PermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 权限
 *
 * @author zhaohongliang 2023-09-13 18:59
 * @since 1.0
 */
@Service
public class PermissionRepositoryImpl implements PermissionRepository {

    @Autowired
    private PermissionMapper permissionMapper;

    /**
     * insert
     *
     * @param permissionPo
     * @return
     */
    @Override
    public int insert(PermissionPO permissionPo) {
        return permissionMapper.insert(permissionPo);
    }

    /**
     * update
     *
     * @param permissionPo
     * @return
     */
    @Override
    public int update(PermissionPO permissionPo) {
        return permissionMapper.updateById(permissionPo);
    }

    /**
     * delete
     *
     * @param permissionId
     * @return
     */
    @Override
    public int deleteById(String permissionId) {
        return permissionMapper.deleteById(permissionId);
    }
}
