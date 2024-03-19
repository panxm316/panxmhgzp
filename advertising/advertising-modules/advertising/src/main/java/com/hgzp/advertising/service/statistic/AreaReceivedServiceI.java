package com.hgzp.advertising.service.statistic;

import com.hgzp.advertising.pagemodel.statistic.vo.AdreceivedVO;
import com.hgzp.core.page.Json;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * AreaReceivedServiceI
 * 创建人：lhl
 * 类描述：区域实收明细表服务类
 * 创建日期：2024/1/30 12:59
 */
public interface AreaReceivedServiceI {
    /**
     * 区域收明细表(广告+经营)
     * 方法功能: 区域收明细表(广告+经营)
     *
     * @param
     * @return
     * @author lhl
     * @date 2024/1/30
     */
    public AdreceivedVO exportAreaIncomeCombin();
    /**
     * 区域收明细表(日报经营)
     * 方法功能: 区域收明细表(日报经营)
     *
     * @param
     * @return
     * @author lhl
     * @date 2024/1/31
     */
    public AdreceivedVO exportAreaIncomeOp(String reportFormId);
    /**
     * 区域收明细表(日报经营)导出EXCEL
     * 方法功能: 区域收明细表(日报经营)导出EXCEL
     *
     * @param
     * @return
     * @author lhl
     * @date 2024/1/31
     */
    public XSSFWorkbook exportAreaIncomeOpExcel(String reportFormId);

}


