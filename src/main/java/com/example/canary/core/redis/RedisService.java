package com.example.canary.core.redis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * redis
 *
 * @author zhaohongliang 2023-07-17 00:35
 * @since 1.0
 */
@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RedisService {

    private Duration timeout;

    private RedisTemplate<String, Object> redisTemplate;

    public static RedisService create(Duration timeout, RedisTemplate<String, Object> redisTemplate) {
        return new RedisService(timeout, redisTemplate);
    }

    /**
     * 设置普通对象
     *
     * @param key
     * @param value
     * @return
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置普通对象并设置过期时间
     *
     * @param key
     * @param value
     * @param timeout
     * @return
     */
    public void set(String key, Object value, Duration timeout) {
        redisTemplate.opsForValue().set(key, value, timeout);
    }

    /**
     * 设置普通对象并设置过期时间
     *
     * @param key
     * @param value
     * @param timeout
     * @return
     */
    public void set(String key, Object value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    /**
     * 获取普通对象
     *
     * @param key
     * @return
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 设置过期时间
     *
     * @param key
     * @param timeout
     * @return
     */
    public Boolean expire(String key, Duration timeout) {
        return redisTemplate.expire(key, timeout);
    }

    /**
     * 设置过期时间
     *
     * @param key
     * @param timeout
     * @return
     */
    public Boolean expire(String key, long timeout) {
        return redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置截止时间
     *
     * @param key
     * @param date
     * @return
     */
    public Boolean expireAt(String key, Date date) {
        return redisTemplate.expireAt(key, date);
    }

    /**
     * 设置截止时间
     *
     * @param key
     * @param expireAt
     * @return
     */
    public Boolean expireAt(String key, Instant expireAt) {
        return redisTemplate.expireAt(key, expireAt);
    }
}
