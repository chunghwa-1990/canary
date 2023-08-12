package com.example.canary.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色
 *
 * @author zhaohongliang 2023-08-03 21:17
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("s_sys_role")
public class RolePO extends RoleBase {

    private static final long serialVersionUID = 5390080297645288091L;
}
