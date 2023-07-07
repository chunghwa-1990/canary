package com.example.canary.core.enums;

/**
 * 枚举接口
 *
 * @ClassName BaseEnum
 * @Description 枚举接口
 * @Author zhaohongliang
 * @Date 2023-07-02 21:00
 * @Since 1.0
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
