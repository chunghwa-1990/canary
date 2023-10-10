package com.example.canary.sys.service;

import com.example.canary.common.exception.ResultEntity;
import com.example.canary.sys.entity.Menu1stDTO;
import com.example.canary.sys.entity.Menu2ndDTO;
import com.example.canary.sys.entity.MenuPO;
import com.example.canary.sys.entity.MenuPermissionPO;
import com.example.canary.sys.entity.MenuPermissionVO;
import com.example.canary.sys.entity.PermissionAO;
import com.example.canary.sys.entity.PermissionDTO;
import com.example.canary.sys.entity.PermissionPO;
import com.example.canary.sys.entity.PermissionVO;
import com.example.canary.sys.repository.MenuPermissionRepository;
import com.example.canary.sys.repository.MenuRepository;
import com.example.canary.sys.repository.PermissionRepository;
import com.example.canary.sys.repository.RolePermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 权限
 *
 * @author zhaohongliang 2023-09-13 18:57
 * @since 1.0
 */
@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private MenuPermissionRepository menuPermissionRepository;

    @Autowired
    private RolePermissionRepository rolePermissionRepository;


    /**
     * list
     *
     * @return
     */
    @Override
    public ResultEntity<List<MenuPermissionVO>> listPermissions() {

        // 一级菜单
        List<MenuPO> menu1stPoList = menuRepository.selectByLevel(1);
        List<Menu1stDTO> menu1stDtos = menu1stPoList.stream().map(Menu1stDTO::new).toList();

        // 二级菜单
        List<MenuPO> menu2ndPoList = menuRepository.selectByLevel(2);
        List<Menu2ndDTO> menu2ndDtos = menu2ndPoList.stream().map(Menu2ndDTO::new).toList();

        // 权限
        List<PermissionVO> permissionVoList = permissionRepository.selectList();
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

        List<MenuPermissionVO> menuPermissionVoList = menu1stDtos.stream().map(MenuPermissionVO::new).toList();
        return ResultEntity.success(menuPermissionVoList);
    }

    /**
     * add
     *
     * @param permissionAo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @SuppressWarnings("rawtypes")
    public ResultEntity addPermission(PermissionAO permissionAo) {
        // insert permission
        PermissionPO permissionPo = permissionAo.convertToPo();
        permissionRepository.insert(permissionPo);
        // insert relation
        MenuPermissionPO menuPermissionPo = new MenuPermissionPO(permissionAo.getMenuId(), permissionPo.getId());
        menuPermissionRepository.insert(menuPermissionPo);
        return ResultEntity.success();
    }

    /**
     * update
     * 
     * @param permissionAo
     * @return
     */
    @Override
    @SuppressWarnings("rawtypes")
    @Transactional(rollbackFor = Exception.class)
    public ResultEntity editPermission(PermissionAO permissionAo) {
        // update permission
        PermissionPO permissionPo = permissionAo.convertToPo();
        permissionRepository.update(permissionPo);
        // delete relation
        menuPermissionRepository.deleteByPermissionId(permissionAo.getId());
        // insert relation
        MenuPermissionPO menuPermissionPo = new MenuPermissionPO(permissionAo.getMenuId(), permissionPo.getId());
        menuPermissionRepository.insert(menuPermissionPo);
        return ResultEntity.success();
    }

    /**
     * delete
     * 
     * @param id
     * @return
     */
    @Override
    @SuppressWarnings("rawtypes")
    @Transactional(rollbackFor = Exception.class)
    public ResultEntity deletePermission(String id) {
        // 查询当前权限是否正在被用户使用
        boolean beingUsed = permissionRepository.isBeingUsed(id);
        if (beingUsed) {
            return ResultEntity.fail("此权限正在被用户使用，请先结束绑定后再删除");
        }
        // delete permission
        permissionRepository.deleteById(id);
        // delete relation
        menuPermissionRepository.deleteByPermissionId(id);
        rolePermissionRepository.deleteByPermissionId(id);
        return ResultEntity.success();
    }
}
