package com.example.canary.sys.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.canary.common.exception.ResultEntity;
import com.example.canary.sys.entity.UserAO;
import com.example.canary.sys.entity.UserQuery;
import com.example.canary.sys.entity.UserVO;

/**
 * user
 *
 * @since 1.0
 * @author zhaohongliang
 */
public interface UserService {

    /**
     * query
     *
     * @param query
     * @return
     */
    ResultEntity<Page<UserVO>> pagesUser(UserQuery query);

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
