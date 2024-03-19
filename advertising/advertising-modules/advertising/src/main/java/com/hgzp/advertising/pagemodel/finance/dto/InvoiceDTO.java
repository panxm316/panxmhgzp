package com.hgzp.advertising.pagemodel.finance.dto;

import com.hgzp.advertising.pagemodel.ad.vo.OrderforPreinvoapplyVO;
import com.hgzp.core.annotation.LogData;
import com.hgzp.core.constant.ValidateParam;
import com.hgzp.core.model.Twinvoice;
import com.hgzp.core.page.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 发票表 DTO 数据传输对象
 * </p>
 *
 * @author muyn
 * @since 2023-12-15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDTO extends BaseDTO {

    /**
     * 主键
     */
    @NotNull(message = "id不能为空", groups = {ValidateParam.edit.class, ValidateParam.detail.class})
    private Long id;
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
     * 0-直接客户、1-代理公司、2-内容生产方
     */
    private Integer icusttype;
    /**
     * 发票金额
     */
    private BigDecimal namount;
    /**
     * 创建者id
     */
    private Long operatorid;
    /**
     * 创建者
     */
    private String operator;
    /**
     * 创建时间
     */
    private Date dcreatetime;
    /**
     * 发票类型 0-手开，1-预开 2-挂开 3-冲红 4-预收费 5-预开完成
     */
    private Integer itype;
    /**
     * 发票样式  2-普通发票 0-专用发票 51-电子发票 81-数电专票 82-数电普票
     */
    private Integer istype;
    /**
     * 发票状态 1-有效、0-无效，2-冲红退回
     */
    private Integer istatus;
    /**
     * 付款方式id
     */
    private Long paymethodid;
    /**
     * 付款方式
     */
    private String paymethodname;
    /**
     * 开票项目id
     */
    @NotNull(message = "开票项目id不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private Long printitemid;
    /**
     * 开票项目
     */
    @NotBlank(message = "开票项目不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private String printitemname;
    /**
     * 税收编码（取开票项目中对应的值）
     */
    @NotBlank(message = "税收编码不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private String staxcode;
    /**
     * 业务员id
     */
    private Long employid;
    /**
     * 业务员名称
     */
    private String employname;
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
    @NotBlank(message = "收款人不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private String scashier;
    /**
     * 复核人
     */
    @NotBlank(message = "复核人不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private String schecker;
    /**
     * 备注
     */
    private String sremark;
    /**
     * 客户发票打印名称
     */
    private String sprintname;
    /**
     * 付款方识别号
     */
    @NotBlank(message = "付款方识别号不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private String spayercreditcode;
    /**
     * 付款方地址电话
     */
    @NotBlank(message = "付款方地址电话不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private String spayeraddr;
    /**
     * 付款方银行账户
     */
    @NotBlank(message = "付款方银行账户不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private String spayerbank;
    /**
     * 最后操作员id
     */
    private Long lastoperatorid;
    /**
     * 最后操作员
     */
    private String lastoperator;
    /**
     * 关联发票(红冲发票)
     */
    private Long relateinvoiceid;
    /**
     * 经营主体ID
     */
    private Long businessentityid;
    /**
     * 经营主体
     */
    private String businessentityname;
    /**
     * 电子发票sha1
     */
    private String sinvoicefilesha1;
    /**
     * 电子发票原文件名
     */
    private String soriginalfile;
    /**
     * 并发标志
     */
    private Long version;

    /**
     * 税目
     */
    private String taxitems;

    /**
     * 税率
     */
    private String taxrate;
    /**
     * 上传的附件
     */
    private List<InvoiceFilesDTO> fileList;
    /**
     * 已用金额（关联twcustomercharge，查询所有该发票号数据计算）
     */
    private BigDecimal namounspent;

    /**
     * 剩余金额（预收款：关联twcustomercharge，查询该发票剩余金额）
     */
    private BigDecimal namountbalance;
    /**
     * 欠款金额（预开发票：发票金额减去twcustomercharge中所有入账金额，【如果找不到该发票数据则入账金额为0】）
     */
    private BigDecimal amountarrearage;
    /**
     * 入账金额（twcustomercharge中所有入账金额）
     */
    private BigDecimal namountreceived;

    /**
     * 广告项目id
     */
    private Long adprojectid;

    /**
     * 广告项目名称
     */
    private String adprojectname;

    /**
     * 合同VO
     */
    @NotNull(message = "合同号不能为空", groups = {ValidateParam.add.class, ValidateParam.edit.class})
    private List<OrderforPreinvoapplyVO> contractVos;

    public static InvoiceDTO parseToDTO(Twinvoice entity) {
        InvoiceDTO TwinvoiceDTO = new InvoiceDTO();
        BeanUtils.copyProperties(entity, TwinvoiceDTO);
        return TwinvoiceDTO;
    }

    public static Twinvoice parseToEntity(InvoiceDTO dto) {
        Twinvoice entity = new Twinvoice();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }
}
