package com.example.canary.sys.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.canary.sys.entity.MenuPermissionPO;
import com.example.canary.sys.mapper.MenuPermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 菜单权限关联关系
 *
 * @author zhaohongliang 2023-09-20 23:04
 * @since 1.0
 */
@Service
public class MenuPermissionRepositoryImpl implements MenuPermissionRepository {

    @Autowired
    private MenuPermissionMapper menuPermissionMapper;

    /**
     * insert
     *
     * @param menuPermissionPo
     * @return
     */
    @Override
    @CacheEvict(cacheNames = "permission", key = "'list'")
    public int insert(MenuPermissionPO menuPermissionPo) {
        return menuPermissionMapper.insert(menuPermissionPo);
    }

    /**
     * 根据 permissionId 删除
     *
     * @param permissionId
     * @return
     */
    @Override
    @CacheEvict(cacheNames = "permission", allEntries = true)
    public int deleteByPermissionId(String permissionId) {
        return menuPermissionMapper.deleteByPermissionId(permissionId);
    }

    /**
     * 根据 menuId 查询
     *
     * @param menuId
     * @return
     */
    @Override
    public List<MenuPermissionPO> selectByMenuId(String menuId) {
        LambdaQueryWrapper<MenuPermissionPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MenuPermissionPO::getMenuId, menuId);
        return menuPermissionMapper.selectList(queryWrapper);
    }

}
