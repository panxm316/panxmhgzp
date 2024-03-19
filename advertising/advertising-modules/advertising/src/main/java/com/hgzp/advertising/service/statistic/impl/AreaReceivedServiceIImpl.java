package com.hgzp.advertising.service.statistic.impl;

import com.hgzp.advertising.pagemodel.statistic.vo.AdreceivedItem;
import com.hgzp.advertising.pagemodel.statistic.vo.AdreceivedVO;
import com.hgzp.advertising.service.finance.TbbusinessentityServiceI;
import com.hgzp.advertising.service.statistic.AreaReceivedServiceI;
import com.hgzp.advertising.service.statistic.FinanceStatisticServiceI;
import com.hgzp.advertising.utils.DateUtils;
import com.hgzp.advertising.utils.MoneyUtils;
import com.hgzp.core.model.Tbreportmodelitem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * AreaReceivedServiceIImpl
 * 创建人：lhl
 * 类描述：区域广告实收明细表服务实现类
 * 创建日期：2024/1/30 13:08
 */
@Service
public class AreaReceivedServiceIImpl implements AreaReceivedServiceI {
    @Autowired
    FinanceStatisticServiceI  financeStatisticServiceI;
    @Autowired
    TbbusinessentityServiceI  tbbusinessentityServiceI;

    private String hadeTitle[] = {"名称", "本月数", "累计数"};

    @Override
    public AdreceivedVO exportAreaIncomeCombin() {
        AdreceivedVO  adreceivedVO = new AdreceivedVO();
        adreceivedVO.setReportDate(DateUtils.transferDate2StringCn(new Date()));
        List<Tbreportmodelitem> tbreportmodelitemList = financeStatisticServiceI.getSumItemFromReport("3","公司");
        if( null == tbreportmodelitemList || tbreportmodelitemList.size() <= 0 ) {
            return  adreceivedVO;
        }
        adreceivedVO.setCompany("广告+经营");
        adreceivedVO.setTitle("区域实收明细表(广告+经营)");
        List<AdreceivedItem> adreceivedItemList = new ArrayList<AdreceivedItem>();
        tbreportmodelitemList = financeStatisticServiceI.getSumItemFromReport("3","部门");
        Date  beginDate = DateUtils.getBeginDayOfMonth();
        Date  endDate = DateUtils.getEndDayOfMonth();
        String startTime = DateUtils.transferDate2String(beginDate);
        String endTime = DateUtils.transferDate2String(endDate);
        for( int i = 0; i < tbreportmodelitemList.size(); i++ ) {
            Tbreportmodelitem tbreportmodelitem = tbreportmodelitemList.get(i);
            AdreceivedItem item  = new AdreceivedItem();
            item.setName(tbreportmodelitem.getSheadername());
            if( null == tbreportmodelitem.getSids() || tbreportmodelitem.getSids().equals("") ) {
                item.setAdmonthtotal(BigDecimal.valueOf(0.00));
                item.setAdtotal(BigDecimal.valueOf(0.00));
                item.setOpmonthtotal(BigDecimal.valueOf(0.00));
                item.setOptotal(BigDecimal.valueOf(0.00));
                item.setAmountmonthtotal(BigDecimal.valueOf(0.00));
                item.setAmounttotal(BigDecimal.valueOf(0.00));
                adreceivedItemList.add(item);
            } else {
                List<String> ids = new ArrayList<String>();
                String[] deptIdArray = tbreportmodelitem.getSids().split(",");
                for( int j = 0; j < deptIdArray.length; j++ ) {
                    ids.add(deptIdArray[j]);
                }
                BigDecimal temp = financeStatisticServiceI.summaryNamountapportionArray(ids,null,startTime,endTime);
                item.setAdmonthtotal(temp);
                item.setStradmonthtotal(MoneyUtils.accountantMoney(temp));
                temp = financeStatisticServiceI.summaryNamountapportionArray(ids,null,null,null);
                item.setAdtotal(temp);
                item.setStradtotal(MoneyUtils.accountantMoney(temp));
                adreceivedItemList.add(item);
            }
        }
        // 添加合计行
        AdreceivedItem item  = new AdreceivedItem();
        item.setName("合计");
        BigDecimal temp = adreceivedItemList.stream().map(AdreceivedItem::getAdmonthtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        item.setAdmonthtotal(temp);
        item.setStradmonthtotal(MoneyUtils.accountantMoney(temp));
        temp = adreceivedItemList.stream().map(AdreceivedItem::getAdtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        item.setAdtotal(temp);
        adreceivedItemList.add(item);
        item.setStradtotal(MoneyUtils.accountantMoney(temp));
        adreceivedVO.setItemList(adreceivedItemList);
        return adreceivedVO;
    }

    public XSSFWorkbook exportAreaIncomeCombinExcel(AdreceivedVO vo,String companyName) {
        // 输出EXCEL
        XSSFWorkbook wb = new XSSFWorkbook();
        //创建工作表
        Sheet sheet = wb.createSheet("区域广告实收明细表（" + companyName + "）");
        int excelRow = 0;
        Cell cell;
        short h1 = 600, h2 = 400;
        short columnwidth1 = 50 * 256;
        short columnwidth2 = 22 * 256;
        XSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short) 22);
        font.setBold(true);

        XSSFFont font1 = wb.createFont();
        font1.setFontHeightInPoints((short) 12);

        XSSFFont font2 = wb.createFont();
        font2.setFontHeightInPoints((short) 14);
        font2.setBold(true);

        XSSFCellStyle style = wb.createCellStyle();
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setBorderRight(BorderStyle.MEDIUM);
        style.setFont(font1);
        style.setAlignment(HorizontalAlignment.CENTER);//水平居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中

        XSSFCellStyle style1 = wb.createCellStyle();
        style1.setFont(font);
        style1.setAlignment(HorizontalAlignment.CENTER);//水平居中
        style1.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中

        XSSFCellStyle style2 = wb.createCellStyle();
        style2.setFont(font1);
        style2.setAlignment(HorizontalAlignment.CENTER);//水平居中
        style2.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中

        XSSFCellStyle style3 = wb.createCellStyle();
        IndexedColorMap colorMap = wb.getStylesSource().getIndexedColors();
        XSSFColor color = new XSSFColor(new java.awt.Color(204, 255, 255), colorMap);
        style3.setFillForegroundColor(color);
        style3.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style3.setBorderTop(BorderStyle.MEDIUM);
        style3.setBorderBottom(BorderStyle.MEDIUM);
        style3.setBorderLeft(BorderStyle.MEDIUM);
        style3.setBorderRight(BorderStyle.MEDIUM);

        style3.setFont(font2);
        style3.setAlignment(HorizontalAlignment.CENTER);//水平居中
        style3.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中

        // 创建标题行
        Row titleRow = sheet.createRow(excelRow++);
        titleRow.setHeight(h1);
        for (int i = 0; i < hadeTitle.length; i++) {
            cell = titleRow.createCell(i);
            cell.setCellStyle(style1);
            if( i == 0 ) {
                sheet.setColumnWidth(i, columnwidth1);
            } else {
                sheet.setColumnWidth(i, columnwidth2);
            }
        }
        CellRangeAddress region = new CellRangeAddress(0, 0, 0, hadeTitle.length-1);
        int cell0 = region.getFirstColumn();
        cell = titleRow.getCell(cell0);
        cell.setCellValue("区域广告实收明细表（" + companyName + "）");
        cell.setCellStyle(style1) ;
        sheet.addMergedRegion(region);
        // 创建制表行
        Row tabRow = sheet.createRow(excelRow++);
        tabRow.setHeight(h2);
        for (int i = 0; i < hadeTitle.length; i++) {
            cell = tabRow.createCell(i);
            cell.setCellStyle(style2);
        }
        cell = tabRow.getCell(0);  //制表单位
        cell.setCellValue("区域广告实收明细表（" + companyName + "）");
        cell = tabRow.getCell(1);  //制表日期
        cell.setCellValue(vo.getReportDate());
        cell = tabRow.getCell(2);  //单位
        cell.setCellValue("单位元");
        // 创建标头1
        Row headRow1 = sheet.createRow(excelRow++);
        headRow1.setHeight(h2);
        for (int i = 0; i < hadeTitle.length; i++) {
            cell = headRow1.createCell(i);
            cell.setCellStyle(style3);
            cell.setCellValue(hadeTitle[i]);
        }
        // 输出汇总项
        List<AdreceivedItem> adreceivedItemList = vo.getItemList();
        for (int i = 0; i < adreceivedItemList.size(); i++) {
            AdreceivedItem item = adreceivedItemList.get(i);
            Row itemRow = sheet.createRow(excelRow++);
            itemRow.setHeight(h2);
            for (int cellno = 0; cellno < hadeTitle.length; cellno++) {
                cell = itemRow.createCell(cellno);
                cell.setCellStyle(style);
                if (cellno == 0) {
                    cell.setCellValue(item.getName());
                }
                if (cellno == 1) {
                    cell.setCellValue(item.getStradmonthtotal());
                }
                if (cellno == 2) {
                    cell.setCellValue(item.getStradtotal());
                }
            }
        }
        return wb;

    }

    @Override
    public AdreceivedVO exportAreaIncomeOp(String reportFormId) {
        AdreceivedVO  adreceivedVO = new AdreceivedVO();
        adreceivedVO.setReportDate(DateUtils.transferDate2StringCn(new Date()));
        List<Tbreportmodelitem> tbreportmodelitemList = financeStatisticServiceI.getSumItemFromReport(reportFormId,"公司");
        if( null == tbreportmodelitemList || tbreportmodelitemList.size() <= 0 ) {
            return  adreceivedVO;
        }
        String companyID = tbreportmodelitemList.get(0).getSids();
        if( null == companyID || companyID.isEmpty() ) {
            return  adreceivedVO;
        }
        if( reportFormId.equals("3") ) {
            companyID = null;
            adreceivedVO.setTitle("区域实收明细表(" + "广告+经营" +")");
        } else {
            adreceivedVO.setTitle("区域实收明细表(" + tbreportmodelitemList.get(0).getSheadername() +")");

        }
        adreceivedVO.setCompany(tbreportmodelitemList.get(0).getSheadername());
        List<AdreceivedItem> adreceivedItemList = new ArrayList<AdreceivedItem>();
        tbreportmodelitemList = financeStatisticServiceI.getSumItemFromReport(reportFormId,"部门");
        Date  beginDate = DateUtils.getBeginDayOfMonth();
        Date  endDate = DateUtils.getEndDayOfMonth();
        String startTime = DateUtils.transferDate2String(beginDate);
        String endTime = DateUtils.transferDate2String(endDate);
        for( int i = 0; i < tbreportmodelitemList.size(); i++ ) {
            Tbreportmodelitem tbreportmodelitem = tbreportmodelitemList.get(i);
            AdreceivedItem item  = new AdreceivedItem();
            item.setName(tbreportmodelitem.getSheadername());
            if( null == tbreportmodelitem.getSids() || tbreportmodelitem.getSids().equals("") ) {
                item.setAdmonthtotal(BigDecimal.valueOf(0.00));
                item.setAdtotal(BigDecimal.valueOf(0.00));
                item.setOpmonthtotal(BigDecimal.valueOf(0.00));
                item.setOptotal(BigDecimal.valueOf(0.00));
                item.setAmountmonthtotal(BigDecimal.valueOf(0.00));
                item.setAmounttotal(BigDecimal.valueOf(0.00));
                adreceivedItemList.add(item);
            } else {
                List<String> ids = new ArrayList<String>();
                String[] deptIdArray = tbreportmodelitem.getSids().split(",");
                for( int j = 0; j < deptIdArray.length; j++ ) {
                    ids.add(deptIdArray[j]);
                }
                BigDecimal temp = financeStatisticServiceI.summaryNamountapportionArray(ids,companyID,startTime,endTime);
                item.setAdmonthtotal(temp);
                item.setStradmonthtotal(MoneyUtils.accountantMoney(temp));
                temp = financeStatisticServiceI.summaryNamountapportionArray(ids,companyID,null,null);
                item.setAdtotal(temp);
                item.setStradtotal(MoneyUtils.accountantMoney(temp));
                adreceivedItemList.add(item);
            }
        }
        // 添加合计行
        AdreceivedItem item  = new AdreceivedItem();
        item.setName("合计");
        BigDecimal temp = adreceivedItemList.stream().map(AdreceivedItem::getAdmonthtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        item.setAdmonthtotal(temp);
        item.setStradmonthtotal(MoneyUtils.accountantMoney(temp));
        temp = adreceivedItemList.stream().map(AdreceivedItem::getAdtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        item.setAdtotal(temp);
        adreceivedItemList.add(item);
        item.setStradtotal(MoneyUtils.accountantMoney(temp));
        adreceivedVO.setItemList(adreceivedItemList);
        return adreceivedVO;

    }

    @Override
    public XSSFWorkbook exportAreaIncomeOpExcel(String reportFormId) {
        AdreceivedVO vo =  exportAreaIncomeOp(reportFormId);
        String companyName = "";
        if( reportFormId.equals("3") ) {
            companyName = "广告+经营";
        } else {
            List<Tbreportmodelitem> tbreportmodelitemList = financeStatisticServiceI.getSumItemFromReport(reportFormId,"公司");
            if( null != tbreportmodelitemList || tbreportmodelitemList.size() > 0 ) {
                companyName = tbreportmodelitemList.get(0).getSheadername();
            }

        }

        XSSFWorkbook wb = exportAreaIncomeCombinExcel(vo,companyName);
        return wb;
    }
}


