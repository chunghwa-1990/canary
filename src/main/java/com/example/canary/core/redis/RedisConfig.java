package com.example.canary.core.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * redis 配置
 *
 * @className RedisConfig
 * @Description redis 配置
 * @Author zhaohongliang
 * @Date 2023-07-10 20:44
 * @Since 1.0
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
