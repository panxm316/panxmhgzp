package com.hgzp.advertising.pagemodel.ad.vo;

import com.hgzp.core.page.BaseQueryInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * AdResourceApplicationVO
 * 创建人：suny
 * 类描述：广告资源申请表VO
 * 创建日期：2023/11/11 13:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdResourceApplicationVO extends BaseQueryInfo {
    /**
     * 部门id
     */
    private Long deptid;

    /**
     * 业务员id
     */
    private Long employid;
    /**
     * 客户id
     */
    private Long customerid;
    /**
     * 客户ids
     */
    private List<Long> customerIds;
    /**
     * 0-直接客户、1-代理公司、2-内容生产方
     */
    private Integer icusttype;
    /**
     * 开始使用日期
     */
    private Date dstartdate;
    /**
     * 审批状态(0待审、1在审、2通过、3否决、4撤销、5无效)
     */
    private Integer iapprovestatus;
}
