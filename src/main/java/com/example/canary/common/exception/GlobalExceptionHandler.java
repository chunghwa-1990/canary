package com.example.canary.common.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 全局统一异常处理
 *
 * @since 1.0
 * @author zhaohongliang
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    /**
     * businessException
     *
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = BusinessException.class)
    public ResultEntity<Object> businessExceptionHandler(BusinessException ex) {
        Integer code = ex.getCode();
        String defaultMessage = ex.getMessage();
        // 根据 message key 查询 i18n 实现国际化
        String message = messageSource.getMessage(defaultMessage, null, defaultMessage, LocaleContextHolder.getLocale());
        return ResultEntity.fail(code, message);
    }


    /**
     * HttpRequestMethodNotSupportedException
     *
     * @param ex
     * @return
     */
    @ResponseBody
    // @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResultEntity<Object> httpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException ex) {
        List<Object> args = new ArrayList<>();
        args.add(ex.getMethod());                   // 参数名称
        args.add(ex.getSupportedHttpMethods());     // 参数类型
        // message key
        String messageKey = ResultCodeEnum.METHOD_NOT_ALLOWED.getMessage();
        // 根据 message key 查询 i18n 实现国际化
        String message = messageSource.getMessage(messageKey, args.toArray(), ex.getMessage(), LocaleContextHolder.getLocale());
        return ResultEntity.fail(ResultCodeEnum.METHOD_NOT_ALLOWED.getCode(), message);
    }

    /**
     * MissingServletRequestParameterException
     *
     * @param ex
     * @return
     */
    @ResponseBody
    // @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResultEntity<Object> missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException ex) {
        List<Object> args = new ArrayList<>();
        args.add(ex.getParameterName());     // 参数名称
        args.add(ex.getParameterType());     // 参数类型
        // message key
        String messageKey = ResultCodeEnum.BAD_REQUEST.getMessage();
        // 根据 message key 查询 i18n 实现国际化
        String message = messageSource.getMessage(messageKey, args.toArray(), ex.getMessage(), LocaleContextHolder.getLocale());
        return ResultEntity.fail(ResultCodeEnum.BAD_REQUEST.getCode(), message);
    }

    /**
     * MissingServletRequestPartException
     *
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = MissingServletRequestPartException.class)
    public ResultEntity<Object> missingServletRequestPartExceptionHandler(MissingServletRequestPartException ex) {
        List<Object> args = new ArrayList<>();
        args.add(ex.getRequestPartName());      // 参数名称
        // message key
        String messageKey = ResultCodeEnum.BAD_PART_REQUEST.getMessage();
        // 根据 message key 查询 i18n 实现国际化
        String message = messageSource.getMessage(messageKey, args.toArray(), ex.getMessage(), LocaleContextHolder.getLocale());
        return ResultEntity.fail(ResultCodeEnum.BAD_REQUEST.getCode(), message);
    }

    /**
     * ConstraintViolationException
     *
     * @param ex
     * @return
     */
    @ResponseBody
    // @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResultEntity<Object> constraintViolationExceptionHandler(ConstraintViolationException ex) {
        Set<ConstraintViolation<?>> constraintViolationSet = ex.getConstraintViolations();
        List<ConstraintViolation<?>> constraintViolations = new ArrayList<>(constraintViolationSet);
        List<Object> args = new ArrayList<>();
        String message = ex.getMessage();
        String messageKey = null;

        if (!CollectionUtils.isEmpty(constraintViolations)) {
            // 第一个参数的 messageKey
            messageKey = constraintViolations.get(0).getMessageTemplate();
            // 方法名.参数名
            String propertyPath = constraintViolations.get(0).getPropertyPath().toString();
            args.add(0, propertyPath);
            // 根据 message key 查询 i18n 实现国际化
            message = messageSource.getMessage(messageKey, args.toArray(), ex.getMessage(), LocaleContextHolder.getLocale());
        }
        return ResultEntity.fail(ResultCodeEnum.BAD_REQUEST.getCode(), message);
    }

    /**
     * MethodArgumentNotValidException Handler
     *
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResultEntity<Object> methodArgumentNotValidHandler(MethodArgumentNotValidException ex) {

        // List<FieldError> allErrors = ex.getBindingResult().getFieldErrors();
        // List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
        // String message = allErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(";"));
        // return ResultEntity.fail(ResultCodeEnum.BAD_REQUEST.getCode(), message);

        FieldError fieldError = ex.getBindingResult().getFieldError();
        if (fieldError == null) {
            return ResultEntity.fail(ResultCodeEnum.BAD_REQUEST.getCode(), ex.getMessage());
        }
        // 字段名称
        String field = fieldError.getField();
        // 注解名称
        String messageKey = Optional.ofNullable(fieldError.getCode()).orElseGet(() -> "");
        // 默认异常提示
        String defauldMessage = fieldError.getDefaultMessage();

        // 提取参数
        List<Object> args = Arrays.stream(Optional.ofNullable(fieldError.getArguments()).orElse(new Object[] {})).filter(o -> !(o instanceof DefaultMessageSourceResolvable)).map(Object::toString).collect(Collectors.toList());
        args.add(0, field);

        // 默认根据注解名返回消息，否则返回默认消息
        String message = messageSource.getMessage(messageKey, args.toArray(), defauldMessage, LocaleContextHolder.getLocale());

        return ResultEntity.fail(ResultCodeEnum.BAD_REQUEST.getCode(), message);

    }

    // @ResponseBody
    // @ResponseStatus(HttpStatus.FORBIDDEN)
    // @ExceptionHandler(value = ForbiddenException.class)
    // public ResultEntity<?> forbiddenExceptionHandler(ForbiddenException ex) {
    //     return ResultEntity.fail(ResultCodeEnum.FORBIDDEN);
    // }


    /**
     * runtimeException
     *
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = { RuntimeException.class })
    public ResultEntity<Object> runtimeExceptionHandler(RuntimeException ex) {
        if (StringUtils.hasText(ex.getMessage())) {
            return ResultEntity.fail(ResultCodeEnum.ERROR.getCode(), ex.getMessage());
        }
        return ResultEntity.fail(ResultCodeEnum.ERROR);
    }
}
