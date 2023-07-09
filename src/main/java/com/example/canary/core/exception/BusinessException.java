package com.example.canary.core.exception;

/**
 * business exception
 *
 * @ClassName BusinessException
 * @Description business exception
 * @Author zhaohongliang
 * @Date 2023-07-02 23:10
 * @Since 1.0
 */
public class BusinessException extends BaseException {

    public BusinessException() {
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(ErrorEnum errorEnum) {
        super(errorEnum);
    }
}
