package com.example.canary.sys.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.canary.sys.entity.UserRoleBase;
import com.example.canary.sys.entity.UserRolePO;
import com.example.canary.sys.mapper.UserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户角色关联关系
 *
 * @author zhaohongliang 2023-09-21 14:15
 * @since 1.0
 */
@Service
public class UserRoleRepositoryImpl implements UserRoleRepository{

    @Autowired
    private UserRoleMapper userRoleMapper;

    /**
     * insert
     *
     * @param userRolePo
     * @return
     */
    @Override
    public int insert(UserRolePO userRolePo) {
        return userRoleMapper.insert(userRolePo);
    }

    /**
     * batch insert
     *
     * @param list
     * @return
     */
    @Override
    public int batchInsert(List<UserRolePO> list) {
        return userRoleMapper.batchInsert(list);
    }

    /**
     * delete by userId
     *
     * @param userId
     * @return
     */
    @Override
    public int deleteByUserId(String userId) {
        LambdaQueryWrapper<UserRolePO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRoleBase::getUserId, userId);
        return userRoleMapper.delete(queryWrapper);
    }

    /**
     * select by roleId
     *
     * @param roleId
     * @return
     */
    @Override
    public List<UserRolePO> selectByRoleId(String roleId) {
        LambdaQueryWrapper<UserRolePO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRolePO::getRoleId, roleId);
        return userRoleMapper.selectList(queryWrapper);
    }
}
