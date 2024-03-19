package com.hgzp.advertisingsys.pagemodel.ad.vo;

import com.hgzp.core.page.BaseQueryInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AdTypeVO
 * 创建人：yanz
 * 类描述：广告类型 VO
 * 创建日期：2023/8/31 14:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdTypeVO extends BaseQueryInfo {
    /**
     * 是否启用
     */
    private Boolean buse;


}