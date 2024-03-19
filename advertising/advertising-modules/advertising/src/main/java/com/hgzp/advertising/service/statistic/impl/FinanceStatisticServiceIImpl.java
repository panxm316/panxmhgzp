package com.hgzp.advertising.service.statistic.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hgzp.advertising.pagemodel.statistic.vo.*;
import com.hgzp.advertising.service.statistic.FinanceStatisticServiceI;
import com.hgzp.advertising.service.statistic.ReportmodelGroupServiceI;
import com.hgzp.advertising.service.statistic.ReportmodelItemServiceI;
import com.hgzp.advertising.utils.DateUtils;
import com.hgzp.advertising.utils.MoneyUtils;
import com.hgzp.core.model.Tbdept;
import com.hgzp.core.model.Tbreportmodelgroup;
import com.hgzp.core.model.Tbreportmodelitem;
import com.hgzp.core.model.Twtasks;
import com.hgzp.core.page.Json;
import com.hgzp.mapper.finance.TworderapportiondetailMapper;
import com.hgzp.mapper.statistic.TbreportmodelgroupMapper;
import com.hgzp.mapper.statistic.TbreportmodelitemMapper;
import com.hgzp.pagemodel.ad.AdSummaryVO;
import com.hgzp.service.system.BaseTbdeptServiceI;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * FinanceStatisticServiceIImpl
 * 创建人：lhl
 * 类描述：财务报表实现类
 * 创建日期：2024/1/15 13:04
 */
@Service
public class FinanceStatisticServiceIImpl implements FinanceStatisticServiceI {
    @Autowired
    TworderapportiondetailMapper tworderapportiondetailMapper;
    @Autowired
    BaseTbdeptServiceI baseTbdeptServiceI;
    @Autowired
    ReportmodelGroupServiceI  reportmodelGroupServiceI;
    @Autowired
    ReportmodelItemServiceI reportmodelItemServiceI;
    @Autowired
    private TbreportmodelitemMapper tbreportmodelitemMapper;

    String opCompanyId = "1696392651130298370";
    String adCompanyId = "1696744182430064642";

    private String hadeTitle[] = {"名称", "本月数", "累计数", "本月数", "累计数", "本月数", "累计数"};
    private String hadeTitlemedia[] = {"名称", "见报年份", "版面广告", "南方+", "网站", "双微", "活动","策划制作","本月合计","版面广告", "南方+", "网站", "双微", "活动","策划制作","本年累计"};
    private String hadeTitlDeptMedia[] = {"公司", "部门名称", "版面广告", "南方+", "网站", "双微", "多元化","本月数","版面广告", "南方+", "网站", "双微", "多远化","累计数"};


    @Override
    public XSSFWorkbook exportAdvertisingRealIncomeExcel() {
        AdreceivedVO vo = exportAdvertisingIncomeDep();
        // 输出EXCEL
        XSSFWorkbook wb = new XSSFWorkbook();
        //创建工作表
        Sheet sheet = wb.createSheet("广告实收明细表按部门分");
        int excelRow = 0;
        Cell cell;
        short h1 = 600, h2 = 400;
        short columnwidth1 = 20 * 256;
        short columnwidth2 = 18 * 256;
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
        cell.setCellValue("广告实收明细表按部门分");
        cell.setCellStyle(style1) ;
        sheet.addMergedRegion(region);
        // 创建制表行
        Row tabRow = sheet.createRow(excelRow++);
        tabRow.setHeight(h2);
        for (int i = 0; i < hadeTitle.length; i++) {
            cell = tabRow.createCell(i);
            cell.setCellStyle(style2);
        }
        region = new CellRangeAddress(1, 1, 0,1);
        cell0 = region.getFirstColumn();
        cell = tabRow.getCell(cell0);
        cell.setCellValue("编制单位： 广东南方日报经营有限公司");
        sheet.addMergedRegion(region);
        cell = tabRow.getCell(3);  //制表日期
        cell.setCellValue(vo.getReportDate());
        cell = tabRow.getCell(6);  //单位
        cell.setCellValue("单位元");
        // 创建标头1
        Row headRow1 = sheet.createRow(excelRow++);
        Row headRow2 = sheet.createRow(excelRow++);
        headRow1.setHeight(h2);
        headRow2.setHeight(h2);
        for (int i = 0; i < hadeTitle.length; i++) {
            cell = headRow1.createCell(i);
            cell.setCellStyle(style3);
            cell = headRow2.createCell(i);
            cell.setCellStyle(style3);
        }
        region = new CellRangeAddress(2, 3, 0,0);
        cell0 = region.getFirstColumn();
        cell = headRow1.getCell(cell0);
        cell.setCellValue("名称");
        cell.setCellStyle(style3);
        sheet.addMergedRegion(region);
        region = new CellRangeAddress(2, 2, 1,2);
        cell0 = region.getFirstColumn();
        cell = headRow1.getCell(cell0);
        cell.setCellValue("经营公司");
        cell.setCellStyle(style3);
        sheet.addMergedRegion(region);
        region = new CellRangeAddress(2, 2, 3,4);
        cell0 = region.getFirstColumn();
        cell = headRow1.getCell(cell0);
        cell.setCellValue("广告公司");
        cell.setCellStyle(style3);
        sheet.addMergedRegion(region);
        region = new CellRangeAddress(2, 2, 5,6);
        cell0 = region.getFirstColumn();
        cell = headRow1.getCell(cell0);
        cell.setCellStyle(style3);
        cell.setCellValue("合计");
        sheet.addMergedRegion(region);
        for (int i = 1; i < hadeTitle.length; i++) {
            cell = headRow2.createCell(i);
            if( i % 2 == 0 )
                cell.setCellValue("累计数");
            else
                cell.setCellValue("本月数");
            cell.setCellStyle(style3);
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
                    cell.setCellValue(MoneyUtils.accountantMoney(item.getOpmonthtotal()));
                }
                if (cellno == 2) {
                    cell.setCellValue(MoneyUtils.accountantMoney(item.getOptotal()));
                }
                if (cellno == 3) {
                    cell.setCellValue(MoneyUtils.accountantMoney(item.getAdmonthtotal()));
                }
                if (cellno == 4) {
                    cell.setCellValue(MoneyUtils.accountantMoney(item.getAdtotal()));
                }
                if (cellno == 5) {
                    cell.setCellValue(MoneyUtils.accountantMoney(item.getAmountmonthtotal()));
                }
                if (cellno == 6) {
                    cell.setCellValue(MoneyUtils.accountantMoney(item.getAmounttotal()));
                }

            }
        }
        return wb;
    }

    @Override
    public String getExcelFileName() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(date);
        return String.format("advertisingreceipts-%s.xlsx",formattedDate);
    }

    @Override
    public AdreceivedVO exportAdvertisingIncomeDep() {
        AdreceivedVO  adreceivedVO = new AdreceivedVO();
        adreceivedVO.setCompany("南方日报经营公司");
        adreceivedVO.setReportDate(DateUtils.transferDate2StringCn(new Date()));
        adreceivedVO.setTitle("广告实收明细表");
        List<Tbreportmodelitem> tbreportmodelitemList = getSumItemFromReport("0","部门");
        if( null == tbreportmodelitemList || tbreportmodelitemList.size() <= 0 ) {
            return  adreceivedVO;
        }
        List<AdreceivedItem> adreceivedItemList = new ArrayList<AdreceivedItem>();
        Date  beginDate = DateUtils.getBeginDayOfMonth();
        Date  endDate = DateUtils.getEndDayOfMonth();
        String startTime = DateUtils.transferDate2String(beginDate);
        String endTime = DateUtils.transferDate2String(endDate);
        for( int i = 0; i < tbreportmodelitemList.size(); i++ ) {
            AdreceivedItem item  = new AdreceivedItem();
            Tbreportmodelitem tbreportmodelitem = tbreportmodelitemList.get(i);
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
                BigDecimal temp = summaryNamountapportionArray(ids,adCompanyId,startTime,endTime);
                item.setAdmonthtotal(temp);
                item.setStradmonthtotal(MoneyUtils.accountantMoney(temp));
                temp = summaryNamountapportionArray(ids,adCompanyId,null,null);
                item.setAdtotal(temp);
                item.setStradtotal(MoneyUtils.accountantMoney(temp));
                temp = summaryNamountapportionArray(ids,opCompanyId,startTime,endTime);
                item.setOpmonthtotal(temp);
                item.setStropmonthtotal(MoneyUtils.accountantMoney(temp));
                temp = summaryNamountapportionArray(ids,opCompanyId,null,null);
                item.setOptotal(temp);
                item.setStroptotal(MoneyUtils.accountantMoney(temp));
                temp = BigDecimal.valueOf(item.getAdmonthtotal().doubleValue()+item.getOpmonthtotal().doubleValue());
                item.setAmountmonthtotal(temp);
                item.setStramountmonthtotal(MoneyUtils.accountantMoney(temp));
                temp = BigDecimal.valueOf(item.getAdtotal().doubleValue()+item.getOptotal().doubleValue());
                item.setAmounttotal(temp);
                item.setStramounttotal(MoneyUtils.accountantMoney(temp));
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
        item.setStradtotal(MoneyUtils.accountantMoney(temp));
        temp = adreceivedItemList.stream().map(AdreceivedItem::getOpmonthtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        item.setOpmonthtotal(temp);
        item.setStropmonthtotal(MoneyUtils.accountantMoney(temp));
        temp = adreceivedItemList.stream().map(AdreceivedItem::getOptotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        item.setOptotal(temp);
        item.setStroptotal(MoneyUtils.accountantMoney(temp));
        temp = adreceivedItemList.stream().map(AdreceivedItem::getAmountmonthtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        item.setAmountmonthtotal(temp);
        item.setStramountmonthtotal(MoneyUtils.accountantMoney(temp));
        temp = adreceivedItemList.stream().map(AdreceivedItem::getAmounttotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        item.setAmounttotal(temp);
        item.setStramounttotal(MoneyUtils.accountantMoney(temp));
        adreceivedItemList.add(item);
        adreceivedVO.setItemList(adreceivedItemList);
        return adreceivedVO;
    }

    @Override
    public List<Tbreportmodelitem> getSumItemFromReport(String applyTo,String reportType) {
        Tbreportmodelgroup tbreportmodelgroup = reportmodelGroupServiceI.getTbreportmodelgroup(applyTo);
        if( null == tbreportmodelgroup )
            return  null;
        List<Tbreportmodelitem> list = reportmodelItemServiceI.getReportmodeItem(String.valueOf(tbreportmodelgroup.getId()),reportType);
        return list;
    }

    @Override
    public AdIncomeYearMeidaVO exportAdvertisingYearMedia() {
        AdreceivedVO  adreceivedVO = new AdreceivedVO();
        adreceivedVO.setCompany("南方日报经营公司");
        adreceivedVO.setReportDate(DateUtils.transferDate2StringCn(new Date()));
        adreceivedVO.setTitle("广告实收明细表按业务类型、年份");
        Tbreportmodelgroup tbreportmodelgroup = reportmodelGroupServiceI.getTbreportmodelgroup("1");
        List<Tbreportmodelitem> tbreportmodelitemList = getSumItemFromReport("1","年度");
        if( null == tbreportmodelitemList || tbreportmodelitemList.size() <= 0 ) {
            return  getReporIncomeYearMediaData(adreceivedVO);
        }
        List<AdreceivedItem> adreceivedItemList = new ArrayList<AdreceivedItem>();
        Date  beginDate = DateUtils.getBeginDayOfMonth();
        Date  endDate = DateUtils.getEndDayOfMonth();
        String startTime = DateUtils.transferDate2String(beginDate);
        String endTime = DateUtils.transferDate2String(endDate);
        for( int i = 0; i < tbreportmodelitemList.size(); i++ ) {
            AdreceivedItem item  = new AdreceivedItem();
            Tbreportmodelitem tbreportmodelitem = tbreportmodelitemList.get(i);
            item.setName(tbreportmodelitem.getSheadername());
            String[] array = tbreportmodelitem.getScolumnname().split(",");
            // 经营公司本月数
            item.setOpcolumnSummaryList(getColumnSummaryList(array,opCompanyId,DateUtils.getBeginDayOfMonthWithYear(item.getName()),DateUtils.getEndDayOfMonthWithYear(item.getName())));
            // 经营公司本年数
            item.setOpyearcolumnSummaryList(getColumnSummaryList(array,opCompanyId,DateUtils.getBeginDayOfYear(item.getName()),DateUtils.getEndDayOfYear(item.getName())));
            // 广告公司本月数
            item.setAdcolumnSummaryList(getColumnSummaryList(array,adCompanyId,DateUtils.getBeginDayOfMonthWithYear(item.getName()),DateUtils.getEndDayOfMonthWithYear(item.getName())));
            // 广告公司本年数
            item.setAdyearcolumnSummaryList(getColumnSummaryList(array,adCompanyId,DateUtils.getBeginDayOfYear(item.getName()),DateUtils.getEndDayOfYear(item.getName())));
            // 广告公司、经营公司汇总本月数
            item.setOpadcolumnSummaryList(getColumnSummaryList(array,null,DateUtils.getBeginDayOfMonthWithYear(item.getName()),DateUtils.getEndDayOfMonthWithYear(item.getName())));
            // 广告公司、经营公司汇总本年数
            item.setOpadyearcolumnSummaryList(getColumnSummaryList(array,null,DateUtils.getBeginDayOfYear(item.getName()),DateUtils.getEndDayOfYear(item.getName())));

            adreceivedItemList.add(item);
        }
        adreceivedVO.setItemList(adreceivedItemList);
        return getReporIncomeYearMediaData(adreceivedVO);
    }

    @Override
    public XSSFWorkbook exportAdvertisingYearMediaExcel() {
        AdIncomeYearMeidaVO vo = exportAdvertisingYearMedia();
        // 输出EXCEL
        XSSFWorkbook wb = new XSSFWorkbook();
        //创建工作表
        Sheet sheet = wb.createSheet("广告实收明细表按业务类型、年份");
        int excelRow = 0;
        Cell cell;
        short h1 = 600, h2 = 400;
        short columnwidth1 = 20 * 256;
        short columnwidth2 = 18 * 256;
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

        XSSFCellStyle style4 = wb.createCellStyle();
        colorMap = wb.getStylesSource().getIndexedColors();
        color = new XSSFColor(new java.awt.Color(255, 204, 153), colorMap);
        style4.setFillForegroundColor(color);
        style4.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style4.setBorderTop(BorderStyle.MEDIUM);
        style4.setBorderBottom(BorderStyle.MEDIUM);
        style4.setBorderLeft(BorderStyle.MEDIUM);
        style4.setBorderRight(BorderStyle.MEDIUM);
        style4.setFont(font2);
        style4.setAlignment(HorizontalAlignment.CENTER);//水平居中
        style4.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中

        XSSFCellStyle style5 = wb.createCellStyle();
        style5.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style5.setBorderTop(BorderStyle.MEDIUM);
        style5.setBorderBottom(BorderStyle.MEDIUM);
        style5.setBorderLeft(BorderStyle.MEDIUM);
        style5.setBorderRight(BorderStyle.MEDIUM);
        style5.setFont(font2);
        style5.setAlignment(HorizontalAlignment.CENTER);//水平居中
        style5.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中

        // 创建标题行
        Row titleRow = sheet.createRow(excelRow++);
        titleRow.setHeight(h1);
        for (int i = 0; i < hadeTitlemedia.length; i++) {
            cell = titleRow.createCell(i);
            cell.setCellStyle(style1);
            if( i == 0 ) {
                sheet.setColumnWidth(i, columnwidth1);
            } else {
                sheet.setColumnWidth(i, columnwidth2);
            }
        }
        CellRangeAddress region = new CellRangeAddress(0, 0, 0, hadeTitlemedia.length-1);
        int cell0 = region.getFirstColumn();
        cell = titleRow.getCell(cell0);
        cell.setCellValue("广告实收明细表按业务类型、年份");
        cell.setCellStyle(style1) ;
        sheet.addMergedRegion(region);
        // 创建制表行
        Row tabRow = sheet.createRow(excelRow++);
        tabRow.setHeight(h2);
        for (int i = 0; i < hadeTitlemedia.length; i++) {
            cell = tabRow.createCell(i);
            cell.setCellStyle(style2);
        }
        region = new CellRangeAddress(1, 1, 0,1);
        cell0 = region.getFirstColumn();
        cell = tabRow.getCell(cell0);
        cell.setCellValue("编制单位： 广东南方日报经营有限公司");
        sheet.addMergedRegion(region);
        cell = tabRow.getCell(8);  //制表日期
        cell.setCellValue(vo.getReportDate());
        cell = tabRow.getCell(15);  //单位
        cell.setCellValue("单位元");
        // 创建标头1
        Row headRow1 = sheet.createRow(excelRow++);
        Row headRow2 = sheet.createRow(excelRow++);
        headRow1.setHeight(h2);
        headRow2.setHeight(h2);
        for (int i = 0; i < hadeTitlemedia.length; i++) {
            if( i <= 8 ) {
                cell = headRow1.createCell(i);
                cell.setCellStyle(style3);
                cell = headRow2.createCell(i);
                cell.setCellStyle(style3);
            }else {
                cell = headRow1.createCell(i);
                cell.setCellStyle(style4);
                cell = headRow2.createCell(i);
                cell.setCellStyle(style4);

            }
        }
        region = new CellRangeAddress(2, 3, 0,0);
        cell0 = region.getFirstColumn();
        cell = headRow1.getCell(cell0);
        cell.setCellValue("名称");
        cell.setCellStyle(style5);
        sheet.addMergedRegion(region);
        region = new CellRangeAddress(2, 3, 1,1);
        cell0 = region.getFirstColumn();
        cell = headRow1.getCell(cell0);
        cell.setCellValue("见报年份");
        cell.setCellStyle(style5);
        sheet.addMergedRegion(region);


        region = new CellRangeAddress(2, 2, 2,8);
        cell0 = region.getFirstColumn();
        cell = headRow1.getCell(cell0);
        cell.setCellValue("本月数");
        cell.setCellStyle(style3);
        sheet.addMergedRegion(region);
        region = new CellRangeAddress(2, 2, 9,15);
        cell0 = region.getFirstColumn();
        cell = headRow1.getCell(cell0);
        cell.setCellValue("本年累计数");
        cell.setCellStyle(style4);
        sheet.addMergedRegion(region);
        for (int i = 2; i < hadeTitlemedia.length; i++) {
            if( i <= 8 ) {
                cell = headRow2.createCell(i);
                cell.setCellValue(hadeTitlemedia[i]);
                cell.setCellStyle(style3);
            } else {
                cell = headRow2.createCell(i);
                cell.setCellValue(hadeTitlemedia[i]);
                cell.setCellStyle(style4);

            }
        }
        // 输出汇总项
        List<AdIncomeYearItem> list = vo.getItemList();
        for (int i = 0; i < list.size(); i++) {
            AdIncomeYearItem item = list.get(i);
            Row itemRow = sheet.createRow(excelRow++);
            itemRow.setHeight(h2);
            for (int cellno = 0; cellno < hadeTitlemedia.length; cellno++) {
                cell = itemRow.createCell(cellno);
                cell.setCellStyle(style);
                if (cellno == 0) {
                    cell.setCellValue(item.getCompanyName());
                }
                if (cellno == 1) {
                    cell.setCellValue(item.getYear());
                }
                if (cellno == 2) {
                    cell.setCellValue(item.getMonthBMTotal());
                }
                if (cellno == 3) {
                    cell.setCellValue(item.getMonthNFTotal());
                }
                if (cellno == 4) {
                    cell.setCellValue(item.getMonthWZTotal());
                }
                if (cellno == 5) {
                    cell.setCellValue(item.getMonthSWTotal());
                }
                if (cellno == 6) {
                    cell.setCellValue(item.getMonthHDTotal());
                }
                if (cellno == 7) {
                    cell.setCellValue(item.getMonthCHTotal());
                }
                if (cellno == 8) {
                    cell.setCellValue(item.getMonthTotal());
                }
                if (cellno == 9) {
                    cell.setCellValue(item.getYearBMTotal());
                }
                if (cellno == 10) {
                    cell.setCellValue(item.getYearNFTotal());
                }
                if (cellno == 11) {
                    cell.setCellValue(item.getYearWZTotal());
                }
                if (cellno == 12) {
                    cell.setCellValue(item.getYearSWTotal());
                }
                if (cellno == 13) {
                    cell.setCellValue(item.getYearHDTotal());
                }
                if (cellno == 14) {
                    cell.setCellValue(item.getYearCHTotal());
                }
                if (cellno == 15) {
                    cell.setCellValue(item.getYearTotal());
                }

            }
        }
        int stratrow = 4;
        int endrow = stratrow + vo.getOprow()-1;
        region = new CellRangeAddress(stratrow, endrow, 0,0);
        sheet.addMergedRegion(region);

        stratrow = endrow+1;
        endrow += vo.getAdrow();
        region = new CellRangeAddress(stratrow, endrow, 0,0);
        sheet.addMergedRegion(region);

        stratrow = endrow+1;
        endrow += vo.getSumrow();
        region = new CellRangeAddress(stratrow, endrow, 0,0);
        sheet.addMergedRegion(region);

        return wb;
    }

    @Override
    public AdIncomeYearMeidaVO exportAdvertisingDeptMedia() {
        AdreceivedVO  adreceivedVO = new AdreceivedVO();
        adreceivedVO.setCompany("南方日报经营公司");
        adreceivedVO.setReportDate(DateUtils.transferDate2StringCn(new Date()));
        adreceivedVO.setTitle("广告实收明细表按部门、业务类型");
        Tbreportmodelgroup tbreportmodelgroup = reportmodelGroupServiceI.getTbreportmodelgroup("2");
        List<Tbreportmodelitem> tbreportmodelitemList = getSumItemFromReport("2","部门");
        if( null == tbreportmodelitemList || tbreportmodelitemList.size() <= 0 ) {
            return  getReporIncomeYearMediaData(adreceivedVO);
        }
        List<AdreceivedItem> adreceivedItemList = new ArrayList<AdreceivedItem>();
        Date  beginDate = DateUtils.getBeginDayOfMonth();
        Date  endDate = DateUtils.getEndDayOfMonth();
        String startTime = DateUtils.transferDate2String(beginDate);
        String endTime = DateUtils.transferDate2String(endDate);
        for( int i = 0; i < tbreportmodelitemList.size(); i++ ) {
            AdreceivedItem item  = new AdreceivedItem();
            Tbreportmodelitem tbreportmodelitem = tbreportmodelitemList.get(i);
            item.setName(tbreportmodelitem.getSheadername());
            String[] array = tbreportmodelitem.getSids().split(",");
            // 经营公司本月数
            item.setOpcolumnSummaryList(getColumnSummaryDeptList(array,opCompanyId,startTime,endTime));
            // 经营公司累计数
            item.setOpyearcolumnSummaryList(getColumnSummaryDeptList(array,opCompanyId,null,null));
            // 广告公司本月数
            item.setAdcolumnSummaryList(getColumnSummaryDeptList(array,adCompanyId,startTime,endTime));
            // 广告公司累计数
            item.setAdyearcolumnSummaryList(getColumnSummaryDeptList(array,adCompanyId,null,null));
            // 广告公司、经营公司汇总本月数
            item.setOpadcolumnSummaryList(getColumnSummaryDeptList(array,null,startTime,endTime));
            // 广告公司、经营公司汇总累计数
            item.setOpadyearcolumnSummaryList(getColumnSummaryDeptList(array,null,null,null));

            adreceivedItemList.add(item);
        }
        adreceivedVO.setItemList(adreceivedItemList);
        return getReporIncomeYearMediaData(adreceivedVO);
    }

    @Override
    public XSSFWorkbook exportAdvertisingDeptMediaExcel() {
        AdIncomeYearMeidaVO vo = exportAdvertisingDeptMedia();
        // 输出EXCEL
        XSSFWorkbook wb = new XSSFWorkbook();
        //创建工作表
        Sheet sheet = wb.createSheet("广告实收明细表按部门、业务类型");
        int excelRow = 0;
        Cell cell;
        short h1 = 600, h2 = 400;
        short columnwidth1 = 20 * 256;
        short columnwidth2 = 18 * 256;
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

        XSSFCellStyle style4 = wb.createCellStyle();
        colorMap = wb.getStylesSource().getIndexedColors();
        color = new XSSFColor(new java.awt.Color(255, 204, 153), colorMap);
        style4.setFillForegroundColor(color);
        style4.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style4.setBorderTop(BorderStyle.MEDIUM);
        style4.setBorderBottom(BorderStyle.MEDIUM);
        style4.setBorderLeft(BorderStyle.MEDIUM);
        style4.setBorderRight(BorderStyle.MEDIUM);
        style4.setFont(font2);
        style4.setAlignment(HorizontalAlignment.CENTER);//水平居中
        style4.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中

        XSSFCellStyle style5 = wb.createCellStyle();
        style5.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style5.setBorderTop(BorderStyle.MEDIUM);
        style5.setBorderBottom(BorderStyle.MEDIUM);
        style5.setBorderLeft(BorderStyle.MEDIUM);
        style5.setBorderRight(BorderStyle.MEDIUM);
        style5.setFont(font2);
        style5.setAlignment(HorizontalAlignment.CENTER);//水平居中
        style5.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中

        // 创建标题行
        Row titleRow = sheet.createRow(excelRow++);
        titleRow.setHeight(h1);
        for (int i = 0; i < hadeTitlDeptMedia.length; i++) {
            cell = titleRow.createCell(i);
            cell.setCellStyle(style1);
            if( i == 0 ) {
                sheet.setColumnWidth(i, columnwidth1);
            } else {
                sheet.setColumnWidth(i, columnwidth2);
            }
        }
        CellRangeAddress region = new CellRangeAddress(0, 0, 0, hadeTitlDeptMedia.length-1);
        int cell0 = region.getFirstColumn();
        cell = titleRow.getCell(cell0);
        cell.setCellValue("广告实收明细表按部门、业务类型");
        cell.setCellStyle(style1) ;
        sheet.addMergedRegion(region);
        // 创建制表行
        Row tabRow = sheet.createRow(excelRow++);
        tabRow.setHeight(h2);
        for (int i = 0; i < hadeTitlDeptMedia.length; i++) {
            cell = tabRow.createCell(i);
            cell.setCellStyle(style2);
        }
        region = new CellRangeAddress(1, 1, 0,1);
        cell0 = region.getFirstColumn();
        cell = tabRow.getCell(cell0);
        cell.setCellValue("编制单位： 广东南方日报经营有限公司");
        sheet.addMergedRegion(region);
        cell = tabRow.getCell(6);  //制表日期
        cell.setCellValue(vo.getReportDate());
        cell = tabRow.getCell(13);  //单位
        cell.setCellValue("单位元");
        // 创建标头1
        Row headRow1 = sheet.createRow(excelRow++);
        Row headRow2 = sheet.createRow(excelRow++);
        headRow1.setHeight(h2);
        headRow2.setHeight(h2);
        for (int i = 0; i < hadeTitlDeptMedia.length; i++) {
            if( i <= 7 ) {
                cell = headRow1.createCell(i);
                cell.setCellStyle(style3);
                cell = headRow2.createCell(i);
                cell.setCellStyle(style3);
            }else {
                cell = headRow1.createCell(i);
                cell.setCellStyle(style4);
                cell = headRow2.createCell(i);
                cell.setCellStyle(style4);

            }
        }
        region = new CellRangeAddress(2, 3, 0,0);
        cell0 = region.getFirstColumn();
        cell = headRow1.getCell(cell0);
        cell.setCellValue("公司");
        cell.setCellStyle(style5);
        sheet.addMergedRegion(region);
        region = new CellRangeAddress(2, 3, 1,1);
        cell0 = region.getFirstColumn();
        cell = headRow1.getCell(cell0);
        cell.setCellValue("部门名称");
        cell.setCellStyle(style5);
        sheet.addMergedRegion(region);


        region = new CellRangeAddress(2, 2, 2,7);
        cell0 = region.getFirstColumn();
        cell = headRow1.getCell(cell0);
        cell.setCellValue("本月数");
        cell.setCellStyle(style3);
        sheet.addMergedRegion(region);
        region = new CellRangeAddress(2, 2, 9,13);
        cell0 = region.getFirstColumn();
        cell = headRow1.getCell(cell0);
        cell.setCellValue("累计数");
        cell.setCellStyle(style4);
        sheet.addMergedRegion(region);
        for (int i = 2; i < hadeTitlDeptMedia.length; i++) {
            if( i <= 7 ) {
                cell = headRow2.createCell(i);
                cell.setCellValue(hadeTitlDeptMedia[i]);
                cell.setCellStyle(style3);
            } else {
                cell = headRow2.createCell(i);
                cell.setCellValue(hadeTitlDeptMedia[i]);
                cell.setCellStyle(style4);

            }
        }
        // 输出汇总项
        List<AdIncomeYearItem> list = vo.getItemList();
        for (int i = 0; i < list.size(); i++) {
            AdIncomeYearItem item = list.get(i);
            Row itemRow = sheet.createRow(excelRow++);
            itemRow.setHeight(h2);
            for (int cellno = 0; cellno < hadeTitlDeptMedia.length; cellno++) {
                cell = itemRow.createCell(cellno);
                cell.setCellStyle(style);
                if (cellno == 0) {
                    cell.setCellValue(item.getCompanyName());
                }
                if (cellno == 1) {
                    cell.setCellValue(item.getYear());
                }
                if (cellno == 2) {
                    cell.setCellValue(item.getMonthBMTotal());
                }
                if (cellno == 3) {
                    cell.setCellValue(item.getMonthNFTotal());
                }
                if (cellno == 4) {
                    cell.setCellValue(item.getMonthWZTotal());
                }
                if (cellno == 5) {
                    cell.setCellValue(item.getMonthSWTotal());
                }
                if (cellno == 6) {
                    cell.setCellValue(item.getMonthHDTotal());
                }
                if (cellno == 7) {
                    cell.setCellValue(item.getMonthTotal());
                }
                if (cellno == 8) {
                    cell.setCellValue(item.getYearBMTotal());
                }
                if (cellno == 9) {
                    cell.setCellValue(item.getYearNFTotal());
                }
                if (cellno == 10) {
                    cell.setCellValue(item.getYearWZTotal());
                }
                if (cellno == 11) {
                    cell.setCellValue(item.getYearSWTotal());
                }
                if (cellno == 12) {
                    cell.setCellValue(item.getYearHDTotal());
                }
                if (cellno == 13) {
                    cell.setCellValue(item.getYearTotal());
                }

            }
        }
        int stratrow = 4;
        int endrow = stratrow + vo.getOprow()-1;
        region = new CellRangeAddress(stratrow, endrow, 0,0);
        sheet.addMergedRegion(region);

        stratrow = endrow+1;
        endrow += vo.getAdrow();
        region = new CellRangeAddress(stratrow, endrow, 0,0);
        sheet.addMergedRegion(region);

        stratrow = endrow+1;
        endrow += vo.getSumrow();
        region = new CellRangeAddress(stratrow, endrow, 0,0);
        sheet.addMergedRegion(region);

        return wb;

    }

    public BigDecimal summaryNamountapportion(String departID, String companyId,String startTime,String endTime) {
        AdSummaryVO vo = tworderapportiondetailMapper.summaryNamountapportion(departID,companyId,startTime,endTime);
        if( null != vo ) {
            return vo.getAmountreceivable();
        }
        return BigDecimal.valueOf(0.0);
    }

    @Override
    public BigDecimal summaryNamountapportionArray(List<String> ids, String companyId,String startTime,String endTime) {
        AdSummaryVO vo = tworderapportiondetailMapper.summaryNamountapportionArray(ids,companyId,startTime,endTime);
        if( null != vo ) {
            return vo.getAmountreceivable();
        }
        return BigDecimal.valueOf(0.0);
    }

    public BigDecimal summaryNamountapportionMedia(List<String> ids, List<String> mediaids,List<String> floadids,List<String> foldareaverids,String companyId,String startTime,String endTime) {
        AdSummaryVO vo = tworderapportiondetailMapper.summaryNamountapportionMedia(ids,mediaids,floadids,foldareaverids,companyId,startTime,endTime);
        if( null != vo ) {
            return vo.getAmountreceivable();
        }
        return BigDecimal.valueOf(0.0);
    }

    /**
     * 获取基于公司、期间、业务汇总列表
     * @param
     * @return
     * @author lhl
     * @date 2024/1/25
     */
    public List<ColumnSummary> getColumnSummaryList(String[] array,String companyID,String startTime,String endTime) {
        Tbreportmodelgroup tbreportmodelgroup = reportmodelGroupServiceI.getTbreportmodelgroup("1");
        List<ColumnSummary> columnSummaryList = new ArrayList<ColumnSummary>();
        for( int j = 0; j < array.length; j++ )
        {
            ColumnSummary  columnSummary = new ColumnSummary();
            String columnName = array[j];
            BigDecimal total = BigDecimal.valueOf(0.00);
            Tbreportmodelitem temp = reportmodelItemServiceI.getTbreportmodelitemWithHeadName(String.valueOf(tbreportmodelgroup.getId()),columnName);
            List<String> ids = new ArrayList<String>();
            String[] deptIdArray = temp.getSids().split(",");
            for( int k = 0; k < deptIdArray.length; k++ ) {
                ids.add(deptIdArray[k]);
            }
            if( temp.getSnames().equals("0") ) { //媒体
                total = summaryNamountapportionMedia(null,ids,null,null,companyID,startTime,endTime);
            }
            if( temp.getSnames().equals("1") ) { //叠次
                total = summaryNamountapportionMedia(null,null,ids,null,companyID,startTime,endTime);
            }
            if( temp.getSnames().equals("1") ) { //叠次版本
                total = summaryNamountapportionMedia(null,null,null,ids,companyID,startTime,endTime);
            }
            columnSummary.setSummary(total);
            columnSummary.setName(columnName);
            columnSummary.setStrsummary(MoneyUtils.accountantMoney(total));
            columnSummaryList.add(columnSummary);
        }
        return columnSummaryList;
    }


    /**
     * 获取基于公司、部门、期间、业务汇总列表
     * @param
     * @return
     * @author lhl
     * @date 2024/1/29
     */
    public List<ColumnSummary> getColumnSummaryDeptList(String[] departarray,String companyID,String startTime,String endTime) {
        List<ColumnSummary> columnSummaryList = new ArrayList<ColumnSummary>();
        Tbreportmodelgroup tbreportmodelgroup = reportmodelGroupServiceI.getTbreportmodelgroup("2");
        LambdaQueryWrapper<Tbreportmodelitem> tlqw = Wrappers.lambdaQuery();
        tlqw.eq(Tbreportmodelitem::getReportmodelgroupid,tbreportmodelgroup.getId());
        tlqw.eq(Tbreportmodelitem::getReporttype,"业务");
        tlqw.orderByAsc(Tbreportmodelitem::getIsort);
        List<Tbreportmodelitem> tempList = tbreportmodelitemMapper.selectList(tlqw);
        List<String> deptList = new ArrayList<String>();
        for( int i = 0; i < departarray.length; i++ ) {
            deptList.add(departarray[i]);
        }
        for( int i = 0; i < tempList.size(); i++ )
        {
            ColumnSummary  columnSummary = new ColumnSummary();
            Tbreportmodelitem  temp = tempList.get(i);
            BigDecimal total = BigDecimal.valueOf(0.00);
            List<String> ids = new ArrayList<String>();
            String[] mediaArray = temp.getSids().split(",");
            for( int k = 0; k < mediaArray.length; k++ ) {
                ids.add(mediaArray[k]);
            }
            if( temp.getSnames().equals("0") ) { //媒体
                total = summaryNamountapportionMedia(deptList,ids,null,null,companyID,startTime,endTime);
            }
            if( temp.getSnames().equals("1") ) { //叠次
                total = summaryNamountapportionMedia(deptList,null,ids,null,companyID,startTime,endTime);
            }
            if( temp.getSnames().equals("1") ) { //叠次版本
                total = summaryNamountapportionMedia(deptList,null,null,ids,companyID,startTime,endTime);
            }
            columnSummary.setSummary(total);
            columnSummary.setName(temp.getSheadername());
            columnSummary.setStrsummary(MoneyUtils.accountantMoney(total));
            columnSummaryList.add(columnSummary);
        }
        return columnSummaryList;
    }

    /**
     * 获取广告实收明细表（年度业务）报表数据
     * @param
     * @return
     * @author lhl
     * @date 2024/1/26
     */
    public AdIncomeYearMeidaVO  getReporIncomeYearMediaData(AdreceivedVO vo) {
        AdIncomeYearMeidaVO  reporVO  = new AdIncomeYearMeidaVO();
        List<AdreceivedItem> adreceivedItemList = vo.getItemList();
        List<AdIncomeYearItem> list =  new ArrayList<AdIncomeYearItem>();
        reporVO.setTitle(vo.getTitle());
        reporVO.setReportDate(vo.getReportDate());
        reporVO.setCompany(vo.getCompany());

        if( null == adreceivedItemList ) {
            reporVO.setItemList(list);
            return reporVO;
        }
        double monthTotal = 0.0;
        double yearTotal = 0.0;
        double mbmTotal = 0.0;  //版面广告小计
        double mnfTotal = 0.0;  //南方+广告小计
        double mwzTotal = 0.0;  //网站广告小计
        double mswTotal = 0.0;  //双微广告小计
        double mhdTotal = 0.0;  //活动广告小计
        double mchTotal = 0.0;  //策划广告小计
        double ybmTotal = 0.0;  //版面广告小计
        double ynfTotal = 0.0;  //南方+广告小计
        double ywzTotal = 0.0;  //网站广告小计
        double yswTotal = 0.0;  //双微广告小计
        double yhdTotal = 0.0;  //活动广告小计
        double ychTotal = 0.0;  //策划广告小计

        // 经营公司
        for( int i = 0; i < adreceivedItemList.size(); i++ ) {
            AdIncomeYearItem adIncomeYearItem = new AdIncomeYearItem();
            AdreceivedItem adreceivedItem = adreceivedItemList.get(i);
            adIncomeYearItem.setYear(adreceivedItem.getName());
            adIncomeYearItem.setCompanyName("经营公司");
            monthTotal = 0.0;
            yearTotal = 0.0;
            // 本月数
            List<ColumnSummary> tempList = adreceivedItem.getOpcolumnSummaryList();
            for( int k = 0; k < tempList.size(); k++ ) {
                ColumnSummary  columnSummary = tempList.get(k);
                monthTotal += columnSummary.getSummary().doubleValue();
                if( columnSummary.getName().equals("版面广告") ) {
                    adIncomeYearItem.setMonthBMTotal(columnSummary.getStrsummary());
                    mbmTotal += columnSummary.getSummary().doubleValue();
                }
                if( columnSummary.getName().equals("南方+") ) {
                    adIncomeYearItem.setMonthNFTotal(columnSummary.getStrsummary());
                    mnfTotal += columnSummary.getSummary().doubleValue();
                }
                if( columnSummary.getName().equals("网站") ) {
                    adIncomeYearItem.setMonthWZTotal(columnSummary.getStrsummary());
                    mwzTotal += columnSummary.getSummary().doubleValue();
                }
                if( columnSummary.getName().equals("双微") ) {
                    adIncomeYearItem.setMonthSWTotal(columnSummary.getStrsummary());
                    mswTotal += columnSummary.getSummary().doubleValue();
                }
                if( columnSummary.getName().equals("多元化") ) {
                    adIncomeYearItem.setMonthHDTotal(columnSummary.getStrsummary());
                    mhdTotal += columnSummary.getSummary().doubleValue();
                }
                if( columnSummary.getName().equals("活动") ) {
                    adIncomeYearItem.setMonthHDTotal(columnSummary.getStrsummary());
                    mhdTotal += columnSummary.getSummary().doubleValue();
                }
                if( columnSummary.getName().equals("策划制作") ) {
                    adIncomeYearItem.setMonthCHTotal(columnSummary.getStrsummary());
                    mchTotal += columnSummary.getSummary().doubleValue();
                }
            }
            // 本年数
            tempList = adreceivedItem.getOpyearcolumnSummaryList();
            for( int k = 0; k < tempList.size(); k++ ) {
                ColumnSummary  columnSummary = tempList.get(k);
                yearTotal += columnSummary.getSummary().doubleValue();
                if( columnSummary.getName().equals("版面广告") ) {
                    adIncomeYearItem.setYearBMTotal(columnSummary.getStrsummary());
                    ybmTotal += columnSummary.getSummary().doubleValue();
                }
                if( columnSummary.getName().equals("南方+") ) {
                    adIncomeYearItem.setYearNFTotal(columnSummary.getStrsummary());
                    ynfTotal += columnSummary.getSummary().doubleValue();
                }
                if( columnSummary.getName().equals("网站") ) {
                    adIncomeYearItem.setYearWZTotal(columnSummary.getStrsummary());
                    ywzTotal += columnSummary.getSummary().doubleValue();

                }
                if( columnSummary.getName().equals("双微") ) {
                    adIncomeYearItem.setYearSWTotal(columnSummary.getStrsummary());
                    yswTotal += columnSummary.getSummary().doubleValue();

                }
                if( columnSummary.getName().equals("多元化") ) {
                    adIncomeYearItem.setYearHDTotal(columnSummary.getStrsummary());
                    yhdTotal += columnSummary.getSummary().doubleValue();

                }
                if( columnSummary.getName().equals("活动") ) {
                    adIncomeYearItem.setYearHDTotal(columnSummary.getStrsummary());
                    yhdTotal += columnSummary.getSummary().doubleValue();

                }
                if( columnSummary.getName().equals("策划制作") ) {
                    adIncomeYearItem.setYearCHTotal(columnSummary.getStrsummary());
                    ychTotal += columnSummary.getSummary().doubleValue();
                }
            }
            adIncomeYearItem.setMonthTotal(MoneyUtils.accountantMoney(BigDecimal.valueOf(monthTotal)));
            adIncomeYearItem.setYearTotal(MoneyUtils.accountantMoney(BigDecimal.valueOf(yearTotal)));
            list.add(adIncomeYearItem);
        }
        // 经营公司小计
        AdIncomeYearItem tempadIncomeYearItem = new AdIncomeYearItem();
        tempadIncomeYearItem.setYear("小计");
        tempadIncomeYearItem.setCompanyName("经营公司");
        tempadIncomeYearItem.setMonthBMTotal(MoneyUtils.accountantMoney(BigDecimal.valueOf(mbmTotal)));
        tempadIncomeYearItem.setMonthNFTotal(MoneyUtils.accountantMoney(BigDecimal.valueOf(mnfTotal)));
        tempadIncomeYearItem.setMonthWZTotal(MoneyUtils.accountantMoney(BigDecimal.valueOf(mwzTotal)));
        tempadIncomeYearItem.setMonthSWTotal(MoneyUtils.accountantMoney(BigDecimal.valueOf(mswTotal)));
        tempadIncomeYearItem.setMonthHDTotal(MoneyUtils.accountantMoney(BigDecimal.valueOf(mhdTotal)));
        tempadIncomeYearItem.setMonthCHTotal(MoneyUtils.accountantMoney(BigDecimal.valueOf(mchTotal)));
        double all = mbmTotal + mnfTotal + mwzTotal + mswTotal + mhdTotal + mchTotal;
        tempadIncomeYearItem.setMonthTotal(MoneyUtils.accountantMoney(BigDecimal.valueOf(all)));

        tempadIncomeYearItem.setYearBMTotal(MoneyUtils.accountantMoney(BigDecimal.valueOf(ybmTotal)));
        tempadIncomeYearItem.setYearNFTotal(MoneyUtils.accountantMoney(BigDecimal.valueOf(ynfTotal)));
        tempadIncomeYearItem.setYearWZTotal(MoneyUtils.accountantMoney(BigDecimal.valueOf(ywzTotal)));
        tempadIncomeYearItem.setYearSWTotal(MoneyUtils.accountantMoney(BigDecimal.valueOf(yswTotal)));
        tempadIncomeYearItem.setYearHDTotal(MoneyUtils.accountantMoney(BigDecimal.valueOf(yhdTotal)));
        tempadIncomeYearItem.setYearCHTotal(MoneyUtils.accountantMoney(BigDecimal.valueOf(ychTotal)));
        all = ybmTotal + ynfTotal + ywzTotal + yswTotal + yhdTotal + ychTotal;
        tempadIncomeYearItem.setYearTotal(MoneyUtils.accountantMoney(BigDecimal.valueOf(all)));

        list.add(tempadIncomeYearItem);
        reporVO.setOprow(adreceivedItemList.size()+1);


        // 广告公司
        monthTotal = 0.0;
        yearTotal = 0.0;
        mbmTotal = 0.0;  //版面广告小计
        mnfTotal = 0.0;  //南方+广告小计
        mwzTotal = 0.0;  //网站广告小计
        mswTotal = 0.0;  //双微广告小计
        mhdTotal = 0.0;  //活动广告小计
        mchTotal = 0.0;  //策划广告小计
        ybmTotal = 0.0;  //版面广告小计
        ynfTotal = 0.0;  //南方+广告小计
        ywzTotal = 0.0;  //网站广告小计
        yswTotal = 0.0;  //双微广告小计
        yhdTotal = 0.0;  //活动广告小计
        ychTotal = 0.0;  //策划广告小计

        for( int i = 0; i < adreceivedItemList.size(); i++ ) {
            AdIncomeYearItem adIncomeYearItem = new AdIncomeYearItem();
            AdreceivedItem adreceivedItem = adreceivedItemList.get(i);
            adIncomeYearItem.setYear(adreceivedItem.getName());
            adIncomeYearItem.setCompanyName("广告公司");
            monthTotal = 0.0;
            yearTotal = 0.0;
            // 本月数
            List<ColumnSummary> tempList = adreceivedItem.getAdcolumnSummaryList();
            for( int k = 0; k < tempList.size(); k++ ) {
                ColumnSummary  columnSummary = tempList.get(k);
                monthTotal += columnSummary.getSummary().doubleValue();
                if( columnSummary.getName().equals("版面广告") ) {
                    adIncomeYearItem.setMonthBMTotal(columnSummary.getStrsummary());
                    mbmTotal += columnSummary.getSummary().doubleValue();
                }
                if( columnSummary.getName().equals("南方+") ) {
                    adIncomeYearItem.setMonthNFTotal(columnSummary.getStrsummary());
                    mnfTotal += columnSummary.getSummary().doubleValue();

                }
                if( columnSummary.getName().equals("网站") ) {
                    adIncomeYearItem.setMonthWZTotal(columnSummary.getStrsummary());
                    mwzTotal += columnSummary.getSummary().doubleValue();
                }
                if( columnSummary.getName().equals("双微") ) {
                    adIncomeYearItem.setMonthSWTotal(columnSummary.getStrsummary());
                    mswTotal += columnSummary.getSummary().doubleValue();

                }
                if( columnSummary.getName().equals("多元化") ) {
                    adIncomeYearItem.setMonthHDTotal(columnSummary.getStrsummary());
                    mhdTotal += columnSummary.getSummary().doubleValue();

                }
                if( columnSummary.getName().equals("活动") ) {
                    adIncomeYearItem.setMonthHDTotal(columnSummary.getStrsummary());
                    mhdTotal += columnSummary.getSummary().doubleValue();

                }
                if( columnSummary.getName().equals("策划制作") ) {
                    adIncomeYearItem.setMonthCHTotal(columnSummary.getStrsummary());
                    mchTotal += columnSummary.getSummary().doubleValue();
                }
            }
            // 本年数
            tempList = adreceivedItem.getAdyearcolumnSummaryList();
            for( int k = 0; k < tempList.size(); k++ ) {
                ColumnSummary  columnSummary = tempList.get(k);
                yearTotal += columnSummary.getSummary().doubleValue();
                if( columnSummary.getName().equals("版面广告") ) {
                    adIncomeYearItem.setYearBMTotal(columnSummary.getStrsummary());
                    ybmTotal += columnSummary.getSummary().doubleValue();

                }
                if( columnSummary.getName().equals("南方+") ) {
                    adIncomeYearItem.setYearNFTotal(columnSummary.getStrsummary());
                    ynfTotal += columnSummary.getSummary().doubleValue();

                }
                if( columnSummary.getName().equals("网站") ) {
                    adIncomeYearItem.setYearWZTotal(columnSummary.getStrsummary());
                    ywzTotal += columnSummary.getSummary().doubleValue();

                }
                if( columnSummary.getName().equals("双微") ) {
                    adIncomeYearItem.setYearSWTotal(columnSummary.getStrsummary());
                    yswTotal += columnSummary.getSummary().doubleValue();

                }
                if( columnSummary.getName().equals("多元化") ) {
                    adIncomeYearItem.setYearHDTotal(columnSummary.getStrsummary());
                    yhdTotal += columnSummary.getSummary().doubleValue();

                }
                if( columnSummary.getName().equals("活动") ) {
                    adIncomeYearItem.setYearHDTotal(columnSummary.getStrsummary());
                    yhdTotal += columnSummary.getSummary().doubleValue();

                }
                if( columnSummary.getName().equals("策划制作") ) {
                    adIncomeYearItem.setYearCHTotal(columnSummary.getStrsummary());
                    ychTotal += columnSummary.getSummary().doubleValue();

                }
            }
            adIncomeYearItem.setMonthTotal(MoneyUtils.accountantMoney(BigDecimal.valueOf(monthTotal)));
            adIncomeYearItem.setYearTotal(MoneyUtils.accountantMoney(BigDecimal.valueOf(yearTotal)));
            list.add(adIncomeYearItem);
        }
        // 广告公司小计
        tempadIncomeYearItem = new AdIncomeYearItem();
        tempadIncomeYearItem.setYear("小计");
        tempadIncomeYearItem.setCompanyName("广告公司");
        tempadIncomeYearItem.setMonthBMTotal(MoneyUtils.accountantMoney(BigDecimal.valueOf(mbmTotal)));
        tempadIncomeYearItem.setMonthNFTotal(MoneyUtils.accountantMoney(BigDecimal.valueOf(mnfTotal)));
        tempadIncomeYearItem.setMonthWZTotal(MoneyUtils.accountantMoney(BigDecimal.valueOf(mwzTotal)));
        tempadIncomeYearItem.setMonthSWTotal(MoneyUtils.accountantMoney(BigDecimal.valueOf(mswTotal)));
        tempadIncomeYearItem.setMonthHDTotal(MoneyUtils.accountantMoney(BigDecimal.valueOf(mhdTotal)));
        tempadIncomeYearItem.setMonthCHTotal(MoneyUtils.accountantMoney(BigDecimal.valueOf(mchTotal)));
        all = mbmTotal + mnfTotal + mwzTotal + mswTotal + mhdTotal + mchTotal;
        tempadIncomeYearItem.setMonthTotal(MoneyUtils.accountantMoney(BigDecimal.valueOf(all)));

        tempadIncomeYearItem.setYearBMTotal(MoneyUtils.accountantMoney(BigDecimal.valueOf(ybmTotal)));
        tempadIncomeYearItem.setYearNFTotal(MoneyUtils.accountantMoney(BigDecimal.valueOf(ynfTotal)));
        tempadIncomeYearItem.setYearWZTotal(MoneyUtils.accountantMoney(BigDecimal.valueOf(ywzTotal)));
        tempadIncomeYearItem.setYearSWTotal(MoneyUtils.accountantMoney(BigDecimal.valueOf(yswTotal)));
        tempadIncomeYearItem.setYearHDTotal(MoneyUtils.accountantMoney(BigDecimal.valueOf(yhdTotal)));
        tempadIncomeYearItem.setYearCHTotal(MoneyUtils.accountantMoney(BigDecimal.valueOf(ychTotal)));
        all = ybmTotal + ynfTotal + ywzTotal + yswTotal + yhdTotal + ychTotal;
        tempadIncomeYearItem.setYearTotal(MoneyUtils.accountantMoney(BigDecimal.valueOf(all)));

        list.add(tempadIncomeYearItem);
        reporVO.setAdrow(adreceivedItemList.size()+1);

        // 汇总
        monthTotal = 0.0;
        yearTotal = 0.0;
        mbmTotal = 0.0;  //版面广告小计
        mnfTotal = 0.0;  //南方+广告小计
        mwzTotal = 0.0;  //网站广告小计
        mswTotal = 0.0;  //双微广告小计
        mhdTotal = 0.0;  //活动广告小计
        mchTotal = 0.0;  //策划广告小计
        ybmTotal = 0.0;  //版面广告小计
        ynfTotal = 0.0;  //南方+广告小计
        ywzTotal = 0.0;  //网站广告小计
        yswTotal = 0.0;  //双微广告小计
        yhdTotal = 0.0;  //活动广告小计
        ychTotal = 0.0;  //策划广告小计

        for( int i = 0; i < adreceivedItemList.size(); i++ ) {
            AdIncomeYearItem adIncomeYearItem = new AdIncomeYearItem();
            AdreceivedItem adreceivedItem = adreceivedItemList.get(i);
            adIncomeYearItem.setYear(adreceivedItem.getName());
            adIncomeYearItem.setCompanyName("汇总");
            monthTotal = 0.0;
            yearTotal = 0.0;
            // 本月数
            List<ColumnSummary> tempList = adreceivedItem.getOpadcolumnSummaryList();
            for( int k = 0; k < tempList.size(); k++ ) {
                ColumnSummary  columnSummary = tempList.get(k);
                monthTotal += columnSummary.getSummary().doubleValue();
                if( columnSummary.getName().equals("版面广告") ) {
                    adIncomeYearItem.setMonthBMTotal(columnSummary.getStrsummary());
                    mbmTotal += columnSummary.getSummary().doubleValue();
                }
                if( columnSummary.getName().equals("南方+") ) {
                    adIncomeYearItem.setMonthNFTotal(columnSummary.getStrsummary());
                    mnfTotal += columnSummary.getSummary().doubleValue();

                }
                if( columnSummary.getName().equals("网站") ) {
                    adIncomeYearItem.setMonthWZTotal(columnSummary.getStrsummary());
                    mwzTotal += columnSummary.getSummary().doubleValue();
                }
                if( columnSummary.getName().equals("双微") ) {
                    adIncomeYearItem.setMonthSWTotal(columnSummary.getStrsummary());
                    mswTotal += columnSummary.getSummary().doubleValue();

                }
                if( columnSummary.getName().equals("多元化") ) {
                    adIncomeYearItem.setMonthHDTotal(columnSummary.getStrsummary());
                    mhdTotal += columnSummary.getSummary().doubleValue();

                }
                if( columnSummary.getName().equals("活动") ) {
                    adIncomeYearItem.setMonthHDTotal(columnSummary.getStrsummary());
                    mhdTotal += columnSummary.getSummary().doubleValue();

                }
                if( columnSummary.getName().equals("策划制作") ) {
                    adIncomeYearItem.setMonthCHTotal(columnSummary.getStrsummary());
                    mchTotal += columnSummary.getSummary().doubleValue();
                }
            }
            // 本年数
            tempList = adreceivedItem.getOpadyearcolumnSummaryList();
            for( int k = 0; k < tempList.size(); k++ ) {
                ColumnSummary  columnSummary = tempList.get(k);
                yearTotal += columnSummary.getSummary().doubleValue();
                if( columnSummary.getName().equals("版面广告") ) {
                    adIncomeYearItem.setYearBMTotal(columnSummary.getStrsummary());
                    ybmTotal += columnSummary.getSummary().doubleValue();

                }
                if( columnSummary.getName().equals("南方+") ) {
                    adIncomeYearItem.setYearNFTotal(columnSummary.getStrsummary());
                    ynfTotal += columnSummary.getSummary().doubleValue();

                }
                if( columnSummary.getName().equals("网站") ) {
                    adIncomeYearItem.setYearWZTotal(columnSummary.getStrsummary());
                    ywzTotal += columnSummary.getSummary().doubleValue();

                }
                if( columnSummary.getName().equals("双微") ) {
                    adIncomeYearItem.setYearSWTotal(columnSummary.getStrsummary());
                    yswTotal += columnSummary.getSummary().doubleValue();

                }
                if( columnSummary.getName().equals("多元化") ) {
                    adIncomeYearItem.setYearHDTotal(columnSummary.getStrsummary());
                    yhdTotal += columnSummary.getSummary().doubleValue();

                }

                if( columnSummary.getName().equals("活动") ) {
                    adIncomeYearItem.setYearHDTotal(columnSummary.getStrsummary());
                    yhdTotal += columnSummary.getSummary().doubleValue();

                }
                if( columnSummary.getName().equals("策划制作") ) {
                    adIncomeYearItem.setYearCHTotal(columnSummary.getStrsummary());
                    ychTotal += columnSummary.getSummary().doubleValue();

                }
            }
            adIncomeYearItem.setMonthTotal(MoneyUtils.accountantMoney(BigDecimal.valueOf(monthTotal)));
            adIncomeYearItem.setYearTotal(MoneyUtils.accountantMoney(BigDecimal.valueOf(yearTotal)));
            list.add(adIncomeYearItem);
        }

        // 汇总合计
        tempadIncomeYearItem = new AdIncomeYearItem();
        tempadIncomeYearItem.setYear("合计");
        tempadIncomeYearItem.setCompanyName("汇总");
        tempadIncomeYearItem.setMonthBMTotal(MoneyUtils.accountantMoney(BigDecimal.valueOf(mbmTotal)));
        tempadIncomeYearItem.setMonthNFTotal(MoneyUtils.accountantMoney(BigDecimal.valueOf(mnfTotal)));
        tempadIncomeYearItem.setMonthWZTotal(MoneyUtils.accountantMoney(BigDecimal.valueOf(mwzTotal)));
        tempadIncomeYearItem.setMonthSWTotal(MoneyUtils.accountantMoney(BigDecimal.valueOf(mswTotal)));
        tempadIncomeYearItem.setMonthHDTotal(MoneyUtils.accountantMoney(BigDecimal.valueOf(mhdTotal)));
        tempadIncomeYearItem.setMonthCHTotal(MoneyUtils.accountantMoney(BigDecimal.valueOf(mchTotal)));
        all = mbmTotal + mnfTotal + mwzTotal + mswTotal + mhdTotal + mchTotal;
        tempadIncomeYearItem.setMonthTotal(MoneyUtils.accountantMoney(BigDecimal.valueOf(all)));

        tempadIncomeYearItem.setYearBMTotal(MoneyUtils.accountantMoney(BigDecimal.valueOf(ybmTotal)));
        tempadIncomeYearItem.setYearNFTotal(MoneyUtils.accountantMoney(BigDecimal.valueOf(ynfTotal)));
        tempadIncomeYearItem.setYearWZTotal(MoneyUtils.accountantMoney(BigDecimal.valueOf(ywzTotal)));
        tempadIncomeYearItem.setYearSWTotal(MoneyUtils.accountantMoney(BigDecimal.valueOf(yswTotal)));
        tempadIncomeYearItem.setYearHDTotal(MoneyUtils.accountantMoney(BigDecimal.valueOf(yhdTotal)));
        tempadIncomeYearItem.setYearCHTotal(MoneyUtils.accountantMoney(BigDecimal.valueOf(ychTotal)));
        all = ybmTotal + ynfTotal + ywzTotal + yswTotal + yhdTotal + ychTotal;
        tempadIncomeYearItem.setYearTotal(MoneyUtils.accountantMoney(BigDecimal.valueOf(all)));

        list.add(tempadIncomeYearItem);
        reporVO.setSumrow(adreceivedItemList.size()+1);

        reporVO.setItemList(list);
        return reporVO;
    }

}


