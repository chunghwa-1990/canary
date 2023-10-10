package com.example.canary.sys.entity;

import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 菜单权限关联关系
 *
 * @author zhaohongliang 2023-09-20 21:12
 * @since 1.0
 */
@Data
public class MenuPermissionVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -1712281910265515145L;

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
    private List<Menu2ndDTO> children;

    public MenuPermissionVO() {
    }

    public MenuPermissionVO(Menu1stDTO menu1StDTO) {
        BeanUtils.copyProperties(menu1StDTO, this);
    }
}
