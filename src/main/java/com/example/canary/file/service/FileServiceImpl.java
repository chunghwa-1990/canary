package com.example.canary.file.service;

import com.example.canary.common.exception.ResultEntity;
import com.example.canary.file.entity.FileAO;
import com.example.canary.file.entity.FilePO;
import com.example.canary.file.entity.FileVO;
import com.example.canary.file.repository.FileRepository;
import com.example.canary.util.DigesUtils;
import com.example.canary.util.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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

    @Autowired
    private MultipartProperties multipartProperties;

    @Autowired
    private FileRepository fileRepository;


    /**
     * 文件上传
     *
     * @param file
     * @param fileAo
     * @return
     */
    @Override
    public ResultEntity<FileVO> uploadFile(MultipartFile file, FileAO fileAo) {
        // 判断文件内容是否为空
        if (file.isEmpty()) {
            return ResultEntity.fail("文件内容不可以为空");
        }
        // 原文件名称(名称+后缀)
        String originalFilename = file.getOriginalFilename();
        if (!StringUtils.hasText(originalFilename)) {
            return ResultEntity.fail("文件名字不可以为空");
        }
        // 文件后缀
        String fileSuffix = FileUtils.getFileSuffix(originalFilename);
        // 文件名
        String fileName = com.example.canary.util.StringUtils.randomUUID()  + fileSuffix;
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
            return ResultEntity.fail("不支持" + fileSuffix + "文件类型，支持类型：.jpg .bmp .png 和视屏文件");
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
                return ResultEntity.fail("上传文件发生异常");
            }
        }

        String md5Hex = null;
        String sha256Hex = null;
        try {
            if (fileAo.getMd5Hex()) {
                md5Hex = DigesUtils.md5DigestAsHex(newFile);
                // md5Hex = DigestUtils.md5DigestAsHex(new FileInputStream(newFile))
            }
            if (fileAo.getSha256Hex()) {
                sha256Hex = DigesUtils.sha256DigestAsHex(newFile);
            }
        } catch (IOException | NoSuchAlgorithmException e) {
            log.error("计算文件的SHA256/MD5摘要发生异常:" + e.getMessage());
            return ResultEntity.fail("计算文件的SHA256/MD5摘要发生异常，请稍后再试");
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

        return ResultEntity.success(new FileVO(filePo));
    }
}
