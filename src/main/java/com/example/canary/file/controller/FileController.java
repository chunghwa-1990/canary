package com.example.canary.file.controller;

import com.example.canary.common.api.ApiVersion;
import com.example.canary.common.exception.ResultEntity;
import com.example.canary.file.entity.FileVO;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotBlank;
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


    /**
     * 文件上传
     *
     * @param file
     * @param description
     * @return
     */
    @PostMapping("/upload")
    public ResultEntity<FileVO> uploadFile(@RequestPart("file") MultipartFile file, String description) {
        return null;
    }

    @GetMapping("/view")
    public void viewFile(HttpServletResponse response, @NotBlank @RequestParam String keyName) throws IOException {
        // fileService.viewFile(response, keyName);
    }

}
