package com.example.canary.sys.repository;

import com.example.canary.sys.entity.PermissionAO;
import com.example.canary.sys.entity.PermissionPO;
import com.example.canary.sys.entity.UserPO;
import com.example.canary.sys.mapper.PermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

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
     * @param id
     * @return
     */
    @Override
    public int deleteById(String id) {
        return permissionMapper.deleteById(id);
    }

    /**
     * 根据 id 查询
     *
     * @param id
     * @return
     */
    @Override
    public PermissionPO selectById(String id) {
        return permissionMapper.selectById(id);
    }

    /**
     * 权限是否正在被用户使用
     *
     * @param permissionId
     * @return
     */
    @Override
    public boolean isBeingUsed(String permissionId) {
        return !CollectionUtils.isEmpty(permissionMapper.selectUserByPermissionId(permissionId));
    }
}
