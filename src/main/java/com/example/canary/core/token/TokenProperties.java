package com.example.canary.core.token;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * token properties
 *
 * @since 1.0
 * @author zhaohongliang
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
    // @DurationUnit(value = ChronoUnit.SECONDS)
    private Duration expires;
}
