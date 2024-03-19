package com.hgzp.advertising.service.flow.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.pagemodel.flow.dto.EmpHolidayDTO;
import com.hgzp.advertising.service.flow.TwempholidaysServiceI;
import com.hgzp.core.exception.DataExistException;
import com.hgzp.core.model.Twempholidays;
import com.hgzp.core.page.Json;
import com.hgzp.mapper.flow.TwempholidaysMapper;
import com.hgzp.service.common.impl.MyServiceImpl;
import com.hgzp.utils.WebUtil;
import com.hgzp.utils.model.LoginUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 人员假期表，假期期间工作移交给其他人员 服务实现类
 * </p>
 * E
 *
 * @author wwk
 * @since 2023-10-23
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TwempholidaysServiceImpl extends MyServiceImpl<TwempholidaysMapper, Twempholidays> implements TwempholidaysServiceI {
    @Autowired
    TwempholidaysMapper twempholidaysMapper;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;

    @Override
    public IPage<Twempholidays> getHolidayPageList(Page<Twempholidays> page, Twempholidays query) throws Exception {
        String userId = WebUtil.getLoginUser().getUserid().toString();
        LambdaQueryWrapper<Twempholidays> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.hasText(userId), Twempholidays::getOperatorid, userId);
        lqw.ge(query.getDstartdate() != null, Twempholidays::getDstartdate, query.getDstartdate());
        lqw.le(query.getDenddate() != null, Twempholidays::getDenddate, query.getDenddate());
        lqw.orderByDesc(Twempholidays::getDcreatedate);
        return twempholidaysMapper.selectPage(page, lqw);
    }

    @Override
    public Json saveEmpHolidays(EmpHolidayDTO empholidays) throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        Twempholidays twempholidays = new Twempholidays();
        BeanUtils.copyProperties(empholidays, twempholidays);
        List<Twempholidays> twempholidaysList = this.lambdaQuery()
                .eq(Twempholidays::getEmployid, twempholidays.getNewemployid())
                .and(twempholidays.getDstartdate() != null, m ->
                        m.between(Twempholidays::getDstartdate, twempholidays.getDstartdate(), twempholidays.getDenddate())
                                .or()
                                .between(Twempholidays::getDenddate, twempholidays.getDstartdate(), twempholidays.getDenddate())
                                .or(j -> j
                                        .lt(Twempholidays::getDenddate, twempholidays.getDstartdate())
                                        .gt(Twempholidays::getDenddate, twempholidays.getDenddate())))

                .list();
        if (twempholidaysList != null && twempholidaysList.size() > 0) {
            throw new DataExistException("指定接手人员在此期间有休假，请指定其他人");
        }
        List<Twempholidays> empholidayList = this.lambdaQuery()
                .eq(Twempholidays::getEmployid, twempholidays.getEmployid())
                .and(twempholidays.getDstartdate() != null, m ->
                        m.between(Twempholidays::getDstartdate, twempholidays.getDstartdate(), twempholidays.getDenddate())
                                .or()
                                .between(Twempholidays::getDenddate, twempholidays.getDstartdate(), twempholidays.getDenddate())
                                .or(j -> j
                                        .lt(Twempholidays::getDenddate, twempholidays.getDstartdate())
                                        .gt(Twempholidays::getDenddate, twempholidays.getDenddate()))
                )
                .list();
        if (empholidayList != null && empholidayList.size() > 0) {
            throw new DataExistException("当前休假人员在此期间已有休假");
        }
        twempholidays.setId(IdUtil.getSnowflakeNextId());
        twempholidays.setOperatorid(user.getUserid());
        twempholidays.setOperatorname(user.getUsername());
        twempholidays.setBuse(true);
        twempholidays.setDcreatedate(new Date());

        innerInterceptor.recoredLog();
        twempholidaysMapper.insert(twempholidays);
        return Json.success();
    }

    @Override
    public Json updateEmpHolidays(EmpHolidayDTO empholidays) throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        Twempholidays twempholidays = new Twempholidays();
        BeanUtils.copyProperties(empholidays, twempholidays);
        List<Twempholidays> twempholidaysList = this.lambdaQuery()
                .eq(Twempholidays::getEmployid, twempholidays.getNewemployid())
                .and(twempholidays.getDstartdate() != null, m ->
                        m.between(Twempholidays::getDstartdate, twempholidays.getDstartdate(), twempholidays.getDenddate())
                                .or()
                                .between(Twempholidays::getDenddate, twempholidays.getDstartdate(), twempholidays.getDenddate())
                                .or(j -> j
                                        .lt(Twempholidays::getDenddate, twempholidays.getDstartdate())
                                        .gt(Twempholidays::getDenddate, twempholidays.getDenddate()))
                )
                .list();
        if (twempholidaysList != null && twempholidaysList.size() > 0) {
            return Json.fail("指定人员在此期间有休假，请指定其他人");
        }
        List<Twempholidays> empholidayList = this.lambdaQuery()
                .eq(Twempholidays::getEmployid, twempholidays.getEmployid())
                .ne(Twempholidays::getId, twempholidays.getId())
                .and(twempholidays.getDstartdate() != null, m ->
                        m.between(Twempholidays::getDstartdate, twempholidays.getDstartdate(), twempholidays.getDenddate())
                                .or()
                                .between(Twempholidays::getDenddate, twempholidays.getDstartdate(), twempholidays.getDenddate())
                                .or(j -> j
                                        .lt(Twempholidays::getDenddate, twempholidays.getDstartdate())
                                        .gt(Twempholidays::getDenddate, twempholidays.getDenddate()))
                )
                .list();
        if (empholidayList != null && empholidayList.size() > 0) {
            return Json.fail("当前人员在此期间已有休假");
        }

        innerInterceptor.recoredLog();
        twempholidaysMapper.updateById(twempholidays);
        return Json.success();
    }

    @Override
    public List<String> replaceHolidayUser(List<String> userIdList) {
        if (userIdList.isEmpty()) {
            return userIdList;
        }
        Date now = DateUtil.parse(DateUtil.today()).toJdkDate();

        // 查询今天是否有人员休假
        List<Twempholidays> twempholidaysList = this.lambdaQuery()
                .in(Twempholidays::getEmployid, userIdList)
                .le(Twempholidays::getDstartdate, now)
                .ge(Twempholidays::getDenddate, now)
                .list();
        if (twempholidaysList.isEmpty()) {
            return userIdList;
        }

        //查询替换的人员中是否还有休假的人员
        List<Long> replaceUserIdList = twempholidaysList.stream().map(Twempholidays::getNewemployid).collect(Collectors.toList());
        List<Twempholidays> replaceTwempholidaysList = this.lambdaQuery()
                .in(Twempholidays::getEmployid, replaceUserIdList)
                .le(Twempholidays::getDstartdate, now)
                .ge(Twempholidays::getDenddate, now)
                .list();
        List<Long> replaceUserHolidays = replaceTwempholidaysList.stream().map(Twempholidays::getEmployid).collect(Collectors.toList());


        for (Twempholidays twempholidays : twempholidaysList) {
            Long employid = twempholidays.getEmployid();
            Long replaceid = twempholidays.getNewemployid();
            //判断替换的人员是否还有休假,如果有则继续由前人处理
            boolean contains = replaceUserHolidays.contains(replaceid);
            if (contains) {
                continue;
            }
            userIdList.remove(employid.toString());
            userIdList.add(replaceid.toString());
        }
        return userIdList;
    }


}
