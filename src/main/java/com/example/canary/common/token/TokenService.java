package com.example.canary.common.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.canary.common.redis.RedisService;
import com.example.canary.sys.entity.UserBase;
import com.example.canary.util.StringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * token
 *
 * @author zhaohongliang 2023-07-17 00:23
 * @since 1.0
 */
@Slf4j
@Component
public class TokenService {

    @Autowired
    private TokenProperties tokenProperties;

    @Autowired
    private RedisService redisService;

    /**
     * 创建token
     *
     * @return
     */
    public String generateJwtToken(UserBase userBase) throws JsonProcessingException {

        // subject
        String subject = userBase.getId();
        // 自定义声明
        // String userDataCliam = JacksonUtils.getObjectMapper().writeValueAsString(userBase);
        // 当前时间
        long currentTimeMillis = System.currentTimeMillis();
        // 过期时间
        Date expiresAt = new Date(currentTimeMillis + TokenConstant.TIMEOUT.toMillis());
        // 签发时间
        Date issuedAt = new Date(currentTimeMillis);
        // 生效时间
        Date notBefore = new Date(currentTimeMillis);
        // jwtId
        String jwtId = StringUtil.randomUUID();

        // header
        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");   // token 类型
        header.put("alg", "HS256"); // 签名算法
        header.put("kid", "");  // 自定义字段

        // 加密算法
        Algorithm algorithm = Algorithm.HMAC256(tokenProperties.getSecretKey());

        // token
        String token = JWT.create()
                .withSubject(subject) // 设置主题
                .withIssuer(tokenProperties.getIssuer()) // 设置签发者
                .withAudience(tokenProperties.getAudience()) // 设置受众
                .withExpiresAt(expiresAt) // 设置过期时间
                .withIssuedAt(issuedAt) // 设置签发时间
                .withNotBefore(notBefore) // 设置生效时间
                // .withClaim(TokenConstant.CLAIM_DATA, claim)
                // .withClaim(HeaderConstant.DEVICE_ID, "4f792fbb0e0f4b658c6a4ca58569a845")
                .withClaim(TokenConstant.TOKEN_VERSION, tokenProperties.getVersion()) // 版本号
                .withClaim(TokenConstant.CLAIM_USER_ID, subject) // userId
                .withHeader(header) // 添加自定义 header
                .withJWTId(jwtId)  // 设置 JWT 唯一标识
                .sign(algorithm);

        // tokenKey
        String tokenKey = StringUtil.createRedisKey(TokenConstant.TOKEN, jwtId);
        // 缓存到 redis
        redisService.set(tokenKey, token, tokenProperties.getTimeout());

        return token;
    }

    /**
     * 加入黑名单
     *
     * @param token
     */
    public void addToBlacklist(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        // 过期时间
        Date expiresAt = decodedJWT.getExpiresAt();
        // token 剩余的有效期
        long ttl = expiresAt.getTime() - System.currentTimeMillis();
        if (ttl > 0) {
            // 以 Token 为 Key，过期时间设置为 Token 剩余的有效期
            redisService.set(token, TokenConstant.BLACKLISTED, Duration.ofMillis(ttl));
        }
    }

    /**
     * 黑名单校验
     *
     * @param token
     * @return
     */
    public boolean isTokenBlackListed(String token) {
        Object object = redisService.get(token);
        if (Objects.isNull(object)) {
            return false;
        }
        return true;
    }

}
