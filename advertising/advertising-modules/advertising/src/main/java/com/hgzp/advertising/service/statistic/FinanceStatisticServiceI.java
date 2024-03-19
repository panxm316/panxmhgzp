package com.hgzp.advertising.service.statistic;

import com.hgzp.advertising.pagemodel.statistic.vo.AdIncomeYearMeidaVO;
import com.hgzp.advertising.pagemodel.statistic.vo.AdreceivedVO;
import com.hgzp.core.model.Tbreportmodelitem;
import com.hgzp.core.page.Json;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;

/**
 * FinanceStatisticServiceIImpl
 * 创建人：lhl
 * 类描述：财务报表服务类
 * 创建日期：2024/1/15 13:04
 */
public interface FinanceStatisticServiceI {
    /**
     * 导出广告实收明细表
     * 方法功能: 导导出广告实收明细表
     *
     * @param
     * @return XSSFWorkbook
     * @author lhl
     * @date 2023/12/22
     */
    XSSFWorkbook exportAdvertisingRealIncomeExcel();

    /**
     * 获取导出的EXCEL文件名称
     */
    String getExcelFileName();

    /**
     * 导出广告实收明细表(按部门汇总)
     * 方法功能: 导出广告实收明细表(按部门汇总)
     *
     * @param
     * @return XSSFWorkbook
     * @author lhl
     * @date 2023/12/22
     */
    public AdreceivedVO exportAdvertisingIncomeDep();

    /**
     * 从报表配置记录读取汇总项
     * 方法功能: 从报表配置记录读取汇总项
     *
     * @param
     * @return XSSFWorkbook
     * @author lhl
     * @date 2023/12/22
     */
    public List<Tbreportmodelitem> getSumItemFromReport(String applyTo, String reportType);

    /**
     * 广告实收明细表按年度/业务汇总
     * 方法功能: 广告实收明细表按年度/业务汇总
     *
     * @param
     * @return
     * @author lhl
     * @date 2024/1/25
     */
    public AdIncomeYearMeidaVO exportAdvertisingYearMedia();

    /**
     * 广告实收明细表按年度/业务汇总导出EXCEL
     * 方法功能: 广告实收明细表按年度/业务汇总导出EXCEL
     *
     * @param
     * @return
     * @author lhl
     * @date 2024/1/26
     */
    public XSSFWorkbook exportAdvertisingYearMediaExcel();

    /**
     * 广告实收明细表按部门/业务汇总
     * 方法功能: 广告实收明细表按部门/业务汇总
     *
     * @param
     * @return
     * @author lhl
     * @date 2024/1/29
     */
    public AdIncomeYearMeidaVO exportAdvertisingDeptMedia();

    /**
     * 广告实收明细表按部门/业务汇总导出EXCEL
     * 方法功能: 广告实收明细表按部门/业务汇总导出EXCEL
     *
     * @param
     * @return
     * @author lhl
     * @date 2024/1/29
     */
    public XSSFWorkbook exportAdvertisingDeptMediaExcel();

    /**
     * 汇总部门、期间、公司实数金额
     * 方法功能: 汇总部门、期间、公司实数金额
     *
     * @param
     * @return
     * @author lhl
     * @date 2024/1/29
     */
    public BigDecimal summaryNamountapportionArray(List<String> ids, String companyId, String startTime, String endTime);
}