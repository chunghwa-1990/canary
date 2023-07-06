package com.example.canary.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>响应状态码枚举类</p>
 *
 * @ClassName ResultCodeEnum
 * @Description
 * @Author zhaohongliang
 * @Date 2022-11-24 16:35
 * @Since 1.0
 */
@Getter
@AllArgsConstructor
public enum ResultCodeEnum implements ErrorEnum {


    /**
     * success
     */
    SUCCESS(200, "success"),

    /**
     * Internal Server Error
     */
    ERROR(500, "error"),

    /**
     * Bad Request
     */
    BAD_REQUEST(400, "{ex.missing.request.parameter.message}"),

    /**
     * Bad Request
     */
    BAD_PART_REQUEST(400, "{ex.missing.request.part.message}"),

    /**
     * Unauthorized
     */
    UNAUTHORIZED(401, "Unauthorized"),

    /**
     * forbidden
     */
    FORBIDDEN(403, "forbidden"),

    /**
     * not found
     */
    NOT_FOUND(404, "not found"),

    /**
     * method not allowd
     */
    METHOD_NOT_ALLOWED(405, "method not allowed"),

    // token
    TOKEN_ERROR(401, "{ex.token.error.message}");


    /**
     * 状态码
     */
    private final Integer code;

    /**
     * 信息内容
     */
    private final String message;
}
