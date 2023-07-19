package com.example.canary.sys.service;

import com.example.canary.common.exception.ResultEntity;
import com.example.canary.sys.entity.LoginAO;
import com.example.canary.sys.entity.LoginVO;

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
}
