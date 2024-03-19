package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.Version;
import com.hgzp.core.annotation.LogData;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 客户表
 * </p>
 *
 * @author wwk
 * @since 2023-10-28
 */
@Getter
@Setter
@LogData(alias = "客户管理")
public class Twcustomer implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @LogData(showColumn = "sname")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 名称
     */
    @LogData(alias = "客户名称")
    private String sname;

    /**
     * 简码(拼音)
     */
    @LogData(alias = "简码")
    private String sbrevitycode;

    /**
     * 编号(自增列)
     */
    private Integer icode;

    /**
     * 类型(0-直接客户、1-代理公司、2-内容生产方)
     */
    @LogData(alias = "客户类型")
    private Integer itype;

    /**
     * 地址
     */
    @LogData(alias = "地址")
    private String saddress;

    /**
     * 办公电话
     */
    private String sphone;

    /**
     * 税务登记号
     */
    @LogData(alias = "税务登记号")
    private String screditcode;

    /**
     * 银行及账号
     */
    @LogData(alias = "银行及账号")
    private String sbankaccount;

    /**
     * 联系人
     */
    @LogData(alias = "联系人")
    private String scontacts;

    /**
     * 联系人电话
     */
    @LogData(alias = "联系人电话")
    private String smobilephone;

    /**
     * 邮编
     */
    private String spostcode;

    /**
     * 行业id
     */
    @LogData(alias = "行业id")
    private Long adindustryid;

    /**
     * 行业
     */
    @LogData(alias = "行业")
    private String adindustryname;

    /**
     * 主业务员id
     */
    @LogData(alias = "主业务员id", mappedBy = "tbemploy", mappedByColumn = "susername")
    private Long employid;

    /**
     * 主业务员
     */
    @LogData(alias = "主业务员")
    private String employname;

    /**
     * 父ID
     */
    @LogData(alias = "父ID", mappedBy = "Twcustomer", mappedByColumn = "sname")
    private Long parentid;

    /**
     * 是否大客户
     */
    @LogData(alias = "大客户")
    private Boolean bvip;

    /**
     * 客户状态（活跃，非活跃。。。）
     */
    @LogData(alias = "客户状态")
    private String sstatus;

    /**
     * 审批状态
     */
    @LogData(alias = "审批状态")
    private Integer iapprovestatus;

    /**
     * 审批意见（最后一次审批意见）
     */
    @LogData(alias = "审批意见")
    private String sapprovalopinions;

    /**
     * 是否删除
     */
    @LogData(alias = "删除")
    private Boolean bdelete;

    /**
     * 是否启用
     */
    @LogData(alias = "启用")
    private Boolean buse;

    /**
     * 是否个人
     */
    @LogData(alias = "个人")
    private Boolean bindividual;

    /**
     * 品牌
     */
    private String sbrand;

    /**
     * 客户分类id
     */
    private Long customercategoryid;

    /**
     * 客户分类名称
     */
    private String customercategoryname;

    /**
     * 客户状态id
     */
    private Long customerstatusid;

    /**
     * 客户状态名称
     */
    private String customerstatusname;

    /**
     * 客户信誉度id
     */
    private Long customercreditid;

    /**
     * 客户信誉度名称
     */
    private String customercreditname;

    /**
     * 序号
     */
    @LogData(alias = "序号")
    private Integer isort;

    /**
     * 备注
     */
    private String sremark;

    /**
     * 创建日期
     */
    private Date dcreatetime;

    /**
     * 最后操作员id
     */

    @LogData(alias = "最后操作员", mappedBy = "tbemploy", mappedByColumn = "susername")
    private Long lastoperatorid;

    /**
     * 最后操作员
     */
    private String lastoperator;

    /**
     * 并发标志
     */
    @Version
    private Long version;
    /**
     * 来源Id
     */
    @LogData(showColumn = "来源Id")
    private Long adfromid;

    /**
     * 来源名称
     */
    @LogData(alias = "来源名称")
    private String adfromname;
}
