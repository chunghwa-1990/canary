package com.example.canary.sys.service;

import com.example.canary.core.exception.ResultEntity;
import com.example.canary.sys.entity.LoginAO;
import com.example.canary.sys.entity.LoginVO;

/**
 * system
 *
 * @ClassName SystemService
 * @Description system
 * @Author zhaohongliang
 * @Date 2023-07-06 21:58
 * @Since 1.0
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
