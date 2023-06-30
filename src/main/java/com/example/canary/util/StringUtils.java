package com.example.canary.util;

/**
 * 字符串工具类
 *
 * @ClassName StringUtils
 * @Description 字符串工具类
 * @Author zhaohongliang
 * @Date 2023-06-29 16:57
 * @Since 1.0
 */
public class StringUtils {

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
