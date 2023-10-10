package com.example.canary.sys.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.util.List;

/**
 * 菜单权限关联关系
 *
 * @author zhaohongliang 2023-09-20 21:12
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MenuPermissionVO extends MenuBase {

    @Serial
    private static final long serialVersionUID = -1712281910265515145L;

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
