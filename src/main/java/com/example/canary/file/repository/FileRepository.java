package com.example.canary.file.repository;

import com.example.canary.file.entity.FilePO;

/**
 * 文件
 *
 * @author zhaohongliang 2023-09-23 17:16
 * @since 1.0
 */
public interface FileRepository {

    /**
     * insert
     *
     * @param filePo
     * @return
     */
    int insert(FilePO filePo);

    /**
     * 根据文件 keyName 查询
     *
     * @param keyName
     * @return
     */
    FilePO selectByKeyName(String keyName);
}
