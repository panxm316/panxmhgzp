package com.hgzp.advertising.pagemodel.customer.dto;

import com.hgzp.core.annotation.LogData;
import com.hgzp.core.constant.ValidateParam;
import com.hgzp.core.page.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * CustomerServiceDTO
 * 创建人：suny
 * 类描述：客户服务DTO
 * 创建日期：2023/11/9 9:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@LogData(alias = "客户服务信息")
public class CustomerServiceDTO extends BaseDTO {
    /**
     * id
     */
    @NotNull(message = "客户服务id不能为空", groups = {ValidateParam.edit.class, ValidateParam.detail.class})
    @LogData(showColumn = "stitle")
    private Long id;

    /**
     * 标题
     */
    @NotBlank(message = "客户服务标题不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private String stitle;

    /**
     * 人员id
     */
    private Long employid;

    /**
     * 人员名称
     */
    private String employname;

    /**
     * 客户id
     */
    private Long customerid;

    /**
     * 客户名称
     */
    @NotBlank(message = "客户名称不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private String customername;

    /**
     * 0-客户投诉 1-客户建议 2-客户调查
     */
    @NotNull(message = "客户服务类型不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private Integer iservicetype;

    /**
     * 联系人
     */
    private String scontacts;

    /**
     * 联系方式
     */
    private String sphone;

    /**
     * 开始日期
     */
    private Date dstartdate;

    /**
     * 结束日期
     */
    private Date denddate;

    /**
     * 操作员id
     */
    private Long operatorid;

    /**
     * 操作员
     */
    private String operatorname;

    /**
     * 创建日期
     */
    private Date dcreatetime;

    /**
     * 内容
     */
    @NotBlank(message = "内容不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private String scontent;

    /**
     * 结果
     */
    private String sresult;

    /**
     * 状态(可选可填写：已记录、已解决、已结束等)
     */
    private String sstatus;

    /**
     * 最后修改者id
     */
    private Long lastoperatorid;

    /**
     * 最后修改者
     */
    private String lastoperator;

    /**
     * 最后修改日期
     */
    private Date dlasttime;

    /**
     * 是否删除标记
     */
    private Boolean bdelete;
}
