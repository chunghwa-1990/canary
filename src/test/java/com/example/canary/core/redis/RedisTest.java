package com.example.canary.core.redis;

import com.example.canary.task.schedule.ScheduledConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

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
        template.opsForValue().set("test", "123");
        Object object = template.opsForValue().get("test");
        log.info(object.toString());
    }

}
