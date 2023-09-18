package com.example.canary.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.canary.sys.entity.MenuPO;
import com.example.canary.sys.entity.MenuQuery;
import org.springframework.stereotype.Repository;

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
     * @param query
     * @return
     */
    IPage<MenuPO> selectPage(MenuQuery query);
}
