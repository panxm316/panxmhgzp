package com.hgzp.advertising.service.ad.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgzp.advertising.emnus.approve.ApproveStatus;
import com.hgzp.advertising.emnus.file.SfileType;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.pagemodel.ad.dto.AdResourceApplicationDTO;
import com.hgzp.advertising.pagemodel.ad.dto.AdResourceFilesDTO;
import com.hgzp.advertising.pagemodel.ad.vo.AdCustomerResourceVO;
import com.hgzp.advertising.pagemodel.ad.vo.AdResourceApplicationVO;
import com.hgzp.advertising.pagemodel.flow.dto.ProcessInstanceDTO;
import com.hgzp.advertising.pagemodel.system.dto.EmpAuthorityDTO;
import com.hgzp.advertising.service.ad.TwadresourceadorderServiceI;
import com.hgzp.advertising.service.ad.TwadresourceapplicationServiceI;
import com.hgzp.advertising.service.flow.IProcessInstanceService;
import com.hgzp.advertising.service.flow.TbflowServiceI;
import com.hgzp.advertising.service.flow.TbflowconditionServiceI;
import com.hgzp.common.flowable.constants.FormTypeEnum;
import com.hgzp.core.constant.SecurityConstants;
import com.hgzp.core.emnus.FlowTypes;
import com.hgzp.core.exception.DataNotExistException;
import com.hgzp.core.model.*;
import com.hgzp.core.page.Json;
import com.hgzp.mapper.ad.TwadresourceapplicationMapper;
import com.hgzp.mapper.ad.TwadresourcefilesMapper;
import com.hgzp.mapper.resource.TwresourcesMapper;
import com.hgzp.utils.WebUtil;
import com.hgzp.utils.file.UfileUtil;
import com.hgzp.utils.model.LoginUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * <p>
 * 广告资源申请表 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-11-10
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TwadresourceapplicationServiceImpl extends ServiceImpl<TwadresourceapplicationMapper,
        Twadresourceapplication> implements TwadresourceapplicationServiceI {
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;
    @Autowired
    TwadresourceapplicationMapper twadresourceapplicationMapper;
    @Autowired
    TwadresourcefilesMapper twadresourcefilesMapper;
    @Autowired
    TwresourcesMapper twresourcesMapper;
    @Autowired
    private TbflowconditionServiceI flowconditionServiceI;
    @Resource
    private IProcessInstanceService processInstanceService;
    @Autowired
    private TbflowServiceI tbflowServiceI;
    @Autowired
    private TwadresourceadorderServiceI twadresourceadorderServiceI;

    @Override
    public IPage<AdResourceApplicationDTO> getAdResourceApplicationPageList(Page<Twadresourceapplication> page,
                                                                            AdResourceApplicationVO query) throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        HttpServletRequest request = WebUtil.getHttpServletRequest();
        LambdaQueryWrapper<Twadresourceapplication> lqw = Wrappers.lambdaQuery();
        // 如果不是领导，只能查看自己的资源
        lqw.eq(!user.getBlead(), Twadresourceapplication::getEmployid, user.getUserid());
        // 如果是部门负责人，则根据权限查看子部门人员资源，如果为空则查看当前人员所在部门下的所有资源
        if (user.getBlead()) {
            EmpAuthorityDTO empAuthorityDTO = (EmpAuthorityDTO) request.getAttribute(SecurityConstants
                    .ROLE_PERMISSION);
            List<Long> deptlist = empAuthorityDTO.getDeptIdList();
            // 如果为空则显示全部
            if (deptlist.size() > 0) {
                lqw.in(user.getBlead(), Twadresourceapplication::getDeptid, deptlist);
            }
            // 只有部门领导才能根据人员检索信息
            lqw.eq(query.getEmployid() != null, Twadresourceapplication::getEmployid, query.getEmployid());
        }
        lqw.and(StrUtil.isNotBlank(query.getQueryKey()), i -> i.like(Twadresourceapplication::getSadtitle,
                        query.getQueryKey())
                .or()
                .like(Twadresourceapplication::getCustomername, query.getQueryKey()));
        if (CollectionUtil.isNotEmpty(query.getCustomerIds())) {
            lqw.in(Twadresourceapplication::getCustomerid, query.getCustomerIds());
        } else {
            lqw.eq(query.getCustomerid() != null, Twadresourceapplication::getCustomerid, query.getCustomerid());
        }
        lqw.eq(query.getIapprovestatus() != null, Twadresourceapplication::getIapprovestatus,
                query.getIapprovestatus());
        lqw.eq(query.getIcusttype() != null, Twadresourceapplication::getIcusttype, query.getIcusttype());
        lqw.ge(query.getStartTime() != null, Twadresourceapplication::getDstartdate, query.getStartTime());
        if (query.getEndTime() != null) {
            lqw.lt(Twadresourceapplication::getDstartdate, DateUtil.offsetDay(query.getEndTime(), 1));
        }
        // 快速上版(快速预约)只显示已经审核通过的数据
        lqw.and(i -> i.eq(Twadresourceapplication::getBquickly, true)
                .eq(Twadresourceapplication::getIapprovestatus, ApproveStatus.APPROVE_PASS.key)
                .or()
                .eq(Twadresourceapplication::getBquickly, false)
        );
        IPage<Twadresourceapplication> pages = twadresourceapplicationMapper.selectPage(page, lqw);
        IPage<AdResourceApplicationDTO> resulepage = new Page<>();
        List<Twadresourceapplication> adresourceapplicationList = pages.getRecords();
        List<AdResourceApplicationDTO> result = convertAdResourceApplicationToDTOs(adresourceapplicationList);
        resulepage.setRecords(result);
        resulepage.setTotal(pages.getTotal());
        return resulepage;
    }


    /**
     * 广告资源申请POJO转DTO
     * 方法功能:有查询操作，涉及Twadresourcefiles广告资源文件表
     *
     * @param adresourceapplicationList
     * @return java.util.List<com.hgzp.advertising.pagemodel.ad.dto.AdResourceApplicationDTO>
     * @author yanz
     * @date 2024/1/5 16:24
     */
    public List<AdResourceApplicationDTO> convertAdResourceApplicationToDTOs(List<Twadresourceapplication> adresourceapplicationList) {
        List<AdResourceApplicationDTO> result = new ArrayList<>();
        for (Twadresourceapplication record : adresourceapplicationList) {
            AdResourceApplicationDTO resourceApplicationDTO = new AdResourceApplicationDTO(record);
            LambdaQueryWrapper<Twadresourcefiles> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Twadresourcefiles::getAdresourceapplicationid, record.getId());
            List<Twadresourcefiles> twadresourcefilesList = twadresourcefilesMapper.selectList(queryWrapper);
            List<AdResourceFilesDTO> filesDTOS = new ArrayList<>();
            for (Twadresourcefiles file : twadresourcefilesList) {
                AdResourceFilesDTO adResourceFilesDTO = new AdResourceFilesDTO();
                BeanUtils.copyProperties(file, adResourceFilesDTO);
                String url = UfileUtil.getStaticUrl(file.getSfileid(), file.getSfileformat());
                adResourceFilesDTO.setDurl(url);
                filesDTOS.add(adResourceFilesDTO);
            }
            resourceApplicationDTO.setFilelist(filesDTOS);
            result.add(resourceApplicationDTO);
        }
        return result;
    }

    @Override
    public Long saveAdResourceApplication(AdResourceApplicationDTO entity) throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        Long adresourceapplicationid = IdUtil.getSnowflakeNextId();
        entity.setId(adresourceapplicationid);

        Twadresourceapplication twadresourceapplication = new Twadresourceapplication();
        BeanUtils.copyProperties(entity, twadresourceapplication);
        twadresourceapplication.setDapplytime(new Date());
        twadresourceapplication.setVersion(0L);
        twadresourceapplication.setIapprovestatus(ApproveStatus.APPROVE_EDIT.key);
        if (StringUtils.hasText(entity.getFlowid())) {
            twadresourceapplication.setIapprovestatus(ApproveStatus.APPROVE_EDITING.key);
        }

        innerInterceptor.recoredLog();
        twadresourceapplicationMapper.insert(twadresourceapplication);
        List<AdResourceFilesDTO> fileModels = entity.getFilelist();
        for (AdResourceFilesDTO file : fileModels) {
            Twadresourcefiles twadresourcefiles = new Twadresourcefiles();
            BeanUtils.copyProperties(file, twadresourcefiles);
            twadresourcefiles.setId(IdUtil.getSnowflakeNextId());
            twadresourcefiles.setCreateempid(user.getUserid());
            twadresourcefiles.setCreateempname(user.getUsername());
            twadresourcefiles.setAdresourceapplicationid(adresourceapplicationid);
            // 文件审批状态修改
            twadresourcefiles.setIapprovestatus(ApproveStatus.APPROVE_EDIT.key);
            if (StringUtils.hasText(entity.getFlowid())) {
                twadresourceapplication.setIapprovestatus(ApproveStatus.APPROVE_EDITING.key);
            }
            twadresourcefiles.setDcreatetime(new Date());
            innerInterceptor.recoredLog();
            twadresourcefilesMapper.insert(twadresourcefiles);
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
        return adresourceapplicationid;
    }

    @Override
    public void updateAdResourceApplication(AdResourceApplicationDTO entity) throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        Long adresourceapplicationid = entity.getId();
        Twadresourceapplication twadresourceapplication = new Twadresourceapplication();
        BeanUtils.copyProperties(entity, twadresourceapplication);
        if (StringUtils.hasText(entity.getFlowid())) {
            twadresourceapplication.setIapprovestatus(ApproveStatus.APPROVE_EDITING.key);
        }
        innerInterceptor.recoredLog();
        int icount = twadresourceapplicationMapper.updateById(twadresourceapplication);
        if (icount == 0) {
            throw new Exception("数据已被修改，请刷新后重试");
        }
        List<AdResourceFilesDTO> fileModels = entity.getFilelist();
        for (AdResourceFilesDTO filesDTO : fileModels) {
            // 已删除附件删除处理
            if (filesDTO.getBdelete() != null && filesDTO.getBdelete()) {
                if (filesDTO.getId() != null) {
                    innerInterceptor.recoredLog();
                    twadresourcefilesMapper.deleteById(filesDTO.getId());
                }
                continue;
            }
            Twadresourcefiles twadresourcefiles = new Twadresourcefiles();
            BeanUtils.copyProperties(filesDTO, twadresourcefiles);
            // 已有附件直接更新
            if (filesDTO.getId() != null) {
                innerInterceptor.recoredLog();
                twadresourcefilesMapper.updateById(twadresourcefiles);
                continue;
            }
            // 新增附件插入
            twadresourcefiles.setId(IdUtil.getSnowflakeNextId());
            twadresourcefiles.setAdresourceapplicationid(adresourceapplicationid);
            twadresourcefiles.setCreateempid(user.getUserid());
            twadresourcefiles.setCreateempname(user.getUsername());
            // 文件审批状态修改
            twadresourcefiles.setIapprovestatus(ApproveStatus.APPROVE_EDIT.key);
            if (StringUtils.hasText(entity.getFlowid())) {
                twadresourceapplication.setIapprovestatus(ApproveStatus.APPROVE_EDITING.key);
            }
            twadresourcefiles.setDcreatetime(new Date());
            innerInterceptor.recoredLog();
            twadresourcefilesMapper.insert(twadresourcefiles);
            Long count = new LambdaQueryChainWrapper<>(twresourcesMapper)
                    .eq(filesDTO.getSfileid() != null, Twresources::getSfileid, filesDTO.getSfileid())
                    .count();
            if (count > 0) {
                continue;
            }
            Twresources twresources = new Twresources();
            twresources.setId(IdUtil.getSnowflakeNextId());
            twresources.setSfileformat(filesDTO.getSfileformat());
            twresources.setSfileid(filesDTO.getSfileid());
            twresources.setSfilesize(filesDTO.getSfilesize());
            twresources.setSoriginalfile(filesDTO.getSoriginalfile());
            twresources.setDcreatetime(new Date());
            twresources.setSfiletype(filesDTO.getSfiletype());
            twresources.setEmployid(user.getUserid());
            innerInterceptor.recoredLog();
            twresourcesMapper.insert(twresources);
        }
    }

    @Override
    public void deleteAdResourceApplication(String ids) throws Exception {
        List<String> idList = Arrays.asList(ids.split(","));
        for (String id : idList) {
            Twadresourceapplication twadresourceapplication = twadresourceapplicationMapper.selectById(id);
            if (twadresourceapplication.getIapprovestatus() != ApproveStatus.APPROVE_EDIT.key &&
                    twadresourceapplication.getIapprovestatus() != ApproveStatus.APPROVE_REJECT.key) {
                throw new Exception("广告资源已经提交审核，不允许删除");
            }
            LambdaQueryWrapper<Twadresourcefiles> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Twadresourcefiles::getAdresourceapplicationid, id);
            List<Twadresourcefiles> twadresourcefilesList = twadresourcefilesMapper.selectList(queryWrapper);
            for (Twadresourcefiles file : twadresourcefilesList) {
                innerInterceptor.recoredLog();
                twadresourcefilesMapper.deleteById(file);
            }
        }

        innerInterceptor.recoredLog();
        twadresourceapplicationMapper.deleteBatchIds(idList);
    }

    @Override
    public void saveCheckInfo(AdResourceApplicationDTO entity) throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        Twadresourceapplication twadresourceapplication = new Twadresourceapplication();
        BeanUtils.copyProperties(entity, twadresourceapplication);
        innerInterceptor.recoredLog();
        twadresourceapplicationMapper.updateById(twadresourceapplication);

    }

    @Override
    public void submitCheck(String ids, String flowid) throws Exception {
        // 先将状态改为审核中
        updateCheck(ids, flowid);
        // 再创建流程将流程id保存到申请表中
        addFlowApplication(ids, flowid);
    }

    @Override
    public void updateCheck(String ids, String flowid) throws Exception {
        List<String> idList = Arrays.asList(ids.split(","));
        for (String id : idList) {
            Twadresourceapplication twadresourceapplication = twadresourceapplicationMapper.selectById(id);
            twadresourceapplication.setIapprovestatus(ApproveStatus.APPROVE_EDITING.key);

            innerInterceptor.recoredLog();
            twadresourceapplicationMapper.updateById(twadresourceapplication);
            // 文件审批状态修改
            LambdaQueryWrapper<Twadresourcefiles> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Twadresourcefiles::getAdresourceapplicationid, id);
            List<Twadresourcefiles> twadresourcefilesList = twadresourcefilesMapper.selectList(queryWrapper);
            for (Twadresourcefiles file : twadresourcefilesList) {
                file.setIapprovestatus(ApproveStatus.APPROVE_EDITING.key);
                innerInterceptor.recoredLog();
                twadresourcefilesMapper.updateById(file);
            }
        }
    }

    @Override
    public void addFlowApplication(String ids, String flowid) throws Exception {
        List<String> idList = Arrays.asList(ids.split(","));
        //判断是否需要审批 需要根据流程key获取一下tbflow，如果返回是null，则提示需要设置审批流程，
        // 如果返回的 tbflow的 bactive为0，则直接通过，不需要走审批流程，如果为1时，提交审批流程
        Tbflow flowInfo = tbflowServiceI.getFlowTypeByKey(FlowTypes.FLOW_ADSOURCE.getKey());
        if (flowInfo == null) {
            throw new DataNotExistException("请设置审批流程");
        }
        if (!flowInfo.getBactive()) {
            for (String id : idList) {
                Twadresourceapplication twadresourceapplication = twadresourceapplicationMapper.selectById(id);
                twadresourceapplication.setIapprovestatus(ApproveStatus.APPROVE_PASS.key);
                innerInterceptor.recoredLog();
                twadresourceapplicationMapper.updateById(twadresourceapplication);
            }
        } else {
            for (String id : idList) {
                Twadresourceapplication twadresourceapplication = twadresourceapplicationMapper.selectById(id);

                List<Tbflowcondition> flowConditionList =
                        flowconditionServiceI.getFlowConditionList(FlowTypes.FLOW_ADSOURCE.getKey());
                Map<String, Object> params = new HashMap<>();
                params.put("businessId", id);
                params.put("businessName", twadresourceapplication.getSadtitle());
                for (Tbflowcondition flowCondition : flowConditionList) {
                    if (FormTypeEnum.SELECT_DEPT.getType().equals(flowCondition.getSconditiontype())) {
                        params.put(flowCondition.getSconditionkey(), twadresourceapplication.getDeptid().toString());
                    }
                }
                Json<String> json = processInstanceService.startProcessInstanceByFlowTypes(flowid, params,
                        FlowTypes.FLOW_ADSOURCE);
                if (!json.isSuccess()) {
                    twadresourceapplication.setIapprovestatus(ApproveStatus.APPROVE_EDIT.key);
                    innerInterceptor.recoredLog();
                    twadresourceapplicationMapper.updateById(twadresourceapplication);
                    // 文件审批状态修改
                    LambdaQueryWrapper<Twadresourcefiles> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(Twadresourcefiles::getAdresourceapplicationid, id);
                    List<Twadresourcefiles> twadresourcefilesList = twadresourcefilesMapper.selectList(queryWrapper);
                    for (Twadresourcefiles file : twadresourcefilesList) {
                        file.setIapprovestatus(ApproveStatus.APPROVE_EDIT.key);
                        innerInterceptor.recoredLog();
                        twadresourcefilesMapper.updateById(file);
                    }
                    throw new Exception(json.getMsg());
                }
                twadresourceapplication.setApplicationid(json.getObj());
                innerInterceptor.recoredLog();
                twadresourceapplicationMapper.updateById(twadresourceapplication);
            }
        }
    }

    @Override
    public void updateCheckInfo(String applicationid, String businessId, boolean result, String approveDesc) {
        try {
            Twadresourceapplication twadresourceapplication = twadresourceapplicationMapper.selectById(businessId);
            if (twadresourceapplication == null) {
                throw new Exception("数据已被修改，请刷新后重试");
            }
            twadresourceapplication.setIapprovestatus(result ? ApproveStatus.APPROVE_PASS.key :
                    ApproveStatus.APPROVE_REJECT.key);
            twadresourceapplication.setApplicationid(applicationid);
            twadresourceapplication.setSapprovalopinions(approveDesc);
            innerInterceptor.recoredLog();
            if (twadresourceapplicationMapper.updateById(twadresourceapplication) == 0) {
                throw new Exception("数据已被修改，请刷新后重试");
            }
            // 文件审批状态修改
            LambdaQueryWrapper<Twadresourcefiles> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Twadresourcefiles::getAdresourceapplicationid, businessId);
            List<Twadresourcefiles> twadresourcefilesList = twadresourcefilesMapper.selectList(queryWrapper);
            for (Twadresourcefiles file : twadresourcefilesList) {
                file.setIapprovestatus(result ? ApproveStatus.APPROVE_PASS.key : ApproveStatus.APPROVE_REJECT.key);
                innerInterceptor.recoredLog();
                twadresourcefilesMapper.updateById(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Json<AdResourceApplicationDTO> getAdResourceApplicationById(String id) throws Exception {
        Twadresourceapplication twadresourceapplication = twadresourceapplicationMapper.selectById(id);
        AdResourceApplicationDTO resourceApplicationDTO = new AdResourceApplicationDTO(twadresourceapplication);
        LambdaQueryWrapper<Twadresourcefiles> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Twadresourcefiles::getAdresourceapplicationid, id);
        List<Twadresourcefiles> twadresourcefilesList = twadresourcefilesMapper.selectList(queryWrapper);
        List<AdResourceFilesDTO> filesDTOS = new ArrayList<>();
        for (Twadresourcefiles file : twadresourcefilesList) {
            AdResourceFilesDTO adResourceFilesDTO = new AdResourceFilesDTO();
            BeanUtils.copyProperties(file, adResourceFilesDTO);
            String url = UfileUtil.getStaticUrl(file.getSfileid(), file.getSfileformat());
            adResourceFilesDTO.setDurl(url);
            filesDTOS.add(adResourceFilesDTO);
        }
        resourceApplicationDTO.setFilelist(filesDTOS);
        return Json.success(resourceApplicationDTO);
    }

    @Override
    public Json<List<ProcessInstanceDTO>> getProcessInstanceById(String id) throws Exception {
        List<ProcessInstanceRecord> processRecordList =
                processInstanceService.getHistoryProcessInstanceRecordByBussinessId("businessId",
                        id);
        List<ProcessInstanceDTO> ProcessInstanceDTOs = new ArrayList<>();
        for (ProcessInstanceRecord item : processRecordList) {
            ProcessInstanceDTO instanceVo = new ProcessInstanceDTO();
            instanceVo.setProcessInstanceId(item.getProcessInstanceId());
            instanceVo.setFlowId(item.getFlowId());
            instanceVo.setCreateTime(item.getCreateTime());
            instanceVo.setFormData(item.getFormData());
            instanceVo.setResult(item.getResult());
            ProcessInstanceDTOs.add(instanceVo);
        }
        return Json.success(ProcessInstanceDTOs);
    }

    @Override
    public IPage<AdResourceApplicationDTO> getAdResourceApplicationList(Page<Twadresourceapplication> page,
                                                                        AdCustomerResourceVO query) throws Exception {
        //LoginUser user = WebUtil.getLoginUser();
        LambdaQueryWrapper<Twadresourceapplication> lqw = Wrappers.lambdaQuery();
        //如果客户id不为空，查询客户id
        if (ObjectUtil.isNotNull(query.getCustomerids())) {
            List<String> idList = Arrays.asList(query.getCustomerids().split(","));
            lqw.in(Twadresourceapplication::getCustomerid, idList);
        }
        if (ObjectUtil.isNotNull(query.getEmployid())) {
            // 只能查看自己的资源
            lqw.eq(Twadresourceapplication::getEmployid, query.getEmployid());
        }
        lqw.and(StrUtil.isNotBlank(query.getQueryKey()), i -> i.like(Twadresourceapplication::getSadtitle,
                        query.getQueryKey())
                .or()
                .like(Twadresourceapplication::getCustomername, query.getQueryKey()));
        lqw.ge(query.getStartTime() != null, Twadresourceapplication::getDstartdate, query.getStartTime());
        if (query.getEndTime() != null) {
            lqw.lt(Twadresourceapplication::getDstartdate, DateUtil.offsetDay(query.getEndTime(), 1));
        }

        IPage<Twadresourceapplication> pages = twadresourceapplicationMapper.selectPage(page, lqw);
        IPage<AdResourceApplicationDTO> resulepage = new Page<>();
        List<Twadresourceapplication> adresourceapplicationList = pages.getRecords();
        List<AdResourceApplicationDTO> result = convertAdResourceApplicationToDTOs(adresourceapplicationList);
        resulepage.setRecords(result);
        resulepage.setTotal(pages.getTotal());
        return resulepage;
    }

    @Override
    public void updateAdResourceCustomer(String customerIds, Long newcustomerId, String newcustomername) {
        try {
            String[] splitids = customerIds.split(",");
            List<Twadresourceapplication> resourceas = this.lambdaQuery()
                    .in(Twadresourceapplication::getCustomerid, splitids)
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
            throw new RuntimeException("广告资源合并失败！" + e0.getMessage());
        }
    }

    @Override
    public List<Twadresourcefiles> getResourceFilesByOrderIdForCJ(String orderid) {
        LambdaQueryWrapper<Twadresourceadorder> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Twadresourceadorder::getAdorderid, orderid);
        List<Twadresourceadorder> twadresourceadorder = twadresourceadorderServiceI.list(lqw);
        List<Long> resourceIds = twadresourceadorder.stream().map(Twadresourceadorder::getAdresourceapplicationid).collect
                (ArrayList::new, ArrayList::add, ArrayList::addAll);
        if (resourceIds.size() > 0) {
            LambdaQueryWrapper<Twadresourcefiles> lqw2 = new LambdaQueryWrapper<>();
            lqw2.in(Twadresourcefiles::getAdresourceapplicationid, resourceIds);
            lqw2.eq(Twadresourcefiles::getIfilecategory, SfileType.FileType_9.key);  //广告资源文件
            return twadresourcefilesMapper.selectList(lqw2);
        }
        return new ArrayList<>();
    }
}
