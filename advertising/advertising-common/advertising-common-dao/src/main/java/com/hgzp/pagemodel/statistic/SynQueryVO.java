package com.hgzp.pagemodel.statistic;

import lombok.Data;

import java.util.List;

/**
 * SynQueryVO
 * 创建人：lenovo
 * 类描述：TODO
 * 创建日期：2024/2/27 8:58
 */
@Data
public class SynQueryVO {
    /**
     * 过滤重复 0：不过滤  1：过滤
     */
    int  filterRepeat;
    /**
     * 筛选范围 0：全部 1：仅见报 1：仅收费  2：见报且收费
     */
    int  range;
    /**
     * 排序字段中文名称
     */
    private String sortName;
    /**
     * 排序字段英文名称
     */
    private String enSortName;
    /**
     * 字段列表
     */
    List<SynQueryItem> synQueryItemList;
    /**
     * 页码
     */
    int pageNum;
    /**
     * 页数
     */
    int pageSize;
    /**
     * 排序类型
     */
    private String sorttype;

}


