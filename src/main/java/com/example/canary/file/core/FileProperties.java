package com.example.canary.file.core;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 文件
 *
 * @author zhaohongliang 2023-09-23 23:14
 * @since 1.0
 */
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = FileProperties.PREFIX)
public class FileProperties {

    private FileProperties() {}

    public static final String PREFIX = "file";

    /**
     * 文件存储路径
     */
    private String path;

    /**
     * 上传文件的大小
     */
    private Long maxSize;


}
