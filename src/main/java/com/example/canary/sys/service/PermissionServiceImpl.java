package com.example.canary.sys.service;

import com.example.canary.common.exception.BusinessException;
import com.example.canary.sys.entity.Menu1stDTO;
import com.example.canary.sys.entity.Menu2ndDTO;
import com.example.canary.sys.entity.MenuPO;
import com.example.canary.sys.entity.MenuPermissionPO;
import com.example.canary.sys.entity.MenuPermissionVO;
import com.example.canary.sys.entity.PermissionAO;
import com.example.canary.sys.entity.PermissionDTO;
import com.example.canary.sys.entity.PermissionPO;
import com.example.canary.sys.entity.PermissionVO;
import com.example.canary.sys.repository.MenuPermissionRepository;
import com.example.canary.sys.repository.MenuRepository;
import com.example.canary.sys.repository.PermissionRepository;
import com.example.canary.sys.repository.RolePermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * 权限
 *
 * @author zhaohongliang 2023-09-13 18:57
 * @since 1.0
 */
@Service
@CacheConfig(cacheNames = "permission")
public class PermissionServiceImpl implements PermissionService {

    private final MenuRepository menuRepository;

    private final PermissionRepository permissionRepository;

    private final MenuPermissionRepository menuPermissionRepository;

    private final RolePermissionRepository rolePermissionRepository;

    @Autowired
    public PermissionServiceImpl(MenuRepository menuRepository, PermissionRepository permissionRepository,
                                 MenuPermissionRepository menuPermissionRepository, RolePermissionRepository rolePermissionRepository) {
        this.menuRepository = menuRepository;
        this.permissionRepository = permissionRepository;
        this.menuPermissionRepository = menuPermissionRepository;
        this.rolePermissionRepository = rolePermissionRepository;
    }

    /**
     * 根据用户id 查询菜单和权限
     *
     * @param userId
     * @return
     */
    @Override
    @Cacheable(key = "'user:' + #p0")
    public List<MenuPermissionVO> queryPermissions(String userId) {
        // 权限
        List<PermissionDTO> permissionDtos = permissionRepository.selectByUserId(userId);
        if (CollectionUtils.isEmpty(permissionDtos)) {
            return Collections.emptyList();
        }
        List<String> permissionIds = permissionDtos.stream().map(PermissionDTO::getId).toList();

        // 二级菜单
        List<MenuPO> menu2ndPoList = menuRepository.selectByPermissionIds(permissionIds);
        List<Menu2ndDTO> menu2ndDtos = menu2ndPoList.stream().map(Menu2ndDTO::new).toList();
        List<String> menu1stIds = menu2ndPoList.stream().map(MenuPO::getParentId).toList();

        // 一级菜单
        List<MenuPO> menu1stPoList = menuRepository.selectByIds(menu1stIds);
        List<Menu1stDTO> menu1stDtos = menu1stPoList.stream().map(Menu1stDTO::new).toList();

        for (Menu2ndDTO menu2ndDto : menu2ndDtos) {
            for (PermissionDTO permissionDto : permissionDtos) {
                if (menu2ndDto.getId().equals(permissionDto.getMenuId())) {
                    menu2ndDto.getChildren().add(permissionDto);
                }
            }
        }

        for (Menu1stDTO menu1stDto : menu1stDtos) {
            for(Menu2ndDTO menu2ndDto : menu2ndDtos) {
                if (menu1stDto.getId().equals(menu2ndDto.getParentId())) {
                    menu1stDto.getChildren().add(menu2ndDto);
                }
            }
        }

        return menu1stDtos.stream().map(MenuPermissionVO::new).toList();
    }

    /**
     * list
     *
     * @return
     */
    @Override
    @Cacheable(key = "'list'")
    public List<MenuPermissionVO> listPermissions() {
        // 一级菜单
        List<MenuPO> menu1stPoList = menuRepository.selectByLevel(1);
        List<Menu1stDTO> menu1stDtos = menu1stPoList.stream().map(Menu1stDTO::new).toList();

        // 二级菜单
        List<MenuPO> menu2ndPoList = menuRepository.selectByLevel(2);
        List<Menu2ndDTO> menu2ndDtos = menu2ndPoList.stream().map(Menu2ndDTO::new).toList();

        // 权限
        List<PermissionVO> permissionVoList = permissionRepository.list();
        List<PermissionDTO> permissionBos = permissionVoList.stream().map(PermissionDTO::new).toList();

        for (Menu2ndDTO menu2ndDto : menu2ndDtos) {
            for (PermissionDTO permissionDto : permissionBos) {
                if (menu2ndDto.getId().equals(permissionDto.getMenuId())) {
                    menu2ndDto.getChildren().add(permissionDto);
                }
            }
        }

        for (Menu1stDTO menu1stDto : menu1stDtos) {
            for(Menu2ndDTO menu2ndDto : menu2ndDtos) {
                if (menu1stDto.getId().equals(menu2ndDto.getParentId())) {
                    menu1stDto.getChildren().add(menu2ndDto);
                }
            }
        }

        return menu1stDtos.stream().map(MenuPermissionVO::new).toList();
    }

    /**
     * add
     *
     * @param permissionAo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = "permission", key = "'list'")
    public PermissionVO addPermission(PermissionAO permissionAo) {
        // insert permission
        PermissionPO permissionPo = permissionAo.convertToPo();
        permissionRepository.insert(permissionPo);
        // insert relation
        MenuPermissionPO menuPermissionPo = new MenuPermissionPO(permissionAo.getMenuId(), permissionPo.getId());
        menuPermissionRepository.insert(menuPermissionPo);
        return new PermissionVO(permissionPo);
    }

    /**
     * update
     * 
     * @param permissionAo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = "permission", allEntries = true)
    public PermissionVO editPermission(PermissionAO permissionAo) {
        // update permission
        PermissionPO permissionPo = permissionAo.convertToPo();
        permissionRepository.update(permissionPo);
        // delete relation
        menuPermissionRepository.deleteByPermissionId(permissionAo.getId());
        // insert relation
        MenuPermissionPO menuPermissionPo = new MenuPermissionPO(permissionAo.getMenuId(), permissionPo.getId());
        menuPermissionRepository.insert(menuPermissionPo);
        return new PermissionVO(permissionPo);
    }

    /**
     * delete
     * 
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = "permission", allEntries = true)
    public void deletePermission(String id) {
        // 查询当前权限是否正在被用户使用
        boolean beingUsed = permissionRepository.isBeingUsed(id);
        if (beingUsed) {
            throw new BusinessException("此权限正在被用户使用，请先结束绑定后再删除");
        }
        // delete permission
        permissionRepository.deleteById(id);
        // delete relation
        menuPermissionRepository.deleteByPermissionId(id);
        rolePermissionRepository.deleteByPermissionId(id);
    }
}
