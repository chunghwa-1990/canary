package com.example.canary.common.exception;

/**
 * 错误枚举
 *
 * @since 1.0
 * @author zhaohongliang
 */
public interface ErrorEnum {

    /**
     * 获取状态码
     *
     * @return code
     */
    Integer getCode();

    /**
     * 获取错误信息
     *
     * @return message
     */
    String getMessage();


}
