package com.example.canary.sys.entity;

import com.example.canary.common.mybatis.BasePage;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 角色
 *
 * @author zhaohongliang 2023-08-13 17:04
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleQuery extends BasePage<RolePO> implements Serializable {

    @Serial
    private static final long serialVersionUID = 5481461766246420400L;

    /**
     * 关键词
     */
    private String keywords;
}
