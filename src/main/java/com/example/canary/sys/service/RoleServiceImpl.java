package com.example.canary.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.canary.common.exception.ResultEntity;
import com.example.canary.sys.entity.RoleAO;
import com.example.canary.sys.entity.RolePO;
import com.example.canary.sys.entity.RolePermissionPO;
import com.example.canary.sys.entity.RoleQuery;
import com.example.canary.sys.entity.RoleVO;
import com.example.canary.sys.repository.RolePermissionRepository;
import com.example.canary.sys.repository.RoleRepository;
import com.example.canary.sys.repository.UserRoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 角色
 *
 * @author zhaohongliang 2023-08-03 21:13
 * @since 1.0
 */
@Slf4j
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    /**
     * pages
     *
     * @param query
     * @return
     */
    @Override
    public ResultEntity<IPage<RoleVO>> pagesRole(RoleQuery query) {
        IPage<RoleVO> pages = roleRepository.pages(query);
        return ResultEntity.success(pages);
    }

    /**
     * add
     *
     * @param roleAo
     * @return
     */
    @Override
    @SuppressWarnings("rawtypes")
    @Transactional(rollbackFor = Exception.class)
    public ResultEntity addRole(RoleAO roleAo) {
        // insert role
        RolePO rolePo = roleAo.convertToPo();
        roleRepository.insert(rolePo);
        // batch insert relation
        List<RolePermissionPO> rolePermissions = roleAo.getRolePermissions(rolePo.getId());
        if (!CollectionUtils.isEmpty(rolePermissions)) {
            rolePermissionRepository.batchInsert(rolePermissions);
        }
        return ResultEntity.success();
    }

    /**
     * edit
     *
     * @param roleAo
     * @return
     */
    @Override
    @SuppressWarnings("rawtypes")
    @Transactional(rollbackFor = Exception.class)
    public ResultEntity editRole(RoleAO roleAo) {
        // update role
        RolePO rolePo = roleAo.convertToPo();
        roleRepository.update(rolePo);
        // delete relation
        rolePermissionRepository.deleteByRoleId(roleAo.getId());
        // batch insert relation
        List<RolePermissionPO> rolePermissions = roleAo.getRolePermissions(rolePo.getId());
        if (!CollectionUtils.isEmpty(rolePermissions)) {
            rolePermissionRepository.batchInsert(rolePermissions);
        }
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
    public ResultEntity deleteRole(String id) {
        // 查询角色是否正在被用户使用
        boolean beingUsed = roleRepository.isBeingUsed(id);
        if (beingUsed) {
            return ResultEntity.fail("此角色正在被用户使用，请先结束绑定后再删除");
        }
        // delete role
        roleRepository.deleteById(id);
        // delete relation
        userRoleRepository.deleteByRoleId(id);
        rolePermissionRepository.deleteByRoleId(id);
        return ResultEntity.success();
    }
}
