package com.example.canary.common.token;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;

/**
 * @author zhaohongliang
 * @version 1.0
 */
@Slf4j
@SpringBootTest
class TokenPropertiesTest {

    @Autowired
    private TokenProperties tokenProperties;

    @Test
    void test() {
        Duration duration = tokenProperties.getTimeout();
        log.info("expires:{}" , duration.toMillis());
    }

}