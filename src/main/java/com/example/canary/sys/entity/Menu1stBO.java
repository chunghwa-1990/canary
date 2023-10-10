package com.example.canary.sys.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * 一级菜单
 *
 * @author zhaohongliang 2023-10-10 13:11
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Menu1stBO extends MenuBase {

    @Serial
    private static final long serialVersionUID = -2947726161054592768L;

    /**
     * 下级菜单
     */
    private List<Menu2ndBO> children = new ArrayList<>();

    public Menu1stBO() {
    }

    public Menu1stBO(MenuPO menuPo) {
        BeanUtils.copyProperties(menuPo, this);
    }
}
