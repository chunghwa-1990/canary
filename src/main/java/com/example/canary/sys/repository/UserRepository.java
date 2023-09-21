package com.example.canary.sys.repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.canary.common.exception.ResultEntity;
import com.example.canary.sys.entity.UserPO;
import com.example.canary.sys.entity.UserQuery;
import com.example.canary.sys.entity.UserVO;

/**
 * user
 *
 * @since 1.0
 * @author zhaohongliang
 */
public interface UserRepository {

    /**
     * 分页
     *
     * @param query
     * @return
     */
    IPage<UserPO> selectPage(UserQuery query);

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

    /**
     * select by id
     *
     * @param userId
     * @return
     */
    UserPO selectById(String userId);
}
