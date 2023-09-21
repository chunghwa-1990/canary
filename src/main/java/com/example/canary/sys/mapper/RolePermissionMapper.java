package com.example.canary.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.canary.sys.entity.RolePermissionPO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * 角色权限关联关系
 *
 * @author zhaohongliang 2023-09-21 10:34
 * @since 1.0
 */
@Repository
public interface RolePermissionMapper extends BaseMapper<RolePermissionPO> {


    /**
     * batch insert
     *
     * @param list
     * @return
     */
    int batchInsert(@Param("list") List<RolePermissionPO> list);
}
