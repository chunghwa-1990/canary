package com.example.canary.sys.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.canary.sys.entity.MenuPO;
import com.example.canary.sys.entity.MenuQuery;
import com.example.canary.sys.entity.MenuVO;
import com.example.canary.sys.mapper.MenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public IPage<MenuVO> selectPageVo(MenuQuery query) {
        return menuMapper.selectPageVo(query.getPage(), query);
    }

    /**
     * insert
     *
     * @param menuPo
     * @return
     */
    @Override
    @CacheEvict(cacheNames = "permission", allEntries = true)
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
    @CacheEvict(cacheNames = "permission", allEntries = true)
    public int update(MenuPO menuPo) {
        return menuMapper.updateById(menuPo);
    }

    /**
     * delete
     *
     * @param id
     * @return
     */
    @Override
    @CacheEvict(cacheNames = "permission", allEntries = true)
    public int deleteById(String id) {
        return menuMapper.deleteById(id);
    }

    /**
     * 根据 level 查询
     *
     * @param level
     * @return
     */
    @Override
    public List<MenuPO> selectByLevel(int level) {
        LambdaQueryWrapper<MenuPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MenuPO::getLevel, level);
        return menuMapper.selectList(queryWrapper);
    }

    /**
     * 根据 id 查询
     *
     * @param id
     * @return
     */
    @Override
    public MenuPO selectById(String id) {
        return menuMapper.selectById(id);
    }

    /**
     * 根据 parentId 查询
     *
     * @param parentId
     * @return
     */
    @Override
    public List<MenuPO> selectByParentId(String parentId) {
        LambdaQueryWrapper<MenuPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MenuPO::getParentId, parentId);
        return menuMapper.selectList(queryWrapper);
    }
}
