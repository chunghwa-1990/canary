package com.example.canary.sys.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * user
 *
 * @ClassName UserQuery
 * @Description user
 * @Author zhaohongliang
 * @Date 2023-07-07 15:54
 * @Since 1.0
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
