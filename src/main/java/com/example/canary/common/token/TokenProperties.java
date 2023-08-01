package com.example.canary.common.token;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.Date;

/**
 * token properties
 *
 * @since 1.0
 * @author zhaohongliang
 */
@Setter
@Getter
@ConfigurationProperties(prefix = TokenProperties.PREFIX )
public class TokenProperties {

    private TokenProperties() {}

    public static final String PREFIX = "token";

    /**
     * 密钥
     */
    private String secret;

    /**
     * 过期时间, 默认: 7200000 毫秒
     */
    // @DurationUnit(value = ChronoUnit.SECONDS)
    private Duration timeout = Duration.ofMillis(7200000);

    /**
     * Initialize a {@link TokenBuilder} with the state of this instance.
     * @return a {@link TokenBuilder} initialized with the customizations defined on
     * this instance
     */
    public TokenBuilder initializeTokenBuilder() {
        return TokenBuilder.create(this);
    }
}
