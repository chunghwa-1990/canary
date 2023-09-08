package com.example.canary.sys.controller;

import com.example.canary.common.api.ApiVersion;
import com.example.canary.common.exception.ResultEntity;
import com.example.canary.sys.entity.MenuQuery;
import com.example.canary.sys.entity.MenuVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
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


}
