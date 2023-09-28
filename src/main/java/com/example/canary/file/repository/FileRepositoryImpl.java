package com.example.canary.file.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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

    /**
     * 根据文件 keyName 查询
     *
     * @param keyName
     * @return
     */
    @Override
    public FilePO selectByKeyName(String keyName) {
        LambdaQueryWrapper<FilePO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FilePO::getKeyName, keyName);
        return fileMapper.selectOne(queryWrapper);
    }
}
