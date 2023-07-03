package com.example.canary.exception;

import com.example.canary.common.enums.BaseEnum;
import com.example.canary.common.enums.ResultCodeEnum;

/**
 * 自定义异常抽象类
 *
 * @ClassName BaseException
 * @Description 自定义异常抽象类
 * @Author zhaohongliang
 * @Date 2023-07-02 17:48
 * @Since 1.0
 */
public abstract class BaseException extends RuntimeException {

    private final Integer code;
    /**
     * 无参构造器通用异常
     */
    BaseException() {
        super(ResultCodeEnum.Common.ERROR.getMessage());
        this.code = ResultCodeEnum.Common.ERROR.getCode();
    }

    /**
     * 指定message通用异常
     *
     * @param message
     */
    BaseException(String message) {
        super(message);
        this.code = ResultCodeEnum.Common.ERROR.getCode();
    }

    /**
     * 指定code通用异常
     *
     * @param codeEnum
     */
    BaseException(BaseEnum codeEnum) {
        super(codeEnum.getMessage());
        this.code = codeEnum.getCode();
    }

    /**
     * 指定code通用异常（不推荐使用此构造器定义异常，此后可能被废弃，推荐在ResultCodeEnum中定义code和message自定义BaseException）
     *
     * @param code
     * @param message
     */
    @Deprecated(since = "1.0", forRemoval = true)
    BaseException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 指定cause通用异常
     *
     * @param cause 导火索
     */
    BaseException(Throwable cause) {
        super(cause);
        this.code = ResultCodeEnum.Common.ERROR.getCode();
    }

    /**
     * 指定code和cause通用异常
     *
     * @param codeEnum
     * @param cause
     */
    BaseException(BaseEnum codeEnum, Throwable cause) {
        super(cause);
        this.code = codeEnum.getCode();
    }

    /**
     * 指定message和cause通用异常
     *
     * @param message
     * @param cause
     */
    BaseException(String message, Throwable cause) {
        super(message, cause);
        this.code = ResultCodeEnum.Common.ERROR.getCode();
    }

    /**
     * 指定message和cause通用异常（不推荐使用此构造器定义异常，此后可能被废弃，推荐在ResultCodeEnum中定义code和message自定义BaseException）
     *
     * @param code
     * @param message
     * @param cause
     */
    @Deprecated(since = "1.0", forRemoval = true)
    BaseException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    /**
     * 获取 错误码
     *
     * @return code 错误码
     */
    public Integer getCode() {
        return this.code;
    }
}
