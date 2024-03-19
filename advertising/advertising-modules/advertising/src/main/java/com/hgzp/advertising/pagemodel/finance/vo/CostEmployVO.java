package com.hgzp.advertising.pagemodel.finance.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.hgzp.core.model.Twcostemploy;
import lombok.*;
import org.springframework.beans.BeanUtils;

/**
 * <p>
 * 成本表(人员) VO 前端页面视图对象
 * </p>
 *
 * @author muyn
 * @since 2024-03-15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CostEmployVO {

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
     * 导入时间
     */
    private Date dcreatetime;
    /**
     * 记账日期
     */
    private Date daccountdate;
    /**
     * 业务日期
     */
    private Date dbusinessdate;
    /**
     * 凭据单号
     */
    private String scredential;
    /**
     * 摘要
     */
    private String sdescription;
    /**
     * 借方名称
     */
    private String sborrowername;
    /**
     * 贷方名称
     */
    private String slendername;
    /**
     * 金额类型
     */
    private String scredentialtype;
    /**
     * 金额
     */
    private BigDecimal namount;
    /**
     * 分配金额
     */
    private BigDecimal namountallcate;
    /**
     * 导入历史ID
     */
    private Long bankaccounthistoryid;
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
    private Date dlastoperatedate;
    /**
     * 并发标记
     */
    private Long version;

    public static CostEmployVO parseToVO(Twcostemploy entity) {
        CostEmployVO TwcostemployVO = new CostEmployVO();
        BeanUtils.copyProperties(entity, TwcostemployVO);
        return TwcostemployVO;
    }

    public static Twcostemploy parseToEntity(CostEmployVO vo) {
        Twcostemploy entity = new Twcostemploy();
        BeanUtils.copyProperties(vo, entity);
        return entity;
    }
}
