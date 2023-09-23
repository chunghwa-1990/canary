package com.example.canary.file.repository;

import com.example.canary.file.entity.FilePO;
import com.example.canary.file.mapper.FileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 文件
 *
 * @author zhaohongliang 2023-09-23 17:17
 * @since 1.0
 */
@Service
public class FileRepositoryImpl implements FileRepository {

    @Autowired
    private FileMapper fileMapper;

    /**
     * insert
     *
     * @param filePo
     * @return
     */
    @Override
    public int insert(FilePO filePo) {
        return fileMapper.insert(filePo);
    }
}
