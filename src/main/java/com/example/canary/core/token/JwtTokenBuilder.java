package com.example.canary.core.token;

import com.example.canary.util.JwtUtils;
import lombok.Data;

/**
 * jwt
 *
 * @ClassName JwtTokenBuilder
 * @Description jwt
 * @Author zhaohongliang
 * @Date 2023-07-06 15:07
 * @Since 1.0
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
    private Long expires;

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
