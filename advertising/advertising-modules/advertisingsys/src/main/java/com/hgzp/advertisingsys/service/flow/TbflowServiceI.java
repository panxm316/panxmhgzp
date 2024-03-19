package com.hgzp.advertisingsys.service.flow;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.core.model.Tbflow;
import com.hgzp.core.model.Tbflowcondition;
import com.hgzp.core.page.BaseQueryInfo;
import com.hgzp.service.common.IMyService;

import java.util.List;

/**
 * TbflowServiceI
 * 创建人：songly
 * 类描述：流程设置
 * 创建日期：2024/2/16 10:07
 */
public interface TbflowServiceI extends IMyService<Tbflow> {
    /**
     * 分页查询流程类型设置
     * 方法功能:
     *
     * @param page
     * @param baseQueryInfo
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.core.model.Tbflow>
     * @author songly
     * @date 2024/2/16 13:58
     */
    IPage<Tbflow> getFlowPageList(Page<Tbflow> page, BaseQueryInfo baseQueryInfo);

    /**
     * 流程设置列表
     * 方法功能:
     *
     * @param baseQueryInfo
     * @return java.util.List<com.hgzp.core.model.Tbflow>
     * @author songly
     * @date 2024/2/16 13:58
     */
    List<Tbflow> getFlowList(BaseQueryInfo baseQueryInfo);
    /**
     * 获取有效的流程类型
     * 方法功能:
     *
     * @param
     * @return java.util.List<com.hgzp.core.model.Tbflow>
     * @author songly
     * @date 2024/2/16 15:53
     */
    List<Tbflow> getFlowTypeValid();

    /**
     * 是否存在同名流程设置
     * 方法功能:
     *
     * @param tbflow
     * @return boolean
     * @author songly
     * @date 2024/2/16 13:58
     */
    boolean isExistFlow(Tbflow tbflow);

    /**
     * 根据流程key获取流程设置
     * 方法功能:
     *
     * @param flowKey
     * @return tbflow
     * @author songly
     * @date 2024/2/16 11:11
     */
    Tbflow getFlowTypeByKey(String flowKey);
}