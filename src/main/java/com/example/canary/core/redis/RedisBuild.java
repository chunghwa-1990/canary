package com.example.canary.core.redis;

import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * redis
 *
 * @author zhaohongliang 2023-07-17 04:22
 * @since 1.0
 */
public record RedisBuild(RedisTemplate<String, Object> redisTemplate) {

    public static RedisBuild create(RedisTemplate<String, Object> redisTemplate) {
        return new RedisBuild(redisTemplate);
    }

    public RedisService build() {
        return configure(new RedisService());
    }

    public RedisService configure(RedisService service) {
        PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
        map.from(this.redisTemplate).to(service::setRedisTemplate);
        return service;
    }
}
