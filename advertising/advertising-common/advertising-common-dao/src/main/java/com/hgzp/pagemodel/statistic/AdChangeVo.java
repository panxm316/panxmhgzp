package com.hgzp.pagemodel.statistic;

import com.hgzp.core.annotation.LogData;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * AdChangeVo
 * 创建人：lhl
 * 类描述：广告变更记录
 * 创建日期：2024/2/20 10:30
 */
@Data
public class AdChangeVo {
    /**
     * 登记时间
     */
    private Date createtime;
    /**
     * 认刊号
     */
    private String scontractnum;
    /**
     * 代理公司名称
     */
    private String agencyname;
    /**
     * 客户名称
     */
    private String customername;
    /**
     * 预见报开始日期
     */
    private Date prestartdate;
    /**
     * 应收金额
     */
    private BigDecimal amountreceivable;
    /**
     * 主业务员名称
     */
    private String employname;
    /**
     * 叠次名称
     */
    private String foldname;
    /**
     * 经营主体名称
     */
    private String businessentityname;
    /**
     * 修改后应收金额
     */
    private BigDecimal changeAmountreceivable;
    /**
     * 修改后发布时间
     */
    private Date changeprestartdate;
    /**
     * 备注
     */
    private String sremark;
    /**
     * 录入方式 0-正常 1-广告预约 2-快速预约 3-补刊
     */
    private Integer ibooktype;


}


