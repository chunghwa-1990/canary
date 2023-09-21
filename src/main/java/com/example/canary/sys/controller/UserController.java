package com.example.canary.sys.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.canary.common.api.ApiVersion;
import com.example.canary.common.exception.ResultEntity;
import com.example.canary.common.exception.ValidGroup;
import com.example.canary.sys.entity.UserAO;
import com.example.canary.sys.entity.UserQuery;
import com.example.canary.sys.entity.UserVO;
import com.example.canary.sys.service.UserService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户
 *
 * @since 1.0
 * @author zhaohongliang
 */
@Validated
@ApiVersion
@RestController
@RequestMapping("/sys/user")
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
    public ResultEntity<IPage<UserVO>> pagesUser(UserQuery query) {
        return userService.pagesUser(query);
    }

    /**
     * add
     *
     * @param userAo
     * @return
     */
    @PostMapping("/add")
    @SuppressWarnings("rawtypes")
    public ResultEntity addUser(@Validated({ ValidGroup.Add.class }) @RequestBody UserAO userAo) {
        return userService.addUser(userAo);
    }

    /**
     * edit
     *
     * @param userAo
     * @return
     */
    @PutMapping("/edit")
    @SuppressWarnings("rawtypes")
    public ResultEntity editUser(@Validated({ ValidGroup.Edit.class }) @RequestBody UserAO userAo) {
        return userService.editUser(userAo);
    }

    /**
     * delete
     *
     * @param id
     * @return
     */
    @DeleteMapping("/delete")
    @SuppressWarnings("rawtypes")
    public ResultEntity deleteUser(@NotBlank String id) {
        return userService.deleteUser(id);
    }
}
