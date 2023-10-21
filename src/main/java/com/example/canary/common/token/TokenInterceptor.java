package com.example.canary.common.token;

import com.example.canary.common.constant.HeaderConstant;
import com.example.canary.common.context.CanaryContext;
import com.example.canary.common.context.CurrentUser;
import com.example.canary.common.exception.ResultCodeEnum;
import com.example.canary.common.exception.ResultEntity;
import com.example.canary.common.redis.RedisService;
import com.example.canary.sys.entity.UserVO;
import com.example.canary.util.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.Writer;

/**
 * 拦截器
 *
 * @since 1.0
 * @author zhaohongliang
 */
@Slf4j
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RedisService redisService;

    @Override
    public boolean preHandle(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler) throws Exception {
        // token
        String token = request.getHeader(HeaderConstant.TOKEN);

        // 校验token
        if (!StringUtils.hasText(token)) {
            setResponse(response, ResultEntity.fail(ResultCodeEnum.TOKEN_ERROR));
            return false;
        }

        // userId
        String userId = JwtUtils.getAudience(token, 0);
        if (!StringUtils.hasText(userId)) {
            setResponse(response, ResultEntity.fail(ResultCodeEnum.TOKEN_ERROR));
            return false;
        }

        // token key
        String tokenKey = tokenService.createTokenKey(userId);
        // 从redis获取token
        Object object = redisService.get(tokenKey);
        if (object == null) {
            setResponse(response, ResultEntity.fail(ResultCodeEnum.TOKEN_ERROR));
            return false;
        }

        // 校验是否一致
        ObjectMapper objectMapper = new ObjectMapper();
        String redisToken = objectMapper.convertValue(object, String.class);
        if (!token.equals(redisToken)) {
            setResponse(response, ResultEntity.fail(ResultCodeEnum.TOKEN_ERROR));
            return false;
        }

        // 载荷
        String claimStr = JwtUtils.getClaimStr(redisToken, TokenConstant.CLAIM_DATA);
        if (!StringUtils.hasText(claimStr)) {
            setResponse(response, ResultEntity.fail(ResultCodeEnum.TOKEN_ERROR));
            return false;
        }

        // user
        UserVO userVo = objectMapper.readValue(claimStr, UserVO.class);
        if (userVo == null) {
            setResponse(response, ResultEntity.fail(ResultCodeEnum.TOKEN_ERROR));
            return false;
        }

        // 把redis中token的载荷赋值给 ThreadLocal<CurrentUser>
        CurrentUser<UserVO> currentUser = new CurrentUser<>(userVo);
        CanaryContext.setCurrentUser(currentUser);

        // token续期
        redisService.expire(userId, tokenService.getTimeout());

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
