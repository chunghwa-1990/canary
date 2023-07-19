package com.example.canary.common.token;

import org.springframework.boot.context.properties.PropertyMapper;

import java.time.Duration;

/**
 * jwt
 *
 * @since 1.0
 * @author zhaohongliang
 */
public class TokenBuilder {

    private final String secret;

    private final Duration timeout;

    /**
     * 无参构造
     */
    public TokenBuilder() {
        this.secret = null;
        this.timeout = null;
    }

    /**
     * 全参构造
     *
     * @param secret
     * @param timeout
     */
    public TokenBuilder(String secret, Duration timeout) {
        this.secret = secret;
        this.timeout = timeout;
    }

    /**
     * 设置密钥
     *
     * @param secret
     * @return
     */
    public TokenBuilder secret(String secret) {
        return new TokenBuilder(secret, this.timeout);
    }

    /**
     * 设置过期时间
     *
     * @param timeout
     * @return
     */
    public TokenBuilder timeout(Duration timeout) {
        return new TokenBuilder(this.secret, timeout);
    }

    /**
     * 创建 tokenBuilder
     *
     * @return
     */
    public static TokenBuilder create() {
        return new TokenBuilder();
    }

    /**
     * 根据 properties 创建 tokenBuilder
     *
     * @param properties
     * @return
     */
    public static TokenBuilder create(TokenProperties properties) {
        return new TokenBuilder(properties.getSecret(), properties.getTimeout());
    }

    /**
     * 构建 tokenService
     *
     * @return
     */
    public TokenService build() {
        return configure(new TokenService());
    }

    /**
     *
     *
     * @param service
     * @return
     */
    public TokenService configure(TokenService service) {
        PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
        map.from(this.secret).to(service::setSecret);
        map.from(this.timeout).to(service::setTimeout);
        return service;
    }

}
