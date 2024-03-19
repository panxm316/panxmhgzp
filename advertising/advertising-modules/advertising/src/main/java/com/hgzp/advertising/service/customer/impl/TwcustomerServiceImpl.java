package com.hgzp.advertising.service.customer.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.shaded.com.google.gson.JsonObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.emnus.approve.ApproveStatus;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.pagemodel.business.dto.TwtaskMessageDTO;
import com.hgzp.advertising.pagemodel.customer.vo.CustomerBelongVo;
import com.hgzp.advertising.pagemodel.customer.vo.CustomerReq;
import com.hgzp.advertising.pagemodel.customer.vo.CustomerVo;
import com.hgzp.advertising.pagemodel.customer.vo.CustomerfilesVo;
import com.hgzp.advertising.service.ad.TwadresourceapplicationServiceI;
import com.hgzp.advertising.service.ad.TworderServiceI;
import com.hgzp.advertising.service.ad.TworderitemServiceI;
import com.hgzp.advertising.service.business.CommissionServiceI;
import com.hgzp.advertising.service.business.TwpreinvoiceapplicationServiceI;
import com.hgzp.advertising.service.business.TwworkreportsServiceI;
import com.hgzp.advertising.service.customer.TwcustomerServiceI;
import com.hgzp.advertising.service.customer.TwcustomerbelongServiceI;
import com.hgzp.advertising.service.customer.TwcustomerfilesServiceI;
import com.hgzp.advertising.service.customer.TwcustomerserviceServiceI;
import com.hgzp.advertising.service.finance.InvoiceServiceI;
import com.hgzp.advertising.service.finance.TwcustomeraccountsServiceI;
import com.hgzp.advertising.service.finance.TwcustomerchargeServiceI;
import com.hgzp.advertising.service.system.TwmessageServiceI;
import com.hgzp.advertising.service.system.TwoperatelogServiceI;
import com.hgzp.common.flowable.dto.R;
import com.hgzp.core.emnus.MessageTypeEnum;
import com.hgzp.core.exception.DataExistException;
import com.hgzp.core.model.*;
import com.hgzp.core.page.DataCombo;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 客户表 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-10-25
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TwcustomerServiceImpl extends MyServiceImpl<TwcustomerMapper, Twcustomer> implements TwcustomerServiceI {

    @Autowired
    private TwcustomerMapper customerMapper;
    @Autowired
    private TwcustomerfilesServiceI customerFilesServiceI;
    @Autowired
    private TwcustomerbelongServiceI customerbelongServiceI;
    @Autowired
    private TworderServiceI orderServiceI;
    @Autowired
    private TwcustomerchargeServiceI customerchargeServiceI;
    @Autowired
    private InvoiceServiceI invoiceServiceI;
    @Autowired
    TwpreinvoiceapplicationServiceI preinvoiceapplicationServiceI;
    @Autowired
    private TwadresourceapplicationServiceI adresourceapplicationServiceI;
    @Autowired
    private CommissionServiceI commissionServiceI;
    @Autowired
    private TwcustomerserviceServiceI customerserviceServiceI;
    @Autowired
    private TwworkreportsServiceI workreportsServiceI;
    @Autowired
    TwcustomeraccountsServiceI customeraccountsService;
    @Autowired
    TwmessageServiceI messageService;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;

    @Override
    public IPage<CustomerVo> getCustomerPageList(Page<Twcustomer> page, CustomerReq query) {
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
        lqw.eq(ObjectUtil.isNotNull(query.getBuse()), Twcustomer::getBuse, query.getBuse())
                .eq(ObjectUtil.isNotNull(query.getAdindustryid()), Twcustomer::getAdindustryid, query.getAdindustryid())
                .eq(ObjectUtil.isNotNull(query.getCustomerstatusid()), Twcustomer::getCustomerstatusid,
                        query.getCustomerstatusid())
                .eq(ObjectUtil.isNotNull(query.getCustomercategoryid()), Twcustomer::getCustomercategoryid,
                        query.getCustomercategoryid())
                .eq(ObjectUtil.isNotNull(query.getCustomercreditid()), Twcustomer::getCustomercreditid,
                        query.getCustomercreditid())
                .eq(ObjectUtil.isNotNull(query.getBindividual()), Twcustomer::getBindividual, query.getBindividual())
                .eq(ObjectUtil.isNotNull(query.getEmployid()), Twcustomer::getEmployid, query.getEmployid())
                .eq(StrUtil.isNotBlank(query.getSbrevitycode()), Twcustomer::getSbrevitycode, query.getSbrevitycode())
                .eq(ObjectUtil.isNotNull(query.getItype()), Twcustomer::getItype, query.getItype())
                //.ne(Twcustomer::getIapprovestatus, 0) 只能看通过或否决
                .and(i -> i.eq(Twcustomer::getIapprovestatus, ApproveStatus.APPROVE_PASS.getKey())
                        .or()
                        .eq(Twcustomer::getIapprovestatus, ApproveStatus.APPROVE_REJECT.getKey()))
                .eq(ObjectUtil.isNotNull(query.getBdelete()), Twcustomer::getBdelete, query.getBdelete());

        IPage<Twcustomer> lsResult = customerMapper.selectPage(page, lqw);
        Page<CustomerVo> reslutPage = new Page<CustomerVo>();
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

            lsFlowData.add(flowVo);
        }
        reslutPage.setRecords(lsFlowData);
        reslutPage.setTotal(lsResult.getTotal());
        return reslutPage;
    }

    @Override
    public List<DataCombo> getCustomerCombo(String name) {
        List<Twcustomer> customerList = new LambdaQueryChainWrapper<>(customerMapper)
                .eq(Twcustomer::getBdelete, false)
                .like(Twcustomer::getSname, name)
                .orderByAsc(Twcustomer::getIsort)
                .list();

        List<DataCombo> comboList = customerList.stream()
                .map(item -> new DataCombo(item.getId().toString(), item.getSname()))
                .collect(Collectors.toList());
        return comboList;
    }

    @Override
    public CustomerVo getCustomerById(String id) {
        Twcustomer customer = getById(id);
        //父节点名称
        StringBuffer sname = new StringBuffer();
        getParentName(customer.getParentid(), sname);

        CustomerVo result = new CustomerVo();
        BeanUtils.copyProperties(customer, result);
        if (sname.length() > 0) {
            result.setParentName(sname.substring(1));
        }
        result.setCustomerfiles(customerFilesServiceI.getCustomerFiles(id));
        result.setCustomerbelong(customerbelongServiceI.getCustomerbelong(id));
        //业务员
        return result;
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
        LoginUser user = WebUtil.getLoginUser();
        Long customerId = IdUtil.getSnowflakeNextId();
        Twcustomer customer = new Twcustomer();
        BeanUtils.copyProperties(customerVo, customer);
        customer.setId(customerId);
        //bindividual!=0（非个人）判重名
        if (!customer.getBindividual() && isExistCustomer(customer)) {
            throw new DataExistException("已存在同名客户");
        }
        customer.setLastoperatorid(user.getUserid());
        customer.setLastoperator(user.getUsername());
        //通过此功能新增的客户iapprovestatus 默认是2
        if (customer.getIapprovestatus() != 2) {
            customer.setIapprovestatus(2);
        }
        customer.setSbrevitycode(PinyinUtil.converterToFirstSpell(customer.getSname()));
        customer.setIcode(null);
        customer.setDcreatetime(new Date());
        innerInterceptor.recoredLog();
        save(customer);
        //添加业务员
        if (customerVo.getCustomerbelong().size() > 0) {
            for (Twcustomerbelong belongItem : customerVo.getCustomerbelong()) {
                Twcustomerbelong customerBelong = new Twcustomerbelong();
                customerBelong.setCustomerid(customer.getId());
                customerBelong.setEmployid(belongItem.getEmployid());
                customerBelong.setEmployname(belongItem.getEmployname());
                customerBelong.setDeptid(belongItem.getDeptid());
                customerBelong.setDeptname(belongItem.getDeptname());
                customerBelong.setIapprovestatus(0);
                customerBelong.setBprimary(true);
                customerbelongServiceI.saveCustomerBelong(customerBelong);
            }
        }
        //添加附件
        if (customerVo.getCustomerfiles().size() > 0) {
            List<Twcustomerfiles> fileList = new ArrayList<>();
            for (CustomerfilesVo item : customerVo.getCustomerfiles()) {
                Twcustomerfiles newItem = new Twcustomerfiles();
                BeanUtils.copyProperties(item, newItem);
                newItem.setCustomerid(customerId);
                fileList.add(newItem);
            }
            customerFilesServiceI.saveCustomerFiles(fileList);
        }
    }

    @Override
    public void updateCustomer(CustomerVo customerVo) throws Exception {
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
        if (customer.getEmployid() == null) {
            customer.setEmployid((long) 0);
        }
        if (customer.getParentid() == null) {
            customer.setParentid((long) 0);
        }

        innerInterceptor.recoredLog();
        if (customerMapper.updateById(customer) == 0) {
            throw new OptimisticLockingFailureException("数据已改变，请刷新后再重新操作！");
        }

        //更新业务员
        if (customerVo.getCustomerbelong().size() > 0) {
            List<CustomerBelongVo> lsCustomerBelongs =
                    customerbelongServiceI.getCustomerbelong(customer.getId().toString());
            //主业务员
            CustomerBelongVo primaryBelong =
                    customerVo.getCustomerbelong().stream().filter(i -> i.getBprimary()).findFirst().get();

            if (ObjectUtil.isNull(primaryBelong)) {
                throw new OptimisticLockingFailureException("必须有一个主业务员！");
            }
            //过滤不存的业务员
            List<Long> lsEmpIds =
                    customerVo.getCustomerbelong().stream().map(Twcustomerbelong::getEmployid).collect(Collectors.toList());
            String notExistEmpIds =
                    lsCustomerBelongs.stream().filter(item -> !lsEmpIds.contains(item.getEmployid())).map(Twcustomerbelong::getId).map(Object::toString).collect(Collectors.joining(","));
            if (StrUtil.isNotBlank(notExistEmpIds)) {
                customerbelongServiceI.deleteCustomerBelongByIds(notExistEmpIds, false);
            }

            for (Twcustomerbelong belongItem : customerVo.getCustomerbelong()) {
                //判断业务员是否存在，存在就更新
                boolean bExist = false;
                if (lsCustomerBelongs.size() > 0) {
                    //业务员存在
                    bExist = lsCustomerBelongs.stream().anyMatch(i -> i.getEmployid().equals(belongItem.getEmployid()));
                }
                if (!bExist) {
                    //不存在就添加，已填的业务员修改为非主业务员
                    Twcustomerbelong customerBelong = new CustomerBelongVo();
                    customerBelong.setCustomerid(customer.getId());
                    customerBelong.setEmployid(belongItem.getEmployid());
                    customerBelong.setEmployname(belongItem.getEmployname());
                    customerBelong.setDeptid(belongItem.getDeptid());
                    customerBelong.setDeptname(belongItem.getDeptname());
                    customerBelong.setIapprovestatus(customer.getIapprovestatus());
                    customerBelong.setIapprovestatus(customer.getIapprovestatus());
                    customerBelong.setDcreatetime(new Date());
                    customerBelong.setBprimary(belongItem.getBprimary());
                    customerbelongServiceI.save(customerBelong);
                } else {
                    //如果存在且主业务员改变了就更新
                  //  boolean bprimaryChange = !primaryBelong.equals(primaryBelongDB);
                  //  if (bprimaryChange) {
                        customerbelongServiceI.updateCustomerBelong(belongItem);
                  //  }
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
    public Json deleteCustomerByIds(String ids) {
        try {
            LoginUser user = WebUtil.getLoginUser();
            String sInfo = "";
            int iCount = 0;
            String[] split = ids.split(",");
            List<Twcustomer> lsCustomer = this.lambdaQuery()
                    .in(Twcustomer::getId, split)
                    .list();

            innerInterceptor.recoredLog();
            for (Twcustomer t : lsCustomer) {
                //有业务 不能删除
                Json jsonRet = isExistCustomerbusiness(t);
                if (!jsonRet.isSuccess()) {
                    sInfo += jsonRet.getMsg() + "\r\n";
                    continue;
                }
                //
                t.setBdelete(true);
                t.setBuse(false);
                updateById(t);
                //通知业务员 如果删除的主业务员是自己的，不要发通知，否则发通知给业务员
                if (!user.getUserid().equals(t.getEmployid())) {
                    sendMsgToEmploy(user, t);
                }
                iCount++;
            }
            Json json = new Json();
            if (iCount > 0) {
                json.setSuccess(true);
            } else {
                json.setSuccess(false);
            }
            if (!"".equals(sInfo)) {
                json.setMsg(sInfo);
            }
            return json;
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }

    }

    //判断客户是否有业务
    private Json isExistCustomerbusiness(Twcustomer customer) {
        //有订单 不能删除
        Long count = new LambdaQueryChainWrapper<>(orderServiceI.getBaseMapper())
                .eq(Tworder::getBdelete, false)
                .eq(Tworder::getBuse, true)
                .eq(Tworder::getCustomerid, customer.getId())
                .count();
        if (count > 0) {
            return Json.fail(customer.getSname() + " 已有订单，不能删除  ");
        }
        //有资源 不能删除
        count = new LambdaQueryChainWrapper<>(adresourceapplicationServiceI.getBaseMapper())
                .eq(Twadresourceapplication::getCustomerid, customer.getId())
                .count();
        if (count > 0) {
            return Json.fail(customer.getSname() + " 存在客户资源，不能删除  ");
        }
        //有客户服务 不能删除
        count = new LambdaQueryChainWrapper<>(customerserviceServiceI.getBaseMapper())
                .eq(Twcustomerservice::getCustomerid, customer.getId())
                .count();
        if (count > 0) {
            return Json.fail(customer.getSname() + " 存在客户服务，不能删除  ");
        }
        //有工作报告 不能删除
        count = new LambdaQueryChainWrapper<>(workreportsServiceI.getBaseMapper())
                .eq(Twworkreports::getCustomerid, customer.getId())
                .count();
        if (count > 0) {
            return Json.fail(customer.getSname() + " 存在工作报告，不能删除  ");
        }
        return Json.success();
    }

    public boolean sendMsgToEmploy(LoginUser user, Twcustomer customer) {
        Twmessage message = new Twmessage();
        message.setId(IdUtil.getSnowflakeNextId());
        message.setStitle(customer.getSname() + "已被" + user.getUsername() + "删除！");
        message.setScontent(customer.getSname() + "已被" + user.getUsername() + "删除！");
        message.setSparam("");
        message.setStype(MessageTypeEnum.TODO_NOTICE.getType());
        message.setDcreatetime(new Date());
        message.setCreateempid(user.getUserid());
        message.setReceiveempid(customer.getEmployid());
        message.setBreaded(false);
        message.setBdeleted(false);
        message.setSflowid("");
        message.setSprocessinstanceid("");
        message.setDprocessinstancecreate(new Date());
        message.setSuniqueid("");
        messageService.save(message);
        return true;
    }

    @Override
    public boolean isExistCustomer(Twcustomer twcustomer) {
        Long count = new LambdaQueryChainWrapper<>(customerMapper)
                .eq(Twcustomer::getBdelete, false)
                .eq(Twcustomer::getSname, twcustomer.getSname())
                .ne(Twcustomer::getBindividual, true)
                .ne(twcustomer.getId() != null, Twcustomer::getId, twcustomer.getId())
                .count();
        return count > 0;
    }


    @Override
    public void combineCustomer(String sMainId, String subIds) throws Exception {
        //待合并客户信息
        if (StrUtil.isBlank(sMainId)) {
            throw new IllegalArgumentException("合并至客户Id为空");
        }
        if (StrUtil.isBlank(subIds)) {
            throw new IllegalArgumentException("待合并客户Id为空");
        }
        Twcustomer mainCustomer = this.getById(sMainId);
        //被合并客户信息
        String[] split = subIds.split(",");
        List<Twcustomer> lsSubCustomer = this.lambdaQuery()
                .in(Twcustomer::getId, split)
                .eq(Twcustomer::getBdelete, false)
                .eq(Twcustomer::getBuse, true)
                .list();
        //将待合并的客户信息删除
        for (Twcustomer item : lsSubCustomer) {
            item.setBdelete(true);
            innerInterceptor.recoredLog();
            updateById(item);
        }
        //合并客户资料附件
        customerFilesServiceI.combineCustomerFiles(subIds, mainCustomer.getId());
        //合并归属
        customerbelongServiceI.combineCustomerBelong(sMainId, subIds);
        //订单合并  修改订单客户Id和名称
        orderServiceI.updateOrderCustomer(subIds, mainCustomer.getId(), mainCustomer.getSname());
        // 客户收费合并  Twcustomercharge
        customerchargeServiceI.updateChargeCustomer(subIds, mainCustomer.getId(), mainCustomer.getSname());
        //发票合并  Twinvoice
        invoiceServiceI.updateInvoiceCustomer(subIds, mainCustomer.getId(), mainCustomer.getSname());
        // 预开发票合并 Twpreinvoiceapplication
        preinvoiceapplicationServiceI.updateInvoiceApplicationCustomer(subIds, mainCustomer.getId(),
                mainCustomer.getSname());
        //资源申请表合并 Twadresourceapplication
        adresourceapplicationServiceI.updateAdResourceCustomer(subIds, mainCustomer.getId(), mainCustomer.getSname());
        //佣金计提明细表合并 Twcommission
        commissionServiceI.updateCommissionCustomer(subIds, mainCustomer.getId(), mainCustomer.getSname());
        //客户服务表合并 Twcustomerservice
        customerserviceServiceI.updateCustomerServiceCustomer(subIds, mainCustomer.getId(), mainCustomer.getSname());
        //工作报告合并 Twworkreports
        workreportsServiceI.updateWorkreportCustomer(subIds, mainCustomer.getId(), mainCustomer.getSname());
        //客户账户 Twcustomeraccounts
        customeraccountsService.updateCustomeraccountsCustomer(subIds, mainCustomer.getId(), mainCustomer.getSname());
    }

}
