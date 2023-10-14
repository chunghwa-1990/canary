package com.example.canary.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.canary.sys.entity.MenuAO;
import com.example.canary.sys.entity.MenuQuery;
import com.example.canary.sys.entity.MenuVO;

/**
 * 菜单
 *
 * @author zhaohongliang 2023-09-09 14:53
 * @since 1.0
 */
public interface MenuService {

    /**
     * pages
     *
     * @param query
     * @return
     */
    IPage<MenuVO> pagesMenu(MenuQuery query);

    /**
     * add
     *
     * @param menuAo
     * @return
     */
    MenuVO addMenu(MenuAO menuAo);

    /**
     * update
     *
     * @param menuAo
     * @return
     */
    MenuVO editMenu(MenuAO menuAo);

    /**
     * delete
     *
     * @param id
     * @return
     */
    void deleteMenu(String id);
}
