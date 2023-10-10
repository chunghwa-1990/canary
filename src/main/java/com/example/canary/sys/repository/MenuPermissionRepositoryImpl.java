package com.example.canary.sys.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.canary.sys.entity.Menu1stDTO;
import com.example.canary.sys.entity.Menu2ndDTO;
import com.example.canary.sys.entity.MenuPO;
import com.example.canary.sys.entity.MenuPermissionPO;
import com.example.canary.sys.entity.MenuPermissionVO;
import com.example.canary.sys.entity.PermissionDTO;
import com.example.canary.sys.mapper.MenuMapper;
import com.example.canary.sys.mapper.MenuPermissionMapper;
import com.example.canary.sys.mapper.RolePermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
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
    private MenuMapper menuMapper;

    @Autowired
    private MenuPermissionMapper menuPermissionMapper;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    /**
     * insert
     *
     * @param menuPermissionPo
     * @return
     */
    @Override
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

    /**
     * 根据用户id 查询菜单权限
     *
     * @param userId
     * @return
     */
    @Override
    public List<MenuPermissionVO> selectByUserId(String userId) {
        // 权限
        List<PermissionDTO> permissionDtos = rolePermissionMapper.selectPermissionsByUserId(userId);
        if (CollectionUtils.isEmpty(permissionDtos)) {
            return Collections.emptyList();
        }
        List<String> permissionIds = permissionDtos.stream().map(PermissionDTO::getId).toList();

        // 二级菜单
        List<MenuPO> menu2ndPoList = menuPermissionMapper.selectMenusByPermissionIds(permissionIds);
        List<Menu2ndDTO> menu2ndDtos = menu2ndPoList.stream().map(Menu2ndDTO::new).toList();
        List<String> menu1stIds = menu2ndPoList.stream().map(MenuPO::getParentId).toList();

        // 一级菜单
        List<MenuPO> menu1stPoList = menuMapper.selectByIds(menu1stIds);
        List<Menu1stDTO> menu1stDtos = menu1stPoList.stream().map(Menu1stDTO::new).toList();

        for (Menu2ndDTO menu2ndDto : menu2ndDtos) {
            for (PermissionDTO permissionDto : permissionDtos) {
                if (menu2ndDto.getId().equals(permissionDto.getMenuId())) {
                    menu2ndDto.getChildren().add(permissionDto);
                }
            }
        }

        for (Menu1stDTO menu1stDto : menu1stDtos) {
            for(Menu2ndDTO menu2ndDto : menu2ndDtos) {
                if (menu1stDto.getId().equals(menu2ndDto.getParentId())) {
                    menu1stDto.getChildren().add(menu2ndDto);
                }
            }
        }

        return menu1stDtos.stream().map(MenuPermissionVO::new).toList();
    }
}
