package com.example.canary.common.context;

import com.example.canary.sys.entity.UserBase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 当前用户
 *
 * @since 1.0
 * @author zhaohongliang
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CurrentUser<T> {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 数据
     */
    private T data;

    /**
     * 带参构造
     *
     * @param data
     */
    public CurrentUser(T data) {
        this.data = data;
        if (data instanceof UserBase userBase) {
            userId = userBase.getId();
        }
    }
}
