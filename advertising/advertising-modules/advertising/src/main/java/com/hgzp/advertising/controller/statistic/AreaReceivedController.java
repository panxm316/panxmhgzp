package com.hgzp.advertising.controller.statistic;

import com.hgzp.advertising.pagemodel.statistic.vo.AdreceivedVO;
import com.hgzp.advertising.service.statistic.AreaReceivedServiceI;
import com.hgzp.advertising.service.statistic.FinanceStatisticServiceI;
import com.hgzp.advertising.service.statistic.ReportmodelGroupServiceI;
import com.hgzp.advertising.service.statistic.ReportmodelItemServiceI;
import com.hgzp.core.page.Json;
import com.hgzp.core.web.BaseController;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * AreaReceivedController
 * 创建人：lhl
 * 类描述：区域实收明细表统计控制器
 * 创建日期：2024/1/30 12:55
 *
 * @folder statistic/AreaReceivedController
 */
@RestController
@RequestMapping("/statistic/areareceived")
@Validated
@Component
public class AreaReceivedController extends BaseController {
    @Autowired
    private AreaReceivedServiceI areaReceivedServiceI;
    @Autowired
    FinanceStatisticServiceI financeStatisticServiceI;


    /**
     * 区域收明细表(广告+经营)
     * 方法功能: 区域收明细表(广告+经营)
     *
     * @param
     * @return
     * @author lhl
     * @date 2024/30
     */
    @GetMapping("/exportAreaIncomeCombin")
    public Json<AdreceivedVO> exportAreaIncomeCombin() {
        return  Json.success(areaReceivedServiceI.exportAreaIncomeCombin());
    }

     /**
     * 区域收明细表(日报经营)
     * 方法功能: 区域收明细表(日报经营)
     *
     * @param
     * @return
     * @author lhl
     * @date 2024/1/31
     */
    @GetMapping("/exportAreaIncomeOp")
    public Json<AdreceivedVO> exportAreaIncomeOp(String reportFormId) {
        return  Json.success(areaReceivedServiceI.exportAreaIncomeOp(reportFormId));
    }

    /**
     * 区域收明细表(日报经营)导出EXCEL
     * 方法功能: 区域收明细表(日报经营)导出EXCEL
     *
     * @param
     * @return
     * @author lhl
     * @date 2024/1/31
     */
    @GetMapping("/exportAreaIncomeOpExcel")
    public void exportAreaIncomeOpExcel(String reportFormId,HttpServletResponse response) {
        try {
            XSSFWorkbook xssfWorkbook = areaReceivedServiceI.exportAreaIncomeOpExcel(reportFormId);
            // 设置返回类型为excel
            response.setContentType("application/vnd.ms-excel; charset=UTF-8");
            response.setHeader("Content-Disposition", financeStatisticServiceI.getExcelFileName());
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            OutputStream output = response.getOutputStream();
            xssfWorkbook.write(output);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


