package com.example.canary.sys.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * login
 *
 * @ClassName LoginVO
 * @Description login
 * @Author zhaohongliang
 * @Date 2023-07-06 12:47
 * @Since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -3880883170325579213L;

    /**
     * token
     */
    private String token;
}
