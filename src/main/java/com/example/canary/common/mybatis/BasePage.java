package com.example.canary.common.mybatis;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.List;

/**
 * page
 *
 * @author zhaohongliang 2023-09-17 15:00
 * @since 1.0
 */
@Data
public class BasePage<T extends Serializable> {


    /**
     * 每页显示条数
     */
    private long size = 500;

    /**
     * 当前页
     */
    private long current = 1;

    /**
     * 排序
     */
    private List<OrderItem> orders;

    /**
     * 是否驼峰
     */
    private boolean camelCase = true;

    /**
     * 获取当前页第一行行号
     *
     * @return
     */
    public long getStartNum() {
        return (current - 1) * size;
    }

    /**
     * 获取当前页面末尾行行号
     *
     * @return
     */
    public long getEndNum() {
        return current * size;
    }


    /**
     * 获取 page
     *
     * @return
     */
    public Page<T> getPage() {
        Page<T> page = new Page<>();
        BeanUtils.copyProperties(this, page);
        if (!CollectionUtils.isEmpty(orders)) {
            orders.forEach(item -> {
                String column = com.example.canary.util.StringUtils.toUnderlineCase(item.getColumn());
                item.setColumn(column);
            });
        }
        return page;
    }

}
