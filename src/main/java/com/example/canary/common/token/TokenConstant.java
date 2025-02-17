package com.example.canary.common.token;

import java.time.Duration;

/**
 * jwt常量池
 *
 * @since 1.0
 * @author zhaohongliang
 */
public class TokenConstant {

    private TokenConstant() {}

    /**
     * token
     */
    public static final String TOKEN = "token";

    /**
     * 黑名单
     */
    public static final String BLACKLISTED = "blacklisted";

    /**
     * 版本号，支持强制所有 token 失效
     */
    public static final String TOKEN_VERSION = "version";

    /**
     * 过期时间，默认为 7 天
     */
    public static final Duration TIMEOUT = Duration.ofHours(24 * 7);

    /**
     * 自定义声明，user 对象
     */
    public static final String CLAIM_DATA = "userData";

    /**
     * 自定义声明，user id
     */
    public static final String CLAIM_USER_ID = "userId";
}
