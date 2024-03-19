package com.hgzp.advertising.service.flow;

import com.hgzp.core.model.Tbflowcondition;
import com.hgzp.service.common.IMyService;

import java.util.List;

/**
 * @author new wei
 * @date 2023/11/6 12:51
 */
public interface TbflowconditionServiceI extends IMyService<Tbflowcondition> {
    /**
     * getFlowConditionList
     * 方法功能: 根据分组获取条件信息
     * @author songly
     * @date 2023/11/06 15:13
     * @param sflowtype
     * @return java.util.List<com.hgzp.core.model.Tbflowcondition>
     */
    List<Tbflowcondition> getFlowConditionList(String sflowtype);
}
