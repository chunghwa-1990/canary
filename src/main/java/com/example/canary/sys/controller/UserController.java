package com.example.canary.sys.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.canary.common.api.ApiVersion;
import com.example.canary.common.exception.ResultEntity;
import com.example.canary.common.exception.ValidGroup;
import com.example.canary.sys.entity.UserAO;
import com.example.canary.sys.entity.UserQuery;
import com.example.canary.sys.entity.UserVO;
import com.example.canary.sys.service.UserService;
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
 * user controller
 *
 * @since 1.0
 * @author zhaohongliang
 */
@Validated
@ApiVersion
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * pages
     *
     * @param query
     * @return
     */
    @RequestMapping("/pages")
    public ResultEntity<Page<UserVO>> pagesUser(UserQuery query) {
        return null;
    }

    /**
     * save
     *
     * @param userAo
     * @return
     */
    @PostMapping("/save")
    @SuppressWarnings("rawtypes")
    public ResultEntity saveUser(@Validated({ ValidGroup.Add.class }) @RequestBody UserAO userAo) {
        return userService.saveUser(userAo);
    }

    /**
     * save
     *
     * @param userAo
     * @return
     */
    @PutMapping("/update")
    @SuppressWarnings("rawtypes")
    public ResultEntity updateUser(@Validated({ ValidGroup.Edit.class }) @RequestBody UserAO userAo) {
        return userService.updateUser(userAo);
    }

    /**
     * save
     *
     * @param userId
     * @return
     */
    @DeleteMapping("/delete")
    @SuppressWarnings("rawtypes")
    public ResultEntity deleteUser(@NotNull String userId) {
        return userService.deleteUser(userId);
    }
}
