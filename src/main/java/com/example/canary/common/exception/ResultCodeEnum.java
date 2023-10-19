package com.example.canary.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应状态码枚举类
 *
 * @since 1.0
 * @author zhaohongliang
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
    BAD_REQUEST(400, "bad request"),

    /**
     * Bad Request
     */
    BAD_PART_REQUEST(400, "bad part request"),

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
    TOKEN_ERROR(401, "token error or expired"),

    /**
     * 通信故障，连接失败
     */
    MYSQL_CONNECTION_REFUSED(500, "communications link failure, connection refused");

    /**
     * 状态码
     */
    private final Integer code;

    /**
     * 信息内容
     */
    private final String message;
}
