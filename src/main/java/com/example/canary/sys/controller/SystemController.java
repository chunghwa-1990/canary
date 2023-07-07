package com.example.canary.sys.controller;

import com.example.canary.core.api.ApiVersion;
import com.example.canary.core.exception.ResultEntity;
import com.example.canary.sys.entity.LoginAO;
import com.example.canary.sys.entity.LoginVO;
import com.example.canary.sys.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * system
 *
 * @ClassName SystemController
 * @Description system
 * @Author zhaohongliang
 * @Date 2023-07-06 12:46
 * @Since 1.0
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
        return null;
    }
}
