package com.example.canary.sys.controller;

import com.example.canary.common.api.ApiVersion;
import com.example.canary.common.exception.ResultEntity;
import com.example.canary.sys.entity.LoginAO;
import com.example.canary.sys.entity.LoginVO;
import com.example.canary.sys.entity.MenuPermissionVO;
import com.example.canary.sys.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * system
 *
 * @since 1.0
 * @author zhaohongliang
 */
@Validated
@ApiVersion
@RestController
@RequestMapping("/sys")
public class SystemController {

    @Autowired
    private SystemService systemService;

    /**
     * login
     *
     * @param loginAo
     * @return
     */
    @PostMapping("/login")
    public ResultEntity<LoginVO> login(@Validated @RequestBody LoginAO loginAo) {
        return systemService.login(loginAo);
    }

    /**
     * logout
     *
     * @return
     */
    @PostMapping("/logout")
    @SuppressWarnings("rawtypes")
    public ResultEntity logout() {
        return systemService.logout();
    }

    /**
     * 菜单和权限
     *
     * @return
     */
    @GetMapping("/permissions")
    public ResultEntity<List<MenuPermissionVO>> listPermissions() {
        return systemService.listPermissions();
    }
}
