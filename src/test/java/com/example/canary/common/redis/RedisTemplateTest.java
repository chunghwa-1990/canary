package com.example.canary.common.redis;

import com.example.canary.sys.entity.UserPO;
import com.example.canary.util.StringUtils;
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
class RedisTemplateTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void test() {
        String uuid = StringUtils.randomUUID();
        UserPO userPo = new UserPO();
        userPo.setId(uuid);
        userPo.setAccount("test123");
        redisTemplate.opsForValue().set(uuid, userPo);
        Object object = redisTemplate.opsForValue().get(uuid);
        if (object != null) {
            log.info(object.toString());
        }
    }

}
