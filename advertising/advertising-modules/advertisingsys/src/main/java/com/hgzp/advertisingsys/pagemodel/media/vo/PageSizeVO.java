package com.hgzp.advertisingsys.pagemodel.media.vo;

import com.hgzp.core.page.BaseQueryInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PageSizeVO
 * 创建人：yanz
 * 类描述：报纸版心 VO
 * 创建日期：2023/8/31 14:50
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageSizeVO extends BaseQueryInfo {
    /**
     * 是否启用
     */
    private Boolean buse;

}