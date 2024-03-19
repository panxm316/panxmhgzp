package com.hgzp.advertising.service.ad.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgzp.advertising.emnus.approve.ApproveStatus;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.service.ad.SpecialProjectServiceI;
import com.hgzp.core.model.Twadproject;
import com.hgzp.core.model.Tworder;
import com.hgzp.core.model.Twspecialproject;
import com.hgzp.core.page.BaseQueryInfo;
import com.hgzp.mapper.ad.TworderMapper;
import com.hgzp.mapper.ad.TwspecialprojectMapper;
import com.hgzp.pagemodel.ad.AdProjectCountVO;
import com.hgzp.pagemodel.ad.AdSummaryVO;
import com.hgzp.service.common.impl.MyServiceImpl;
import com.hgzp.utils.WebUtil;
import com.hgzp.utils.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 特刊项目 服务实现类
 * </p>
 *
 * @author muyn
 * @since 2024-03-13
 */
@Service
public class SpecialProjectServiceImpl extends MyServiceImpl<TwspecialprojectMapper, Twspecialproject> implements SpecialProjectServiceI {

    @Autowired
    private HgDataChangeRecorderInnerInterceptor innerInterceptor;
    @Autowired
    private TwspecialprojectMapper twspecialprojectMapper;
    @Autowired
    TworderMapper tworderMapper;


    @Override
    public Twspecialproject getById(Serializable id) {
        Twspecialproject twspecialproject = super.getById(id);
        if (ObjectUtil.isNull(twspecialproject)) {
            throw new RuntimeException("Twspecialproject不存在");
        }
        return twspecialproject;
    }

    @Override
    public void saveAdProject(Twspecialproject specialproject, HttpServletRequest request) throws Exception {
        LoginUser user = WebUtil.getLoginUser(request);
        specialproject.setCreateempid(user.getUserid());
        specialproject.setScreatename(user.getUsername());
        Date createdate = new Date();
        specialproject.setDcreatedate(createdate);
        innerInterceptor.recoredLog();
        twspecialprojectMapper.insert(specialproject);
    }

    @Override
    public boolean isExistSpecialProject(Twspecialproject specialproject) {
        Long count = new LambdaQueryChainWrapper<>(twspecialprojectMapper)
                .eq(Twspecialproject::getSname, specialproject.getSname())
                .ne(specialproject.getId() != null, Twspecialproject::getId, specialproject.getId())
                .count();
        return count > 0;
    }

    @Override
    public IPage<Twspecialproject> getSpecialProjectPageList(Page<Twspecialproject> page, BaseQueryInfo query) {
        /**eq：等于某个日期
         ne：不等于某个日期
         gt：大于某个日期
         ge：大于等于某个日期
         lt：小于某个日期
         le：小于等于某个日期*/
        LambdaQueryWrapper<Twspecialproject> lqw = Wrappers.lambdaQuery();
        if (query.getStartTime() == null) {
            if (query.getEndTime() != null) {
                //wm.dstartdate <= _dtEndDate
                lqw.le(Twspecialproject::getDstartdate, query.getEndTime());
            }
        } else {
            if (query.getEndTime() == null) {
                //(wm.dstartdate >= _dtstartDate || (wm.dstartdate < _dtEndDate && wm.denddate >= _dtstartDate))
                lqw.and(wrapper -> wrapper.and(wrapper1 -> wrapper1.ge(Twspecialproject::getDstartdate,
                        query.getStartTime()))
                        .or((wrapper2 -> wrapper2.ge(Twspecialproject::getDenddate, query.getStartTime()))));
            } else {
                // ((wm.dstartdate >= _dtstartDate && wm.dstartdate <= _dtEndDate) || (wm.dstartdate < _dtEndDate &&
                // wm.denddate >= _dtstartDate))))
                lqw.and(wrapper -> wrapper.and(wrapper1 -> wrapper1.ge(Twspecialproject::getDstartdate,
                        query.getStartTime()).le(Twspecialproject::getDstartdate, DateUtil.offsetDay(query.getEndTime(), 1)))
                        .or(wrapper2 -> wrapper2.le(Twspecialproject::getDstartdate, DateUtil.offsetDay(query.getEndTime(), 1)).ge(Twspecialproject::getDenddate, query.getStartTime())));
            }
        }

        lqw.like(StrUtil.isNotBlank(query.getQueryKey()), Twspecialproject::getSname, query.getQueryKey());
        IPage<Twspecialproject> adprojectPage = twspecialprojectMapper.selectPage(page, lqw);
        return adprojectPage;

    }

    @Override
    public String deleteSpecialProject(String ids) {
        String[] split = ids.split(",");
        List<Twspecialproject> lsProjects = new LambdaQueryChainWrapper<>(twspecialprojectMapper).in(Twspecialproject::getId,
                split).list();
        String sInfo = "";
        for (Twspecialproject item : lsProjects) {
            //判断是否被用
            Long count = new LambdaQueryChainWrapper<>(tworderMapper)
                    .eq(Tworder::getSpecialprojectid, item.getId())
                    .count();
            if (count == 0) {
                twspecialprojectMapper.deleteById(item.getId());
            } else {
                sInfo += item.getSname() + "项目被订单使用，不能删除！";
            }
        }
        return sInfo;
    }

    @Override
    public IPage<AdProjectCountVO> getSpecialProjectCountList(String stratTime, String endTime, String adProjectId, int pageNum, int pageSize, String publishstatus, String queryKey, String projectEnd) {
        IPage<AdProjectCountVO> resulepage = new Page<>();
        int startRecord = pageNum<=1? 0: (pageNum-1)*pageSize;
        int endRecord = pageSize;
        List<Long> ids = new ArrayList<Long>();
        if( null != adProjectId && !adProjectId.isEmpty() ) {
            ids.add(Long.valueOf(adProjectId));
        } else {
            LambdaQueryWrapper<Twspecialproject> lqw = Wrappers.lambdaQuery();
            lqw.ge(Twspecialproject::getDenddate,stratTime);
            lqw.le(Twspecialproject::getDstartdate,endTime);
            // lqw.between(Twadproject::getDstartdate, stratTime,endTime);
            // lqw.between(Twadproject::getDenddate, stratTime,endTime);
            lqw.like(null != queryKey, Twspecialproject::getSname, queryKey);
            if( projectEnd.equals("0") )
                lqw.eq(Twspecialproject::getBprojectcomplete,false);
            List<Twspecialproject> twadprojectList=  twspecialprojectMapper.selectList(lqw);
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
        list = twspecialprojectMapper.querySpecialProjectCount(ids,startRecord, endRecord, publishstatus);
        for( int i = 0; i < list.size(); i++ ) {
            AdProjectCountVO  vo = list.get(i);
            if( vo.getAmountreceivable() == null ) {
                vo.setAmountreceivable(BigDecimal.valueOf(0.0));
            }
            if( vo.getAmountreceived() == null ) {
                vo.setAmountreceived(BigDecimal.valueOf(0.0));
            }
            if( vo.getAmountarrearage() == null ) {
                vo.setAmountarrearage(BigDecimal.valueOf(0.0));
            }
            if( vo.getAmountachievement() == null ) {
                vo.setAmountachievement(BigDecimal.valueOf(0.0));
            }

        }
        resulepage.setRecords(list);
        resulepage.setTotal(ids.size());
        return  resulepage;

    }

}
