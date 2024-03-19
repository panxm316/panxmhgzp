package com.hgzp.pagemodel.schedule;

import com.hgzp.core.annotation.LogData;
import com.hgzp.core.page.DataCombo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * OrderPublishQueryResultVo
 * 创建人：lhl
 * 类描述：广告刊发查询结果对象
 * 创建日期：2023/12/14 17:01
 */
@Data
public class OrderPublishQueryResultVo {
    /**
     * id
     */
    private String id;
    /**
     * 合同号
     */
    private String scontractnum;
    /**
     * 广告项目名称
     */
    private String adprojectname;
    /**
     * 客户名称
     */
    private String customername;
    /**
     * 代理公司名称
     */
    private String agencyname;
    /**
     * 内容生产方
     */
    private String agentname;
    /**
     * 行业名称
     */
    private String adindustryname;
    /**
     * 应收金额
     */
    private BigDecimal amountreceivable;
    /**
     * 已收金额
     */
    private BigDecimal amountreceived;

    /**
     * 欠款金额
     */
    private BigDecimal amountarrearage;

    /**
     * 广告标题
     */
    private String sadtitle;
    /**
     * 预见报开始日期
     */
    private Date prestartdate;
    /**
     * 叠次名称
     */
    private String foldname;
    /**
     * 叠次版本
     */
    private String foldareavername;
    /**
     * 广告形式名称
     */
    private String addisplayformname;
    /**
     * 版面类别名称
     */
    private String pagesortname;
    /**
     * 色彩名称
     */
    private String adcolorname;
    /**
     * 规格名称
     */
    private String adspecname;
    /**
     * 发布状态
     */
    private Integer ipublishstatus;
    /**
     * 核查状态
     */
    private Integer ipublishcheckstatus;

    /**
     * 核查报告内容报告内容
     */
    private String spublishcheckcontent;
    /**
     * 联系人
     */
    @LogData(alias = "联系人")
    private String scontacts;

    /**
     * 联系人地址
     */
    private String saddress;

    /**
     * 联系人电话
     */
    private String smobilephone;

    /**
     * 邮编
     */
    private String spostcode;

    /**
     * 媒体
     */
    private String medianame;

    /**
     * 录入日期
     */
    private String createtime;

    /**
     * 主业务员名称
     */
    private String employname;

    /**
     * 版面名称
     */
    private String foldpageplanname;
    /**
     * 排后叠次
     */
    private String arrangefoldname;
    /**
     * 排后叠次版本
     */
    private String arrangefoldareavername;
    /**
     * 排后版面名称
     */
    private String arrangefoldpageplanname;
    /**
     * 版数
     */
    private List<DataCombo> pageNumList;
    /**
     * 媒体id
     */
    private Long mediaid;
    /**
     * 叠次id
     */
    private Long foldid;
    /**
     * 叠次版本id
     */
    private Long foldareaverid;
    /**
     * 媒体类型key
     */
    private String mediatypekey;
    /**
     * 版面id
     */
    private Long foldpageplanid;
    /**
     * 安排备注
     */
    private String sremark;
    /**
     * 排后叠次版本ID
     */
    private String arrangefoldareaverid;
    /**
     * 排后广告URL
     */
    private String spublishedurl;

}


