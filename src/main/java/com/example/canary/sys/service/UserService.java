package com.example.canary.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
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
    ResultEntity<IPage<UserVO>> pagesUser(UserQuery query);

    /**
     * add
     *
     * @param userAo
     * @return
     */
    @SuppressWarnings("rawtypes")
    ResultEntity addUser(UserAO userAo);

    /**
     * edit
     *
     * @param userAo
     * @return
     */
    @SuppressWarnings("rawtypes")
    ResultEntity editUser(UserAO userAo);

    /**
     * delete
     *
     * @param userId
     * @return
     */
    @SuppressWarnings("rawtypes")
    ResultEntity deleteUser(String userId);
}
