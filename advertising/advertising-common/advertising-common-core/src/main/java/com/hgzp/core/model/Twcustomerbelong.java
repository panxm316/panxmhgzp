package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;

import com.hgzp.core.annotation.LogData;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 客户归属业务员表
 * </p>
 *
 * @author wwk
 * @since 2023-10-25
 */
@Getter
@Setter
@LogData(alias = "客户归属")
public class Twcustomerbelong implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @LogData()
    private Long id;

    /**
     * 客户id
     */
    @LogData(alias = "客户名称", mappedBy = "twcustomer", mappedByColumn = "sname")
    private Long customerid;

    /**
     * 部门id
     */
    @LogData(alias = "部门id")
    private Long deptid;

    /**
     * 部门名称
     */
    @LogData(alias = "部门名称")
    private String deptname;

    /**
     * 业务员id
     */
    @LogData(alias = "业务员id")
    private Long employid;

    /**
     * 业务员名称
     */
    @LogData(alias = "业务员名称")
    private String employname;

    /**
     * 工作流申请表id
     */
    @LogData(alias = "工作流申请表id")
    private String applicationid;

    /**
     * 创建日期
     */
    @LogData(alias = "创建日期")
    private Date dcreatetime;

    /**
     * 是否主业务员
     */
    @LogData(alias = "主业务员")
    private Boolean bprimary;

    /**
     * 审批状态
     */
    @LogData(alias = "审批状态")
    private Integer iapprovestatus;
}
