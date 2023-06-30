package com.example.canary.util;

import java.time.LocalDate;

/**
 * 测试
 *
 * @ClassName TestUtil
 * @Description 测试
 * @Author zhaohongliang
 * @Date 2023-06-28 09:03
 * @Since 1.0
 */
public class TestUtil {

    public static void main(String[] args) {

        LocalDate date = LocalDate.of(2022, 4, 3);
        LocalDate planDate = date.plusDays(Math.round(6000 / 450 * 30.5));
        System.out.println(planDate);
        LocalDate now = LocalDate.of(2023, 6, 27);
        LocalDate planDate1 = now.plusDays((int) Math.round((double) (117835 - 117735) / 450 * 30.5));
        System.out.println(planDate1);

    }
}
