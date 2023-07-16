package com.example.canary.core.token;

import org.springframework.boot.context.properties.PropertyMapper;

import java.time.Duration;

/**
 * jwt
 *
 * @since 1.0
 * @author zhaohongliang
 */
public record TokenBuilder(String secret, Duration timeout) {


    public static TokenBuilder create(TokenProperties properties) {
        return new TokenBuilder(properties.getSecret(), properties.getTimeout());
    }

    public TokenService build() {
        return configure(new TokenService());
    }

    public TokenService configure(TokenService service) {
        PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
        map.from(this.secret).to(service::setSecret);
        map.from(this.timeout).to(service::setTimeout);
        return service;
    }

}
