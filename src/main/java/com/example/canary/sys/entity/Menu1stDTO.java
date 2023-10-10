package com.example.canary.sys.entity;

import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 一级菜单
 *
 * @author zhaohongliang 2023-10-10 13:11
 * @since 1.0
 */
@Data
public class Menu1stDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -2947726161054592768L;

    /**
     * id
     */
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 图标
     */
    private String icon;

    /**
     * 路由
     */
    private String route;

    /**
     * 菜单级别 [0,1]
     */
    private Integer level;

    /**
     * 下级菜单
     */
    private List<Menu2ndDTO> children = new ArrayList<>();

    public Menu1stDTO() {
    }

    public Menu1stDTO(MenuPO menuPo) {
        BeanUtils.copyProperties(menuPo, this);
    }
}
