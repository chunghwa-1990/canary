package com.example.canary.sys.service;

import com.example.canary.core.exception.ResultEntity;
import com.example.canary.sys.entity.UserAO;
import com.example.canary.sys.entity.UserPO;
import com.example.canary.sys.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * user
 *
 * @ClassName UserServiceImpl
 * @Description user
 * @Author zhaohongliang
 * @Date 2023-07-06 12:43
 * @Since 1.0
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

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
        try {
            userRepository.insert(userPo);
        } catch (Exception e) {
            log.error(e.getMessage());
            Throwable cause = e.getCause();
            if (cause instanceof SQLIntegrityConstraintViolationException) {
                String errorMessage = cause.getMessage();
                if (StringUtils.hasText(errorMessage) && errorMessage.contains("udx_account_1")) {
                    return ResultEntity.fail("user account has exist");
                }
            }
            return ResultEntity.fail();
        }
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
        try {
            userRepository.update(userPo);
        } catch (Exception e) {
            log.error(e.getMessage());
            Throwable cause = e.getCause();
            if (cause instanceof SQLIntegrityConstraintViolationException) {
                String errorMessage = cause.getMessage();
                if (StringUtils.hasText(errorMessage) && errorMessage.contains("udx_account_1")) {
                    return ResultEntity.fail("user account has exist");
                }
            }
            return ResultEntity.fail();
        }
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
