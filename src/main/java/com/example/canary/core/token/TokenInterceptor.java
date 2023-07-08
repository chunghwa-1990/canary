package com.example.canary.core.token;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.example.canary.core.constant.HeaderConstant;
import com.example.canary.core.exception.BusinessException;
import com.example.canary.core.exception.ResultCodeEnum;
import com.example.canary.core.exception.ResultEntity;
import com.example.canary.sys.entity.UserVO;
import com.example.canary.util.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.Writer;

/**
 * 拦截器
 *
 * @ClassName TokenInterceptor
 * @Description 拦截器
 * @Author zhaohongliang
 * @Date 2023-07-05 18:33
 * @Since 1.0
 */
@Slf4j
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenProperties tokenProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader(HeaderConstant.TOKEN);
        if (!StringUtils.hasText(token) || !JwtUtils.verify(token)) {
            // throw new BusinessException(ResultCodeEnum.TOKEN_ERROR)
            setResponse(request, response, HttpStatus.UNAUTHORIZED, ResultEntity.fail(ResultCodeEnum.TOKEN_ERROR));
            return false;
        }
        String claimStr = JwtUtils.getClaimStr(token, JwtConstant.CLAIM_DATA);
        if (!StringUtils.hasText(claimStr)) {
            setResponse(request, response, HttpStatus.UNAUTHORIZED, ResultEntity.fail(ResultCodeEnum.TOKEN_ERROR));
            return false;
        }
        UserVO userVo = JSONObject.parseObject(claimStr, UserVO.class);
        if (userVo == null) {
            setResponse(request, response, HttpStatus.UNAUTHORIZED, ResultEntity.fail(ResultCodeEnum.TOKEN_ERROR));
            return false;
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

    private static void setResponse(HttpServletRequest request,
                                      HttpServletResponse response, HttpStatus httpStatus, ResultEntity<?> resultEntity) {

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(httpStatus.value());
        try (Writer writer = response.getWriter()) {
            writer.write(JSON.toJSONString(resultEntity));
            writer.flush();
        } catch (IOException e) {
            log.error("response异常:" + e);
        }
    }
}
