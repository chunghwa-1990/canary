package com.example.canary.core.token;

import java.time.Duration;

/**
 * jwt
 *
 * @since 1.0
 * @author zhaohongliang
 */
public interface TokenBuilder {

    /**
     * 设置密钥
     *
     * @param secret 密钥
     */
    void setSecret(String secret);

    /**
     * 设置过期间隔
     *
     * @param expires 过期间隔
     */
    void setExpires(Duration expires);

    /**
     * 设置 aud
     *
     * @param audience aud
     */
    void setAudience(String audience);

    /**
     * 设置载荷
     *
     * @param claim 载荷
     */
    void setClaim(String claim);

    /**
     * 构建 token
     *
     * @return token
     */
    String build();
}
