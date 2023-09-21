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
import com.example.canary.util.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * pages
     *
     * @param query
     * @return
     */
    @Override
    public ResultEntity<IPage<RoleVO>> pagesRole(RoleQuery query) {
        IPage<RolePO> pagePo = roleRepository.selectPagePo(query);
        List<RoleVO> records = pagePo.getRecords().stream().map(RoleVO::new).collect(Collectors.toList());
        IPage<RoleVO> pageVo = PageUtils.convertToVo(pagePo, records);
        return ResultEntity.success(pageVo);
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
        List<RolePermissionPO> rolePermissionPoList = roleAo.getRolePermissionList(rolePo.getId());
        if (!CollectionUtils.isEmpty(rolePermissionPoList)) {
            rolePermissionRepository.batchInsert(rolePermissionPoList);
        }
        return ResultEntity.success();
    }

    /**
     * update
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
        List<RolePermissionPO> rolePermissionPoList = roleAo.getRolePermissionList(rolePo.getId());
        if (!CollectionUtils.isEmpty(rolePermissionPoList)) {
            rolePermissionRepository.batchInsert(rolePermissionPoList);
        }
        return ResultEntity.success();
    }

    /**
     * delete
     *
     * @param roleId
     * @return
     */
    @Override
    @SuppressWarnings("rawtypes")
    @Transactional(rollbackFor = Exception.class)
    public ResultEntity deleteRole(String roleId) {
        // delete role
        roleRepository.deleteById(roleId);
        // delete relation
        rolePermissionRepository.deleteByRoleId(roleId);
        return ResultEntity.success();
    }
}
