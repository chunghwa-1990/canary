package com.example.canary.core.token;

import com.example.canary.core.constant.HeaderConstant;
import com.example.canary.core.context.CanaryContext;
import com.example.canary.core.context.CurrentUser;
import com.example.canary.core.exception.ResultCodeEnum;
import com.example.canary.core.exception.ResultEntity;
import com.example.canary.sys.entity.UserVO;
import com.example.canary.util.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.TimeUnit;

/**
 * 拦截器
 *
 * @since 1.0
 * @author zhaohongliang
 */
@Slf4j
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenProperties tokenProperties;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
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

        // 从redis获取token
        Object object = redisTemplate.opsForValue().get(userId);
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
        String claimStr = JwtUtils.getClaimStr(redisToken, JwtConstant.CLAIM_DATA);
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
        redisTemplate.expire(userId, tokenProperties.getExpires().toSeconds(), TimeUnit.SECONDS);

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, Exception ex) throws Exception {
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
            log.error("response异常:" + e);
        }
    }
}
