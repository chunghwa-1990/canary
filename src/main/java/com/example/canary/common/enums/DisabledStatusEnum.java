package com.example.canary.common.enums;

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

    FALSE(0, "否"),
    TRUE(1, "是");

    private Integer code;

    private String message;
}
