package com.example.canary.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.canary.common.exception.BusinessException;
import com.example.canary.common.exception.ResultEntity;
import com.example.canary.sys.entity.MenuAO;
import com.example.canary.sys.entity.MenuPO;
import com.example.canary.sys.entity.MenuPermissionPO;
import com.example.canary.sys.entity.MenuQuery;
import com.example.canary.sys.entity.MenuVO;
import com.example.canary.sys.repository.MenuPermissionRepository;
import com.example.canary.sys.repository.MenuRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

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

    @Autowired
    private MenuPermissionRepository menuPermissionRepository;

    /**
     * pages
     *
     * @param query
     * @return
     */
    @Override
    public IPage<MenuVO> pagesMenu(MenuQuery query) {
        IPage<MenuVO> pageVo = menuRepository.pages(query);
        return pageVo;
    }

    /**
     * add
     *
     * @param menuAo
     * @return
     */
    @Override
    public MenuVO addMenu(MenuAO menuAo) {
        MenuPO menuPo = menuAo.convertToPo();
        menuRepository.insert(menuPo);
        return new MenuVO(menuPo);
    }

    /**
     * update
     *
     * @param menuAo
     * @return
     */
    @Override
    public MenuVO editMenu(MenuAO menuAo) {
        MenuPO menuPo = menuAo.convertToPo();
        menuRepository.update(menuPo);
        return new MenuVO(menuPo);
    }

    /**
     * delete
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMenu(String id) {
        MenuPO menuPo = menuRepository.selectById(id);
        if (menuPo == null) {
            throw new BusinessException("菜单不存在或ID错误");
        }
        if (menuPo.getLevel() == 1) {
            List<MenuPO> menuChildren = menuRepository.selectByParentId(id);
            if (!CollectionUtils.isEmpty(menuChildren)) {
                throw new BusinessException("无法删除已有下级的菜单");
            }
        } else {
            List<MenuPermissionPO> menuPermissions = menuPermissionRepository.selectByMenuId(id);
            if (!CollectionUtils.isEmpty(menuPermissions)) {
                throw new BusinessException("无法删除已有权限的菜单");
            }
        }
        // delete
        menuRepository.deleteById(id);
    }
}
