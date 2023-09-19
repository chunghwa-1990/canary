package com.example.canary.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.canary.common.exception.ResultEntity;
import com.example.canary.sys.entity.MenuAO;
import com.example.canary.sys.entity.MenuPO;
import com.example.canary.sys.entity.MenuQuery;
import com.example.canary.sys.entity.MenuVO;
import com.example.canary.sys.repository.MenuRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单
 *
 * @author zhaohongliang 2023-09-09 14:53
 * @since 1.0
 */
@Slf4j
@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuRepository menuRepository;

    /**
     * pages
     *
     * @param query
     * @return
     */
    @Override
    public ResultEntity<IPage<MenuVO>> pagesMenu(MenuQuery query) {
        IPage<MenuVO> pageVo = menuRepository.selectPageVo(query);
        return ResultEntity.success(pageVo);
    }

    /**
     * add
     *
     * @param menuAo
     * @return
     */
    @Override
    @SuppressWarnings("rawtypes")
    public ResultEntity addMenu(MenuAO menuAo) {
        MenuPO menuPo = menuAo.convertToPo();
        menuRepository.insert(menuPo);
        return ResultEntity.success();
    }

    /**
     * update
     *
     * @param menuAo
     * @return
     */
    @Override
    @SuppressWarnings("rawtypes")
    public ResultEntity editMenu(MenuAO menuAo) {
        MenuPO menuPo = menuAo.convertToPo();
        menuRepository.update(menuPo);
        return ResultEntity.success();
    }

    /**
     * delete
     *
     * @param menuId
     * @return
     */
    @Override
    @SuppressWarnings("rawtypes")
    public ResultEntity deleteMenu(String menuId) {
        menuRepository.deleteById(menuId);
        return ResultEntity.success();
    }
}
