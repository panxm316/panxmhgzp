package com.hgzp.advertising.controller.statistic;

import com.hgzp.advertising.pagemodel.business.dto.TwtaskDTO;
import com.hgzp.advertising.pagemodel.statistic.vo.AdIncomeYearMeidaVO;
import com.hgzp.advertising.pagemodel.statistic.vo.AdreceivedVO;
import com.hgzp.advertising.service.statistic.FinanceStatisticServiceI;
import com.hgzp.core.page.Json;
import com.hgzp.core.web.BaseController;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * FinanceStatisticController
 * 创建人：lhl
 * 类描述： 财务报表统计 Controller
 * 创建日期：2024/1/15
 *
 * @folder statistic/FinanceStatisticController
 */
@RestController
@RequestMapping("/statistic/financestatistic")
@Validated
@Component
public class FinanceStatisticController  extends BaseController {

	@Autowired
	FinanceStatisticServiceI financeStatisticServiceI;

	private static final Logger logger = LoggerFactory.getLogger(FinanceStatisticController.class);

	/**
	 * 导出广告实收明细表
	 * 方法功能: 导出广告实收明细表
	 *
	 * @param response
	 * @return
	 * @author lhl
	 * @date 2024/1/15
	 */
	@GetMapping("/exportAdvertisingRealIncomeExcel")
	public void exportAdvertisingRealIncomeExcel(HttpServletResponse response) {
		try {
			XSSFWorkbook xssfWorkbook = financeStatisticServiceI.exportAdvertisingRealIncomeExcel();
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
	 * 广告实收明细表按部门汇总
	 * 方法功能: 广告实收明细表按部门汇总
	 *
	 * @param
	 * @return
	 * @author lhl
	 * @date 2024/1/15
	 */
	@GetMapping("/exportAdvertisingIncomeDep")
	public Json<AdreceivedVO> exportAdvertisingIncomeDep() {
		return  Json.success(financeStatisticServiceI.exportAdvertisingIncomeDep());
	}

	/**
	 * 广告实收明细表按年度/业务汇总
	 * 方法功能: 广告实收明细表按年度/业务汇总
	 *
	 * @param
	 * @return
	 * @author lhl
	 * @date 2024/1/25
	 */
	@GetMapping("/exportAdvertisingYearMedia")
	public Json<AdIncomeYearMeidaVO> exportAdvertisingYearMedia() {
		return  Json.success(financeStatisticServiceI.exportAdvertisingYearMedia());
	}

	/**
	 * 广告实收明细表按年度/业务汇总导出EXCEL
	 * 方法功能: 广告实收明细表按年度/业务汇总导出EXCEL
	 *
	 * @param
	 * @return
	 * @author lhl
	 * @date 2024/1/26
	 */
	@GetMapping("/exportAdvertisingYearMediaExcel")
	public void exportAdvertisingYearMediaExcel(HttpServletResponse response) {
		try {
			XSSFWorkbook xssfWorkbook = financeStatisticServiceI.exportAdvertisingYearMediaExcel();
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
	 * 广告实收明细表按部门/业务汇总
	 * 方法功能: 广告实收明细表按部门/业务汇总
	 *
	 * @param
	 * @return
	 * @author lhl
	 * @date 2024/1/29
	 */
	@GetMapping("/exportAdvertisingDeptMedia")
	public Json<AdIncomeYearMeidaVO> exportAdvertisingDeptMedia() {
		return  Json.success(financeStatisticServiceI.exportAdvertisingDeptMedia());
	}

	/**
	 * 广告实收明细表按部门/业务汇总导出EXCEL
	 * 方法功能: 广告实收明细表按部门/业务汇总导出EXCEL
	 *
	 * @param
	 * @return
	 * @author lhl
	 * @date 2024/1/29
	 */
	@GetMapping("/exportAdvertisingDeptMediaExcel")
	public void exportAdvertisingDeptMediaExcel(HttpServletResponse response) {
		try {
			XSSFWorkbook xssfWorkbook = financeStatisticServiceI.exportAdvertisingDeptMediaExcel();
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