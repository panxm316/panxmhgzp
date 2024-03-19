package com.hgzp.advertising.service.business.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.pagemodel.ad.dto.OrderDebtDTO;
import com.hgzp.advertising.pagemodel.business.dto.BankAccountAllocateDTO;
import com.hgzp.advertising.pagemodel.business.vo.BankAccountAllocateVO;
import com.hgzp.advertising.service.ad.TworderitemServiceI;
import com.hgzp.advertising.service.business.TwbankaccountallocateServiceI;
import com.hgzp.advertising.service.business.TwpreinvoiceapplicationServiceI;
import com.hgzp.advertising.service.finance.TwbankaccountsServiceI;
import com.hgzp.core.exception.DataExistException;
import com.hgzp.core.exception.DataNotExistException;
import com.hgzp.core.model.Twbankaccountallocate;
import com.hgzp.core.model.Twbankaccounts;
import com.hgzp.core.model.Twpreinvoiceapplication;
import com.hgzp.mapper.business.TwbankaccountallocateMapper;
import com.hgzp.utils.WebUtil;
import com.hgzp.utils.model.LoginUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.zip.DataFormatException;

/**
 * <p>
 * 银行流水分配表 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-12-04
 */
@Service
public class TwbankaccountallocateServiceImpl extends ServiceImpl<TwbankaccountallocateMapper, Twbankaccountallocate> implements TwbankaccountallocateServiceI {
    @Autowired
    HgDataChangeRecorderInnerInterceptor InnerInterceptor;
    @Autowired
    TwbankaccountsServiceI twbankaccountsServiceI;
    @Autowired
    TwpreinvoiceapplicationServiceI twpreinvoiceapplicationServiceI;
    @Autowired
    TworderitemServiceI tworderitemServiceI;
    @Autowired
    TwbankaccountallocateMapper twbankaccountallocateMapper;

    @Override
    public IPage<BankAccountAllocateDTO> getBankAccountAllocatePageList1(Page<Twbankaccountallocate> page, BankAccountAllocateVO query) throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        LambdaQueryWrapper<Twbankaccountallocate> lqw = Wrappers.lambdaQuery();
        lqw.eq(Twbankaccountallocate::getCreateempid, user.getUserid());
        lqw.ge(query.getStartTime() != null, Twbankaccountallocate::getDlastmodifydate, query.getStartTime());
        if (query.getEndTime() != null) {
            lqw.lt(Twbankaccountallocate::getDlastmodifydate, DateUtil.offsetDay(query.getEndTime(), 1));
        }

        lqw.eq(query.getBankaccountid() != null, Twbankaccountallocate::getBankaccountid, query.getBankaccountid());
        // 订单
        if (StringUtils.hasText(query.getOrderid())) {
            List<String> idList = Arrays.asList(query.getOrderid().split(","));
            lqw.and(item -> item.eq(query.getInvoiceid() != null, Twbankaccountallocate::getInvoiceid, query.getInvoiceid())
                    .or()
                    .in(Twbankaccountallocate::getOrderid, idList));
        } else {
            lqw.eq(query.getInvoiceid() != null, Twbankaccountallocate::getInvoiceid, query.getInvoiceid());
        }
        lqw.orderByDesc(Twbankaccountallocate::getDlastmodifydate);
        IPage<Twbankaccountallocate> twbankaccountallocateIPage = twbankaccountallocateMapper.selectPage(page, lqw);
        Page<BankAccountAllocateDTO> bankAccountAllocateDTOPage = new Page<>();
        if (twbankaccountallocateIPage.getTotal() == 0) {
            return bankAccountAllocateDTOPage;
        }
        List<BankAccountAllocateDTO> result = new ArrayList<>();
        twbankaccountallocateIPage.getRecords().forEach(item -> {
            BankAccountAllocateDTO bankAccountAllocateDTO = new BankAccountAllocateDTO();
            BeanUtils.copyProperties(item, bankAccountAllocateDTO);
            // 原有分配金额
            bankAccountAllocateDTO.setOldnamountallcate(item.getNamountallcate());
            Twbankaccounts twbankaccounts = twbankaccountsServiceI.getById(item.getBankaccountid());
            if (twbankaccounts != null) {
                bankAccountAllocateDTO.setSlendername(twbankaccounts.getSlendername());
                bankAccountAllocateDTO.setSborrowername(twbankaccounts.getSborrowername());
            }
            // 发票信息
            Twpreinvoiceapplication twpreinvoiceapplication = twpreinvoiceapplicationServiceI.getById(item.getInvoiceid());
            if (twpreinvoiceapplication != null) {
                bankAccountAllocateDTO.setNamountapply(twpreinvoiceapplication.getNamountapply());
            }
            result.add(bankAccountAllocateDTO);
        });
        bankAccountAllocateDTOPage.setRecords(result);
        bankAccountAllocateDTOPage.setTotal(result.size());
        return bankAccountAllocateDTOPage;
    }

    @Override
    public void saveBankAccountAllocate1(BankAccountAllocateDTO entity) throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        validateAmount(entity);
        if (entity.getOrders() != null && entity.getOrders().size() > 0) {
            entity.getOrders().forEach(orderDebtDTO -> {
                BigDecimal amountreceived = orderDebtDTO.getAmountreceived() != null ? orderDebtDTO.getAmountreceived() : BigDecimal.ZERO;
                if (amountreceived.compareTo(BigDecimal.ZERO) > 0) {
                    Twbankaccountallocate twbankaccountallocate = new Twbankaccountallocate();
                    BeanUtils.copyProperties(entity, twbankaccountallocate);
                    twbankaccountallocate.setNamountallcate(orderDebtDTO.getAmountreceived());
                    twbankaccountallocate.setOrderid(orderDebtDTO.getId());
                    twbankaccountallocate.setScontractnum(orderDebtDTO.getScontractnum());
                    twbankaccountallocate.setCreateempid(user.getUserid());
                    twbankaccountallocate.setCreateempname(user.getUsername());
                    twbankaccountallocate.setDcreatetime(new Date());
                    twbankaccountallocate.setLastoperatorid(user.getUserid());
                    twbankaccountallocate.setLastoperator(user.getUsername());
                    twbankaccountallocate.setDlastmodifydate(new Date());
                    twbankaccountallocate.setVersion(0L);
                    InnerInterceptor.recoredLog();
                    this.save(twbankaccountallocate);
                }
            });
        } else {
            Twbankaccountallocate twbankaccountallocate = new Twbankaccountallocate();
            BeanUtils.copyProperties(entity, twbankaccountallocate);
            twbankaccountallocate.setCreateempid(user.getUserid());
            twbankaccountallocate.setCreateempname(user.getUsername());
            twbankaccountallocate.setDcreatetime(new Date());
            twbankaccountallocate.setLastoperatorid(user.getUserid());
            twbankaccountallocate.setLastoperator(user.getUsername());
            twbankaccountallocate.setDlastmodifydate(new Date());
            twbankaccountallocate.setVersion(0L);
            InnerInterceptor.recoredLog();
            this.save(twbankaccountallocate);
        }
        Twbankaccounts twbankaccounts = twbankaccountsServiceI.getById(entity.getBankaccountid());
        twbankaccounts.setNamountallocate(twbankaccounts.getNamountallocate().add(entity.getNamountallcate()));
        twbankaccountsServiceI.updateById(twbankaccounts);
    }

    @Override
    public void updateBankAccountAllocate1(BankAccountAllocateDTO entity) throws Exception {
        LoginUser user = WebUtil.getLoginUser();

        validateAmount(entity);
        Twbankaccountallocate twbankaccountallocate = this.getById(entity.getId());
        if (twbankaccountallocate == null) {
            throw new DataNotExistException("未找到对应的银行流水分配信息");
        }
        Twbankaccounts twbankaccounts = twbankaccountsServiceI.getById(twbankaccountallocate.getBankaccountid());
        if (twbankaccounts == null) {
            throw new DataNotExistException("未找到对应的银行流水信息");
        }
        // 银行流水已分配金额，减去原分配的金额，加上新分配的金额
        twbankaccounts.setNamountallocate(twbankaccounts.getNamountallocate().subtract(twbankaccountallocate.getNamountallcate()).add(entity.getNamountallcate()));
        InnerInterceptor.recoredLog();
        twbankaccountsServiceI.updateById(twbankaccounts);
        BeanUtils.copyProperties(entity, twbankaccountallocate);
        twbankaccountallocate.setLastoperatorid(user.getUserid());
        twbankaccountallocate.setLastoperator(user.getUsername());
        twbankaccountallocate.setDlastmodifydate(new Date());
        InnerInterceptor.recoredLog();
        this.updateById(twbankaccountallocate);
    }

    @Override
    public void updateBankAccountAllocateStatus1(String id, int istatus) throws Exception {
        Twbankaccountallocate twbankaccountallocate = this.getById(id);
        twbankaccountallocate.setIstatus(istatus);
        InnerInterceptor.recoredLog();
        this.updateById(twbankaccountallocate);
    }

    /**
     * 方法功能: 判断分配的额度是否符合对应的范围
     *
     * @param entity
     * @return void
     * @author suny
     * @date 2023/12/8 11:11
     */
    private void validateAmount(BankAccountAllocateDTO entity) throws Exception {
        BigDecimal curNamountallocate = entity.getNamountallcate();
        if (curNamountallocate.compareTo(BigDecimal.ZERO) <= 0) {
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
        BigDecimal Namountallocate = twbankaccounts.getNamountallocate().subtract(entity.getOldnamountallcate());
        // 未分配金额
        BigDecimal Namountunallocate = Namount.subtract(Namountallocate);
        if (Namount.compareTo(Namountallocate) < 0) {
            throw new Exception("分配金额不能大于导入金额");
        }
        // TODO 查询当前银行流水分配金额列表
        List<Twbankaccountallocate> twbankaccountallocates = this.lambdaQuery()
                .eq(Twbankaccountallocate::getBankaccountid, entity.getBankaccountid())
                .ne(entity.getId() != null, Twbankaccountallocate::getId, entity.getId()) // 排除当前修改的记录
                .list();
        // TOTO 当前银行流水分配出去的总金额
        BigDecimal bankaccountallocate = twbankaccountallocates.stream().map(Twbankaccountallocate::getNamountallcate).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (Namountallocate.compareTo(bankaccountallocate) < 0) {
            throw new DataFormatException("其他人已对当前银行流水做了分配，请重新分配额度");
        }
        //还未分配的额度与当前分配的额度比较，如果分配额度超过未分配额度则抛出错误
        if (Namountunallocate.compareTo(curNamountallocate) < 0) {
            throw new DataExistException("分配金额总数超过了银行流水总金额");
        }
        // </editor-fold>

        // <editor-fold desc="判断当前分配的发票分配金额状态，判断，当前分配后的金额是否超过申请额度">
        // TODO 查询指定的发票金额分配列表
        List<Twbankaccountallocate> twbankaccountallocates1 = this.lambdaQuery()
                .eq(Twbankaccountallocate::getInvoiceid, entity.getInvoiceid())
                .ne(entity.getId() != null, Twbankaccountallocate::getId, entity.getId()) // 排除当前修改的记录
                .list();
        // TODO 当前发票已分配金额总数
        BigDecimal invoiceAllocate = twbankaccountallocates1.stream().map(Twbankaccountallocate::getNamountallcate).reduce(BigDecimal.ZERO, BigDecimal::add);
        Twpreinvoiceapplication twpreinvoiceapplication = twpreinvoiceapplicationServiceI.getById(entity.getInvoiceid());
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
        if (uninvoiceAllocate.compareTo(curNamountallocate) < 0) {
            throw new DataExistException("分配额度超过发票总值");
        }
        //</editor-fold>

        //<editor-fold desc="每个合同订单分配的额度跟实际欠款额度比较">
        if (entity.getBassignorder()) {
            entity.getOrders().forEach(orderDebtDTO -> {
                // TODO 查询指定的订单金额分配列表
                List<Twbankaccountallocate> twbankaccountallocates2 = this.lambdaQuery()
                        .eq(Twbankaccountallocate::getOrderid, orderDebtDTO.getId())
                        .list();
                // TODO 当前发票已分配金额总数
                BigDecimal orderAllocate = twbankaccountallocates2.stream().map(Twbankaccountallocate::getNamountallcate).reduce(BigDecimal.ZERO, BigDecimal::add);
                // 订单总额度小于等于已分配金额则已经结束分配
                if (orderDebtDTO.getAmountreceivable().compareTo(orderAllocate) <= 0) {
                    throw new DataExistException(orderDebtDTO.getSordernum() + " 订单已经对账结束");
                }
                // 订单合同还未支付的额度
                BigDecimal unorderAllocate = orderDebtDTO.getAmountreceivable().subtract(orderAllocate);
                // 订单合同还未支付的额度小于分配的额度时，抛出错误
                if (unorderAllocate.compareTo(orderDebtDTO.getAmountarrearage()) < 0) {
                    throw new DataExistException(orderDebtDTO.getSordernum() + "分配额度超过订单总值");
                }
            });
        } else if (entity.getId() != null) {
            // 如果新分配金额大于原先分配金额，则需要判断是否超过了总额度
            if (entity.getNamountallcate().compareTo(entity.getOldnamountallcate()) > 0) {
                //<editor-fold desc="订单额度判断">
                // TODO 查询指定的订单金额分配列表
                List<Twbankaccountallocate> twbankaccountallocates2 = this.lambdaQuery()
                        .eq(Twbankaccountallocate::getOrderid, entity.getOrderid())
                        .ne(Twbankaccountallocate::getId, entity.getId())
                        .list();
                // TODO 当前订单除去本条记录已分配金额总数
                BigDecimal orderAllocate = twbankaccountallocates2.stream().map(Twbankaccountallocate::getNamountallcate).reduce(BigDecimal.ZERO, BigDecimal::add);
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
                if (unorderAllocate.compareTo(entity.getNamountallcate()) < 0) {
                    throw new DataExistException(entity.getScontractnum() + "分配额度超过订单总值");
                }
                //</editor-fold>
            }
        }
        //</editor-fold>
    }

    @Override
    public BigDecimal getInvoiceAllocateByInvoiceId1(Long invoiceId, Long orderId) {
        // TODO 查询指定的发票金额分配列表
        List<Twbankaccountallocate> twbankaccountallocates1 = this.lambdaQuery()
                .eq(invoiceId != null, Twbankaccountallocate::getInvoiceid, invoiceId)
                .eq(orderId != null, Twbankaccountallocate::getOrderid, orderId)
                .list();
        // TODO 当前发票已分配金额总数
        BigDecimal invoiceAllocate = twbankaccountallocates1.stream().map(Twbankaccountallocate::getNamountallcate).reduce(BigDecimal.ZERO, BigDecimal::add);
        return invoiceAllocate;
    }

    /**
     * 删除选择的银行流水分配表，同时更新银行流水表的分配金额
     * 方法功能: 分配保存后要修改流水表中的namountallocate，如果修改或删除了分配表则要同步此字段数据
     *
     * @param ids
     * @return void
     * @author suny
     * @date 2023/12/12 16:37
     */
    @Override
    public void deleteBankAccountAllocate1(String ids) throws Exception {
        List<String> idList = Arrays.asList(ids.split(","));
        for (String id : idList) {
            Twbankaccountallocate twbankaccountallocate = this.getById(id);
            if (twbankaccountallocate == null) {
                throw new DataNotExistException("未找到对应的银行流水分配信息");
            }
            Twbankaccounts twbankaccounts = twbankaccountsServiceI.getById(twbankaccountallocate.getBankaccountid());
            if (twbankaccounts == null) {
                throw new DataNotExistException("未找到对应的银行流水信息");
            }
            twbankaccounts.setNamountallocate(twbankaccounts.getNamountallocate().subtract(twbankaccountallocate.getNamountallcate()));
            InnerInterceptor.recoredLog();
            twbankaccountsServiceI.updateById(twbankaccounts);
            InnerInterceptor.recoredLog();
            this.removeById(id);
        }
    }
}
