package com.example.canary.sys.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * 二级菜单
 *
 * @author zhaohongliang 2023-10-10 13:16
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Menu2ndBO extends MenuBase {

    @Serial
    private static final long serialVersionUID = -1711840199658241472L;

    /**
     * 权限
     */
    private List<PermissionBO> children = new ArrayList<>();

    public Menu2ndBO() {
    }

    public Menu2ndBO(MenuPO menuPo) {
        BeanUtils.copyProperties(menuPo, this);
    }
}
