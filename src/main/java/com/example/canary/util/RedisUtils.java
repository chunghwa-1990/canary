package com.example.canary.util;

import com.example.canary.core.context.SpringContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * redis 工具类
 *
 * @author zhaohongliang 2023-07-16 22:29
 * @since 1.0
 */
@Slf4j
public class RedisUtils {

    private RedisUtils() {
    }

    @SuppressWarnings("unchecked")
    private static final RedisTemplate<String, Object> REDIS_TEMPLATE = SpringContext.getBean("redisTemplate", RedisTemplate.class);

    /**
     * 设置普通对象
     *
     * @param key
     * @param value
     * @return
     */
    public static boolean set(String key, Object value) {
        try {
            REDIS_TEMPLATE.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error("redis设置普通对象发生异常，异常信息：" + e.getMessage());
            return false;
        }
    }

    /**
     * 设置普通对象并设置过期时间
     *
     * @param key
     * @param value
     * @param timeout
     * @return
     */
    public static boolean set(String key, Object value, Duration timeout) {
        try {
            REDIS_TEMPLATE.opsForValue().set(key, value, timeout);
            return true;
        } catch (Exception e) {
            log.error("redis设置普通对象发生异常，异常信息：" + e.getMessage());
            return false;
        }
    }

    /**
     * 设置普通对象并设置过期时间
     *
     * @param key
     * @param value
     * @param timeout
     * @return
     */
    public static boolean set(String key, Object value, long timeout) {
        try {
            REDIS_TEMPLATE.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            log.error("redis设置普通对象发生异常，异常信息：" + e.getMessage());
            return false;
        }
    }
}
