package com.hgzp.advertisingsys.pagemodel.flow.vo;

import com.hgzp.core.model.Tbflowcondition;
import com.hgzp.core.page.BaseQueryInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * FlowConditionVo
 * 创建人：songly
 * 类描述：工作流条件 Vo
 * 创建日期：2023/10/12 9:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlowConditionVo extends Tbflowcondition {

    private String queryKey; // 查询关键字
    /**
     * 条件类型(Money、SelectMultiDept、SelectMultiUser)名称
     */
    private String sconditiontypename;

    /**
     * 流程类型名称
     */
    private String sflowtypename;

}