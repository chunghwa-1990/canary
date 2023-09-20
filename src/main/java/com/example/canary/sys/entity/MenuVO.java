package com.example.canary.sys.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.util.List;

/**
 * 菜单
 *
 * @author zhaohongliang 2023-09-08 20:40
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MenuVO extends MenuBase {

    @Serial
    private static final long serialVersionUID = -8349806436315205064L;

    /**
     * 权限
     */
    private List<PermissionVO> permissions;

    public MenuVO() {
    }

    public MenuVO(MenuPO menuPo) {
        BeanUtils.copyProperties(menuPo, this);
    }
}
