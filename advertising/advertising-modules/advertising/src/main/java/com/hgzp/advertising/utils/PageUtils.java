package com.hgzp.advertising.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分页工具类
 *
 * @author wangxk
 * @since 2024-01-02
 */
public class PageUtils {

    /**
     * 将T类型分页对象转换为R类型的分页对象
     *
     * @param page 分页 IPage<T>
     * @param func 转换函数
     * @param <T> 原始类型
     * @param <R> 目标类型
     * @return IPage<R>
     */
    public static <T, R> IPage<R> parse(IPage<T> page, Function<T, R> func) {
        List<R> convertedRecords = page.getRecords().stream().map(func).collect(Collectors.toList());
        IPage<R> convertedPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal(), page.searchCount());
        convertedPage.setRecords(convertedRecords);
        return convertedPage;
    }

}
