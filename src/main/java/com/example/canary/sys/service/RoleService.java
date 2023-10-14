package com.example.canary.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
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
    IPage<RoleVO> pagesRole(RoleQuery query);

    /**
     * add
     *
     * @param roleAo
     * @return
     */
    RoleVO addRole(RoleAO roleAo);

    /**
     * edit
     *
     * @param roleAo
     * @return
     */
    RoleVO editRole(RoleAO roleAo);

    /**
     * delete
     *
     * @param id
     */
    void deleteRole(String id);
}
