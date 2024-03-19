package com.hgzp.advertising.pagemodel.ad.dto;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.hgzp.core.model.Tworderitem;
import com.hgzp.core.page.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static com.hgzp.core.constant.ValidateParam.*;

/**
 * <p>
 * 订单刊期表 DTO 数据传输对象
 * </p>
 *
 * @author wangxk
 * @since 2024-01-02
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Data
@Accessors(chain = true)
public class TworderitemDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = {edit.class, detail.class})
    private Long id;
    /**
     * 订单id
     */
    @NotNull(message = "订单id不能为空", groups = {add.class, edit.class, detail.class})
    private Long orderid;
    /**
     * 订单号
     */
    @NotBlank(message = "订单号不能为空", groups = {add.class, edit.class, detail.class})
    private String sordernum;
    /**
     * 合同号
     */
    @NotBlank(message = "合同号不能为空", groups = {add.class, edit.class, detail.class})
    private String scontractnum;
    /**
     * 创建者id
     */
    @NotNull(message = "创建者id不能为空", groups = {add.class, edit.class, detail.class})
    private Long createempid;
    /**
     * 创建者名称
     */
    @NotBlank(message = "创建者名称不能为空", groups = {add.class, edit.class, detail.class})
    private String createempname;
    /**
     * 创建日期
     */
    @NotNull(message = "创建日期不能为空", groups = {add.class, edit.class, detail.class})
    private Date createtime;
    /**
     * 录入方式 0-正常 1-广告预约 2-快速预约 3-补刊
     */
    @NotNull(message = "录入方式 0-正常 1-广告预约 2-快速预约 3-补刊不能为空", groups = {add.class, edit.class, detail.class})
    private Integer ibooktype;
    /**
     * 媒体类型key
     */
    @NotBlank(message = "媒体类型key不能为空", groups = {add.class, edit.class, detail.class})
    private String mediatypekey;
    /**
     * 媒体类型名称
     */
    @NotBlank(message = "媒体类型名称不能为空", groups = {add.class, edit.class, detail.class})
    private String mediatypename;
    /**
     * 媒体id
     */
    @NotNull(message = "媒体id不能为空", groups = {add.class, edit.class, detail.class})
    private Long mediaid;
    /**
     * 媒体名称
     */
    @NotBlank(message = "媒体名称不能为空", groups = {add.class, edit.class, detail.class})
    private String medianame;
    /**
     * 预见报开始日期
     */
    @NotNull(message = "预见报开始日期不能为空", groups = {add.class, edit.class, detail.class})
    private Date prestartdate;
    /**
     * 预见报结束日期
     */
    @NotNull(message = "预见报结束日期不能为空", groups = {add.class, edit.class, detail.class})
    private Date preenddate;
    /**
     * 叠次id
     */
    @NotNull(message = "叠次id不能为空", groups = {add.class, edit.class, detail.class})
    private Long foldid;
    /**
     * 叠次名称
     */
    @NotBlank(message = "叠次名称不能为空", groups = {add.class, edit.class, detail.class})
    private String foldname;
    /**
     * 叠次版本id
     */
    @NotNull(message = "叠次版本id不能为空", groups = {add.class, edit.class, detail.class})
    private Long foldareaverid;
    /**
     * 叠次版本名称
     */
    @NotBlank(message = "叠次版本名称不能为空", groups = {add.class, edit.class, detail.class})
    private String foldareavername;
    /**
     * 广告形式id
     */
    @NotNull(message = "广告形式id不能为空", groups = {add.class, edit.class, detail.class})
    private Long addisplayformid;
    /**
     * 广告形式名称
     */
    @NotBlank(message = "广告形式名称不能为空", groups = {add.class, edit.class, detail.class})
    private String addisplayformname;
    /**
     * 版面类别id
     */
    @NotNull(message = "版面类别id不能为空", groups = {add.class, edit.class, detail.class})
    private Long pagesortid;
    /**
     * 版面类别名称
     */
    @NotBlank(message = "版面类别名称不能为空", groups = {add.class, edit.class, detail.class})
    private String pagesortname;
    /**
     * 色彩id
     */
    @NotNull(message = "色彩id不能为空", groups = {add.class, edit.class, detail.class})
    private Long adcolorid;
    /**
     * 色彩名称
     */
    @NotBlank(message = "色彩名称不能为空", groups = {add.class, edit.class, detail.class})
    private String adcolorname;
    /**
     * 规格id
     */
    @NotNull(message = "规格id不能为空", groups = {add.class, edit.class, detail.class})
    private Long adspecid;
    /**
     * 规格名称
     */
    @NotBlank(message = "规格名称不能为空", groups = {add.class, edit.class, detail.class})
    private String adspecname;
    /**
     * 宽(cm)
     */
    @NotNull(message = "宽(cm)不能为空", groups = {add.class, edit.class, detail.class})
    private BigDecimal nwidth;
    /**
     * 高(cm)
     */
    @NotNull(message = "高(cm)不能为空", groups = {add.class, edit.class, detail.class})
    private BigDecimal nheight;
    /**
     * 星期id
     */
    @NotNull(message = "星期id不能为空", groups = {add.class, edit.class, detail.class})
    private Long weeksettingid;
    /**
     * 星期名称
     */
    @NotBlank(message = "星期名称不能为空", groups = {add.class, edit.class, detail.class})
    private String weeksettingname;
    /**
     * 版面id
     */
    @NotNull(message = "版面id不能为空", groups = {add.class, edit.class, detail.class})
    private Long foldpageplanid;
    /**
     * 广告标题
     */
    @NotBlank(message = "广告标题不能为空", groups = {add.class, edit.class, detail.class})
    private String sadtitle;
    /**
     * 广告内容
     */
    @NotBlank(message = "广告内容不能为空", groups = {add.class, edit.class, detail.class})
    private String sadcontent;
    /**
     * 版面名称
     */
    @NotBlank(message = "版面名称不能为空", groups = {add.class, edit.class, detail.class})
    private String foldpageplanname;
    /**
     * 刊例价
     */
    @NotNull(message = "刊例价不能为空", groups = {add.class, edit.class, detail.class})
    private BigDecimal baseprice;
    /**
     * 价格明细表id
     */
    @NotNull(message = "价格明细表id不能为空", groups = {add.class, edit.class, detail.class})
    private Long priceitemid;
    /**
     * 标准价格
     */
    @NotNull(message = "标准价格不能为空", groups = {add.class, edit.class, detail.class})
    private BigDecimal normalprice;
    /**
     * 应收金额
     */
    @NotNull(message = "应收金额不能为空", groups = {add.class, edit.class, detail.class})
    private BigDecimal amountreceivable;
    /**
     * 已收金额
     */
    @NotNull(message = "已收金额不能为空", groups = {add.class, edit.class, detail.class})
    private BigDecimal amountreceived;
    /**
     * 欠款金额
     */
    @NotNull(message = "欠款金额不能为空", groups = {add.class, edit.class, detail.class})
    private BigDecimal amountarrearage;
    /**
     * 折扣率
     */
    @NotNull(message = "折扣率不能为空", groups = {add.class, edit.class, detail.class})
    private BigDecimal ndiscountrate;
    /**
     * 业绩金额
     */
    @NotNull(message = "业绩金额不能为空", groups = {add.class, edit.class, detail.class})
    private BigDecimal amountachievement;
    /**
     * 业绩时间(用于后期统计)
     */
    @NotNull(message = "业绩时间(用于后期统计)不能为空", groups = {add.class, edit.class, detail.class})
    private Date dachievementdate;
    /**
     * 成本金额
     */
    @NotNull(message = "成本金额不能为空", groups = {add.class, edit.class, detail.class})
    private BigDecimal namountcost;
    /**
     * 加刊审批状态
     */
    @NotNull(message = "加刊审批状态不能为空", groups = {add.class, edit.class, detail.class})
    private Integer iaddapprovestatus;
    /**
     * 改刊审批状态
     */
    @NotNull(message = "改刊审批状态不能为空", groups = {add.class, edit.class, detail.class})
    private Integer ichangeapprovestatus;
    /**
     * 停刊审批状态
     */
    @NotNull(message = "停刊审批状态不能为空", groups = {add.class, edit.class, detail.class})
    private Integer istopapprovestatus;
    /**
     * 折扣审批状态
     */
    @NotNull(message = "折扣审批状态不能为空", groups = {add.class, edit.class, detail.class})
    private Integer idiscountapprovestatus;
    /**
     * 订单审批状态
     */
    @NotNull(message = "订单审批状态不能为空", groups = {add.class, edit.class, detail.class})
    private Integer iapprovestatus;
    /**
     * 发布状态
     */
    @NotNull(message = "发布状态不能为空", groups = {add.class, edit.class, detail.class})
    private Integer ipublishstatus;
    /**
     * 是否有效
     */
    @NotNull(message = "是否有效不能为空", groups = {add.class, edit.class, detail.class})
    private Boolean buse;
    /**
     * 是否删除
     */
    @NotNull(message = "是否删除不能为空", groups = {add.class, edit.class, detail.class})
    private Boolean bdelete;
    /**
     * 是否填报欠款原因
     */
    @NotNull(message = "是否填报欠款原因不能为空", groups = {add.class, edit.class, detail.class})
    private Boolean breportreason;
    /**
     * 备注
     */
    @NotBlank(message = "备注不能为空", groups = {add.class, edit.class, detail.class})
    private String sremark;
    /**
     * 刊期编码(自增列)
     */
    @NotNull(message = "刊期编码(自增列)不能为空", groups = {add.class, edit.class, detail.class})
    private Long iitemcode;
    /**
     * 刊发检测状态(0-正常 1-未刊发 2-刊发错误
     */
    @NotNull(message = "刊发检测状态(0-正常 1-未刊发 2-刊发错误不能为空", groups = {add.class, edit.class, detail.class})
    private Integer ipublishcheckstatus;
    /**
     * 刊发检测报告内容
     */
    @NotBlank(message = "刊发检测报告内容不能为空", groups = {add.class, edit.class, detail.class})
    private String spublishcheckcontent;
    /**
     * 并发标记
     */
    @NotNull(message = "并发标记不能为空", groups = {add.class, edit.class, detail.class})
    private Long version;

    /**
     * 归属业务员列表
     */
    @Valid
    @NotNull(message = "归属业务员列表不能为空", groups = {savePreOrder.class})
    private List<TworderitembelongDTO> orderitembelong;

    public static TworderitemDTO parseToDTO(Tworderitem entity) {
        TworderitemDTO TworderitemDTO = new TworderitemDTO();
        BeanUtils.copyProperties(entity, TworderitemDTO);
        return TworderitemDTO;
    }

    public static Tworderitem parseToEntity(TworderitemDTO dto) {
        Tworderitem entity = new Tworderitem();
        BeanUtils.copyProperties(dto, entity);
        if (entity.getId() == null) {
            entity.setId(IdWorker.getId());
        }
        return entity;
    }
}
