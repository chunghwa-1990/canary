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
    private PermissionRepository permissionRepository;

    @Autowired
    private MenuPermissionRepository menuPermissionRepository;

    @Autowired
    private RolePermissionRepository rolePermissionRepository;


    /**
     * 根据用户id 查询菜单和权限
     *
     * @param userId
     * @return
     */
    @Override
    public ResultEntity<List<MenuPermissionVO>> queryPermissions(String userId) {
        return ResultEntity.success(permissionRepository.selectByUserId(userId));
    }

    /**
     * list
     *
     * @return
     */
    @Override
    public ResultEntity<List<MenuPermissionVO>> listPermissions() {
        return ResultEntity.success(permissionRepository.list());
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
