package com.example.canary.sys.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.canary.common.exception.ResultEntity;
import com.example.canary.sys.entity.RoleAO;
import com.example.canary.sys.entity.RoleQuery;
import com.example.canary.sys.entity.RoleVO;
import org.springframework.stereotype.Service;

/**
 * 角色
 *
 * @author zhaohongliang 2023-08-03 21:13
 * @since 1.0
 */
@Service
public class RoleServiceImpl implements RoleService{

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
     * save
     *
     * @param roleAo
     * @return
     */
    @Override
    @SuppressWarnings("rawtypes")
    public ResultEntity saveRole(RoleAO roleAo) {
        return null;
    }
}
