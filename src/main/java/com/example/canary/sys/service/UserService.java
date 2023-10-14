package com.example.canary.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
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
    IPage<UserVO> pagesUser(UserQuery query);

    /**
     * add
     *
     * @param userAo
     * @return
     */
    UserVO addUser(UserAO userAo);

    /**
     * edit
     *
     * @param userAo
     * @return
     */
    UserVO editUser(UserAO userAo);

    /**
     * delete
     *
     * @param id
     */
    void deleteUser(String id);
}
