package com.example.canary.sys.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.canary.common.exception.ResultEntity;
import com.example.canary.sys.entity.UserAO;
import com.example.canary.sys.entity.UserPO;
import com.example.canary.sys.entity.UserQuery;
import com.example.canary.sys.entity.UserVO;
import com.example.canary.sys.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * user
 *
 * @since 1.0
 * @author zhaohongliang
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * query
     *
     * @param query
     * @return
     */
    @Override
    public ResultEntity<Page<UserVO>> pagesUser(UserQuery query) {
        return null;
    }

    /**
     * save
     *
     * @param userAo
     * @return
     */
    @Override
    @SuppressWarnings("rawtypes")
    public ResultEntity saveUser(UserAO userAo) {
        UserPO userPo = userAo.convertToPo();
        userRepository.insert(userPo);
        return ResultEntity.success();
    }

    /**
     * update
     *
     * @param userAo
     * @return
     */
    @Override
    @SuppressWarnings("rawtypes")
    public ResultEntity updateUser(UserAO userAo) {
        UserPO userPo = userAo.convertToPo();
        userRepository.update(userPo);
        return ResultEntity.success();
    }

    /**
     * deleted
     *
     * @param userId
     * @return
     */
    @Override
    @SuppressWarnings("rawtypes")
    public ResultEntity deleteUser(String userId) {
        userRepository.deleteById(userId);
        return ResultEntity.success();
    }
}
