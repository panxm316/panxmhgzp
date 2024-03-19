package com.hgzp.advertising.service.statistic.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgzp.advertising.emnus.statistic.DataTypeEnum;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.pagemodel.statistic.dto.DatabackupGroupDTO;
import com.hgzp.advertising.pagemodel.statistic.vo.DataBackupDetailVO;
import com.hgzp.advertising.pagemodel.statistic.vo.DatabackupGroupVO;
import com.hgzp.advertising.service.ad.TworderitemServiceI;
import com.hgzp.advertising.service.statistic.DatabackupGroupServiceI;
import com.hgzp.core.exception.DataNotExistException;
import com.hgzp.core.model.Twdatabackupdetail1;
import com.hgzp.core.model.Twdatabackupgroup;
import com.hgzp.core.model.Tworderitem;
import com.hgzp.core.page.BaseQueryInfo;
import com.hgzp.mapper.statistic.Twdatabackupdetail1Mapper;
import com.hgzp.mapper.statistic.TwdatabackupgroupMapper;
import com.hgzp.utils.WebUtil;
import com.hgzp.utils.model.LoginUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 数据轧账总表 服务实现类
 * </p>
 *
 * @author muyn
 * @since 2024-01-19
 */
@Service
public class DatabackupGroupServiceImpl extends ServiceImpl<TwdatabackupgroupMapper, Twdatabackupgroup> implements DatabackupGroupServiceI {

    @Autowired
    private HgDataChangeRecorderInnerInterceptor innerInterceptor;
    @Autowired
    private TwdatabackupgroupMapper twdatabackupgroupMapper;
    @Autowired
    private Twdatabackupdetail1Mapper twdatabackupdetail1Mapper;
    @Autowired
    private TworderitemServiceI tworderitemServiceI;

    @Override
    public IPage<Twdatabackupgroup> getDataBackupGroupPageList(Page<Twdatabackupgroup> page, DatabackupGroupVO query) throws Exception {
        LambdaQueryWrapper<Twdatabackupgroup> lqw = Wrappers.lambdaQuery();
        lqw.ge(query.getStartTime() != null, Twdatabackupgroup::getDcreatetime, query.getStartTime());
        if (query.getEndTime() != null) {
            lqw.lt(Twdatabackupgroup::getDcreatetime, DateUtil.offsetDay(query.getEndTime(), 1));
        }
        return twdatabackupgroupMapper.selectPage(page, lqw);
    }

    @Override
    public IPage<Twdatabackupdetail1> getDataBackupDetailPageList(Page<Twdatabackupdetail1> page, DataBackupDetailVO query) throws Exception {
        Twdatabackupgroup twdatabackupgroup = twdatabackupgroupMapper.selectById(query.getDatabackupgroupid());
        if (twdatabackupgroup == null) {
            throw new DataNotExistException("数据轧账总表不存在");
        }
        String tableName = twdatabackupgroup.getSdetailtablename();
        Long pagenumb = page.getCurrent() <= 1 ? 0 : (page.getCurrent() - 1) * page.getSize();
        Date endTime = query.getEndTime() == null ? new Date() : DateUtil.offsetDay(query.getEndTime(), 1);
        List<Twdatabackupdetail1> pagelist = twdatabackupdetail1Mapper.selectByTablename(tableName, query.getStartTime(), endTime, query.getMediaid(),
                query.getQueryKey(), query.getDeptid(), query.getEmployid(), pagenumb, page.getSize());
        IPage<Twdatabackupdetail1> resulepage = new Page<>();
        resulepage.setRecords(pagelist);
        resulepage.setTotal(twdatabackupdetail1Mapper.getCountByTable(tableName, query.getStartTime(), endTime, query.getMediaid(),
                query.getQueryKey(), query.getDeptid(), query.getEmployid()));
        return resulepage;
    }

    @Override
    public long getOrderItemCount(BaseQueryInfo query) throws Exception {
        // 时间范围（开始时间可选、结束时间必选）
        if(query.getEndTime() == null){
            throw new DataNotExistException("结束时间不能为空");
        }
        Long count = tworderitemServiceI.count(Wrappers.<Tworderitem>lambdaQuery()
                .ge(query.getStartTime() != null, Tworderitem::getPrestartdate, query.getStartTime())
                .lt(Tworderitem::getPrestartdate, DateUtil.offsetDay(query.getEndTime(), 1)));
        return count;
    }

    @Override
    public void saveDataBackupDetail(DatabackupGroupDTO entity) throws Exception {
        // 时间范围（开始时间可选、结束时间必选）
        if(entity.getDenddate() == null){
            throw new DataNotExistException("结束时间不能为空");
        }
        LoginUser user = WebUtil.getLoginUser();
        String tableName = "twdatabackupdetail" + DateUtil.format(new Date(), "yyyyMMddHHmmss");
        twdatabackupdetail1Mapper.createTable(tableName);
        Twdatabackupgroup twdatabackupgroup = new Twdatabackupgroup();
        BeanUtils.copyProperties(entity, twdatabackupgroup);
        twdatabackupgroup.setSdetailtablename(tableName);
        twdatabackupgroup.setDcreatetime(new Date());
        twdatabackupgroup.setCreateempid(user.getUserid());
        twdatabackupgroup.setCreateempname(user.getUsername());
        twdatabackupgroup.setDatatype(DataTypeEnum.statistic_orderitem.getKey());
        innerInterceptor.recoredLog();
        twdatabackupgroupMapper.insert(twdatabackupgroup);
        if(entity.getDenddate() != null){
            entity.setDenddate(DateUtil.offsetDay(entity.getDenddate(), 1));
        }
        twdatabackupdetail1Mapper.updateDataBackupDetail(tableName, twdatabackupgroup.getId(), entity.getDstartdate(), entity.getDenddate());
    }
}
