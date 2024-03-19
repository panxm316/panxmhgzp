package com.hgzp.service.common;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * IMyService
 * 创建人：peij
 * 类描述：通用service
 * 创建日期：2023/8/18 11:22
 */
public interface IMyService<T>  extends IService<T> {
    /**
     * getMaxSort
     * 方法功能: 获取isort最大值
     * @author peij
     * @date 2023/8/18 14:17
     * @param
     * @return java.lang.Integer
     */
    Integer getMaxSort();
}