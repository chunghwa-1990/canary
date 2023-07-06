package com.example.canary.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 禁用状态
 *
 * @ClassName DisabledStatusEnum
 * @Description 禁用状态
 * @Author zhaohongliang
 * @Date 2023-06-26 17:28
 * @Since 1.0
 */
@Getter
@AllArgsConstructor
public enum DisabledStatusEnum implements BaseEnum {

    /**
     * 否
     */
    FALSE(0, "否"),

    /**
     * 是
     */
    TRUE(1, "是");

    /**
     * 状态码
     */
    private final Integer code;

    /**
     * 信息内容
     */
    private final String message;
}
