package com.example.canary.util;

import com.example.canary.common.exception.BusinessException;
import org.springframework.util.unit.DataSize;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
     * 计算文件的SHA-256摘要
     *
     * @param filePath
     * @return
     */
    public static String sha256(String filePath) {
        try {
            File file = new File(filePath);
            BufferedInputStream bis = new BufferedInputStream(Files.newInputStream(file.toPath()));
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            int bufferSize = 8192;
            byte[] buffer = new byte[bufferSize];
            int sizeRead;
            while ((sizeRead = bis.read(buffer)) != -1) {
                digest.update(buffer, 0, sizeRead);
            }
            bis.close();
            return EncryptUtils.toHex(digest.digest());
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new BusinessException("计算文件的SHA-256摘要发生异常，请稍后重试");
        }
    }


    /**
     * 计算文件的SHA-256摘要
     *
     * @param file
     * @return
     */
    public static String sha256(File file) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            InputStream is = Files.newInputStream(file.toPath());
            DigestInputStream dis = new DigestInputStream(is, digest);
            int bufferSize = 8192;
            byte[] buffer = new byte[bufferSize];
            while (dis.read(buffer) != -1) {
                // do nothing
            }
            dis.close();
            return EncryptUtils.toHex(digest.digest());
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new BusinessException("计算文件的SHA-256摘要发生异常，请稍后重试");
        }
    }

    /**
     * 计算文件的SHA-256摘要
     *
     * @param is
     * @return
     */
    public static String sha256(InputStream is) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            DigestInputStream dis = new DigestInputStream(is, digest);
            int bufferSize = 8192;
            byte[] buffer = new byte[bufferSize];
            while (dis.read(buffer) != -1) {
                // do nothing
            }
            dis.close();
            return EncryptUtils.toHex(digest.digest());
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new BusinessException("计算文件的SHA-256摘要发生异常，请稍后重试");
        }
    }

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
        return "/" + joiner.toString();
    }

    /**
     * 格式化文件大小（人类可以看懂的文件大小）
     *
     * @param fileSize
     * @return
     */
    public static String formatHumanFileSize(long fileSize) {
        DataSize dateSize = DataSize.ofBytes(fileSize);
        if (fileSize >= GB) {
            return dateSize.toGigabytes() + "GB";
        } else if (fileSize >= MB) {
            return dateSize.toMegabytes() + "MB";
        } else if (fileSize >= KB) {
            return dateSize.toKilobytes() + "KB";
        } else {
            return dateSize.toBytes() + "B";
        }
    }

    public static void main(String[] args) {
        String s = "97821574b54abc6c5fa4b134357d4c47914cd2a0d60db47489709c737ad92c46";
        System.out.println(s.length());
    }

}