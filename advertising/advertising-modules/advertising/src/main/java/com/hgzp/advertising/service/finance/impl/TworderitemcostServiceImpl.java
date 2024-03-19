package com.hgzp.advertising.service.finance.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgzp.advertising.emnus.approve.AppStatus;
import com.hgzp.advertising.emnus.approve.ApproveStatus;
import com.hgzp.advertising.emnus.file.SfileType;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.pagemodel.business.dto.OrderAndItemDTO;
import com.hgzp.advertising.pagemodel.business.dto.OrderItemCostDTO;
import com.hgzp.advertising.pagemodel.business.dto.OrderItemCostFilesDTO;
import com.hgzp.advertising.pagemodel.business.vo.OrderAndItemVO;
import com.hgzp.advertising.service.ad.TworderServiceI;
import com.hgzp.advertising.service.ad.TworderitemServiceI;
import com.hgzp.advertising.service.ad.TworderitembelongServiceI;
import com.hgzp.advertising.service.finance.TworderitemcostServiceI;
import com.hgzp.advertising.service.finance.TworderitemcostfilesServiceI;
import com.hgzp.core.exception.DataExistException;
import com.hgzp.core.exception.DataNotExistException;
import com.hgzp.core.model.*;
import com.hgzp.mapper.business.TworderitemcostMapper;
import com.hgzp.mapper.business.TworderitemcostfilesMapper;
import com.hgzp.mapper.resource.TwresourcesMapper;
import com.hgzp.utils.WebUtil;
import com.hgzp.utils.file.UfileUtil;
import com.hgzp.utils.model.LoginUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 广告成本表 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-12-04
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TworderitemcostServiceImpl extends ServiceImpl<TworderitemcostMapper, Tworderitemcost> implements TworderitemcostServiceI {
    @Value("${ufile.uWebURL}")
    private String uWebURL;
    @Autowired
    TworderitembelongServiceI tworderitembelongServiceI;
    @Autowired
    TworderitemServiceI tworderitemServiceI;
    @Autowired
    TworderitemcostfilesServiceI tworderitemcostfilesServiceI;
    @Autowired
    TworderServiceI tworderServiceI;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;
    @Autowired
    TwresourcesMapper twresourcesMapper;
    @Autowired
    TworderitemcostMapper tworderitemcostMapper;
    @Autowired
    TworderitemcostfilesMapper tworderitemcostfilesMapper;

    @Override
    public IPage<OrderAndItemDTO> getOrderAndItemPageList(Page<Tworderitem> page, OrderAndItemVO query) throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        Date endTime = query.getEndTime() == null ? null : DateUtil.offsetDay(query.getEndTime(), 1);
        Page<OrderAndItemDTO> orderAndItemDTOPage = new Page<>();
        List<Tworderitembelong> tworderitembelongs = tworderitembelongServiceI.lambdaQuery()
                .eq(Tworderitembelong::getEmployid, user.getUserid())
                .eq(StringUtils.hasText(query.getScontractnum()), Tworderitembelong::getScontractnum, query.getScontractnum())
                .ge(query.getStartTime() != null, Tworderitembelong::getCreatetime, query.getStartTime())
                .lt(query.getEndTime() != null, Tworderitembelong::getCreatetime, endTime)
                .list();
        // TODO 属于当前用户的订单明细id列表
        List<Long> orderitemids = tworderitembelongs.stream().map(Tworderitembelong::getOrderitemid).collect(Collectors.toList());
        List<Tworderitem> tworderitems = tworderitemServiceI.lambdaQuery()
                .ge(query.getStartTime() != null, Tworderitem::getCreatetime, query.getStartTime())
                .lt(query.getEndTime() != null, Tworderitem::getCreatetime, endTime)
                .in(orderitemids.size() > 0, Tworderitem::getId, orderitemids)
                .eq(StringUtils.hasText(query.getScontractnum()), Tworderitem::getScontractnum, query.getScontractnum())
                .eq(Tworderitem::getIapprovestatus, ApproveStatus.APPROVE_PASS.getKey())
                .isNotNull(Tworderitem::getScontractnum)
                .orderByDesc(Tworderitem::getPrestartdate)
                .list();


        // TODO 订单明细列表
        List<OrderAndItemDTO> orderAndItemDTOS = tworderitems.stream().map(tworderitem -> {
            OrderAndItemDTO orderAndItemDTO = new OrderAndItemDTO();
            BeanUtils.copyProperties(tworderitem, orderAndItemDTO);
            orderAndItemDTO.setOrderitemid(tworderitem.getId());
            // TODO 订单信息
            Tworder tworder = tworderServiceI.getById(tworderitem.getOrderid());
            if (tworder != null) {
                orderAndItemDTO.setAdprojectname(tworder.getAdprojectname());
                orderAndItemDTO.setScontractnum(tworder.getScontractnum());
                orderAndItemDTO.setCustomername(tworder.getCustomername());
                orderAndItemDTO.setAgencyname(tworder.getAgencyname());
                orderAndItemDTO.setAgentname(tworder.getAgentname());
                orderAndItemDTO.setAdindustryname(tworder.getAdindustryname());
            }

            List<Tworderitemcost> Tworderitemcosts = this.lambdaQuery()
                    .eq(Tworderitemcost::getOrderitemid, tworderitem.getId())
                    .list();
            List<OrderItemCostDTO> orderItemCostDTOS = convertToOrderItemCostDTOs(Tworderitemcosts);

            orderAndItemDTO.setOrderItemCostDTOList(orderItemCostDTOS);
            return orderAndItemDTO;
        }).collect(Collectors.toList());
        // 按照时间排序
//        orderAndItemDTOS.sort((o1, o2) -> o2.getDcreatetime().compareTo(o1.getDcreatetime()));

        orderAndItemDTOPage.setRecords(orderAndItemDTOS);
        orderAndItemDTOPage.setTotal(orderAndItemDTOS.size());
        return orderAndItemDTOPage;
    }

    /**
     * 广告成本POJO转DTO
     * 方法功能:有查询操作，涉及Tworderitemcosts广告成本、Tworderitemcostfiles证明文件表
     *
     * @param Tworderitemcosts
     * @return java.util.List<com.hgzp.advertising.pagemodel.business.dto.OrderItemCostDTO>
     * @author yanz
     * @date 2024/1/5 15:01
     */
    public List<OrderItemCostDTO> convertToOrderItemCostDTOs(List<Tworderitemcost> Tworderitemcosts) {
        // TODO 广告成本列表
        List<OrderItemCostDTO> orderItemCostDTOS = Tworderitemcosts.stream().map(tworderitemcost -> {
            OrderItemCostDTO orderItemCostDTO = new OrderItemCostDTO();
            BeanUtils.copyProperties(tworderitemcost, orderItemCostDTO);
            // TODO 证明文件列表
            List<Tworderitemcostfiles> tworderitemcostfilesList = tworderitemcostfilesServiceI.lambdaQuery()
                    .eq(Tworderitemcostfiles::getOrderitemcostid, tworderitemcost.getId())
                    .list();
            List<OrderItemCostFilesDTO> orderItemCostFilesDTOS = tworderitemcostfilesList.stream().map(tworderitemcostfiles -> {
                OrderItemCostFilesDTO orderItemCostFilesDTO = new OrderItemCostFilesDTO();
                BeanUtils.copyProperties(tworderitemcostfiles, orderItemCostFilesDTO);
                String url = uWebURL + tworderitemcostfiles.getSfileid();
                orderItemCostFilesDTO.setUrl(url);
                String durl = UfileUtil.getStaticUrl(tworderitemcostfiles.getSfileid(), tworderitemcostfiles.getSfileformat());
                orderItemCostFilesDTO.setDurl(durl);
                return orderItemCostFilesDTO;
            }).collect(Collectors.toList());

            orderItemCostDTO.setFileList(orderItemCostFilesDTOS);
            return orderItemCostDTO;
        }).collect(Collectors.toList());
        return orderItemCostDTOS;
    }

    @Override
    public void saveOrderItemCost(OrderItemCostDTO entity) throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        Tworderitemcost tworderitemcost = new Tworderitemcost();
        BeanUtils.copyProperties(entity, tworderitemcost);
        tworderitemcost.setId(IdUtil.getSnowflakeNextId());
        tworderitemcost.setCreateempid(user.getUserid());
        tworderitemcost.setCreateempname(user.getUsername());
        tworderitemcost.setDcreatedate(new Date());
        innerInterceptor.recoredLog();
        tworderitemcostMapper.insert(tworderitemcost);
        // TODO 提交时成本金额加到订单明细成本金额，删除或者退回时成本金额减去订单明细成本金额
        if (entity.getIstatus() == AppStatus.APPRSTATUS_PASS.getKey()) {
            Tworderitem tworderitem = tworderitemServiceI.getById(tworderitemcost.getOrderitemid());
            tworderitem.setNamountcost(tworderitem.getNamountcost().add(tworderitemcost.getNamountcost()));
            innerInterceptor.recoredLog();
            tworderitemServiceI.updateById(tworderitem);
        }
        List<OrderItemCostFilesDTO> fileList = entity.getFileList();
        if (fileList == null || fileList.size() == 0) {
            throw new DataNotExistException("成本信息附件不能为空");
        }
        if (fileList != null && fileList.size() > 0) {
            for (OrderItemCostFilesDTO filesDTO : fileList) {
                Tworderitemcostfiles file = new Tworderitemcostfiles();
                BeanUtils.copyProperties(filesDTO, file);
                file.setOrderitemcostid(tworderitemcost.getId());
                file.setCreateempid(user.getUserid());
                file.setCreateempname(user.getUsername());
                file.setDcreatetime(new Date());
                file.setIfilecategory(SfileType.FileType_10.key);
                innerInterceptor.recoredLog();
                tworderitemcostfilesMapper.insert(file);

                Long count = new LambdaQueryChainWrapper<>(twresourcesMapper)
                        .eq(file.getSfileid() != null, Twresources::getSfileid, file.getSfileid())
                        .count();
                if (count > 0) {
                    continue;
                }
                Twresources twresources = new Twresources();
                twresources.setId(IdUtil.getSnowflakeNextId());
                twresources.setSfileformat(file.getSfileformat());
                twresources.setSfileid(file.getSfileid());
                twresources.setSfilesize(file.getSfilesize());
                twresources.setSoriginalfile(file.getSoriginalfile());
                twresources.setDcreatetime(new Date());
                twresources.setSfiletype(file.getSfiletype());
                twresources.setEmployid(user.getUserid());
                innerInterceptor.recoredLog();
                twresourcesMapper.insert(twresources);
            }
        }
    }

    @Override
    public void updateOrderItemCost(OrderItemCostDTO entity) throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        Tworderitemcost tworderitemcost = this.getById(entity.getId());
        if (tworderitemcost == null) {
            throw new DataNotExistException("成本信息不存在");
        }
        // TODO 提交时成本金额加到订单明细成本金额，删除或者退回时成本金额减去订单明细成本金额
        if (entity.getIstatus() == AppStatus.APPRSTATUS_PASS.getKey()) {
            Tworderitem tworderitem = tworderitemServiceI.getById(tworderitemcost.getOrderitemid());
            tworderitem.setNamountcost(tworderitem.getNamountcost().add(tworderitemcost.getNamountcost()));
            innerInterceptor.recoredLog();
            tworderitemServiceI.updateById(tworderitem);
        }

        BeanUtils.copyProperties(entity, tworderitemcost);
        if (tworderitemcost.getIstatus() == AppStatus.APPRSTATUS_EDITING.getKey() || tworderitemcost.getIstatus() == AppStatus.APPRSTATUS_PASS.getKey()) {
            throw new DataExistException("成本信息已经提交，不允许修改");
        }
        innerInterceptor.recoredLog();
        tworderitemcostMapper.updateById(tworderitemcost);
        List<OrderItemCostFilesDTO> fileModels = entity.getFileList();
        List<OrderItemCostFilesDTO> files = fileModels.stream().filter(file -> file.getBdelete() == null || file.getBdelete() == false).collect(Collectors.toList());
        if (files == null || files.size() == 0) {
            throw new DataNotExistException("成本信息附件不能为空");
        }
        for (OrderItemCostFilesDTO file : fileModels) {
            // 已删除附件删除处理
            if (file.getBdelete() != null && file.getBdelete()) {
                if (file.getId() != null) {
                    innerInterceptor.recoredLog();
                    tworderitemcostfilesMapper.deleteById(file.getId());
                }
                continue;
            }
            Tworderitemcostfiles tworderitemcostfiles = new Tworderitemcostfiles();
            BeanUtils.copyProperties(file, tworderitemcostfiles);
            // 已有附件直接更新
            if (file.getId() != null) {
                innerInterceptor.recoredLog();
                tworderitemcostfilesMapper.updateById(tworderitemcostfiles);
                continue;
            }
            // 新增附件插入
            tworderitemcostfiles.setId(IdUtil.getSnowflakeNextId());
            tworderitemcostfiles.setOrderitemcostid(entity.getId());
            tworderitemcostfiles.setCreateempid(user.getUserid());
            tworderitemcostfiles.setCreateempname(user.getUsername());
            tworderitemcostfiles.setDcreatetime(new Date());
            tworderitemcostfiles.setIfilecategory(SfileType.FileType_10.key);
            innerInterceptor.recoredLog();
            tworderitemcostfilesMapper.insert(tworderitemcostfiles);
            Long count = new LambdaQueryChainWrapper<>(twresourcesMapper)
                    .eq(file.getSfileid() != null, Twresources::getSfileid, file.getSfileid())
                    .count();
            if (count > 0) {
                continue;
            }
            Twresources twresources = new Twresources();
            twresources.setId(IdUtil.getSnowflakeNextId());
            twresources.setSfileformat(file.getSfileformat());
            twresources.setSfileid(file.getSfileid());
            twresources.setSfilesize(file.getSfilesize());
            twresources.setSoriginalfile(file.getSoriginalfile());
            twresources.setDcreatetime(new Date());
            twresources.setSfiletype(file.getSfiletype());
            twresources.setEmployid(user.getUserid());
            innerInterceptor.recoredLog();
            twresourcesMapper.insert(twresources);
        }
    }

    @Override
    public void deleteOrderItemCost(String ids) throws Exception {
        List<String> idList = Arrays.asList(ids.split(","));
        for (String id : idList) {
            Tworderitemcost tworderitemcost = tworderitemcostMapper.selectById(id);
            if (tworderitemcost.getIstatus() == AppStatus.APPRSTATUS_EDITING.getKey() || tworderitemcost.getIstatus() == AppStatus.APPRSTATUS_PASS.getKey()) {
                throw new DataExistException("成本信息已经提交，不允许删除");
            }
            LambdaQueryWrapper<Tworderitemcostfiles> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Tworderitemcostfiles::getOrderitemcostid, id);
            List<Tworderitemcostfiles> tworderitemcostfiles = tworderitemcostfilesMapper.selectList(queryWrapper);
            for (Tworderitemcostfiles file : tworderitemcostfiles) {
                innerInterceptor.recoredLog();
                tworderitemcostfilesMapper.deleteById(file);
            }
        }

        innerInterceptor.recoredLog();
        tworderitemcostMapper.deleteBatchIds(idList);
    }

    @Override
    public void updateOrderItemCostStatus(String id, Integer status) throws Exception {
        Tworderitemcost tworderitemcost = this.getById(id);
        if (tworderitemcost == null) {
            throw new DataNotExistException("成本信息不存在");
        }
        tworderitemcost.setIstatus(status);
        innerInterceptor.recoredLog();
        tworderitemcostMapper.updateById(tworderitemcost);
        Tworderitem tworderitem = tworderitemServiceI.getById(tworderitemcost.getOrderitemid());
        if (tworderitem != null) {
            // TODO 提交时成本金额加到订单明细成本金额，删除或者退回时成本金额减去订单明细成本金额
            tworderitem.setNamountcost(tworderitem.getNamountcost().add(tworderitemcost.getNamountcost()));
            innerInterceptor.recoredLog();
            tworderitemServiceI.updateById(tworderitem);
        }
    }
}
