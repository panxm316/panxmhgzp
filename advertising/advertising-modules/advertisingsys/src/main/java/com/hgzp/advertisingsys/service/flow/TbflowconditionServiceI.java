package com.hgzp.advertisingsys.service.flow;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertisingsys.pagemodel.flow.vo.FlowConditionVo;
import com.hgzp.advertisingsys.pagemodel.flow.vo.FlowConditionEx;
import com.hgzp.core.model.Tbflowcondition;
import com.hgzp.service.common.IMyService;

import java.util.List;

/**
 * <p>
 * 工作流条件表 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-10-12
 */
public interface TbflowconditionServiceI extends IMyService<Tbflowcondition> {

    /**
     * getPageSizePageList
     * 方法功能:  分页 获取工作流条件
     * @author songly
     * @date 2023/10/12 9:50
     * @param vo
     * @return
     */
    IPage<FlowConditionVo> getFlowConditionPageList(Page<Tbflowcondition> page, FlowConditionVo vo);
    /**
     * getFlowConditionList
     * 方法功能: 根据分组获取条件信息
     * @author songly
     * @date 2023/10/13 15:13
     * @param sflowtype
     * @return java.util.List<com.hgzp.core.model.Tbflowcondition>
     */
    List<FlowConditionEx> getFlowConditionList(String sflowtype);
    /**
     * deleteFlowConditionByIds
     * 方法功能:据id批量删除
     *
     * @param idList
     * @return boolean
     * @author yanz
     * @date 2023/8/25 10:52
     */
    boolean deleteFlowConditionByIds(List<String> idList);

   boolean isExistFlowCondition(Tbflowcondition tbflowcondition);
}
