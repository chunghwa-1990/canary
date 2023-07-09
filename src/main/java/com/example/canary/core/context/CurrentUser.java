package com.example.canary.core.context;

import com.example.canary.sys.entity.UserBase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 当前用户
 *
 * @ClassName CurrentUser
 * @Description 当前用户
 * @Author zhaohongliang
 * @Date 2023-07-09 00:31
 * @Since 1.0
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CurrentUser<T> {

    /**
     * id
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
