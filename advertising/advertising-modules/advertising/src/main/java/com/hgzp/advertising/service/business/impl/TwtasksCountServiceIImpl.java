package com.hgzp.advertising.service.business.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgzp.advertising.pagemodel.business.dto.TwtaskMessageDTO;
import com.hgzp.advertising.service.business.TwtasksCountServiceI;
import com.hgzp.advertising.service.business.TwtasksServiceI;
import com.hgzp.advertising.service.media.TbmediaServiceI;
import com.hgzp.advertising.service.system.TbdeptServiceI;
import com.hgzp.advertising.service.system.TwmessageServiceI;
import com.hgzp.common.flowable.dto.third.MessageDto;
import com.hgzp.core.emnus.MessageTypeEnum;
import com.hgzp.core.model.*;
import com.hgzp.mapper.ad.TworderitemMapper;
import com.hgzp.mapper.business.TwtasksMapper;
import com.hgzp.mapper.system.TbdeptexMapper;
import com.hgzp.mapper.system.TbemployMapper;
import com.hgzp.mapper.system.TbemployroleMapper;
import com.hgzp.pagemodel.business.TaskCountVo;
import com.hgzp.pagemodel.schedule.OrderPublishQueryResultVo;
import com.hgzp.service.system.BaseTbemployServiceI;
import com.hgzp.utils.WebUtil;
import com.hgzp.utils.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * TwtasksCountServiceIImpl
 * 创建人：lhl
 * 类描述：任务额度汇总统计 实现类
 * 创建日期：2023/12/26 16:05
 */
@Service
public class TwtasksCountServiceIImpl extends ServiceImpl<TworderitemMapper, Tworderitem> implements TwtasksCountServiceI {
    @Autowired
    TworderitemMapper  tworderitemMapper;
    @Autowired
    TwtasksServiceI  twtasksServiceI;
    @Autowired
    TbdeptServiceI tbdeptServiceI;
    @Autowired
    TbmediaServiceI tbmediaServiceI;
    @Autowired
    TbdeptexMapper tbdeptexMapper;
    @Autowired
    BaseTbemployServiceI baseTbemployServiceI;
    @Autowired
    TbemployMapper  tbemployMapper;
    @Resource
    private TwmessageServiceI messageService;


    @Override
    public IPage<TaskCountVo> getDepartmentTaskCountList(String mediaId, List<Long> ids, String stratTime, String endTime, int pageNum, int pageSize, String publishstatus,String dateType) {
        IPage<TaskCountVo> resulepage = new Page<>();
        int startRecord = pageNum<=1? 0: (pageNum-1)*pageSize;
        int endRecord = pageSize;
        List<TaskCountVo> list = new ArrayList<>();
        if( ids.size() <= 0  ) {
            resulepage.setRecords(list);
            resulepage.setTotal(0);
            return  resulepage;
        }
        list = tworderitemMapper.queryDepartmentTaskCountList(mediaId, ids,stratTime, endTime, startRecord, endRecord, publishstatus);
        for(int i = 0; i <list.size(); i++) {
            TaskCountVo vo = list.get(i);
            Twtasks twtasks = twtasksServiceI.getTwtasksByDepartId(String.valueOf(vo.getDeptid()),"0");
            if (null == twtasks) {
                vo.setNtaskamount(BigDecimal.valueOf(0.0));
                vo.setRatio("100%");
                vo.setSremark("");
                vo.setTaskid(0L);
            } else {
                vo.setNtaskamount(twtasks.getNtaskamount());
                if( twtasks.getNtaskamount().doubleValue() > 0.0 ) {
                    if( null != vo.getAmountachievement() ) {
                        double ratio =  vo.getAmountachievement().doubleValue() / (twtasks.getNtaskamount().doubleValue()*10000);
                        vo.setRatio(String.format("%.2f%%",ratio*100));
                    }
                    else {
                        vo.setAmountachievement(BigDecimal.valueOf(0.0));
                        vo.setRatio("汇总失败");
                    }
                    vo.setSremark(twtasks.getSremark());
                    vo.setTaskid(twtasks.getId());
                }
            }
            Date startDate = transferString2Date(stratTime);
            int year = getYear(startDate);
            int month = getMonth(startDate) + 1;
            // 年度
            if( dateType.equals("0") ) {
                vo.setCountdate(String.format("%d",year));
            } else {
                vo.setCountdate(String.format("%d-%d",year,month));
            }
            // 部门
            Tbdept tbdept = tbdeptServiceI.getById(vo.getDeptid());
            if( null != tbdept ) {
                String parentName = "";
                vo.setDepartmentname(tbdept.getSdeptname());
                List<Long> idList =  tbdeptServiceI.getParentDeptId(tbdept.getId());
                for( int m = 0; m < idList.size(); m++ ) {
                    Tbdept tempdept = tbdeptServiceI.getById(idList.get(m));
                    if( null != tempdept ) {
                        parentName += tempdept.getSdeptname();
                        parentName += "-";
                    }
                }
                if( !parentName.isEmpty() ) {
                    parentName = parentName.substring(0,parentName.length()-1);
                } else {
                    parentName = "";
                }
                vo.setSuperiorname(parentName);

            } else {
                vo.setDepartmentname("");
                vo.setSuperiorname("");
            }
            // 媒体
            if( null != mediaId ) {
                Tbmedia tbmedia = tbmediaServiceI.getById(mediaId);
                if( null != tbmedia ) {
                    vo.setMedianame(tbmedia.getSname());
                    vo.setMediaid(tbmedia.getId());
                } else {
                    vo.setMedianame("");
                    vo.setMediaid(0L);
                }

            }
            list.set(i,vo);
        }
        resulepage.setRecords(list);
        resulepage.setTotal(ids.size());
        return  resulepage;

    }

    @Override
    public List<Long> filterDepartmentLevel(List<Long> ids, String level) {
        List<Long>  depIds = new ArrayList<Long>();
        for( int i = 0; i < ids.size(); i++ ) {
            LambdaQueryWrapper<Tbdeptex> lqw = Wrappers.lambdaQuery();
            if( level.equals("0") ) {
                lqw.eq(Tbdeptex::getParentid1, ids.get(i));
            } else if( level.equals("1") ) {
                lqw.eq(Tbdeptex::getParentid2, ids.get(i));
            } else if( level.equals("2") ) {
                lqw.eq(Tbdeptex::getParentid3, ids.get(i));
            } else if( level.equals("3") ) {
                lqw.eq(Tbdeptex::getParentid4, ids.get(i));
            }  else if( level.equals("4") ) {
                lqw.eq(Tbdeptex::getParentid5, ids.get(i));
            }
            long count = tbdeptexMapper.selectCount(lqw);
            if( count > 0 )
                depIds.add(ids.get(i));
        }
        return depIds;
    }

    @Override
    public IPage<TaskCountVo> getEmployeTaskCountList(String mediaId, List<Long> ids, String stratTime, String endTime, int pageNum, int pageSize, String publishstatus, String dateType) {
        IPage<TaskCountVo> resulepage = new Page<>();
        int startRecord = pageNum<=1? 0: (pageNum-1)*pageSize;
        int endRecord = pageSize;
        List<TaskCountVo> list = new ArrayList<>();
        if( ids.size() <= 0  ) {
            resulepage.setRecords(list);
            resulepage.setTotal(0);
            return  resulepage;
        }
        List<Long> employList = new ArrayList<Long>();
        for( int i = 0; i <  ids.size(); i++ )  {
            List<Long> tempList = getEmployeOfDepart(ids.get(i));
            employList.addAll(tempList);
        }
        list = tworderitemMapper.queryEmployeTaskCountList(mediaId,employList,stratTime, endTime, startRecord, endRecord, publishstatus);
        for(int i = 0; i <list.size(); i++) {
            TaskCountVo vo = list.get(i);
            Twtasks twtasks = twtasksServiceI.getTwtasksByDepartId(String.valueOf(vo.getEmployid()),"1");
            if (null == twtasks) {
                vo.setNtaskamount(BigDecimal.valueOf(0.0));
                vo.setRatio("100%");
                vo.setSremark("");
                vo.setTaskid(0L);
            } else {
                vo.setNtaskamount(twtasks.getNtaskamount());
                if( twtasks.getNtaskamount().doubleValue() > 0.0 ) {
                    if( null != vo.getAmountachievement() ) {
                        double ratio =  vo.getAmountachievement().doubleValue() / (twtasks.getNtaskamount().doubleValue()*10000);
                        vo.setRatio(String.format("%.2f%%",ratio*100));
                    }
                    else {
                        vo.setAmountachievement(BigDecimal.valueOf(0.0));
                        vo.setRatio("汇总失败");
                    }
                    vo.setSremark(twtasks.getSremark());
                    vo.setTaskid(twtasks.getId());
                }
            }
            Date startDate = transferString2Date(stratTime);
            int year = getYear(startDate);
            int month = getMonth(startDate) + 1;
            // 年度
            if( dateType.equals("0") ) {
                vo.setCountdate(String.format("%d",year));
            } else {
                vo.setCountdate(String.format("%d-%d",year,month));
            }
            // 部门
            Tbdept tbdept = tbdeptServiceI.getById(vo.getDeptid());
            if( null != tbdept ) {
                vo.setDepartmentname(tbdept.getSdeptname());
             } else {
                vo.setDepartmentname("");
            }
            // 人员
            Tbemploy tbemploy = baseTbemployServiceI.getById(vo.getEmployid());
            if( null != tbemploy ) {
                vo.setEmployename(tbemploy.getSusername());
            } else {
                vo.setEmployename("");
            }
            // 媒体
            if( null != mediaId ) {
                Tbmedia tbmedia = tbmediaServiceI.getById(mediaId);
                if( null != tbmedia ) {
                    vo.setMedianame(tbmedia.getSname());
                    vo.setMediaid(tbmedia.getId());
                } else {
                    vo.setMedianame("");
                    vo.setMediaid(0L);
                }

            }
            list.set(i,vo);
        }
        resulepage.setRecords(list);
        resulepage.setTotal(ids.size());
        return  resulepage;

    }

    @Override
    public boolean sendTwtaskMessage(TwtaskMessageDTO twtaskMessageDTO) throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        Long receiveempid;
        if(  twtaskMessageDTO.getMessagetype().equals("0") ) {
            receiveempid = getDepartLeader(twtaskMessageDTO.getDepartid());
        } else{
            receiveempid = twtaskMessageDTO.getEmplyeid();
        }
        if( 0L == receiveempid ) {
            return false;
        }
        Twmessage message = new Twmessage();
        message.setId(IdUtil.getSnowflakeNextId());
        message.setStitle(twtaskMessageDTO.getTitle());
        message.setScontent(twtaskMessageDTO.getContent());
        message.setSparam("");
        message.setStype(MessageTypeEnum.TODO_TSAKNOTICE.getType());
        message.setDcreatetime(new Date());
        message.setCreateempid(user.getUserid());
        message.setReceiveempid(receiveempid);
        message.setBreaded(false);
        message.setBdeleted(false);
        message.setSflowid("");
        message.setSprocessinstanceid("");
        message.setDprocessinstancecreate(new Date());
        message.setSuniqueid("");
        messageService.save(message);
        return true;
    }

    @Override
    public Long getDepartLeader(Long departId) {
        LambdaQueryWrapper<Tbemploy> lqw = Wrappers.lambdaQuery();
        lqw.eq(Tbemploy::getDeptid,departId);
        lqw.eq(Tbemploy::getBlead,true);
        List<Tbemploy> list = tbemployMapper.selectList(lqw);
        if( list.size() > 0 ) {
            return list.get(0).getId();
        }
        return 0L;
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

    public List<Long> getEmployeOfDepart(Long depatId) {
        List<Long>  idList = new ArrayList<Long>();
        LambdaQueryWrapper<Tbemploy> lqw = Wrappers.lambdaQuery();
        lqw.eq(Tbemploy::getDeptid,depatId);
        List<Tbemploy> list = tbemployMapper.selectList(lqw);
        for( int i = 0; i < list.size(); i++ ) {
            idList.add(list.get(i).getId());
        }
        return  idList;
    }

}


