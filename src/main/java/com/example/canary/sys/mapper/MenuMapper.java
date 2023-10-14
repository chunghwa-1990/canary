package com.example.canary.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.canary.sys.entity.MenuPO;
import com.example.canary.sys.entity.MenuQuery;
import com.example.canary.sys.entity.MenuVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 菜单
 *
 * @author zhaohongliang 2023-09-09 14:55
 * @since 1.0
 */
@Repository
public interface MenuMapper extends BaseMapper<MenuPO> {

    /**
     * pages
     *
     * @param page
     * @param query
     * @return
     */
    IPage<MenuVO> pages(@Param("page") Page<MenuPO> page, @Param("query") MenuQuery query);

    /**
     * delete
     *
     * @param id
     * @return
     */
    @Update("UPDATE sys_menu SET is_deleted = id WHERE id = #{id}")
    int deleteById(@Param("id") String id);

    /**
     * 根据id 查询菜单
     *
     * @param ids
     * @return
     */
    List<MenuPO> selectByIds(@Param("ids") List<String> ids);

    /**
     * 根绝权限id 查询菜单
     *
     * @param permissionIds
     * @return
     */
    List<MenuPO> selectByPermissionIds(@Param("permissionIds") List<String> permissionIds);
}
