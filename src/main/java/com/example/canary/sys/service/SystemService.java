package com.example.canary.sys.service;

import com.example.canary.common.exception.ResultEntity;
import com.example.canary.sys.entity.LoginAO;
import com.example.canary.sys.entity.LoginVO;
import com.example.canary.sys.entity.MenuPermissionVO;

import java.util.List;

/**
 * system
 *
 * @since 1.0
 * @author zhaohongliang
 */
public interface SystemService {

    /**
     * login
     *
     * @param loginAo
     * @return
     */
    ResultEntity<LoginVO> login(LoginAO loginAo);

    /**
     * logout
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    ResultEntity logout();

    /**
     * 菜单和权限
     *
     * @return
     */
    ResultEntity<List<MenuPermissionVO>> listPermissions();
}
