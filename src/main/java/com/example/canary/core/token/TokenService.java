package com.example.canary.core.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.canary.sys.entity.UserBase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * token
 *
 * @author zhaohongliang 2023-07-17 00:23
 * @since 1.0
 */
@Slf4j
public record TokenService(String secret, Duration timeout) {

    public static TokenService create(TokenProperties tokenProperties) {
        return new TokenService(tokenProperties.getSecret(), tokenProperties.getTimeout());
    }

    /**
     * 创建token
     *
     * @return
     */
    public String createJwtToken(UserBase userBase) throws JsonProcessingException {

        // aud
        String audience = userBase.getId();
        // 载荷
        String claim = new ObjectMapper().writeValueAsString(userBase);

        // header
        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "HS256");

        // 加密算法
        Algorithm algorithm = Algorithm.HMAC256(secret);

        return JWT.create()
                // header
                .withHeader(header)
                // payload
                .withAudience(audience)
                .withClaim(TokenConstant.CLAIM_DATA, claim)
                // sign
                .sign(algorithm);
    }


}
