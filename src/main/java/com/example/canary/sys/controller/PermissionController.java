package com.example.canary.sys.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.canary.common.api.ApiVersion;
import com.example.canary.common.exception.ResultEntity;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    /**
     * pages
     *
     * @return
     */
    @GetMapping("/pages")
    public ResultEntity<Page<?>> pagesPermission() {
        return null;
    }

    @PostMapping("/save")
    @SuppressWarnings("rawtypes")
    public ResultEntity savePermissions() {
        return null;
    }

    @PutMapping("/update")
    @SuppressWarnings("rawtypes")
    public ResultEntity updatePermissions() {
        return null;
    }

    @DeleteMapping("/delete")
    @SuppressWarnings("rawtypes")
    public ResultEntity deletePermissions(@NotBlank String permissionId) {
        return null;
    }

}
