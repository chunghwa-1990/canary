package com.example.canary.sys.repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.canary.sys.entity.RolePO;
import com.example.canary.sys.entity.RoleQuery;

/**
 * 角色
 *
 * @author zhaohongliang 2023-08-03 21:14
 * @since 1.0
 */
public interface RoleRepository {

    /**
     * page
     *
     * @param query
     * @return
     */
    IPage<RolePO> selectPage(RoleQuery query);

    /**
     * insert
     *
     * @param rolePo
     * @return
     */
    int insert(RolePO rolePo);

    /**
     * update
     *
     * @param rolePo
     * @return
     */
    int update(RolePO rolePo);

    /**
     * delete
     *
     * @param roleId
     * @return
     */
    int deleteById(String roleId);
}
