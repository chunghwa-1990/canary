package com.example.canary.core.exception;


/**
 * 自定义异常抽象类
 *
 * @since 1.0
 * @author zhaohongliang
 */
public abstract class BaseException extends RuntimeException {

    private final Integer code;
    /**
     * 无参构造器通用异常
     */
    BaseException() {
        super(ResultCodeEnum.ERROR.getMessage());
        this.code = ResultCodeEnum.ERROR.getCode();
    }

    /**
     * 指定message通用异常
     *
     * @param message
     */
    BaseException(String message) {
        super(message);
        this.code = ResultCodeEnum.ERROR.getCode();
    }

    /**
     * 指定code通用异常
     *
     * @param errorEnum
     */
    BaseException(ErrorEnum errorEnum) {
        super(errorEnum.getMessage());
        this.code = errorEnum.getCode();
    }

    /**
     * 指定code通用异常（不推荐使用此构造器定义异常，此后可能被废弃，推荐在ResultCodeEnum中定义code和message自定义BaseException）
     *
     * @param code
     * @param message
     */
    // @Deprecated(since = "1.0", forRemoval = true)
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
        this.code = ResultCodeEnum.ERROR.getCode();
    }

    /**
     * 指定code和cause通用异常
     *
     * @param errorEnum
     * @param cause
     */
    BaseException(ErrorEnum errorEnum, Throwable cause) {
        super(cause);
        this.code = errorEnum.getCode();
    }

    /**
     * 指定message和cause通用异常
     *
     * @param message
     * @param cause
     */
    BaseException(String message, Throwable cause) {
        super(message, cause);
        this.code = ResultCodeEnum.ERROR.getCode();
    }

    /**
     * 指定message和cause通用异常（不推荐使用此构造器定义异常，此后可能被废弃，推荐在ResultCodeEnum中定义code和message自定义BaseException）
     *
     * @param code
     * @param message
     * @param cause
     */
    // @Deprecated(since = "1.0", forRemoval = true)
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
