package com.hgzp.advertisingsys.pagemodel.ad.vo;

import com.hgzp.core.page.BaseQueryInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PricePackageType
 * 创建人：yanz
 * 类描述：打包类型 VO
 * 创建日期：2023/8/31 14:44
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PricePackageTypeVO extends BaseQueryInfo {
    /**
     * 是否启用
     */
    private Boolean buse;


}