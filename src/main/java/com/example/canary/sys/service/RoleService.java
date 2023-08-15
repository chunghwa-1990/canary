package com.example.canary.sys.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
    ResultEntity<Page<RoleVO>> pagesRole(RoleQuery query);

    /**
     * save
     *
     * @param roleAo
     * @return
     */
    @SuppressWarnings("rawtypes")
    ResultEntity saveRole(RoleAO roleAo);

    /**
     * update
     *
     * @param roleAo
     * @return
     */
    @suppressWarnings("rawtypes")
    ResultEntity updateRole(RoleAO roleAo);
}
