package com.example.canary.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.canary.sys.entity.MenuPO;
import com.example.canary.sys.entity.MenuQuery;
import com.example.canary.sys.entity.MenuVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

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
     * @param page
     * @param orderMap
     * @return
     */
    IPage<MenuVO> selectPageVo(@Param("query") MenuQuery query, @Param("page") Page<MenuPO> page,
                             @Param("orderMap") Map<String, String> orderMap);
}
