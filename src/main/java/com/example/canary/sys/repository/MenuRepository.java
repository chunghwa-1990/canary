package com.example.canary.sys.repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.canary.sys.entity.MenuPO;
import com.example.canary.sys.entity.MenuQuery;
import com.example.canary.sys.entity.MenuVO;

import java.util.List;

/**
 * 菜单
 *
 * @author zhaohongliang 2023-09-09 14:54
 * @since 1.0
 */
public interface MenuRepository {

    /**
     * pages
     *
     * @param query
     * @return
     */
    IPage<MenuVO> pages(MenuQuery query);

    /**
     * insert
     *
     * @param menuPo
     * @return
     */
    int insert(MenuPO menuPo);

    /**
     * update
     *
     * @param menuPo
     * @return
     */
    int update(MenuPO menuPo);

    /**
     * delete
     *
     * @param id
     * @return
     */
    int deleteById(String id);

    /**
     * 根据 level 查询
     *
     * @param level
     * @return
     */
    List<MenuPO> selectByLevel(int level);

    /**
     * 根据 id 查询
     *
     * @param id
     * @return
     */
    MenuPO selectById(String id);

    /**
     * 根据 parentId 查询
     *
     * @param parentId
     * @return
     */
    List<MenuPO> selectByParentId(String parentId);
}
