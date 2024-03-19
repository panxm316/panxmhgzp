package com.hgzp.advertising.pagemodel.schedule.vo;

import com.hgzp.core.model.Tbpagecolor;
import lombok.Data;

import java.util.List;

/**
 PageColorVo
 创建人：songly
 类描述：TODO
 创建日期：2023/11/15 10:40
 */
@Data
public class PageColorVo extends Tbpagecolor {
    /**
     * 版面色彩列表
     */
    private List<String> lsColors;
}