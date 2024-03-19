package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.Version;
import com.hgzp.core.annotation.LogData;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 发票表
 * </p>
 *
 * @author wwk
 * @since 2023-10-27
 */
@Getter
@Setter
@LogData(alias = "发票")
public class Twinvoice implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @LogData(showColumn = "customername")
    private Long id;

    /**
     * 客户id
     */
    @LogData(alias = "客户id", mappedBy = "twcustomer", mappedByColumn = "sname")
    private Long customerid;

    /**
     * 客户名称
     */
    @LogData(alias = "客户名称")
    private String customername;

    /**
     * 0-直接客户、1-代理公司、2-内容生产方
     */
    @LogData(alias = "客户类型")
    private Integer icusttype;

    /**
     * 发票金额
     */
    @LogData(alias = "发票金额")
    private BigDecimal namount;

    /**
     * 创建者id
     */
    @LogData(alias = "操作人员", mappedBy = "tbemploy", mappedByColumn = "susername")
    private Long operatorid;

    /**
     * 创建者
     */
    @LogData(alias = "创建者")
    private String operator;

    /**
     * 创建时间
     */
    @LogData(alias = "创建时间")
    private Date dcreatetime;

    /**
     * 0-手开，1-预开 2-挂开 3-冲红 4-预收费 5-预开完成
     */
    @LogData(alias = "发票类型")
    private Integer itype;

    /**
     * 发票样式  2-普通发票 0-专用发票 51-电子发票 81-数电专票 82-数电普票
     */
    @LogData(alias = "发票样式")
    private Integer istype;

    /**
     * 1-有效、0-无效，2-冲红退回
     */
    @LogData(alias = "发票状态")
    private Integer istatus;

    /**
     * 付款方式id
     */
    @LogData(alias = "付款方式ID", mappedBy = "tbpaymethod", mappedByColumn = "sname")
    private Long paymethodid;

    /**
     * 付款方式
     */
    @LogData(alias = "付款方式")
    private String paymethodname;

    /**
     * 开票项目id
     */
    @LogData(alias = "开票项目id", mappedBy = "tbadprintitem", mappedByColumn = "sname")
    private Long printitemid;

    /**
     * 开票项目
     */
    @LogData(alias = "开票项目")
    private String printitemname;

    /**
     * 税收编码
     */
    @LogData(alias = "税收编码")
    private String staxcode;

    /**
     * 业务员id
     */
    @LogData(alias = "业务员", mappedBy = "tbemploy", mappedByColumn = "susername")
    private Long employid;

    /**
     * 业务员名称
     */
    @LogData(alias = "业务员")
    private String employname;

    /**
     * 发票号
     */
    @LogData(alias = "发票号")
    private String invoice;

    /**
     * 发票编码
     */
    @LogData(alias = "发票编码")
    private String invoicecode;

    /**
     * 收款人
     */
    @LogData(alias = "收款人")
    private String scashier;

    /**
     * 复核人
     */
    @LogData(alias = "复核人")
    private String schecker;

    @LogData(alias = "备注")
    private String sremark;

    /**
     * 客户发票打印名称
     */
    @LogData(alias = "客户发票打印名称")
    private String sprintname;

    /**
     * 付款方识别号
     */
    @LogData(alias = "付款方识别号")
    private String spayercreditcode;

    /**
     * 付款方地址电话
     */
    @LogData(alias = "付款方地址电话")
    private String spayeraddr;

    /**
     * 付款方银行账户
     */
    @LogData(alias = "付款方银行账户")
    private String spayerbank;

    /**
     * 最后操作员
     */
    @LogData(alias = "最后操作员")
    private String lastoperator;

    /**
     * 最后操作员id
     */
    @LogData(alias = "最后操作员", mappedBy = "tbemploy", mappedByColumn = "susername")
    private Long lastoperatorid;

    /**
     * 关联发票（冲红发票）
     */
    @LogData(alias = "关联发票id")
    private Integer relateinvoiceid;


    /**
     * 经营主体id
     */
    @LogData(alias = "经营主体id", mappedBy = "tbbusinessentity", mappedByColumn = "sname")
    private Long businessentityid;

    /**
     * 经营主体名称
     */
    @LogData(alias = "经营主体名称")
    private String businessentityname;

    /**
     * 广告项目id
     */
    @LogData(alias = "广告项目id", mappedBy = "twadproject", mappedByColumn = "sname")
    private Long adprojectid;

    /**
     * 广告项目名称
     */
    private String adprojectname;

    /**
     * 并发标志
     */
    @Version
    private Long version;
}
