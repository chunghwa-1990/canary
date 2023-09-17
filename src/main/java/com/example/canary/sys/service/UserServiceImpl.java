package com.example.canary.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.canary.common.exception.ResultEntity;
import com.example.canary.sys.entity.UserAO;
import com.example.canary.sys.entity.UserPO;
import com.example.canary.sys.entity.UserQuery;
import com.example.canary.sys.entity.UserVO;
import com.example.canary.sys.repository.UserRepository;
import com.example.canary.util.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    public ResultEntity<IPage<UserVO>> pagesUser(UserQuery query) {
        // 分页
        IPage<UserPO> pagePo = userRepository.selectPage(query);
        // 转化
        List<UserVO> records = pagePo.getRecords().stream().map(UserVO::new).collect(Collectors.toList());
        IPage<UserVO> pageVo = PageUtils.convertToVo(pagePo, records);
        return ResultEntity.success(pageVo);
    }

    /**
     * add
     *
     * @param userAo
     * @return
     */
    @Override
    @SuppressWarnings("rawtypes")
    public ResultEntity addUser(UserAO userAo) {
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
    public ResultEntity editUser(UserAO userAo) {
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
