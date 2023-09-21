package com.example.canary.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.canary.common.exception.ResultEntity;
import com.example.canary.sys.entity.RoleAO;
import com.example.canary.sys.entity.RoleQuery;
import com.example.canary.sys.entity.RoleVO;

/**
 * 角色
 *
 * @author zhaohongliang 2023-08-03 21:13
 * @since 1.0
 */
public interface RoleService {

    /**
     * pages
     *
     * @param query
     * @return
     */
    ResultEntity<IPage<RoleVO>> pagesRole(RoleQuery query);

    /**
     * add
     *
     * @param roleAo
     * @return
     */
    @SuppressWarnings("rawtypes")
    ResultEntity addRole(RoleAO roleAo);

    /**
     * edit
     *
     * @param roleAo
     * @return
     */
    @SuppressWarnings("rawtypes")
    ResultEntity editRole(RoleAO roleAo);

    /**
     * delete
     *
     * @param roleId
     * @return
     */
    @SuppressWarnings("rawtypes")
    ResultEntity deleteRole(String roleId);
}
