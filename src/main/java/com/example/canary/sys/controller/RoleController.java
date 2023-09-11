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
     * save
     *
     * @param roleAo
     * @return
     */
    @RequestMapping("/save")
    @SuppressWarnings("rawtypes")
    public ResultEntity saveRole(@Validated({ ValidGroup.Add.class }) @RequestBody RoleAO roleAo) {
        return roleService.saveRole(roleAo);
    }

    /**
     * update
     *
     * @param roleAo
     * @return
     */
    @RequestMapping("/update")
    @SuppressWarnings("rawtypes")
    public ResultEntity updateRole(@Validated({ ValidGroup.Add.class }) @RequestBody RoleAO roleAo) {
        return roleService.updateRole(roleAo);
    }

    /**
     * delete
     *
     * @param roleId
     * @return
     */
    @RequestMapping("/delete")
    @SuppressWarnings("rawtypes")
    public ResultEntity deleteRole(@NotBlank String roleId) {
        return roleService.deleteRole(roleId);
    }
}
