package com.example.canary.common.token;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * token properties
 *
 * @since 1.0
 * @author zhaohongliang
 */
@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = TokenProperties.PREFIX, ignoreUnknownFields = false)
public class TokenProperties {

    // private TokenProperties() {}

    public static final String PREFIX = "token";

    /**
     * 签发者
     */
    private String issuer;

    /**
     * 受众
     */
    private String[] audience;

    /**
     * 密钥
     */
    private String secretKey;

    /**
     * 过期时间, 默认: 7200000 毫秒
     */
    // @DurationUnit(value = ChronoUnit.SECONDS)
    private Duration timeout = Duration.ofMillis(7200000);

    /**
     * token version
     */
    private Integer version = 1;

}
