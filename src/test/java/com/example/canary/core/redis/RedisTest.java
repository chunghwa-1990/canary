package com.example.canary.core.redis;

import com.example.canary.sys.entity.UserPO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * redis test
 *
 * @since 1.0
 * @author zhaohongliang 2023-07-12 13:27
 */
@Slf4j
@SpringBootTest
class RedisTest {

    @Autowired
    private RedisTemplate<String, Object> template;

    @Test
    void test() {
        UserPO userPo = new UserPO();
        userPo.setAccount("test");
        template.opsForValue().set("test", userPo);
        Object object = template.opsForValue().get("test");
        if (object != null) {
            log.info(object.toString());
        }
    }

}
