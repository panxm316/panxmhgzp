package com.hgzp.advertising.service.statistic.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.pagemodel.statistic.dto.ReportmodelGroupDTO;
import com.hgzp.advertising.pagemodel.statistic.dto.ReportmodelItemDTO;
import com.hgzp.advertising.pagemodel.statistic.dto.SumObjectDTO;
import com.hgzp.advertising.service.media.TbfoldServiceI;
import com.hgzp.advertising.service.media.TbfoldareaverServiceI;
import com.hgzp.advertising.service.media.TbmediaServiceI;
import com.hgzp.advertising.service.statistic.ReportmodelItemServiceI;
import com.hgzp.advertising.service.system.TbdeptServiceI;
import com.hgzp.core.model.*;
import com.hgzp.mapper.statistic.TbreportmodelgroupMapper;
import com.hgzp.mapper.statistic.TbreportmodelitemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 报表模板明细设置 服务实现类
 * </p>
 *
 * @author muyn
 * @since 2024-01-16
 */
@Service
public class ReportmodelItemServiceImpl extends ServiceImpl<TbreportmodelitemMapper, Tbreportmodelitem> implements ReportmodelItemServiceI {

    @Autowired
    private TbreportmodelitemMapper tbreportmodelitemMapper;
    @Autowired
    private TbreportmodelgroupMapper tbreportmodelgroupMapper;
    @Autowired
    private HgDataChangeRecorderInnerInterceptor innerInterceptor;
    @Autowired
    private TbdeptServiceI  tbdeptServiceI;
    @Autowired
    private TbmediaServiceI tbmediaServiceI;
    @Autowired
    private TbfoldServiceI  tbfoldServiceI;
    @Autowired
    private TbfoldareaverServiceI tbfoldareaverServiceI;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(ReportmodelItemDTO reportmodelitemDTO) {
        Tbreportmodelitem tbreportmodelitem = reportmodelitemDTO.parseToEntity(reportmodelitemDTO);
        innerInterceptor.recoredLog();
        boolean success = save(tbreportmodelitem);
        if (!success) {
            throw new RuntimeException("Tbreportmodelitem保存失败");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(ReportmodelItemDTO reportmodelitemDTO) {
        Tbreportmodelitem tbreportmodelitem = reportmodelitemDTO.parseToEntity(reportmodelitemDTO);
        innerInterceptor.recoredLog();
        boolean success = removeById(tbreportmodelitem);
        if (!success) {
            throw new RuntimeException("Tbreportmodelitem删除失败");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(ReportmodelItemDTO reportmodelitemDTO) {
        Tbreportmodelitem tbreportmodelitem = ReportmodelItemDTO.parseToEntity(reportmodelitemDTO);
        innerInterceptor.recoredLog();
        boolean success = updateById(tbreportmodelitem);
        if (!success) {
            throw new RuntimeException("Tbreportmodelitem修改失败");
        }
    }

    @Override
    public List<ReportmodelItemDTO> getReportSumItemList(String reportGroupId,String reportType) {
        Tbreportmodelgroup  tbreportmodelgroup = tbreportmodelgroupMapper.selectById(reportGroupId);
        List<ReportmodelItemDTO>  dtoList = new ArrayList<ReportmodelItemDTO>();
        if( null == tbreportmodelgroup ) {
            return  dtoList;
        }
        // 广告实收明细表年度业务汇总项
        if( tbreportmodelgroup.getReporttype().equals("3") ) {
            LambdaQueryWrapper<Tbreportmodelitem> tlqw = Wrappers.lambdaQuery();
            tlqw.eq(null != reportGroupId, Tbreportmodelitem::getReportmodelgroupid,reportGroupId);
            tlqw.eq(Tbreportmodelitem::getReporttype,"年度");
            List<Tbreportmodelitem> tempList = tbreportmodelitemMapper.selectList(tlqw);
            for( int i = 0; i < tempList.size(); i++ ) {
                Tbreportmodelitem  tbreportmodelitem = tempList.get(i);
                String str = getReportSumItemStr(reportGroupId,"业务");
                if( !str.isEmpty() ) {
                    if( !str.equals(tbreportmodelitem.getSids()) ) {
                        tbreportmodelitem.setScolumnname(str);
                        tbreportmodelitemMapper.updateById(tbreportmodelitem);
                    }
                }
            }
        }

        LambdaQueryWrapper<Tbreportmodelitem> lqw = Wrappers.lambdaQuery();

        lqw.eq(null != reportGroupId, Tbreportmodelitem::getReportmodelgroupid,reportGroupId);
        lqw.eq(null != reportType, Tbreportmodelitem::getReporttype,reportType);

        // 按日期排序
        lqw.orderByAsc(Tbreportmodelitem::getIsort);
        List<Tbreportmodelitem> tbreportmodelitemList = tbreportmodelitemMapper.selectList(lqw);
        for( int i = 0; i < tbreportmodelitemList.size(); i++ ) {
            Tbreportmodelitem tbreportmodelitem = tbreportmodelitemList.get(i);
            ReportmodelItemDTO dto = ReportmodelItemDTO.parseToDTO(tbreportmodelitem);
            dtoList.add(dto);
        }

        return dtoList;
    }

    @Override
    public boolean addSumObject(SumObjectDTO sumObjectDTO) {
        Tbreportmodelitem tbreportmodelitem = getById(sumObjectDTO.getReportGroupItemId());
        if( null == tbreportmodelitem ) {
            return  false;
        }
        tbreportmodelitem.setIdepttype(sumObjectDTO.getIdepttype());
        tbreportmodelitem.setSids(sumObjectDTO.getSids());
        // 部门
        if( tbreportmodelitem.getReporttype().equals("部门") ) {
            String deptArray[] = sumObjectDTO.getSids().split(",");
            String strName = "";
            for( int i = 0; i < deptArray.length; i++ ) {
                Tbdept tbdept = tbdeptServiceI.getById(deptArray[i]);
                if( null != tbdept ) {
                    strName += tbdept.getSdeptname();
                    strName += ",";
                }
            }
            if( strName.length() > 0 ) {
                strName = strName.substring(0,strName.length()-1);
            }
            tbreportmodelitem.setScolumnname(strName);
        } else {  // 业务
            String mediaArray[] = sumObjectDTO.getSids().split(",");
            String strName = "";
            for( int i = 0; i < mediaArray.length; i++ ) {
                strName += getMediaName(mediaArray[i],sumObjectDTO.getSnames());
                strName += ",";
            }
            if( strName.length() > 0 ) {
                strName = strName.substring(0,strName.length()-1);
            }
            tbreportmodelitem.setScolumnname(strName);
            tbreportmodelitem.setSnames(sumObjectDTO.getSnames());
        }
        updateById(tbreportmodelitem);
        return true;
    }

    @Override
    public List<Tbreportmodelitem> getReportmodeItem(String reportGroupId, String reportType) {
        LambdaQueryWrapper<Tbreportmodelitem> lqw = Wrappers.lambdaQuery();
        lqw.eq(null != reportGroupId, Tbreportmodelitem::getReportmodelgroupid,reportGroupId);
        lqw.eq(null != reportType, Tbreportmodelitem::getReporttype,reportType);
        lqw.orderByAsc(Tbreportmodelitem::getIsort);
        List<Tbreportmodelitem> tbreportmodelitemList = tbreportmodelitemMapper.selectList(lqw);
        return tbreportmodelitemList;
    }

    public String getMediaName(String mediaId,String type) {
        String  name = "";
        if( type.equals("0") ) {
            Tbmedia tbmedia = tbmediaServiceI.getById(mediaId);
            if( null != tbmedia ) {
                return tbmedia.getSname();
            }
        } else if(type.equals("1")) {
            Tbfold tbfold = tbfoldServiceI.getById(mediaId);
            if( null != tbfold ) {
                return  tbfold.getSname();
            }
        } else if(type.equals("2")) {
            Tbfoldareaver  tbfoldareaver = tbfoldareaverServiceI.getById(mediaId);
            if( null != tbfoldareaver ) {
                return tbfoldareaver.getSname();
            }
        }
        return name;
    }

    public String getReportSumItemStr(String reportGroupId,String reportType) {
        String str = "";
        Tbreportmodelgroup  tbreportmodelgroup = tbreportmodelgroupMapper.selectById(reportGroupId);
        LambdaQueryWrapper<Tbreportmodelitem> lqw = Wrappers.lambdaQuery();
        lqw.eq(null != reportGroupId, Tbreportmodelitem::getReportmodelgroupid,reportGroupId);
        lqw.eq(null != reportType, Tbreportmodelitem::getReporttype,reportType);
        List<Tbreportmodelitem> tempList = tbreportmodelitemMapper.selectList(lqw);
        for( int i = 0; i < tempList.size(); i++ ) {
            Tbreportmodelitem  tbreportmodelitem = tempList.get(i);
            str += tbreportmodelitem.getSheadername();
            if( i < tempList.size()-1 ) {
                str += ",";
            }
        }
        return str;
    }

    @Override
    public Tbreportmodelitem getTbreportmodelitemWithHeadName(String groupId, String headName) {
        LambdaQueryWrapper<Tbreportmodelitem> tlqw = Wrappers.lambdaQuery();
        tlqw.eq(Tbreportmodelitem::getReportmodelgroupid,groupId);
        tlqw.eq(Tbreportmodelitem::getSheadername,headName);
        List<Tbreportmodelitem> tempList = tbreportmodelitemMapper.selectList(tlqw);
        if( null == tempList || tempList.size() <= 0 ) {
            return  null;
        }
        return tempList.get(0);
    }

}
