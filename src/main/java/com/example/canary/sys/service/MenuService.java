package com.example.canary.sys.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.canary.common.exception.ResultEntity;
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
    ResultEntity<Page<MenuVO>> pagesMenu(MenuQuery query);

    /**
     * save
     *
     * @param menuAo
     * @return
     */
    @SuppressWarnings("rawtypes")
    ResultEntity saveMenu(MenuAO menuAo);

    /**
     * update
     *
     * @param menuAo
     * @return
     */
    @SuppressWarnings("rawtypes")
    ResultEntity updateMenu(MenuAO menuAo);

    /**
     * delete
     *
     * @param menuId
     * @return
     */
    @SuppressWarnings("rawtypes")
    ResultEntity deleteMenu(String menuId);
}
