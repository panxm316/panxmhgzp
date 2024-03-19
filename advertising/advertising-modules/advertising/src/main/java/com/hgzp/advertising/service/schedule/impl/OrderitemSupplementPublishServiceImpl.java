package com.hgzp.advertising.service.schedule.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.emnus.approve.ApproveStatus;
import com.hgzp.advertising.emnus.schedule.PublishStatus;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.pagemodel.schedule.dto.OrderArrangeIdDTO;
import com.hgzp.advertising.pagemodel.schedule.dto.OrderItemBatchArrangeDTO;
import com.hgzp.advertising.pagemodel.schedule.dto.OrderitemSupplementPublishReq;
import com.hgzp.advertising.pagemodel.schedule.vo.OrderitemarrangeVO;
import com.hgzp.advertising.service.ad.TworderServiceI;
import com.hgzp.advertising.service.ad.TworderitemServiceI;
import com.hgzp.advertising.service.media.TbmediapublicnumServiceI;
import com.hgzp.advertising.service.schedule.OrderitemSupplementPublishServiceI;
import com.hgzp.core.model.*;
import com.hgzp.mapper.ad.TworderitemMapper;
import com.hgzp.mapper.schedule.TworderitemarrangeMapper;
import com.hgzp.service.common.impl.MyServiceImpl;
import com.hgzp.utils.WebUtil;
import com.hgzp.utils.model.LoginUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * OrderitemSupplementPublishServiceImpl
 * 创建人：songly
 * 类描述：补刊业务
 * 创建日期：2023/12/23 14:39
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class OrderitemSupplementPublishServiceImpl extends MyServiceImpl<TworderitemarrangeMapper,
        Tworderitemarrange> implements OrderitemSupplementPublishServiceI {
    @Autowired
    private TworderitemarrangeMapper orderitemarrangeMapper;
    @Autowired
    private TworderitemMapper orderitemMapper;
    @Autowired
    private TworderServiceI orderServiceI;
    @Autowired
    private TworderitemServiceI orderitemServiceI;
    @Autowired
    private TbmediapublicnumServiceI mediapublicnumServiceI;
    @Resource
    private HgDataChangeRecorderInnerInterceptor innerInterceptor;

    @Override
    public IPage<OrderitemarrangeVO> getOrderitemsupplementpublishPageList(Page<Tworderitem> page,
                                                                           OrderitemSupplementPublishReq query) {
        LambdaQueryWrapper<Tworderitem> lqw = Wrappers.lambdaQuery();
        //多个关键字以空格间隔  广告标题，内容
        if (ObjectUtil.isNotNull(query.getQueryKey())) {
            String[] keywords = query.getQueryKey().trim().split(" ");
            lqw.and(item -> {
                for (String skey : keywords) {
                    item.or(StrUtil.isNotBlank(skey), i -> i.like(Tworderitem::getSadtitle, skey)
                            .or().like(Tworderitem::getSadcontent, skey)
                    );
                }
            });
        }

        lqw.eq(ObjectUtil.isNotNull(query.getMediaid()), Tworderitem::getMediaid, query.getMediaid())
                .eq(ObjectUtil.isNotNull(query.getFoldid()), Tworderitem::getFoldid, query.getFoldid())
                .eq(ObjectUtil.isNotNull(query.getFoldareaverid()), Tworderitem::getFoldareaverid,
                        query.getFoldareaverid())
                .eq(ObjectUtil.isNotNull(query.getScontractnum()), Tworderitem::getScontractnum,
                        query.getScontractnum())
                .eq(Tworderitem::getBdelete, 0)
                .ge(query.getStartTime() != null, Tworderitem::getPrestartdate, query.getStartTime());
        if (query.getEndTime() != null) {
            lqw.le(Tworderitem::getPrestartdate, query.getEndTime());
        }
        if (ObjectUtil.isNull(query.getIbooktype())) {
            lqw.and(i -> i.eq(Tworderitem::getIbooktype, 2)
                    .or(item -> item.eq(Tworderitem::getIbooktype, 3).eq(Tworderitem::getIapprovestatus,
                            ApproveStatus.APPROVE_PASS.getKey())));
        } else {
            lqw.eq(Tworderitem::getIbooktype, query.getIbooktype());
            if (query.getIbooktype() == 3) {
                lqw.eq(Tworderitem::getIpublishstatus, ApproveStatus.APPROVE_PASS.getKey());
            }
        }

        lqw.orderByAsc(Tworderitem::getPrestartdate);
        IPage<Tworderitem> lsResult = orderitemMapper.selectPage(page, lqw);

        IPage<OrderitemarrangeVO> arrangePageVo = new Page<>();
        List<OrderitemarrangeVO> lsarrangeVo = new ArrayList<>();
        for (Tworderitem record : lsResult.getRecords()) {
            Tworder tworder = orderServiceI.getById(record.getOrderid());
            List<Tworderitemarrange> lstworderitemarrange =
                    orderitemarrangeMapper.selectList(Wrappers.<Tworderitemarrange>lambdaQuery()
                            .eq(Tworderitemarrange::getOrderitemid, record.getId())
                            .eq(Tworderitemarrange::getOrderid, record.getOrderid())
                    );
            if (lstworderitemarrange.size() > 0) {
                for (Tworderitemarrange item : lstworderitemarrange) {
                    OrderitemarrangeVO arrangeVo = new OrderitemarrangeVO();
                    BeanUtils.copyProperties(item, arrangeVo);
                    arrangeVo.setScontractnum(tworder.getScontractnum());
                    arrangeVo.setCustomerid(tworder.getCustomerid());
                    arrangeVo.setCustomername(tworder.getCustomername());
                    arrangeVo.setAdtypeid(tworder.getAdtypeid());
                    arrangeVo.setAdtypename(tworder.getAdtypename());

                    arrangeVo.setIpublishstatus(record.getIpublishstatus());
                    arrangeVo.setSpublishstatusName(PublishStatus.getNameByKey(record.getIpublishstatus()));
                    arrangeVo.setPresremark(record.getSremark());
                    //主业务员 tworderitembelong 中可能多个主业务员，需取订单中的主业务员
                    arrangeVo.setEmployid(tworder.getEmployid());
                    arrangeVo.setEmployname(tworder.getEmployname());
                    //预刊发
                    arrangeVo.setPrefoldid(record.getFoldid());
                    arrangeVo.setPrefoldname(record.getFoldname());
                    arrangeVo.setPrefoldareaverid(record.getFoldareaverid());
                    arrangeVo.setPrefoldareavername(record.getFoldareavername());
                    arrangeVo.setPrefoldpageplanid(record.getFoldpageplanid());
                    if (StrUtil.isNotBlank(record.getFoldpageplanname())) {
                        arrangeVo.setPrefoldpageplanname(Integer.valueOf(record.getFoldpageplanname()));
                    }
                    lsarrangeVo.add(arrangeVo);
                }
            } else {
                OrderitemarrangeVO arrangeVo = new OrderitemarrangeVO();
                arrangeVo.setOrderid(record.getOrderid());
                arrangeVo.setOrderitemid(record.getId());

                arrangeVo.setMediatypekey(record.getMediatypekey());
                arrangeVo.setMediatypename(record.getMediatypename());
                arrangeVo.setMediaid(record.getMediaid());
                arrangeVo.setMedianame(record.getMedianame());
                arrangeVo.setDpublishdate(record.getPrestartdate());

                arrangeVo.setAdcolorid(record.getAdcolorid());
                arrangeVo.setAdcolorname(record.getAdcolorname());
                arrangeVo.setPagesortid(record.getPagesortid());
                arrangeVo.setPagesortname(record.getPagesortname());
                //规格
                arrangeVo.setAdspecid(record.getAdspecid());
                arrangeVo.setAdspecname(record.getAdspecname());
                arrangeVo.setNwidth(record.getNwidth());
                arrangeVo.setNheight(record.getNheight());

                arrangeVo.setSadtitle(record.getSadtitle());
                arrangeVo.setSadcontent(record.getSadcontent());

                arrangeVo.setScontractnum(tworder.getScontractnum());
                arrangeVo.setCustomerid(tworder.getCustomerid());
                arrangeVo.setCustomername(tworder.getCustomername());
                arrangeVo.setAdtypeid(tworder.getAdtypeid());
                arrangeVo.setAdtypename(tworder.getAdtypename());

                arrangeVo.setIpublishstatus(record.getIpublishstatus());
                arrangeVo.setSpublishstatusName(PublishStatus.getNameByKey(record.getIpublishstatus()));
                arrangeVo.setPresremark(record.getSremark());
                //主业务员 tworderitembelong 中可能多个主业务员，需取订单中的主业务员
                arrangeVo.setEmployid(tworder.getEmployid());
                arrangeVo.setEmployname(tworder.getEmployname());
                //预刊发
                arrangeVo.setPrefoldid(record.getFoldid());
                arrangeVo.setPrefoldname(record.getFoldname());
                arrangeVo.setPrefoldareaverid(record.getFoldareaverid());
                arrangeVo.setPrefoldareavername(record.getFoldareavername());
                arrangeVo.setPrefoldpageplanid(record.getFoldpageplanid());
                if (StrUtil.isNotBlank(record.getFoldpageplanname())) {
                    arrangeVo.setPrefoldpageplanname(Integer.valueOf(record.getFoldpageplanname()));
                }
                lsarrangeVo.add(arrangeVo);
            }
        }
        arrangePageVo.setRecords(lsarrangeVo);
        arrangePageVo.setTotal(lsResult.getTotal());
        return arrangePageVo;
    }

    @Override
    public OrderitemarrangeVO getOrderitemsupplementpublishById(String id) {
        Tworderitemarrange tworderitemarrange = orderitemarrangeMapper.selectById(id);

        OrderitemarrangeVO arrangeVo = new OrderitemarrangeVO();
        BeanUtils.copyProperties(tworderitemarrange, arrangeVo);
        //订单信息
        Tworder tworder = orderServiceI.getById(tworderitemarrange.getOrderid());
        arrangeVo.setScontractnum(tworder.getScontractnum());
        arrangeVo.setCustomerid(tworder.getCustomerid());
        arrangeVo.setCustomername(tworder.getCustomername());
        arrangeVo.setAdtypeid(tworder.getAdtypeid());
        arrangeVo.setAdtypename(tworder.getAdtypename());
        //主业务员
        arrangeVo.setEmployid(tworder.getEmployid());
        arrangeVo.setEmployname(tworder.getEmployname());
        //发布状态
        Tworderitem tworderitem = orderitemServiceI.getById(tworderitemarrange.getOrderitemid());
        if (ObjectUtil.isNotNull(tworderitem)) {
            arrangeVo.setIpublishstatus(tworderitem.getIpublishstatus());
            arrangeVo.setSpublishstatusName(PublishStatus.getNameByKey(tworderitem.getIpublishstatus()));
            arrangeVo.setPresremark(tworderitem.getSremark());

            //预刊发
            arrangeVo.setPrefoldid(tworderitem.getFoldid());
            arrangeVo.setPrefoldname(tworderitem.getFoldname());
            arrangeVo.setPrefoldareaverid(tworderitem.getFoldareaverid());
            arrangeVo.setPrefoldareavername(tworderitem.getFoldareavername());
            arrangeVo.setPrefoldpageplanid(tworderitem.getFoldpageplanid());
            arrangeVo.setPrefoldpageplanname(Integer.valueOf(tworderitem.getFoldpageplanname()));
        }
        return arrangeVo;
    }

    @Override
    public List<OrderitemarrangeVO> getOrderitemsupplementpublishlist(OrderitemSupplementPublishReq query) {
        //先查询订单明细信息
        LambdaQueryWrapper<Tworderitem> lqw = Wrappers.lambdaQuery();
        //多个关键字以空格间隔  广告标题，内容
        if (ObjectUtil.isNotNull(query.getQueryKey())) {
            String[] keywords = query.getQueryKey().trim().split(" ");
            lqw.and(item -> {
                for (String skey : keywords) {
                    item.or(StrUtil.isNotBlank(skey), i -> i.like(Tworderitem::getSadtitle, skey)
                            .or().like(Tworderitem::getSadcontent, skey)
                    );
                }
            });
        }
        lqw.eq(ObjectUtil.isNotNull(query.getMediaid()), Tworderitem::getMediaid, query.getMediaid())
                .eq(ObjectUtil.isNotNull(query.getFoldid()), Tworderitem::getFoldid, query.getFoldid())
                .eq(ObjectUtil.isNotNull(query.getFoldareaverid()), Tworderitem::getFoldareaverid,
                        query.getFoldareaverid())
                .eq(ObjectUtil.isNotNull(query.getScontractnum()), Tworderitem::getScontractnum,
                        query.getScontractnum())
                .ge(query.getStartTime() != null, Tworderitem::getPrestartdate, query.getStartTime());
        if (query.getEndTime() != null) {
            lqw.le(Tworderitem::getPrestartdate, query.getEndTime());
        }
        if (ObjectUtil.isNull(query.getIbooktype())) {
            lqw.and(i -> i.eq(Tworderitem::getIbooktype, 2).or().eq(Tworderitem::getIbooktype, 3));
        } else {
            lqw.eq(Tworderitem::getIbooktype, query.getIbooktype());
        }
        lqw.orderByAsc(Tworderitem::getPrestartdate);

        List<Tworderitem> lsOrderitem = orderitemServiceI.list(lqw);

        List<OrderitemarrangeVO> lsarrangeVo = new ArrayList<>();
        for (Tworderitem record : lsOrderitem) {
            Tworder tworder = orderServiceI.getById(record.getOrderid());

            //一个订单明细可以重复安排
            List<Tworderitemarrange> lstworderitemarrange =
                    orderitemarrangeMapper.selectList(Wrappers.<Tworderitemarrange>lambdaQuery()
                            .eq(Tworderitemarrange::getOrderitemid, record.getId())
                            .eq(Tworderitemarrange::getOrderid, record.getOrderid())
                    );
            if (lstworderitemarrange.size() > 0) {
                for (Tworderitemarrange item : lstworderitemarrange) {
                    OrderitemarrangeVO arrangeVo = new OrderitemarrangeVO();
                    BeanUtils.copyProperties(item, arrangeVo);
                    arrangeVo.setScontractnum(tworder.getScontractnum());
                    arrangeVo.setCustomerid(tworder.getCustomerid());
                    arrangeVo.setCustomername(tworder.getCustomername());
                    arrangeVo.setAdtypeid(tworder.getAdtypeid());
                    arrangeVo.setAdtypename(tworder.getAdtypename());

                    arrangeVo.setIpublishstatus(record.getIpublishstatus());
                    arrangeVo.setSpublishstatusName(PublishStatus.getNameByKey(record.getIpublishstatus()));
                    arrangeVo.setPresremark(record.getSremark());
                    //主业务员
                    arrangeVo.setEmployid(tworder.getEmployid());
                    arrangeVo.setEmployname(tworder.getEmployname());
                    //预刊发
                    arrangeVo.setPrefoldid(record.getFoldid());
                    arrangeVo.setPrefoldname(record.getFoldname());
                    arrangeVo.setPrefoldareaverid(record.getFoldareaverid());
                    arrangeVo.setPrefoldareavername(record.getFoldareavername());
                    arrangeVo.setPrefoldpageplanid(record.getFoldpageplanid());
                    arrangeVo.setPrefoldpageplanname(Integer.valueOf(record.getFoldpageplanname()));

                    lsarrangeVo.add(arrangeVo);
                }
            } else {
                OrderitemarrangeVO arrangeVo = new OrderitemarrangeVO();
                arrangeVo.setOrderid(record.getOrderid());
                arrangeVo.setOrderitemid(record.getId());

                arrangeVo.setMediatypekey(record.getMediatypekey());
                arrangeVo.setMediatypename(record.getMediatypename());
                arrangeVo.setMediaid(record.getMediaid());
                arrangeVo.setMedianame(record.getMedianame());
                arrangeVo.setDpublishdate(record.getPrestartdate());

                arrangeVo.setAdcolorid(record.getAdcolorid());
                arrangeVo.setAdcolorname(record.getAdcolorname());
                arrangeVo.setPagesortid(record.getPagesortid());
                arrangeVo.setPagesortname(record.getPagesortname());
                //规格
                arrangeVo.setAdspecid(record.getAdspecid());
                arrangeVo.setAdspecname(record.getAdspecname());
                arrangeVo.setNwidth(record.getNwidth());
                arrangeVo.setNheight(record.getNheight());

                arrangeVo.setSadtitle(record.getSadtitle());
                arrangeVo.setSadcontent(record.getSadcontent());

                arrangeVo.setScontractnum(tworder.getScontractnum());
                arrangeVo.setCustomerid(tworder.getCustomerid());
                arrangeVo.setCustomername(tworder.getCustomername());
                arrangeVo.setAdtypeid(tworder.getAdtypeid());
                arrangeVo.setAdtypename(tworder.getAdtypename());

                arrangeVo.setIpublishstatus(record.getIpublishstatus());
                arrangeVo.setSpublishstatusName(PublishStatus.getNameByKey(record.getIpublishstatus()));
                arrangeVo.setPresremark(record.getSremark());
                //主业务员
                arrangeVo.setEmployid(tworder.getEmployid());
                arrangeVo.setEmployname(tworder.getEmployname());
                //预刊发
                arrangeVo.setPrefoldid(record.getFoldid());
                arrangeVo.setPrefoldname(record.getFoldname());
                arrangeVo.setPrefoldareaverid(record.getFoldareaverid());
                arrangeVo.setPrefoldareavername(record.getFoldareavername());
                arrangeVo.setPrefoldpageplanid(record.getFoldpageplanid());
                arrangeVo.setPrefoldpageplanname(Integer.valueOf(record.getFoldpageplanname()));

                lsarrangeVo.add(arrangeVo);
            }
        }
        return lsarrangeVo;
    }

    @Override
    public void bacthOrderitemsupplementpublish(OrderItemBatchArrangeDTO orderItemBatchArrangeDto) throws Exception {
        if (ObjectUtil.isNull(orderItemBatchArrangeDto)) {
            throw new Exception("参数不能为空！");
        }
        if (orderItemBatchArrangeDto.getOrderitemids().size() == 0) {
            throw new Exception("订单明细id不能为空！");
        }

        LoginUser user = WebUtil.getLoginUser();

        for (OrderArrangeIdDTO item : orderItemBatchArrangeDto.getOrderitemids()) {
            //获取订单明细信息
            Tworderitem tworderitem = orderitemServiceI.getById(item.getOrderitemid());
            if (ObjectUtil.isNull(tworderitem)) {
                continue;
            }

            Tworder tworder = orderServiceI.getById(item.getOrderid());
            if (ObjectUtil.isNull(tworder)) {
                continue;
            }

            Tworderitemarrange tworderitemarrange = getById(item.getId());
            //已见报的订单明细更新
            if (ObjectUtil.isNotNull(tworderitemarrange)) {
                tworderitemarrange.setVersion(item.getVersion());
                if (ObjectUtil.isNotNull(orderItemBatchArrangeDto.getFoldid())) {
                    tworderitemarrange.setFoldid(orderItemBatchArrangeDto.getFoldid());
                    tworderitemarrange.setFoldname(orderItemBatchArrangeDto.getFoldname());
                } else {
                    tworderitemarrange.setFoldid(tworderitem.getFoldid());
                    tworderitemarrange.setFoldname(tworderitem.getFoldname());
                }
                if (ObjectUtil.isNotNull(orderItemBatchArrangeDto.getFoldareaverid())) {
                    tworderitemarrange.setFoldareaverid(orderItemBatchArrangeDto.getFoldareaverid());
                    tworderitemarrange.setFoldareavername(orderItemBatchArrangeDto.getFoldareavername());
                } else {
                    tworderitemarrange.setFoldareaverid(tworderitem.getFoldareaverid());
                    tworderitemarrange.setFoldareavername(tworderitem.getFoldareavername());
                }
                if (ObjectUtil.isNotNull(orderItemBatchArrangeDto.getFoldpageplanid())) {
                    tworderitemarrange.setFoldpageplanid(orderItemBatchArrangeDto.getFoldpageplanid());
                    if (StrUtil.isNotBlank(orderItemBatchArrangeDto.getFoldpageplanname())) {
                        tworderitemarrange.setFoldpageplanname(Integer.valueOf(orderItemBatchArrangeDto.getFoldpageplanname()));
                    }
                } else {
                    tworderitemarrange.setFoldpageplanid(tworderitem.getFoldpageplanid());
                    if (StrUtil.isNotBlank(tworderitem.getFoldpageplanname())) {
                        tworderitemarrange.setFoldpageplanname(Integer.valueOf(tworderitem.getFoldpageplanname()));
                    }
                }

                if (StrUtil.isNotBlank(orderItemBatchArrangeDto.getFoldpageplanname())) {
                    tworderitemarrange.setFoldpageplanname(Integer.valueOf(orderItemBatchArrangeDto.getFoldpageplanname()));
                }
                tworderitemarrange.setLastoperatorid(user.getUserid());
                tworderitemarrange.setLastoperator(user.getUsername());
                tworderitemarrange.setDlastmodifydate(DateUtil.date());

                innerInterceptor.recoredLog();
                if (orderitemarrangeMapper.updateById(tworderitemarrange) == 0) {
                    throw new OptimisticLockingFailureException("数据已改变，请刷新后再重新操作！");
                }
            } else {
                tworderitemarrange = new Tworderitemarrange();
                tworderitemarrange.setOrderid(tworderitem.getOrderid());
                tworderitemarrange.setCreateempid(user.getUserid());
                tworderitemarrange.setCreateempname(user.getUsername());
                tworderitemarrange.setDcreatetime(DateUtil.date());
                tworderitemarrange.setOrderitemid(Long.valueOf(item.getOrderitemid()));
                tworderitemarrange.setMediatypekey(tworderitem.getMediatypekey());
                tworderitemarrange.setMediatypename(tworderitem.getMediatypename());
                tworderitemarrange.setMediaid(tworderitem.getMediaid());
                tworderitemarrange.setMedianame(tworderitem.getMedianame());
                tworderitemarrange.setDpublishdate(tworderitem.getPrestartdate());
                //批量确认补刊，需设置目标版面id
                if (ObjectUtil.isNotNull(orderItemBatchArrangeDto.getFoldid())) {
                    tworderitemarrange.setFoldid(orderItemBatchArrangeDto.getFoldid());
                    tworderitemarrange.setFoldname(orderItemBatchArrangeDto.getFoldname());
                } else {
                    tworderitemarrange.setFoldid(tworderitem.getFoldid());
                    tworderitemarrange.setFoldname(tworderitem.getFoldname());
                }
                if (ObjectUtil.isNotNull(orderItemBatchArrangeDto.getFoldareaverid())) {
                    tworderitemarrange.setFoldareaverid(orderItemBatchArrangeDto.getFoldareaverid());
                    tworderitemarrange.setFoldareavername(orderItemBatchArrangeDto.getFoldareavername());
                } else {
                    tworderitemarrange.setFoldareaverid(tworderitem.getFoldareaverid());
                    tworderitemarrange.setFoldareavername(tworderitem.getFoldareavername());
                }
                if (ObjectUtil.isNotNull(orderItemBatchArrangeDto.getFoldpageplanid())) {
                    tworderitemarrange.setFoldpageplanid(orderItemBatchArrangeDto.getFoldpageplanid());
                    if (StrUtil.isNotBlank(orderItemBatchArrangeDto.getFoldpageplanname())) {
                        tworderitemarrange.setFoldpageplanname(Integer.valueOf(orderItemBatchArrangeDto.getFoldpageplanname()));
                    }
                } else {
                    tworderitemarrange.setFoldpageplanid(tworderitem.getFoldpageplanid());
                    if (StrUtil.isNotBlank(tworderitem.getFoldpageplanname())) {
                        tworderitemarrange.setFoldpageplanname(Integer.valueOf(tworderitem.getFoldpageplanname()));
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
                //查询刊期
                if ("paper".equals(tworderitem.getMediatypekey())) {
                    String sPublishNo = mediapublicnumServiceI.getMediaPublishNO(tworderitem.getMediaid(),
                            tworderitem.getPrestartdate());
                    if (StrUtil.isNotBlank(sPublishNo)) {
                        tworderitemarrange.setPublishnum(Integer.valueOf(sPublishNo));
                    }
                }

                tworderitemarrange.setSadtitle(tworderitem.getSadtitle());
                tworderitemarrange.setSadcontent(tworderitem.getSadcontent());
                tworderitemarrange.setEditorid(orderItemBatchArrangeDto.getEditorid());
                tworderitemarrange.setEditorname(orderItemBatchArrangeDto.getEditorname());
                tworderitemarrange.setLastoperatorid(user.getUserid());
                tworderitemarrange.setLastoperator(user.getUsername());
                tworderitemarrange.setDlastmodifydate(DateUtil.date());
                tworderitemarrange.setVersion(0L);

                innerInterceptor.recoredLog();
                save(tworderitemarrange);
            }
            //更新订单明细状态
//            if ("paper".equals(tworderitem.getMediatypekey())) {
//                //报刊 更新订单明细状态为见报（4）
//                tworderitem.setIpublishstatus(PublishStatus.PUBLISH_End.getKey());
//            } else {
            //非报刊 更新订单明细状态为已发布（5）
            tworderitem.setIpublishstatus(PublishStatus.PUBLISH_Published.getKey());
//            }
            innerInterceptor.recoredLog();
            if (orderitemMapper.updateById(tworderitem) == 0) {
                throw new OptimisticLockingFailureException("数据已改变，请刷新后再重新操作！");
            }

        }
    }

    @Override
    public void orderitemsupplementRecall(String id) throws Exception {
        if (StrUtil.isBlank(id)) {
            throw new Exception("参数不能为空！");
        }
        Tworderitem tworderitem = orderitemServiceI.getById(id);
        if (ObjectUtil.isNull(tworderitem)) {
            throw new Exception("订单明细不存在！");
        }

        //删除订单明细安排信息
        List<Long> lsIds = new LambdaQueryChainWrapper<>(orderitemarrangeMapper)
                .eq(Tworderitemarrange::getOrderitemid, id)
                .list()
                .stream()
                .map(Tworderitemarrange::getId)
                .distinct()
                .collect(Collectors.toList());
        if (lsIds.size() > 0) {
            innerInterceptor.recoredLog();
            orderitemMapper.deleteBatchIds(lsIds);
        }

        //更新订单明细状态
        // if ("paper".equals(tworderitem.getMediatypekey())) {
        //报刊 更新订单明细状态为预定（1）
        tworderitem.setIpublishstatus(PublishStatus.PUBLISH_Reserve.getKey());
//        } else {
//            //非报刊 更新订单明细状态为待发布（2）
//            tworderitem.setIpublishstatus(PublishStatus.PUBLISH_Tobereleased.getKey());
//        }
        innerInterceptor.recoredLog();
        if (orderitemMapper.updateById(tworderitem) == 0) {
            throw new OptimisticLockingFailureException("数据已改变，请刷新后再重新操作！");
        }

    }

    @Override
    public void addOrderitemByItemId(String id) throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        //加刊 新增加一条广告明细，状态为待审， 加刊审批通过后审批状态 为通过）
        Tworderitem tworderitem = orderitemServiceI.getById(id);
        if (ObjectUtil.isNull(tworderitem)) {
            throw new Exception("订单明细不存在！");
        }
        //复制订单明细信息
        Date curDate = DateUtil.date();
        Long newId = IdUtil.getSnowflakeNextId();
        Tworderitem tworderitemNew = new Tworderitem();
        BeanUtils.copyProperties(tworderitem, tworderitemNew);
        tworderitemNew.setId(newId);
        tworderitemNew.setPrestartdate(DateUtil.offsetDay(curDate, 1));
        tworderitemNew.setPreenddate(DateUtil.offsetDay(curDate, 2));
        tworderitemNew.setCreatetime(curDate);
        tworderitemNew.setCreateempid(user.getUserid());
        tworderitemNew.setCreateempname(user.getUsername());
        tworderitemNew.setIaddapprovestatus(ApproveStatus.APPROVE_EDIT.getKey());
        tworderitemNew.setIapprovestatus(ApproveStatus.APPROVE_EDIT.getKey());
        tworderitemNew.setBuse(true);
        tworderitemNew.setBdelete(false);
        orderitemServiceI.save(tworderitemNew);
    }

    @Override
    public void ChangeOrderitemByItemId(String id) throws Exception {
        //改刊（只能选择一条广告明细记录进行改刊，改刊后原记录设置为无效，新增加一条新的广告明细，提交审批后，两条记录的改刊状态皆为审批流程相应的状态（待审，在审，通过。。。。））
        LoginUser user = WebUtil.getLoginUser();
        //
        Tworderitem tworderitem = orderitemServiceI.getById(id);

        if (ObjectUtil.isNull(tworderitem)) {
            throw new Exception("订单明细不存在！");
        }
        if (tworderitem.getAmountreceived().compareTo(BigDecimal.ZERO) > 0) {
            throw new Exception("订单明细有平账信息不能改刊！");
        }

        Date curDate = DateUtil.date();
        Long newId = IdUtil.getSnowflakeNextId();
        //新增一条新的订单明细
        Tworderitem tworderitemNew = new Tworderitem();
        BeanUtils.copyProperties(tworderitem, tworderitemNew);
        tworderitemNew.setId(newId);
        tworderitemNew.setPrestartdate(DateUtil.offsetDay(curDate, 1));
        tworderitemNew.setPreenddate(DateUtil.offsetDay(curDate, 2));
        tworderitemNew.setCreatetime(curDate);
        tworderitemNew.setCreateempid(user.getUserid());
        tworderitemNew.setCreateempname(user.getUsername());
        tworderitemNew.setIchangeapprovestatus(ApproveStatus.APPROVE_EDIT.getKey());
        tworderitemNew.setIapprovestatus(ApproveStatus.APPROVE_EDIT.getKey());
        tworderitemNew.setBuse(true);
        innerInterceptor.recoredLog();
        orderitemServiceI.save(tworderitemNew);

        //改刊原来记录记住新的订单明细id
        tworderitem.setRelateorderid(newId);
        tworderitem.setIchangeapprovestatus(ApproveStatus.APPROVE_EDIT.getKey());
        tworderitem.setIapprovestatus(ApproveStatus.APPROVE_EDIT.getKey());
        innerInterceptor.recoredLog();
        if(orderitemMapper.updateById(tworderitem)==0){
            throw new OptimisticLockingFailureException("数据已改变，请刷新后再重新操作！");
        }

    }

    @Override
    public void StopOrderitemByItemId(String ids) throws Exception {
        //停刊（可以选择多条记录进行停刊，状态为待审 需审批，停刊审批通过后状态 改为无效且停刊审批状态为通过）
        LoginUser user = WebUtil.getLoginUser();
        List<String> idList = Arrays.asList(ids.split(","));
        if (idList.size() == 0) {
            throw new Exception("参数不能为空！");
        }

        List<Tworderitem> lsorderitem = orderitemMapper.selectBatchIds(idList);
        if (lsorderitem.size() == 0) {
            throw new Exception("订单明细不存在！");
        }
        lsorderitem.forEach(item -> {
            item.setIstopapprovestatus(ApproveStatus.APPROVE_EDIT.getKey());
            item.setIapprovestatus(ApproveStatus.APPROVE_EDIT.getKey());
            innerInterceptor.recoredLog();
            if(orderitemMapper.updateById(item)==0){
                throw new OptimisticLockingFailureException("数据已改变，请刷新后再重新操作！");
            }
        });

    }

}