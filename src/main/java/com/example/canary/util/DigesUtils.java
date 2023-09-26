package com.example.canary.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author zhaohongliang 2023-09-26 14:18
 * @since 1.0
 */
public class DigesUtils {

    private DigesUtils() {
    }


    /**
     * MD5
     */
    private static final String MD5_ALGORITHM = "MD5";

    /**
     * sha-256
     */
    private static final String SHA256_ALGORITHM = "SHA256";

    /**
     * 计算文件的MD5摘要
     *
     * @param filePath
     * @return
     */
    public static String md5DigestAsHex(String filePath) throws IOException, NoSuchAlgorithmException {
        File file = new File(filePath);
        return sha256DigestAsHex(file);
    }

    /**
     * 计算文件的MD5摘要
     *
     * @param file
     * @return
     */
    public static String md5DigestAsHex(File file) throws IOException, NoSuchAlgorithmException {
        InputStream inputStream = new FileInputStream(file);
        return digestAsHexStr(MD5_ALGORITHM, inputStream);
    }

    /**
     * 计算文件的SHA-256摘要
     *
     * @param filePath
     * @return
     */
    public static String sha256DigestAsHex(String filePath) throws IOException, NoSuchAlgorithmException {
        File file = new File(filePath);
        return sha256DigestAsHex(file);
    }

    /**
     * 计算文件的SHA-256摘要
     *
     * @param file
     * @return
     */
    public static String sha256DigestAsHex(File file) throws IOException, NoSuchAlgorithmException {
        InputStream inputStream = new FileInputStream(file);
        return digestAsHexStr(SHA256_ALGORITHM, inputStream);
    }

    /**
     * 计算文件的摘要
     *
     * @param algorithm
     * @param inputStream
     * @return
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    private static String digestAsHexStr(String algorithm, InputStream inputStream) throws NoSuchAlgorithmException, IOException {
        byte[] digest = digest(algorithm, inputStream);
        return EncryptUtils.toHex(digest);
    }

    /**
     * 计算文件的摘要
     *
     * @param algorithm
     * @param inputStream
     * @return
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    private static byte[] digest(String algorithm, InputStream inputStream) throws NoSuchAlgorithmException, IOException {
        MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
        int bufferSize = 8192;
        byte[] buffer = new byte[bufferSize];
        int sizeRead;
        while ((sizeRead = inputStream.read(buffer)) != -1) {
            messageDigest.update(buffer, 0, sizeRead);
        }
        inputStream.close();
        return messageDigest.digest();
    }

    /**
     * 计算文件的摘要
     *
     * @param algorithm
     * @param inputStream
     * @return
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    private static byte[] digestByte(String algorithm, InputStream inputStream) throws NoSuchAlgorithmException, IOException {
        MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
        DigestInputStream dis = new DigestInputStream(inputStream, messageDigest);
        int bufferSize = 8192;
        byte[] buffer = new byte[bufferSize];
        while (dis.read(buffer) != -1) {
            // do nothing
        }
        dis.close();
        return messageDigest.digest();
    }
}
