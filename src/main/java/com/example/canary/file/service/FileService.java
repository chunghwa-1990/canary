package com.example.canary.file.service;

import com.example.canary.common.exception.ResultEntity;
import com.example.canary.file.entity.FileAO;
import com.example.canary.file.entity.FileVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件
 *
 * @author zhaohongliang 2023-09-23 16:56
 * @since 1.0
 */
public interface FileService {

    /**
     * 文件上传
     *
     * @param file
     * @param fileAo
     * @return
     */
    ResultEntity<FileVO> uploadFile(MultipartFile file, FileAO fileAo);
}
