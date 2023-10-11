package com.example.canary.sys.repository;

import com.example.canary.sys.entity.Menu1stDTO;
import com.example.canary.sys.entity.Menu2ndDTO;
import com.example.canary.sys.entity.MenuPO;
import com.example.canary.sys.entity.MenuPermissionVO;
import com.example.canary.sys.entity.PermissionDTO;
import com.example.canary.sys.entity.PermissionPO;
import com.example.canary.sys.entity.PermissionVO;
import com.example.canary.sys.mapper.MenuMapper;
import com.example.canary.sys.mapper.MenuPermissionMapper;
import com.example.canary.sys.mapper.PermissionMapper;
import com.example.canary.sys.mapper.RolePermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
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
    private MenuRepository menuRepository;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Autowired
    private MenuPermissionMapper menuPermissionMapper;

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

    /**
     * 根据用户id 查询菜单权限
     *
     * @param userId
     * @return
     */
    @Override
    public List<MenuPermissionVO> selectByUserId(String userId) {
        // 权限
        List<PermissionDTO> permissionDtos = permissionMapper.selectByUserId(userId);
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

    /**
     * 查询所有菜单和权限
     *
     * @return
     */
    @Override
    public List<MenuPermissionVO> list() {
        // 一级菜单
        List<MenuPO> menu1stPoList = menuRepository.selectByLevel(1);
        List<Menu1stDTO> menu1stDtos = menu1stPoList.stream().map(Menu1stDTO::new).toList();

        // 二级菜单
        List<MenuPO> menu2ndPoList = menuRepository.selectByLevel(2);
        List<Menu2ndDTO> menu2ndDtos = menu2ndPoList.stream().map(Menu2ndDTO::new).toList();

        // 权限
        List<PermissionVO> permissionVoList = permissionMapper.list();
        List<PermissionDTO> permissionBos = permissionVoList.stream().map(PermissionDTO::new).toList();

        for (Menu2ndDTO menu2ndDto : menu2ndDtos) {
            for (PermissionDTO permissionDto : permissionBos) {
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
