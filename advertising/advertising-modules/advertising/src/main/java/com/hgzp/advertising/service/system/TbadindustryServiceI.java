package com.hgzp.advertising.service.system;

import com.hgzp.core.model.Tbadindustry;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.TreeModel;
import com.hgzp.service.common.IMyService;

import java.util.List;

/**
 * @author new wei
 * @date 2023/10/28 8:57
 */
public interface TbadindustryServiceI extends IMyService<Tbadindustry> {
    /**
     * 获取行业下拉
     * 方法功能: 获取行业数据
     *
     * @param
     * @return java.util.List<com.hgzp.core.page.DataCombo>
     * @author songly
     * @date 2023/10/28 9:12
     */
    List<TreeModel> getTbAdIndustryTree();

    /**
     * 获取行业列表（只取分类广告）
     * 方法功能: 获取行业列表（只取分类广告）
     *
     * @param
     * @return java.util.List<com.hgzp.core.model.Tbadindustry>
     * @author suny
     * @date 2024/2/27 13:02
     */
    List<Tbadindustry> getTbAdIndustryListForCJ();

    /**
     * 保存行业头信息
     * 方法功能: 保存行业头信息
     * @author suny
     * @date 2024/2/27 13:28
     * @param id
     * @param path
     * @param filename
     * @return com.hgzp.core.page.Json
     */
    Json SaveSortAdSetForCJ(String id, String path, String filename);
}
