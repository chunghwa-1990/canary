package com.example.canary.sys.repository;

import com.example.canary.sys.entity.RolePO;
import com.example.canary.sys.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 角色
 *
 * @author zhaohongliang 2023-08-03 21:14
 * @since 1.0
 */
@Service
public class RoleRepositoryImpl implements RoleRepository {

    @Autowired
    private RoleMapper roleMapper;


    /**
     * insert
     *
     * @param rolePo
     * @return
     */
    @Override
    public int insert(RolePO rolePo) {
        return roleMapper.insert(rolePo);
    }

    /**
     * update
     *
     * @param rolePo
     * @return
     */
    @Override
    public int update(RolePO rolePo) {
        return roleMapper.updateById(rolePo);
    }

    /**
     * delete
     *
     * @param roleId
     * @return
     */
    @Override
    public int deleteById(String roleId) {
        return roleMapper.deleteById(roleId);
    }
}
