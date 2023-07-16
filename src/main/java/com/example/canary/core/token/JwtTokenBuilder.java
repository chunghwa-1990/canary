package com.example.canary.core.token;

import com.example.canary.util.JwtUtils;
import lombok.Data;

import java.time.Duration;

/**
 * jwt
 *
 * @author zhaohongliang
 */
@Data
public class JwtTokenBuilder implements TokenBuilder {

    /**
     * 密钥
     */
    private String secret;

    /**
     * 过期间隔
     */
    private Duration expires;

    /**
     * aud
     */
    private String audience;

    /**
     * 载荷
     */
    private String claim;

    /**
     * 构建 token
     *
     * @return token
     */
    @Override
    public String build() {
        return JwtUtils.createJwtToken(secret, expires, claim, audience);
    }
}
