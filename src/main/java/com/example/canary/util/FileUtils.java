package com.example.canary.util;

import java.time.LocalDate;
import java.util.StringJoiner;

/**
 * 文件工具类
 *
 * @author zhaohongliang 2023-09-23 19:32
 * @since 1.0
 */
public class FileUtils {

    private FileUtils() {
    }

    /**
     * 千字节 1KB=1024B
     */
    private static final long KB = 1024;

    /**
     * 兆字节 1MB=1024KB
     */
    private static final long MB = 1024 * KB;

    /**
     * 千兆字节 1GB=1024MB
     */
    private static final long GB = 1024 * MB;

    /**
     * 获取文件名称
     *
     * @param originalFileName
     * @return
     */
    public static String getFileName(String originalFileName) {
        return originalFileName.substring(0, originalFileName.lastIndexOf("."));
    }

    /**
     * 获取文件后缀
     *
     * @param originalFileName
     * @return
     */
    public static String getFileSuffix(String originalFileName) {
        return originalFileName.substring(originalFileName.lastIndexOf("."));
    }

    /**
     * 获取文件的存放路径
     *
     * @param fileName
     * @return
     */
    public static String getKeyName(String fileName) {
        StringJoiner joiner = new StringJoiner("/");
        joiner.add(LocalDate.now().getYear() + "");
        joiner.add(LocalDate.now().getMonthValue() + "");
        joiner.add(LocalDate.now().getDayOfMonth() + "");
        joiner.add(fileName);
        return joiner.toString();
    }

}