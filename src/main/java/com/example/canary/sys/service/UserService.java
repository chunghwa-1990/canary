package com.example.canary.sys.service;

import com.example.canary.core.exception.ResultEntity;
import com.example.canary.sys.entity.UserAO;

/**
 * user
 *
 * @since 1.0
 * @author zhaohongliang
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
