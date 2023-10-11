package com.example.canary.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.canary.sys.entity.RolePO;
import com.example.canary.sys.entity.UserPO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * 角色
 *
 * @author zhaohongliang 2023-08-03 21:15
 * @since 1.0
 */
@Repository
public interface RoleMapper extends BaseMapper<RolePO> {

    /**
     * 查询正在使用该角色的用户
     *
     * @param roleId
     * @return
     */
    List<UserPO> selectUserByRoleId(@Param("roleId") String roleId);

    /**
     * delete
     *
     * @param id
     * @return
     */
    @Update("UPDATE sys_role SET is_deleted = id WHERE id = #{id}")
    int deleteById(@Param("id") String id);

    /**
     * 根据用户ID查询角色
     *
     * @param userId
     * @return
     */
    List<RolePO> selectByUserId(String userId);

}
