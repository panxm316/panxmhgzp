package com.hgzp.advertising.service.business;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hgzp.advertising.pagemodel.business.dto.WorkReportsDTO;
import com.hgzp.advertising.pagemodel.business.vo.CustomerWorkReportsVO;
import com.hgzp.advertising.pagemodel.business.vo.WorkReportsVO;
import com.hgzp.core.model.Twworkreports;

/**
 * <p>
 * 工作报告 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-10-27
 */
public interface TwworkreportsServiceI extends IService<Twworkreports> {
    /**
     * 方法功能: 根据查询条件获取工作报告分页列表
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.advertising.pagemodel.business.dto.WorkReportsDTO>
     * @author suny
     * @date 2023/11/7 10:11
     */
    IPage<WorkReportsDTO> getWorkReportPageList(Page<Twworkreports> page, WorkReportsVO query) throws Exception;

    /**
     * 方法功能: 新增保存工作报告
     *
     * @param entity
     * @return void
     * @author suny
     * @date 2023/10/30 9:43
     */
    void saveWorkReport(WorkReportsDTO entity) throws Exception;

    /**
     * 方法功能: 修改更新工作报告
     *
     * @param entity
     * @return void
     * @author suny
     * @date 2023/10/30 9:43
     */
    void updateWorkReport(WorkReportsDTO entity) throws Exception;

    /**
     * 方法功能: 提交保存审核信息
     *
     * @param entity
     * @return void
     * @author suny
     * @date 2023/11/2 10:00
     */
    void saveCheckInfo(WorkReportsDTO entity) throws Exception;

    /**
     * 方法功能:  删除选择的工作报告和相应的附件
     *
     * @param ids
     * @return void
     * @author suny
     * @date 2023/11/1 15:56
     */
    void deleteWorkReport(String ids) throws Exception;

    /**
     * 方法功能:  提交审核
     *
     * @param ids
     * @return void
     * @author suny
     * @date 2023/11/1 14:51
     */
    void updateCheck(String ids);

    /**
     * 更新工作报告表中的客户id和客户名称
     * 方法功能:
     *
     * @param customerIds
     * @param newcustomerId
     * @param newcustomername
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2024/1/18 10:12
     */
    void updateWorkreportCustomer(String customerIds, Long newcustomerId, String newcustomername) throws Exception;

    /**
     * 获取客户的工作报告分页列表
     * 方法功能:
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.advertising.pagemodel.business.dto.WorkReportsDTO>
     * @author songly
     * @date 2024/3/7 11:13
     */
    IPage<WorkReportsDTO> getCustomerWorkReportPageList(Page<Twworkreports> page, CustomerWorkReportsVO query) throws Exception;

    /**
     * 根据id获取工作报告
     * 方法功能: 根据id获取工作报告
     *
     * @param id
     * @return com.hgzp.advertising.pagemodel.business.dto.WorkReportsDTO
     * @throws Exception
     */
    WorkReportsDTO getWorkReportById(String id) throws Exception;
}
