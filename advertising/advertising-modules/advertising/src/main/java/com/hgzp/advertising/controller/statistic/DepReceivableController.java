package com.hgzp.advertising.controller.statistic;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hgzp.advertising.pagemodel.business.dto.TaskQuotaAddDTO;
import com.hgzp.advertising.pagemodel.statistic.vo.AdreceivedVO;
import com.hgzp.advertising.service.statistic.DepReceivableServiceI;
import com.hgzp.advertising.service.statistic.FinanceStatisticServiceI;
import com.hgzp.core.model.Twcustomer;
import com.hgzp.core.page.Json;
import com.hgzp.pagemodel.ad.AdCustomerResourceVO;
import com.hgzp.pagemodel.schedule.OrderPublishQueryResultVo;
import com.hgzp.pagemodel.statistic.AdChangeVo;
import com.hgzp.pagemodel.statistic.SynQueryResultVO;
import com.hgzp.pagemodel.statistic.SynQueryVO;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * DepReceivableController
 * 创建人：lhl
 * 类描述：部门应收汇总报表控制器
 * 创建日期：2024/2/1 8:57
 *
 * @folder statistic/DepReceivableController
 */
@RestController
@RequestMapping("/statistic/depReceivable")
@Validated
@Component
public class DepReceivableController {
    @Autowired
    private DepReceivableServiceI  depReceivableServiceI;
    @Autowired
    FinanceStatisticServiceI financeStatisticServiceI;

    /**
     * 业务部门广告应收业绩完成汇总表
     * 方法功能: 业务部门广告应收业绩完成汇总表
     *
     * @param
     * @return
     * @author lhl
     * @date 2024/2/1
     */
    @GetMapping("/exportDeptReceivable")
    public Json<AdreceivedVO> exportDeptReceivable() {
        return  Json.success(depReceivableServiceI.exportDeptReceivable());
    }

    /**
     * 业务部门广告应收业绩完成汇总表导出EXCEL
     * 方法功能: 业务部门广告应收业绩完成汇总表导出EXCEL
     *
     * @param
     * @return
     * @author lhl
     * @date 2024/2/1
     */
    @GetMapping("/exportDeptReceivableExcel")
    public void exportDeptReceivableExcel(HttpServletResponse response) {
        try {
            XSSFWorkbook xssfWorkbook = depReceivableServiceI.exportDeptReceivableExcel();
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

    /**
     * 区域完成情况统计表
     * 方法功能: 区域完成情况统计表
     *
     * @param
     * @return
     * @author lhl
     * @date 2024/2/3
     */
    @GetMapping("/exportAreaReceivable")
    public Json<AdreceivedVO> exportAreaReceivable() {
        return  Json.success(depReceivableServiceI.exportAreaReceivable());
    }

    /**
     * 区域完成情况统计表导出EXCEL
     * 方法功能: 区域完成情况统计表导出EXCEL
     *
     * @param
     * @return
     * @author lhl
     * @date 2024/2/3
     */
    @GetMapping("/exportAreaReceivableExcel")
    public void exportAreaReceivableExcel(HttpServletResponse response) {
        try {
            XSSFWorkbook xssfWorkbook = depReceivableServiceI.exportAreaReceivableExcel();
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

    /**
     * 区域应收情况统计表
     * 方法功能: 区域应收情况统计表
     *
     * @param
     * @return
     * @author lhl
     * @date 2024/2/5
     */
    @GetMapping("/exportAreaIncomeReceivable")
    public Json<AdreceivedVO> exportAreaIncomeReceivable() {
        return  Json.success(depReceivableServiceI.exportAreaIncomeReceivable());
    }

    /**
     * 区域应收情况统计表导出EXCEL
     * 方法功能: 区域应收情况统计表导出EXCEL
     *
     * @param
     * @return
     * @author lhl
     * @date 2024/2/5
     */
    @GetMapping("/exportAreaIncomeReceivableExcel")
    public void exportAreaIncomeReceivableExcel(HttpServletResponse response) {
        try {
            XSSFWorkbook xssfWorkbook = depReceivableServiceI.exportAreaIncomeReceivableExcel();
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

    /**
     * 广告应收分析
     * 方法功能: 广告应收分析
     *
     * @param
     * @return
     * @author lhl
     * @date 2024/2/18
     */
    @GetMapping("/exportReceivableAnalysis")
    public Json<AdreceivedVO> exportReceivableAnalysis(String mediaId,String dateTime) {
        return  Json.success(depReceivableServiceI.exportReceivableAnalysis(mediaId,dateTime));
    }

    /**
     * 广告应收分析导出EXCEL
     * 方法功能: 广告应收分析导出EXCEL
     *
     * @param
     * @return
     * @author lhl
     * @date 2024/2/19
     */
    @GetMapping("/exportReceivableAnalysisExcel")
    public void exportReceivableAnalysisExcel(String mediaId,String dateTime,HttpServletResponse response) {
        try {
            XSSFWorkbook xssfWorkbook = depReceivableServiceI.exportReceivableAnalysisExcel(mediaId,dateTime);
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

    /**
     * 查询变动订单
     * 方法功能: 查询变动订单
     *
     * @param
     * @return
     * @author lhl
     * @date 2024/2/20
     */
    @GetMapping("/queryChangeOrder")
    public Json<List<AdChangeVo>> queryChangeOrder(String dateTime,int pageNum,int pageSize) {
        IPage<AdChangeVo> list = depReceivableServiceI.queryChangeOrder(dateTime,pageNum,pageSize);
        return Json.success(list);

    }

    /**
     * 查询变动订单导出EXCEL
     * 方法功能: 查询变动订单导出EXCEL
     *
     * @param
     * @return
     * @author lhl
     * @date 2024/2/21
     */
    @GetMapping("/exportChangeOrderExcel")
    public void exportChangeOrderExcel(String dateTime,int pageNum,int pageSize,HttpServletResponse response) {
        try {
            XSSFWorkbook xssfWorkbook = depReceivableServiceI.exportChangeOrderExcel(dateTime);
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

    /**
     * 综合查询
     * 方法功能: 综合查询
     *
     * @param
     * @return
     * @author lhl
     * @date 2024/2/26
     */
    @PostMapping("/synQuery")
    public Json<List<SynQueryResultVO>> synQuery(@RequestBody SynQueryVO vo) {
        IPage<SynQueryResultVO> list = depReceivableServiceI.synQuery(vo,vo.getPageNum(),vo.getPageSize());
        return Json.success(list);

    }

    /**
     * 客户来源查询
     * 方法功能: 客户来源查询
     *
     * @param
     * @return
     * @author lhl
     * @date 2024/3/12
     */
    @GetMapping("/customerResourceQuery")
    public Json<List<AdCustomerResourceVO>> customerResourceQuery(String level, String startTime, String endTime) {
        List<AdCustomerResourceVO> list = depReceivableServiceI.customerResourceQuery(level,startTime,endTime);
        return Json.success(list);
    }

    /**
     * 客户明细查询
     * 方法功能: 客户明细查询
     *
     * @param
     * @return
     * @author lhl
     * @date 2024/3/13
     */
    @GetMapping("/getCustomerDetail")
    public Json<List<Twcustomer>> getCustomerDetail(Long id, String startTime, String endTime) {
        List<Twcustomer> list = depReceivableServiceI.getCustomerDetail(id,startTime,endTime);
        return Json.success(list);
    }

}


