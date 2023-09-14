package com.example.canary.sys.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.canary.common.api.ApiVersion;
import com.example.canary.common.exception.ResultEntity;
import com.example.canary.common.exception.ValidGroup;
import com.example.canary.sys.entity.PermissionAO;
import com.example.canary.sys.entity.PermissionQuery;
import com.example.canary.sys.entity.PermissionVO;
import com.example.canary.sys.service.PermissionService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 权限
 *
 * @author zhaohongliang 2023-09-11 23:44
 * @since 1.0
 */
@Validated
@ApiVersion
@RestController
@RequestMapping("/sys/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    /**
     * pages
     *
     * @return
     */
    @GetMapping("/pages")
    public ResultEntity<Page<PermissionVO>> pagesPermission(PermissionQuery query) {
        return permissionService.pagesPermission(query);
    }

    /**
     * save
     *
     * @return
     */
    @PostMapping("/save")
    @SuppressWarnings("rawtypes")
    public ResultEntity savePermission(@Validated({ ValidGroup.Add.class }) @RequestBody PermissionAO permissionAo) {
        return permissionService.savePermission(permissionAo);
    }

    /**
     * update
     *
     * @return
     */
    @PutMapping("/update")
    @SuppressWarnings("rawtypes")
    public ResultEntity updatePermission(@Validated({ ValidGroup.Edit.class }) @RequestBody PermissionAO permissionAo) {
        return permissionService.updatePermission(permissionAo);
    }

    /**
     * delete
     *
     * @param permissionId
     * @return
     */
    @DeleteMapping("/delete")
    @SuppressWarnings("rawtypes")
    public ResultEntity deletePermission(@NotBlank String permissionId) {
        return permissionService.deletePermission(permissionId);
    }

}
