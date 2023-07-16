package com.example.canary.util;

import java.util.UUID;

/**
 * 字符串工具类
 *
 * @since 1.0
 * @author zhaohongliang
 */
public class StringUtils {

    private StringUtils() {

    }

    /**
     * 生成UUID
     *
     * @return
     */
    public static String randomUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 小驼峰命名
     *
     * @return
     */
    public static String toLowerCamelCase(String str) {
        char[] chars = str.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }

    public static String toUpperCamelCase(String str) {
        return null;
    }
}
