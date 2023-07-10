package com.example.canary.sys.service;

import com.example.canary.core.token.JwtTokenBuilder;
import com.example.canary.core.token.TokenBuilder;
import com.example.canary.core.token.TokenDirector;
import com.example.canary.core.token.TokenProperties;
import com.example.canary.sys.entity.UserPO;
import com.example.canary.task.schedule.CronTaskRegistrar;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName SystemServiceImplTest
 * @Description
 * @Author zhaohongliang
 * @Date 2023-07-06 23:38
 * @Since 1.0
 */
@Slf4j
@SpringBootTest
class SystemServiceImplTest {

    @Autowired
    private TokenProperties tokenProperties;

    @Autowired
    private CronTaskRegistrar cronTaskRegistrar;

    @Test
    void test() {

        UserPO userPo = new UserPO();
        userPo.setAccount("test");

        TokenBuilder builder = new JwtTokenBuilder();
        TokenDirector director = new TokenDirector(builder);
        String token = null;
        try {
            token = director.createToken(tokenProperties, userPo.convertToVo());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        log.info(token);

        cronTaskRegistrar.getScheduledFutureMap().get("0").cancel(true);

    }

}