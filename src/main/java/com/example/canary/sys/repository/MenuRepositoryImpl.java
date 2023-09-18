package com.example.canary.sys.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.canary.sys.entity.MenuPO;
import com.example.canary.sys.entity.MenuQuery;
import com.example.canary.sys.mapper.MenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 菜单
 *
 * @author zhaohongliang 2023-09-09 14:54
 * @since 1.0
 */
@Service
public class MenuRepositoryImpl implements MenuRepository {

    @Autowired
    private MenuMapper menuMapper;

    /**
     * pages
     *
     * @param query
     * @return
     */
    @Override
    public IPage<MenuPO> selectPage(MenuQuery query) {
        // return menuMapper.selectPage(query.getPage());
        return null;
    }

    /**
     * insert
     *
     * @param menuPo
     * @return
     */
    @Override
    public int insert(MenuPO menuPo) {
        return menuMapper.insert(menuPo);
    }

    /**
     * update
     *
     * @param menuPo
     * @return
     */
    @Override
    public int update(MenuPO menuPo) {
        return menuMapper.updateById(menuPo);
    }

    /**
     * delete
     *
     * @param menuId
     * @return
     */
    @Override
    public int deleteById(String menuId) {
        return menuMapper.deleteById(menuId);
    }
}
