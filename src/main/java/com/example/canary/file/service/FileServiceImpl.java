package com.example.canary.file.service;

import com.example.canary.common.exception.BusinessException;
import com.example.canary.file.entity.FileAO;
import com.example.canary.file.entity.FilePO;
import com.example.canary.file.entity.FileVO;
import com.example.canary.file.repository.FileRepository;
import com.example.canary.util.DigesUtils;
import com.example.canary.util.FileUtils;
import com.example.canary.util.StringUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;

/**
 * 文件
 *
 * @author zhaohongliang 2023-09-23 16:56
 * @since 1.0
 */
@Slf4j
@Service
public class FileServiceImpl implements FileService {

    private final MultipartProperties multipartProperties;

    private final FileRepository fileRepository;

    @Autowired
    public FileServiceImpl(MultipartProperties multipartProperties, FileRepository fileRepository) {
        this.multipartProperties = multipartProperties;
        this.fileRepository = fileRepository;
    }

    /**
     * 文件上传
     *
     * @param file
     * @param fileAo
     * @return
     */
    @Override
    public FileVO uploadFile(MultipartFile file, FileAO fileAo) {
        // 判断文件内容是否为空
        if (file.isEmpty()) {
            throw new BusinessException("文件内容不可以为空");
        }
        // 原文件名称(名称+后缀)
        String originalFilename = file.getOriginalFilename();
        if (!StringUtils.hasText(originalFilename)) {
            throw new BusinessException("文件名字不可以为空");
        }
        // 文件后缀
        String fileSuffix = FileUtils.getFileSuffix(originalFilename);
        // 文件名
        String fileName = StringUtil.randomUUID()  + fileSuffix;
        // 文件下载时用的 key
        String keyName = FileUtils.getKeyName(fileName);
        // 文件磁盘的存储路径
        String filePath = multipartProperties.getLocation() + keyName;
        // 原文件类型
        String contentType = file.getContentType();

        // 文件类型为空或非zip格式
        if (!StringUtils.hasText(contentType) || !(contentType.equals(MediaType.IMAGE_JPEG_VALUE)
                || contentType.equals(MediaType.IMAGE_PNG_VALUE) || contentType.equals(MediaType.parseMediaType("image/bmp").getType())
                || contentType.startsWith("video/"))) {
            throw new BusinessException("不支持" + fileSuffix + "文件类型，支持类型：.jpg .bmp .png 和视屏文件");
        }
        // 原文件大小
        long fileSize = file.getSize();

        File newFile = new File(filePath);
        // 是否存在此文件保存路径
        if (!newFile.getParentFile().exists()) {
            newFile.getParentFile().mkdirs();
        }

        if (!newFile.exists()) {
            try {
                file.transferTo(newFile);
            } catch (IOException e) {
                log.error("上传文件发生异常，异常信息：{}", e.getMessage());
                throw new BusinessException("上传文件发生异常");
            }
        }

        String md5Hex = null;
        String sha256Hex = null;
        try {
            if (Boolean.TRUE.equals(fileAo.getMd5Hex())) {
                md5Hex = DigesUtils.md5DigestAsHex(newFile);
                // md5Hex = DigestUtils.md5DigestAsHex(new FileInputStream(newFile))
            }
            if (Boolean.TRUE.equals(fileAo.getSha256Hex())) {
                sha256Hex = DigesUtils.sha256DigestAsHex(newFile);
            }
        } catch (IOException | NoSuchAlgorithmException e) {
            log.error("计算文件的SHA256/MD5摘要发生异常，异常信息：" + e.getMessage());
            throw new BusinessException("计算文件的SHA256/MD5摘要发生异常，请稍后再试");
        }

        FilePO filePo = new FilePO();
        filePo.setKeyName(keyName);
        filePo.setOriginalFilename(originalFilename);
        filePo.setFileName(fileName);
        filePo.setFileSize(fileSize);
        filePo.setFilePath(filePath);
        filePo.setFileSuffix(fileSuffix);
        filePo.setHumanFileSize(FileUtils.formatHumanFileSize(fileSize));
        filePo.setMd5Hex(md5Hex);
        filePo.setSha256Hex(sha256Hex);
        filePo.setContentType(contentType);
        filePo.setDescription(fileAo.getDescription());
        fileRepository.insert(filePo);

        return new FileVO(filePo);
    }

    /**
     * 文件预览
     *
     * @param response
     * @param keyName
     */
    @Override
    public void viewFile(HttpServletResponse response, String keyName) throws IOException {
        FilePO filePo = fileRepository.selectByKeyName(keyName);
        if (filePo == null) {
            throw new BusinessException("文件不存在");
        }
        response.setHeader("Content-Type", filePo.getContentType());
        response.setHeader("Cache-Control", "no-cache");

        File file = new File(filePo.getFilePath());
        try (FileInputStream fis = new FileInputStream(file); OutputStream os = response.getOutputStream()) {
            IOUtils.copy(fis, os);
        }
    }
}
