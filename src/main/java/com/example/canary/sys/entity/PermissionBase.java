package com.example.canary.sys.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 权限
 *
 * @author zhaohongliang 2023-09-13 19:00
 * @since 1.0
 */
@Data
public class PermissionBase implements Serializable {

    @Serial
    private static final long serialVersionUID = 5669917258682365283L;

    /**
     * id
     */
    private String id;

    /**
     * name
     */
    private String name;


}
