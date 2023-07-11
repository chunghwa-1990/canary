package com.example.canary.core.token;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * token properties
 *
 * @ClassName TokenProperties
 * @Description token properties
 * @Author zhaohongliang
 * @Date 2023-07-06 13:28
 * @Since 1.0
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "token" )
public class TokenProperties {

    private TokenProperties() {}

    /**
     * 密钥
     */
    private String secret;

    /**
     * 到期时间
     */
    private Duration expires;
}
