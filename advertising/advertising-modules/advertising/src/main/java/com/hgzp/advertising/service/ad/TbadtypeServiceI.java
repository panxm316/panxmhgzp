package com.hgzp.advertising.service.ad;

import com.hgzp.core.model.Tbadtype;
import com.hgzp.service.common.IMyService;

import java.util.List;

/**
 * @author new wei
 * @date 2023/12/5 15:56
 */
public interface TbadtypeServiceI extends IMyService<Tbadtype> {
    /**
     * 获取广告类型列表
     * 方法功能:
     *
     * @param
     * @return java.util.List<com.hgzp.core.model.Tbadtype>
     * @author songly
     * @date 2023/12/5 15:59
     */
    List<Tbadtype> getAdTypeList();
}
