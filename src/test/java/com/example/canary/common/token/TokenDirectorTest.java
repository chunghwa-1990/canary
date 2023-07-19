package com.example.canary.common.token;

import com.example.canary.sys.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author zhaohongliang 2023-07-16 23:48
 * @since 1.0
 */
@Slf4j
@SpringBootTest
class TokenDirectorTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenProperties tokenProperties;

    @Test
    void createToken() {
        // UserPO userPo = userRepository.selectByAccount("admin");
        // TokenBuilder builder = new JwtTokenBuilder();
        // TokenDirector director = new TokenDirector(builder);
        // String token = null;
        // try {
        //     token = director.createToken(tokenProperties, userPo.convertToVo());
        // } catch (JsonProcessingException e) {
        //     e.printStackTrace();
        // }
        // log.info("token:{}", token);
    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

}