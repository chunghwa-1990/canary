package com.example.canary.sys.service;

import com.example.canary.core.exception.ResultEntity;
import com.example.canary.sys.entity.LoginAO;
import com.example.canary.sys.entity.LoginVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.Serializable;

/**
 * @author zhaohongliang
 * @since 1.0
 */
@Slf4j
@SpringBootTest
class SystemServiceImplTest {

    @Autowired
    private SystemService systemService;

    @Test
    void test() throws JsonProcessingException {
        LoginAO loginAo = new LoginAO();
        loginAo.setAccount("admin");
        loginAo.setPassword("123456");
        ResultEntity<LoginVO> result = systemService.login(loginAo);
        ObjectMapper objectMapper = new ObjectMapper();
        log.info(objectMapper.writeValueAsString(result));
    }

}