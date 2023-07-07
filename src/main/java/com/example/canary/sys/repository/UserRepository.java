package com.example.canary.sys.repository;

import com.example.canary.sys.entity.UserPO;

/**
 * user
 *
 * @ClassName UserRepository
 * @Description user
 * @Author zhaohongliang
 * @Date 2023-07-06 12:44
 * @Since 1.0
 */
public interface UserRepository {

    /**
     * 根据账号查询
     *
     * @param account
     * @return
     */
    UserPO selectByAccount(String account);

    /**
     * insert
     *
     * @param userPo
     * @return
     */
    int insert(UserPO userPo);

    /**
     * update
     *
     * @param userPo
     * @return
     */
    int update(UserPO userPo);

    /**
     * delete
     *
     * @param userId
     * @return
     */
    int deleteById(String userId);
}
