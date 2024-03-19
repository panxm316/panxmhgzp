package com.hgzp.advertising.service.schedule.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hgzp.advertising.pagemodel.schedule.vo.*;
import com.hgzp.advertising.service.media.TbmediaServiceI;
import com.hgzp.advertising.service.schedule.ScheduleseekServiceI;
import com.hgzp.core.model.*;
import com.hgzp.core.page.DataCombo;
import com.hgzp.mapper.ad.TworderitemMapper;
import com.hgzp.mapper.business.TwtasksMapper;
import com.hgzp.mapper.media.TbfoldareaverMapper;
import com.hgzp.mapper.price.TbaddisplayformMapper;
import com.hgzp.mapper.schedule.TbfoldpageplanMapper;
import com.hgzp.pagemodel.schedule.OrderPublishQueryResultVo;
import com.hgzp.pagemodel.schedule.PageOrderItemVo;
import org.apache.commons.collections4.ArrayStack;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ScheduleseekServiceIImpl
 * 创建人：lhl
 * 类描述：排期查看服务实现类
 * 创建日期：2023/11/27 12:41
 */
@Service
public class ScheduleseekServiceIImpl implements ScheduleseekServiceI {

    @Autowired
    TbfoldareaverMapper tbfoldareaverMapper;
    @Autowired
    TbfoldpageplanMapper tbfoldpageplanMapper;
    @Autowired
    TworderitemMapper tworderitemMapper;
    @Autowired
    TbmediaServiceI tbmediaServiceI;
    @Autowired
    TbaddisplayformMapper tbaddisplayformMapper;


    @Override
    public List<DataCombo> getFoldAreaverCombo(String foldid) {
        if (null == foldid || foldid.isEmpty()) {
            List<DataCombo> comboList = new ArrayList<DataCombo>();
            return comboList;
        }
        LambdaQueryWrapper<Tbfoldareaver> lqw = Wrappers.lambdaQuery();
        // 叠次ID
        lqw.eq(null != foldid, Tbfoldareaver::getFoldid, foldid);
        lqw.eq(Tbfoldareaver::getBuse, true);
        lqw.orderByDesc(Tbfoldareaver::getIsort);
        List<Tbfoldareaver> tbfoldareaverList = tbfoldareaverMapper.selectList(lqw);
        List<DataCombo> comboList = tbfoldareaverList.stream()
                .map(item -> new DataCombo(item.getId().toString(), item.getSname()))
                .collect(Collectors.toList());
        return comboList;

    }

    @Override
    public List<MediaPagePlanVo> getFoldPagePlanList(String mediaId, String foldId, String areaverId,
                                                     String startTime, String endTime) {
        QueryWrapper<Tbfoldpageplan> query = new QueryWrapper<>();
        query.select(" DISTINCT foldareaverid", "medianame", "foldid", "foldname", "foldareavername", "publishdate").lambda()
                .eq(null != foldId, Tbfoldpageplan::getFoldid, foldId)
                .eq(null != areaverId, Tbfoldpageplan::getFoldareaverid, areaverId)
                .eq(null != mediaId, Tbfoldpageplan::getMediaid, mediaId)
                .between(Tbfoldpageplan::getPublishdate, startTime, endTime)
                .orderByDesc(Tbfoldpageplan::getPublishdate);
        List<Tbfoldpageplan> tbfoldpageplanList = tbfoldpageplanMapper.selectList(query);
        if (null == tbfoldpageplanList)
            return null;
        //tbfoldpageplanList = tbfoldpageplanList.stream().distinct().collect(Collectors.toList());
        List<MediaPagePlanVo> mediaPagePlanVoslist = new ArrayList<MediaPagePlanVo>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < tbfoldpageplanList.size(); i++) {
            MediaPagePlanVo mediaPagePlanVo = new MediaPagePlanVo();
            mediaPagePlanVo.setFoldid(tbfoldpageplanList.get(i).getFoldid());
            mediaPagePlanVo.setFoldareaverid(tbfoldpageplanList.get(i).getFoldareaverid());
            mediaPagePlanVo.setPublishdate(tbfoldpageplanList.get(i).getPublishdate());
            mediaPagePlanVo.setFoldname(tbfoldpageplanList.get(i).getFoldname());
            mediaPagePlanVo.setFoldareavername(tbfoldpageplanList.get(i).getFoldareavername());
            mediaPagePlanVo.setMedianame(sdf.format(tbfoldpageplanList.get(i).getPublishdate()) + "-" + mediaPagePlanVo.getFoldname() + "-" + mediaPagePlanVo.getFoldareavername());
            mediaPagePlanVoslist.add(mediaPagePlanVo);
        }
        return mediaPagePlanVoslist;
    }

    @Override
    public List<PageOrderVO> getPageList(String mediaId, String foldId, String areaverId, String publishTime) {
        List<PageOrderVO> pageOrderVOList = new ArrayList<PageOrderVO>();
        LambdaQueryWrapper<Tbfoldpageplan> lqw = Wrappers.lambdaQuery();
        // 媒体ID
        lqw.eq(null != mediaId, Tbfoldpageplan::getMediaid, mediaId);
        // 叠次ID
        lqw.eq(null != foldId, Tbfoldpageplan::getFoldid, foldId);
        // 叠次版ID
        lqw.eq(null != areaverId, Tbfoldpageplan::getFoldareaverid, areaverId);
        // 日期
        lqw.eq(null != publishTime, Tbfoldpageplan::getPublishdate, publishTime);
        lqw.orderByAsc(Tbfoldpageplan::getPagenum);
        List<Tbfoldpageplan> tbfoldpageplanList = tbfoldpageplanMapper.selectList(lqw);
        for( int i = 0; i < tbfoldpageplanList.size(); i++ ) {
            Tbfoldpageplan tbfoldpageplan = tbfoldpageplanList.get(i);
            PageOrderVO vo = new PageOrderVO();
            vo.setIpagewidth(tbfoldpageplan.getIpagewidth());
            vo.setIpageheight(tbfoldpageplan.getIpageheight());
            vo.setId(tbfoldpageplan.getId());
            vo.setAdcolorname(tbfoldpageplan.getAdcolorname());
            List<PageOrderItemVo> list = tworderitemMapper.queryPageOrderItemList(tbfoldpageplan.getId());
            for( int m = 0; m < list.size(); m++ ) {
                PageOrderItemVo itmevo = list.get(m);
                itmevo.setEmployname(margeName(new String[]{itmevo.getEmployname(),itmevo.getAgentemployname(),itmevo.getAgencyemployname()}));
                itmevo.setAdspecname(margeName(new String[]{itmevo.getAdspecname(),itmevo.getAdcolorname()}));
                list.set(m,itmevo);
            }
            vo.setPageOrderItemVoList(list);
            pageOrderVOList.add(vo);
        }
        return pageOrderVOList;
    }

    @Override
    public List<PageOrderItemVo> getAdvOrderList(String mediaId, String foldId, String areaverId, String startTime,
                                                String endTime) {
        List<PageOrderItemVo> list = tworderitemMapper.queryNewMediaOrderItemList(mediaId,foldId,areaverId,startTime,endTime,null);
        for( int m = 0; m < list.size(); m++ ) {
            PageOrderItemVo itmevo = list.get(m);
            itmevo.setEmployname(margeName(new String[]{itmevo.getEmployname(),itmevo.getAgentemployname(),itmevo.getAgencyemployname()}));
            itmevo.setAdspecname(margeName(new String[]{itmevo.getAdspecname(),itmevo.getAdcolorname()}));
            itmevo.setDay(getDay(itmevo.getDpublishdate()));
            list.set(m,itmevo);
        }
        return  list;
    }

    @Override
    public List<AdvDisplayFormVo> getAdvOrderofDay(String mediaId, String foldId, String areaverId, String strartTime
            , String endTime) {
        List<AdvDisplayFormVo> advDisplayFormVoList = new ArrayList<AdvDisplayFormVo>();
        Tbmedia tbmedia = tbmediaServiceI.getById(mediaId);
        if (null == tbmedia) {
            return advDisplayFormVoList;
        }
        LambdaQueryWrapper<Tbaddisplayform> lqw = Wrappers.lambdaQuery();
        // 媒体ID
        lqw.eq(null != tbmedia.getMediatypekey(), Tbaddisplayform::getMediatypekey, tbmedia.getMediatypekey());
        List<Tbaddisplayform> tbaddisplayformList = tbaddisplayformMapper.selectList(lqw);
        if (null == tbaddisplayformList || tbaddisplayformList.size() <= 0)
            return advDisplayFormVoList;
        for (int i = 0; i < tbaddisplayformList.size(); i++) {
            Tbaddisplayform tbaddisplayform = tbaddisplayformList.get(i);
            AdvDisplayFormVo advDisplayFormVo = new AdvDisplayFormVo();
            advDisplayFormVo.setId(tbaddisplayform.getId());
            advDisplayFormVo.setName(tbaddisplayform.getSname());
            List<PageOrderItemVo> list = tworderitemMapper.queryNewMediaOrderItemList(mediaId,foldId,areaverId,strartTime,endTime,String.valueOf(tbaddisplayform.getId()));

            Date startDate = transferString2Date(strartTime);
            int year = getYear(startDate);
            int month = getMonth(startDate) + 1;
            int days = getDaysInMonth(year, month);
            List<AdvOfDayVo> advOfDayVoList = new ArrayList<AdvOfDayVo>();
            for (int k = 1; k <= days; k++) {
                AdvOfDayVo advOfDayVo = new AdvOfDayVo();
                advOfDayVo.setDay(k);
                List<AdvOrderItemVo> advOrderItemVoList = new ArrayList<AdvOrderItemVo>();
                List<PageOrderItemVo> itemList = getOrderList(list, k);
                if (null != itemList && itemList.size() > 0) {
                    for (int m = 0; m < itemList.size(); m++) {
                        PageOrderItemVo itmevo = itemList.get(m);
                        itmevo.setEmployname(margeName(new String[]{itmevo.getEmployname(),itmevo.getAgentemployname(),itmevo.getAgencyemployname()}));
                        itmevo.setAdspecname(margeName(new String[]{itmevo.getAdspecname(),itmevo.getAdcolorname()}));
                        itmevo.setDay(getDay(itmevo.getDpublishdate()));
                        itemList.set(m,itmevo);

                    }

                }
                advOfDayVo.setPageOrderItemVoList(itemList);
                advOfDayVoList.add(advOfDayVo);
            }
            advDisplayFormVo.setAdvOfDayVoList(advOfDayVoList);
            advDisplayFormVoList.add(advDisplayFormVo);
        }
        return advDisplayFormVoList;
    }

    @Override
    public List<OrderPublishQueryResultVo> queryOredrPredetermineList(String mediaId, String foldId, String areaverId, String strartTime, String endTime) {
        return tworderitemMapper.queryOredrPredetermineList(Long.valueOf(mediaId),foldId,areaverId,strartTime,endTime);
    }

    // 获取天数
    public int getDay(Date date) {
        Calendar cal = Calendar.getInstance();
        if (null != cal)
            cal.setTime(date);
        int day = cal.get(Calendar.DATE);
        return day;
    }

    // 获取月份
    public int getMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        if (null != cal)
            cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        return month;
    }

    // 获取年份
    public int getYear(Date date) {
        Calendar cal = Calendar.getInstance();
        if (null != cal)
            cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        return year;
    }

    public Date transferString2Date(String s) {
        Date date = new Date();
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(s);
        } catch (ParseException e) {
            //LOGGER.error("时间转换错误, string = {}", s, e);
        }
        return date;
    }

    // 获取指定月份的天数
    public int getDaysInMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    // 获取指定月份的天数
    public List<PageOrderItemVo> getOrderList(List<PageOrderItemVo> list, int day) {
        List<PageOrderItemVo> itemList = new ArrayList<PageOrderItemVo>();
        if (null == list && list.size() <= 0)
            return itemList;
        for (int i = 0; i < list.size(); i++) {
            PageOrderItemVo vo = list.get(i);
            Date date = vo.getDpublishdate();
            int nowday = getDay(date);
            if (nowday == day)
                itemList.add(vo);
        }
        return itemList;
    }

    public String  margeName(String[] paramList)
    {
        String name = "";
        if( null == paramList )
            return  name;
        for( int i = 0; i < paramList.length; i++ )
        {
            String item = paramList[i];
            if( null != item && !item.isEmpty() ) {
                name += item;
                name += "/";
            }
        }
        if( name.length() > 0 ) {
            name = name.substring(0,name.length()-1);
        }
        return name;
    }

}


