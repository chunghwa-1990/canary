package com.example.canary.core.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    private String secret;

    private Long expires;

    private String audience;

    private String claim;
    public String build() {
        // 有效起始时间
        Date beginTime = new Date();
        // 有效结束时间
        Date endTime = new Date(System.currentTimeMillis() + this.expires);

        // Header
        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "HS256");

        // 加密算法
        Algorithm algorithm = Algorithm.HMAC256(secret);

        return JWT.create().withHeader(header)
                .withAudience(audience)
                .withIssuedAt(beginTime)
                .withExpiresAt(endTime)
                .withClaim(JwtConstant.CLAIM_DATA, claim)
                .sign(algorithm);
    }
}
