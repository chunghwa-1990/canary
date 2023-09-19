package com.example.canary.common.mybatis;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

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
        return getPage(true);
    }

    /**
     * 获取 page
     *
     * @return
     */
    public Page<T> getPage(boolean isCamel) {
        Page<T> page = new Page<>();
        BeanUtils.copyProperties(this, page);
        if (isCamel && (!CollectionUtils.isEmpty(orders))) {
                orders.forEach(orderItem -> orderItem.setColumn(
                        com.example.canary.util.StringUtils.toUnderlineCase(orderItem.getColumn())
                ));
        }
        return page;
    }

    /**
     * 获取排序规则
     *
     * @param clazz
     * @return map
     */
    @Deprecated(since = "1.1", forRemoval = true)
    public Map<String, String> getOrderItemMap(Class<? extends Serializable> clazz) {
        Map<String, String> orderItemMap = new LinkedHashMap<>();
        if (!CollectionUtils.isEmpty(orders)) {
            orders.forEach(item -> {
                try {
                    clazz.getDeclaredField(item.getColumn());
                    orderItemMap.put(com.example.canary.util.StringUtils.toLowerCamelCase(item.getColumn()), item.isAsc() ? "asc" : "desc");
                } catch (NoSuchFieldException e) {
                    throw new IllegalArgumentException(item.getColumn() + "不符合列名的要求");
                }
            });
        }
        return orderItemMap;
    }

    /**
     * 获取排序规则
     *
     * @param clazz
     * @return
     */
    public Map<String, String> getOrderMap(Class<?> clazz) {
        // 判空
        if (CollectionUtils.isEmpty(orders)) {
            return Collections.emptyMap();
        }

        Map<String, String> orderItemMap = new LinkedHashMap<>();
        List<String> orderFeildList = orders.stream().map(OrderItem::getColumn).toList();
        List<Field> fields = new ArrayList<>(Arrays.asList(clazz.getDeclaredFields()));
        if (clazz.getSuperclass() != null) {
            List<Field> superFields = Arrays.asList(clazz.getSuperclass().getDeclaredFields());
            fields = Stream.concat(fields.stream(), superFields.stream()).toList();
        }
        Set<String> fieldSet = new HashSet<>();
        fields.forEach(field -> {
            String fieldName = field.getName();
            fieldSet.add(fieldName);
        });
        String errorFiled = orderFeildList.stream().filter(e -> !fieldSet.contains(e)).findFirst().orElse(null);
        if (StringUtils.hasText(errorFiled)) {
            throw new IllegalArgumentException(errorFiled + " 不符合列名的要求");
        }
        fields.forEach(field -> {
            String fieldName = field.getName();
            if (StringUtils.hasText(fieldName)) {
                orders.forEach(item -> {
                    if (item.getColumn().equals(fieldName)) {
                        String cloumn = null;
                        if (field.isAnnotationPresent(TableField.class)) {
                            TableField tableField = field.getAnnotation(TableField.class);
                            cloumn = tableField.value();
                            if (!StringUtils.hasText(cloumn)) {
                                cloumn = com.example.canary.util.StringUtils.toLowerCamelCase(cloumn);
                            }
                        } else {
                            cloumn = com.example.canary.util.StringUtils.toLowerCamelCase(fieldName);
                        }
                        orderItemMap.put(cloumn, item.isAsc() ? "asc" : "desc");
                    }
                });
            }
        });
        return orderItemMap;
    }

}
