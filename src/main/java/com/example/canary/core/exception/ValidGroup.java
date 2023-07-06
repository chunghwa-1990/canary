package com.example.canary.core.exception;

import jakarta.validation.GroupSequence;

/**
 * 分组校验
 *
 * @ClassName ValidGroup
 * @Description 分组校验
 * @Author zhaohongliang
 * @Date 2023-07-01 13:06
 * @Since 1.0
 */
public class ValidGroup {

    /**
     * add
     */
    public interface Add {}

    /**
     * edit
     */
    public interface Edit {}

    /**
     * password
     */
    public interface Pwd {}

    /**
     * query
     */
    public interface Query {}

    /**
     * all
     */
    @GroupSequence({ Add.class, Edit.class, Query.class })
    public interface All {}
}
