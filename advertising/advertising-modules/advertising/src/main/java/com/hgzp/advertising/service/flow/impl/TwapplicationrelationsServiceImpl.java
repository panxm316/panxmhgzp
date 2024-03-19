package com.hgzp.advertising.service.flow.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.emnus.approve.ApproveType;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.pagemodel.ad.dto.OrderDTO;
import com.hgzp.advertising.pagemodel.flow.TwapplicationrelationsVO;
import com.hgzp.advertising.pagemodel.flow.dto.TwapplicationrelationsDTO;
import com.hgzp.advertising.service.flow.TwapplicationrelationsServiceI;
import com.hgzp.core.emnus.FlowTypes;
import com.hgzp.core.model.Twapplicationrelations;
import com.hgzp.mapper.flow.TwapplicationrelationsMapper;
import com.hgzp.service.common.impl.MyServiceImpl;
import com.hgzp.utils.WebUtil;
import com.hgzp.utils.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 审批流程关联表 服务实现类
 * </p>
 *
 * @author muyn
 * @since 2023-12-05
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TwapplicationrelationsServiceImpl extends MyServiceImpl<TwapplicationrelationsMapper,
        Twapplicationrelations> implements TwapplicationrelationsServiceI {

    @Resource
    private HgDataChangeRecorderInnerInterceptor innerInterceptor;
    @Resource
    private TwapplicationrelationsMapper applicationrelationsMapper;

    @Override
    public void saveApplicationRelations(TwapplicationrelationsVO twapplicationrelationsVO) {
        Twapplicationrelations twapplicationrelations =
                TwapplicationrelationsVO.parseToEntity(twapplicationrelationsVO);
        innerInterceptor.recoredLog();
        boolean success = save(twapplicationrelations);
        if (!success) {
            throw new RuntimeException("审批流程关联表保存失败");
        }
    }

    @Override
    public void saveApplicationRelations(String sapplicationid, Long sMainId, Long sChildId, Integer iApproveStatus,
                                         String sFlowType, String approveType) {
        try {
            LoginUser loginUser = WebUtil.getLoginUser();
            Twapplicationrelations twapplicationrelations = new Twapplicationrelations();
            twapplicationrelations.setApplicationid(sapplicationid);
            twapplicationrelations.setFlowtype(sFlowType);
            twapplicationrelations.setMainid(sMainId);
            twapplicationrelations.setChildid(sChildId);
            twapplicationrelations.setIapprovestatus(iApproveStatus);
            twapplicationrelations.setCreateempid(loginUser.getUserid());
            twapplicationrelations.setCreateempname(loginUser.getUsername());
            twapplicationrelations.setDcreatetime(new Date());
            twapplicationrelations.setDlastmodifydate(new Date());
            twapplicationrelations.setApprovetype(approveType);

            innerInterceptor.recoredLog();
            boolean success = save(twapplicationrelations);
            if (!success) {
                throw new RuntimeException("流程关联数据保存失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteApplicationRelations(TwapplicationrelationsDTO twapplicationrelationsDTO) {
        Twapplicationrelations twapplicationrelations =
                TwapplicationrelationsDTO.parseToEntity(twapplicationrelationsDTO);
        innerInterceptor.recoredLog();
        boolean success = removeById(twapplicationrelations);
        if (!success) {
            throw new RuntimeException("审批流程关联表删除失败");
        }
    }

    @Override
    public void updateApplicationRelations(TwapplicationrelationsDTO twapplicationrelationsDTO) {
        Twapplicationrelations twapplicationrelations =
                TwapplicationrelationsDTO.parseToEntity(twapplicationrelationsDTO);
        innerInterceptor.recoredLog();
        boolean success = updateById(twapplicationrelations);
        if (!success) {
            throw new RuntimeException("审批流程关联表修改失败");
        }
    }

    @Override
    public TwapplicationrelationsVO getApplicationRelationsById(String id) {
        Twapplicationrelations twapplicationrelations = this.getById(id);
        if (ObjectUtil.isNull(twapplicationrelations)) {
            throw new RuntimeException("审批流程关联信息不存在");
        }
        TwapplicationrelationsVO apprelationsVO = TwapplicationrelationsVO.parseToVO(twapplicationrelations);
        apprelationsVO.setFlowtypename(FlowTypes.getNameByKey(twapplicationrelations.getFlowtype()));
        apprelationsVO.setApprovetypename(ApproveType.getNameByKey(Integer.valueOf(twapplicationrelations.getApprovetype())));
        return apprelationsVO;
    }

    @Override
    public TwapplicationrelationsVO getApplicationRelations(TwapplicationrelationsDTO applicationrelationsDTO) {
        List<Twapplicationrelations> lsapprelations = this.lambdaQuery()
                .eq(ObjectUtil.isNotNull(applicationrelationsDTO.getApplicationid()),
                        Twapplicationrelations::getApplicationid, applicationrelationsDTO.getApplicationid())
                .eq(ObjectUtil.isNotNull(applicationrelationsDTO.getFlowtype()),
                        Twapplicationrelations::getFlowtype, applicationrelationsDTO.getFlowtype())
                .eq(ObjectUtil.isNotNull(applicationrelationsDTO.getMainid()), Twapplicationrelations::getMainid,
                        applicationrelationsDTO.getMainid())
                .eq(ObjectUtil.isNotNull(applicationrelationsDTO.getChildid()), Twapplicationrelations::getChildid,
                        applicationrelationsDTO.getChildid())
                .eq(ObjectUtil.isNotNull(applicationrelationsDTO.getApprovetype()),
                        Twapplicationrelations::getApprovetype, applicationrelationsDTO.getApprovetype())
                .eq(ObjectUtil.isNotNull(applicationrelationsDTO.getNewbusinessentityid()),
                        Twapplicationrelations::getNewbusinessentityid,
                        applicationrelationsDTO.getNewbusinessentityid())
                .eq(ObjectUtil.isNotNull(applicationrelationsDTO.getNewcustomerid()),
                        Twapplicationrelations::getNewcustomerid, applicationrelationsDTO.getNewcustomerid())
                .orderByDesc(Twapplicationrelations::getDcreatetime)
                .list();
        if (lsapprelations.size() == 0) {
            return null;
        }

        TwapplicationrelationsVO ItemVO = TwapplicationrelationsVO.parseToVO(lsapprelations.get(0));
        ItemVO.setFlowtypename(FlowTypes.getNameByKey(lsapprelations.get(0).getFlowtype()));
        ItemVO.setApprovetypename(ApproveType.getNameByKey(Integer.valueOf(lsapprelations.get(0).getApprovetype())));
        return ItemVO;
    }

    @Override
    public List<TwapplicationrelationsVO> getApplicationRelationsList(TwapplicationrelationsDTO applicationrelationsDTO) {
        List<Twapplicationrelations> lsapprelations = this.lambdaQuery()
                .eq(ObjectUtil.isNotNull(applicationrelationsDTO.getApplicationid()),
                        Twapplicationrelations::getApplicationid, applicationrelationsDTO.getApplicationid())
                .eq(ObjectUtil.isNotNull(applicationrelationsDTO.getFlowtype()),
                        Twapplicationrelations::getFlowtype, applicationrelationsDTO.getFlowtype())
                .eq(ObjectUtil.isNotNull(applicationrelationsDTO.getMainid()), Twapplicationrelations::getMainid,
                        applicationrelationsDTO.getMainid())
                .eq(ObjectUtil.isNotNull(applicationrelationsDTO.getChildid()), Twapplicationrelations::getChildid,
                        applicationrelationsDTO.getChildid())
                .eq(ObjectUtil.isNotNull(applicationrelationsDTO.getApprovetype()),
                        Twapplicationrelations::getApprovetype, applicationrelationsDTO.getApprovetype())
                .eq(ObjectUtil.isNotNull(applicationrelationsDTO.getNewbusinessentityid()),
                        Twapplicationrelations::getNewbusinessentityid,
                        applicationrelationsDTO.getNewbusinessentityid())
                .eq(ObjectUtil.isNotNull(applicationrelationsDTO.getNewcustomerid()),
                        Twapplicationrelations::getNewcustomerid, applicationrelationsDTO.getNewcustomerid())
                .orderByDesc(Twapplicationrelations::getDcreatetime)
                .list();
        List<TwapplicationrelationsVO> lsResult = new ArrayList<>();
        for (Twapplicationrelations item : lsapprelations) {
            TwapplicationrelationsVO ItemVO = TwapplicationrelationsVO.parseToVO(item);
            ItemVO.setFlowtypename(FlowTypes.getNameByKey(item.getFlowtype()));
            ItemVO.setApprovetypename(ApproveType.getNameByKey(Integer.valueOf(item.getApprovetype())));
            lsResult.add(ItemVO);
        }
        return lsResult;
    }

    @Override
    public IPage<TwapplicationrelationsVO> getApplicationrelationsPageList(IPage<Twapplicationrelations> page,
                                                                           TwapplicationrelationsDTO applicationrelationsDTO) {
        LambdaQueryWrapper<Twapplicationrelations> lqw = Wrappers.lambdaQuery();
        lqw.eq(ObjectUtil.isNotNull(applicationrelationsDTO.getApplicationid()),
                        Twapplicationrelations::getApplicationid, applicationrelationsDTO.getApplicationid())
                .eq(ObjectUtil.isNotNull(applicationrelationsDTO.getFlowtype()),
                        Twapplicationrelations::getFlowtype, applicationrelationsDTO.getFlowtype())
                .eq(ObjectUtil.isNotNull(applicationrelationsDTO.getMainid()), Twapplicationrelations::getMainid,
                        applicationrelationsDTO.getMainid())
                .eq(ObjectUtil.isNotNull(applicationrelationsDTO.getChildid()), Twapplicationrelations::getChildid,
                        applicationrelationsDTO.getChildid())
                .eq(ObjectUtil.isNotNull(applicationrelationsDTO.getApprovetype()),
                        Twapplicationrelations::getApprovetype, applicationrelationsDTO.getApprovetype())
                .eq(ObjectUtil.isNotNull(applicationrelationsDTO.getNewbusinessentityid()),
                        Twapplicationrelations::getNewbusinessentityid,
                        applicationrelationsDTO.getNewbusinessentityid())
                .eq(ObjectUtil.isNotNull(applicationrelationsDTO.getNewcustomerid()),
                        Twapplicationrelations::getNewcustomerid, applicationrelationsDTO.getNewcustomerid())
                .orderByDesc(Twapplicationrelations::getDcreatetime);

        IPage<Twapplicationrelations> lsResult = applicationrelationsMapper.selectPage(page, lqw);
        Page<TwapplicationrelationsVO> reslutPage = new Page<TwapplicationrelationsVO>();
        if (lsResult.getTotal() == 0) {
            return reslutPage;
        }
        List<TwapplicationrelationsVO> lsVO = lsResult.getRecords().stream().map(TwapplicationrelationsVO::parseToVO)
                .collect(Collectors.toList());
        for (TwapplicationrelationsVO item : lsVO) {
            item.setFlowtypename(FlowTypes.getNameByKey(item.getFlowtype()));
            item.setApprovetypename(ApproveType.getNameByKey(Integer.valueOf(item.getApprovetype())));
        }
        reslutPage.setTotal(lsResult.getTotal());
        reslutPage.setRecords(lsVO);

        return reslutPage;
    }

}
