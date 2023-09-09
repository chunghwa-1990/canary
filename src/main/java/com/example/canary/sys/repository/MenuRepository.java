package com.example.canary.sys.repository;

import com.example.canary.sys.entity.MenuPO;

/**
 * 菜单
 *
 * @author zhaohongliang 2023-09-09 14:54
 * @since 1.0
 */
public interface MenuRepository {

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
     * @param menuId
     * @return
     */
    int deleteById(String menuId);
}
