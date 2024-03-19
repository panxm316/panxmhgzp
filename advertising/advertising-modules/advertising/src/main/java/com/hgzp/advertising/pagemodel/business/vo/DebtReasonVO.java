package com.hgzp.advertising.pagemodel.business.vo;

import com.hgzp.core.page.BaseQueryInfo;
import lombok.Data;

/**
 * DebtReasonVO
 * 创建人：suny
 * 类描述：欠款原因表 查询实体对象
 * 创建日期：2023/11/17 10:13
 */
@Data
public class DebtReasonVO extends BaseQueryInfo {
    /**
     * 部门id
     */
    private Long deptid;
    /**
     * 主业务员id
     */
    private Long employid;
    /**
     * 代理公司业务员id
     */
    private Long agencyemployid;
    /**
     * 合同号
     */
    private String scontractnum;
    /**
     * 发票号
     */
    private String invoice;
    /**
     * 是否填报欠款原因(是否推送) 默认0 否 1是
     */
    private Boolean breportreason;
    /**
     * 是否财务人员查询，财务人员只能查询已提交并通过的数据
     */
    private Boolean bfinance;
}
