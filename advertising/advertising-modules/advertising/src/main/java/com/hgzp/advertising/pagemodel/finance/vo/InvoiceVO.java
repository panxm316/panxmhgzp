package com.hgzp.advertising.pagemodel.finance.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.hgzp.core.model.Twinvoice;
import com.hgzp.core.page.BaseQueryInfo;
import lombok.*;
import lombok.experimental.Accessors;
import com.hgzp.core.annotation.LogData;
import org.springframework.beans.BeanUtils;

/**
 * <p>
 * 发票表 VO 前端页面视图对象
 * </p>
 *
 * @author muyn
 * @since 2023-12-15
 */
@Data
public class InvoiceVO extends BaseQueryInfo {

    /**
     * 主键
     */
    private Long id;
    /**
     * 客户id
     */
    private Long customerid;
    /**
     * 0-直接客户、1-代理公司、2-内容生产方
     */
    private Integer icusttype;
    /**
     * 0-手开，1-预开 2-挂开 3-冲红 4-预收费 5-预开完成
     */
    private Integer itype;
    /**
     * 1-有效、0-无效，2-冲红退回
     */
    private Integer istatus;
    /**
     * 付款方式id
     */
    private Long paymethodid;
    /**
     * 开票项目id
     */
    private Long printitemid;
    /**
     * 税收编码（取开票项目中对应的值）
     */
    private String staxcode;
    /**
     * 业务员id
     */
    private Long employid;
    /**
     * 发票号
     */
    private String invoice;
    /**
     * 发票编码
     */
    private String invoicecode;
    /**
     * 收款人
     */
    private String scashier;
    /**
     * 复核人
     */
    private String schecker;
    /**
     * 客户发票打印名称
     */
    private String sprintname;
    /**
     * 付款方识别号
     */
    private String spayercreditcode;
    /**
     * 关联发票(红冲发票)
     */
    private Long relateinvoiceid;
    /**
     * 经营主体ID
     */
    private Long businessentityid;
    /**
     * 是否当前人员（0：否 1：是，如果是则表示只查询当前人员）
     */
    private Boolean bcurflag;
    /**
     * 是否欠费(查询是否仅欠款的数据，0:否 1：是)
     */
    private Boolean barrearsflag;

    /**
     * 广告项目id
     */
    private Long adprojectid;

    /**
     * 广告项目名称
     */
    private String adprojectname;
}
