package com.example.canary.sys.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * user
 *
 * @since 1.0
 * @author zhaohongliang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserAO extends UserBase {

    @Serial
    private static final long serialVersionUID = -5266978191980847566L;

    /**
     * 角色ID
     */
    private Set<String> roleIds;

    /**
     * 密码
     */
    private String password;

    public UserPO convertToPo() {
        UserPO userPo = new UserPO();
        BeanUtils.copyProperties(this, userPo);
        return userPo;
    }

    public List<UserRolePO> getUserRoles(String userId) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return Collections.emptyList();
        }
        List<UserRolePO> userRoles = new ArrayList<>();
        roleIds.forEach(s-> userRoles.add(new UserRolePO(userId, s)));
        return userRoles;
    }
}
