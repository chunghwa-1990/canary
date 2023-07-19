package com.example.canary.common.exception;

/**
 * business exception
 *
 * @since 1.0
 * @author zhaohongliang
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
