package com.hgzp.advertisingsys.pagemodel.flow.vo;
import com.hgzp.core.model.Tbflowcondition;
import com.hgzp.core.page.DataCombo;
import lombok.Data;

import java.util.List;

/**
 tbflowconditionEx
 创建人：songly
 类描述：带下拉信息的条件类
 创建日期：2023/10/18 10:04
 */
@Data
public class FlowConditionEx extends Tbflowcondition {
    private List<DataCombo> data;
}