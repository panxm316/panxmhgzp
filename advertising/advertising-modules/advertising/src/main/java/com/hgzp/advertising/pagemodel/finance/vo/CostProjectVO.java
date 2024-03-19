package com.hgzp.advertising.pagemodel.finance.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.hgzp.core.model.Twcostproject;
import lombok.*;
import org.springframework.beans.BeanUtils;

/**
 * <p>
 * 成本表(项目) VO 前端页面视图对象
 * </p>
 *
 * @author muyn
 * @since 2024-03-15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CostProjectVO {

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
     * 项目代码
     */
    private String sprojectcode;
    /**
     * 活动名称
     */
    private String sprojectname;
    /**
     * 科目名称
     */
    private String ssubjectname;
    /**
     * 科目代码
     */
    private String ssubjectcode;
    /**
     * 期初借方
     */
    private String sfirstborrower;
    /**
     * 期初贷方
     */
    private String sfirstlender;
    /**
     * 本期借方
     */
    private BigDecimal ncurrentborrower;
    /**
     * 分配金额
     */
    private BigDecimal namountallcate;
    /**
     * 本期贷方
     */
    private BigDecimal ncurrentlender;
    /**
     * 本年借方
     */
    private BigDecimal nyearborrower;
    /**
     * 本年贷方
     */
    private BigDecimal nyearlender;
    /**
     * 期末借方
     */
    private BigDecimal nendborrower;
    /**
     * 期末贷方
     */
    private BigDecimal nendlender;
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

    public static CostProjectVO parseToVO(Twcostproject entity) {
        CostProjectVO TwcostprojectVO = new CostProjectVO();
        BeanUtils.copyProperties(entity, TwcostprojectVO);
        return TwcostprojectVO;
    }

    public static Twcostproject parseToEntity(CostProjectVO vo) {
        Twcostproject entity = new Twcostproject();
        BeanUtils.copyProperties(vo, entity);
        return entity;
    }
}
