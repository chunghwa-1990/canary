package com.example.canary.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * 日期工具类
 *
 * @ClassName DateUtils
 * @Description 日期工具类
 * @Author zhaohongliang
 * @Date 2023-06-30 00:03
 * @Since 1.0
 */
public class DateUtils {

    /**
     * Instant convert to LocalDateTime
     *
     * @param instant
     * @return
     */
    public static LocalDateTime toLocalDateTime(Instant instant) {
        ZoneId zoneId = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zoneId);
    }
}
