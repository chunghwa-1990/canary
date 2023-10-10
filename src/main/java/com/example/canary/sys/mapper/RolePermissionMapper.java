package com.example.canary.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.canary.sys.entity.PermissionDTO;
import com.example.canary.sys.entity.PermissionPO;
import com.example.canary.sys.entity.RolePermissionPO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    /**
     * delete by roleId
     *
     * @param roleId
     * @return
     */
    @Update("UPDATE sys_role_permission SET is_deleted = id WHERE role_id = #{roleId}")
    int deleteByRoleId(@Param("roleId") String roleId);

    /**
     * delete by permissionId
     *
     * @param permissionId
     * @return
     */
    @Update("UPDATE sys_role_permission SET is_deleted = id WHERE permission_id = #{permissionId}")
    int deleteByPermissionId(@Param("permissionId") String permissionId);

    /**
     * 根据角色id 查询权限
     *
     * @param roleIds
     * @return
     */
    List<PermissionPO> selectPermissionsByRoleIds(@Param("roleIds") List<String> roleIds);

    /**
     *
     * @param userId
     * @return
     */
    List<PermissionDTO> selectPermissionsByUserId(@Param("userId") String userId);
}
