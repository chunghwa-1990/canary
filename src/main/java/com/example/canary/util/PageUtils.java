package com.example.canary.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * 分页工具类
 *
 * @author zhaohongliang 2023-09-17 13:02
 * @since 1.0
 */
public class PageUtils {

    private PageUtils() {
    }

    /**
     * 分页转化
     *
     * @param pagePo
     * @param records
     * @return
     * @param <T>
     */
    public static <T> IPage<T> convertToVo(IPage<?> pagePo, List<T> records) {
        IPage<T> pageVo = new Page<>();
        BeanUtils.copyProperties(pagePo, pageVo);
        pageVo.setRecords(records);
        return pageVo;
    }

}
