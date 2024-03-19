package com.hgzp.advertising.service.statistic.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.pagemodel.statistic.dto.ReportmodelGroupDTO;
import com.hgzp.advertising.service.statistic.ReportmodelGroupServiceI;
import com.hgzp.core.model.Tbreportmodelgroup;
import com.hgzp.core.model.Tbreportmodelitem;
import com.hgzp.core.model.Twtasks;
import com.hgzp.mapper.statistic.TbreportmodelgroupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 报表模板组设置 服务实现类
 * </p>
 *
 * @author muyn
 * @since 2024-01-16
 */
@Service
public class ReportmodelGroupServiceImpl extends ServiceImpl<TbreportmodelgroupMapper, Tbreportmodelgroup> implements ReportmodelGroupServiceI {
    @Autowired
    private TbreportmodelgroupMapper tbreportmodelgroupMapper;

    @Autowired
    private HgDataChangeRecorderInnerInterceptor innerInterceptor;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(ReportmodelGroupDTO  reportmodelgroupDTO) {
        Tbreportmodelgroup tbreportmodelgroup = ReportmodelGroupDTO.parseToEntity(reportmodelgroupDTO);
        innerInterceptor.recoredLog();
        boolean success = save(tbreportmodelgroup);
        if (!success) {
            throw new RuntimeException("Tbreportmodelgroup保存失败");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(ReportmodelGroupDTO reportmodelgroupDTO) {
        Tbreportmodelgroup tbreportmodelgroup = ReportmodelGroupDTO.parseToEntity(reportmodelgroupDTO);
        innerInterceptor.recoredLog();
        boolean success = removeById(tbreportmodelgroup);
        if (!success) {
            throw new RuntimeException("Tbreportmodelgroup删除失败");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(ReportmodelGroupDTO reportmodelgroupDTO) {
        Tbreportmodelgroup tbreportmodelgroup = ReportmodelGroupDTO.parseToEntity(reportmodelgroupDTO);
        innerInterceptor.recoredLog();
        boolean success = updateById(tbreportmodelgroup);
        if (!success) {
            throw new RuntimeException("Tbreportmodelgroup修改失败");
        }
    }

    @Override
    public List<ReportmodelGroupDTO> getReportFormList(String reportFormId) {
        List<ReportmodelGroupDTO>  dtoList = new ArrayList<ReportmodelGroupDTO>();
        LambdaQueryWrapper<Tbreportmodelgroup> lqw = Wrappers.lambdaQuery();
        lqw.eq(null != reportFormId, Tbreportmodelgroup::getApplyto,reportFormId);
        // 按日期排序
        lqw.orderByDesc(Tbreportmodelgroup::getDcreatetime);
        List<Tbreportmodelgroup> tbreportmodelgroupList = tbreportmodelgroupMapper.selectList(lqw);
        for( int i = 0; i < tbreportmodelgroupList.size(); i++ ) {
            Tbreportmodelgroup tbreportmodelgroup = tbreportmodelgroupList.get(i);
            ReportmodelGroupDTO dto = ReportmodelGroupDTO.parseToDTO(tbreportmodelgroup);
            dtoList.add(dto);
        }

        return dtoList;
    }

    @Override
    public void enableReportForm(String reportId,String reportFormId) {
        LambdaQueryWrapper<Tbreportmodelgroup> lqw = Wrappers.lambdaQuery();
        lqw.eq(null != reportFormId, Tbreportmodelgroup::getApplyto,reportFormId);
        List<Tbreportmodelgroup> tbreportmodelgroupList = tbreportmodelgroupMapper.selectList(lqw);
        for( int i = 0; i < tbreportmodelgroupList.size(); i++ ) {
            Tbreportmodelgroup tbreportmodelgroup = tbreportmodelgroupList.get(i);
            if( tbreportmodelgroup.getId().equals(Long.valueOf(reportId)) ) {
                tbreportmodelgroup.setBuse(true);
            } else {
                tbreportmodelgroup.setBuse(false);
            }
            innerInterceptor.recoredLog();
            updateById(tbreportmodelgroup);
        }

    }

    @Override
    public Tbreportmodelgroup getTbreportmodelgroup(String reportFormId) {
        LambdaQueryWrapper<Tbreportmodelgroup> lqw = Wrappers.lambdaQuery();
        lqw.eq(null != reportFormId, Tbreportmodelgroup::getApplyto,reportFormId);
        lqw.eq(Tbreportmodelgroup::getBuse,true);
        List<Tbreportmodelgroup>  list = tbreportmodelgroupMapper.selectList(lqw);
        if( list.size() > 0 )
            return  list.get(0);
        return null;
    }




}
