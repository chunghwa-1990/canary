package com.example.canary.sys.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户角色关联关系
 *
 * @author zhaohongliang 2023-09-21 13:22
 * @since 1.0
 */
@Data
public class UserRoleBase implements Serializable {

    @Serial
    private static final long serialVersionUID = -7612498677731548822L;

    /**
     * ID
     */
    private String id;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 角色ID
     */
    private String roleId;

    public UserRoleBase() {
    }

    public UserRoleBase(String userId, String roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

}
