package com.hgzp.advertising.service.statistic.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.pagemodel.statistic.vo.AdIncomeYearMeidaVO;
import com.hgzp.advertising.pagemodel.statistic.vo.AdreceivedItem;
import com.hgzp.advertising.pagemodel.statistic.vo.AdreceivedVO;
import com.hgzp.advertising.service.ad.TbadfromServiceI;
import com.hgzp.advertising.service.business.TwtasksServiceI;
import com.hgzp.advertising.service.media.TbmediaServiceI;
import com.hgzp.advertising.service.statistic.DepReceivableServiceI;
import com.hgzp.advertising.service.statistic.FinanceStatisticServiceI;
import com.hgzp.advertising.utils.DateUtils;
import com.hgzp.core.model.*;
import com.hgzp.mapper.ad.TbadfromMapper;
import com.hgzp.mapper.ad.TbadindustryMapper;
import com.hgzp.mapper.customer.TwcustomerMapper;
import com.hgzp.mapper.finance.TworderapportiondetailMapper;
import com.hgzp.pagemodel.ad.AdCustomerResourceVO;
import com.hgzp.pagemodel.ad.AdCustomerYearItem;
import com.hgzp.pagemodel.ad.AdSummaryVO;
import com.hgzp.pagemodel.schedule.OrderPublishQueryResultVo;
import com.hgzp.pagemodel.statistic.AdChangeVo;
import com.hgzp.pagemodel.statistic.SynQueryResultVO;
import com.hgzp.pagemodel.statistic.SynQueryVO;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.print.attribute.standard.Media;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * DepReceivableServiceIImpl
 * 创建人：lhl
 * 类描述：业务部门应收汇总报表实现类
 * 创建日期：2024/2/1 9:01
 */
@Service
public class DepReceivableServiceIImpl implements DepReceivableServiceI {
    @Autowired
    FinanceStatisticServiceI financeStatisticServiceI;
    @Autowired
    TworderapportiondetailMapper tworderapportiondetailMapper;
    @Autowired
    TwtasksServiceI  twtasksServiceI;
    @Autowired
    TbadindustryMapper  tbadindustryMapper;
    @Autowired
    TbmediaServiceI  tbmediaServiceI;
    @Autowired
    TbadfromServiceI tbadfromServiceI;
    @Autowired
    TbadfromMapper tbadfromMapper;
    @Autowired
    TwcustomerMapper twcustomerMapper;



    private String hadeTitle[] = {"序号", "部门名称", "平面、多元", "南方+", "部门合计", "南方+任务", "南方+完成率","部门任务","部门完成率","平面、多元","南方+","合计","平面、多元","南方+","合计","平面、多元","南方+","合计"};
    private String areahadeTitle[] = {"区域", "报端总任务数", "南方+任务数", "平面、多元", "南方+", "报端合计","报端总任务完成进度","南方+完成进度","平面、多元","南方+","报端合计","平面、多元","南方+","报端合计","平面、多元","南方+","报端合计"};
    private String areaIncomeTitle[] = {"区域", "版面", "新媒体", "南方+", "多元化", "合计","版面","南方+","版面", "新媒体", "南方+", "多元化", "合计","版面","南方+"};
    private String anlysisTitle[] = {"行业名称", "本年累计", "去年累计", "变动金额", "变动幅度"};
    private String changeOrderTitle[] = {"序号", "登记时间","认刊号", "代理名称", "客户名称", "原系统见报日期","原系统应收金额（元）","变动金额（元）","业务员","叠次","经营主体","调整要求","修改后应收金额（元）","修改后发布时间","备注"};

    @Override
    public AdreceivedVO exportDeptReceivable() {
        AdreceivedVO  adreceivedVO = new AdreceivedVO();
        adreceivedVO.setReportDate(DateUtils.transferDate2StringCn(new Date()));
        List<Tbreportmodelitem> tbreportmodelitemList = financeStatisticServiceI.getSumItemFromReport("6","部门");
        if( null == tbreportmodelitemList || tbreportmodelitemList.size() <= 0 ) {
            return  adreceivedVO;
        }
        adreceivedVO.setCompany("取数时间：" + DateUtils.transferDate2StringCn(new Date()));
        adreceivedVO.setTitle( DateUtils.getMonthToDay() + "业务部门广告应收业绩完成汇总表");
        adreceivedVO.setThisYear(String.valueOf(DateUtils.getNowYear()));
        adreceivedVO.setThisYearReportDate(DateUtils.getMonthToDay());
        adreceivedVO.setLastYearReportDate(DateUtils.getLastYearMonthToDay());
        List<AdreceivedItem> adreceivedItemList = new ArrayList<AdreceivedItem>();
        adreceivedVO.setItemList(adreceivedItemList);
        Date  beginDate = DateUtils.getBeginDayOfYear();
        Date  endDate = new Date();
        String startTime = DateUtils.transferDate2String(beginDate);
        String endTime = DateUtils.transferDate2String(endDate);
        for( int i = 0; i < tbreportmodelitemList.size(); i++ ) {
            AdreceivedItem item = new AdreceivedItem();
            Tbreportmodelitem tbreportmodelitem = tbreportmodelitemList.get(i);
            item.setName(tbreportmodelitem.getSheadername());
            // 合计
            if( null == tbreportmodelitem.getSids() || tbreportmodelitem.getSids().isEmpty() ) {

            } else {
                List<String> depids = new ArrayList<String>();
                String[] deptIdArray = tbreportmodelitem.getSids().split(",");
                for( int j = 0; j < deptIdArray.length; j++ ) {
                    depids.add(deptIdArray[j]);
                }
                // 获取部门任务
                item.setAmounttotal(twtasksServiceI.getDeptTask(tbreportmodelitem.getSids(),null,String.valueOf(DateUtils.getNowYear()),null));
                List<Tbreportmodelitem> buissesList = financeStatisticServiceI.getSumItemFromReport("6","业务");
                for( int j = 0; j < buissesList.size(); j++ ) {
                    BigDecimal total = BigDecimal.valueOf(0.00);
                    BigDecimal pretotal = BigDecimal.valueOf(0.00);
                    Tbreportmodelitem buissesItem = buissesList.get(j);
                    List<String> mediaids = new ArrayList<String>();
                    deptIdArray = buissesItem.getSids().split(",");
                    for( int k = 0; k < deptIdArray.length; k++ ) {
                        mediaids.add(deptIdArray[k]);
                    }
                    if( buissesItem.getSnames().equals("0") ) { //媒体
                        total = summaryReceivable(depids,mediaids,null,null,null,DateUtils.getBeginDayOfThisyear(),DateUtils.getThisyearCurrentTime());
                        pretotal = summaryReceivable(depids,mediaids,null,null,null,DateUtils.getBeginDayOfLastyear(),DateUtils.getLastyearCurrentTime());

                    }
                    if( buissesItem.getSnames().equals("1") ) { //叠次
                        total = summaryReceivable(depids,null,mediaids,null,null,DateUtils.getBeginDayOfThisyear(),DateUtils.getThisyearCurrentTime());
                        pretotal = summaryReceivable(depids,null,mediaids,null,null,DateUtils.getBeginDayOfLastyear(),DateUtils.getLastyearCurrentTime());
                    }
                    if( buissesItem.getSnames().equals("1") ) { //叠次版本
                        total = summaryReceivable(depids,null,null,mediaids,null,DateUtils.getBeginDayOfThisyear(),DateUtils.getThisyearCurrentTime());
                        pretotal = summaryReceivable(depids,null,null,mediaids,null,DateUtils.getBeginDayOfLastyear(),DateUtils.getLastyearCurrentTime());
                    }
                    if(buissesItem.getSheadername().equals("平面、多元") ) {
                        item.setAdtotal(total);
                        item.setAdmonthtotal(pretotal);
                    }
                    if(buissesItem.getSheadername().equals("南方+") ) {
                        item.setOptotal(total);
                        item.setOpmonthtotal(pretotal);
                        // 获取南方+任务
                        item.setAmountmonthtotal(twtasksServiceI.getDeptTask(tbreportmodelitem.getSids(),buissesItem.getSids(),String.valueOf(DateUtils.getNowYear()),"0"));
                    }
                }
            }
            adreceivedItemList.add(item);
        }
        adreceivedVO.setItemList(adreceivedItemList);
        return adreceivedVO;
    }

    @Override
    public XSSFWorkbook exportDeptReceivableExcel() {
        AdreceivedVO vo = exportDeptReceivable();
        // 输出EXCEL
        XSSFWorkbook wb = new XSSFWorkbook();
        //创建工作表
        Sheet sheet = wb.createSheet("部门汇总表");
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

        XSSFCellStyle stylep = wb.createCellStyle();
        DataFormat format = wb.createDataFormat();
        stylep.setBorderTop(BorderStyle.MEDIUM);
        stylep.setBorderBottom(BorderStyle.MEDIUM);
        stylep.setBorderLeft(BorderStyle.MEDIUM);
        stylep.setBorderRight(BorderStyle.MEDIUM);
        stylep.setFont(font1);
        stylep.setAlignment(HorizontalAlignment.CENTER);//水平居中
        stylep.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        stylep.setDataFormat(format.getFormat("#,##0.00"));
        stylep.setDataFormat(format.getFormat("0.00%"));

        XSSFCellStyle style = wb.createCellStyle();
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setBorderRight(BorderStyle.MEDIUM);
        style.setFont(font1);
        style.setAlignment(HorizontalAlignment.CENTER);//水平居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        style.setDataFormat(format.getFormat("#,##0.00"));

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
        cell.setCellValue(vo.getTitle());
        cell.setCellStyle(style1) ;
        sheet.addMergedRegion(region);
        // 创建制表行
        Row tabRow = sheet.createRow(excelRow++);
        tabRow.setHeight(h2);
        for (int i = 0; i < hadeTitle.length; i++) {
            cell = tabRow.createCell(i);
            cell.setCellStyle(style2);
        }
        region = new CellRangeAddress(1, 1, 0,2);
        cell0 = region.getFirstColumn();
        cell = tabRow.getCell(cell0);
        cell.setCellValue(vo.getCompany());
        sheet.addMergedRegion(region);
        cell = tabRow.getCell(hadeTitle.length-1);  //单位
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
        cell.setCellValue("序号");
        cell.setCellStyle(style3);
        sheet.addMergedRegion(region);
        region = new CellRangeAddress(2, 3, 1,1);
        cell0 = region.getFirstColumn();
        cell = headRow1.getCell(cell0);
        cell.setCellValue("名称");
        cell.setCellStyle(style3);
        sheet.addMergedRegion(region);
        region = new CellRangeAddress(2, 2, 2,4);
        cell0 = region.getFirstColumn();
        cell = headRow1.getCell(cell0);
        cell.setCellValue(DateUtils.getMonthToDay() + "广告应收款");
        cell.setCellStyle(style3);
        sheet.addMergedRegion(region);
        region = new CellRangeAddress(2, 2, 5,8);
        cell0 = region.getFirstColumn();
        cell = headRow1.getCell(cell0);
        cell.setCellStyle(style3);
        cell.setCellValue(String.valueOf(DateUtils.getNowYear()) + "年任务完成情况（万元）");
        sheet.addMergedRegion(region);
        region = new CellRangeAddress(2, 2, 9,11);
        cell0 = region.getFirstColumn();
        cell = headRow1.getCell(cell0);
        cell.setCellStyle(style3);
        cell.setCellValue(DateUtils.getLastYearMonthToDay()+ "广告应收款");
        sheet.addMergedRegion(region);
        region = new CellRangeAddress(2, 2, 12,14);
        cell0 = region.getFirstColumn();
        cell = headRow1.getCell(cell0);
        cell.setCellStyle(style3);
        cell.setCellValue("同比增长值");
        sheet.addMergedRegion(region);
        region = new CellRangeAddress(2, 2, 15,hadeTitle.length-1);
        cell0 = region.getFirstColumn();
        cell = headRow1.getCell(cell0);
        cell.setCellStyle(style3);
        cell.setCellValue("同比增长率");
        sheet.addMergedRegion(region);

        for (int i = 2; i < hadeTitle.length; i++) {
            cell = headRow2.createCell(i);
            if( i == 2 || i == 9 || i == 12 || i == 15 ) {
                cell.setCellValue("平面、多元");
            }
            if( i == 3 || i == 10 || i == 13 || i == 16 ) {
                cell.setCellValue("南方+");
            }
            if( i == 4 ) {
                cell.setCellValue("部门合计");
            }
            if( i == 11 || i == 14 || i == 17 ) {
                cell.setCellValue("合计");
            }
            if( i == 5 ) {
                cell.setCellValue("南方+任务");
            }
            if( i == 6 ) {
                cell.setCellValue("南方+完成率");
            }
            if( i == 7 ) {
                cell.setCellValue("部门任务");
            }
            if( i == 8 ) {
                cell.setCellValue("部门完成率");
            }
            cell.setCellStyle(style3);
        }
        // 输出汇总项
        List<AdreceivedItem> adreceivedItemList = vo.getItemList();
        for (int i = 0; i < adreceivedItemList.size(); i++) {
            double sumtotal = 0.0;
            AdreceivedItem item = adreceivedItemList.get(i);
            Row itemRow = sheet.createRow(excelRow++);
            itemRow.setHeight(h2);
            for (int cellno = 0; cellno < hadeTitle.length; cellno++) {
                cell = itemRow.createCell(cellno);
                cell.setCellStyle(style);
                if (cellno == 0) {
                    cell.setCellValue(String.valueOf(i+1));
                }
                if (cellno == 1) {
                    cell.setCellValue(item.getName());
                }
                if (cellno == 2 ) {  // 平面、多元汇总
                    if( -1 == item.getName().indexOf("总计") ) {
                        if (null != item.getAdtotal()) {
                            cell.setCellValue(item.getAdtotal().doubleValue());
                        }
                    } else {
                        cell.setCellFormula("SUM(" + "C5:" + "C" + String.valueOf(i + 5-1) + ")");
                    }

                }
                if (cellno == 3) {  // 南方+汇总
                    if( -1 == item.getName().indexOf("总计") ) {
                        if (null != item.getOptotal()) {
                            cell.setCellValue(item.getOptotal().doubleValue());
                        }
                    } else {
                        cell.setCellFormula("SUM(" + "D5:" + "D" + String.valueOf(i + 5-1) + ")");
                    }
                }
                if (cellno == 4) {  // 部门合计
                    if( -1 == item.getName().indexOf("总计") ) {
                        cell.setCellFormula("C" + String.valueOf(i + 5) + "+" + "D" + String.valueOf(i + 5));
                    } else {
                        cell.setCellFormula("SUM(" + "E5:" + "E" + String.valueOf(i + 5-1) + ")");
                    }
                }
                if (cellno == 5) {  // 南方+任务
                    if( -1 == item.getName().indexOf("总计") ) {
                        if (null != item.getAmountmonthtotal()) {
                            cell.setCellValue(item.getAmountmonthtotal().doubleValue());
                        }
                    } else {
                        cell.setCellFormula("SUM(" + "F5:" + "F" + String.valueOf(i + 5-1) + ")");
                    }
                }
                if (cellno == 6) {  // 南方+完成率
                    /*if( -1 == item.getName().indexOf("总计") ) {
                        if (null != item.getAmountmonthtotal()) {
                            if (item.getAmountmonthtotal().doubleValue() > 0) {
                                cell.setCellFormula("D" + String.valueOf(i + 5) + "/" + "F" + String.valueOf(i + 5) + "/10000");
                            } else {
                                cell.setCellValue(0.00);
                            }

                        } else {
                            cell.setCellValue(0.00);
                        }
                    } else {
                        cell.setCellFormula("D" + String.valueOf(i + 5) + "/" + "F" + String.valueOf(i + 5) + "/10000");
                    }
                    cell.setCellStyle(stylep);*/
                    cell.setCellFormula("D" + String.valueOf(i + 5) + "/" + "F" + String.valueOf(i + 5) + "/10000");
                    cell.setCellStyle(stylep);

                }
                if (cellno == 7) {  // 部门任务
                    if( -1 == item.getName().indexOf("总计") ) {
                        if (null != item.getAmounttotal()) {
                            cell.setCellValue(item.getAmounttotal().doubleValue());
                        }
                    } else {
                        cell.setCellFormula("SUM(" + "H5:" + "H" + String.valueOf(i + 5-1) + ")");
                    }
                }
                if (cellno == 8) {  // 部门完成率
                    /*if( -1 == item.getName().indexOf("总计") ) {
                        if (null != item.getAmounttotal()) {
                            if (item.getAmounttotal().doubleValue() > 0) {
                                cell.setCellFormula("E" + String.valueOf(i + 5) + "/" + "H" + String.valueOf(i + 5) + "/10000");
                            } else {
                                cell.setCellValue(0.00);
                            }

                        } else {
                            cell.setCellValue(0.00);
                        }
                    } else {
                        cell.setCellFormula("E" + String.valueOf(i + 5) + "/" + "H" + String.valueOf(i + 5) + "/10000");
                    }
                    cell.setCellStyle(stylep);*/
                    cell.setCellFormula("E" + String.valueOf(i + 5) + "/" + "H" + String.valueOf(i + 5) + "/10000");
                    cell.setCellStyle(stylep);
                }
                if (cellno == 9) {  // 去年平面、多元汇总
                    if( -1 == item.getName().indexOf("总计") ) {
                        if (null != item.getAdmonthtotal()) {
                            cell.setCellValue(item.getAdmonthtotal().doubleValue());
                        }
                    } else {
                        cell.setCellFormula("SUM(" + "J5:" + "J" + String.valueOf(i + 5-1) + ")");
                    }
                }
                if (cellno == 10) {  // 去年南方+
                    if( -1 == item.getName().indexOf("总计") ) {
                        if (null != item.getOpmonthtotal()) {
                            cell.setCellValue(item.getOpmonthtotal().doubleValue());
                        }
                    } else {
                        cell.setCellFormula("SUM(" + "K5:" + "K" + String.valueOf(i + 5-1) + ")");
                    }
                }
                if (cellno == 11) {  // 去年广告应收款合计
                    if( -1 == item.getName().indexOf("总计") ) {
                        if (null != item.getAdmonthtotal() && null != item.getOpmonthtotal()) {
                            sumtotal = item.getAdmonthtotal().doubleValue() + item.getOpmonthtotal().doubleValue();
                        } else {
                            sumtotal = 0.0;
                        }
                    } else {
                        cell.setCellFormula("SUM(" + "L5:" + "L" + String.valueOf(i + 5-1) + ")");
                    }
                    cell.setCellFormula("J" + String.valueOf(i+5) + "+" + "K" + String.valueOf(i+5));
                }
                if (cellno == 12) {  //同比增长（平面、多元）
                    cell.setCellFormula("C" + String.valueOf(i+5) + "-" + "J" + String.valueOf(i+5));
                }
                if (cellno == 13) {  //同比增长（南方+）
                    cell.setCellFormula("D" + String.valueOf(i+5) + "-" + "K" + String.valueOf(i+5));
                }
                if (cellno == 14) {  //同比增长合计
                    cell.setCellFormula("E" + String.valueOf(i+5) + "-" + "L" + String.valueOf(i+5));
                }
                if (cellno == 15) {  //同比增长率（平面、多元）
                    cell.setCellFormula("C" + String.valueOf(i + 5) + "/" + "J" + String.valueOf(i + 5) + "-1");
                    cell.setCellStyle(stylep);
                }
                if (cellno == 16) {  //同比增长率（南方+）
                    cell.setCellFormula("D" + String.valueOf(i + 5) + "/" + "K" + String.valueOf(i + 5) + "-1");
                    cell.setCellStyle(stylep);
                }
                if (cellno == 17) {  //同比增长率（合计）
                    cell.setCellFormula("E" + String.valueOf(i + 5) + "/" + "L" + String.valueOf(i + 5) + "-1");
                    cell.setCellStyle(stylep);
                }

            }
        }
        return wb;
    }

    @Override
    public AdreceivedVO exportAreaReceivable() {
        AdreceivedVO  adreceivedVO = new AdreceivedVO();
        adreceivedVO.setReportDate(DateUtils.transferDate2StringCn(new Date()));
        List<Tbreportmodelitem> tbreportmodelitemList = financeStatisticServiceI.getSumItemFromReport("7","部门");
        if( null == tbreportmodelitemList || tbreportmodelitemList.size() <= 0 ) {
            return  adreceivedVO;
        }
        adreceivedVO.setCompany("取数时间：" + DateUtils.transferDate2StringCn(new Date()));
        adreceivedVO.setTitle( DateUtils.getMonthToDay() + "区域办、内容生产团队完成情况表");
        adreceivedVO.setThisYear(String.valueOf(DateUtils.getNowYear()));
        adreceivedVO.setThisYearReportDate(DateUtils.getMonthToDay());
        adreceivedVO.setLastYearReportDate(DateUtils.getLastYearMonthToDay());
        List<AdreceivedItem> adreceivedItemList = new ArrayList<AdreceivedItem>();
        adreceivedVO.setItemList(adreceivedItemList);
        Date  beginDate = DateUtils.getBeginDayOfYear();
        Date  endDate = new Date();
        String startTime = DateUtils.transferDate2String(beginDate);
        String endTime = DateUtils.transferDate2String(endDate);
        for( int i = 0; i < tbreportmodelitemList.size(); i++ ) {
            AdreceivedItem item = new AdreceivedItem();
            Tbreportmodelitem tbreportmodelitem = tbreportmodelitemList.get(i);
            item.setName(tbreportmodelitem.getSheadername());
            // 合计
            if( null == tbreportmodelitem.getSids() || tbreportmodelitem.getSids().isEmpty() ) {

            } else {
                List<String> depids = new ArrayList<String>();
                String[] deptIdArray = tbreportmodelitem.getSids().split(",");
                for( int j = 0; j < deptIdArray.length; j++ ) {
                    depids.add(deptIdArray[j]);
                }
                // 获取部门任务
                item.setAmounttotal(twtasksServiceI.getDeptTask(tbreportmodelitem.getSids(),null,String.valueOf(DateUtils.getNowYear()),null));
                List<Tbreportmodelitem> buissesList = financeStatisticServiceI.getSumItemFromReport("6","业务");
                for( int j = 0; j < buissesList.size(); j++ ) {
                    BigDecimal total = BigDecimal.valueOf(0.00);
                    BigDecimal pretotal = BigDecimal.valueOf(0.00);
                    Tbreportmodelitem buissesItem = buissesList.get(j);
                    List<String> mediaids = new ArrayList<String>();
                    deptIdArray = buissesItem.getSids().split(",");
                    for( int k = 0; k < deptIdArray.length; k++ ) {
                        mediaids.add(deptIdArray[k]);
                    }
                    if( buissesItem.getSnames().equals("0") ) { //媒体
                        total = summaryReceivable(depids,mediaids,null,null,null,DateUtils.getBeginDayOfThisyear(),DateUtils.getThisyearCurrentTime());
                        pretotal = summaryReceivable(depids,mediaids,null,null,null,DateUtils.getBeginDayOfLastyear(),DateUtils.getLastyearCurrentTime());

                    }
                    if( buissesItem.getSnames().equals("1") ) { //叠次
                        total = summaryReceivable(depids,null,mediaids,null,null,DateUtils.getBeginDayOfThisyear(),DateUtils.getThisyearCurrentTime());
                        pretotal = summaryReceivable(depids,null,mediaids,null,null,DateUtils.getBeginDayOfLastyear(),DateUtils.getLastyearCurrentTime());
                    }
                    if( buissesItem.getSnames().equals("1") ) { //叠次版本
                        total = summaryReceivable(depids,null,null,mediaids,null,DateUtils.getBeginDayOfThisyear(),DateUtils.getThisyearCurrentTime());
                        pretotal = summaryReceivable(depids,null,null,mediaids,null,DateUtils.getBeginDayOfLastyear(),DateUtils.getLastyearCurrentTime());
                    }
                    if(buissesItem.getSheadername().equals("平面、多元") ) {
                        item.setAdtotal(total);
                        item.setAdmonthtotal(pretotal);
                    }
                    if(buissesItem.getSheadername().equals("南方+") ) {
                        item.setOptotal(total);
                        item.setOpmonthtotal(pretotal);
                        // 获取南方+任务
                        item.setAmountmonthtotal(twtasksServiceI.getDeptTask(tbreportmodelitem.getSids(),buissesItem.getSids(),String.valueOf(DateUtils.getNowYear()),"0"));
                    }
                }
            }
            adreceivedItemList.add(item);
        }
        adreceivedVO.setItemList(adreceivedItemList);
        return adreceivedVO;
    }

    @Override
    public XSSFWorkbook exportAreaReceivableExcel() {
        AdreceivedVO vo = exportAreaReceivable();
        // 输出EXCEL
        XSSFWorkbook wb = new XSSFWorkbook();
        //创建工作表
        Sheet sheet = wb.createSheet("区域完成情况统计表");
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

        XSSFCellStyle stylep = wb.createCellStyle();
        DataFormat format = wb.createDataFormat();
        stylep.setBorderTop(BorderStyle.MEDIUM);
        stylep.setBorderBottom(BorderStyle.MEDIUM);
        stylep.setBorderLeft(BorderStyle.MEDIUM);
        stylep.setBorderRight(BorderStyle.MEDIUM);
        stylep.setFont(font1);
        stylep.setAlignment(HorizontalAlignment.CENTER);//水平居中
        stylep.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        stylep.setDataFormat(format.getFormat("#,##0.00"));
        stylep.setDataFormat(format.getFormat("0.00%"));

        XSSFCellStyle style = wb.createCellStyle();
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setBorderRight(BorderStyle.MEDIUM);
        style.setFont(font1);
        style.setAlignment(HorizontalAlignment.CENTER);//水平居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        style.setDataFormat(format.getFormat("#,##0.00"));

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
        for (int i = 0; i < areahadeTitle.length; i++) {
            cell = titleRow.createCell(i);
            cell.setCellStyle(style1);
            if( i == 0 ) {
                sheet.setColumnWidth(i, columnwidth1);
            } else {
                sheet.setColumnWidth(i, columnwidth2);
            }
        }
        CellRangeAddress region = new CellRangeAddress(0, 0, 0, areahadeTitle.length-1);
        int cell0 = region.getFirstColumn();
        cell = titleRow.getCell(cell0);
        cell.setCellValue(vo.getTitle());
        cell.setCellStyle(style1) ;
        sheet.addMergedRegion(region);
        // 创建制表行
        Row tabRow = sheet.createRow(excelRow++);
        tabRow.setHeight(h2);
        for (int i = 0; i < areahadeTitle.length; i++) {
            cell = tabRow.createCell(i);
            cell.setCellStyle(style2);
        }
        region = new CellRangeAddress(1, 1, 0,2);
        cell0 = region.getFirstColumn();
        cell = tabRow.getCell(cell0);
        cell.setCellValue(vo.getCompany());
        sheet.addMergedRegion(region);
        cell = tabRow.getCell(areahadeTitle.length-1);  //单位
        cell.setCellValue("单位（万元）");
        // 创建标头1
        Row headRow1 = sheet.createRow(excelRow++);
        Row headRow2 = sheet.createRow(excelRow++);
        headRow1.setHeight(h2);
        headRow2.setHeight(h2);
        for (int i = 0; i < areahadeTitle.length; i++) {
            cell = headRow1.createCell(i);
            cell.setCellStyle(style3);
            cell = headRow2.createCell(i);
            cell.setCellStyle(style3);
        }
        region = new CellRangeAddress(2, 3, 0,0);
        cell0 = region.getFirstColumn();
        cell = headRow1.getCell(cell0);
        cell.setCellValue("区域");
        cell.setCellStyle(style3);
        sheet.addMergedRegion(region);
        region = new CellRangeAddress(2, 3, 1,1);
        cell0 = region.getFirstColumn();
        cell = headRow1.getCell(cell0);
        cell.setCellValue(vo.getThisYear() + "年报端总任务数");
        cell.setCellStyle(style3);
        sheet.addMergedRegion(region);

        region = new CellRangeAddress(2, 3, 2,2);
        cell0 = region.getFirstColumn();
        cell = headRow1.getCell(cell0);
        cell.setCellValue(vo.getThisYear() + "南方+任务数");
        cell.setCellStyle(style3);
        sheet.addMergedRegion(region);

        region = new CellRangeAddress(2, 2, 3,5);
        cell0 = region.getFirstColumn();
        cell = headRow1.getCell(cell0);
        cell.setCellValue(DateUtils.getMonthToDay() + "实际完成数");
        cell.setCellStyle(style3);
        sheet.addMergedRegion(region);

        region = new CellRangeAddress(2, 3, 6,6);
        cell0 = region.getFirstColumn();
        cell = headRow1.getCell(cell0);
        cell.setCellValue(vo.getThisYear() + "年报端总任务完成进度");
        cell.setCellStyle(style3);
        sheet.addMergedRegion(region);

        region = new CellRangeAddress(2, 3, 7,7);
        cell0 = region.getFirstColumn();
        cell = headRow1.getCell(cell0);
        cell.setCellValue(vo.getThisYear() + "年南方+完成进度");
        cell.setCellStyle(style3);
        sheet.addMergedRegion(region);


        region = new CellRangeAddress(2, 2, 8,10);
        cell0 = region.getFirstColumn();
        cell = headRow1.getCell(cell0);
        cell.setCellStyle(style3);
        cell.setCellValue(String.valueOf(DateUtils.getNowYear()) + "年实际完成数");
        sheet.addMergedRegion(region);

        region = new CellRangeAddress(2, 2, 11,13);
        cell0 = region.getFirstColumn();
        cell = headRow1.getCell(cell0);
        cell.setCellStyle(style3);
        cell.setCellValue("同比增长值");
        sheet.addMergedRegion(region);
        region = new CellRangeAddress(2, 2, 14,areahadeTitle.length-1);
        cell0 = region.getFirstColumn();
        cell = headRow1.getCell(cell0);
        cell.setCellStyle(style3);
        cell.setCellValue("同比增长率");
        sheet.addMergedRegion(region);

        for (int i = 3; i < areahadeTitle.length; i++) {
            cell = headRow2.createCell(i);
            if( i == 3 || i == 8 || i == 11 || i == 14 ) {
                cell.setCellValue("平面、多元");
            }
            if( i == 4 || i == 9 || i == 12 || i == 15 ) {
                cell.setCellValue("南方+");
            }
            if( i == 5 || i== 10 || i == 13 || i == 16 ) {
                cell.setCellValue("报端合计");
            }
            if( i == 6 ) {
                cell.setCellValue(vo.getThisYear() + "年报端总任务完成进度");
            }
            if( i == 7 ) {
                cell.setCellValue(vo.getThisYear() + "年南方+完成进度");
            }
            cell.setCellStyle(style3);
        }
        // 输出汇总项
        List<AdreceivedItem> adreceivedItemList = vo.getItemList();
        for (int i = 0; i < adreceivedItemList.size(); i++) {
            double sumtotal = 0.0;
            AdreceivedItem item = adreceivedItemList.get(i);
            Row itemRow = sheet.createRow(excelRow++);
            itemRow.setHeight(h2);
            for (int cellno = 0; cellno < areahadeTitle.length; cellno++) {
                cell = itemRow.createCell(cellno);
                cell.setCellStyle(style);
                if (cellno == 0) {
                    cell.setCellValue(item.getName());
                }
                if (cellno == 1) {  //报端总任务数
                    if( null != item.getAmounttotal() ) {
                        cell.setCellValue(item.getAmounttotal().doubleValue());
                    }
                }
                if (cellno == 2) {  // 南方+任务数
                    if (null != item.getAmountmonthtotal()) {
                       cell.setCellValue(item.getAmountmonthtotal().doubleValue());
                    }
                }
                if (cellno == 3) {  // 实际完成数平面、多元
                    if (null != item.getAdtotal()) {
                        cell.setCellValue(item.getAdtotal().doubleValue()/10000);
                    }
                }

                if (cellno == 4) {  // 实际完成数南方+
                    if (null != item.getOptotal()) {
                        cell.setCellValue(item.getOptotal().doubleValue()/10000);
                    }
                }
                if (cellno == 5) {  // 报端合计
                    cell.setCellFormula("D" + String.valueOf(i + 5)  + "+" + "E" + String.valueOf(i + 5));
                }
                if (cellno == 6) {  // 报端完成进度
                    cell.setCellFormula("F" + String.valueOf(i + 5) + "/" + "B" + String.valueOf(i + 5));
                    cell.setCellStyle(stylep);
                 }
                if (cellno == 7) {  // 南方+完成进度
                    cell.setCellFormula("E" + String.valueOf(i + 5) + "/" + "C" + String.valueOf(i + 5));
                    cell.setCellStyle(stylep);
                }
                if (cellno == 8) {  // 去年平面、多元汇总
                    if (null != item.getAdmonthtotal()) {
                        cell.setCellValue(item.getAdmonthtotal().doubleValue()/10000);
                    }
                }
                if (cellno == 9) {  // 去年南方+
                    if (null != item.getOpmonthtotal()) {
                        cell.setCellValue(item.getOpmonthtotal().doubleValue()/10000);
                    }
                }
                if (cellno == 10) {  // 去年报端合计
                    cell.setCellFormula("I" + String.valueOf(i + 5)  + "+" + "J" + String.valueOf(i + 5));
                }
                if (cellno == 11) {  //同比增长（平面、多元）
                    cell.setCellFormula("D" + String.valueOf(i+5) + "-" + "I" + String.valueOf(i+5));
                }
                if (cellno == 12) {  //同比增长（南方+）
                    cell.setCellFormula("E" + String.valueOf(i+5) + "-" + "J" + String.valueOf(i+5));
                }
                if (cellno == 13) {  //同比增长合计
                    cell.setCellFormula("F" + String.valueOf(i+5) + "-" + "K" + String.valueOf(i+5));
                }
                if (cellno == 14) {  //同比增长率（平面、多元）
                    cell.setCellFormula("D" + String.valueOf(i + 5) + "/" + "I" + String.valueOf(i + 5) + "-1");
                    cell.setCellStyle(stylep);
                }
                if (cellno == 15) {  //同比增长率（南方+）
                    cell.setCellFormula("E" + String.valueOf(i + 5) + "/" + "J" + String.valueOf(i + 5) + "-1");
                    cell.setCellStyle(stylep);
                }
                if (cellno == 16) {  //同比增长率（合计）
                    cell.setCellFormula("F" + String.valueOf(i + 5) + "/" + "K" + String.valueOf(i + 5) + "-1");
                    cell.setCellStyle(stylep);
                }

            }
        }
        return wb;
    }

    @Override
    public AdreceivedVO exportAreaIncomeReceivable() {
        AdreceivedVO  adreceivedVO = new AdreceivedVO();
        adreceivedVO.setReportDate(DateUtils.transferDate2StringCn(new Date()));
        List<Tbreportmodelitem> tbreportmodelitemList = financeStatisticServiceI.getSumItemFromReport("8","部门");
        if( null == tbreportmodelitemList || tbreportmodelitemList.size() <= 0 ) {
            return  adreceivedVO;
        }
        adreceivedVO.setCompany("取数时间：" + DateUtils.transferDate2StringCn(new Date()));
        adreceivedVO.setTitle( DateUtils.getMonthToDay() + "区域应收统计表");
        adreceivedVO.setThisYear(String.valueOf(DateUtils.getNowYear()));
        adreceivedVO.setThisYearReportDate(DateUtils.getMonthToDay());
        adreceivedVO.setLastYearReportDate(DateUtils.getLastYearMonthToDay());
        List<AdreceivedItem> adreceivedItemList = new ArrayList<AdreceivedItem>();
        adreceivedVO.setItemList(adreceivedItemList);
        Date  beginDate = DateUtils.getBeginDayOfYear();
        Date  endDate = new Date();
        String startTime = DateUtils.transferDate2String(beginDate);
        String endTime = DateUtils.transferDate2String(endDate);
        for( int i = 0; i < tbreportmodelitemList.size(); i++ ) {
            AdreceivedItem item = new AdreceivedItem();
            Tbreportmodelitem tbreportmodelitem = tbreportmodelitemList.get(i);
            item.setName(tbreportmodelitem.getSheadername());
            // 合计
            if( null == tbreportmodelitem.getSids() || tbreportmodelitem.getSids().isEmpty() ) {

            } else {
                List<String> depids = new ArrayList<String>();
                String[] deptIdArray = tbreportmodelitem.getSids().split(",");
                for( int j = 0; j < deptIdArray.length; j++ ) {
                    depids.add(deptIdArray[j]);
                }
                List<Tbreportmodelitem> buissesList = financeStatisticServiceI.getSumItemFromReport("8","业务");
                for( int j = 0; j < buissesList.size(); j++ ) {
                    BigDecimal total = BigDecimal.valueOf(0.00);
                    BigDecimal pretotal = BigDecimal.valueOf(0.00);

                    Tbreportmodelitem buissesItem = buissesList.get(j);
                    List<String> mediaids = new ArrayList<String>();
                    deptIdArray = buissesItem.getSids().split(",");
                    for( int k = 0; k < deptIdArray.length; k++ ) {
                        mediaids.add(deptIdArray[k]);
                    }
                    if( buissesItem.getSnames().equals("0") ) { //媒体
                        total = summaryReceivable(depids,mediaids,null,null,null,DateUtils.getBeginDayOfThisyear(),DateUtils.getThisyearCurrentTime());
                        pretotal = summaryReceivable(depids,mediaids,null,null,null,DateUtils.getBeginDayOfLastyear(),DateUtils.getLastyearCurrentTime());

                    }
                    if( buissesItem.getSnames().equals("1") ) { //叠次
                        total = summaryReceivable(depids,null,mediaids,null,null,DateUtils.getBeginDayOfThisyear(),DateUtils.getThisyearCurrentTime());
                        pretotal = summaryReceivable(depids,null,mediaids,null,null,DateUtils.getBeginDayOfLastyear(),DateUtils.getLastyearCurrentTime());
                    }
                    if( buissesItem.getSnames().equals("1") ) { //叠次版本
                        total = summaryReceivable(depids,null,null,mediaids,null,DateUtils.getBeginDayOfThisyear(),DateUtils.getThisyearCurrentTime());
                        pretotal = summaryReceivable(depids,null,null,mediaids,null,DateUtils.getBeginDayOfLastyear(),DateUtils.getLastyearCurrentTime());
                    }
                    if(buissesItem.getSheadername().equals("版面") ) {
                        item.setAdtotal(total);
                        item.setAdmonthtotal(pretotal);
                    }
                    if(buissesItem.getSheadername().equals("新媒体") ) {
                        item.setOptotal(total);
                        item.setOpmonthtotal(pretotal);
                    }
                    if(buissesItem.getSheadername().equals("南方+") ) {
                        item.setAmounttotal(total);
                        item.setAmountmonthtotal(pretotal);
                    }
                    if(buissesItem.getSheadername().equals("多元化") ) {
                        item.setDytotal(total);
                        item.setPredytotal(pretotal);
                    }

                }
            }
            adreceivedItemList.add(item);
        }
        adreceivedVO.setItemList(adreceivedItemList);
        return adreceivedVO;

    }

    @Override
    public XSSFWorkbook exportAreaIncomeReceivableExcel() {
        AdreceivedVO vo = exportAreaIncomeReceivable();
        // 输出EXCEL
        XSSFWorkbook wb = new XSSFWorkbook();
        //创建工作表
        Sheet sheet = wb.createSheet("区域应收统计表");
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

        XSSFCellStyle stylep = wb.createCellStyle();
        DataFormat format = wb.createDataFormat();
        stylep.setBorderTop(BorderStyle.MEDIUM);
        stylep.setBorderBottom(BorderStyle.MEDIUM);
        stylep.setBorderLeft(BorderStyle.MEDIUM);
        stylep.setBorderRight(BorderStyle.MEDIUM);
        stylep.setFont(font1);
        stylep.setAlignment(HorizontalAlignment.CENTER);//水平居中
        stylep.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        stylep.setDataFormat(format.getFormat("#,##0.00"));
        stylep.setDataFormat(format.getFormat("0.00%"));

        XSSFCellStyle style = wb.createCellStyle();
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setBorderRight(BorderStyle.MEDIUM);
        style.setFont(font1);
        style.setAlignment(HorizontalAlignment.CENTER);//水平居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        style.setDataFormat(format.getFormat("#,##0.00"));

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
        for (int i = 0; i < areaIncomeTitle.length; i++) {
            cell = titleRow.createCell(i);
            cell.setCellStyle(style1);
            if( i == 0 ) {
                sheet.setColumnWidth(i, columnwidth1);
            } else {
                sheet.setColumnWidth(i, columnwidth2);
            }
        }
        CellRangeAddress region = new CellRangeAddress(0, 0, 0, areaIncomeTitle.length-1);
        int cell0 = region.getFirstColumn();
        cell = titleRow.getCell(cell0);
        cell.setCellValue(vo.getTitle());
        cell.setCellStyle(style1) ;
        sheet.addMergedRegion(region);

        // 创建制表行
        Row tabRow = sheet.createRow(excelRow++);
        tabRow.setHeight(h2);
        for (int i = 0; i < areaIncomeTitle.length; i++) {
            cell = tabRow.createCell(i);
            cell.setCellStyle(style2);
        }
        region = new CellRangeAddress(1, 1, 0,2);
        cell0 = region.getFirstColumn();
        cell = tabRow.getCell(cell0);
        cell.setCellValue(vo.getCompany());
        sheet.addMergedRegion(region);
        cell = tabRow.getCell(areaIncomeTitle.length-1);  //单位
        cell.setCellValue("单位元");

        // 创建标头1
        Row headRow1 = sheet.createRow(excelRow++);
        Row headRow2 = sheet.createRow(excelRow++);
        Row headRow3 = sheet.createRow(excelRow++);
        headRow1.setHeight(h2);
        headRow2.setHeight(h2);
        headRow3.setHeight(h2);
        for (int i = 0; i < areaIncomeTitle.length; i++) {
            cell = headRow1.createCell(i);
            cell.setCellStyle(style3);
            cell = headRow2.createCell(i);
            cell.setCellStyle(style3);
            cell = headRow3.createCell(i);
            cell.setCellStyle(style3);
        }

        // 区域列合并
        region = new CellRangeAddress(2, 4, 0,0);
        cell0 = region.getFirstColumn();
        cell = headRow1.getCell(cell0);
        cell.setCellValue("区域");
        cell.setCellStyle(style3);
        sheet.addMergedRegion(region);

        // 日期列合并
        region = new CellRangeAddress(2, 2, 1,7);
        cell0 = region.getFirstColumn();
        cell = headRow1.getCell(cell0);
        cell.setCellValue(vo.getThisYearReportDate());
        cell.setCellStyle(style3);
        sheet.addMergedRegion(region);

        // 去年日期列合并
        region = new CellRangeAddress(2, 2, 8,14);
        cell0 = region.getFirstColumn();
        cell = headRow1.getCell(cell0);
        cell.setCellValue(vo.getLastYearReportDate());
        cell.setCellStyle(style3);
        sheet.addMergedRegion(region);

        // 明细列合并
        region = new CellRangeAddress(3, 3, 1,5);
        cell0 = region.getFirstColumn();
        cell = headRow2.getCell(cell0);
        cell.setCellValue("收入明细");
        cell.setCellStyle(style3);
        sheet.addMergedRegion(region);

        // 展馆列合并
        region = new CellRangeAddress(3, 3, 6,7);
        cell0 = region.getFirstColumn();
        cell = headRow2.getCell(cell0);
        cell.setCellValue("其中：展馆投放");
        cell.setCellStyle(style3);
        sheet.addMergedRegion(region);

        // 去年明细列合并
        region = new CellRangeAddress(3, 3, 8,12);
        cell0 = region.getFirstColumn();
        cell = headRow2.getCell(cell0);
        cell.setCellValue("收入明细");
        cell.setCellStyle(style3);
        sheet.addMergedRegion(region);

        // 去年展馆列合并
        region = new CellRangeAddress(3, 3, 13,14);
        cell0 = region.getFirstColumn();
        cell = headRow2.getCell(cell0);
        cell.setCellValue("其中：展馆投放");
        cell.setCellStyle(style3);
        sheet.addMergedRegion(region);

        for (int i = 0; i < areaIncomeTitle.length; i++) {
            cell = headRow3.createCell(i);
            if( i == 1 || i == 6 || i == 8 || i == 13 ) {
                cell.setCellValue("版面");
            }
            if( i == 2 || i == 9 ) {
                cell.setCellValue("新媒体");
            }
            if( i == 3 || i== 7 || i == 10 || i == 14 ) {
                cell.setCellValue("南方+");
            }
            if( i == 4 || i == 11) {
                cell.setCellValue("多元化");
            }
            if( i == 5 || i == 12) {
                cell.setCellValue("合计");
            }
            cell.setCellStyle(style3);
        }

        // 输出汇总项
        List<AdreceivedItem> adreceivedItemList = vo.getItemList();
        for (int i = 0; i < adreceivedItemList.size(); i++) {
            double sumtotal = 0.0;
            AdreceivedItem item = adreceivedItemList.get(i);
            Row itemRow = sheet.createRow(excelRow++);
            itemRow.setHeight(h2);
            for (int cellno = 0; cellno < areaIncomeTitle.length; cellno++) {
                cell = itemRow.createCell(cellno);
                cell.setCellStyle(style);
                if (cellno == 0) {
                    cell.setCellValue(item.getName());
                }
                if (cellno == 1) {  //版面
                    if( null != item.getAdtotal() ) {
                        cell.setCellValue(item.getAdtotal().doubleValue());
                    }
                }
                if (cellno == 2) {  //新媒体
                    if( null != item.getOptotal() ) {
                        cell.setCellValue(item.getOptotal().doubleValue());
                    }
                }
                if (cellno == 3) {  //南方+
                    if( null != item.getAdmonthtotal() ) {
                        cell.setCellValue(item.getAdmonthtotal().doubleValue());
                    }
                }
                if (cellno == 4) {  //多元化
                    if( null != item.getDytotal() ) {
                        cell.setCellValue(item.getDytotal().doubleValue());
                    }
                }
                if (cellno == 5) {  //合计
                     cell.setCellFormula("B" + String.valueOf(i + 6)  + "+" + "C" + String.valueOf(i + 6) + "+" + "E" + String.valueOf(i + 6));
                }
                if (cellno == 8) {  //去年版面
                    if( null != item.getAdmonthtotal() ) {
                        cell.setCellValue(item.getAdmonthtotal().doubleValue());
                    }
                }
                if (cellno == 9) {  //去年新媒体
                    if( null != item.getOpmonthtotal() ) {
                        cell.setCellValue(item.getOpmonthtotal().doubleValue());
                    }
                }
                if (cellno == 10) {  //去年南方+
                    if( null != item.getAmountmonthtotal() ) {
                        cell.setCellValue(item.getAmountmonthtotal().doubleValue());
                    }
                }
                if (cellno == 11) {  //去年多元化+
                    if( null != item.getPredytotal() ) {
                        cell.setCellValue(item.getPredytotal().doubleValue());
                    }
                }
                if (cellno == 12) {  //去年合计
                    cell.setCellFormula("I" + String.valueOf(i + 6)  + "+" + "J" + String.valueOf(i + 6) + "+"  + "J" + String.valueOf(i + 6));

                }


            }
        }
        return wb;
    }

    @Override
    public AdreceivedVO exportReceivableAnalysis(String mediaId,String dateTime) {
        AdreceivedVO  adreceivedVO = new AdreceivedVO();
        adreceivedVO.setReportDate("取数日期: " + DateUtils.transferDate2StringCn(new Date()));
        LambdaQueryWrapper<Tbadindustry> lqw = Wrappers.lambdaQuery();
        lqw.eq(Tbadindustry::getBuse,true);
        Date date =  DateUtils.transferString2Date(dateTime);
        Tbmedia media = tbmediaServiceI.getById(mediaId);
        if( null == media ) {
            return  adreceivedVO;
        }
        adreceivedVO.setTitle(media.getSname() + (DateUtils.getMonth(date)+1) + "月广告应收分析");
        String thisYearStratTime =  DateUtils.getBeginDayOfYear(String.valueOf(DateUtils.getYear(date)));
        String thisYearendTime = DateUtils.getDayMonthWithYear(DateUtils.getYear(date),DateUtils.getMonth(date));
        String lastYearStratTime =  DateUtils.getBeginDayOfYear(String.valueOf(DateUtils.getYear(date)-1));
        String lastYearendTime = DateUtils.getDayMonthWithYear(DateUtils.getYear(date)-1,DateUtils.getMonth(date));
        List<Tbadindustry> list =  tbadindustryMapper.selectList(lqw);
        List<AdreceivedItem> adreceivedItemList = new ArrayList<AdreceivedItem>();
        for ( int i = 0; i < list.size(); i++ ) {
            BigDecimal thisTotal = BigDecimal.valueOf(0.0);
            BigDecimal lastotal = BigDecimal.valueOf(0.0);
            AdreceivedItem item = new AdreceivedItem();
            Tbadindustry tbadindustry = list.get(i);
            item.setName(tbadindustry.getSname());
            thisTotal = summaryAnalysisReceivable(tbadindustry.getId().toString(),mediaId,thisYearStratTime,thisYearendTime);
            lastotal = summaryAnalysisReceivable(tbadindustry.getId().toString(),mediaId,lastYearStratTime,lastYearendTime);
            item.setAdtotal(thisTotal);
            item.setOptotal(lastotal);
            adreceivedItemList.add(item);
        }
        adreceivedVO.setItemList(adreceivedItemList);
        return adreceivedVO;
    }

    @Override
    public XSSFWorkbook exportReceivableAnalysisExcel(String mediaId, String dateTime) {
        AdreceivedVO vo = exportReceivableAnalysis(mediaId,dateTime);
        // 输出EXCEL
        XSSFWorkbook wb = new XSSFWorkbook();
        //创建工作表
        Sheet sheet = wb.createSheet("分析材料");
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

        XSSFCellStyle stylep = wb.createCellStyle();
        DataFormat format = wb.createDataFormat();
        stylep.setBorderTop(BorderStyle.MEDIUM);
        stylep.setBorderBottom(BorderStyle.MEDIUM);
        stylep.setBorderLeft(BorderStyle.MEDIUM);
        stylep.setBorderRight(BorderStyle.MEDIUM);
        stylep.setFont(font1);
        stylep.setAlignment(HorizontalAlignment.CENTER);//水平居中
        stylep.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        stylep.setDataFormat(format.getFormat("#,##0.00"));
        stylep.setDataFormat(format.getFormat("0.00%"));

        XSSFCellStyle style = wb.createCellStyle();
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setBorderRight(BorderStyle.MEDIUM);
        style.setFont(font1);
        style.setAlignment(HorizontalAlignment.CENTER);//水平居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        style.setDataFormat(format.getFormat("#,##0.00"));

        XSSFCellStyle style1 = wb.createCellStyle();
        style1.setFont(font);
        style1.setAlignment(HorizontalAlignment.CENTER);//水平居中
        style1.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中

        XSSFCellStyle style2 = wb.createCellStyle();
        style2.setFont(font1);
        style2.setAlignment(HorizontalAlignment.CENTER);//水平居中
        style2.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中

        XSSFCellStyle style3 = wb.createCellStyle();
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
        for (int i = 0; i < anlysisTitle.length; i++) {
            cell = titleRow.createCell(i);
            cell.setCellStyle(style1);
            if( i == 0 ) {
                sheet.setColumnWidth(i, columnwidth1);
            } else {
                sheet.setColumnWidth(i, columnwidth2);
            }
        }
        CellRangeAddress region = new CellRangeAddress(0, 0, 0, anlysisTitle.length-1);
        int cell0 = region.getFirstColumn();
        cell = titleRow.getCell(cell0);
        cell.setCellValue(vo.getTitle());
        cell.setCellStyle(style1) ;
        sheet.addMergedRegion(region);

        // 创建制表行
        Row tabRow = sheet.createRow(excelRow++);
        tabRow.setHeight(h2);
        for (int i = 0; i < anlysisTitle.length; i++) {
            cell = tabRow.createCell(i);
            cell.setCellStyle(style2);
        }
        region = new CellRangeAddress(1, 1, 0,2);
        cell0 = region.getFirstColumn();
        cell = tabRow.getCell(cell0);
        cell.setCellValue(vo.getReportDate());
        sheet.addMergedRegion(region);
        cell = tabRow.getCell(anlysisTitle.length-1);  //单位
        cell.setCellValue("单位（万元）");

        // 创建标头1
        Row headRow1 = sheet.createRow(excelRow++);
        headRow1.setHeight(h2);
        for (int i = 0; i < anlysisTitle.length; i++) {
            cell = headRow1.createCell(i);
            cell.setCellValue(anlysisTitle[i]);
            cell.setCellStyle(style3);
        }
        // 输出汇总项
        List<AdreceivedItem> adreceivedItemList = vo.getItemList();
        for (int i = 0; i < adreceivedItemList.size(); i++) {
            double sumtotal = 0.0;
            AdreceivedItem item = adreceivedItemList.get(i);
            Row itemRow = sheet.createRow(excelRow++);
            itemRow.setHeight(h2);
            for (int cellno = 0; cellno < anlysisTitle.length; cellno++) {
                cell = itemRow.createCell(cellno);
                cell.setCellStyle(style);
                if (cellno == 0) {
                    cell.setCellValue(item.getName());
                }
                if (cellno == 1) {  //今年累计
                    if( null != item.getAdtotal() ) {
                        cell.setCellValue(item.getAdtotal().doubleValue()/10000);
                    }
                }
                if (cellno == 2) {  //去年累计
                    if( null != item.getOptotal() ) {
                        cell.setCellValue(item.getOptotal().doubleValue()/10000);
                    }
                }
                if (cellno == 3) {  //变动金额
                    cell.setCellFormula("B" + String.valueOf(i + 4)  + "-" + "C" + String.valueOf(i + 4) );
                }
                if (cellno == 4) {  //变动幅度
                    cell.setCellFormula("D" + String.valueOf(i + 4) + "/" + "C" + String.valueOf(i + 4));
                    cell.setCellStyle(stylep);
                }
            }
        }
        return wb;
    }

    @Override
    public IPage<AdChangeVo> queryChangeOrder(String dateTime, int pageNum, int pageSize) {
        IPage<AdChangeVo> resulepage = new Page<>();
        long startRecord = pageNum<=1? 0: (pageNum-1)*pageSize;
        long endRecord = pageSize;
        Date date =  DateUtils.transferString2Date(dateTime);
        String thisYearStratTime =  DateUtils.getStartDayMonthWithYear(DateUtils.getYear(date),DateUtils.getMonth(date));
        String thisYearendTime = DateUtils.getDayMonthWithYear(DateUtils.getYear(date),DateUtils.getMonth(date));
        List<AdChangeVo> list = tworderapportiondetailMapper.queryChangeOredrItem(thisYearStratTime,thisYearendTime,startRecord,endRecord);
        long count = tworderapportiondetailMapper.getChangeOredrItemCount(thisYearStratTime,thisYearendTime);
        resulepage.setRecords(list);
        resulepage.setTotal(count);
        return  resulepage;
    }

    @Override
    public XSSFWorkbook exportChangeOrderExcel(String dateTime) {
        Date date =  DateUtils.transferString2Date(dateTime);
        String thisYearStratTime =  DateUtils.getStartDayMonthWithYear(DateUtils.getYear(date),DateUtils.getMonth(date));
        String thisYearendTime = DateUtils.getDayMonthWithYear(DateUtils.getYear(date),DateUtils.getMonth(date));
        List<AdChangeVo> list = tworderapportiondetailMapper.queryChangeOredrItem(thisYearStratTime,thisYearendTime,0L,10000L);
        // 输出EXCEL
        XSSFWorkbook wb = new XSSFWorkbook();
        //创建工作表
        Sheet sheet = wb.createSheet("调整记录");
        int excelRow = 0;
        Cell cell;
        short h1 = 600, h2 = 400;
        short columnwidth1 = 10 * 256;
        short columnwidth2 = 26 * 256;
        XSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short) 22);
        font.setBold(true);

        XSSFFont font1 = wb.createFont();
        font1.setFontHeightInPoints((short) 12);

        XSSFFont font2 = wb.createFont();
        font2.setFontHeightInPoints((short) 12);

        XSSFCellStyle stylep = wb.createCellStyle();
        DataFormat format = wb.createDataFormat();
        stylep.setBorderTop(BorderStyle.MEDIUM);
        stylep.setBorderBottom(BorderStyle.MEDIUM);
        stylep.setBorderLeft(BorderStyle.MEDIUM);
        stylep.setBorderRight(BorderStyle.MEDIUM);
        stylep.setFont(font1);
        stylep.setAlignment(HorizontalAlignment.CENTER);//水平居中
        stylep.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        stylep.setDataFormat(format.getFormat("#,##0.00"));
        stylep.setDataFormat(format.getFormat("0.00%"));

        XSSFCellStyle style = wb.createCellStyle();
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setBorderRight(BorderStyle.MEDIUM);
        style.setFont(font1);
        style.setAlignment(HorizontalAlignment.CENTER);//水平居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        style.setDataFormat(format.getFormat("#,##0.00"));

        XSSFCellStyle style1 = wb.createCellStyle();
        style1.setFont(font);
        style1.setAlignment(HorizontalAlignment.CENTER);//水平居中
        style1.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中

        XSSFCellStyle style2 = wb.createCellStyle();
        style2.setFont(font1);
        style2.setAlignment(HorizontalAlignment.CENTER);//水平居中
        style2.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中

        XSSFCellStyle style3 = wb.createCellStyle();
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
        for (int i = 0; i < changeOrderTitle.length; i++) {
            cell = titleRow.createCell(i);
            cell.setCellStyle(style3);
            if( i == 0 ) {
                sheet.setColumnWidth(i, columnwidth1);
            } else {
                sheet.setColumnWidth(i, columnwidth2);
            }
        }
        CellRangeAddress region = new CellRangeAddress(0, 0, 0, changeOrderTitle.length-1);
        int cell0 = region.getFirstColumn();
        cell = titleRow.getCell(cell0);
        cell.setCellValue("华光系统广告数据调整记录表");
        cell.setCellStyle(style1) ;
        sheet.addMergedRegion(region);

        // 创建制表行
        Row tabRow = sheet.createRow(excelRow++);
        tabRow.setHeight(h2);
        for (int i = 0; i < changeOrderTitle.length; i++) {
            cell = tabRow.createCell(i);
            cell.setCellStyle(style2);
        }
        region = new CellRangeAddress(1, 1, 0,2);
        cell0 = region.getFirstColumn();
        cell = tabRow.getCell(cell0);
        cell.setCellValue("制表时间：" + DateUtils.transferDate2StringCn(date));
        sheet.addMergedRegion(region);
        // 创建标头1
        Row headRow1 = sheet.createRow(excelRow++);
        headRow1.setHeight(h2);
        for (int i = 0; i < changeOrderTitle.length; i++) {
            cell = headRow1.createCell(i);
            cell.setCellValue(changeOrderTitle[i]);
            cell.setCellStyle(style3);
        }

        for (int i = 0; i < list.size(); i++) {
            double sumtotal = 0.0;
            AdChangeVo item = list.get(i);
            Row itemRow = sheet.createRow(excelRow++);
            itemRow.setHeight(h2);
            for (int cellno = 0; cellno < changeOrderTitle.length; cellno++) {
                cell = itemRow.createCell(cellno);
                cell.setCellStyle(style3);
                if (cellno == 0) {
                    cell.setCellValue(i+1);
                }
                if (cellno == 1) {  //登记时间
                    if( null != item.getCreatetime() ) {
                        cell.setCellValue(DateUtils.transferDate2String(item.getCreatetime()));
                    }
                }
                if (cellno == 2) {  //认刊号
                    cell.setCellValue(item.getScontractnum());
                }
                if (cellno == 3) {  //代理公司
                    cell.setCellValue(item.getAgencyname());
                }
                if (cellno == 4) {  //客户名称
                    cell.setCellValue(item.getCustomername());
                }
                if (cellno == 5) {  //原系统见报日
                    cell.setCellValue(DateUtils.transferDate2String(item.getPrestartdate()));
                }
                if (cellno == 6) {  //原系统应收金额
                    if( null != item.getAmountreceivable() ) {
                        cell.setCellValue(item.getAmountreceivable().doubleValue());
                        cell.setCellStyle(style);
                    }
                }
                if (cellno == 7) {  //变动金额
                    if( null != item.getAmountreceivable() ) {
                        cell.setCellValue(item.getAmountreceivable().doubleValue());
                        cell.setCellStyle(style);
                    }
                }
                if (cellno == 8) {  //业务员
                    cell.setCellValue(item.getEmployname());
                }
                if (cellno == 9) {  //叠次
                    cell.setCellValue(item.getFoldname());
                }
                if (cellno == 10) {  //经营主体
                    cell.setCellValue(item.getBusinessentityname());
                }
                if (cellno == 11) {  //调整要求
                    if( item.getIbooktype() == 3 )
                        cell.setCellValue("补刊");
                    else
                        cell.setCellValue("改刊");
                }
                if (cellno == 12) {  //修改后应收金额
                    if( null != item.getChangeAmountreceivable() ) {
                        cell.setCellValue(item.getChangeAmountreceivable().doubleValue());
                        cell.setCellStyle(style);
                    }
                }
                if (cellno == 13) {  //修改后发布时间
                    cell.setCellValue(DateUtils.transferDate2String(item.getPrestartdate()));
                }
                if (cellno == 14) {  //备注
                    cell.setCellValue(item.getSremark());
                }

            }
        }

        return wb;
    }

    @Override
    public IPage<SynQueryResultVO> synQuery(SynQueryVO vo, int pageNum, int pageSize) {
        IPage<SynQueryResultVO> resulepage = new Page<>();
        long startRecord = pageNum<=1? 0: (pageNum-1)*pageSize;
        long endRecord = pageSize;
        List<SynQueryResultVO> list = tworderapportiondetailMapper.querySyn(vo.getSynQueryItemList(),String.valueOf(vo.getRange()),vo.getEnSortName(),vo.getSorttype(),String.valueOf(vo.getFilterRepeat()),startRecord,endRecord);
        long count = tworderapportiondetailMapper.getQuerySynCount(vo.getSynQueryItemList(),String.valueOf(vo.getRange()),String.valueOf(vo.getFilterRepeat()));
        resulepage.setRecords(list);
        resulepage.setTotal(count);
        return  resulepage;
    }

    @Override
    public List<AdCustomerResourceVO> customerResourceQuery(String level, String startTime, String endTime) {
        List<AdCustomerResourceVO> adCustomerResourceVOList = new ArrayList<AdCustomerResourceVO>();
        int depth = Integer.valueOf(level);
        LambdaQueryWrapper<Tbadfrom> lqw = Wrappers.lambdaQuery();
        lqw.eq(Tbadfrom::getBuse,true);
        lqw.eq(Tbadfrom::getIdepth,depth);
        lqw.orderByAsc(Tbadfrom::getIsort);
        List<Tbadfrom> list =  tbadfromMapper.selectList(lqw);
        if( null == list && list.size() <= 0 ) {
            return adCustomerResourceVOList;
        }
        for( int i = 0; i < list.size(); i++ ) {
            AdCustomerResourceVO vo = new AdCustomerResourceVO();
            Tbadfrom  tbadfrom =  list.get(i);
            List<Long> longList = tbadfromServiceI.getParentAdFromId(tbadfrom.getId());
            String tempName = "";
            for( int j = 0; j < longList.size(); j++ ) {
                tempName += tbadfromServiceI.getById(longList.get(j)).getSname();
                tempName += "/";
            }
            tempName += tbadfrom.getSname();
            vo.setAreaname(tempName);
            vo.setId(tbadfrom.getId());

            // 获取子节点
            List<Long> childList = tbadfromServiceI.getChildAdFromId(tbadfrom.getId());
            childList.add(tbadfrom.getId());
            tempName = "";
            for( int j = 0; j < childList.size(); j++ ) {
                tempName += tbadfromServiceI.getById(childList.get(j)).getSname();
                tempName += "/";
            }
            vo.setChildname(tempName);
            // 按年度汇总
            //Integer nStartYear = Integer.valueOf(startTime);
            //Integer nEndYear = Integer.valueOf(endTime);
            List<AdCustomerYearItem>  adCustomerYearItemList = new ArrayList<AdCustomerYearItem>();
            long count = tbadfromMapper.summaryCustomer(childList,startTime,endTime);
            /*for (int year = nStartYear; year <= nEndYear; year++) {
                AdCustomerYearItem adCustomerYearItem = new AdCustomerYearItem();
                String thisStartTime = DateUtils.getBeginDayOfYear(String.valueOf(year));
                String thisEndTime = DateUtils.getEndDayOfYear(String.valueOf(year));
                long count = tbadfromMapper.summaryCustomer(childList,thisStartTime,thisEndTime);
                total += count;
                adCustomerYearItem.setYear(String.valueOf(year));
                adCustomerYearItem.setCustomercount(count);
                adCustomerYearItemList.add(adCustomerYearItem);
            }*/
            vo.setCustomerYearItemList(adCustomerYearItemList);
            vo.setTotalcount(count);
            adCustomerResourceVOList.add(vo);
        }
        return adCustomerResourceVOList;
    }

    @Override
    public List<Twcustomer> getCustomerDetail(Long id, String startTime, String endTime) {
        /*LambdaQueryWrapper<Twcustomer> lqw = Wrappers.lambdaQuery();
        lqw.eq(Twcustomer::getBuse,true);
        lqw.eq(Twcustomer::getAdfromid,id);
        lqw.between(Twcustomer::getDcreatetime,startTime,endTime);
        List<Twcustomer> list =  twcustomerMapper.selectList(lqw);*/
        // 获取子节点
        List<Long> childList = tbadfromServiceI.getChildAdFromId(id);
        childList.add(id);
        List<Twcustomer> list = twcustomerMapper.getCustomerList(childList,startTime,endTime);
        return list;
    }

    public BigDecimal summaryReceivable(List<String> ids, List<String> mediaids, List<String> floadids, List<String> foldareaverids, String companyId, String startTime, String endTime) {
        AdSummaryVO vo = tworderapportiondetailMapper.summaryReceivable(ids,null,mediaids,floadids,foldareaverids,startTime,endTime);
        if( null != vo ) {
            return vo.getAmountreceivable();
        }
        return BigDecimal.valueOf(0.0);
    }

    public BigDecimal summaryAnalysisReceivable(String industryId, String mediaId,String startTime, String endTime) {
        AdSummaryVO vo = tworderapportiondetailMapper.summaryIndustyReceivable(industryId,mediaId,startTime,endTime);
        if( null != vo ) {
            return vo.getAmountreceivable();
        }
        return BigDecimal.valueOf(0.0);
    }

}


