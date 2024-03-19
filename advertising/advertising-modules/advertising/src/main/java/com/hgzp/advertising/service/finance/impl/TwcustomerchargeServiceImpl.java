package com.hgzp.advertising.service.finance.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgzp.advertising.emnus.approve.AppStatus;
import com.hgzp.advertising.emnus.customer.CustomerType;
import com.hgzp.advertising.emnus.customer.EcustomerChargeType;
import com.hgzp.advertising.emnus.finance.InvoiceStatus;
import com.hgzp.advertising.emnus.finance.InvoiceType;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.pagemodel.ad.dto.OrderDebtDTO;
import com.hgzp.advertising.pagemodel.business.dto.PreInvoiceApplicationDTO;
import com.hgzp.advertising.pagemodel.finance.dto.CustomerChargeBankDTO;
import com.hgzp.advertising.pagemodel.finance.dto.CustomerChargeDTO;
import com.hgzp.advertising.pagemodel.finance.vo.CustomerChargeBankVO;
import com.hgzp.advertising.pagemodel.finance.vo.CustomerChargeVO;
import com.hgzp.advertising.service.ad.TworderitemServiceI;
import com.hgzp.advertising.service.business.TwpreinvoiceapplicationServiceI;
import com.hgzp.advertising.service.customer.TwcustomerServiceI;
import com.hgzp.advertising.service.finance.*;
import com.hgzp.advertising.service.system.ProduceServiceI;
import com.hgzp.core.exception.DataExistException;
import com.hgzp.core.exception.DataNotExistException;
import com.hgzp.core.model.*;
import com.hgzp.core.page.BaseQueryInfo;
import com.hgzp.core.page.Json;
import com.hgzp.mapper.finance.TbadprintitemMapper;
import com.hgzp.mapper.finance.TwcustomerchargeMapper;
import com.hgzp.utils.WebUtil;
import com.hgzp.utils.model.LoginUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;

import static com.hgzp.advertising.emnus.finance.PreinvoiceStyle.PreinvoiceStyle_5;

/**
 * <p>
 * 客户收费表 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-10-27
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TwcustomerchargeServiceImpl extends ServiceImpl<TwcustomerchargeMapper, Twcustomercharge> implements TwcustomerchargeServiceI {

    @Autowired
    TwpreinvoiceapplicationServiceI preinvoapplyServiceI;
    @Autowired
    OrderApportiondetailServiceI orderApportiondetailServiceI;

    private final String DIRECT_PAYMENT = "直接收款";
    private final String APPOINTMENT_PAYMENT = "预收款";


    @Autowired
    InvoiceServiceI twinvoiceService;
    @Autowired
    ProduceServiceI produceService;
    @Autowired
    TwcustomeraccountsServiceI twcustomeraccountsService;
    @Autowired
    HgDataChangeRecorderInnerInterceptor logInterceptor;
    @Autowired
    TbadprintitemMapper tbadprintitemMapper;
    @Autowired
    TwcustomerchargeMapper twcustomerchargeMapper;
    @Autowired
    TwbankaccountsServiceI twbankaccountsServiceI;
    @Autowired
    TwpreinvoiceapplicationServiceI twpreinvoiceapplicationServiceI;
    @Autowired
    TworderitemServiceI tworderitemServiceI;
    @Autowired
    OrderApportiondetailServiceI orderapportiondetailServiceI;
    @Autowired
    TwcustomerServiceI customerServiceI;


    /**
     * 预收款明细分页查询
     * 方法功能: 预收款明细分页查询
     *
     * @param query
     * @param page
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.advertising.pagemodel.finance.vo.CustomerChargeVO>
     * @author wangwk
     * @date 2023/10/30 17:18
     */
    @Override
    public IPage<CustomerChargeVO> getCustomerChargePageList(CustomerChargeVO query, IPage<Twcustomercharge> page) {
        if (query.getEndTime() != null) {
            Date endDate = DateUtil.offsetDay(query.getEndTime(), 1).toJdkDate();
            query.setEndTime(endDate);
        }

        IPage<Twcustomercharge> twcustomerchargeIPage = this.lambdaQuery()
                .eq(Twcustomercharge::getItype, EcustomerChargeType.EntryType_AdPay.key)
                .ge(query.getStartTime() != null, Twcustomercharge::getDpaydate, query.getStartTime())
                .lt(query.getEndTime() != null, Twcustomercharge::getDpaydate, query.getEndTime())
                .eq(query.getPaymethodid() != null, Twcustomercharge::getPaymethodid, query.getPaymethodid())
                .eq(query.getStype() != null, Twcustomercharge::getStype, query.getStype())
                .eq(query.getCustomerid() != null, Twcustomercharge::getCustomerid, query.getCustomerid())
                .eq(query.getEmployid() != null, Twcustomercharge::getEmployid, query.getEmployid())
                .ge(query.getNamountreceivedge() != null, Twcustomercharge::getNamountreceived,
                        query.getNamountreceivedge())
                .le(query.getNamountreceivedle() != null, Twcustomercharge::getNamountreceived,
                        query.getNamountreceivedle())
                .and(StrUtil.isNotBlank(query.getQueryKey()), o -> o.like(StrUtil.isNotBlank(query.getQueryKey()),
                                Twcustomercharge::getCustomername, query.getQueryKey())
                        .or()
                        .like(StrUtil.isNotBlank(query.getQueryKey()), Twcustomercharge::getEmployname,
                                query.getQueryKey())
                        .or()
                        .like(StrUtil.isNotBlank(query.getQueryKey()), Twcustomercharge::getInvoice,
                                query.getQueryKey())
                        .or()
                        .like(StrUtil.isNotBlank(query.getQueryKey()), Twcustomercharge::getSremark,
                                query.getQueryKey())
                ).page(page);

        List<Twcustomercharge> twcustomerchargeList = twcustomerchargeIPage.getRecords();
        //发票id
        List<Long> invoidceIdList =
                twcustomerchargeList.stream().map(Twcustomercharge::getInvoiceid).collect(Collectors.toList());
        Map<Long, List<Twinvoice>> twinvoiceListMap = new HashMap<>();
        if (invoidceIdList.size() != 0) {
            twinvoiceListMap = twinvoiceService.lambdaQuery()
                    .in(Twinvoice::getId, invoidceIdList)
                    .list()
                    .stream()
                    .collect(Collectors.groupingBy(Twinvoice::getId));
        }

        List<CustomerChargeVO> chargeVOList = twcustomerchargeList.stream()
                .map(CustomerChargeVO::new)
                .collect(Collectors.toList());

        for (CustomerChargeVO customerChargeVO : chargeVOList) {
            Twinvoice twinvoice = twinvoiceListMap.get(customerChargeVO.getInvoiceid()).get(0);

            customerChargeVO.setBusinessentityid(twinvoice.getBusinessentityid());
            customerChargeVO.setBusinessentityname(twinvoice.getBusinessentityname());
            customerChargeVO.setInvoicecode(twinvoice.getInvoicecode());
            customerChargeVO.setPrintitemid(twinvoice.getPrintitemid());
            customerChargeVO.setPrintitemname(twinvoice.getPrintitemname());
            customerChargeVO.setCusttypeName(CustomerType.getNameByKey(twinvoice.getIcusttype()));
        }
        IPage<CustomerChargeVO> voiPage = new Page<>();
        voiPage.setTotal(twcustomerchargeIPage.getTotal());
        voiPage.setRecords(chargeVOList);

        return voiPage;
    }


    /**
     * 获取收款明细详情
     * 方法功能: 获取收款明细详情
     *
     * @param id
     * @return com.hgzp.advertising.pagemodel.finance.vo.CustomerChargeVO
     * @author wangwk
     * @date 2023/11/1 10:55
     */
    @Override
    public CustomerChargeVO getCustomerChargeDetail(Long id) {
        Twcustomercharge twcustomercharge = this.lambdaQuery()
                .eq(Twcustomercharge::getId, id)
                .one();

        Twinvoice twinvoice = twinvoiceService.lambdaQuery()
                .eq(Twinvoice::getId, twcustomercharge.getInvoiceid())
                .one();

        CustomerChargeVO customerChargeVO = new CustomerChargeVO(twcustomercharge);
        customerChargeVO.setBusinessentityid(twinvoice.getBusinessentityid());
        customerChargeVO.setBusinessentityname(twinvoice.getBusinessentityname());
        customerChargeVO.setInvoicecode(twinvoice.getInvoicecode());
        customerChargeVO.setIcusttype(twinvoice.getIcusttype());
        customerChargeVO.setItypeinvoice(twinvoice.getItype());
        customerChargeVO.setInvoiceversion(twinvoice.getVersion());
        customerChargeVO.setPrintitemid(twinvoice.getPrintitemid());
        customerChargeVO.setPrintitemname(twinvoice.getPrintitemname());
        return customerChargeVO;
    }


    /**
     * 保存客户预收费
     * 方法功能:
     * 1、判断客户是否存在账户，如果不存在则创建账户
     * 2、保存客户预收费
     * 3、保存发票
     *
     * @param customerChargeDTO
     * @return void
     * @author wangwk
     * @date 2023/10/28 16:10
     */
    @Override
    public Long saveCustomerCharge(CustomerChargeDTO customerChargeDTO) throws Exception {
        LoginUser loginUser = WebUtil.getLoginUser();
        long customerchargeId = IdUtil.getSnowflakeNextId();
        //判断是否存在客户账户，没有则创建
        twcustomeraccountsService.updateCustomeraccountsNamountbalance(customerChargeDTO.getCustomerid(),
                customerChargeDTO.getNamountreceived(), customerchargeId);
        Tbadprintitem tbadprintitem = tbadprintitemMapper.selectById(customerChargeDTO.getPrintitemid());

        //保存发票
        String fpNo = produceService.getFpNo();
        Long invoiceId = IdUtil.getSnowflakeNextId();

        Twinvoice twinvoice = new Twinvoice();
        BeanUtils.copyProperties(customerChargeDTO, twinvoice);
        twinvoice.setId(invoiceId);
        twinvoice.setNamount(customerChargeDTO.getNamountreceived());
        twinvoice.setOperator(loginUser.getUsername());
        twinvoice.setOperatorid(loginUser.getUserid());
        twinvoice.setDcreatetime(new Date());
        twinvoice.setIstatus(InvoiceStatus.VALID.getKey());
        twinvoice.setInvoice(fpNo);
        twinvoice.setLastoperator(loginUser.getUsername());
        twinvoice.setLastoperatorid(loginUser.getUserid());
        twinvoice.setVersion(0L);
        twinvoice.setStaxcode(tbadprintitem.getStaxcode());
        twinvoice.setSprintname(customerChargeDTO.getCustomername());
        twinvoice.setIstype(PreinvoiceStyle_5.getKey());
        twinvoice.setItype(InvoiceType.Invoicetype_5.getKey());

        logInterceptor.recoredLog();
        twinvoiceService.save(twinvoice);

        //保存收费明细
        Twcustomercharge twcustomercharge = new Twcustomercharge();
        BeanUtils.copyProperties(customerChargeDTO, twcustomercharge);
        twcustomercharge.setId(customerchargeId);
        twcustomercharge.setNamounspent(BigDecimal.ZERO);
        twcustomercharge.setNamountbalance(customerChargeDTO.getNamountreceived());
        twcustomercharge.setDcreatetime(new Date());
        twcustomercharge.setLastoperator(loginUser.getUsername());
        twcustomercharge.setLastoperatorid(loginUser.getUserid());
        twcustomercharge.setStype(APPOINTMENT_PAYMENT);
        twcustomercharge.setInvoiceid(invoiceId);
        twcustomercharge.setInvoice(fpNo);
        twcustomercharge.setVersion(0L);
        // 20240104  suny 添加，数据表添加多个字段，需要赋值
        twcustomercharge.setCreateempid(loginUser.getUserid());
        twcustomercharge.setCreateempname(loginUser.getUsername());
        twcustomercharge.setDcreatetime(new Date());
        twcustomercharge.setIstatus(AppStatus.APPRSTATUS_EDIT.getKey());
        twcustomercharge.setItype(EcustomerChargeType.EntryType_AdPay.key);
        twcustomercharge.setLastoperatorid(loginUser.getUserid());
        twcustomercharge.setLastoperator(loginUser.getUsername());
        twcustomercharge.setDlastmodifydate(new Date());
        twcustomercharge.setDpaydate(new Date());
        twcustomercharge.setVersion(0L);
        logInterceptor.recoredLog();
        this.save(twcustomercharge);
        // 20240110 suny 返回发票id
        return invoiceId;
    }

    /**
     * 修改收费明细
     * 方法功能:
     * 1、修改账户总额
     * 2、更新发票表
     * 3、更新收费明细表
     *
     * @param customerChargeDTO
     * @return void
     * @author wangwk
     * @date 2023/11/1 13:40
     */
    @Override
    public void updateCustomerCharge(CustomerChargeDTO customerChargeDTO) throws Exception {
        LoginUser loginUser = WebUtil.getLoginUser();

        Twcustomercharge chargebyId = this.getById(customerChargeDTO.getId());
        //账户余额变动
        BigDecimal namountbalance = customerChargeDTO.getNamountreceived().subtract(chargebyId.getNamountreceived());
        twcustomeraccountsService.updateCustomeraccountsNamountbalance(customerChargeDTO.getCustomerid(),
                namountbalance, chargebyId.getId());

        Tbadprintitem tbadprintitem = tbadprintitemMapper.selectById(customerChargeDTO.getPrintitemid());

        //更新发票
        Twinvoice twinvoice = new Twinvoice();
        twinvoice.setPrintitemid(customerChargeDTO.getPrintitemid());
        twinvoice.setPrintitemname(customerChargeDTO.getPrintitemname());
        twinvoice.setStaxcode(tbadprintitem.getStaxcode());
        twinvoice.setBusinessentityid(customerChargeDTO.getBusinessentityid());
        twinvoice.setBusinessentityname(customerChargeDTO.getBusinessentityname());
        twinvoice.setIcusttype(customerChargeDTO.getIcusttype());
        twinvoice.setItype(customerChargeDTO.getItypeinvoice());
        twinvoice.setId(chargebyId.getInvoiceid());
        twinvoice.setNamount(customerChargeDTO.getNamountreceived());
        twinvoice.setLastoperator(loginUser.getUsername());
        twinvoice.setLastoperatorid(loginUser.getUserid());
        twinvoice.setVersion(customerChargeDTO.getInvoiceversion());
        twinvoice.setSprintname(customerChargeDTO.getCustomername());
        logInterceptor.recoredLog();
        boolean b = twinvoiceService.updateById(twinvoice);
        if (!b) {
            throw new OptimisticLockingFailureException("发票已被修改，请刷新页面后重试");
        }

        //更新收费明细
        Twcustomercharge twcustomercharge = new Twcustomercharge();
        BeanUtils.copyProperties(customerChargeDTO, twcustomercharge);
        twcustomercharge.setNamountbalance(customerChargeDTO.getNamountreceived());
        twcustomercharge.setLastoperatorid(loginUser.getUserid());
        twcustomercharge.setLastoperator(loginUser.getUsername());
        logInterceptor.recoredLog();
        boolean b1 = this.updateById(twcustomercharge);
        if (!b1) {
            throw new OptimisticLockingFailureException("收费明细已被修改，请刷新页面后重试");
        }
    }

    /**
     * 删除收费明细
     * 方法功能:
     * 1、账户余额变动
     * 2、删除发票
     * 3、删除收费明细
     *
     * @param id
     * @return void
     */
    @Override
    public void deleteCustomerCharge(Long id) throws Exception {
        LoginUser loginUser = WebUtil.getLoginUser();
        Twcustomercharge twcustomercharge = this.getById(id);
        //账户余额变动
        BigDecimal namountbalance = twcustomercharge.getNamountreceived().negate();
        twcustomeraccountsService.updateCustomeraccountsNamountbalance(twcustomercharge.getCustomerid(),
                namountbalance, twcustomercharge.getId());

        //删除收费明细
        logInterceptor.recoredLog();
        this.removeById(id);

        //删除发票
        logInterceptor.recoredLog();
        twinvoiceService.removeById(twcustomercharge.getInvoiceid());
    }

    //------------------------以下是银行流水分配使用的客户收款查询------------------------
    @Override
    public IPage<CustomerChargeBankDTO> getBankwCustomerChargePageList(Page<Twcustomercharge> page,
                                                                       CustomerChargeBankVO query) throws Exception {
        LoginUser user = WebUtil.getLoginUser();

        LambdaQueryWrapper<Twcustomercharge> lqw = Wrappers.lambdaQuery();
        // 银行流水类型
        lqw.eq(Twcustomercharge::getItype, EcustomerChargeType.EntryType_BankPay.key);
        lqw.eq(Twcustomercharge::getCreateempid, user.getUserid());
        lqw.ge(query.getStartTime() != null, Twcustomercharge::getDlastmodifydate, query.getStartTime());
        if (query.getEndTime() != null) {
            lqw.lt(Twcustomercharge::getDlastmodifydate, DateUtil.offsetDay(query.getEndTime(), 1));
        }

        lqw.eq(query.getBankaccountid() != null, Twcustomercharge::getBankaccountid, query.getBankaccountid());
        //订单
        if (StringUtils.hasText(query.getOrderid())) {
            List<String> idList = Arrays.asList(query.getOrderid().split(","));
            lqw.and(item -> item.eq(query.getInvoiceid() != null, Twcustomercharge::getInvoiceid, query.getInvoiceid())
                    .or()
                    .in(Twcustomercharge::getOrderid, idList));
        } else {
            lqw.eq(query.getInvoiceid() != null, Twcustomercharge::getInvoiceid, query.getInvoiceid());
        }
        lqw.orderByDesc(Twcustomercharge::getDlastmodifydate);
        IPage<Twcustomercharge> twcustomerchargePage = twcustomerchargeMapper.selectPage(page, lqw);
        Page<CustomerChargeBankDTO> bankAccountAllocateDTOPage = new Page<>();
        if (twcustomerchargePage.getTotal() == 0) {
            return bankAccountAllocateDTOPage;
        }
        List<Twcustomercharge> twcustomercharges = twcustomerchargePage.getRecords();
        List<CustomerChargeBankDTO> result = convertToCustomerChargeBankDtosForAllocationDetail(twcustomercharges);
        bankAccountAllocateDTOPage.setRecords(result);
        bankAccountAllocateDTOPage.setTotal(result.size());
        return bankAccountAllocateDTOPage;
    }

    /**
     * 银行流水分配明细POJO转DTO
     * 方法功能:有查询操作，涉及 银行流水单 Twbankaccounts 表、发票信息 Twpreinvoiceapplication 表（Twcustomercharge -> CustomerChargeBankDTO）
     *
     * @param twcustomercharges
     * @return java.util.List<com.hgzp.advertising.pagemodel.finance.dto.CustomerChargeBankDTO>
     * @author yanz
     * @date 2024/1/8 9:05
     */
    @Override
    public List<CustomerChargeBankDTO> convertToCustomerChargeBankDtosForAllocationDetail(List<Twcustomercharge> twcustomercharges) {
        List<CustomerChargeBankDTO> result = new ArrayList<>();
        twcustomercharges.forEach(item -> {
            CustomerChargeBankDTO customerChargeBankDTO = new CustomerChargeBankDTO();
            BeanUtils.copyProperties(item, customerChargeBankDTO);
            // 原有分配金额
            customerChargeBankDTO.setOldnamountreceived(item.getNamountreceived());
            Twbankaccounts twbankaccounts = twbankaccountsServiceI.getById(item.getBankaccountid());
            if (twbankaccounts != null) {
                customerChargeBankDTO.setSlendername(twbankaccounts.getSlendername());
                customerChargeBankDTO.setSborrowername(twbankaccounts.getSborrowername());
            }
            // 发票信息
            Twpreinvoiceapplication twpreinvoiceapplication =
                    twpreinvoiceapplicationServiceI.getById(item.getPreinvoiceapplicationid());
            if (twpreinvoiceapplication != null) {
                customerChargeBankDTO.setNamountapply(twpreinvoiceapplication.getNamountapply());
            }
            result.add(customerChargeBankDTO);
        });
        return result;
    }

    @Override
    public void saveBankCustomerCharge(CustomerChargeBankDTO entity) throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        validateAmount(entity);
        if (entity.getOrders() != null && entity.getOrders().size() > 0) {
            entity.getOrders().forEach(orderDebtDTO -> {
                BigDecimal amountreceived = orderDebtDTO.getAmountreceived() != null ?
                        orderDebtDTO.getAmountreceived() : BigDecimal.ZERO;
                if (amountreceived.compareTo(BigDecimal.ZERO) > 0) {
                    // 分配金额大于0，保存分配信息
                    Twcustomercharge twcustomercharge = new Twcustomercharge();
                    BeanUtils.copyProperties(entity, twcustomercharge);
                    twcustomercharge.setItype(EcustomerChargeType.EntryType_BankPay.key);
                    twcustomercharge.setStype(EcustomerChargeType.EntryType_BankPay.name);
                    twcustomercharge.setNamountreceived(orderDebtDTO.getAmountreceived());
                    twcustomercharge.setOrderid(orderDebtDTO.getId());
                    twcustomercharge.setScontractnum(orderDebtDTO.getScontractnum());
                    twcustomercharge.setCreateempid(user.getUserid());
                    twcustomercharge.setCreateempname(user.getUsername());
                    twcustomercharge.setDcreatetime(new Date());
                    twcustomercharge.setLastoperatorid(user.getUserid());
                    twcustomercharge.setLastoperator(user.getUsername());
                    twcustomercharge.setDlastmodifydate(new Date());
                    twcustomercharge.setNamounspent(BigDecimal.ZERO);
                    twcustomercharge.setNamountbalance(orderDebtDTO.getAmountreceived());
                    twcustomercharge.setDpaydate(new Date());
                    twcustomercharge.setVersion(0L);
                    logInterceptor.recoredLog();
                    this.save(twcustomercharge);
                }
            });
        } else {
            Twcustomercharge twcustomercharge = new Twcustomercharge();
            BeanUtils.copyProperties(entity, twcustomercharge);
            twcustomercharge.setItype(EcustomerChargeType.EntryType_BankPay.key);
            twcustomercharge.setStype(EcustomerChargeType.EntryType_BankPay.name);
            twcustomercharge.setCreateempid(user.getUserid());
            twcustomercharge.setCreateempname(user.getUsername());
            twcustomercharge.setDcreatetime(new Date());
            twcustomercharge.setLastoperatorid(user.getUserid());
            twcustomercharge.setLastoperator(user.getUsername());
            twcustomercharge.setDlastmodifydate(new Date());
            twcustomercharge.setNamounspent(BigDecimal.ZERO);
            twcustomercharge.setNamountbalance(entity.getNamountreceived());
            twcustomercharge.setDpaydate(new Date());
            twcustomercharge.setVersion(0L);
            logInterceptor.recoredLog();
            this.save(twcustomercharge);
        }
        Twbankaccounts twbankaccounts = twbankaccountsServiceI.getById(entity.getBankaccountid());
        twbankaccounts.setNamountallocate(twbankaccounts.getNamountallocate().add(entity.getNamountreceived()));
        // 银行流水表版本号
        twbankaccounts.setVersion(entity.getVersionBankAccount());
        twbankaccountsServiceI.updateById(twbankaccounts);
    }

    @Override
    public void updateBankCustomerCharge(CustomerChargeBankDTO entity) throws Exception {
        LoginUser user = WebUtil.getLoginUser();

        validateAmount(entity);
        Twcustomercharge twcustomercharge = this.getById(entity.getId());
        if (twcustomercharge == null) {
            throw new DataNotExistException("未找到对应的银行流水分配信息");
        }
        Twbankaccounts twbankaccounts = twbankaccountsServiceI.getById(twcustomercharge.getBankaccountid());
        if (twbankaccounts == null) {
            throw new DataNotExistException("未找到对应的银行流水信息");
        }
        // 银行流水已分配金额，减去原分配的金额，加上新分配的金额
        twbankaccounts.setNamountallocate(twbankaccounts.getNamountallocate().subtract(twcustomercharge.getNamountreceived()).add(entity.getNamountreceived()));
        // 银行流水表版本号
        twbankaccounts.setVersion(entity.getVersionBankAccount());
        logInterceptor.recoredLog();
        twbankaccountsServiceI.updateById(twbankaccounts);
        BeanUtils.copyProperties(entity, twcustomercharge);
        twcustomercharge.setLastoperatorid(user.getUserid());
        twcustomercharge.setLastoperator(user.getUsername());
        twcustomercharge.setDlastmodifydate(new Date());
        twcustomercharge.setNamountbalance(entity.getNamountreceived());
        logInterceptor.recoredLog();
        this.updateById(twcustomercharge);
    }

    /**
     * 方法功能: 判断分配的额度是否符合对应的范围
     *
     * @param entity
     * @return void
     * @author suny
     * @date 2023/12/8 11:11
     */
    private void validateAmount(CustomerChargeBankDTO entity) throws Exception {
        BigDecimal curNamountreceived = entity.getNamountreceived();
        if (curNamountreceived.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DataExistException("请设置发票分配金额");
        }
        // <editor-fold desc="判断当前银行流水分配情况，判断分配金额是否超过总额度">
        Twbankaccounts twbankaccounts = twbankaccountsServiceI.getById(entity.getBankaccountid());
        if (twbankaccounts == null) {
            throw new Exception("银行流水不存在");
        }
        // 导入金额
        BigDecimal Namount = twbankaccounts.getNamount();
        // 已分配金额, 减去原有分配金额
        BigDecimal Namountallocate = twbankaccounts.getNamountallocate().subtract(entity.getOldnamountreceived());
        // 未分配金额
        BigDecimal Namountunallocate = Namount.subtract(Namountallocate);
        if (Namount.compareTo(Namountallocate) < 0) {
            throw new Exception("分配金额不能大于导入金额");
        }
        // TODO 查询当前银行流水分配金额列表
        List<Twcustomercharge> twcustomercharges = this.lambdaQuery()
                .eq(Twcustomercharge::getBankaccountid, entity.getBankaccountid())
                .ne(entity.getId() != null, Twcustomercharge::getId, entity.getId()) // 排除当前修改的记录
                .list();
        // TOTO 当前银行流水分配出去的总金额
        BigDecimal bankaccountallocate =
                twcustomercharges.stream().map(Twcustomercharge::getNamountreceived).reduce(BigDecimal.ZERO,
                        BigDecimal::add);
        if (Namountallocate.compareTo(bankaccountallocate) < 0) {
            throw new DataFormatException("其他人已对当前银行流水做了分配，请重新分配额度");
        }
        //还未分配的额度与当前分配的额度比较，如果分配额度超过未分配额度则抛出错误
        if (Namountunallocate.compareTo(curNamountreceived) < 0) {
            throw new DataExistException("分配金额总数超过了银行流水总金额");
        }
        // </editor-fold>

        // <editor-fold desc="判断当前分配的发票分配金额状态，判断，当前分配后的金额是否超过申请额度">
        // TODO 查询指定的发票金额分配列表
        List<Twcustomercharge> twcustomercharges1 = this.lambdaQuery()
                .eq(Twcustomercharge::getInvoiceid, entity.getInvoiceid())
                .ne(Twcustomercharge::getIstatus, AppStatus.APPRSTATUS_EDIT.getKey())  // 待提交的
                .ne(Twcustomercharge::getIstatus, AppStatus.APPRSTATUS_REJECT.getKey())  // 已退回的
                .ne(entity.getId() != null, Twcustomercharge::getId, entity.getId()) // 排除当前修改的记录
                .list();
        // TODO 当前发票已分配金额总数
        BigDecimal invoiceAllocate =
                twcustomercharges1.stream().map(Twcustomercharge::getNamountreceived).reduce(BigDecimal.ZERO,
                        BigDecimal::add);
        Twpreinvoiceapplication twpreinvoiceapplication =
                twpreinvoiceapplicationServiceI.getById(entity.getPreinvoiceapplicationid());
        if (twpreinvoiceapplication == null) {
            throw new DataNotExistException("未找到对应的发票信息");
        }
        // 发票申请金额和分配金额以及持平
        if (invoiceAllocate.compareTo(twpreinvoiceapplication.getNamountapply()) >= 0) {
            throw new DataExistException("该发票以及对账结束");
        }
        // 发票还未对账的金额数
        BigDecimal uninvoiceAllocate = twpreinvoiceapplication.getNamountapply().subtract(invoiceAllocate);
        // 还未分配的额度与当前分配的额度比较，如果还未分配的额度小于分配的额度，则抛出错误
        if (uninvoiceAllocate.compareTo(curNamountreceived) < 0) {
            throw new DataExistException("分配额度超过发票总值");
        }
        //</editor-fold>

        //<editor-fold desc="每个合同订单分配的额度跟实际欠款额度比较">
        if (entity.getBassignorder() != null && entity.getBassignorder()) {
            entity.getOrders().forEach(orderDebtDTO -> {
                // TODO 查询指定的订单金额分配列表
                List<Twcustomercharge> twcustomercharges2 = this.lambdaQuery()
                        .eq(Twcustomercharge::getOrderid, orderDebtDTO.getId())
                        .ne(Twcustomercharge::getIstatus, AppStatus.APPRSTATUS_EDIT.getKey())  // 待提交的
                        .ne(Twcustomercharge::getIstatus, AppStatus.APPRSTATUS_REJECT.getKey())  // 已退回的
                        .list();
                // TODO 当前发票已分配金额总数
                BigDecimal orderAllocate =
                        twcustomercharges2.stream().map(Twcustomercharge::getNamountreceived).reduce(BigDecimal.ZERO,
                                BigDecimal::add);
                // 订单总额度小于等于已分配金额则已经结束分配
                if (orderDebtDTO.getAmountreceivable().compareTo(orderAllocate) <= 0) {
                    throw new DataExistException(orderDebtDTO.getSordernum() + " 订单已经对账结束");
                }
                // 订单合同还未支付的额度
                BigDecimal unorderAllocate = orderDebtDTO.getAmountreceivable().subtract(orderAllocate);
                // 订单合同还未支付的额度小于分配的额度时，抛出错误
                if (unorderAllocate.compareTo(orderDebtDTO.getAmountreceived()) < 0) {
                    throw new DataExistException(orderDebtDTO.getSordernum() + "分配额度超过订单总值");
                }
            });
        } else if (entity.getId() != null) {
            // 如果新分配金额大于原先分配金额，则需要判断是否超过了总额度
            if (entity.getNamountreceived().compareTo(entity.getOldnamountreceived()) > 0) {
                //<editor-fold desc="订单额度判断">
                // TODO 查询指定的订单金额分配列表
                List<Twcustomercharge> twcustomercharges2 = this.lambdaQuery()
                        .eq(Twcustomercharge::getOrderid, entity.getOrderid())
                        .ne(Twcustomercharge::getId, entity.getId())
                        .ne(Twcustomercharge::getIstatus, AppStatus.APPRSTATUS_EDIT.getKey())  // 待提交的
                        .ne(Twcustomercharge::getIstatus, AppStatus.APPRSTATUS_REJECT.getKey())  // 已退回的
                        .list();
                // TODO 当前订单除去本条记录已分配金额总数
                BigDecimal orderAllocate =
                        twcustomercharges2.stream().map(Twcustomercharge::getNamountreceived).reduce(BigDecimal.ZERO,
                                BigDecimal::add);
                // 当前订单欠款额度
                BigDecimal amountarrearage = BigDecimal.ZERO;
                if (entity.getOrderid() != null) {
                    OrderDebtDTO orders = null;
                    try {
                        // TODO 从订单表中获取订单欠款情况
                        orders = tworderitemServiceI.getOrderDebtInfo(entity.getOrderid().toString());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    if (orders != null) {
                        amountarrearage = orders.getAmountarrearage();
                    }
                }
//
                // 订单合同还未支付的额度
                BigDecimal unorderAllocate = amountarrearage.subtract(orderAllocate);
                // 订单合同还未支付的额度小于分配的额度时，抛出错误
                if (unorderAllocate.compareTo(entity.getNamountreceived()) < 0) {
                    throw new DataExistException(entity.getScontractnum() + "分配额度超过订单总值");
                }
                //</editor-fold>
            }
        }
        //</editor-fold>
    }

    @Override
    public void updateCustomerChargeStatus(String id, int istatus) throws Exception {
        Twcustomercharge twcustomercharge = this.getById(id);
        twcustomercharge.setIstatus(istatus);
        logInterceptor.recoredLog();
        this.updateById(twcustomercharge);
    }

    @Override
    public BigDecimal getCustomerChargeByInvoiceId(Long preinvoiceId, Long orderId) {
        // TODO 查询指定的发票金额分配列表
        List<Twcustomercharge> twcustomercharges = this.lambdaQuery()
                .eq(preinvoiceId != null, Twcustomercharge::getPreinvoiceapplicationid, preinvoiceId)
                .eq(orderId != null, Twcustomercharge::getOrderid, orderId)
                .ne(Twcustomercharge::getIstatus, AppStatus.APPRSTATUS_EDIT.getKey())  // 待提交的
                .ne(Twcustomercharge::getIstatus, AppStatus.APPRSTATUS_REJECT.getKey())  // 已退回的
                .list();
        // TODO 当前发票已分配金额总数
        BigDecimal invoiceAllocate =
                twcustomercharges.stream().map(Twcustomercharge::getNamountreceived).reduce(BigDecimal.ZERO,
                        BigDecimal::add);
        return invoiceAllocate;
    }

    @Override
    public void deleteBankCustomerCharge(String ids) throws Exception {
        List<String> idList = Arrays.asList(ids.split(","));
        for (String id : idList) {
            Twcustomercharge twcustomercharge = this.getById(id);
            if (twcustomercharge == null) {
                throw new DataNotExistException("未找到对应的银行流水分配信息");
            }
            Twbankaccounts twbankaccounts = twbankaccountsServiceI.getById(twcustomercharge.getBankaccountid());
            if (twbankaccounts == null) {
                throw new DataNotExistException("未找到对应的银行流水信息");
            }
            twbankaccounts.setNamountallocate(twbankaccounts.getNamountallocate().subtract(twcustomercharge.getNamountreceived()));
            logInterceptor.recoredLog();
            twbankaccountsServiceI.updateById(twbankaccounts);
            logInterceptor.recoredLog();
            this.removeById(id);
        }
    }

    /**
     * 获取核销记录列表 - 预开发票核销
     * 方法功能:获取核销记录列表，条件限制：预开申请id不为空、已提交&已确认的数据、只取“预开”类型的预开发票申请记录，“挂开”类型的不显示
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.advertising.pagemodel.finance.dto.CustomerChargeBankDTO>
     * @author yanz
     * @date 2023/12/22 12:41
     */
    @Override
    public IPage<CustomerChargeBankDTO> getInvoiceChargePageList(Page<Twcustomercharge> page, BaseQueryInfo query) throws Exception {
        // 条件限制：预开申请id不为空、已提交&已确认的数据、只取“预开”类型的预开发票申请记录，“挂开”类型的不显示
        LambdaQueryWrapper<Twcustomercharge> lqw = Wrappers.lambdaQuery();
        lqw.isNotNull(Twcustomercharge::getPreinvoiceapplicationid);
        // istatus AppStatus已提交或已确认的
        lqw.and(i -> i.eq(Twcustomercharge::getIstatus, AppStatus.APPRSTATUS_EDITING.getKey()).or().eq(Twcustomercharge::getIstatus, AppStatus.APPRSTATUS_PASS.getKey()));
        // 关键字查询 - 业务员、客户、合同号、发票号
        if (StrUtil.isNotBlank(query.getQueryKey())) {
            lqw.and(i -> i.like(Twcustomercharge::getEmployname, query.getQueryKey())
                    .or().like(Twcustomercharge::getCustomername, query.getQueryKey())
                    .or().like(Twcustomercharge::getScontractnum, query.getQueryKey())
                    .or().like(Twcustomercharge::getInvoice, query.getQueryKey()));
        }
        // 日期限制
        lqw.ge(ObjUtil.isNotNull(query.getStartTime()), Twcustomercharge::getDcreatetime, query.getStartTime());
        if (ObjUtil.isNotNull(query.getEndTime())) {
            lqw.lt(Twcustomercharge::getDlastmodifydate, DateUtil.offsetDay(query.getEndTime(), 1));
        }
        // 结果
        Page<Twcustomercharge> paged = this.page(page, lqw);
        Page<CustomerChargeBankDTO> dtoPage = new Page<>();
        if (paged.getTotal() == 0) {
            return dtoPage;
        }
        // 只取“预开”类型的预开发票申请记录，“挂开”类型的不显示
        List<Long> preinvoiceapplicationIds = paged.getRecords().stream()
                .map(Twcustomercharge::getPreinvoiceapplicationid)
                .collect(Collectors.toList());
        Map<Long, Twpreinvoiceapplication> preinvoiceapplicationMap =
                twpreinvoiceapplicationServiceI.lambdaQuery().in(Twpreinvoiceapplication::getId,
                                preinvoiceapplicationIds).list()
                        .stream()
                        .collect(Collectors.toMap(Twpreinvoiceapplication::getId, Function.identity()));
        List<CustomerChargeBankDTO> result = paged.getRecords().stream()
                .filter(item -> {
                    Twpreinvoiceapplication twpreinvoiceapplication =
                            preinvoiceapplicationMap.get(item.getPreinvoiceapplicationid());
                    return twpreinvoiceapplication != null && twpreinvoiceapplication.getIapplytype() == InvoiceType.Invoicetype_2.getKey();
                })
                .map(item -> {
                    CustomerChargeBankDTO dto = new CustomerChargeBankDTO();
                    BeanUtils.copyProperties(item, dto);
                    dto.setNamountapply(preinvoiceapplicationMap.get(item.getPreinvoiceapplicationid()).getNamountapply());
                    return dto;
                })
                .sorted(Comparator.comparing(CustomerChargeBankDTO::getDcreatetime).reversed())
                .collect(Collectors.toList());
        dtoPage.setRecords(result);
        dtoPage.setTotal(dtoPage.getRecords().size());
        return dtoPage;
    }

    /**
     * 保存核销结果/核销确认结果
     * 方法功能:保存核销结果/核销确认结果、意见
     *
     * @param customerChargeId
     * @param iStatus
     * @param comments
     * @return 成功为true，失败false
     * @author yanz
     * @date 2023/12/22 14:25
     */
    @Override
    public Boolean submitConfirmation(Long customerChargeId, Integer iStatus, String comments) {
        // 使用lambdaupdate时，id错误会使 logInterceptor.recoredLog() 报错，暂用此方法
        Twcustomercharge twcustomercharge = this.getById(customerChargeId);
        if (ObjUtil.isNull(twcustomercharge)) {
            return false;
        }
        twcustomercharge.setIstatus(iStatus);
        twcustomercharge.setSremark(comments);
        logInterceptor.recoredLog();
        if (!this.updateById(twcustomercharge)) {
            throw new DataNotExistException("核销确认结果保存失败");
        }
        return true;
    }

    /**
     * 查询收费表中 有余额的 客户预收费项目
     * 方法功能:根据客户名称或者发票号，查询收费表中 有余额的 客户预收费项目（限预收款）
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.advertising.pagemodel.finance.dto.CustomerChargeBankDTO>
     * @author yanz
     * @date 2023/12/25 10:29
     */
    @Override
    public IPage<CustomerChargeBankDTO> getCustomerChargeBankDTOList(Page<Twcustomercharge> page,
                                                                     CustomerChargeBankVO query) throws Exception {
        //    根据客户或者发票号（可以不用这些查询条件），查询收费表中 有余额的 客户预收费项目（不需要关联其他，返回的对象中包含以下字段）
        LambdaQueryWrapper<Twcustomercharge> lqw = Wrappers.lambdaQuery();
        // 客户id是前台选择出来的，用等于判断就行，发票号还是用querykey吧 - suny 12-25 12:42:08
        if (ObjUtil.isNotNull(query.getCustomerid())) {
            lqw.like(Twcustomercharge::getCustomerid, query.getCustomerid());
        }
        // 有余额的
        lqw.gt(Twcustomercharge::getNamountbalance, BigDecimal.ZERO);
        // 限预收款
        lqw.eq(Twcustomercharge::getItype, EcustomerChargeType.EntryType_AdPay.getKey());
        // 关键字查询 - 发票号
        if (StrUtil.isNotBlank(query.getQueryKey())) {
            lqw.like(Twcustomercharge::getInvoice, query.getQueryKey());
        }
        // 日期限制
        lqw.ge(ObjUtil.isNotNull(query.getStartTime()), Twcustomercharge::getDcreatetime, query.getStartTime());
        if (ObjUtil.isNotNull(query.getEndTime())) {
            lqw.lt(Twcustomercharge::getDlastmodifydate, DateUtil.offsetDay(query.getEndTime(), 1));
        }
        // 结果
        Page<Twcustomercharge> paged = this.page(page, lqw);
        Page<CustomerChargeBankDTO> dtoPage = new Page<>();
        if (paged.getTotal() == 0) {
            return dtoPage;
        }
        //（客户编号、客户名称、客户属性、经营主体、业务员、发票号、开票日期、收费日期、收款金额、剩余金额、开票名称）我在CustomerChargeBankDTO对象中添加以下字段，可以直接用此DTO:
        // businessentityname dinvoicecreatetime printitemname
        List<CustomerChargeBankDTO> result = convertToCustomerChargeBankDtosForToPrePaymentInfo(paged.getRecords());
        dtoPage.setRecords(result);
        dtoPage.setTotal(dtoPage.getRecords().size());
        return dtoPage;
    }

    /**
     * 客户收费POJO转DTO
     * 方法功能:有查询操作，涉及invoice表（Twcustomercharge -> CustomerChargeBankDTO）
     *
     * @param twcustomercharges
     * @return java.util.List<com.hgzp.advertising.pagemodel.finance.dto.CustomerChargeBankDTO>
     * @author yanz
     * @date 2024/1/6 9:10
     */
    public List<CustomerChargeBankDTO> convertToCustomerChargeBankDtosForToPrePaymentInfo(List<Twcustomercharge> twcustomercharges) {
        List<CustomerChargeBankDTO> results = twcustomercharges.stream()
                .map(item -> {
                    CustomerChargeBankDTO dto = new CustomerChargeBankDTO();
                    BeanUtils.copyProperties(item, dto);
                    Twinvoice twinvoice = twinvoiceService.getById(dto.getInvoiceid());
                    dto.setBusinessentityname(twinvoice.getBusinessentityname());
                    dto.setDinvoicecreatetime(twinvoice.getDcreatetime());
                    dto.setPrintitemname(twinvoice.getPrintitemname());
                    dto.setNamountapply(twinvoice.getNamount()); // 20240306 suny 发票金额
                    Twcustomer twcustomer = null;
                    if (ObjUtil.isNotNull(item.getCustomerid())) {
                        twcustomer = customerServiceI.getById(item.getCustomerid());
                    }
                    if (ObjUtil.isNotNull(twcustomer)) {
                        dto.setIcode(twcustomer.getIcode());
                        dto.setCustomeritype(twcustomer.getItype());
                    }
                    return dto;
                }).sorted(Comparator.comparing(CustomerChargeBankDTO::getDcreatetime).reversed()).collect(Collectors.toList());
        return results;
    }

    @Override
    public void updateChargeCustomer(String customerIds, Long newcustomerId, String newcustomername) throws Exception {
        try {
            List<String> idList = Arrays.asList(customerIds.split(","));
            List<Twcustomercharge> lscustomercharges =
                    this.lambdaQuery().in(Twcustomercharge::getCustomerid, idList).list();
            if (lscustomercharges.size() > 0) {
                lscustomercharges.forEach(item -> {
                    item.setCustomerid(newcustomerId);
                    item.setCustomername(newcustomername);
                });
                logInterceptor.recoredLog();
                this.updateBatchById(lscustomercharges);
            }
        } catch (Exception e0) {
            throw new RuntimeException("客户收费合并失败！" + e0.getMessage());
        }

    }

    /**
     * 核销预开发票
     * 方法功能:核销预开发票
     *
     * @param customerChargeId
     * @param orderitemIds     英文逗号分隔
     * @param dateString       传入分摊日期（字符串），yyyy-MM-dd 格式
     * @return void
     * @author yanz
     * @date 2023/12/23 10:26
     */
    @Override
    public void writeOffPreinvoiceapplication(Long customerChargeId, List<Long> orderitemIds, String dateString) throws Exception {
//        1、根据twcustomercharge表的id，找到对应的预开发票id和订单id（允许为空）
        Twcustomercharge twcustomercharge = this.getById(customerChargeId);
        if (ObjUtil.isNull(twcustomercharge)) {
            throw new DataNotExistException("未找到对应的收费明细");
        }
//        2、根据预开发票id和订单id，查询相关的所有订单详情列表
        Long orderId = twcustomercharge.getOrderid();
        PreInvoiceApplicationDTO preInvoiceApplicationDTO = preinvoapplyServiceI.getPreInvoiceApplyDtoById
                (
                        twcustomercharge.getPreinvoiceapplicationid()
                        , orderId
                );
        List<Tworderitem> orderItems = preInvoiceApplicationDTO.getOrderItems();
        if (ObjUtil.isEmpty(orderItems)) {
            throw new DataNotExistException("未找到对应的订单刊期");
        }
//        3、
//            3.1、如果分配表指定了订单，则需要判断分配的剩余额度是否超过了订单总欠款余额
//            3.2、如果没有指定订单，需要判断分配的剩余额度是否超过了当前预开发票关联的所有订单总欠款金额
        BigDecimal namountbalance = twcustomercharge.getNamountbalance();
//        根据上述3.1、3.2，判断额度是否超标，如果超标，返回false，提示退回业务员重新进行分配
        BigDecimal arrearageSum = orderItems.stream().map(Tworderitem::getAmountarrearage).reduce(BigDecimal.ZERO,
                BigDecimal::add);
        if (NumberUtil.isGreater(namountbalance, arrearageSum)) {
            throw new IllegalStateException("分配的额度超过了订单总欠款余额，退回业务员重新进行分配");
        }

//        4、根据提交的orderitems的ids，查询orderitem的list，按照时间正序排序
//        5、遍历orderitem的list表，根据欠款情况将分配额度更新到orderitem表，同时添加一条twordercapportiondetail记录，同时计算分配额度剩余情况
//        6、如果还有剩余则继续下一条记录，如果少于orderitem当前记录的欠款额度，将额度全部分配给当前item，更新收款和欠款，同时更新twcustomercharge表的分配和已用，同时状态改为已核销
//        7、如果orderitem全部处理完，仍有剩余，则将已用和剩余更新到twcustomercharge表，状态不变
        orderapportiondetailServiceI.saveOrderApportiondetail(customerChargeId, orderitemIds, dateString);
    }

    @Override
    public IPage<Twcustomercharge> getCustomerChargeByCustomerid(IPage<Twcustomercharge> page, CustomerChargeVO query) {
        LambdaQueryWrapper<Twcustomercharge> lqw = Wrappers.lambdaQuery();
        lqw.ge(query.getStartTime() != null, Twcustomercharge::getDlastmodifydate, query.getStartTime());
        if (query.getEndTime() != null) {
            lqw.lt(Twcustomercharge::getDlastmodifydate, DateUtil.offsetDay(query.getEndTime(), 1));
        }

        lqw.eq(query.getCustomerid() != null, Twcustomercharge::getCustomerid, query.getCustomerid());
        lqw.orderByDesc(Twcustomercharge::getDlastmodifydate);
        IPage<Twcustomercharge> twcustomerchargePage = twcustomerchargeMapper.selectPage(page, lqw);
        return twcustomerchargePage;
    }

    @Override
    public Twcustomercharge getMyCustomerChargeCount(CustomerChargeVO query) {
        if (query.getEndTime() != null) {
            query.setEndTime(DateUtil.offsetDay(query.getEndTime(), 1));
        } else {
            query.setEndTime(DateUtil.offsetDay(new Date(), 1));
        }
        Twcustomercharge twcustomercharge = twcustomerchargeMapper.getSumCustomerCharge(query.getStartTime(), query.getEndTime(), query.getCustomerid());
        return twcustomercharge;
    }

    @Override
    public List<CustomerChargeBankDTO> getCustomerChargeByBankid(Long bankid) {
        LambdaQueryWrapper<Twcustomercharge> lqw = Wrappers.lambdaQuery();
        lqw.eq(Twcustomercharge::getBankaccountid, bankid);
        List<Twcustomercharge> list = twcustomerchargeMapper.selectList(lqw);
        List<CustomerChargeBankDTO> result = convertToCustomerChargeBankDtosForToPrePaymentInfo(list);
        return result;
    }

    @Override
    public Json bankAcountWriteOff(CustomerChargeBankDTO entity) throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        validateAmount(entity);
        List<String> itemids = Arrays.asList(entity.getItemids().split(","));
        // 总入账金额
        BigDecimal entityCount = entity.getNamountreceived();
        // 总共分配额度
        BigDecimal itemCount = BigDecimal.ZERO;
        for (String itemid : itemids) {
            if (entityCount.compareTo(itemCount) <= 0) {
                break;
            }
            Tworderitem tworderitem = tworderitemServiceI.getById(itemid);
            BigDecimal amountreceived = tworderitem.getAmountreceived() != null ?
                    tworderitem.getAmountreceived() : BigDecimal.ZERO;
            // 当前订单明细欠款额度
            BigDecimal amountCount = tworderitem.getAmountarrearage();
            // 本次核销剩余未分配的额度
            BigDecimal leaveCount = entityCount.subtract(itemCount);
            //欠款额度>0
            if (amountCount.compareTo(BigDecimal.ZERO) > 0) {
                // 分配金额大于0，保存分配信息
                Twcustomercharge twcustomercharge = new Twcustomercharge();
                BeanUtils.copyProperties(entity, twcustomercharge);
                twcustomercharge.setItype(EcustomerChargeType.EntryType_BankPay.key);
                twcustomercharge.setStype(EcustomerChargeType.EntryType_BankPay.name);
                twcustomercharge.setNamountreceived(leaveCount.compareTo(amountCount) > 0 ? amountCount : leaveCount);
                twcustomercharge.setOrderid(tworderitem.getOrderid());
                twcustomercharge.setScontractnum(tworderitem.getScontractnum());
                twcustomercharge.setCreateempid(user.getUserid());
                twcustomercharge.setCreateempname(user.getUsername());
                twcustomercharge.setDcreatetime(new Date());
                twcustomercharge.setLastoperatorid(user.getUserid());
                twcustomercharge.setLastoperator(user.getUsername());
                twcustomercharge.setDlastmodifydate(new Date());
                twcustomercharge.setNamounspent(BigDecimal.ZERO);
                twcustomercharge.setNamountbalance(leaveCount.compareTo(amountCount) > 0 ? amountCount : leaveCount);
                twcustomercharge.setDpaydate(new Date());
                twcustomercharge.setVersion(0L);
                logInterceptor.recoredLog();
                this.save(twcustomercharge);
                List<Long> ids = new ArrayList<>();
                ids.add(tworderitem.getId());
                orderApportiondetailServiceI.saveOrderApportiondetail(twcustomercharge.getId(), ids, entity.getDateString());
            }
            if (leaveCount.compareTo(amountCount) > 0) {
                itemCount = itemCount.add(amountCount);
            } else {
                itemCount = itemCount.add(leaveCount);
            }
        }
        Twbankaccounts twbankaccounts = twbankaccountsServiceI.getById(entity.getBankaccountid());
        twbankaccounts.setNamountallocate(twbankaccounts.getNamountallocate().add(itemCount));
        // 银行流水表版本号
        twbankaccounts.setVersion(entity.getVersionBankAccount());
        twbankaccountsServiceI.updateById(twbankaccounts);
        return null;
    }
}
