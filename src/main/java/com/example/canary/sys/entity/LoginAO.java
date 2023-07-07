package com.example.canary.sys.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * login
 *
 * @ClassName LoginAO
 * @Description login
 * @Author zhaohongliang
 * @Date 2023-07-06 22:00
 * @Since 1.0
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
