package com.example.canary.sys.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.canary.common.enums.StatusEnum;
import com.example.canary.sys.entity.UserPO;
import com.example.canary.sys.entity.UserQuery;
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
 * @since 1.0
 * @author zhaohongliang
 */
@Service
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private UserMapper userMapper;

    /**
     * 分页
     *
     * @param query
     * @return
     */
    @Override
    public IPage<UserPO> pages(UserQuery query) {
        LambdaQueryWrapper<UserPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.and(StringUtils.hasText(query.getKeywords()), wrapper -> wrapper.like(UserPO::getAccount, query.getKeywords())
                .or().like(UserPO::getNickName, query.getKeywords())
                .or().like(UserPO::getRealName, query.getKeywords()));
        return userMapper.selectPage(query.getPage(), queryWrapper);
    }

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
     * @param id
     * @return
     */
    @Override
    public int deleteById(String id) {
        return userMapper.deleteById(id);
    }

    /**
     * select by id
     *
     * @param id
     * @return
     */
    @Override
    public UserPO selectById(String id) {
        return userMapper.selectById(id);
    }
}
