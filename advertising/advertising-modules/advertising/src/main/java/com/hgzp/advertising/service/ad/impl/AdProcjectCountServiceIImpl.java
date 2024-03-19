package com.hgzp.advertising.service.ad.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.pagemodel.ad.vo.AdProjectContractVO;
import com.hgzp.advertising.pagemodel.ad.vo.AdProjectCountOrderDetailsVO;
import com.hgzp.advertising.pagemodel.ad.vo.TworderitemVO;
import com.hgzp.advertising.service.ad.AdProcjectCountServiceI;
import com.hgzp.core.model.Twadproject;
import com.hgzp.core.model.Tworder;
import com.hgzp.core.model.Tworderitem;
import com.hgzp.core.model.Twtasks;
import com.hgzp.mapper.ad.TwadprojectMapper;
import com.hgzp.mapper.ad.TworderMapper;
import com.hgzp.mapper.ad.TworderitemMapper;
import com.hgzp.mapper.finance.TworderapportiondetailMapper;
import com.hgzp.pagemodel.ad.AdOrderItemCostVO;
import com.hgzp.pagemodel.ad.AdProjectCountVO;
import com.hgzp.pagemodel.ad.AdSummaryAllVO;
import com.hgzp.pagemodel.ad.AdSummaryVO;
import com.hgzp.pagemodel.business.TaskCountVo;
import com.hgzp.pagemodel.schedule.OrderPublishQueryResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * AdProcjectCountServiceIImpl
 * 创建人：lhl
 * 类描述：广告项目汇总实现类
 * 创建日期：2024/1/5 8:40
 */
@Service
public class AdProcjectCountServiceIImpl implements AdProcjectCountServiceI {
    @Autowired
    TwadprojectMapper  twadprojectMapper;
    @Autowired
    TworderitemMapper tworderitemMapper;
    @Autowired
    TworderMapper  tworderMapper;
    @Autowired
    TworderapportiondetailMapper tworderapportiondetailMapper;


    @Override
    public IPage<AdProjectCountVO> getAdProjectCountList(String stratTime, String endTime, String adProjectId, int pageNum, int pageSize, String publishstatus,String queryKey,String projectEnd) {
        IPage<AdProjectCountVO> resulepage = new Page<>();
        int startRecord = pageNum<=1? 0: (pageNum-1)*pageSize;
        int endRecord = pageSize;
        List<Long> ids = new ArrayList<Long>();
        if( null != adProjectId && !adProjectId.isEmpty() ) {
            ids.add(Long.valueOf(adProjectId));
        } else {
            LambdaQueryWrapper<Twadproject> lqw = Wrappers.lambdaQuery();
            lqw.ge(Twadproject::getDenddate,stratTime);
            lqw.le(Twadproject::getDstartdate,endTime);
            // lqw.between(Twadproject::getDstartdate, stratTime,endTime);
            // lqw.between(Twadproject::getDenddate, stratTime,endTime);
            lqw.like(null != queryKey, Twadproject::getSname, queryKey);
            if( projectEnd.equals("0") )
                lqw.eq(Twadproject::getBprojectcomplete,false);
            List<Twadproject> twadprojectList=  twadprojectMapper.selectList(lqw);
            for( int i = 0; i < twadprojectList.size(); i++ ) {
                ids.add(twadprojectList.get(i).getId());
            }
        }

        List<AdProjectCountVO> list = new ArrayList<>();
        if( ids.size() <= 0  ) {
            resulepage.setRecords(list);
            resulepage.setTotal(0);
            return  resulepage;
        }
        list = twadprojectMapper.queryAdProjectCount(ids,startRecord, endRecord, publishstatus);
        for(int i = 0; i <list.size(); i++) {
            AdProjectCountVO  vo = list.get(i);
            if( null == vo.getAmountachievement() ) {
                vo.setAmountachievement(BigDecimal.valueOf(0.0));
            }
            if( null == vo.getAmountarrearage() ) {
                vo.setAmountarrearage(BigDecimal.valueOf(0.0));
            }
            if( null == vo.getAmountreceived() ) {
                vo.setAmountreceived(BigDecimal.valueOf(0.0));
            }
            if( null == vo.getAmountreceivable() ) {
                vo.setAmountreceivable(BigDecimal.valueOf(0.0));
            }
            // 去年已完成
            vo.setFinshedlastyear(summaryReceivable(String.valueOf(vo.getId()),0));
            // 去年未完成
            vo.setNofinshedlastyear(summaryReceivable(String.valueOf(vo.getId()),1));
            // 今年已完成
            vo.setFinshedthisyear(summaryReceivable(String.valueOf(vo.getId()),2));
            // 今年未完成
            vo.setNofinshedthisyear(summaryReceivable(String.valueOf(vo.getId()),3));
            // 明年未完成
            vo.setNofinshednextyear(summaryReceivable(String.valueOf(vo.getId()),4));
            // 历史业绩
            vo.setHistory(summaryReceivable(String.valueOf(vo.getId()),5));
            // 成本汇总
            AdSummaryVO adSummaryVO = tworderitemMapper.summaryOrderCost(String.valueOf(vo.getId()));
            if( null != adSummaryVO ) {
                vo.setCostamount(adSummaryVO.getAmountreceivable());
            } else {
                vo.setCostamount(BigDecimal.valueOf(0.0));
            }
            list.set(i,vo);
        }
        resulepage.setRecords(list);
        resulepage.setTotal(ids.size());
        return  resulepage;
    }

    @Override
    public List<Twadproject> getAdProjectList() {
        LambdaQueryWrapper<Twadproject> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(Twadproject::getIsort);
        return  twadprojectMapper.selectList(lqw);
    }

    // detailtype
    // 0 : 去年已完成 1：去年未完成  2：今年已完成 3：今年未完成  4：明年未完成  5:历史业绩

    @Override
    public AdProjectCountOrderDetailsVO getAdProjectOrderDetails(String adProjectId,String mediaId,int pageNum, int pageSize,String detailtype) {
        /**
        AdProjectCountOrderDetailsVO  vo = new AdProjectCountOrderDetailsVO();
        List<OrderPublishQueryResultVo> list = tworderitemMapper.queryAdProjectOredrDetails(Long.valueOf(adProjectId),Long.valueOf(mediaId));
        vo.setOrderCount(list.size());
        vo.setOrderDetailsList(list);
        BigDecimal total  = list.stream().map(OrderPublishQueryResultVo::getAmountreceivable).reduce(BigDecimal.ZERO, BigDecimal::add);
        vo.setTotal(total);
        List<OrderPublishQueryResultVo> clist = list.stream().filter(s->s.getIpublishstatus()==4 || s.getIpublishstatus() == 5).collect(Collectors.toList());
        vo.setCompleted(clist.stream().map(OrderPublishQueryResultVo::getAmountreceivable).reduce(BigDecimal.ZERO, BigDecimal::add));
        List<OrderPublishQueryResultVo> nlist = list.stream().filter(s->s.getIpublishstatus()==0 || s.getIpublishstatus() == 2 || s.getIpublishstatus() == 3 || s.getIpublishstatus() == 1).collect(Collectors.toList());
        vo.setNoCompleted(nlist.stream().map(OrderPublishQueryResultVo::getAmountreceivable).reduce(BigDecimal.ZERO, BigDecimal::add));
        return vo; */
        pageNum = pageNum<=1? 0: (pageNum-1)*pageSize;

        AdProjectCountOrderDetailsVO  vo = new AdProjectCountOrderDetailsVO();
        String lastStartTime,lastEndTime,thisStartTime,thisEndTime,nextStartTime,nextEndTime,historyTime;
        List<OrderPublishQueryResultVo> list = new ArrayList<OrderPublishQueryResultVo>();
        int lastYear = getlastYear(),thisYear = getNowYear(),nextYear = getNextYear();
        historyTime = "2001-01-01";
        lastStartTime = String.format("%d-01-01",lastYear);
        lastEndTime = String.format("%d-12-31",lastYear);
        thisStartTime = String.format("%d-01-01",thisYear);
        thisEndTime = String.format("%d-12-31",thisYear);
        nextStartTime = String.format("%d-01-01",nextYear);
        nextEndTime = String.format("%d-12-31",nextYear);
        long count = 0;
        if( detailtype.equals("0") ) {
            lastStartTime = historyTime;
            list = tworderitemMapper.queryAdProjectOredrDetailsWithTime(Long.valueOf(adProjectId),mediaId,lastStartTime,lastEndTime,thisStartTime,thisEndTime,"0",pageNum,pageSize);
            count = tworderitemMapper.queryAdProjectOredrDetailsWithTimeCount(Long.valueOf(adProjectId),mediaId,lastStartTime,lastEndTime,thisStartTime,thisEndTime,"0",pageNum,pageSize);
        } else if(detailtype.equals("1") ) {
            lastStartTime = historyTime;
            list = tworderitemMapper.queryAdProjectOredrDetailsWithTime(Long.valueOf(adProjectId),mediaId,lastStartTime,lastEndTime,thisStartTime,thisEndTime,null,pageNum,pageSize);
            count = tworderitemMapper.queryAdProjectOredrDetailsWithTimeCount(Long.valueOf(adProjectId),mediaId,lastStartTime,lastEndTime,thisStartTime,thisEndTime,null,pageNum,pageSize);
        } else if( detailtype.equals("2") ) {
            lastStartTime = thisStartTime;
            lastEndTime = thisEndTime;
            list = tworderitemMapper.queryAdProjectOredrDetailsWithTime(Long.valueOf(adProjectId),mediaId,lastStartTime,lastEndTime,thisStartTime,thisEndTime,"0",pageNum,pageSize);
            count = tworderitemMapper.queryAdProjectOredrDetailsWithTimeCount(Long.valueOf(adProjectId),mediaId,lastStartTime,lastEndTime,thisStartTime,thisEndTime,"0",pageNum,pageSize);
        } else if( detailtype.equals("3") ) {
            lastStartTime = thisStartTime;
            lastEndTime = thisEndTime;
            list = tworderitemMapper.queryAdProjectOredrDetailsWithTime(Long.valueOf(adProjectId),mediaId,lastStartTime,lastEndTime,thisStartTime,thisEndTime,null,pageNum,pageSize);
            count = tworderitemMapper.queryAdProjectOredrDetailsWithTimeCount(Long.valueOf(adProjectId),mediaId,lastStartTime,lastEndTime,thisStartTime,thisEndTime,null,pageNum,pageSize);
        } else if( detailtype.equals("4") ) {
            lastStartTime = thisStartTime;
            lastEndTime = thisEndTime;
            thisStartTime = nextStartTime;
            thisEndTime = nextEndTime;
            list = tworderitemMapper.queryAdProjectOredrDetailsWithTime(Long.valueOf(adProjectId),mediaId,lastStartTime,lastEndTime,thisStartTime,thisEndTime,null,pageNum,pageSize);
            count = tworderitemMapper.queryAdProjectOredrDetailsWithTimeCount(Long.valueOf(adProjectId),mediaId,lastStartTime,lastEndTime,thisStartTime,thisEndTime,null,pageNum,pageSize);
        } else if( detailtype.equals("5") ) {
            lastStartTime = historyTime;
            list = tworderitemMapper.queryHistoryAdProjectOredrDetailsWithTime(Long.valueOf(adProjectId),mediaId,lastStartTime,lastEndTime,thisStartTime,thisEndTime,null,pageNum,pageSize);
            count = tworderitemMapper.queryHistoryAdProjectOredrDetailsWithTimeCount(Long.valueOf(adProjectId),mediaId,lastStartTime,lastEndTime,thisStartTime,thisEndTime,null,pageNum,pageSize);
        }
        vo.setOrderCount(count);
        vo.setOrderDetailsList(list);
        return vo;
    }

    @Override
    public IPage<AdOrderItemCostVO> getAdProjectCostDetails(String adProjectId, int pageNum, int pageSize) {
        IPage<AdOrderItemCostVO> resulepage = new Page<>();
        int startRecord = pageNum<=1? 0: (pageNum-1)*pageSize;
        int endRecord = pageSize;
        List<AdOrderItemCostVO> list =  tworderitemMapper.queryOrderItemCostList(Long.valueOf(adProjectId),startRecord,endRecord);
        for(int i = 0; i <list.size(); i++) {
            AdOrderItemCostVO vo = list.get(i);
            if( null == vo.getNamountcost() ) {
                vo.setNamountcost(BigDecimal.valueOf(0.0));
            }
            if( null == vo.getSdescription() ) {
                vo.setSdescription("");
            }
            list.set(i,vo);
        }
        resulepage.setRecords(list);
        resulepage.setTotal(tworderitemMapper.queryOrderItemCostCount(Long.valueOf(adProjectId)).size());
        return resulepage;
    }

    @Override
    public IPage<AdProjectContractVO> getAdProjectContract(String adProjectId,int projectType, int pageNum, int pageSize) {
        IPage<AdProjectContractVO> resulepage = new Page<>();
        int startRecord = pageNum<=1? 0: (pageNum-1)*pageSize;
        int endRecord = pageSize;
        LambdaQueryWrapper<Tworder> lqw = Wrappers.lambdaQuery();
        List<AdProjectContractVO> list = new ArrayList<AdProjectContractVO>();
        if(  0 == projectType ) {
            lqw.eq(null != adProjectId, Tworder::getAdprojectid, adProjectId);
        } else {
            lqw.eq(null != adProjectId, Tworder::getSpecialprojectid, adProjectId);
        }
        List<Tworder>  tworderList = tworderMapper.selectList(lqw);
        if( null != tworderList ) {
            for( int i = 0; i < tworderList.size(); i++ ) {
                Tworder  tworder = tworderList.get(i);
                AdProjectContractVO vo = new AdProjectContractVO();
                vo.setId(tworder.getId());
                vo.setScontractnum(tworder.getScontractnum());
                vo.setSordernum(tworder.getSordernum());
                vo.setCustomername(tworder.getCustomername());
                vo.setAgencyname(tworder.getAgencyname());
                vo.setAgentname(tworder.getAgentname());
                vo.setAdindustryname(tworder.getAdindustryname());
                vo.setEmployname(tworder.getEmployname());
                AdSummaryAllVO allVO = tworderapportiondetailMapper.getAdSummaryAll(String.valueOf(tworder.getId()));
                if( null != allVO ) {
                    vo.setAmountreceivable(allVO.getAmountreceivable());
                    vo.setAmountreceived(allVO.getAmountreceived());
                    vo.setAmountarrearage(allVO.getAmountarrearage());
                } else {
                    vo.setAmountreceivable(BigDecimal.valueOf(0.00));
                    vo.setAmountreceived(BigDecimal.valueOf(0.00));
                    vo.setAmountarrearage(BigDecimal.valueOf(0.00));
                }
                list.add(vo);
            }
            int endIndex = Math.min(startRecord + pageSize, tworderList.size());
            List<AdProjectContractVO> sublist = list.subList(startRecord, endIndex);
            resulepage.setRecords(sublist);
            resulepage.setTotal(tworderList.size());
        } else {
            resulepage.setTotal(0);
            resulepage.setRecords(list);
        }
        return resulepage;
    }

    @Override
    public IPage<TworderitemVO> getAdOrderItem(String adOrderId, int pageNum, int pageSize) {
        IPage<TworderitemVO> resulepage = new Page<>();
        int startRecord = pageNum<=1? 0: (pageNum-1)*pageSize;
        int endRecord = pageSize;
        LambdaQueryWrapper<Tworderitem> lqw = Wrappers.lambdaQuery();
        List<TworderitemVO> list = new ArrayList<TworderitemVO>();
        lqw.eq(null != adOrderId, Tworderitem::getOrderid,adOrderId);
        List<Tworderitem>  tworderitemList = tworderitemMapper.selectList(lqw);
        if( null != tworderitemList ) {
            for( int i = 0; i < tworderitemList.size(); i++ ) {
                TworderitemVO vo = TworderitemVO.parseToVO(tworderitemList.get(i));
                list.add(vo);
            }
            int endIndex = Math.min(startRecord + pageSize, tworderitemList.size());
            List<TworderitemVO> sublist = list.subList(startRecord, endIndex);
            resulepage.setRecords(sublist);
            resulepage.setTotal(tworderitemList.size());
        } else {
            resulepage.setTotal(0);
            resulepage.setRecords(list);
        }
        return resulepage;
    }

    // type参数
    // 0 : 去年已完成 1：去年未完成  2：今年已完成 3：今年未完成  4：明年未完成  5:历史业绩
    public BigDecimal summaryReceivable(String adProjectId,int type) {
        String lastStartTime,lastEndTime,thisStartTime,thisEndTime,nextStartTime,nextEndTime,historyTime;
        int lastYear = getlastYear(),thisYear = getNowYear(),nextYear = getNextYear();
        historyTime = "2001-01-01";
        lastStartTime = String.format("%d-01-01",lastYear);
        lastEndTime = String.format("%d-12-31",lastYear);
        thisStartTime = String.format("%d-01-01",thisYear);
        thisEndTime = String.format("%d-12-31",thisYear);
        nextStartTime = String.format("%d-01-01",nextYear);
        nextEndTime = String.format("%d-12-31",nextYear);
        AdSummaryVO vo = new AdSummaryVO();
        vo.setAmountreceivable(BigDecimal.valueOf(0.0));
        if( 0 == type ) {
            lastStartTime = historyTime;
            vo = tworderitemMapper.summaryReceivable(adProjectId,lastStartTime,lastEndTime,thisStartTime,thisEndTime,"0");
        } else if( 1 == type ) {
            lastStartTime = historyTime;
            vo = tworderitemMapper.summaryReceivable(adProjectId,lastStartTime,lastEndTime,thisStartTime,thisEndTime,null);
        } else if( 2 == type ) {
            vo = tworderitemMapper.summaryReceivable(adProjectId,thisStartTime,thisEndTime,thisStartTime,thisEndTime,"0");
        } else if( 3 == type ) {
            vo = tworderitemMapper.summaryReceivable(adProjectId,thisStartTime,thisEndTime,thisStartTime,thisEndTime,null);
        } else if( 4 == type ) {
            vo = tworderitemMapper.summaryReceivable(adProjectId,thisStartTime,thisEndTime,nextStartTime,nextEndTime,null);
        } else if( 5 == type ) {
            lastStartTime = historyTime;
            vo = tworderitemMapper.summaryHistoryReceivable(adProjectId,lastStartTime,lastEndTime,nextStartTime,nextEndTime,null);

        }
        if( null != vo ) {
            return vo.getAmountreceivable();
        }
        return BigDecimal.valueOf(0.0);
    }

    //获取今年是哪一年
    public Integer getNowYear() {
        Date date = new Date();
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return Integer.valueOf(gc.get(1));
    }

    //获取去年是哪一年
    public Integer getlastYear() {
        Date date = new Date();
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return Integer.valueOf(gc.get(1)-1);
    }

    //获取明年是哪一年
    public Integer getNextYear() {
        Date date = new Date();
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return Integer.valueOf(gc.get(1)+1);
    }

}


