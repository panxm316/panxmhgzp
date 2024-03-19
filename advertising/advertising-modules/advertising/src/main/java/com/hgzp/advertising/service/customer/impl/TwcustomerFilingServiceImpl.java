package com.hgzp.advertising.service.customer.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.emnus.approve.ApproveStatus;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.pagemodel.customer.vo.*;
import com.hgzp.advertising.pagemodel.system.dto.EmpAuthorityDTO;
import com.hgzp.advertising.service.customer.TwcustomerFilingServiceI;
import com.hgzp.advertising.service.customer.TwcustomerbelongServiceI;
import com.hgzp.advertising.service.customer.TwcustomerfilesServiceI;
import com.hgzp.advertising.service.flow.IProcessInstanceService;
import com.hgzp.advertising.service.flow.TbflowconditionServiceI;
import com.hgzp.advertising.service.system.TbemployServiceI;
import com.hgzp.core.constant.SecurityConstants;
import com.hgzp.core.emnus.FlowTypes;
import com.hgzp.core.exception.DataExistException;
import com.hgzp.core.model.*;
import com.hgzp.core.page.Json;
import com.hgzp.mapper.customer.TwcustomerMapper;
import com.hgzp.service.common.impl.MyServiceImpl;
import com.hgzp.utils.PinyinUtil;
import com.hgzp.utils.WebUtil;
import com.hgzp.utils.model.LoginUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * CustomerFilingServiceImpl
 * 创建人：songly
 * 类描述：客户备案
 * 创建日期：2023/11/6 9:00
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TwcustomerFilingServiceImpl extends MyServiceImpl<TwcustomerMapper, Twcustomer> implements TwcustomerFilingServiceI {
    @Autowired
    private TwcustomerMapper customerMapper;
    @Autowired
    private TwcustomerfilesServiceI customerFilesServiceI;
    @Autowired
    private TwcustomerbelongServiceI customerbelongServiceI;
    @Autowired
    private TbemployServiceI employServiceI;
    @Autowired
    private TbflowconditionServiceI flowconditionServiceI;

    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;

    @Resource
    private IProcessInstanceService processInstanceService;

    @Override
    public IPage<CustomerVo> getMyCustomerPageList(Page<Twcustomer> page, CustomerReq query) throws Exception {

        LoginUser user = WebUtil.getLoginUser();
        HttpServletRequest request = WebUtil.getHttpServletRequest();
        List<Long> lsEmployIds = new ArrayList<>();
        // 如果是领导 查询负责的部门人员，如果结果为0，则是全部
        EmpAuthorityDTO empAuthorityDTO = (EmpAuthorityDTO) request.getAttribute(SecurityConstants.ROLE_PERMISSION);
        //如果LoginUserId不为空且账户是业务员就认为仅仅检索自己的
        if(ObjectUtil.isNotNull(query.getBmycustomer())&&query.getBmycustomer()&&user.getBsalesman()){
            lsEmployIds.add(Long.valueOf(query.getLoginUserId()));
        }else {
            if (user.getBlead()) {
                // 如果list中size等于零则是全部，不用控制;否则查权限范围内的部门
                if (empAuthorityDTO.getDeptIdList().size() > 0) {
                    List<Long> deptlist = empAuthorityDTO.getDeptIdList();
                    //获取人员
                    lsEmployIds = employServiceI.getEmpIdsByDeptIds(deptlist);
                    //添加自己
                    if (!lsEmployIds.contains(user.getUserid())) {
                        lsEmployIds.add(user.getUserid());
                    }
                }
            } else {
                lsEmployIds = empAuthorityDTO.getEmpIdList();
            }
        }

        LambdaQueryWrapper<Twcustomer> lqw = Wrappers.lambdaQuery();
        //多个关键字以空格间隔
        if (StrUtil.isNotBlank(query.getQueryKey())) {
            String[] keywords = query.getQueryKey().trim().split(" ");

            lqw.and(item -> {
                for (String skey : keywords) {
                    item.or(StrUtil.isNotBlank(skey), i -> i.like(Twcustomer::getSname, skey)
                            .or().like(Twcustomer::getSaddress, skey)
                            .or().like(Twcustomer::getScontacts, skey)
                            .or().like(Twcustomer::getScreditcode, skey)
                            .or().like(Twcustomer::getSbrevitycode, skey)
                    );
                }
            });
        }
        //业务员
        if (lsEmployIds.size() > 0) {
           String sEmpFilter = lsEmployIds.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(","));

            lqw.and(StrUtil.isNotBlank(sEmpFilter), i -> i.inSql(Twcustomer::getId,
                    ("select customerid from Twcustomerbelong where employid in (" + sEmpFilter +")")));
        }
        //负责人根据部门权限查看且不能查看待审和否决的数据
        lqw.and(user.getBlead(), i -> i.ne(Twcustomer::getIapprovestatus, ApproveStatus.APPROVE_EDIT.getKey())
                        .ne(Twcustomer::getIapprovestatus, ApproveStatus.APPROVE_REJECT.getKey()).or()
                .eq(Twcustomer::getEmployid,user.getUserid()).or().eq(Twcustomer::getLastoperatorid, user.getUserid()));

        //lqw.ne(user.getBlead(), Twcustomer::getIapprovestatus, ApproveStatus.APPROVE_EDIT.getKey());

        lqw.eq(Twcustomer::getBuse, 1).eq(ObjectUtil.isNotNull(query.getItype()), Twcustomer::getItype, query.getItype())
                .eq(ObjectUtil.isNotNull(query.getIapprovestatus()), Twcustomer::getIapprovestatus,
                        query.getIapprovestatus())
                .eq(StrUtil.isNotBlank(query.getSbrevitycode()), Twcustomer::getSbrevitycode, query.getSbrevitycode())
                .eq(Twcustomer::getBdelete, 0);

        IPage<Twcustomer> lsResult = customerMapper.selectPage(page, lqw);
        Page<CustomerVo> reslutPage = new Page<>();
        if (lsResult.getTotal() == 0) {
            return reslutPage;
        }
        List<CustomerVo> lsFlowData = new ArrayList<>();
        for (Twcustomer record : lsResult.getRecords()) {
            CustomerVo flowVo = new CustomerVo();
            BeanUtils.copyProperties(record, flowVo);
            if (record.getParentid() != null && record.getParentid() != 0) {
                StringBuffer sname = new StringBuffer();
                getParentName(record.getParentid(), sname);
                if (sname.length() > 0) {
                    flowVo.setParentName(sname.substring(1));
                }
            }
            //查附件
            List<CustomerfilesVo> lsFiles = customerFilesServiceI.getCustomerFiles(record.getId().toString());
            flowVo.setCustomerfiles(lsFiles);
            //归属
            List<CustomerBelongVo> lsbelong = customerbelongServiceI.getCustomerbelong(record.getId().toString());
            flowVo.setCustomerbelong(lsbelong);

            //历史流程
            List<ProcessInstanceRecord> lsProcessRecord =
                    processInstanceService.getHistoryProcessInstanceRecordByBussinessId("businessId",
                            record.getId().toString());
            if (lsProcessRecord.size() > 0) {
                List<ProcessInstanceVo> lsProcessInstance = new ArrayList<>();
                for (ProcessInstanceRecord item : lsProcessRecord) {
                    ProcessInstanceVo instanceVo = new ProcessInstanceVo();
                    instanceVo.setProcessInstanceId(item.getProcessInstanceId());
                    instanceVo.setFlowId(item.getFlowId());
                    instanceVo.setCreateTime(item.getCreateTime());
                    instanceVo.setResult(item.getResult());
                    lsProcessInstance.add(instanceVo);
                }
                flowVo.setCustomerProcessInstance(lsProcessInstance);
            }

            lsFlowData.add(flowVo);
        }

        reslutPage.setRecords(lsFlowData);
        reslutPage.setTotal(lsResult.getTotal());
        return reslutPage;
    }

    //获取父部门名称
    private void getParentName(Long sParentId, StringBuffer sDeptName) {
        LambdaQueryWrapper<Twcustomer> lqw = Wrappers.lambdaQuery();
        lqw.eq(ObjectUtil.isNotNull(sParentId), Twcustomer::getId, sParentId).last("LIMIT 1");
        Twcustomer sCustomer = customerMapper.selectOne(lqw);
        if (sCustomer != null) {
            sDeptName.append("●" + sCustomer.getSname());
            if (sCustomer.getParentid() != null) {
                getParentName(sCustomer.getParentid(), sDeptName);
            }
        }
    }

    @Override
    public void saveCustomer(CustomerVo customerVo) throws Exception {
        if (customerVo.getCustomerfiles().size() == 0) {
            throw new DataExistException("必须上传营业执照扫描件!");
        }
        LoginUser user = WebUtil.getLoginUser();
        Twcustomer customer = new Twcustomer();
        BeanUtils.copyProperties(customerVo, customer);
        //parentid null，bvip 0,sstatus 活跃, bindividual 0
        customer.setBvip(false);
        customer.setSstatus("活跃");
        customer.setBindividual(false);

        //bindividual!=0（非个人）判重名
        if (isExistCustomer(customer)) {
            throw new DataExistException("已存在同名客户");
        }
        customer.setIapprovestatus(ApproveStatus.APPROVE_EDIT.key);
        customer.setLastoperatorid(user.getUserid());
        customer.setLastoperator(user.getUsername());
        customer.setSbrevitycode(PinyinUtil.converterToFirstSpell(customer.getSname()));
        customer.setIcode(null);
        customer.setDcreatetime(new Date());
        innerInterceptor.recoredLog();
        if (!save(customer)) {
            throw new Exception("客户保存失败！");
        }
        //添加业务员
        Twcustomerbelong customerBelong = new Twcustomerbelong();
        if (ObjectUtil.isNotNull(customer.getEmployid())) {
            customerBelong.setCustomerid(customer.getId());
            customerBelong.setEmployid(customer.getEmployid());
            customerBelong.setEmployname(customer.getEmployname());
        } else {
            //未传业务员就默认当前账户为主业务员
            customerBelong.setCustomerid(customer.getId());
            customerBelong.setEmployid(user.getUserid());
            customerBelong.setEmployname(user.getUsername());
        }
        customerBelong.setDeptid(user.getDeptid());
        customerBelong.setDeptname(user.getDeptname());

        customerBelong.setIapprovestatus(customer.getIapprovestatus());
        customerBelong.setBprimary(true);
        Json jsonRet = customerbelongServiceI.saveCustomerBelong(customerBelong);
        if (!jsonRet.isSuccess()) {
            throw new DataExistException(jsonRet.getMsg());
        }
        //添加附件
        if (customerVo.getCustomerfiles().size() > 0) {
            List<Twcustomerfiles> fileList = new ArrayList<>();
            for (CustomerfilesVo item : customerVo.getCustomerfiles()) {
                Twcustomerfiles newItem = new Twcustomerfiles();
                BeanUtils.copyProperties(item, newItem);
                newItem.setCustomerid(customerVo.getId());
                fileList.add(newItem);
            }
            customerFilesServiceI.saveCustomerFiles(fileList);
        }
    }

    @Override
    public void updateCustomer(CustomerVo customerVo) throws Exception {
        if (customerVo.getCustomerfiles().size() == 0) {
            throw new DataExistException("必须上传营业执照扫描件!");
        }
        if (customerVo.getId().equals(customerVo.getParentid())) {
            throw new DataExistException("客户ID同父ID不能相同！");
        }
        Twcustomer customer = new Twcustomer();
        BeanUtils.copyProperties(customerVo, customer);
        LoginUser user = WebUtil.getLoginUser();
        //bindividual!=0（非个人）判重名
        if (!customer.getBindividual() && isExistCustomer(customer)) {
            throw new DataExistException("已存在同名客户！");
        }
        customer.setSbrevitycode(PinyinUtil.converterToFirstSpell(customer.getSname()));
        customer.setLastoperator(user.getUsername());
        customer.setLastoperatorid(user.getUserid());
        if (customer.getParentid() == null) {
            customer.setParentid((long) 0);
        }

        innerInterceptor.recoredLog();
        if (customerMapper.updateById(customer) == 0) {
            throw new OptimisticLockingFailureException("数据已改变，请刷新后再重新操作！");
        }
        //更新业务员
        if (ObjectUtil.isNotNull(customer.getEmployid())) {
            //判断业务员是否存在，存在就更新
            boolean bExist = false;
            List<CustomerBelongVo> lsCustomerBelongs =
                    customerbelongServiceI.getCustomerbelong(customer.getId().toString());
            if (lsCustomerBelongs.size() > 0) {
                //业务员存在
                bExist = lsCustomerBelongs.stream().anyMatch(i -> i.getEmployid().equals(customer.getEmployid()));
            }
            if (!bExist) {
                //不存在就添加，已填的业务员修改为非主业务员
                Twcustomerbelong customerBelong = new CustomerBelongVo();
                customerBelong.setCustomerid(customer.getId());
                customerBelong.setEmployid(customer.getEmployid());
                customerBelong.setIapprovestatus(customer.getIapprovestatus());
                customerBelong.setBprimary(true);
                customerbelongServiceI.save(customerBelong);
                for (CustomerBelongVo item : lsCustomerBelongs) {
                    item.setBprimary(false);
                    customerbelongServiceI.updateCustomerBelong(item);
                }
            } else {
                //如果存在且主业务员改变了就更新
                bExist =
                        lsCustomerBelongs.stream().anyMatch(i -> i.getEmployid().equals(customer.getEmployid()) && i.getBprimary());
                if (!bExist) {
                    for (CustomerBelongVo item : lsCustomerBelongs) {
                        if (item.getEmployid().equals(customer.getEmployid())) {
                            item.setBprimary(true);
                        } else {
                            item.setBprimary(false);
                        }
                        customerbelongServiceI.updateCustomerBelong(item);
                    }
                }
            }
        } else {
            customerbelongServiceI.deleteCustomerBelongByIds(customerVo.getId().toString(), true);
        }
        //更新附件
        if (customerVo.getCustomerfiles().size() > 0) {
            List<Twcustomerfiles> fileList = new ArrayList<>();
            for (CustomerfilesVo item : customerVo.getCustomerfiles()) {
                Twcustomerfiles newItem = new Twcustomerfiles();
                BeanUtils.copyProperties(item, newItem);
                fileList.add(newItem);
            }
            customerFilesServiceI.updateCustomerFiles(fileList);
        } else {
            //如果数据表中有附件，要删除
            customerFilesServiceI.deleteFilesByIds(customerVo.getId().toString(), true);
        }
    }

    @Override
    public Json<String> approveCustomer(String customerId, String flowId) {
        try {
            LoginUser user = WebUtil.getLoginUser();
            Twcustomer twcustomer = this.getById(customerId);
            String customerName = twcustomer.getSname();
            // 申请审批
            if (StrUtil.isNotBlank(flowId)) {
                //获取流程条件
                List<Tbflowcondition> flowConditionList =
                        flowconditionServiceI.getFlowConditionList(FlowTypes.FLOW_CUSTOMER.getKey());
                if (CollUtil.isEmpty(flowConditionList)) {
                    return Json.fail("流程条件为空");
                }
                //获取流程条件
                Map<String, Object> flowParamMap = new HashMap<>();
                flowParamMap.put("businessId", customerId);
                flowParamMap.put("businessName", customerName);
                for (Tbflowcondition tbflowcondition : flowConditionList) {
                    if ("deptid".equals(tbflowcondition.getSconditionkey().toLowerCase())) {
                        flowParamMap.put(tbflowcondition.getSconditionkey(), user.getDeptid().toString());
                        break;
                    }
                }
                return processInstanceService.startProcessInstanceByFlowTypes(flowId, flowParamMap,
                        FlowTypes.FLOW_CUSTOMER);
            }
        } catch (Exception e) {
            return Json.fail("审批申请失败" + e.getMessage());
        }
        return Json.fail("流程id为空");
    }

    @Override
    public Json updateCustomerApprovalopinions(String applicationid, String sCustomerId, boolean bUpdatepinion,
                                               String approveDesc,Integer iapproveStatus) {
        try {
            LoginUser user = WebUtil.getLoginUser();
            Twcustomer customerInfo = this.getById(sCustomerId);
            if (customerInfo == null) {
                return Json.fail("未找到客户信息！");
            }
            //审批后回调时如果状态是自动拒绝 就不更新状态 解决流程自动拒绝早于提交审核时更新状态
            if(bUpdatepinion&&customerInfo.getIapprovestatus()==ApproveStatus.APPROVE_REJECT.key&&iapproveStatus==ApproveStatus.APPROVE_REJECT.key){
                return Json.fail("操作失败！该客户已被拒绝！");
            }

            innerInterceptor.recoredLog();
            customerInfo.setIapprovestatus(iapproveStatus);
            if(bUpdatepinion) {
                customerInfo.setSapprovalopinions(approveDesc);
            }
            customerInfo.setLastoperator(user.getUsername());
            customerInfo.setLastoperatorid(user.getUserid());
            if (customerMapper.updateById(customerInfo) == 0) {
                throw new OptimisticLockingFailureException("数据已改变，请刷新后再重新操作！");
            }
            //更新归属状态
            List<CustomerBelongVo> lsBelongs = customerbelongServiceI.getCustomerbelong(sCustomerId);
            if (lsBelongs.size() > 0) {
                for (CustomerBelongVo belong : lsBelongs) {
                    belong.setApplicationid(applicationid);
                    belong.setIapprovestatus(iapproveStatus);
                    customerbelongServiceI.updateCustomerBelong(belong);
                }
            }
            return Json.success("操作成功！");
        } catch (Exception e) {
            return Json.fail("操作失败！ " + e.getMessage());
        }
    }

//    private Json updateCustomerApprovalopinions0000(String applicationid, String sCustomerId, boolean result,
//                                               String approveDesc) {
//        try {
//            LoginUser user = WebUtil.getLoginUser();
//            Twcustomer customerInfo = this.getById(sCustomerId);
//            if (customerInfo == null) {
//                return Json.fail("未找到客户信息！");
//            }
//            innerInterceptor.recoredLog();
//            customerInfo.setIapprovestatus(result ? ApproveStatus.APPROVE_PASS.key : ApproveStatus.APPROVE_REJECT.key);
//            customerInfo.setSapprovalopinions(approveDesc);
//            customerInfo.setLastoperator(user.getUsername());
//            customerInfo.setLastoperatorid(user.getUserid());
//            if (customerMapper.updateById(customerInfo) == 0) {
//                throw new OptimisticLockingFailureException("数据已改变，请刷新后再重新操作！");
//            }
//            //更新归属状态
//            List<CustomerBelongVo> lsBelongs = customerbelongServiceI.getCustomerbelong(sCustomerId);
//            if (lsBelongs.size() > 0) {
//                for (CustomerBelongVo belong : lsBelongs) {
//                    belong.setApplicationid(applicationid);
//                    belong.setIapprovestatus(result ? ApproveStatus.APPROVE_PASS.getKey() :
//                            ApproveStatus.APPROVE_REJECT.getKey());
//                    customerbelongServiceI.updateCustomerBelong(belong);
//                }
//            }
//            return Json.success("操作成功！");
//        } catch (Exception e) {
//            return Json.fail("操作失败！ " + e.getMessage());
//        }
//    }

//    @Override
//    public Json updateCustomerStatus00(String applicationid, String sCustomerId, Integer iApprovestatus) throws Exception {
//        //更新状态及申请Id
//        LoginUser user = WebUtil.getLoginUser();
//        Twcustomer newCustomer = this.getById(sCustomerId);
//        newCustomer.setLastoperator(user.getUsername());
//        newCustomer.setLastoperatorid(user.getUserid());
//        newCustomer.setIapprovestatus(iApprovestatus);
////        if (ObjectUtil.isNotNull(newCustomer.getIapprovestatus()) && newCustomer.getIapprovestatus() !=
////        ApproveStatus.APPROVE_EDITING.key) {
////            newCustomer.setIapprovestatus(ApproveStatus.APPROVE_EDITING.key);
////        }
//        innerInterceptor.recoredLog();
//        if (customerMapper.updateById(newCustomer) == 0) {
//            throw new OptimisticLockingFailureException("数据已改变，请刷新后再重新操作！");
//        }
//
//        //更新归属
//        List<CustomerBelongVo> lsCustomerBelongs = customerbelongServiceI.getCustomerbelong(sCustomerId);
//        if (lsCustomerBelongs.size() > 0) {
//            for (CustomerBelongVo item : lsCustomerBelongs) {
//                item.setApplicationid(applicationid);
//                item.setIapprovestatus(iApprovestatus);
////                if (ObjectUtil.isNotNull(item.getIapprovestatus()) && item.getIapprovestatus() != ApproveStatus
////                .APPROVE_EDITING.key) {
////                    item.setIapprovestatus(ApproveStatus.APPROVE_EDITING.key);
////                }
//                customerbelongServiceI.updateCustomerBelong(item);
//            }
//        }
//        return Json.success();
//    }

    //判断重名
    private boolean isExistCustomer(Twcustomer twcustomer) {
        Long count = new LambdaQueryChainWrapper<>(customerMapper)
                .eq(Twcustomer::getBdelete, false)
                .eq(Twcustomer::getSname, twcustomer.getSname())
                .ne(Twcustomer::getBindividual, true)
                .ne(twcustomer.getId() != null, Twcustomer::getId, twcustomer.getId())
                .count();
        return count > 0;
    }

}