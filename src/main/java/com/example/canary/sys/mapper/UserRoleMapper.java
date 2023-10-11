package com.example.canary.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.canary.sys.entity.UserRolePO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户角色关联关系
 *
 * @author zhaohongliang 2023-09-21 14:16
 * @since 1.0
 */
@Repository
public interface UserRoleMapper extends BaseMapper<UserRolePO> {

    /**
     * batch insert
     *
     * @param list
     * @return
     */
    int batchInsert(@Param("list") List<UserRolePO> list);

    /**
     * delete by roleId
     *
     * @param roleId
     * @return
     */
    @Update("UPDATE sys_user_role SET is_deleted = id WHERE role_id = #{roleId}")
    int deleteByRoleId(@Param("roleId") String roleId);

}
