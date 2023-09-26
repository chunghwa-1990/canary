package com.example.canary.file.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 文件
 *
 * @author zhaohongliang 2023-09-22 21:31
 * @since 1.0
 */
@Data
public class FileAO implements Serializable {

    @Serial
    private static final long serialVersionUID = -96207850969671216L;

    private String description;

    /**
     * 是否计算文件的md5摘要
     * true:  是
     * false: 否
     */
    private Boolean md5Hex = false;

    /**
     * 是否计算文件的sha256摘要
     * true:  是
     * false: 否
     */
    private Boolean sha256Hex = false;
}
