package com.example.canary.util;

import java.math.BigInteger;
import java.util.Locale;

/**
 * 加密工具类
 *
 * @author zhaohongliang 2023-09-24 22:28
 * @since 1.0
 */
public class EncryptUtils {

    private EncryptUtils() {
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param bytes
     * @return
     */
    public static String toHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param bytes
     * @return
     */
    private static String toHexStr(byte[] bytes) {
        BigInteger bi = new BigInteger(1, bytes);
        String format = String.format(Locale.ENGLISH, "%%0%dx", bytes.length << 1);
        return String.format(format, bi);
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param bytes
     * @return
     */
    private static String toHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            // Byte.toUnsignedInt(b) 等同于 0xff & b
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        byte[] bytes = {'1', 'b', 'c', 'd', 'e', 'f'};
        System.out.println(EncryptUtils.toHex(bytes));
        System.out.println(EncryptUtils.toHexStr(bytes));
        System.out.println(EncryptUtils.toHexString(bytes));
    }

}