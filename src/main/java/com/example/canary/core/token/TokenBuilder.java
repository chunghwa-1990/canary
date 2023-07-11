package com.example.canary.core.token;

import java.time.Duration;

/**
 * jwt
 *
 * @ClassName TokenBuilder
 * @Description jwt
 * @Author zhaohongliang
 * @Date 2023-07-06 15:06
 * @Since 1.0
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
