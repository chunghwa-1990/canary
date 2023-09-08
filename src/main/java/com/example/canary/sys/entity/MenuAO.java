package com.example.canary.sys.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 菜单
 *
 * @author zhaohongliang 2023-09-08 20:40
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MenuAO extends MenuBase {

    @Serial
    private static final long serialVersionUID = -1822076114325324597L;

}
