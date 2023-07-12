package com.example.canary.core.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * redis 配置
 *
 * @since 1.0
 * @author zhaohongliang
 */
@Slf4j
@Configuration
public class RedisConfig {

    /**
     * redis 模版
     *
     * @param redisConnectionFactory 默认使用 LettuceConnectionFactory
     * @return template
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }




}
