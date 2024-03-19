package com.hgzp.advertising.service.schedule.impl;

import cn.hutool.core.date.DateUtil;
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
import com.hgzp.advertising.pagemodel.ad.vo.OrderItemMarrangePosReq;
import com.hgzp.advertising.pagemodel.schedule.dto.OrderArrangeIdDTO;
import com.hgzp.advertising.pagemodel.schedule.dto.OrderItemBatchArrangeDTO;
import com.hgzp.advertising.pagemodel.schedule.dto.OrderitemarrangeReq;
import com.hgzp.advertising.pagemodel.schedule.vo.FoldPageplanReq;
import com.hgzp.advertising.pagemodel.schedule.vo.OrderitemarrangeVO;
import com.hgzp.advertising.pagemodel.schedule.vo.PageOrderItemArrangeVo;
import com.hgzp.advertising.pagemodel.schedule.vo.PageOrderItemMarrangeDetail;
import com.hgzp.advertising.service.ad.TworderServiceI;
import com.hgzp.advertising.service.ad.TworderitemServiceI;
import com.hgzp.advertising.service.media.TbfoldServiceI;
import com.hgzp.advertising.service.media.TbfoldareaverServiceI;
import com.hgzp.advertising.service.media.TbmediapublicnumServiceI;
import com.hgzp.advertising.service.schedule.OrderitemarrangeServiceI;
import com.hgzp.advertising.service.schedule.TbfoldpageplanServiceI;
import com.hgzp.core.model.*;
import com.hgzp.core.page.DataCombo;
import com.hgzp.core.page.Json;
import com.hgzp.mapper.ad.TbadtypeMapper;
import com.hgzp.mapper.ad.TworderitemMapper;
import com.hgzp.mapper.price.TbaddisplayformMapper;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 广告安排表 服务实现类
 * </p>
 *
 * @author muyn
 * @since 2023-12-06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class OrderitemarrangeServiceImpl extends MyServiceImpl<TworderitemarrangeMapper, Tworderitemarrange> implements OrderitemarrangeServiceI {

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
    @Autowired
    private TbfoldpageplanServiceI foldpageplanServiceI;
    @Autowired
    private TbadtypeMapper tbadtypeMapper;
    @Autowired
    private TbaddisplayformMapper tbaddisplayformMapper;
    @Resource
    private HgDataChangeRecorderInnerInterceptor innerInterceptor;
    @Autowired
    private TbfoldServiceI foldServiceI;
    @Autowired
    private TbfoldareaverServiceI foldareaverServiceI;

    @Override
    public void updateOrderitemarrange(Tworderitemarrange tworderitemarrange) throws Exception {
        if (orderitemarrangeMapper.updateById(tworderitemarrange) == 0) {
            throw new Exception("数据已被修改，请刷新后重试");
        }
    }

    @Override
    public List<Tworderitemarrange> getOrderitemarrangeByItemId(Long orderId, Long orderItemid) throws Exception {
        List<Tworderitemarrange> orderitemarrange = new LambdaQueryChainWrapper<>(orderitemarrangeMapper)
                .eq(Tworderitemarrange::getOrderid, orderId)
                .eq(Tworderitemarrange::getOrderitemid, orderItemid)
                .list();
        return orderitemarrange;
    }

    @Override
    public void bacthOrderitemarrange(OrderItemBatchArrangeDTO orderItemBatchArrangeDto) throws Exception {
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
            //已安排就更新
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
                tworderitemarrange.setLastoperatorid(user.getUserid());
                tworderitemarrange.setLastoperator(user.getUsername());
                tworderitemarrange.setDlastmodifydate(DateUtil.date());
                tworderitemarrange.setEditorid(orderItemBatchArrangeDto.getEditorid());
                tworderitemarrange.setEditorname(orderItemBatchArrangeDto.getEditorname());

                tworderitemarrange.setNwidth(tworderitem.getNwidth());
                tworderitemarrange.setNheight(tworderitem.getNheight());
                tworderitemarrange.setNleftx(item.getNleftx());
                tworderitemarrange.setNtopy(item.getNtopy());

                tworderitemarrange.setSremark(orderItemBatchArrangeDto.getSremark());

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
                tworderitemarrange.setNleftx(item.getNleftx());
                tworderitemarrange.setNtopy(item.getNtopy());
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
                tworderitemarrange.setSremark(orderItemBatchArrangeDto.getSremark());

                innerInterceptor.recoredLog();
                save(tworderitemarrange);
            }
            //更新订单明细状态
//            if ("paper".equals(tworderitem.getMediatypekey())) {
            //报刊 更新订单明细状态为安排（3）
            tworderitem.setIpublishstatus(PublishStatus.PUBLISH_Arrange.getKey());
//            } else {
//                //非报刊 更新订单明细状态为待发布（2）
//                tworderitem.setIpublishstatus(PublishStatus.PUBLISH_Tobereleased.getKey());
//            }
            innerInterceptor.recoredLog();
            if (orderitemMapper.updateById(tworderitem) == 0) {
                throw new OptimisticLockingFailureException("数据已改变，请刷新后再重新操作！");
            }
        }

    }

    @Override
    public OrderitemarrangeVO getOrderitemarrangeById(String id) {
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
    public List<OrderitemarrangeVO> getOrderitemarrangelist(OrderitemarrangeReq query) {
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
        lqw.eq(ObjectUtil.isNotNull(query.getMediatypekey()), Tworderitem::getMediatypekey,
                        query.getMediatypekey())
                .eq(ObjectUtil.isNotNull(query.getMediaid()), Tworderitem::getMediaid, query.getMediaid())
                .eq(ObjectUtil.isNotNull(query.getFoldid()), Tworderitem::getFoldid, query.getFoldid())
                .eq(ObjectUtil.isNotNull(query.getFoldareaverid()), Tworderitem::getFoldareaverid,
                        query.getFoldareaverid())
                .eq(ObjectUtil.isNotNull(query.getDpublishdate()), Tworderitem::getPrestartdate,
                        query.getDpublishdate())
                .eq(Tworderitem::getIbooktype, 0)
                .and(ObjectUtil.isNotNull(query.getAdtypeid()), i -> i.inSql(Tworderitem::getOrderid,
                        ("select id from tworder where adtypeid = " + query.getAdtypeid())))
                .and(ObjectUtil.isNotNull(query.getEditorid()), i -> i.inSql(Tworderitem::getId,
                        ("select orderitemid from tworderitemarrange where editorid = " + query.getEditorid())))
                .eq(Tworderitem::getIbooktype, 0)
                .orderByAsc(Tworderitem::getPrestartdate);

        List<Tworderitem> lsOrderitem = orderitemServiceI.list(lqw);

        List<OrderitemarrangeVO> lsarrangeVo = new ArrayList<>();
        for (Tworderitem record : lsOrderitem) {
            //判断广告类型
            Tworder tworder = orderServiceI.getById(record.getOrderid());
//            if (ObjectUtil.isNotNull(query.getAdtypeid()) && tworder.getAdtypeid() != query.getAdtypeid()) {
//                continue;
//            }
            List<Tworderitemarrange> lstworderitemarrange =
                    orderitemarrangeMapper.selectList(Wrappers.<Tworderitemarrange>lambdaQuery()
                            .eq(Tworderitemarrange::getOrderitemid, record.getId())
                            .eq(ObjectUtil.isNotNull(query.getEditorid()), Tworderitemarrange::getEditorid,
                                    query.getEditorid())
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
    public IPage<OrderitemarrangeVO> getOrderitemarrangePageList(Page<Tworderitem> page, OrderitemarrangeReq query) {
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
                .eq(ObjectUtil.isNotNull(query.getDpublishdate()), Tworderitem::getPrestartdate,
                        query.getDpublishdate())
                .ne(Tworderitem::getBdelete, 1).ne(Tworderitem::getBuse, 0)
                //.eq(Tworderitem::getIbooktype, 0)
                .and(i -> i.eq(Tworderitem::getIbooktype, 0).or().eq(Tworderitem::getIbooktype, 1))
                .and(ObjectUtil.isNotNull(query.getEditorid()), i -> i.inSql(Tworderitem::getId,
                        ("select orderitemid from tworderitemarrange where editorid = " + query.getEditorid())));
        if (ObjectUtil.isNotNull(query.getAdtypeid())) {
            lqw.inSql(Tworderitem::getOrderid, ("select id from tworder where adtypeid = " + query.getAdtypeid()));
        }
//        else{
//            //获取分类广告Id
//            Long adtypeId = tbadtypeMapper.selectOne(Wrappers.<Tbadtype>lambdaQuery().eq(Tbadtype::getSname,
//            "分类广告")).getId();
//            lqw.inSql(Tworderitem::getOrderid, ("select id from tworder where adtypeid <> " + adtypeId));
//        }
        //广告形式过滤分类广告
        Long addisplayformId =
                tbaddisplayformMapper.selectOne(Wrappers.<Tbaddisplayform>lambdaQuery().eq(Tbaddisplayform::getSname,
                        "分类广告")).getId();
        lqw.ne(Tworderitem::getAddisplayformid, addisplayformId);
        lqw.orderByAsc(Tworderitem::getPrestartdate);
        IPage<Tworderitem> lsResult = orderitemMapper.selectPage(page, lqw);

        IPage<OrderitemarrangeVO> arrangePageVo = new Page<>();
        List<OrderitemarrangeVO> lsarrangeVo = new ArrayList<>();
        for (Tworderitem record : lsResult.getRecords()) {
            Tworder tworder = orderServiceI.getById(record.getOrderid());
            List<Tworderitemarrange> lstworderitemarrange =
                    orderitemarrangeMapper.selectList(Wrappers.<Tworderitemarrange>lambdaQuery()
                            .eq(Tworderitemarrange::getOrderitemid, record.getId())
                            .eq(ObjectUtil.isNotNull(query.getEditorid()), Tworderitemarrange::getEditorid,
                                    query.getEditorid())
                            .eq(Tworderitemarrange::getOrderid, record.getOrderid())
                    );
            if (lstworderitemarrange.size() > 0) {
                for (Tworderitemarrange item : lstworderitemarrange) {
                    OrderitemarrangeVO arrangeVo = new OrderitemarrangeVO();
                    BeanUtils.copyProperties(item, arrangeVo);
                    arrangeVo.setIbooktype(record.getIbooktype());
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
                    //获取版数
                    FoldPageplanReq foldPageplanReq = new FoldPageplanReq();
                    foldPageplanReq.setMediaid(item.getMediaid());
                    foldPageplanReq.setFoldid(item.getFoldid());
                    foldPageplanReq.setMediatypekey(item.getMediatypekey());
                    foldPageplanReq.setFoldareaverid(item.getFoldareaverid());
                    foldPageplanReq.setPublishdate(item.getDpublishdate());
                    List<DataCombo> lsPageNum = foldpageplanServiceI.getFoldPagePlanePageNum(foldPageplanReq);
                    arrangeVo.setPageNumList(lsPageNum);

                    lsarrangeVo.add(arrangeVo);
                }
            } else {
                OrderitemarrangeVO arrangeVo = new OrderitemarrangeVO();
                arrangeVo.setOrderid(record.getOrderid());
                arrangeVo.setOrderitemid(record.getId());
                arrangeVo.setIbooktype(record.getIbooktype());

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

                //广告形式
                arrangeVo.setAddisplayformid(record.getAddisplayformid());
                arrangeVo.setAddisplayformname(record.getAddisplayformname());

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
                //默认叠次版面
                arrangeVo.setFoldid(record.getFoldid());
                arrangeVo.setFoldname(record.getFoldname());
                arrangeVo.setFoldareaverid(record.getFoldareaverid());
                arrangeVo.setFoldareavername(record.getFoldareavername());
                arrangeVo.setFoldpageplanid(record.getFoldpageplanid());
                if (StrUtil.isNotBlank(record.getFoldpageplanname())) {
                    arrangeVo.setFoldpageplanname(Integer.valueOf(record.getFoldpageplanname()));
                }
                //获取版数
                FoldPageplanReq foldPageplanReq = new FoldPageplanReq();
                foldPageplanReq.setMediaid(record.getMediaid());
                foldPageplanReq.setFoldid(record.getFoldid());
                foldPageplanReq.setMediatypekey(record.getMediatypekey());
                foldPageplanReq.setFoldareaverid(record.getFoldareaverid());
                foldPageplanReq.setPublishdate(record.getPrestartdate());
                List<DataCombo> lsPageNum = foldpageplanServiceI.getFoldPagePlanePageNum(foldPageplanReq);
                arrangeVo.setPageNumList(lsPageNum);


                lsarrangeVo.add(arrangeVo);
            }
        }
        arrangePageVo.setRecords(lsarrangeVo);
        arrangePageVo.setTotal(lsResult.getTotal());
        return arrangePageVo;
    }

    @Override
    public List<PageOrderItemArrangeVo> getPageOrderItemArrangeVoList(OrderitemarrangeReq query) {
        //查询版面计划信息
        FoldPageplanReq tbfoldpageplan = new FoldPageplanReq();
        tbfoldpageplan.setMediaid(query.getMediaid());
        tbfoldpageplan.setFoldid(query.getFoldid());
        tbfoldpageplan.setFoldareaverid(query.getFoldareaverid());
        tbfoldpageplan.setPublishdate(query.getDpublishdate());
        tbfoldpageplan.setPagenum(query.getPagenum());

        List<Tbfoldpageplan> lsFoldpageplan = foldpageplanServiceI.getFoldPagePlaneList(tbfoldpageplan);

        List<PageOrderItemArrangeVo> lsarrangeVo = new ArrayList<>();
        if (lsFoldpageplan.size() == 0) {
            return lsarrangeVo;
        }
        //广告形式过滤分类广告
        Long addisplayformId =
                tbaddisplayformMapper.selectOne(Wrappers.<Tbaddisplayform>lambdaQuery().eq(Tbaddisplayform::getSname,
                        "分类广告")).getId();

        for (Tbfoldpageplan item : lsFoldpageplan) {
            PageOrderItemArrangeVo arrangeVo = new PageOrderItemArrangeVo();
            arrangeVo.setId(item.getId());
            arrangeVo.setIpagewidth(item.getIpagewidth());
            arrangeVo.setIpageheight(item.getIpageheight());
            arrangeVo.setAdcolorname(item.getAdcolorname());

            //已安排订单
            List<Tworderitemarrange> lsOrderitemarrange =
                    orderitemarrangeMapper.selectList(Wrappers.<Tworderitemarrange>lambdaQuery()
                            .eq(Tworderitemarrange::getFoldpageplanid, item.getId())
                            .eq(Tworderitemarrange::getFoldareaverid, query.getFoldareaverid())
                            .eq(Tworderitemarrange::getDpublishdate, query.getDpublishdate())
                            .eq(Tworderitemarrange::getMediaid, query.getMediaid())
                            .ne(Tworderitemarrange::getAddisplayformid, addisplayformId)
                            .and(ObjectUtil.isNotNull(query.getAdtypeid()), i -> i.inSql(Tworderitemarrange::getOrderid,
                                    ("select id from tworder where adtypeid = " + query.getAdtypeid())))
                            .and(i -> i.inSql(Tworderitemarrange::getOrderid,
                                    ("select id from tworder where bdelete <>1")))
                    );
            if (lsOrderitemarrange.size() > 0) {
                List<PageOrderItemMarrangeDetail> lsOrderitemarrangeDetail = new ArrayList<>();
                for (Tworderitemarrange itemarrange : lsOrderitemarrange) {
                    Tworderitem tworderitem = orderitemServiceI.getById(itemarrange.getOrderitemid());
                    //订单状态为预定状态的不显示
                    if (tworderitem.getIpublishstatus() == PublishStatus.PUBLISH_Reserve.getKey()) {
                        continue;
                    }
                    PageOrderItemMarrangeDetail orderitemarrangeVO = new PageOrderItemMarrangeDetail();
                    BeanUtils.copyProperties(itemarrange, orderitemarrangeVO);
                    Tworder tworder = orderServiceI.getById(itemarrange.getOrderid());
                    if (ObjectUtil.isNotNull(tworder)) {
                        orderitemarrangeVO.setIbooktype(tworder.getIbooktype());
                        orderitemarrangeVO.setScontractnum(tworder.getScontractnum());
                        orderitemarrangeVO.setEmployname(tworder.getEmployname());
                        orderitemarrangeVO.setCustomername(tworder.getCustomername());
                        orderitemarrangeVO.setAgencyemployname(tworder.getAgencyemployname());
                        orderitemarrangeVO.setAgentname(tworder.getAgentname());
                    }
                    if (ObjectUtil.isNotNull(tworderitem)) {
                        orderitemarrangeVO.setIapprovestatus(tworderitem.getIapprovestatus());
                        orderitemarrangeVO.setIapprovestatusName(ApproveStatus.getNameByKey(tworderitem.getIapprovestatus()));
                        orderitemarrangeVO.setIpublishstatus(tworderitem.getIpublishstatus());
                        orderitemarrangeVO.setIpublishstatusName(PublishStatus.getNameByKey(tworderitem.getIpublishstatus()));
                        orderitemarrangeVO.setPrefoldpageplanname(tworderitem.getFoldpageplanname());
                        orderitemarrangeVO.setPresremark(tworderitem.getSremark());
                    }
                    //补刊关联预约订单Id
                    orderitemarrangeVO.setRelateorderid(tworder.getRelateorderid());

                    lsOrderitemarrangeDetail.add(orderitemarrangeVO);
                }
                arrangeVo.setOrderitemarrangeList(lsOrderitemarrangeDetail);
            }


            lsarrangeVo.add(arrangeVo);
        }
        return lsarrangeVo;
    }

    @Override
    public void updateOrderitemarrangeStatus(String id) throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        //获取订单明细安排信息
        Tworderitemarrange orderitemarrange = this.getById(id);
        if (ObjectUtil.isNull(orderitemarrange)) {
            throw new Exception("订单明细安排信息不存在！");
        }
        //获取订单明细信息
        Tworderitem tworderitem = orderitemServiceI.getById(orderitemarrange.getOrderitemid());
        if (ObjectUtil.isNull(tworderitem)) {
            throw new Exception("订单明细信息不存在！");
        }
        //恢复预安排版面信息
        orderitemarrange.setFoldid(tworderitem.getFoldid());
        orderitemarrange.setFoldname(tworderitem.getFoldname());
        orderitemarrange.setFoldareaverid(tworderitem.getFoldareaverid());
        orderitemarrange.setFoldareavername(tworderitem.getFoldareavername());
        orderitemarrange.setFoldpageplanid(tworderitem.getFoldpageplanid());
        if (StrUtil.isNotBlank(tworderitem.getFoldpageplanname())) {
            orderitemarrange.setFoldpageplanname(Integer.valueOf(tworderitem.getFoldpageplanname()));
        }
        orderitemarrange.setLastoperatorid(user.getUserid());
        orderitemarrange.setLastoperator(user.getUsername());
        orderitemarrange.setDlastmodifydate(DateUtil.date());
        innerInterceptor.recoredLog();
        if (orderitemarrangeMapper.updateById(orderitemarrange) == 0) {
            throw new OptimisticLockingFailureException("订单安排数据已改变，请刷新后再重新操作！");
        }

        //更新订单明细状态为预定（1）
        tworderitem.setIpublishstatus(PublishStatus.PUBLISH_Reserve.getKey());
        innerInterceptor.recoredLog();
        if (orderitemMapper.updateById(tworderitem) == 0) {
            throw new OptimisticLockingFailureException("订单明细数据已改变，请刷新后再重新操作！");
        }
    }

    @Override
    public void updateOrderitemarrangePos(OrderItemMarrangePosReq marrangePosReq) throws Exception {
        Tworderitemarrange orderitemarrange = this.getById(marrangePosReq.getId());
        if (ObjectUtil.isNull(orderitemarrange)) {
            throw new Exception("订单明细安排信息不存在！");
        }
        orderitemarrange.setNleftx(marrangePosReq.getNleftx());
        orderitemarrange.setNtopy(marrangePosReq.getNtopy());

        innerInterceptor.recoredLog();
        if (orderitemarrangeMapper.updateById(orderitemarrange) == 0) {
            throw new OptimisticLockingFailureException("数据已改变，请刷新后再重新操作！");
        }
    }

    @Override
    public void updateOrderitemPublishStatus(String ids, Boolean bPublish) throws Exception {
        List<Long> orderItemIdList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        //订单明细
        List<Tworderitem> orderItems = orderitemMapper.selectBatchIds(orderItemIdList);
        if (orderItems.size() > 0) {
            orderItems.forEach(item -> {
                if (bPublish) {
                    //发布
                    item.setIpublishstatus(PublishStatus.PUBLISH_Published.getKey());
                } else {
                    //取消发布
//                    if ("paper".equals(item.getMediatypekey())) {
//                        //报刊 更新订单明细状态为安排（3）
                    item.setIpublishstatus(PublishStatus.PUBLISH_Arrange.getKey());
//                    } else {
//                        //非报刊 更新订单明细状态为待发布（2）
//                        item.setIpublishstatus(PublishStatus.PUBLISH_Tobereleased.getKey());
//                    }
                }
                innerInterceptor.recoredLog();
                if (orderitemMapper.updateById(item) == 0) {
                    throw new OptimisticLockingFailureException("数据已改变，请刷新后再重新操作！");
                }
            });
        }
    }


    @Override
    public Json updateItemArrangeByCJ(String inforid, String pubDate, String pubStatus, String issueDate, String issueFolder, String foldVersion, String issuePage) {
        LoginUser user;
        try {
            user = WebUtil.getLoginUser();
        } catch (Exception e) {
            return Json.fail("请先登录！");
        }
        List<Tworderitem> lsOrderitem = orderitemMapper.selectList(Wrappers.<Tworderitem>lambdaQuery()
                        .eq(Tworderitem::getSordernum, inforid)
                        .ge(Tworderitem::getPrestartdate, pubDate)
//                .eq(StringUtils.isNotEmpty(issueFolder), Tworderitem::getFoldid, issueFolder)
//                .eq(StringUtils.isNotEmpty(foldVersion), Tworderitem::getFoldareaverid, foldVersion)
                        .eq(Tworderitem::getAddisplayformname, "分类广告")
        );
        for (Tworderitem record : lsOrderitem) {

            Tworderitem tworderitem = record;
            if (tworderitem == null) {
                return Json.fail("未找到订单明细！");
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//            Date dPubDate;
//            Date dIssueDate;
//            try {
//                dPubDate = sdf.parse(pubDate);
//                dIssueDate = sdf.parse(issueDate);
//            } catch (Exception e) {
////                return Json.fail("刊发日期格式错误！");
//            }

            if ("见报".equals(pubStatus)) {
                pubStatus = "已发布";
            }
            PublishStatus.getKeyByName(pubStatus);
            tworderitem.setIpublishstatus(PublishStatus.getKeyByName(pubStatus));

            Tworderitemarrange tworderitemarrange = orderitemarrangeMapper.selectOne(Wrappers.<Tworderitemarrange>lambdaQuery()
                    .eq(Tworderitemarrange::getOrderitemid, tworderitem.getId())
            );
            boolean bNew = false;
            if (tworderitemarrange == null) {
                bNew = true;
                tworderitemarrange = new Tworderitemarrange();
                BeanUtils.copyProperties(tworderitem, tworderitemarrange);
                tworderitemarrange.setOrderitemid(tworderitem.getId());
                tworderitemarrange.setCreateempid(user.getUserid());
                tworderitemarrange.setCreateempname(user.getUsername());
                tworderitemarrange.setDcreatetime(DateUtil.date());
                tworderitemarrange.setVersion(0L);
            }
            try {
                Date dIssueDate = sdf.parse(issueDate);
                tworderitemarrange.setDpublishdate(dIssueDate);
            } catch (Exception e) {
//                return Json.fail("刊发日期格式错误！");
            }
            LambdaQueryWrapper<Tbfoldpageplan> lqw = Wrappers.lambdaQuery();
            lqw.eq(Tbfoldpageplan::getFoldname, issueFolder);
            lqw.eq(Tbfoldpageplan::getFoldareavername, foldVersion);
            lqw.eq(Tbfoldpageplan::getPublishdate, issueDate);
            lqw.eq(Tbfoldpageplan::getPagenum, issuePage);
            Tbfoldpageplan tbfoldpageplan = foldpageplanServiceI.getOne(lqw);
            if (tbfoldpageplan == null) {
                return Json.fail("未找到版面计划！");
            }

            tworderitemarrange.setFoldid(tbfoldpageplan.getFoldid());
            tworderitemarrange.setFoldname(tbfoldpageplan.getFoldname());
            tworderitemarrange.setFoldareaverid(tbfoldpageplan.getFoldareaverid());
            tworderitemarrange.setFoldareavername(tbfoldpageplan.getFoldareavername());
            tworderitemarrange.setFoldpageplanid(tbfoldpageplan.getId());
//            Tbfoldpageplan tbfoldpageplan = foldpageplanServiceI.getById(issuePage);
            tworderitemarrange.setFoldpageplanname(tbfoldpageplan.getPagenum());
            tworderitemarrange.setPublishnum(tbfoldpageplan.getPublishnum());
            tworderitemarrange.setLastoperatorid(user.getUserid());
            tworderitemarrange.setLastoperator(user.getUsername());
            tworderitemarrange.setDlastmodifydate(DateUtil.date());


            if (bNew) {
                innerInterceptor.recoredLog();
                if (orderitemarrangeMapper.insert(tworderitemarrange) == 0) {
                    return Json.fail("数据已改变，请刷新后再重新操作！");
                }
            } else {
                innerInterceptor.recoredLog();
                if (orderitemarrangeMapper.updateById(tworderitemarrange) == 0) {
                    return Json.fail("数据已改变，请刷新后再重新操作！");
                }
            }
            tworderitem.setFoldpageplanid(tbfoldpageplan.getId());
//            tworderitem.setFoldpageplanname(tbfoldpageplan.getPagenum());
            innerInterceptor.recoredLog();
            if (orderitemMapper.updateById(tworderitem) == 0) {
                return Json.fail("数据已改变，请刷新后再重新操作！");
            }
        }
        return Json.success();
    }

    @Override
    public Json DelAdTypeSetForCJ(String inforid, String pubDate) {
        List<Long> lsOrderitem = orderitemMapper.selectList(Wrappers.<Tworderitem>lambdaQuery()
                .eq(Tworderitem::getSordernum, inforid)
                .eq(Tworderitem::getAddisplayformname, "分类广告")
        ).stream().map(Tworderitem::getId).collect(Collectors.toList());
        if (lsOrderitem.size() == 0) {
            return Json.fail("未找到分类广告！");
        }
        LambdaQueryWrapper<Tworderitemarrange> lqw = Wrappers.lambdaQuery();
        lqw.in(Tworderitemarrange::getOrderitemid, lsOrderitem);
        List<Tworderitemarrange> tworderitemarrangeList = orderitemarrangeMapper.selectList(lqw);
        if (tworderitemarrangeList.size() == 0) {
            return Json.fail("未找到订单明细安排！");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dPubDate;
        try {
            dPubDate = sdf.parse(pubDate);
        } catch (Exception e) {
            return Json.fail("刊发日期格式错误！");
        }
        for (Tworderitemarrange tworderitemarrange : tworderitemarrangeList) {
            Tworderitem tworderitem = orderitemMapper.selectById(tworderitemarrange
                    .getOrderitemid());
            if (tworderitem == null) {
                return Json.fail("未找到订单明细！");
            }
            tworderitem.setPrestartdate(dPubDate);
            tworderitem.setIpublishstatus(PublishStatus.PUBLISH_Arrange.getKey());
            innerInterceptor.recoredLog();
            if (orderitemMapper.updateById(tworderitem) == 0) {
                return Json.fail("数据已改变，请刷新后再重新操作！");
            }
            innerInterceptor.recoredLog();
            if (orderitemarrangeMapper.deleteById(tworderitem
                    .getId()) == 0) {
                return Json.fail("数据已改变，请刷新后再重新操作！");
            }
        }

        return Json.success();
    }

}
