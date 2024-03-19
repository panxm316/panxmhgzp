package com.hgzp.advertising.service.business.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgzp.advertising.emnus.approve.ApproveStatus;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.pagemodel.business.dto.WorkReportFilesDTO;
import com.hgzp.advertising.pagemodel.business.dto.WorkReportsDTO;
import com.hgzp.advertising.pagemodel.business.vo.CustomerWorkReportsVO;
import com.hgzp.advertising.pagemodel.business.vo.WorkReportsVO;
import com.hgzp.advertising.pagemodel.system.dto.EmpAuthorityDTO;
import com.hgzp.advertising.service.business.TwworkreportfilesServiceI;
import com.hgzp.advertising.service.business.TwworkreportsServiceI;
import com.hgzp.core.constant.SecurityConstants;
import com.hgzp.core.model.Twresources;
import com.hgzp.core.model.Twworkreportfiles;
import com.hgzp.core.model.Twworkreports;
import com.hgzp.mapper.business.TwworkreportfilesMapper;
import com.hgzp.mapper.business.TwworkreportsMapper;
import com.hgzp.mapper.resource.TwresourcesMapper;
import com.hgzp.utils.WebUtil;
import com.hgzp.utils.file.UfileUtil;
import com.hgzp.utils.model.LoginUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 工作报告 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-10-27
 */
@Service
public class TwworkreportsServiceImpl extends ServiceImpl<TwworkreportsMapper, Twworkreports> implements TwworkreportsServiceI {
    @Value("${ufile.uExtURL}")
    private String uExtURL;
    @Autowired
    TwworkreportsMapper twworkreportsMapper;
    @Autowired
    TwworkreportfilesMapper twworkreportfilesMapper;
    @Autowired
    TwworkreportfilesServiceI twworkreportfilesServiceI;
    @Autowired
    TwresourcesMapper twresourcesMapper;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;

    @Override
    public IPage<WorkReportsDTO> getWorkReportPageList(Page<Twworkreports> page, WorkReportsVO query) throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        HttpServletRequest request = WebUtil.getHttpServletRequest();
        LambdaQueryWrapper<Twworkreports> lqw = Wrappers.lambdaQuery();
        // 如果不是领导，只能查看自己的报告
        lqw.eq(!user.getBlead(), Twworkreports::getEmployid, user.getUserid());
        // 如果是部门不责任，则根据权限查看子部门人员报告，如果为空则查看当前人员所在部门下的所有报告
        if (user.getBlead()) {
            EmpAuthorityDTO empAuthorityDTO = (EmpAuthorityDTO) request.getAttribute(SecurityConstants.ROLE_PERMISSION);
            List<Long> deptlist = empAuthorityDTO.getDeptIdList();
            // 如果为空则显示全部
            if (deptlist.size() > 0) {
                lqw.in(user.getBlead(), Twworkreports::getDeptid, deptlist);
            }
        }
        lqw.eq(query.getEmployid() != null, Twworkreports::getEmployid, query.getEmployid());
        lqw.eq(query.getIworktype() != null, Twworkreports::getIworktype, query.getIworktype());
        lqw.eq(query.getIapprovestatus() != null, Twworkreports::getIapprovestatus, query.getIapprovestatus());
        lqw.ge(query.getStartTime() != null, Twworkreports::getDcreatedate, query.getStartTime());
        if (query.getEndTime() != null) {
            lqw.lt(Twworkreports::getDcreatedate, DateUtil.offsetDay(query.getEndTime(), 1));
        }
        lqw.orderByDesc(Twworkreports::getDcreatedate);
        IPage<Twworkreports> pages = twworkreportsMapper.selectPage(page, lqw);
        IPage<WorkReportsDTO> resulepage = new Page<>();
        List<WorkReportsDTO> result = new ArrayList<>();
        for (Twworkreports record : pages.getRecords()) {
            WorkReportsDTO workReportsDTO = new WorkReportsDTO(record);
            LambdaQueryWrapper<Twworkreportfiles> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Twworkreportfiles::getWorkreportid, record.getId());
            List<Twworkreportfiles> twworkreportfilesList = twworkreportfilesMapper.selectList(queryWrapper);
            List<WorkReportFilesDTO> twworkreportfiles = new ArrayList<>();
            for (Twworkreportfiles file : twworkreportfilesList) {
                WorkReportFilesDTO workReportFilesDTO = new WorkReportFilesDTO();
                BeanUtils.copyProperties(file, workReportFilesDTO);
                String url = UfileUtil.getStaticUrl(file.getSfileid(), file.getSfileformat());
                workReportFilesDTO.setDurl(url);
                twworkreportfiles.add(workReportFilesDTO);
            }
            workReportsDTO.setFilelist(twworkreportfiles);
            result.add(workReportsDTO);
        }
        resulepage.setRecords(result);
        resulepage.setTotal(pages.getTotal());
        return resulepage;
    }

    @Override
    public void saveWorkReport(WorkReportsDTO entity) throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        Long workid = IdUtil.getSnowflakeNextId();
        entity.setId(workid);

        Twworkreports twworkreports = new Twworkreports();
        BeanUtils.copyProperties(entity, twworkreports);
        twworkreports.setDcreatedate(new Date());
        twworkreports.setBuse(true);

        innerInterceptor.recoredLog();
        twworkreportsMapper.insert(twworkreports);
        List<WorkReportFilesDTO> fileModels = entity.getFilelist();
        for (WorkReportFilesDTO file : fileModels) {
            Twworkreportfiles twworkreportfiles = new Twworkreportfiles();
            BeanUtils.copyProperties(file, twworkreportfiles);
            twworkreportfiles.setId(IdUtil.getSnowflakeNextId());
            twworkreportfiles.setWorkreportid(workid);
            twworkreportfiles.setCreateempid(user.getUserid());
            twworkreportfiles.setDcreatetime(new Date());
            twworkreportfiles.setBdelete(false);
            innerInterceptor.recoredLog();
            twworkreportfilesMapper.insert(twworkreportfiles);
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
    public void updateWorkReport(WorkReportsDTO entity) throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        Long workid = entity.getId();
        Twworkreports twworkreports = new Twworkreports();
        BeanUtils.copyProperties(entity, twworkreports);
        innerInterceptor.recoredLog();
        twworkreportsMapper.updateById(twworkreports);
        List<WorkReportFilesDTO> fileModels = entity.getFilelist();
        for (WorkReportFilesDTO file : fileModels) {
            // 已删除附件删除处理
            if (file.getBdelete()) {
                if (file.getId() != null) {
                    innerInterceptor.recoredLog();
                    twworkreportfilesMapper.deleteById(file.getId());
                }
                continue;
            }
            Twworkreportfiles twworkreportfiles = new Twworkreportfiles();
            BeanUtils.copyProperties(file, twworkreportfiles);
            // 已有附件直接更新
            if (file.getId() != null) {
                innerInterceptor.recoredLog();
                twworkreportfilesMapper.updateById(twworkreportfiles);
                continue;
            }
            // 新增附件插入
            twworkreportfiles.setId(IdUtil.getSnowflakeNextId());
            twworkreportfiles.setWorkreportid(workid);
            twworkreportfiles.setCreateempid(user.getUserid());
            twworkreportfiles.setDcreatetime(new Date());
            twworkreportfiles.setBdelete(false);
            innerInterceptor.recoredLog();
            twworkreportfilesMapper.insert(twworkreportfiles);
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
    public void saveCheckInfo(WorkReportsDTO entity) throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        Twworkreports twworkreports = new Twworkreports();
        BeanUtils.copyProperties(entity, twworkreports);
        twworkreports.setDcheckdate(new Date());
        twworkreports.setIcheckerid(user.getUserid());
        twworkreports.setScheckername(user.getUsername());
        innerInterceptor.recoredLog();
        twworkreportsMapper.updateById(twworkreports);
    }

    @Override
    public void deleteWorkReport(String ids) throws Exception {
        List<String> idList = Arrays.asList(ids.split(","));
        for (String id : idList) {
            Twworkreports twworkreports = twworkreportsMapper.selectById(id);
            if (twworkreports.getIapprovestatus() != ApproveStatus.APPROVE_EDIT.key) {
                throw new Exception("工作报告已经提交审核，不允许删除");
            }
            LambdaQueryWrapper<Twworkreportfiles> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Twworkreportfiles::getWorkreportid, id);
            List<Twworkreportfiles> twworkreportfilesList = twworkreportfilesMapper.selectList(queryWrapper);
            for (Twworkreportfiles file : twworkreportfilesList) {
                innerInterceptor.recoredLog();
                twworkreportfilesMapper.deleteById(file);
            }
        }

        innerInterceptor.recoredLog();
        twworkreportsMapper.deleteBatchIds(idList);
    }

    @Override
    public void updateCheck(String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        for (String id : idList) {
            Twworkreports twworkreports = twworkreportsMapper.selectById(id);
            if (twworkreports != null) {
                twworkreports.setIapprovestatus(ApproveStatus.APPROVE_EDITING.key);
                innerInterceptor.recoredLog();
                twworkreportsMapper.updateById(twworkreports);
            }
        }
    }

    @Override
    public void updateWorkreportCustomer(String customerIds, Long newcustomerId, String newcustomername) throws Exception {
        try {
            String[] splitids = customerIds.split(",");
            List<Twworkreports> resourceas = this.lambdaQuery()
                    .in(Twworkreports::getCustomerid, splitids)
                    .list();
            if (resourceas.size() > 0) {
                resourceas.forEach(item -> {
                    item.setCustomerid(newcustomerId);
                    item.setCustomername(newcustomername);
                });

                innerInterceptor.recoredLog();
                this.updateBatchById(resourceas);
            }
        } catch (Exception e0) {
            throw new RuntimeException("工作报告合并失败！" + e0.getMessage());
        }
    }

    @Override
    public IPage<WorkReportsDTO> getCustomerWorkReportPageList(Page<Twworkreports> page, CustomerWorkReportsVO query) throws Exception {
        LambdaQueryWrapper<Twworkreports> lqw = Wrappers.lambdaQuery();
        lqw.eq(query.getCustomerid() != null, Twworkreports::getCustomerid, query.getCustomerid());
        lqw.eq(query.getIworktype() != null, Twworkreports::getIworktype, query.getIworktype());
        lqw.ge(query.getStartTime() != null, Twworkreports::getDcreatedate, query.getStartTime());
        //多个关键字以空格间隔
        if (StrUtil.isNotBlank(query.getQueryKey())) {
            String[] keywords = query.getQueryKey().trim().split(" ");
            lqw.and(item -> {
                for (String skey : keywords) {
                    item.or(StrUtil.isNotBlank(skey), i -> i.like(Twworkreports::getScheckername, skey)
                            .or().like(Twworkreports::getDeptname, skey)
                            .or().like(Twworkreports::getEmployname, skey)
                            .or().like(Twworkreports::getSworkcontent, skey)
                    );
                }
            });
        }
        if (query.getEndTime() != null) {
            lqw.lt(Twworkreports::getDcreatedate, DateUtil.offsetDay(query.getEndTime(), 1));
        }
        lqw.orderByDesc(Twworkreports::getDcreatedate);
        IPage<Twworkreports> pages = twworkreportsMapper.selectPage(page, lqw);
        IPage<WorkReportsDTO> resulepage = new Page<>();
        List<WorkReportsDTO> result = new ArrayList<>();
        for (Twworkreports record : pages.getRecords()) {
            WorkReportsDTO workReportsDTO = new WorkReportsDTO(record);
            LambdaQueryWrapper<Twworkreportfiles> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Twworkreportfiles::getWorkreportid, record.getId());
            List<Twworkreportfiles> twworkreportfilesList = twworkreportfilesMapper.selectList(queryWrapper);
            List<WorkReportFilesDTO> twworkreportfiles = new ArrayList<>();
            for (Twworkreportfiles file : twworkreportfilesList) {
                WorkReportFilesDTO workReportFilesDTO = new WorkReportFilesDTO();
                BeanUtils.copyProperties(file, workReportFilesDTO);
                String url = UfileUtil.getStaticUrl(file.getSfileid(), file.getSfileformat());
                workReportFilesDTO.setDurl(url);
                twworkreportfiles.add(workReportFilesDTO);
            }
            workReportsDTO.setFilelist(twworkreportfiles);
            result.add(workReportsDTO);
        }
        resulepage.setRecords(result);
        resulepage.setTotal(pages.getTotal());
        return resulepage;
    }

    @Override
    public WorkReportsDTO getWorkReportById(String id) throws Exception {
        Twworkreports twworkreports = twworkreportsMapper.selectById(id);
        WorkReportsDTO workReportsDTO = new WorkReportsDTO(twworkreports);
        LambdaQueryWrapper<Twworkreportfiles> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Twworkreportfiles::getWorkreportid, id);
        List<Twworkreportfiles> twworkreportfilesList = twworkreportfilesMapper.selectList(queryWrapper);
        List<WorkReportFilesDTO> twworkreportfiles = new ArrayList<>();
        for (Twworkreportfiles file : twworkreportfilesList) {
            WorkReportFilesDTO workReportFilesDTO = new WorkReportFilesDTO();
            BeanUtils.copyProperties(file, workReportFilesDTO);
            String url = UfileUtil.getStaticUrl(file.getSfileid(), file.getSfileformat());
            workReportFilesDTO.setDurl(url);
            twworkreportfiles.add(workReportFilesDTO);
        }
        workReportsDTO.setFilelist(twworkreportfiles);
        return workReportsDTO;
    }
}
