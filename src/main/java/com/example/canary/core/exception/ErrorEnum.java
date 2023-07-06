package com.example.canary.core.exception;

/**
 * 错误枚举
 *
 * @ClassName ErrorEnum
 * @Description 错误枚举
 * @Author zhaohongliang
 * @Date 2023-07-06 10:56
 * @Since 1.0
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
