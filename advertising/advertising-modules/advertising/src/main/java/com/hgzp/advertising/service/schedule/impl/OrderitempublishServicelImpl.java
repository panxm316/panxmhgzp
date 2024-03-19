package com.hgzp.advertising.service.schedule.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.pagemodel.business.dto.TaskQuotaDTO;
import com.hgzp.advertising.pagemodel.schedule.dto.ArrangeOrderItemDTO;
import com.hgzp.advertising.pagemodel.schedule.dto.CheckOrderItemDTO;
import com.hgzp.advertising.pagemodel.schedule.dto.ModifyOrderItemStatusDTO;
import com.hgzp.advertising.pagemodel.schedule.vo.FoldPageplanReq;
import com.hgzp.advertising.pagemodel.schedule.vo.PendPublishOrderVo;
import com.hgzp.advertising.service.media.TbfoldServiceI;
import com.hgzp.advertising.service.media.TbfoldareaverServiceI;
import com.hgzp.advertising.service.media.TbmediaServiceI;
import com.hgzp.advertising.service.media.TbmediapublicnumServiceI;
import com.hgzp.advertising.service.schedule.OrderitempublishServicel;
import com.hgzp.advertising.service.schedule.TbfoldpageplanServiceI;
import com.hgzp.advertising.service.system.TwparameterServiceI;
import com.hgzp.core.emnus.ResultDefines;
import com.hgzp.core.model.*;
import com.hgzp.core.page.DataCombo;
import com.hgzp.core.page.Json;
import com.hgzp.mapper.ad.TworderitemMapper;
import com.hgzp.mapper.business.TwtasksMapper;
import com.hgzp.mapper.schedule.TworderitemarrangeMapper;
import com.hgzp.pagemodel.schedule.OrderPublishQueryResultVo;
import com.hgzp.utils.WebUtil;
import com.hgzp.utils.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * OrderitempublishServicelImpl
 * 创建人：lhl
 * 类描述：广告刊发服务实现类
 * 创建日期：2023/12/14 13:14
 */
@Service
public class OrderitempublishServicelImpl extends ServiceImpl<TworderitemMapper, Tworderitem> implements OrderitempublishServicel {
    @Autowired
    TworderitemMapper  tworderitemMapper;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;
    @Autowired
    TbmediaServiceI tbmediaServiceI;
    @Autowired
    private TworderitemarrangeMapper orderitemarrangeMapper;
    @Autowired
    private TbmediapublicnumServiceI mediapublicnumServiceI;
    @Autowired
    TbfoldareaverServiceI  tbfoldareaverServiceI;
    @Autowired
    TbfoldServiceI tbfoldServiceI;
    @Autowired
    TwparameterServiceI  twparameterServiceI;
    @Autowired
    private TbfoldpageplanServiceI foldpageplanServiceI;


    private String  mediaKey = "needurlofmedia";




    private String hadeTitle[] = {"合同号", "核查状态", "核查报告", "直接客户", "代理公司", "内容生产方", "广告行业", "应收金额", "已收金额", "欠款金额", "刊发日期", "明细标题"};

    @Override
    public IPage<OrderPublishQueryResultVo> getPendPublishOrderItemList(String mediaId, String foldId, String areaverId, String stratTime, String endTime,int pageNum,int pageSize,String publishstatus) {
        IPage<OrderPublishQueryResultVo> resulepage = new Page<>();
        int startRecord = pageNum<=1? 0: (pageNum-1)*pageSize;
        int endRecord = pageSize;
        List<OrderPublishQueryResultVo> list = tworderitemMapper.queryOredrPublishList(Long.parseLong(mediaId), foldId, areaverId, stratTime, endTime, startRecord, endRecord, publishstatus);
        if( null != list && list.size() > 0 ) {
            for( int i = 0; i < list.size(); i++ ) {
                OrderPublishQueryResultVo vo = list.get(i);
                List<Tworderitemarrange> lstworderitemarrange =
                        orderitemarrangeMapper.selectList(Wrappers.<Tworderitemarrange>lambdaQuery()
                                .eq(Tworderitemarrange::getOrderitemid,vo.getId())
                        );
                //获取版数
                if( lstworderitemarrange.size() > 0 ) {
                    if( vo.getMediatypekey().equals("paper") ) {
                        FoldPageplanReq foldPageplanReq = new FoldPageplanReq();
                        Tworderitemarrange item = lstworderitemarrange.get(0);
                        foldPageplanReq.setMediaid(item.getMediaid());
                        foldPageplanReq.setFoldid(item.getFoldid());
                        foldPageplanReq.setMediatypekey(item.getMediatypekey());
                        foldPageplanReq.setFoldareaverid(item.getFoldareaverid());
                        foldPageplanReq.setPublishdate(item.getDpublishdate());
                        List<DataCombo> lsPageNum = foldpageplanServiceI.getFoldPagePlanePageNum(foldPageplanReq);
                        vo.setPageNumList(lsPageNum);
                    } else if( vo.getMediatypekey().equals("app") || vo.getMediatypekey().equals("multi")) {
                        Tworderitemarrange item = lstworderitemarrange.get(0);
                        List<DataCombo> lsPageNum = tbfoldareaverServiceI.getFoldAreaverCombo(String.valueOf(item.getFoldid()));
                        vo.setPageNumList(lsPageNum);
                    }

                } else {
                    if( vo.getMediatypekey().equals("paper") ) {
                        FoldPageplanReq foldPageplanReq = new FoldPageplanReq();
                        foldPageplanReq.setMediaid(vo.getMediaid());
                        foldPageplanReq.setFoldid(vo.getFoldid());
                        foldPageplanReq.setMediatypekey(vo.getMediatypekey());
                        foldPageplanReq.setFoldareaverid(vo.getFoldareaverid());
                        foldPageplanReq.setPublishdate(vo.getPrestartdate());
                        List<DataCombo> lsPageNum = foldpageplanServiceI.getFoldPagePlanePageNum(foldPageplanReq);
                        vo.setPageNumList(lsPageNum);
                    } else if( vo.getMediatypekey().equals("app") || vo.getMediatypekey().equals("multi")) {
                        List<DataCombo> lsPageNum = tbfoldareaverServiceI.getFoldAreaverCombo(String.valueOf(vo.getFoldid()));
                        vo.setPageNumList(lsPageNum);
                    }
                }
                list.set(i,vo);
            }
        }
        long count = tworderitemMapper.queryOredrPublishCount(Long.parseLong(mediaId),foldId,areaverId,stratTime,endTime,publishstatus);
        resulepage.setRecords(list);
        resulepage.setTotal(count);
        return  resulepage;
    }

    @Override
    public Json modifyOrderItemStatus(ModifyOrderItemStatusDTO modifyOrderItemStatusDTO) throws Exception {
        innerInterceptor.recoredLog();
        String sMessge = "刊发状态更新成功\n";
        Tworderitem tworderitem = getById(modifyOrderItemStatusDTO.getId());
        if( null == tworderitem ) {
           sMessge = String.format("刊发状态更新失败,订单记录(ID:%d)不存在\n",modifyOrderItemStatusDTO.getId());
           return Json.success(sMessge);
        }
        tworderitem.setIpublishstatus(modifyOrderItemStatusDTO.getIpublishstatus());
        tworderitemMapper.updateById(tworderitem);
        return Json.success(sMessge);
    }

    @Override
    public Json checkOrderItem(CheckOrderItemDTO checkOrderItemDTO) throws Exception {
        innerInterceptor.recoredLog();
        String sMessge = "订单核查成功\n";
        Tworderitem tworderitem = getById(checkOrderItemDTO.getId());
        if( null == tworderitem ) {
            sMessge = String.format("订单核查失败,订单记录(ID:%d)不存在\n",checkOrderItemDTO.getId());
            return Json.success(sMessge);
        }
        tworderitem.setIpublishcheckstatus(checkOrderItemDTO.getIpublishcheckstatus());
        tworderitem.setSpublishcheckcontent(checkOrderItemDTO.getSpublishcheckcontent());
        tworderitemMapper.updateById(tworderitem);
        return Json.success(sMessge);
    }

    @Override
    public IPage<OrderPublishQueryResultVo> getCheckOrderList(String mediaId, String stratTime, String endTime, int pageNum, int pageSize) {
        IPage<OrderPublishQueryResultVo> resulepage = new Page<>();
        int startRecord = pageNum<=1? 0: (pageNum-1)*pageSize;
        int endRecord = pageSize;
        List<OrderPublishQueryResultVo> list =  tworderitemMapper.queryCheckOredrList(Long.parseLong(mediaId),stratTime,endTime,startRecord,endRecord);
        resulepage.setRecords(list);
        resulepage.setTotal(tworderitemMapper.queryCheckOredrListCount(Long.parseLong(mediaId),stratTime,endTime));
        return  resulepage;
    }

    @Override
    public XSSFWorkbook exportCheckOrderExcel(String mediaId, String stratTime, String endTime, int pageNum, int pageSize) {
        int startRecord = pageNum<=1? 0: (pageNum-1)*pageSize;
        int endRecord = pageSize;
        List<OrderPublishQueryResultVo> list =  tworderitemMapper.queryCheckOredrList(Long.parseLong(mediaId),stratTime,endTime,startRecord,endRecord);
        Tbmedia tbmedia = tbmediaServiceI.getById(mediaId);
        int allcolumn = 12;
        int excelRow = 0;
        Cell cell;
            // 输出EXCEL
            XSSFWorkbook wb = new XSSFWorkbook();
            //创建工作表
            Sheet sheet = wb.createSheet(tbmedia.getSname() + "核查报告单");
            XSSFFont font = wb.createFont();
            font.setFontHeightInPoints((short) 12);
            XSSFCellStyle style = wb.createCellStyle();
            style.setBorderTop(BorderStyle.MEDIUM);
            style.setBorderBottom(BorderStyle.MEDIUM);
            style.setBorderLeft(BorderStyle.MEDIUM);
            style.setBorderRight(BorderStyle.MEDIUM);
            style.setFont(font);
            style.setAlignment(HorizontalAlignment.CENTER);//水平居中
            style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
            int startrow = 0, endrow = 0, startcol = 0, endcol = 11;
            Row titleRow = sheet.createRow(excelRow++);
            short h1 = 1200, h2 = 1000;
            short columnwidth = 3000;
            titleRow.setHeight(h1);
            for (int i = 0; i < hadeTitle.length; i++) {
                cell = titleRow.createCell(i);
                cell.setCellStyle(style);
            }
            CellRangeAddress region = new CellRangeAddress(startrow, endrow, startcol, endcol);
            int cell0 = region.getFirstColumn();
            cell = titleRow.getCell(cell0);
            cell.setCellValue(tbmedia.getSname() + "核查报告单");
            sheet.addMergedRegion(region);
            for (int i = 0; i < hadeTitle.length; i++) {
                if( i == 2 )
                    sheet.setColumnWidth(2, 60 * 256);
                else
                    sheet.setColumnWidth(i, 30 * 256);
            }
            // 创建条目
            Row itemRow = sheet.createRow(excelRow++);
            itemRow.setHeight(h2);
            for (int i = 0; i < hadeTitle.length; i++) {
                cell = itemRow.createCell(i);
                cell.setCellStyle(style);
                cell.setCellValue(hadeTitle[i]);
            }
            // 循环
            for (int i = 0; i < list.size(); i++) {
                OrderPublishQueryResultVo vo = list.get(i);
                itemRow = sheet.createRow(excelRow++);
                itemRow.setHeight(h2);
                for (int cellno = 0; cellno < hadeTitle.length; cellno++) {
                    cell = itemRow.createCell(cellno);
                    cell.setCellStyle(style);
                    if (cellno == 0)
                        cell.setCellValue(vo.getScontractnum());
                    if (cellno == 1) {
                        if (vo.getIpublishcheckstatus() == 1)
                            cell.setCellValue("未刊发");
                        else
                            cell.setCellValue("刊发错误");
                    }
                    if (cellno == 2)
                        cell.setCellValue(vo.getSpublishcheckcontent());
                    if (cellno == 3)
                        cell.setCellValue(vo.getCustomername());
                    if (cellno == 4)
                        cell.setCellValue(vo.getAgencyname());
                    if (cellno == 5)
                        cell.setCellValue(vo.getAgentname());
                    if (cellno == 6)
                        cell.setCellValue(vo.getAdindustryname());
                    if (cellno == 7)
                        cell.setCellValue(vo.getAmountreceivable().toString());
                    if (cellno == 8)
                        cell.setCellValue(vo.getAmountreceived().toString());
                    if (cellno == 9)
                        cell.setCellValue(vo.getAmountarrearage().toString());
                    if (cellno == 10) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String formattedDate = sdf.format(vo.getPrestartdate());
                        cell.setCellValue(formattedDate);
                    }
                    if (cellno == 11)
                        cell.setCellValue(vo.getSadtitle());
                }
        }
        return wb;
    }

    @Override
    public String getExcelFileName() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(date);
        return String.format("checkreport-%s.xls",formattedDate);
    }

    @Override
    public Json arrangeOrderItem(ArrangeOrderItemDTO arrangeOrderItemDTO) throws Exception {
        // 获取配置参数
        //TwparameterServiceI.getParameterByKey(sKey)

        LoginUser user = WebUtil.getLoginUser();
        String sMessge = "";
        Tworderitem tworderitem = tworderitemMapper.selectById(arrangeOrderItemDTO.getId());
        if( null == tworderitem ) {
            sMessge = "待安排的广告订单不存在\n";
            return Json.success(sMessge);
        }
        // 查询安排表
        LambdaQueryWrapper<Tworderitemarrange> lqw = Wrappers.lambdaQuery();
        lqw.eq(null != tworderitem.getId(), Tworderitemarrange::getOrderitemid,tworderitem.getId());
        List<Tworderitemarrange>  list = orderitemarrangeMapper.selectList(lqw);
        if( null == list || list.size() <= 0 ) {
            Tworderitemarrange tworderitemarrange = new Tworderitemarrange();
            tworderitemarrange.setOrderid(tworderitem.getOrderid());
            tworderitemarrange.setCreateempid(user.getUserid());
            tworderitemarrange.setCreateempname(user.getUsername());
            tworderitemarrange.setDcreatetime(DateUtil.date());
            tworderitemarrange.setOrderitemid(Long.valueOf(tworderitem.getOrderid()));
            tworderitemarrange.setMediatypekey(tworderitem.getMediatypekey());
            tworderitemarrange.setMediatypename(tworderitem.getMediatypename());
            tworderitemarrange.setMediaid(tworderitem.getMediaid());
            tworderitemarrange.setMedianame(tworderitem.getMedianame());
            tworderitemarrange.setDpublishdate(tworderitem.getPrestartdate());
            if( null != tworderitem.getFoldid() ) {
                tworderitemarrange.setFoldid(tworderitem.getFoldid());
                tworderitemarrange.setFoldname(tworderitem.getFoldname());
            }
            if( null != tworderitem.getFoldpageplanid() ) {
                tworderitemarrange.setFoldpageplanid(tworderitem.getFoldpageplanid());
            }
            // 安排报刊订单
            if( arrangeOrderItemDTO.getMediaType().equals("0") ) {
                if( null != tworderitem.getFoldareaverid() ) {
                    tworderitemarrange.setFoldareaverid(tworderitem.getFoldareaverid());
                    tworderitemarrange.setFoldareavername(tworderitem.getFoldareavername());
                }
                tworderitemarrange.setFoldpageplanid(Long.valueOf(arrangeOrderItemDTO.getArrangeData()));
                Tbfoldpageplan tbfoldpageplan =  foldpageplanServiceI.getById(Long.valueOf(arrangeOrderItemDTO.getArrangeData()));
                if( null != tbfoldpageplan ) {
                    tworderitemarrange.setFoldpageplanname(tbfoldpageplan.getPagenum());
                }
            }
            // 安排微信、微博订单
            if( arrangeOrderItemDTO.getMediaType().equals("2") ) {
                if( null != tworderitem.getFoldid() ) {
                    tworderitemarrange.setFoldid(tworderitem.getFoldid());
                    tworderitemarrange.setFoldareavername(tworderitem.getFoldname());
                }
            }

            // 安排南方+订单
            if( arrangeOrderItemDTO.getMediaType().equals("1") || arrangeOrderItemDTO.getMediaType().equals("3")) {
                tworderitemarrange.setFoldareaverid(Long.valueOf(arrangeOrderItemDTO.getArrangeData()));
                Tbfoldareaver tbfoldareaver = tbfoldareaverServiceI.getById(Long.valueOf(arrangeOrderItemDTO.getArrangeData()));
                if( null != tbfoldareaver ) {
                    tworderitemarrange.setFoldareavername(tbfoldareaver.getSname());
                    Tbfold tbfold = tbfoldServiceI.getById(tbfoldareaver.getFoldid());
                    if( null != tbfold ) {
                        tworderitemarrange.setFoldid(tbfold.getId());
                        tworderitemarrange.setFoldname(tbfold.getSname());
                    }

                } else {
                    tworderitemarrange.setFoldareavername("");
                }
                String value = twparameterServiceI.getParameterByKey(mediaKey);
                if( null != tworderitem.getMediaid() ) {
                    if( value.indexOf(tworderitem.getMedianame()) >= 0) {
                        tworderitemarrange.setSpublishedurl(arrangeOrderItemDTO.getAdURL());
                    }
                }
            }
            tworderitemarrange.setAddisplayformid(tworderitem.getAddisplayformid());
            tworderitemarrange.setAddisplayformname(tworderitem.getAddisplayformname());
            tworderitemarrange.setPagesortid(tworderitem.getPagesortid());
            tworderitemarrange.setPagesortname(tworderitem.getPagesortname());
            tworderitemarrange.setAdcolorid(tworderitem.getAdcolorid());
            tworderitemarrange.setAdcolorname(tworderitem.getAdcolorname());
            tworderitemarrange.setAdspecid(tworderitem.getAdspecid());
            tworderitemarrange.setAdspecname(tworderitem.getAdspecname());
            tworderitemarrange.setNwidth(tworderitem.getNwidth());
            tworderitemarrange.setNheight(tworderitem.getNheight());
            tworderitemarrange.setNleftx(0);
            tworderitemarrange.setNtopy(0);
            tworderitemarrange.setSadtitle(tworderitem.getSadtitle());
            tworderitemarrange.setSadcontent(tworderitem.getSadcontent());
            tworderitemarrange.setSremark(arrangeOrderItemDTO.getRemark());
            tworderitemarrange.setVersion(0L);
            //查询刊期
            if ("paper".equals(tworderitem.getMediatypekey())) {
                String sPublishNo = mediapublicnumServiceI.getMediaPublishNO(tworderitem.getMediaid(),
                        tworderitem.getPrestartdate());
                if (StrUtil.isNotBlank(sPublishNo)) {
                    tworderitemarrange.setPublishnum(Integer.valueOf(sPublishNo));
                }
            }
            innerInterceptor.recoredLog();
            orderitemarrangeMapper.insert(tworderitemarrange);
            // 修改订单状态
            ModifyOrderItemStatusDTO  modifyOrderItemStatusDTO = new ModifyOrderItemStatusDTO();
            modifyOrderItemStatusDTO.setId(tworderitem.getId());
            modifyOrderItemStatusDTO.setIpublishstatus(5);
            modifyOrderItemStatus(modifyOrderItemStatusDTO);
        } else {
            Tworderitemarrange tworderitemarrange = list.get(0);
            if( null == tworderitemarrange ) {
                sMessge = "数据错误\n";
                return Json.success(sMessge);
            }
            // 安排报刊订单
            if( arrangeOrderItemDTO.getMediaType().equals("0") ) {
                tworderitemarrange.setFoldpageplanid(Long.valueOf(arrangeOrderItemDTO.getArrangeData()));
                Tbfoldpageplan tbfoldpageplan =  foldpageplanServiceI.getById(Long.valueOf(arrangeOrderItemDTO.getArrangeData()));
                if( null != tbfoldpageplan ) {
                    tworderitemarrange.setFoldpageplanname(tbfoldpageplan.getPagenum());
                }
            }
            // 安排微信、微博订单
            if( arrangeOrderItemDTO.getMediaType().equals("2") ) {
                if( null != tworderitem.getFoldid() ) {
                    tworderitemarrange.setFoldid(tworderitem.getFoldid());
                    tworderitemarrange.setFoldareavername(tworderitem.getFoldname());
                }
            }
            // 安排南方+订单、多元化订单
            if( arrangeOrderItemDTO.getMediaType().equals("1") || arrangeOrderItemDTO.getMediaType().equals("3")) {
                tworderitemarrange.setFoldareaverid(Long.valueOf(arrangeOrderItemDTO.getArrangeData()));
                Tbfoldareaver tbfoldareaver = tbfoldareaverServiceI.getById(Long.valueOf(arrangeOrderItemDTO.getArrangeData()));
                if( null != tbfoldareaver ) {
                    tworderitemarrange.setFoldareavername(tbfoldareaver.getSname());
                    Tbfold tbfold = tbfoldServiceI.getById(tbfoldareaver.getFoldid());
                    if( null != tbfold ) {
                        tworderitemarrange.setFoldid(tbfold.getId());
                        tworderitemarrange.setFoldname(tbfold.getSname());
                    }

                } else {
                    tworderitemarrange.setFoldareavername("");
                }
                String value = twparameterServiceI.getParameterByKey(mediaKey);
                if( null != tworderitem.getMediaid() ) {
                    if( value.indexOf(tworderitem.getMedianame()) >= 0) {
                        tworderitemarrange.setSpublishedurl(arrangeOrderItemDTO.getAdURL());
                    }
                }

            }
            tworderitemarrange.setSremark(arrangeOrderItemDTO.getRemark());
            innerInterceptor.recoredLog();
            orderitemarrangeMapper.updateById(tworderitemarrange);
            // 修改订单状态
            ModifyOrderItemStatusDTO  modifyOrderItemStatusDTO = new ModifyOrderItemStatusDTO();
            modifyOrderItemStatusDTO.setId(tworderitem.getId());
            modifyOrderItemStatusDTO.setIpublishstatus(5);
            modifyOrderItemStatus(modifyOrderItemStatusDTO);
        }
        sMessge = "广告订单安排成功\n";
        return Json.success(sMessge);
    }

    @Override
    public String getAdUrlParameter(String mediaId) {
        String value = twparameterServiceI.getParameterByKey(mediaKey);
        if( null == mediaId ) {
            return "0";
        }
        Tbmedia tbmedia = tbmediaServiceI.getById(mediaId);
        if( null == tbmedia ) {
            return "0";
        }
        if( value.indexOf(tbmedia.getSname()) >= 0) {
            return "1";
        }
        return "0";
    }
}


