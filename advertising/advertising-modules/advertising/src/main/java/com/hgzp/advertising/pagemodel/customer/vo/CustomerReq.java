package com.hgzp.advertising.pagemodel.customer.vo;

/**
 * @author new wei
 * @date 2023/10/25 14:51
 */

import com.hgzp.core.annotation.LogData;
import com.hgzp.core.page.BaseQueryInfo;
import lombok.Data;

import java.util.Date;

/**
 CustomerReq
 创建人：hgsongly
 类描述：TODO
 创建日期：2023/10/25 14:51
 */
@Data
public class CustomerReq extends BaseQueryInfo {
    /**
     * 简码(拼音)
     */
    @LogData(alias = "简码")
    private String sbrevitycode;
    /**
     * 类型(直接客户、代理公司、内容生产方)
     */
    private Integer itype;

    /**
     * 行业id
     */
    private Long adindustryid;
    /**
     * 主业务员id
     */
    private Long employid;
    /**
     * 是否大客户
     */
    private Boolean bvip;

    /**
     * 客户状态（活跃，非活跃。。。）
     */
   // private String sstatus;
    private Long customerstatusid;
    /**
     * 客户分类id
     */
    private Long customercategoryid;
    /**
     * 客户信誉度id
     */
    private Long customercreditid;
    /**
     * 审批状态
     */
    private Integer iapprovestatus;

    /**
     * 是否删除
     */
    private Boolean bdelete;

    /**
     * 是否启用
     */
    private Boolean buse;

    /**
     * 是否个人
     */
    private Boolean bindividual;
    /**
     * 仅我的客户
     */
    private Boolean bmycustomer;

}