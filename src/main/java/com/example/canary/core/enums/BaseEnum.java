package com.example.canary.core.enums;

/**
 * 枚举接口
 *
 * @since 1.0
 * @author zhaohongliang
 */
public interface BaseEnum {

    /**
     * 获取状态码
     *
     * @return code
     */
    Integer getCode();

    /**
     * 获取描述
     *
     * @return message
     */
    String getDescription();

}
