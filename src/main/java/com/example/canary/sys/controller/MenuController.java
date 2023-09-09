package com.example.canary.sys.controller;

import com.example.canary.common.api.ApiVersion;
import com.example.canary.common.exception.ResultEntity;
import com.example.canary.common.exception.ValidGroup;
import com.example.canary.sys.entity.MenuAO;
import com.example.canary.sys.entity.MenuQuery;
import com.example.canary.sys.entity.MenuVO;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 菜单
 *
 * @author zhaohongliang 2023-09-08 20:34
 * @since 1.0
 */
@Validated
@ApiVersion
@RestController
@RequestMapping("/sys/menu")
public class MenuController {

    /**
     * pages
     *
     * @param query
     * @return
     */
    @GetMapping("/pages")
    public ResultEntity<MenuVO> pagesMenu(MenuQuery query) {
        return null;
    }

    /**
     * update
     *
     * @param menuAo
     * @return
     */
    @PostMapping("/save")
    @SuppressWarnings("rawtypes")
    public ResultEntity saveMenu(@Validated({ ValidGroup.Add.class }) @RequestBody MenuAO menuAo) {
        return null;
    }

    /**
     * update
     *
     * @param menuAo
     * @return
     */
    @PutMapping("/update")
    @SuppressWarnings("rawtypes")
    public ResultEntity updateMenu(@Validated({ ValidGroup.Edit.class }) @RequestBody MenuAO menuAo) {
        return null;
    }

    /**
     * delete
     *
     * @param menuId
     * @return
     */
    @DeleteMapping("/delete")
    @SuppressWarnings("rawtypes")
    public ResultEntity deleteMenu(@NotNull String menuId) {
        return null;
    }



}