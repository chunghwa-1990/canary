package com.example.canary.file.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 文件
 *
 * @author zhaohongliang 2023-09-22 21:31
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FileAO extends FileBase {

    @Serial
    private static final long serialVersionUID = -96207850969671216L;

}
