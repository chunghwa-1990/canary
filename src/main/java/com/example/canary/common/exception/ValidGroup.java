package com.example.canary.common.exception;

import jakarta.validation.GroupSequence;

/**
 * 分组校验
 *
 * @since 1.0
 * @author zhaohongliang
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
