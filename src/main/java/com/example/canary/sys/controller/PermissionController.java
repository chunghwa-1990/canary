package com.example.canary.sys.controller;

import com.example.canary.common.api.ApiVersion;
import com.example.canary.common.exception.ValidGroup;
import com.example.canary.sys.entity.MenuPermissionVO;
import com.example.canary.sys.entity.PermissionAO;
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

import java.util.List;

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

    private final PermissionService permissionService;

    @Autowired
    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    /**
     * 根据用户id 查询菜单和权限
     *
     * @return
     */
    @GetMapping
    public List<MenuPermissionVO> queryPermissions(@NotBlank String userId) {
        return permissionService.queryPermissions(userId);
    }

    /**
     * list
     *
     * @return
     */
    @GetMapping("/list")
    public List<MenuPermissionVO> listPermissions() {
        return permissionService.listPermissions();
    }

    /**
     * add
     *
     * @return
     */
    @PostMapping("/add")
    public PermissionVO addPermission(@Validated({ ValidGroup.Add.class }) @RequestBody PermissionAO permissionAo) {
        return permissionService.addPermission(permissionAo);
    }

    /**
     * update
     *
     * @return
     */
    @PutMapping("/edit")
    public PermissionVO editPermission(@Validated({ ValidGroup.Edit.class }) @RequestBody PermissionAO permissionAo) {
        return permissionService.editPermission(permissionAo);
    }

    /**
     * delete
     *
     * @param id
     */
    @DeleteMapping("/delete")
    public void deletePermission(@NotBlank String id) {
        permissionService.deletePermission(id);
    }

}
