package com.example.canary.sys.entity;

import com.example.canary.common.mybatis.BasePage;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * user
 *
 * @since 1.0
 * @author zhaohongliang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserQuery extends BasePage<UserPO> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1977316676639195587L;

    /**
     * 关键词
     */
    private String keywords;
}
