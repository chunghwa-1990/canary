package com.example.canary.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * 日期工具类
 *
 * @since 1.0
 * @author zhaohongliang
 */
public class DateUtils {

    private DateUtils() {

    }

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
