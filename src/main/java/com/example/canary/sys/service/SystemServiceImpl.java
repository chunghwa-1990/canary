package com.example.canary.sys.service;

import com.example.canary.common.context.CanaryContext;
import com.example.canary.common.exception.BusinessException;
import com.example.canary.common.exception.ResultEntity;
import com.example.canary.common.redis.RedisService;
import com.example.canary.common.token.TokenService;
import com.example.canary.sys.entity.LoginAO;
import com.example.canary.sys.entity.LoginVO;
import com.example.canary.sys.entity.UserPO;
import com.example.canary.sys.repository.MenuPermissionRepository;
import com.example.canary.sys.repository.UserRepository;
import com.example.canary.sys.repository.UserRoleRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * system
 *
 * @since 1.0
 * @author zhaohongliang
 */
@Slf4j
@Service
public class SystemServiceImpl implements SystemService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RedisService redisService;

    /**
     * login
     *
     * @param loginAo
     * @return
     */
    @Override
    public ResultEntity<LoginVO> login(LoginAO loginAo) {

        UserPO userPo = userRepository.selectByAccount(loginAo.getAccount());
        if (userPo == null) {
            return ResultEntity.fail("用户名或密码错误");
        }

        // 待加密的明文
        String planText = loginAo.getPassword() + userPo.getSalt();
        // 加密后的秘文
        String cipherText = DigestUtils.md5DigestAsHex(planText.getBytes(StandardCharsets.UTF_8));
        if (!userPo.getPassword().equals(cipherText)) {
            return ResultEntity.fail("用户名或密码错误");
        }

        String token = null;
        try {
            token = tokenService.createJwtToken(userPo.convertToVo());
        } catch (JsonProcessingException e) {
            throw new BusinessException("create token has error");
        }

        // redis
        redisService.set(userPo.getId(), token, tokenService.getTimeout());
        LoginVO loginVo = new LoginVO(token);
        return ResultEntity.success(loginVo);
    }

    /**
     * logout
     *
     * @return
     */
    @Override
    @SuppressWarnings("rawtypes")
    public ResultEntity logout() {
        String key = CanaryContext.getCurrentUser().getUserId();
        redisService.delete(key);
        return ResultEntity.success();
    }

}
