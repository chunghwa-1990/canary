package com.example.canary.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * jwt工具类
 *
 * @since 1.0
 * @author zhaohongliang
 */
public class JwtUtils {

    private static final String TOKEN_VERSION = "version";

    private JwtUtils() {

    }

    /**
     * 创建 token
     *
     * @param subject 主题
     * @param issuer 签发着
     * @param secretKeyKey 密钥
     * @param expires 过期时间
     * @param claim 自定义声明
     * @param audience 受众
     * @return
     */
    public static String generateJwtToken(String subject, String issuer, String secretKeyKey, Duration expires,
                                        Integer claim, String... audience) {

        // 当前时间
        long currentTimeMillis = System.currentTimeMillis();
        // 过期时间
        Date expiresAt = new Date(currentTimeMillis + expires.toMillis());
        // 签发时间
        Date issuedAt = new Date(currentTimeMillis);
        // 生效时间
        Date notBefore = new Date(currentTimeMillis + 30000);
        // jwtId
        String jwtId = StringUtil.randomUUID();

        // header
        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");   // token 类型
        header.put("alg", "HS256"); // 签名算法
        header.put("pid", "");  // 自定义字段

        // 加密算法
        Algorithm algorithm = Algorithm.HMAC256(secretKeyKey);

        return JWT.create()
                .withSubject(subject) // 设置主题
                .withIssuer(issuer) // 设置签发者
                .withAudience(audience) // 设置受众
                .withExpiresAt(expiresAt) // 设置过期时间
                .withIssuedAt(issuedAt) // 设置签发时间
                .withNotBefore(notBefore) // 设置生效时间
                .withHeader(header) // 添加自定义 header
                .withClaim(TOKEN_VERSION, claim) // 添加自定义声明
                .withHeader(header) // 添加自定义 header
                .withJWTId(jwtId)  // 设置 JWT 唯一标识
                .sign(algorithm);
    }

    /**
     * 校验签名
     *
     * @param secretKey
     * @param token
     * @return
     */
    public static DecodedJWT verifySignature(String secretKey, String token) throws JWTVerificationException {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }

    /**
     * 校验是否过期
     *
     * @param expiresAt
     * @return
     */
    public static boolean isExpired(Date expiresAt) {
        // 如果过期时间在当前时间之前，则表示已过期
        return expiresAt.before(new Date());
    }

    /**
     * 校验是否已生效
     *
     * @param notBefore
     * @return
     */
    public static boolean isNotBefore(Date notBefore) {
        // 如果生效时间在当前时间之后，则表示未生效
        return notBefore.after(new Date());
    }

    public static void main(String[] args) throws InterruptedException {

        String secretKey = "test1234";
        Duration expires = Duration.ofMillis(1000);

        String token = JwtUtils.generateJwtToken("菜10", "cai-app", secretKey, expires, 1,"123456", "000");
        System.out.println(token);
        Thread.sleep(2000);
        DecodedJWT decodedJWT = JwtUtils.verifySignature(secretKey, token);
        String subject = decodedJWT.getSubject();
        Date issuedAt = decodedJWT.getIssuedAt();
        Date notBefore = decodedJWT.getNotBefore();
    }
}
