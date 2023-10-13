package com.example.canary.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.canary.common.context.CanaryContext;
import com.example.canary.common.exception.ResultEntity;
import com.example.canary.sys.entity.UserAO;
import com.example.canary.sys.entity.UserPO;
import com.example.canary.sys.entity.UserQuery;
import com.example.canary.sys.entity.UserRolePO;
import com.example.canary.sys.entity.UserVO;
import com.example.canary.sys.repository.UserRepository;
import com.example.canary.sys.repository.UserRoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

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

    @Autowired
    private UserRoleRepository userRoleRepository;

    /**
     * query
     *
     * @param query
     * @return
     */
    @Override
    public ResultEntity<IPage<UserVO>> pagesUser(UserQuery query) {
        IPage<UserVO> pages = userRepository.pages(query);
        return ResultEntity.success(pages);
    }

    /**
     * add
     *
     * @param userAo
     * @return
     */
    @Override
    @SuppressWarnings("rawtypes")
    @Transactional(rollbackFor = Exception.class)
    public ResultEntity addUser(UserAO userAo) {
        // insert user
        UserPO userPo = userAo.convertToPo();
        userRepository.insert(userPo);
        // batch insert relation
        List<UserRolePO> userRoles = userAo.getUserRoles(userPo.getId());
        if (!CollectionUtils.isEmpty(userRoles)) {
            userRoleRepository.batchInsert(userRoles);
        }
        return ResultEntity.success();
    }

    /**
     * edit
     *
     * @param userAo
     * @return
     */
    @Override
    @SuppressWarnings("rawtypes")
    public ResultEntity editUser(UserAO userAo) {
        // update user
        UserPO userPo = userAo.convertToPo();
        userRepository.update(userPo);
        // delete relation
        userRoleRepository.deleteByUserId(userPo.getId());
        // batch insert
        List<UserRolePO> userRoles = userAo.getUserRoles(userPo.getId());
        if (!CollectionUtils.isEmpty(userRoles)) {
            userRoleRepository.batchInsert(userRoles);
        }
        return ResultEntity.success();
    }

    /**
     * deleted
     *
     * @param id
     * @return
     */
    @Override
    @SuppressWarnings("rawtypes")
    public ResultEntity deleteUser(String id) {
        // 当前用户
        String currentUserId = CanaryContext.getCurrentUser().getUserId();
        if (id.equals(currentUserId)) {
            return ResultEntity.fail("无法删除当前用户");
        }
        // 查询当前删除的用户
        UserPO userPo = userRepository.selectById(id);
        if (userPo == null) {
            return ResultEntity.fail("此用户不存在或ID错误");
        }
        if (userPo.getIsAdmin() == 1) {
            return ResultEntity.fail("无法删除超级管理员");
        }
        // delete user
        userRepository.deleteById(id);
        // delete relation
        userRoleRepository.deleteByUserId(id);
        return ResultEntity.success();
    }
}
