package com.example.canary.sys.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.canary.common.exception.ResultEntity;
import com.example.canary.sys.entity.PermissionAO;
import com.example.canary.sys.entity.PermissionPO;
import com.example.canary.sys.entity.PermissionQuery;
import com.example.canary.sys.entity.PermissionVO;
import com.example.canary.sys.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 权限
 *
 * @author zhaohongliang 2023-09-13 18:57
 * @since 1.0
 */
@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;


    /**
     * pages
     *
     * @param query
     * @return
     */
    @Override
    public ResultEntity<Page<PermissionVO>> pagesPermission(PermissionQuery query) {
        return null;
    }

    /**
     * add
     *
     * @param permissionAo
     * @return
     */
    @Override
    @SuppressWarnings("rawtypes")  
    public ResultEntity addPermission(PermissionAO permissionAo) {
        PermissionPO permissionPo = permissionAo.convertToPo();
        permissionRepository.insert(permissionPo);
        return ResultEntity.success();
    }

    /**
     * update
     * 
     * @param permissionAo
     * @return
     */
    @Override
    @SuppressWarnings("rawtypes")
    public ResultEntity editPermission(PermissionAO permissionAo) {
        PermissionPO permissionPo = permissionAo.convertToPo();
        permissionRepository.update(permissionPo);
        return ResultEntity.success();
    }

    /**
     * delete
     * 
     * @param permissionId
     * @return
     */
    @Override
    @SuppressWarnings("rawtypes")
    public ResultEntity deletePermission(String permissionId) {
        permissionRepository.deleteById(permissionId);
        return ResultEntity.success();
    }
}
