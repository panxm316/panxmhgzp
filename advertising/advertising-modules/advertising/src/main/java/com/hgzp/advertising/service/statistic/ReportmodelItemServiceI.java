package com.hgzp.advertising.service.statistic;

import com.hgzp.advertising.pagemodel.statistic.dto.ReportmodelItemDTO;
import com.hgzp.advertising.pagemodel.statistic.dto.SumObjectDTO;
import com.hgzp.core.model.Tbreportmodelitem;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.core.page.Json;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 报表模板明细设置 服务类
 * </p>
 *
 * @author muyn
 * @since 2024-01-16
 */
public interface ReportmodelItemServiceI extends IService<Tbreportmodelitem> {

    /**
     * 新增Tbreportmodelitem
     *
     * @author muyn
     * @since 2024-01-16
     * @param reportmodelitemDTO 报表模板明细设置数据传输对象
     * @return void
     */
    void save(ReportmodelItemDTO reportmodelitemDTO);

    /**
     * 删除Tbreportmodelitem
     *
     * @author muyn
     * @since 2024-01-16
     * @param reportmodelitemDTO 报表模板明细设置数据传输对象
     * @return void
     */
    void delete(ReportmodelItemDTO reportmodelitemDTO);

    /**
     * 修改Tbreportmodelitem
     *
     * @author muyn
     * @since 2024-01-16
     * @param reportmodelitemDTO 报表模板明细设置数据传输对象
     * @return void
     */
    void update(ReportmodelItemDTO reportmodelitemDTO);

    /**
     * 查询报表汇总项记录
     * 方法功能: 查询报表汇总项记录
     *
     * @param reportGroupId
     * @return JSON
     * @author lhl
     * @date 2024/1/19
     */
    List<ReportmodelItemDTO> getReportSumItemList(String reportGroupId,String reportType);

    /**
     * 配置部门/区域/业务
     * 方法功能: 配置部门/区域/业务
     *
     * @param sumObjectDTO
     * @return JSON
     * @author lhl
     * @date 2024/1/20
     */
    boolean addSumObject(SumObjectDTO sumObjectDTO);

    /**
     * 获取报表汇总项记录
     * 方法功能: 获取报表汇总项记录
     *
     * @param reportGroupId
     * @return JSON
     * @author lhl
     * @date 2024/1/23
     */
    List<Tbreportmodelitem> getReportmodeItem(String reportGroupId,String reportType);

    /**
     * 查询汇总项
     * 方法功能: 查询汇总项
     *
     * @param groupId,headName
     * @return JSON
     * @author lhl
     * @date 2024/1/25
     */
    public Tbreportmodelitem getTbreportmodelitemWithHeadName(String groupId, String headName);

}
