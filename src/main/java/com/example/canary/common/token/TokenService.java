package com.example.canary.common.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.canary.sys.entity.UserBase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

/**
 * token
 *
 * @author zhaohongliang 2023-07-17 00:23
 * @since 1.0
 */
@Slf4j
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TokenService {

    /**
     * 密钥
     */
    private String secret;

    /**
     * 过期时间
     */
    private Duration timeout;

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

    /**
     * 创建 tokenkey
     *
     * @param subs
     * @return
     */
    public String createTokenKey(String ... subs) {
        if (subs == null || subs.length <= 0) {
            throw new IllegalArgumentException("redis的key拼接异常");
        }
        StringJoiner joiner = new StringJoiner(":");
        joiner.add("com.example.canary").add("token");
        for (String sub : subs) {
            joiner.add(sub);
        }
        return joiner.toString();
    }


}
