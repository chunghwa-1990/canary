package com.example.canary.sys.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * login
 *
 * @since 1.0
 * @author zhaohongliang
 */
@Data
public class LoginAO implements Serializable {

    @Serial
    private static final long serialVersionUID = 3440453106355021969L;


    /**
     * 账号
     */
    @NotBlank
    private String account;

    /**
     * 密码
     */
    @NotBlank
    private String password;
}
