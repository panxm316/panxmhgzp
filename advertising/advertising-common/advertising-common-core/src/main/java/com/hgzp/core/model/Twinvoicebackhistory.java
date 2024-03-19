package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.hgzp.core.annotation.LogData;
import com.hgzp.utils.model.LoginUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 发票(收款)回退历史表
 * </p>
 *
 * @author wangxk
 * @since 2023-12-26
 */
@Getter
@Setter
@NoArgsConstructor
@LogData(alias = "发票(收款)回退历史表")
public class Twinvoicebackhistory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @LogData(alias = "主键")
    private Long id;

    /**
     * 创建者id
     */
    @LogData(alias = "创建者id")
    private Long createempid;

    /**
     * 创建者名称
     */
    @LogData(alias = "创建者名称")
    private String createempname;

    /**
     * 创建日期
     */
    @LogData(alias = "创建日期")
    private Date dcreatetime;

    /**
     * 分摊明细表id
     */
    @LogData(alias = "分摊明细表id")
    private Long orderapportiondetailid;

    /**
     * 收费表id
     */
    @LogData(alias = "收费表id")
    private Long customerchargeid;

    /**
     * 发票表id
     */
    @LogData(alias = "发票表id")
    private Long invoiceid;

    /**
     * 发票号
     */
    @LogData(alias = "发票号")
    private String invoice;

    /**
     * 原分摊金额(退回金额)
     */
    @LogData(alias = "原分摊金额(退回金额)")
    private BigDecimal namountapportion;

    /**
     * 订单id
     */
    @LogData(alias = "订单id")
    private Long orderid;

    /**
     * 刊期id
     */
    @LogData(alias = "刊期id")
    private Long orderitemid;

    /**
     * 刊期编码
     */
    @LogData(alias = "刊期编码")
    private Long orderitemcode;

    /**
     * 合同号
     */
    @LogData(alias = "合同号")
    private String scontractnum;

    /**
     * 分摊日期
     */
    @LogData(alias = "分摊日期")
    private Date dapportiondate;

    /**
     * 分摊种类(0-预收款 1-直接收款 2-银行流水)
     */
    @LogData(alias = "分摊种类(0-预收款 1-直接收款 2-银行流水)")
    private Integer sapportiontype;

    /**
     * 说明
     */
    @LogData(alias = "说明")
    private String sdescription;

    /**
     * 方法功能:退回分摊（revertWriteOff）使用。删除分摊表该记录前，将相关信息插入分摊历史表twinvoicebackhistory
     *
     * @param sdesc
     * @param apportionDetail
     * @param loginUser
     * @return
     * @author yanz
     * @date 2024/1/4 10:16
     */
    public Twinvoicebackhistory(String sdesc, Tworderapportiondetail apportionDetail, LoginUser loginUser) {
        BeanUtils.copyProperties(apportionDetail, this);
        this.setCreateempid(loginUser.getUserid());
        this.setCreateempname(loginUser.getUsername());
        this.setDcreatetime(new Date());
        this.setOrderapportiondetailid(apportionDetail.getId());
        this.setSdescription(sdesc);
    }
}
