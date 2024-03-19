package com.hgzp.advertising.pagemodel.ad.vo;

import com.hgzp.core.annotation.LogData;
import com.hgzp.core.model.Twadproject;
import com.hgzp.core.model.Twadprojectfiles;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 AdprojectVO
 创建人：songly
 类描述：TODO
 创建日期：2024/3/15 18:38
 */
@Data
public class AdprojectVO extends Twadproject {
    /**
     * 合同类型(销售合同、采购合同、互换合同、框架合同)
     */
    private String contracttypename;

    /**
     * 销售合同类型(常规合同、认刊书)
     */
    private String salecontracttypename;

    /**
     * 用章类型(多选)(公章、合同专用章、经营合同专用章、法人章等)
     */
    private String stamptypename;
    /**     * 项目文件列表     */
    private List<AdprojectfilesVO> projectfiles;
    /**
     * 项目成本盈余(项目成本-订单明细中的成本之和)
     */
    private BigDecimal nprojectcostresidue;
}