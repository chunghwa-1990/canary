package com.example.canary.sys.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * user
 *
 * @ClassName UserVO
 * @Description user
 * @Author zhaohongliang
 * @Date 2023-07-06 12:35
 * @Since 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserVO extends UserBase {

    @Serial
    private static final long serialVersionUID = -3473337968969252412L;
}
