package com.hgzp.advertising.pagemodel.price.vo;
import com.hgzp.core.annotation.LogData;
import com.hgzp.core.model.Tbdiscountitem;
import com.hgzp.core.page.BaseQueryInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 DiscountItemVo
 创建人：songly
 类描述：TODO
 创建日期：2023/11/22 14:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscountItemVo extends Tbdiscountitem {
    private String queryKey; // 查询关键字
    /**
     * 折扣组id
     */
    private Long discountgroupid;
    /**
     * 年份
     */
    private String syear;
}