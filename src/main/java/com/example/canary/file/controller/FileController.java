package com.example.canary.file.controller;

import com.example.canary.common.api.ApiVersion;
import com.example.canary.common.exception.ResultEntity;
import com.example.canary.file.entity.FileAO;
import com.example.canary.file.entity.FileVO;
import com.example.canary.file.service.FileService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 文件
 *
 * @author zhaohongliang 2023-09-22 21:33
 * @since 1.0
 */
@Validated
@ApiVersion
@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;

    /**
     * 文件上传
     *
     * @param file
     * @param fileAo
     * @return
     */
    @PostMapping("/upload")
    public FileVO uploadFile(@RequestPart("file") MultipartFile file, FileAO fileAo) {
        return fileService.uploadFile(file, fileAo);
    }

    /**
     * 文件预览
     *
     * @param response
     * @param keyName
     * @throws IOException
     */
    @GetMapping("/view")
    public void viewFile(HttpServletResponse response, @NotBlank @RequestParam String keyName) throws IOException {
        fileService.viewFile(response, keyName);
    }

}
