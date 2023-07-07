package com.example.canary.sys.service;

import com.example.canary.core.exception.ResultEntity;
import com.example.canary.core.token.JwtTokenBuilder;
import com.example.canary.core.token.TokenBuilder;
import com.example.canary.core.token.TokenDirector;
import com.example.canary.core.token.TokenProperties;
import com.example.canary.sys.entity.LoginAO;
import com.example.canary.sys.entity.LoginVO;
import com.example.canary.sys.entity.UserPO;
import com.example.canary.sys.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * system
 *
 * @ClassName SystemServiceImpl
 * @Description system
 * @Author zhaohongliang
 * @Date 2023-07-06 21:58
 * @Since 1.0
 */
@Slf4j
@Service
public class SystemServiceImpl implements SystemService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenProperties tokenProperties;

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

        TokenBuilder builder = new JwtTokenBuilder();
        TokenDirector director = new TokenDirector(builder);
        String token = null;
        try {
            token = director.createToken(tokenProperties, userPo);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return ResultEntity.fail();
        }
        LoginVO loginVo = new LoginVO(token);
        return ResultEntity.success(loginVo);
    }
}
