package com.example.canary.sys.repository;

import ch.qos.logback.core.testUtil.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.canary.core.enums.StatusEnum;
import com.example.canary.sys.entity.UserPO;
import com.example.canary.sys.mapper.UserMapper;
import com.example.canary.util.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;

/**
 * user
 *
 * @ClassName UserRepositoryImpl
 * @Description user
 * @Author zhaohongliang
 * @Date 2023-07-06 12:44
 * @Since 1.0
 */
@Service
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private UserMapper userMapper;

    /**
     * 根据账号查询
     *
     * @param account
     * @return
     */
    @Override
    public UserPO selectByAccount(String account) {
        LambdaQueryWrapper<UserPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserPO::getAccount, account);
        queryWrapper.eq(UserPO::getDisabled, StatusEnum.Disabled.FALSE.getCode());
        queryWrapper.eq(UserPO::getDeleted, StatusEnum.Deleted.FALSE.getCode());
        return userMapper.selectOne(queryWrapper);
    }

    /**
     * insert
     *
     * @param userPo
     * @return
     */
    @Override
    public int insert(UserPO userPo) {
        if (StringUtils.hasText(userPo.getPassword())) {
            String salt = RandomUtils.randomStr(6);
            String pass = userPo.getPassword() + salt;
            String password = DigestUtils.md5DigestAsHex(pass.getBytes(StandardCharsets.UTF_8));
            userPo.setSalt(salt);
            userPo.setPassword(password);
        }
        return userMapper.insert(userPo);
    }

    /**
     * update
     *
     * @param userPo
     * @return
     */
    @Override
    public int update(UserPO userPo) {
        return userMapper.updateById(userPo);
    }

    /**
     * delete
     *
     * @param userId
     * @return
     */
    @Override
    public int deleteById(String userId) {
        return userMapper.deleteById(userId);
    }
}
