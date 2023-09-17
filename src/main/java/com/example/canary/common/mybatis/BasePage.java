package com.example.canary.common.mybatis;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;

/**
 * page
 *
 * @author zhaohongliang 2023-09-17 15:00
 * @since 1.0
 */
public class BasePage<T> extends Page<T> {

    /**
     * 获取 page
     *
     * @return
     */
    public Page<T> getPage() {
        Page<T> page = new Page<>();
        BeanUtils.copyProperties(this, page);
        return page;
    }
}
