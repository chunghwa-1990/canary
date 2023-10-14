package com.example.canary.file.service;

import com.example.canary.file.entity.FileAO;
import com.example.canary.file.entity.FileVO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
    FileVO uploadFile(MultipartFile file, FileAO fileAo);

    /**
     * 文件预览
     *
     * @param response
     * @param keyName
     */
    void viewFile(HttpServletResponse response, String keyName) throws IOException;
}
