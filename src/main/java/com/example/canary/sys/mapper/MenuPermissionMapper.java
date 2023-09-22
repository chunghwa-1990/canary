package com.example.canary.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.canary.sys.entity.MenuPermissionPO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * 角色菜单关联关系
 *
 * @author zhaohongliang 2023-09-20 23:11
 * @since 1.0
 */
@Repository
public interface MenuPermissionMapper extends BaseMapper<MenuPermissionPO> {

    /**
     * delete by permissionId
     *
     * @param permissionId
     * @return
     */
    @Update("UPDATE sys_menu_permission SET is_deleted = id WHERE permission_id = #{permissionId}")
    int deleteByPermissionId(@Param("permissionId") String permissionId);
}
