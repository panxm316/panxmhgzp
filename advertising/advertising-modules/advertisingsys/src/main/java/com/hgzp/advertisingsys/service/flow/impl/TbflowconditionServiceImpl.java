package com.hgzp.advertisingsys.service.flow.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertisingsys.pagemodel.flow.vo.FlowConditionVo;
import com.hgzp.advertisingsys.pagemodel.flow.vo.FlowConditionEx;
import com.hgzp.advertisingsys.service.ad.TbadtypeServiceI;
import com.hgzp.advertisingsys.service.finance.TbbusinessentityServiceI;
import com.hgzp.advertisingsys.service.flow.TbflowconditionServiceI;
import com.hgzp.advertisingsys.service.media.TbmediaServiceI;
import com.hgzp.common.flowable.constants.FormTypeEnum;
import com.hgzp.core.emnus.FlowTypes;
import com.hgzp.core.model.*;
import com.hgzp.core.page.DataCombo;
import com.hgzp.mapper.flow.TbflowconditionMapper;
import com.hgzp.service.common.impl.MyServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 工作流条件 服务实现类
 * </p>
 * 创建人：songly
 * 类描述：TODO
 * 创建日期：2023/10/12 9:10
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TbflowconditionServiceImpl extends MyServiceImpl<TbflowconditionMapper, Tbflowcondition> implements TbflowconditionServiceI {
    @Resource
    private TbflowconditionMapper flowMapper;
    @Autowired
    private TbmediaServiceI tbmediaService;
    @Autowired
    private TbadtypeServiceI adTypeService;
    @Autowired
    private TbbusinessentityServiceI bzService;

    /**
     * getFlowConditionPageList
     * 方法功能:  获取工作流分页
     *
     * @param page
     * @param vo
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.core.model.Tbflowcondition>
     * @author songly
     * @date 2023/10/12 9:54
     */
    @Override
    public IPage<FlowConditionVo> getFlowConditionPageList(Page<Tbflowcondition> page, FlowConditionVo vo) {
        LambdaQueryWrapper<Tbflowcondition> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.hasText(vo.getQueryKey()), Tbflowcondition::getSname, vo.getQueryKey())
                .eq(ObjectUtil.isNotNull(vo.getBuse()), Tbflowcondition::getBuse, vo.getBuse())
                .eq(ObjectUtil.isNotNull(vo.getSflowtype()), Tbflowcondition::getSflowtype, vo.getSflowtype())
                .inSql(Tbflowcondition::getSflowtype, "select skey from tbflow where buse=1")
                .eq(ObjectUtil.isNotNull(vo.getSconditiontype()), Tbflowcondition::getSconditiontype, vo.getSconditiontype());
        IPage<Tbflowcondition> lsResult = flowMapper.selectPage(page, lqw);
        Page<FlowConditionVo> reslutPage = new Page<FlowConditionVo>();
        if (lsResult.getTotal() == 0) {
            return reslutPage;
        }
        List<FlowConditionVo> lsFlowData = new ArrayList<>();
        for (Tbflowcondition record : lsResult.getRecords()) {
            FlowConditionVo flowVo = new FlowConditionVo();
            BeanUtils.copyProperties(record, flowVo);
            flowVo.setSconditiontypename(FormTypeEnum.getNameByType(record.getSconditiontype()));
            flowVo.setSflowtypename(FlowTypes.getNameByKey(record.getSflowtype()));
            lsFlowData.add(flowVo);
        }
        reslutPage.setRecords(lsFlowData);
        reslutPage.setTotal(lsResult.getTotal());
        return reslutPage;
    }

    @Override
    public List<FlowConditionEx> getFlowConditionList(String sflowtype) {
        List<Tbflowcondition> lsFlowInfo = new LambdaQueryChainWrapper<>(flowMapper)
         .eq(Tbflowcondition::getBuse, true)
                .eq(ObjectUtil.isNotNull(sflowtype), Tbflowcondition::getSflowtype, sflowtype)
                .orderByAsc(Tbflowcondition::getIsort)
                .list();

        List<FlowConditionEx> lsResult=new ArrayList<>();
        for (Tbflowcondition record : lsFlowInfo) {
            FlowConditionEx flowVo = new FlowConditionEx();
            BeanUtils.copyProperties(record, flowVo);
            if("Media".equals(record.getSconditiontable())){
                List<DataCombo> mediaDataCombo = tbmediaService.getMediaDataCombo();
                flowVo.setData(mediaDataCombo);
            }
            if("AdType".equals(record.getSconditiontable())){
                List<DataCombo> mediaDataCombo = adTypeService.getAdTypeCombo();
                flowVo.setData(mediaDataCombo);
            }
            if("Businessentity".equals(record.getSconditiontable())){
                List<DataCombo> mediaDataCombo = bzService.getBzEntityTypeCombo();
                flowVo.setData(mediaDataCombo);
            }
            lsResult.add(flowVo);
        }
       return lsResult;
    }



    /**
     * deleteFlowConditionByIds
     * 方法功能:据id批量删除
     *
     * @param idList
     * @return boolean
     * @author yanz
     * @date 2023/8/25 10:53
     */
    @Override
    public boolean deleteFlowConditionByIds(List<String> idList) {
        flowMapper.deleteBatchIds(idList);
        return true;
    }

    @Override
    public boolean isExistFlowCondition(Tbflowcondition tbflowcondition) {
        Long count = new LambdaQueryChainWrapper<>(flowMapper)
                .eq(Tbflowcondition::getSname, tbflowcondition.getSname())
                .eq(Tbflowcondition::getSflowtype, tbflowcondition.getSflowtype())
                .ne(tbflowcondition.getId() != null, Tbflowcondition::getId, tbflowcondition.getId())
                .count();
        return count > 0;
    }

}