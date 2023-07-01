package com.example.canary.common.enums;

/**
 * <p>响应状态码枚举类</p>
 *
 * @ClassName ResultCodeEnum
 * @Description
 * @Author zhaohongliang
 * @Date 2022-11-24 16:35
 * @Since 1.0
 */
public enum ResultCodeEnum {

    /**
     * success
     */
    SUCCESS(200, "success"),    // OK

    /**
     * Internal Server Error
     */
    ERROR(500, "error"),    // 服务器内部错误

    /**
     * Bad Request
     */
    BAD_REQUEST(400, "{ex.missing.request.parameter.message}"),    // 错误的请求

    /**
     * Bad Request
     */
    BAD_PART_REQUEST(400, "{ex.missing.request.part.message}"),     // 错误的请求 (文件)

    /**
     * Unauthorized
     */
    UNAUTHORIZED(401, "Unauthorized"),  // 未经授权

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
    METHOD_NOT_ALLOWED(405, "{ex.method.not.supported.message}"),

    /**
     * http client request error
     */
    HTTP_CLIENT_ERROR(500, "{ex.http.client.error.message}"),



    // token
    TOKEN_ERROR(401, "{ex.token.error.message}"),

    // user
    USER_NOT_FOUND(404, "{ex.user.not.fount.message}"),
    USER_PWD_ERROR(500, "{ex.user.pwd.error.message}"),
    USER_ACCOUNT_REPEAT(501, "{ex.user.account.repeat.message}"),

    // file
    FILE_IS_EMPTY(500, "{ex.file.is.empty.message}"),
    FILE_NAME_BLANK(501, "{ex.file.name.is.blank.message}"),

    ;


    /**
     * 状态码
     */
    private Integer code;

    /**
     * 信息内容
     */
    private String message;


    ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }


    /**
     * 获取 状态码
     *
     * @return code 状态码
     */
    public Integer getCode() {
        return this.code;
    }

    /**
     * 设置 状态码
     *
     * @param code 状态码
     */
    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * 获取 信息内容
     *
     * @return message 信息内容
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * 设置 信息内容
     *
     * @param message 信息内容
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
