package com.hgzp.advertisingsys.pagemodel.ad.vo;

import com.hgzp.core.page.BaseQueryInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * WeekSettingVO
 * 创建人：yanz
 * 类描述：星期设置 VO
 * 创建日期：2023/8/31 14:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeekSettingVO extends BaseQueryInfo {
    /**
     * 是否启用
     */
    private Boolean buse;


}