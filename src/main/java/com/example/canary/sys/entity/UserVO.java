package com.example.canary.sys.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * user
 *
 * @since 1.0
 * @author zhaohongliang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserVO extends UserBase {

    @Serial
    private static final long serialVersionUID = -3473337968969252412L;
}
