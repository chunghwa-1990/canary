package com.example.canary.sys.entity;

import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 二级菜单
 *
 * @author zhaohongliang 2023-10-10 13:16
 * @since 1.0
 */
@Data
public class Menu2ndDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -1711840199658241472L;

    /**
     * id
     */
    private String id;

    /**
     * 父菜单id
     */
    private String parentId;

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
     * 权限
     */
    private List<PermissionDTO> children = new ArrayList<>();

    public Menu2ndDTO() {
    }

    public Menu2ndDTO(MenuPO menuPo) {
        BeanUtils.copyProperties(menuPo, this);
    }
}
