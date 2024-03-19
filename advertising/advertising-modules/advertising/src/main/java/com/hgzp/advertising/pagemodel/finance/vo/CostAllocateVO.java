package com.hgzp.advertising.pagemodel.finance.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.hgzp.core.model.Twcostallocate;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

/**
 * <p>
 * 成本分配明细表 VO 前端页面视图对象
 * </p>
 *
 * @author muyn
 * @since 2024-03-18
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Data
@Accessors(chain = true)
public class CostAllocateVO {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;
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
    private Date dcreatetime;
    /**
     * 成本表id
     */
    private Long costid;
    /**
     * 成本类型(employ,project)
     */
    private String costtype;
    /**
     * 分配金额
     */
    private BigDecimal namountallcate;
    /**
     * 订单明细id
     */
    private Long orderitemid;
    /**
     * 说明
     */
    private String sdescription;
    /**
     * 状态(0-待提交 1-已提交 2-已确认）
     */
    private Integer istatus;
    /**
     * 最后操作员id
     */
    private Long lastoperatorid;
    /**
     * 最后操作员
     */
    private String lastoperator;
    /**
     * 最后修改日期
     */
    private Date dlastmodifydate;
    /**
     * 并发标记
     */
    private Long version;

    public static CostAllocateVO parseToVO(Twcostallocate entity) {
        CostAllocateVO TwcostallocateVO = new CostAllocateVO();
        BeanUtils.copyProperties(entity, TwcostallocateVO);
        return TwcostallocateVO;
    }

    public static Twcostallocate parseToEntity(CostAllocateVO vo) {
        Twcostallocate entity = new Twcostallocate();
        BeanUtils.copyProperties(vo, entity);
        return entity;
    }
}
