package com.example.canary.sys.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 角色
 *
 * @author zhaohongliang 2023-08-13 17:00
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleAO extends RoleBase {

    @Serial
    private static final long serialVersionUID = -9153554946923170562L;

    /**
     * 权限ID集合
     */
    private Set<String> permissionIds;

    public RolePO convertToPo() {
        RolePO rolePo = new RolePO();
        BeanUtils.copyProperties(this, rolePo);
        return rolePo;
    }

    public List<RolePermissionPO> getRolePermissionList(String roleId) {
        if (CollectionUtils.isEmpty(permissionIds)) {
            return null;
        }
        List<RolePermissionPO> rolePermissionPoList = new ArrayList<>();
        permissionIds.forEach(s -> rolePermissionPoList.add(new RolePermissionPO(roleId, s)));
        return rolePermissionPoList;
    }
}
