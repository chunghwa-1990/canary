package com.example.canary.sys.entity;

import com.example.canary.common.exception.ValidGroup;
import com.example.canary.common.mybatis.BasePage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.io.Serializable;

/**
 * 菜单
 *
 * @author zhaohongliang 2023-09-08 20:41
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MenuQuery extends BasePage<MenuPO> implements Serializable {

    @Serial
    private static final long serialVersionUID = -2938453170156680797L;

    /**
     * 关键词
     */
    @Length(max = 100, groups = { ValidGroup.Query.class })
    private String keywords;
}
