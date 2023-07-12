package com.example.canary.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 状态枚举
 *
 * @since 1.0
 * @author zhaohongliang
 */
public class StatusEnum {

    @Getter
    @AllArgsConstructor
    public enum Disabled implements BaseEnum {

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
         * 描述
         */
        private final String description;
    }


    @Getter
    @AllArgsConstructor
    public enum Deleted implements BaseEnum {
        /**
         * 否
         */
        FALSE(0, "否");
        /**
         * 状态码
         */
        private final Integer code;

        /**
         * 信息内容
         */
        private final String description;

    }
}
