package com.hgzp.advertising.pagemodel.ad.vo;

import com.hgzp.core.model.Tworderitembelong;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 订单刊期归属表 VO 前端页面视图对象
 * </p>
 *
 * @author wangxk
 * @since 2024-01-03
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Data
@Accessors(chain = true)
public class TworderitembelongVO {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;
    /**
     * 订单明细id
     */
    private Long orderitemid;
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
     * 部门id
     */
    private Long deptid;
    /**
     * 部门名称
     */
    private String deptname;
    /**
     * 业务id
     */
    private Long employid;
    /**
     * 业务员名称
     */
    private String employname;
    /**
     * 类型（0-直客业务员 1-代理业务员 2-内容生产方业务员）
     */
    private Integer employtype;
    /**
     * 业绩比例
     */
    private BigDecimal archievementrate;
    /**
     * 任务比例
     */
    private BigDecimal taskrate;
    /**
     * 佣金比例
     */
    private BigDecimal commissionrate;
    /**
     * 是否主业务员
     */
    private Boolean bprimary;
    /**
     * 备注
     */
    private String sremark;

    public static TworderitembelongVO parseToVO(Tworderitembelong entity) {
        TworderitembelongVO TworderitembelongVO = new TworderitembelongVO();
        BeanUtils.copyProperties(entity, TworderitembelongVO);
        return TworderitembelongVO;
    }

    public static Tworderitembelong parseToEntity(TworderitembelongVO vo) {
        Tworderitembelong entity = new Tworderitembelong();
        BeanUtils.copyProperties(vo, entity);
        return entity;
    }
}
