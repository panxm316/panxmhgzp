package com.hgzp.advertisingsys.pagemodel.finance.vo;

import com.hgzp.core.page.BaseQueryInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AdPrintItemVO
 * 创建人：yanz
 * 类描述：开票设置 VO
 * 创建日期：2023/8/31 14:46
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdPrintItemVO extends BaseQueryInfo {
    /**
     * 是否启用
     */
    private Boolean buse;


}