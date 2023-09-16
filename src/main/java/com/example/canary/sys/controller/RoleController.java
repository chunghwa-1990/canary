package com.example.canary.sys.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.canary.common.api.ApiVersion;
import com.example.canary.common.exception.ResultEntity;
import com.example.canary.common.exception.ValidGroup;
import com.example.canary.sys.entity.RoleAO;
import com.example.canary.sys.entity.RoleQuery;
import com.example.canary.sys.entity.RoleVO;
import com.example.canary.sys.service.RoleService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 角色
 *
 * @author zhaohongliang 2023-08-03 21:00
 * @since 1.0
 */
@Validated
@ApiVersion
@RestController
@RequestMapping("/sys/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * pages
     *
     * @param query
     * @return
     */
    @RequestMapping("/pages")
    public ResultEntity<Page<RoleVO>> pagesRole(RoleQuery query) {
        return roleService.pagesRole(query);
    }

    /**
     * add
     *
     * @param roleAo
     * @return
     */
    @PostMapping("/add")
    @SuppressWarnings("rawtypes")
    public ResultEntity saveRole(@Validated({ ValidGroup.Add.class }) @RequestBody RoleAO roleAo) {
        return roleService.saveRole(roleAo);
    }

    /**
     * edit
     *
     * @param roleAo
     * @return
     */
    @PutMapping("/edit")
    @SuppressWarnings("rawtypes")
    public ResultEntity editRole(@Validated({ ValidGroup.Add.class }) @RequestBody RoleAO roleAo) {
        return roleService.editRole(roleAo);
    }

    /**
     * delete
     *
     * @param roleId
     * @return
     */
    @DeleteMapping("/delete")
    @SuppressWarnings("rawtypes")
    public ResultEntity deleteRole(@NotBlank String roleId) {
        return roleService.deleteRole(roleId);
    }
}
