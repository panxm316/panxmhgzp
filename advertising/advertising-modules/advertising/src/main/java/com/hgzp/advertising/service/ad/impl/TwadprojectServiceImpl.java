package com.hgzp.advertising.service.ad.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.emnus.approve.ApproveStatus;
import com.hgzp.advertising.emnus.contract.ContractType;
import com.hgzp.advertising.emnus.contract.StampType;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.pagemodel.ad.dto.TwadprojectDTO;
import com.hgzp.advertising.pagemodel.ad.vo.AdprojectVO;
import com.hgzp.advertising.pagemodel.ad.vo.AdprojectfilesVO;
import com.hgzp.advertising.pagemodel.business.dto.OrderAndItemDTO;
import com.hgzp.advertising.pagemodel.customer.vo.CustomerBelongVo;
import com.hgzp.advertising.pagemodel.customer.vo.CustomerfilesVo;
import com.hgzp.advertising.service.ad.TwadprojectfilesServiceI;
import com.hgzp.advertising.service.ad.TworderServiceI;
import com.hgzp.advertising.service.ad.TworderitemServiceI;
import com.hgzp.advertising.service.flow.IProcessInstanceService;
import com.hgzp.advertising.service.flow.TbflowconditionServiceI;
import com.hgzp.advertising.service.system.ProduceServiceI;
import com.hgzp.core.emnus.FlowTypes;
import com.hgzp.core.model.*;
import com.hgzp.core.page.BaseQueryInfo;
import com.hgzp.core.page.Json;
import com.hgzp.mapper.ad.TwadprojectMapper;
import com.hgzp.advertising.service.ad.TwadprojectServiceI;
import com.hgzp.mapper.ad.TworderMapper;
import com.hgzp.service.common.impl.MyServiceImpl;
import com.hgzp.utils.WebUtil;
import com.hgzp.utils.model.LoginUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 广告项目 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TwadprojectServiceImpl extends MyServiceImpl<TwadprojectMapper, Twadproject> implements TwadprojectServiceI {

    @Autowired
    TwadprojectMapper adprojectMapper;
    @Autowired
    TworderMapper tworderMapper;
    @Autowired
    private TbflowconditionServiceI flowconditionServiceI;
    @Resource
    private IProcessInstanceService processInstanceService;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;
    @Autowired
    private TwadprojectfilesServiceI adprojectfilesServiceI;
    @Autowired
    private TworderitemServiceI tworderitemServiceI;
    @Autowired
    private ProduceServiceI produceServiceI;

    @Override
    public IPage<AdprojectVO> getAdProjectPageList(Page<Twadproject> page, BaseQueryInfo query) {
        /**eq：等于某个日期
         ne：不等于某个日期
         gt：大于某个日期
         ge：大于等于某个日期
         lt：小于某个日期
         le：小于等于某个日期*/
        LambdaQueryWrapper<Twadproject> lqw = Wrappers.lambdaQuery();
        //多个关键字以空格间隔  广告标题，内容，订单编号，合同号，项目名称，客户名称，经营主体 ，业务员
        if (ObjectUtil.isNotNull(query.getQueryKey())) {
            String[] keywords = query.getQueryKey().trim().split(" ");
            lqw.and(item -> {
                for (String skey : keywords) {
                    item.or(StrUtil.isNotBlank(skey), i -> i.like(Twadproject::getSname, skey)
                            .or().like(Twadproject::getScontractnum, skey)
                            .or().like(Twadproject::getSprojectcontent, skey)
                            .or().like(Twadproject::getProjectcode, skey)
                            .or().like(Twadproject::getAuthorizername, skey)
                            .or().like(Twadproject::getEmployname, skey)
                    );
                }
            });
        }

        if (query.getStartTime() == null) {
            if (query.getEndTime() != null) {
                //wm.dstartdate <= _dtEndDate
                lqw.le(Twadproject::getDstartdate, query.getEndTime());
            }
        } else {
            if (query.getEndTime() == null) {
                //(wm.dstartdate >= _dtstartDate || (wm.dstartdate < _dtEndDate && wm.denddate >= _dtstartDate))
                lqw.and(wrapper -> wrapper.and(wrapper1 -> wrapper1.ge(Twadproject::getDstartdate,
                                query.getStartTime()))
                        .or((wrapper2 -> wrapper2.ge(Twadproject::getDenddate, query.getStartTime()))));
            } else {
                // ((wm.dstartdate >= _dtstartDate && wm.dstartdate <= _dtEndDate) || (wm.dstartdate < _dtEndDate &&
                // wm.denddate >= _dtstartDate))))
                lqw.and(wrapper -> wrapper.and(wrapper1 -> wrapper1.ge(Twadproject::getDstartdate,
                                query.getStartTime()).le(Twadproject::getDstartdate,
                                DateUtil.offsetDay(query.getEndTime(), 1)))
                        .or(wrapper2 -> wrapper2.le(Twadproject::getDstartdate, DateUtil.offsetDay(query.getEndTime()
                                , 1)).ge(Twadproject::getDenddate, query.getStartTime())));
            }
        }

        lqw.like(StrUtil.isNotBlank(query.getQueryKey()), Twadproject::getSname, query.getQueryKey());
        IPage<Twadproject> adprojectPage = adprojectMapper.selectPage(page, lqw);

        IPage<AdprojectVO> adprojectPageVO = new Page<>();
        if(adprojectPage.getRecords().size() == 0){
            return adprojectPageVO;
        }

        List<AdprojectVO> lsAdprojectVO = new ArrayList<>();
        for (Twadproject item : adprojectPage.getRecords()) {
            AdprojectVO adprojectVO = new AdprojectVO();
            BeanUtils.copyProperties(item, adprojectVO);
            adprojectVO.setContracttypename(ContractType.getNameByKey(item.getIcontracttype()));
            adprojectVO.setSalecontracttypename(ContractType.getNameByKey(item.getIsalecontracttype()));
            String stamptypename = "";
            if(ObjectUtil.isNotNull(item.getIstamptype())){
                for (StampType stampType : StampType.values()) {
                    if (item.getIstamptype().toString().contains(stampType.key.toString())) {
                        stamptypename += stampType.name + " ";
                    }
                }
            }
            adprojectVO.setStamptypename(stamptypename);

            List<Twadprojectfiles> adprojectfilesList = adprojectfilesServiceI.getAdprojectfilesList(item.getId().toString());
            List<AdprojectfilesVO> lsAdprojectfilesVO = new ArrayList<>();
            adprojectfilesList.forEach(i -> {
                        AdprojectfilesVO orderAndItemDTO = new AdprojectfilesVO();
                        BeanUtils.copyProperties(i, orderAndItemDTO);
                        orderAndItemDTO.setSdurl(uWebURL + i.getSfileid());
                lsAdprojectfilesVO.add(orderAndItemDTO);
            });
            adprojectVO.setProjectfiles(lsAdprojectfilesVO);

            //成本盈余
            BigDecimal AmountreceivableCount=tworderitemServiceI.getAmountreceivableCountByprojectId(item.getId().toString());
            adprojectVO.setNprojectcostresidue(item.getNprojectcost().subtract(AmountreceivableCount));

            lsAdprojectVO.add(adprojectVO);
        }
        adprojectPageVO.setRecords(lsAdprojectVO);
        adprojectPageVO.setTotal(adprojectPage.getTotal());
        return adprojectPageVO;
    }

    @Override
    public List<AdprojectVO> getAdProjectList() {
        List<Twadproject> lsProjects = new LambdaQueryChainWrapper<>(adprojectMapper)
                .ge(Twadproject::getDenddate, new Date())
                .eq(Twadproject::getBprojectcomplete, false)
                .orderByAsc(Twadproject::getIsort).list();
        List<AdprojectVO> lsAdprojectVO = new ArrayList<>();
        for (Twadproject item : lsProjects) {
            AdprojectVO adprojectVO = new AdprojectVO();
            BeanUtils.copyProperties(item, adprojectVO);
            adprojectVO.setContracttypename(ContractType.getNameByKey(item.getIcontracttype()));
            adprojectVO.setSalecontracttypename(ContractType.getNameByKey(item.getIsalecontracttype()));
            String stamptypename = "";
            if(ObjectUtil.isNotNull(item.getIstamptype())) {
                for (StampType stampType : StampType.values()) {
                    if (item.getIstamptype().toString().contains(stampType.key.toString())) {
                        stamptypename += stampType.name + " ";
                    }
                }
            }
            adprojectVO.setStamptypename(stamptypename);
            List<Twadprojectfiles> adprojectfilesList = adprojectfilesServiceI.getAdprojectfilesList(item.getId().toString());
            List<AdprojectfilesVO> lsAdprojectfilesVO = new ArrayList<>();
            adprojectfilesList.forEach(i -> {
                AdprojectfilesVO orderAndItemDTO = new AdprojectfilesVO();
                BeanUtils.copyProperties(i, orderAndItemDTO);
                orderAndItemDTO.setSdurl(uWebURL + i.getSfileid());
                lsAdprojectfilesVO.add(orderAndItemDTO);
            });
            adprojectVO.setProjectfiles(lsAdprojectfilesVO);

            //成本盈余
            BigDecimal AmountreceivableCount=tworderitemServiceI.getAmountreceivableCountByprojectId(item.getId().toString());
            adprojectVO.setNprojectcostresidue(item.getNprojectcost().subtract(AmountreceivableCount));

            lsAdprojectVO.add(adprojectVO);
        }
        return lsAdprojectVO;
    }

    @Override
    public AdprojectVO getByAdProjectId(String id) {
        Twadproject adproject = adprojectMapper.selectById(id);
        AdprojectVO adprojectVO = new AdprojectVO();
        BeanUtils.copyProperties(adproject, adprojectVO);
        adprojectVO.setContracttypename(ContractType.getNameByKey(adproject.getIcontracttype()));
        adprojectVO.setSalecontracttypename(ContractType.getNameByKey(adproject.getIsalecontracttype()));
        String stamptypename = "";
        if(ObjectUtil.isNotNull(adproject.getIstamptype())) {
            for (StampType stampType : StampType.values()) {
                if (adproject.getIstamptype().toString().contains(stampType.key.toString())) {
                    stamptypename += stampType.name + " ";
                }
            }
        }
        adprojectVO.setStamptypename(stamptypename);
        List<Twadprojectfiles> adprojectfilesList = adprojectfilesServiceI.getAdprojectfilesList(id);
        List<AdprojectfilesVO> lsAdprojectfilesVO = new ArrayList<>();
        adprojectfilesList.forEach(i -> {
            AdprojectfilesVO orderAndItemDTO = new AdprojectfilesVO();
            BeanUtils.copyProperties(i, orderAndItemDTO);
            orderAndItemDTO.setSdurl(uWebURL + i.getSfileid());
            lsAdprojectfilesVO.add(orderAndItemDTO);
        });
        adprojectVO.setProjectfiles(lsAdprojectfilesVO);

        return adprojectVO;
    }

    @Override
    public String deleteAdProject(String ids) {
        String[] split = ids.split(",");
        List<Twadproject> lsProjects = new LambdaQueryChainWrapper<>(adprojectMapper).in(Twadproject::getId,
                split).list();
        String sInfo = "";
        for (Twadproject item : lsProjects) {
            //判断是否被用
            Long count = new LambdaQueryChainWrapper<>(tworderMapper)
                    .eq(Tworder::getAdprojectid, item.getId())
                    .count();
            if (count == 0) {
                innerInterceptor.recoredLog();
                adprojectMapper.deleteById(item.getId());
                adprojectfilesServiceI.deleteAdprojectfilesByAdprojectid(item.getId().toString());
            } else {
                sInfo += item.getSname() + "项目被订单使用，不能删除！";
            }
        }
        return sInfo;
    }
    @Value("${ufile.uWebURL}")
    private String uWebURL;

    @Override
    public void updateAdProject(TwadprojectDTO adprojectDTO) throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        Twadproject adproject = new Twadproject();
        BeanUtils.copyProperties(adprojectDTO, adproject);

        adproject.setCreateempid(user.getUserid());
        adproject.setScreatename(user.getUsername());
        Date createdate = new Date();
        adproject.setDcreatedate(createdate);
       // adproject.setIapprovestatus(ApproveStatus.APPROVE_PASS.getKey());

        innerInterceptor.recoredLog();
        adprojectMapper.insert(adproject);
        //项目附件
        if(adprojectDTO.getProjectfiles().size()>0){
            List<Twadprojectfiles> lsProjectfiles = adprojectfilesServiceI.getAdprojectfilesList(adproject.getId().toString());
            List<Long> lsfileIds =
                    adprojectDTO.getProjectfiles().stream().map(Twadprojectfiles::getId).collect(Collectors.toList());
            //过滤不存的文件
            String notExistfileIds =
                    lsProjectfiles.stream().filter(item -> !lsfileIds.contains(item.getId())).map(Twadprojectfiles::getId).map(Object::toString).collect(Collectors.joining(","));
            //删除不存在的文件
            if (StrUtil.isNotBlank(notExistfileIds)) {
                adprojectfilesServiceI.deleteAdprojectfiles(notExistfileIds);
            }
            for (Twadprojectfiles item : adprojectDTO.getProjectfiles()) {
                item.setAdprojectid(adproject.getId());
                item.setEmployid(user.getUserid());
                item.setDcreatetime(createdate);

                innerInterceptor.recoredLog();
                if(item.getId() != null) {
                    adprojectfilesServiceI.updateAdprojectfiles(item);
                }else{
                    adprojectfilesServiceI.saveAdprojectfiles(item);
                }
            }

        }

    }

    @Override
    public void saveAdProject(TwadprojectDTO adprojectDTO) throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        Twadproject adproject = new Twadproject();
        BeanUtils.copyProperties(adprojectDTO, adproject);
        adproject.setProjectcode(produceServiceI.getXmNo());

        adproject.setCreateempid(user.getUserid());
        adproject.setScreatename(user.getUsername());
        Date createdate = new Date();
        adproject.setDcreatedate(createdate);
      //  adproject.setIapprovestatus(ApproveStatus.APPROVE_PASS.getKey());

        innerInterceptor.recoredLog();
        adprojectMapper.insert(adproject);

        //项目附件
        if(adprojectDTO.getProjectfiles().size()>0){
            for (Twadprojectfiles item : adprojectDTO.getProjectfiles()) {
                item.setAdprojectid(adproject.getId());
                item.setEmployid(user.getUserid());
                item.setDcreatetime(createdate);

                innerInterceptor.recoredLog();
                adprojectfilesServiceI.saveAdprojectfiles(item);
            }
        }
    }

    @Override
    public Json endAdProject(String ids) {
        Json json = new Json();
        try {

            String[] split = ids.split(",");
            //判断是否被用
            List<Twadproject> lsProjects = new LambdaQueryChainWrapper<>(adprojectMapper).in(Twadproject::getId,
                    split).list();
            String sInfo = "";
            innerInterceptor.recoredLog();
            int icount = 0;
            for (Twadproject item : lsProjects) {
//                if (item.getIapprovestatus() == 0) {
//                    sInfo += ";" + item.getSname() + "还未审核，不能结项！";
//                    continue;
//                }
//                if (item.getIapprovestatus() == 1) {
//                    sInfo += ";" + item.getSname() + "审核中，不能结项！";
//                    continue;
//                }
//                if (item.getIapprovestatus() == 2) {
//                    sInfo += ";" + item.getSname() + "已结项，不能再次结项！";
//                    continue;
//                }
//                if (item.getIapprovestatus() == 3) {
//                    sInfo += ";" + item.getSname() + "被否决，不能结项！";
//                    continue;
//                }
//                if (item.getIapprovestatus() == 4) {
//                    sInfo += ";" + item.getSname() + "已撤销，不能结项！";
//                    continue;
//                }
//                if (item.getIapprovestatus() == 5) {
//                    sInfo += ";" + item.getSname() + "无效，不能结项！";
//                    continue;
//                }
                item.setBprojectcomplete(true);
                adprojectMapper.updateById(item);
                icount++;
            }
            ;
            if (sInfo.length() > 0) {
                sInfo = sInfo.substring(1);
            }
            json.setSuccess(icount > 0);
            json.setMsg(sInfo);
        } catch (Exception e) {
            json.setSuccess(false);
            json.setMsg("结项失败！ " + e.getMessage());
        }
        return json;
    }

    @Override
    public boolean isExistAdProject(Twadproject twadproject) {
        Long count = new LambdaQueryChainWrapper<>(adprojectMapper)
                .eq(Twadproject::getSname, twadproject.getSname())
                .ne(twadproject.getId() != null, Twadproject::getId, twadproject.getId())
                .count();
        return count > 0;
    }

    /**
     * getAdProjectBudget
     * 方法功能:获取项目预算
     *
     * @param adprojectId
     * @return java.math.BigDecimal
     * @author yanz
     * @date 2024/3/11 13:14
     */
    @Override
    public BigDecimal getAdProjectBudget(Long adprojectId) {
        Twadproject twadproject = this.lambdaQuery().eq(Twadproject::getId, adprojectId).one();
        return twadproject.getNprojectbudget();
    }

    @Override
    public AdprojectVO getAdProjectByCode(String sprojectcode) {
        Twadproject adproject = new LambdaQueryChainWrapper<>(adprojectMapper)
                .eq(Twadproject::getProjectcode, sprojectcode)
                .one();
        AdprojectVO adprojectVO = new AdprojectVO();
        BeanUtils.copyProperties(adproject, adprojectVO);
        adprojectVO.setContracttypename(ContractType.getNameByKey(adproject.getIcontracttype()));
        adprojectVO.setSalecontracttypename(ContractType.getNameByKey(adproject.getIsalecontracttype()));
        String stamptypename = "";
        for (StampType stampType : StampType.values()) {
            if (adproject.getIstamptype().toString().contains(stampType.key.toString())) {
                stamptypename += stampType.name + " ";
            }
        }
        adprojectVO.setStamptypename(stamptypename);
        return adprojectVO;
    }

    @Override
    public Json<String> approveAdProject(String projectId, String flowId) {
        try {
            LoginUser user = WebUtil.getLoginUser();
            Twadproject twadproject = this.getById(projectId);
            // 申请审批
            if (StrUtil.isNotBlank(flowId)) {
                //获取流程条件
                List<Tbflowcondition> flowConditionList =
                        flowconditionServiceI.getFlowConditionList(FlowTypes.FLOW_PROJECT.getKey());
                if (CollUtil.isEmpty(flowConditionList)) {
                    return Json.fail("流程条件为空");
                }
                //获取流程条件
                Map<String, Object> flowParamMap = new HashMap<>();
                flowParamMap.put("businessId", projectId);
                flowParamMap.put("businessName", twadproject.getSname());
                for (Tbflowcondition tbflowcondition : flowConditionList) {
                    if ("deptid".equals(tbflowcondition.getSconditionkey().toLowerCase())) {
                        flowParamMap.put(tbflowcondition.getSconditionkey(), user.getDeptid().toString());
                        break;
                    }
                }
                return processInstanceService.startProcessInstanceByFlowTypes(flowId, flowParamMap,
                        FlowTypes.FLOW_PROJECT);
            }
        } catch (Exception e) {
            return Json.fail("审批申请失败" + e.getMessage());
        }
        return Json.fail("流程id为空");
    }

    @Override
    public Json updateAdprojectApprovalopinions(String applicationid, String sProjectId, boolean bUpdatepinion,
                                                String approveDesc, Integer iapproveStatus) {
        try {
            LoginUser user = WebUtil.getLoginUser();
            Twadproject projectInfo = this.getById(sProjectId);
            if (projectInfo == null) {
                return Json.fail("未找到项目信息！");
            }
            //如果状态是自动拒绝 就不更新状态
            if (projectInfo.getIapprovestatus() == ApproveStatus.APPROVE_REJECT.key) {
                return Json.fail("操作失败！该项目已被拒绝！");
            }

            innerInterceptor.recoredLog();
            projectInfo.setIapprovestatus(iapproveStatus);
            if (bUpdatepinion) {
                projectInfo.setSapprovalopinions(approveDesc);
            }
            if (adprojectMapper.updateById(projectInfo) == 0) {
                throw new OptimisticLockingFailureException("数据已改变，请刷新后再重新操作！");
            }
            return Json.success("操作成功！");
        } catch (Exception e) {
            return Json.fail("操作失败！ " + e.getMessage());
        }
    }
}
