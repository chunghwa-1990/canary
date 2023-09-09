package com.example.canary.sys.service;

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
    public ResultEntity<Page<MenuVO>> pagesMenu(MenuQuery query) {
        return null;
    }

    /**
     * save
     *
     * @param menuAo
     * @return
     */
    @Override
    @SuppressWarnings("rawtypes")
    public ResultEntity saveMenu(MenuAO menuAo) {
        MenuPO menuPo = menuAo.convertToPo();
        try {
            menuRepository.insert(menuPo);
        } catch (Exception e) {
            log.error(e.getMessage());
            Throwable cause = e.getCause();
            if (cause instanceof SQLIntegrityConstraintViolationException) {
                String errorMessage = cause.getMessage();
                if (StringUtils.hasText(errorMessage) && errorMessage.contains("udx_menu_1")) {
                    return ResultEntity.fail("menu has exist");
                }
            }
            return ResultEntity.fail();
        }
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
    public ResultEntity updateMenu(MenuAO menuAo) {
        MenuPO menuPo = menuAo.convertToPo();
        try {
            menuRepository.update(menuPo);
        } catch (Exception e) {
            log.error(e.getMessage());
            Throwable cause = e.getCause();
            if (cause instanceof SQLIntegrityConstraintViolationException) {
                String errorMessage = cause.getMessage();
                if (StringUtils.hasText(errorMessage) && errorMessage.contains("udx_menu_1")) {
                    return ResultEntity.fail("menu has exist");
                }
            }
            return ResultEntity.fail();
        }
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
