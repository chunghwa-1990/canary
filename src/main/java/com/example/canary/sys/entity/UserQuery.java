package com.example.canary.sys.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * user
 *
 * @since 1.0
 * @author zhaohongliang
 */
@Data
public class UserQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1977316676639195587L;

    /**
     * 关键词
     */
    private String keyword;
}
