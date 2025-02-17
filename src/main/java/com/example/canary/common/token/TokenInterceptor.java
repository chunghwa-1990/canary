package com.example.canary.common.token;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.canary.common.constant.HeaderConstant;
import com.example.canary.common.context.CanaryContext;
import com.example.canary.common.context.CurrentUser;
import com.example.canary.common.exception.ResultCodeEnum;
import com.example.canary.common.exception.ResultEntity;
import com.example.canary.common.redis.RedisService;
import com.example.canary.util.JacksonUtils;
import com.example.canary.util.JwtUtils;
import com.example.canary.util.StringUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.Writer;
import java.time.Duration;
import java.util.Date;
import java.util.Objects;

/**
 * 拦截器
 *
 * @since 1.0
 * @author zhaohongliang
 */
@Slf4j
public class TokenInterceptor implements HandlerInterceptor {

    @Value("${token.secret-key}")
    private String secretKey;

    @Value("${token.timeout}")
    private Duration timeout;

    @Value("${token.version}")
    private Integer tokenVersion;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RedisService redisService;

    @Override
    public boolean preHandle(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler) throws Exception {

        // token
        String token = request.getHeader(HeaderConstant.TOKEN);
        // 校验 token 是否为空
        if (!StringUtils.hasText(token)) {
            setResponse(response, ResultEntity.fail(ResultCodeEnum.TOKEN_ERROR));
            return false;
        }

        // 1.签名校验
        DecodedJWT decodedJWT = null;
        try {
            decodedJWT = JwtUtils.verifySignature(secretKey, token);
        } catch (JWTVerificationException e) {
            setResponse(response, ResultEntity.fail(ResultCodeEnum.TOKEN_ERROR));
            return false;
        }

        // 2.有效期校验
        Date expiresAt = decodedJWT.getExpiresAt(); // 过期时间
        Date notBefore = decodedJWT.getNotBefore(); // 生效时间
        if (JwtUtils.isExpired(expiresAt) || JwtUtils.isNotBefore(notBefore)) {
            setResponse(response, ResultEntity.fail(ResultCodeEnum.TOKEN_ERROR));
            return false;
        }

        // 3.黑名单校验
        if (tokenService.isTokenBlackListed(token)) {
            // response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token 已失效");
            setResponse(response, ResultEntity.fail(ResultCodeEnum.TOKEN_ERROR));
            return false;
        }

        // 4.白名单校验
        String jwtId = decodedJWT.getId(); // jwtId
        if (!StringUtils.hasText(jwtId)) {
            setResponse(response, ResultEntity.fail(ResultCodeEnum.TOKEN_ERROR));
            return false;
        }
        String tokenKey = StringUtil.createRedisKey(TokenConstant.TOKEN, jwtId); // token key
        Object object = redisService.get(tokenKey); // 从 redis 查询 token
        if (Objects.isNull(object)) {
            setResponse(response, ResultEntity.fail(ResultCodeEnum.TOKEN_ERROR));
            return false;
        }
        ObjectMapper objectMapper = JacksonUtils.getObjectMapper();
        String redisToken = objectMapper.convertValue(object, String.class);
        if (!redisToken.equals(token)) {  // 和白名单是否一致
            setResponse(response, ResultEntity.fail(ResultCodeEnum.TOKEN_ERROR));
            return false;
        }

        // 5.版本号校验
        int storedTokenVersion = decodedJWT.getClaim(TokenConstant.TOKEN_VERSION).asInt();
        if (storedTokenVersion != tokenVersion) {
            setResponse(response, ResultEntity.fail(ResultCodeEnum.TOKEN_ERROR));
            return false;
        }

        // 自定义声明 userData 信息
        // String userDataClaim = decodedJWT.getClaim(TokenConstant.CLAIM_DATA).asString();
        // UserVO userVo = objectMapper.readValue(userDataClaim, UserPO.class).convertToVo();

        // 自定义声明 userId
        String userId = decodedJWT.getClaim(TokenConstant.CLAIM_USER_ID).asString();
        // 把 token 的自定义信息赋值给 ThreadLocal<CurrentUser>
        CurrentUser<?> currentUser = new CurrentUser<>(userId);
        CanaryContext.setCurrentUser(currentUser);

        // token 续期
        redisService.expire(jwtId, timeout);

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler, @Nullable Exception ex) {
        CanaryContext.removeCurrentUser();
    }

    private static void setResponse(HttpServletResponse response, ResultEntity<?> resultEntity) {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        try (Writer writer = response.getWriter()) {
            ObjectMapper objectMapper = new ObjectMapper();
            writer.write(objectMapper.writeValueAsString(resultEntity));
            writer.flush();
        } catch (IOException e) {
            log.error("response异常，异常信息：" + e);
        }
    }
}
