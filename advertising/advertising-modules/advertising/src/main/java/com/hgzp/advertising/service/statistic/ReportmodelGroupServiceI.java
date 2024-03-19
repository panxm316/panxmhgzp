package com.hgzp.advertising.service.statistic;

import com.hgzp.advertising.pagemodel.statistic.dto.ReportmodelGroupDTO;
import com.hgzp.advertising.pagemodel.statistic.dto.SumObjectDTO;
import com.hgzp.advertising.pagemodel.statistic.vo.AdreceivedVO;
import com.hgzp.core.constant.ValidateParam;
import com.hgzp.core.model.Tbreportmodelgroup;
import com.hgzp.core.page.Json;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * ReportmodelGroupServiceI
 * 创建人：muyn
 * 类描述：报表模板组设置 服务类
 * 创建日期：2024/1/16 13:04
 */
public interface ReportmodelGroupServiceI {

    /**
     * 新增Tbreportmodelgroup
     *
     * @author muyn
     * @since 2024-01-16
     * @param reportmodelgroupDTO 报表模板组设置数据传输对象
     * @return void
     */
    void save(ReportmodelGroupDTO reportmodelgroupDTO);

    /**
     * reportmodelgroupDTO
     *
     * @author muyn
     * @since 2024-01-16
     * @param reportmodelgroupDTO 报表模板组设置数据传输对象
     * @return void
     */
    void delete(ReportmodelGroupDTO reportmodelgroupDTO);

    /**
     * 修改Tbreportmodelgroup
     *
     * @author muyn
     * @since 2024-01-16
     * @param reportmodelgroupDTO 报表模板组设置数据传输对象
     * @return void
     */
    void update(ReportmodelGroupDTO reportmodelgroupDTO);

    /**
     * 查询报表配置记录
     * 方法功能: 查询报表配置记录
     *
     * @param reportFormId
     * @return JSON
     * @author lhl
     * @date 2024/1/18
     */
    List<ReportmodelGroupDTO> getReportFormList(String reportFormId);

    /**
     * 启用报表配置记录
     * 方法功能: 启用报表配置记录
     *
     * @param reportId
     * @return
     * @author lhl
     * @date 2024/1/19
     */
    void enableReportForm(String reportId,String reportFormId);

    /**
     * 获取报表对应的配置记录
     * 方法功能: 获取报表对应的配置记录
     *
     * @param reportFormId
     * @return
     * @author lhl
     * @date 2024/1/23
     */
    public Tbreportmodelgroup getTbreportmodelgroup(String reportFormId);
 }