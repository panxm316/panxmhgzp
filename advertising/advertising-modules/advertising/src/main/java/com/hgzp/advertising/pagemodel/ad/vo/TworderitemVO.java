package com.hgzp.advertising.pagemodel.ad.vo;

import com.hgzp.core.model.Tworderitem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 订单刊期表 VO 前端页面视图对象
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
public class TworderitemVO {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;
    /**
     * 订单id
     */
    private Long orderid;
    /**
     * 订单号
     */
    private String sordernum;
    /**
     * 合同号
     */
    private String scontractnum;
    /**
     * 创建者id
     */
    private Long createempid;
    /**
     * 创建者名称
     */
    private String createempname;
    /**
     * 创建日期
     */
    private Date createtime;
    /**
     * 录入方式 0-正常 1-广告预约 2-快速预约 3-补刊
     */
    private Integer ibooktype;
    /**
     * 媒体类型key
     */
    private String mediatypekey;
    /**
     * 媒体类型名称
     */
    private String mediatypename;
    /**
     * 媒体id
     */
    private Long mediaid;
    /**
     * 媒体名称
     */
    private String medianame;
    /**
     * 预见报开始日期
     */
    private Date prestartdate;
    /**
     * 预见报结束日期
     */
    private Date preenddate;
    /**
     * 叠次id
     */
    private Long foldid;
    /**
     * 叠次名称
     */
    private String foldname;
    /**
     * 叠次版本id
     */
    private Long foldareaverid;
    /**
     * 叠次版本名称
     */
    private String foldareavername;
    /**
     * 广告形式id
     */
    private Long addisplayformid;
    /**
     * 广告形式名称
     */
    private String addisplayformname;
    /**
     * 版面类别id
     */
    private Long pagesortid;
    /**
     * 版面类别名称
     */
    private String pagesortname;
    /**
     * 色彩id
     */
    private Long adcolorid;
    /**
     * 色彩名称
     */
    private String adcolorname;
    /**
     * 规格id
     */
    private Long adspecid;
    /**
     * 规格名称
     */
    private String adspecname;
    /**
     * 宽(cm)
     */
    private BigDecimal nwidth;
    /**
     * 高(cm)
     */
    private BigDecimal nheight;
    /**
     * 星期id
     */
    private Long weeksettingid;
    /**
     * 星期名称
     */
    private String weeksettingname;
    /**
     * 版面id
     */
    private Long foldpageplanid;
    /**
     * 广告标题
     */
    private String sadtitle;
    /**
     * 广告内容
     */
    private String sadcontent;
    /**
     * 版面名称
     */
    private String foldpageplanname;
    /**
     * 刊例价
     */
    private BigDecimal baseprice;
    /**
     * 价格明细表id
     */
    private Long priceitemid;
    /**
     * 标准价格
     */
    private BigDecimal normalprice;
    /**
     * 应收金额
     */
    private BigDecimal amountreceivable;
    /**
     * 已收金额
     */
    private BigDecimal amountreceived;
    /**
     * 欠款金额
     */
    private BigDecimal amountarrearage;
    /**
     * 折扣率
     */
    private BigDecimal ndiscountrate;
    /**
     * 业绩金额
     */
    private BigDecimal amountachievement;
    /**
     * 业绩时间(用于后期统计)
     */
    private Date dachievementdate;
    /**
     * 成本金额
     */
    private BigDecimal namountcost;
    /**
     * 加刊审批状态
     */
    private Integer iaddapprovestatus;
    /**
     * 改刊审批状态
     */
    private Integer ichangeapprovestatus;
    /**
     * 停刊审批状态
     */
    private Integer istopapprovestatus;
    /**
     * 折扣审批状态
     */
    private Integer idiscountapprovestatus;
    /**
     * 订单审批状态
     */
    private Integer iapprovestatus;
    /**
     * 发布状态
     */
    private Integer ipublishstatus;
    /**
     * 是否有效
     */
    private Boolean buse;
    /**
     * 是否删除
     */
    private Boolean bdelete;
    /**
     * 是否填报欠款原因
     */
    private Boolean breportreason;
    /**
     * 备注
     */
    private String sremark;
    /**
     * 刊期编码(自增列)
     */
    private Long iitemcode;
    /**
     * 刊发检测状态(0-正常 1-未刊发 2-刊发错误
     */
    private Integer ipublishcheckstatus;
    /**
     * 刊发检测报告内容
     */
    private String spublishcheckcontent;
    /**
     * 并发标记
     */
    private Long version;

    // --------- 新增属性 ------------
    private List<TworderitembelongVO> orderitembelong;

    public static TworderitemVO parseToVO(Tworderitem entity) {
        TworderitemVO TworderitemVO = new TworderitemVO();
        BeanUtils.copyProperties(entity, TworderitemVO);
        return TworderitemVO;
    }

    public static Tworderitem parseToEntity(TworderitemVO vo) {
        Tworderitem entity = new Tworderitem();
        BeanUtils.copyProperties(vo, entity);
        return entity;
    }
}
