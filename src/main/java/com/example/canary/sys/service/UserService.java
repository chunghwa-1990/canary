package com.example.canary.sys.service;

import com.example.canary.core.exception.ResultEntity;
import com.example.canary.sys.entity.UserAO;

/**
 * user
 *
 * @ClassName UserService
 * @Description user
 * @Author zhaohongliang
 * @Date 2023-07-06 12:42
 * @Since 1.0
 */
public interface UserService {

    /**
     * save
     *
     * @param userAo
     * @return
     */
    @SuppressWarnings("rawtypes")
    ResultEntity saveUser(UserAO userAo);

    /**
     * update
     *
     * @param userAo
     * @return
     */
    @SuppressWarnings("rawtypes")
    ResultEntity updateUser(UserAO userAo);

    /**
     * delete
     *
     * @param userId
     * @return
     */
    @SuppressWarnings("rawtypes")
    ResultEntity deleteUser(String userId);
}
