package com.hgzp.advertising.service.flow;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.core.model.Tbflow;
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
     * 根据流程key获取流程设置
     * 方法功能:
     *
     * @param flowKey
     * @return tbflow
     * @author songly
     * @date 2024/2/16 11:11
     */
    Tbflow getFlowTypeByKey(String flowKey);
    /**
     * 获取流程设置列表
     * 方法功能:
     * @author songly
     * @date 2024/3/8 8:27
     * @param
     * @return java.util.List<com.hgzp.core.model.Tbflow>
     */
    List<Tbflow> getFlowTypeList();
}