package com.example.canary.sys.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.canary.sys.entity.RolePO;
import com.example.canary.sys.entity.RoleQuery;
import com.example.canary.sys.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 角色
 *
 * @author zhaohongliang 2023-08-03 21:14
 * @since 1.0
 */
@Service
public class RoleRepositoryImpl implements RoleRepository {

    private final RoleMapper roleMapper;

    @Autowired
    public RoleRepositoryImpl(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    /**
     * page
     *
     * @param query
     * @return
     */
    @Override
    public IPage<RolePO> pages(RoleQuery query) {
        LambdaQueryWrapper<RolePO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.and(StringUtils.hasText(query.getKeywords()), wrapper -> wrapper.like(RolePO::getName, query.getKeywords())
                .or().like(RolePO::getDescription, query.getKeywords()));
        return roleMapper.selectPage(query.getPage(), queryWrapper);
    }

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
     * @param id
     * @return
     */
    @Override
    public int deleteById(String id) {
        return roleMapper.deleteById(id);
    }

    /**
     * 角色是否正在被使用
     *
     * @param roleId
     * @return
     */
    @Override
    public boolean isBeingUsed(String roleId) {
        return !CollectionUtils.isEmpty(roleMapper.selectUserByRoleId(roleId));
    }

    /**
     * 根据用户ID查询关联关系
     *
     * @param userId
     * @return
     */
    @Override
    public List<RolePO> selectByUserId(String userId) {
        return roleMapper.selectByUserId(userId);
    }
}
