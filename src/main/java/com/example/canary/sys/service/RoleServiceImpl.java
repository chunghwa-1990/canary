package com.example.canary.sys.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.canary.common.exception.ResultEntity;
import com.example.canary.sys.entity.RoleAO;
import com.example.canary.sys.entity.RolePO;
import com.example.canary.sys.entity.RoleQuery;
import com.example.canary.sys.entity.RoleVO;
import com.example.canary.sys.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.SQLIntegrityConstraintViolationException;

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

    /**
     * pages
     *
     * @param query
     * @return
     */
    @Override
    public ResultEntity<Page<RoleVO>> pagesRole(RoleQuery query) {
        return null;
    }

    /**
     * add
     *
     * @param roleAo
     * @return
     */
    @Override
    @SuppressWarnings("rawtypes")
    public ResultEntity saveRole(RoleAO roleAo) {
        RolePO rolePo = roleAo.convertToPo();
        roleRepository.insert(rolePo);
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
    public ResultEntity editRole(RoleAO roleAo) {
        RolePO rolePo = roleAo.convertToPo();
        roleRepository.update(rolePo);
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
    public ResultEntity deleteRole(String roleId) {
        roleRepository.deleteById(roleId);
        return ResultEntity.success();
    }
}
