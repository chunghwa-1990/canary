package com.example.canary.file.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 文件
 *
 * @author zhaohongliang 2023-09-22 21:21
 * @since 1.0
 */
@Data
public class FileBase implements Serializable {

    @Serial
    private static final long serialVersionUID = 3451033033861765191L;

    /**
     * ID
     */
    private String id;

    /**
     * 下载时用的key
     */
    private String keyName;

    /**
     * 原始文件名
     */
    private String originalFilename;

    /**
     * 磁盘中存储的文件名
     */
    private String fileName;

    /**
     * 磁盘中存储的文件路径
     */
    private String filePath;

    /**
     * 文件大小，单位b(字节)， 1KB=1024B
     */
    private Long fileSize;

    /**
     * 文件大小, 100 B,123 KB,100 MB,100 GB
     */
    private String humanFileSize;

    /**
     * 文件后缀类型
     */
    private String fileSuffix;

    /**
     * md5 摘要计算
     */
    private String md5Hex;

    /**
     * sha256 摘要计算
     */
    private String sha256Hex;

    /**
     * 文件内容类型
     */
    private String contentType;

    /**
     * 业务上用的对该文件的描述
     */
    private String description;
}
