package com.hgzp.advertising.service.finance.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgzp.advertising.emnus.approve.AppStatus;
import com.hgzp.advertising.emnus.approve.ApproveStatus;
import com.hgzp.advertising.emnus.customer.EcustomerChargeType;
import com.hgzp.advertising.emnus.finance.InvoiceStatus;
import com.hgzp.advertising.emnus.finance.InvoiceType;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.pagemodel.business.dto.PreInvoiceApplicationDTO;
import com.hgzp.advertising.pagemodel.business.vo.OrderAndItemVO;
import com.hgzp.advertising.pagemodel.business.vo.PreInvoiceApplicationVO;
import com.hgzp.advertising.pagemodel.finance.dto.*;
import com.hgzp.advertising.pagemodel.finance.vo.InvoiceVO;
import com.hgzp.advertising.service.ad.TworderServiceI;
import com.hgzp.advertising.service.business.TwpreinvoiceapplicationServiceI;
import com.hgzp.advertising.service.finance.*;
import com.hgzp.advertising.service.system.ProduceServiceI;
import com.hgzp.core.exception.DataNotExistException;
import com.hgzp.core.model.*;
import com.hgzp.core.page.Json;
import com.hgzp.mapper.finance.TwinvoiceMapper;
import com.hgzp.utils.WebUtil;
import com.hgzp.utils.model.LoginUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 发票表 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-10-27
 */
@Service
public class InvoiceServiceImpl extends ServiceImpl<TwinvoiceMapper, Twinvoice> implements InvoiceServiceI {

    @Autowired
    private ProduceServiceI produceServiceI;

    @Autowired
    private TwpreinvoiceapplicationServiceI preinvoapplyServiceI;

    @Autowired
    private TbadprintitemServiceI adprintitemServiceI;

    @Autowired
    private TbbusinessentityServiceI businessentityServiceI;

    @Autowired
    private HgDataChangeRecorderInnerInterceptor innerInterceptor;

    @Autowired
    private TworderServiceI orderServiceI;

    @Autowired
    private InvoiceFilesServiceI invoiceFilesServiceI;

    @Autowired
    private TwcustomerchargeServiceI customerchargeServiceI;

    @Autowired
    private OrderApportiondetailServiceI orderApportiondetailServiceI;

    /**
     * 保存预开申请表信息至发票表（临时票号）
     * 方法功能:据预开发票申请id，保存id对应的预开信息至发票表（临时票号），返回保存的发票ID
     *
     * @param preinvoiceapplicationid
     * @return java.lang.Long 发票ID
     * @author yanz
     * @date 2023/12/19 14:41
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long saveInvoiceByPreInvoApplyId(Long preinvoiceapplicationid) throws Exception {
        LoginUser loginUser = WebUtil.getLoginUser();
        String fpNo = produceServiceI.getFpNo();
        Twpreinvoiceapplication preinvoiceapplication = preinvoapplyServiceI.getById(preinvoiceapplicationid);
        // 更新临时发票号
        preinvoiceapplication.setInvoice(fpNo);

        // 新生成发票信息
        Twinvoice invoice = new Twinvoice();
        preinvoiceapplication.setInvoice(fpNo);

        BeanUtils.copyProperties(preinvoiceapplication, invoice);
        invoice.setId(null);

        invoice.setNamount(preinvoiceapplication.getNamountapply());
        invoice.setOperator(loginUser.getUsername());
        invoice.setOperatorid(loginUser.getUserid());
        invoice.setDcreatetime(new Date());
        invoice.setItype(preinvoiceapplication.getIapplytype());
        invoice.setIstype(preinvoiceapplication.getIinvoicetype());
        invoice.setIstatus(InvoiceStatus.VALID.getKey());
        invoice.setStaxcode(adprintitemServiceI.getById(preinvoiceapplication.getPrintitemid()).getStaxcode());
        // 付款方式、发票编码、收款人、复核人、备注、关联发票（红冲发票）、电子发票SHA1、电子发票原文件名为空
        invoice.setSremark(null);
        invoice.setLastoperator(loginUser.getUsername());
        invoice.setLastoperatorid(loginUser.getUserid());
        invoice.setVersion(0L);
        innerInterceptor.recoredLog();
        if (!this.save(invoice)) {
            throw new DataNotExistException("保存发票信息失败");
        }
        preinvoiceapplication.setInvoiceid(invoice.getId());
        innerInterceptor.recoredLog();
        if (!preinvoapplyServiceI.updateById(preinvoiceapplication)) {
            throw new DataNotExistException("更新预开申请信息失败");
        }
        return invoice.getId();
    }

    /**
     * 获取单个发票信息
     * 方法功能:据发票id，取单个发票信息
     *
     * @param id
     * @return com.hgzp.advertising.pagemodel.finance.dto.InvoiceDTO
     * @author yanz
     * @date 2023/12/19 13:33
     */
    @Override
    public InvoiceDTO getInvoiceById(Long id) {
        Twinvoice invoice = this.getById(id);
        InvoiceDTO invoiceDTO = new InvoiceDTO();
        BeanUtils.copyProperties(invoice, invoiceDTO);
        Tbbusinessentity tbbusinessentity = businessentityServiceI.getById(invoice.getBusinessentityid());
        if(tbbusinessentity != null) {
            invoiceDTO.setTaxitems(tbbusinessentity.getTaxitems());
            invoiceDTO.setTaxrate(tbbusinessentity.getTaxrate());
        }
        invoiceDTO.setFileList(invoiceFilesServiceI.getFilesByInvoiceId(id));
        // 取合同表
        Twpreinvoiceapplication twpreinvoiceapplication = preinvoapplyServiceI.lambdaQuery().eq(Twpreinvoiceapplication::getInvoiceid, id).one();
        PreInvoiceApplicationVO vo = preinvoapplyServiceI.getPreinvoiceapplicationVOByIdOrObj(null, twpreinvoiceapplication);

        invoiceDTO.setContractVos(vo.getContractVos());
        return invoiceDTO;
    }

    /**
     * 保存三方获取的发票号、发票编码
     * 方法功能:据传入的发票id（invoiceId），保存三方获取的发票号（invoice）、发票编码（invoiceCode）至发票表、预开发票申请表
     *
     * @param invoiceId
     * @param invoice     发票号
     * @param invoiceCode 发票编码
     * @author yanz
     * @date 2023/12/21 9:06
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveInvoiceCode(Long invoiceId, String invoice, String invoiceCode) throws Exception {
        LoginUser loginUser = WebUtil.getLoginUser();

        // 至发票表
        if (StrUtil.isBlank(invoice) || StrUtil.isBlank(invoiceCode)) {
            throw new IllegalStateException("发票号或发票编码为空");
        }
        Twinvoice twinvoice = this.getById(invoiceId);
        twinvoice.setInvoice(invoice);
        twinvoice.setInvoicecode(invoiceCode);
        twinvoice.setOperator(loginUser.getUsername());
        twinvoice.setOperatorid(loginUser.getUserid());
        innerInterceptor.recoredLog();
        if (!this.updateById(twinvoice)) {
            throw new DataNotExistException("更新至发票信息失败");
        }
        // 预开发票申请表
        Twpreinvoiceapplication twpreinvoiceapplication = preinvoapplyServiceI.lambdaQuery()
                .eq(Twpreinvoiceapplication::getInvoiceid, invoiceId).one();
        twpreinvoiceapplication.setInvoice(invoice);
        twpreinvoiceapplication.setInvoicecode(invoiceCode);
        innerInterceptor.recoredLog();
        if (!preinvoapplyServiceI.updateById(twpreinvoiceapplication)) {
            throw new DataNotExistException("更新预开申请信息失败");
        }
    }

    /**
     * 查询发票列表
     * 方法功能:查询发票列表，参数：时间段、客户id、关键字（发票号、客户名称、业务员、开票项目）
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.advertising.pagemodel.finance.dto.InvoiceDTO>
     * @author yanz
     * @date 2023/12/28 10:33
     */
    @Override
    public IPage<InvoiceDTO> getInvoicePageList(Page<Twinvoice> page, InvoiceVO query) {
        LambdaQueryWrapper<Twinvoice> lqw = Wrappers.lambdaQuery();
        // 日期限制
        lqw.ge(ObjUtil.isNotNull(query.getStartTime()), Twinvoice::getDcreatetime, query.getStartTime());
        if (ObjUtil.isNotNull(query.getEndTime())) {
            lqw.lt(Twinvoice::getDcreatetime, DateUtil.offsetDay(query.getEndTime(), 1));
        }
        // 客户id、关键字（发票号、客户名称、业务员、开票项目）
        lqw.or().eq(Twinvoice::getCustomerid, query.getCustomerid());
        if (StrUtil.isNotBlank(query.getQueryKey())) {
            lqw.and(i -> i.like(Twinvoice::getInvoice, query.getQueryKey())
                    .or().like(Twinvoice::getCustomername, query.getQueryKey())
                    .or().like(Twinvoice::getEmployname, query.getQueryKey())
                    .or().like(Twinvoice::getPrintitemname, query.getQueryKey()));
        }
        Page<Twinvoice> paged = this.page(page, lqw);
        Page<InvoiceDTO> dtoPage = new Page<>();
        if (paged.getTotal() == 0) {
            return dtoPage;
        }
        List<Twinvoice> twinvoices = paged.getRecords();
        List<Long> businessEntityIds = twinvoices.stream()
                .map(Twinvoice::getBusinessentityid)
                .collect(Collectors.toList());

        Map<Long, Tbbusinessentity> businessEntityMap = businessentityServiceI.listByIds(businessEntityIds)
                .stream()
                .collect(Collectors.toMap(Tbbusinessentity::getId, Function.identity()));

        List<InvoiceDTO> invoiceDTOs = new ArrayList<>();
        for (Twinvoice invoice : twinvoices) {
            InvoiceDTO invoiceDTO = new InvoiceDTO();
            BeanUtils.copyProperties(invoice, invoiceDTO);
            Tbbusinessentity tbbusinessentity = businessEntityMap.get(invoice.getBusinessentityid());
            if (tbbusinessentity != null) {
                invoiceDTO.setTaxitems(tbbusinessentity.getTaxitems());
                invoiceDTO.setTaxrate(tbbusinessentity.getTaxrate());
            }
            invoiceDTO.setFileList(invoiceFilesServiceI.getFilesByInvoiceId(invoice.getId()));
            invoiceDTOs.add(invoiceDTO);
        }

        dtoPage.setRecords(invoiceDTOs);
        dtoPage.setTotal(invoiceDTOs.size());
        return dtoPage;
    }

    /**
     * 更新发票数据（发票打印）
     * 方法功能:以InvoiceDTO内容，更新发票数据（发票打印）
     *
     * @param invoiceDTO
     * @return void
     * @author yanz
     * @date 2024/1/2 16:53
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateInvoiceByInvoiceDTO(InvoiceDTO invoiceDTO) throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        // 付款方名称 付款方识别号 付款方地址电话 付款方开户行账号 开票项目 税收编码 备注 开票人 收款人 复核人
        Twinvoice twinvoice = InvoiceDTO.parseToEntity(invoiceDTO);
        innerInterceptor.recoredLog();
        // 20240110 suny 如果是预收款则更新预收款表中状态为已通过
        if (invoiceDTO.getItype() == InvoiceType.Invoicetype_5.getKey()) {
            Twcustomercharge twcustomercharge = customerchargeServiceI.lambdaQuery()
                    .eq(Twcustomercharge::getInvoiceid, invoiceDTO.getId()).one();
            if (ObjUtil.isNull(twcustomercharge)) {
                throw new DataNotExistException("未找到预收款信息");
            }
            twcustomercharge.setIstatus(AppStatus.APPRSTATUS_PASS.getKey());
            innerInterceptor.recoredLog();
            if (!customerchargeServiceI.updateById(twcustomercharge)) {
                throw new DataNotExistException("更新预收款信息失败");
            }
        }
        boolean updated = this.updateById(twinvoice);
        if (!updated) {
            throw new DataNotExistException("更新发票信息失败");
        }

        if (ObjUtil.isNotNull(invoiceDTO.getFileList()) && invoiceDTO.getFileList().size() > 0) {
            List<InvoiceFilesDTO> newFiles = invoiceDTO.getFileList().stream()
                    .filter(newFile -> ObjUtil.isNull(newFile.getId()))
                    .peek(newFile -> {
                        // 20231229 suny
                        newFile.setInvoiceid(invoiceDTO.getId());
                        newFile.setDcreatetime(new Date());
                        newFile.setCreateempid(user.getUserid());
                        newFile.setCreateempname(user.getUsername());
                        newFile.setInvoice(invoiceDTO.getInvoice());
                        newFile.setInvoicecode(invoiceDTO.getInvoicecode());
                    }).collect(Collectors.toList());
            invoiceFilesServiceI.saveInvoiceFiles(newFiles, user);
        }
    }

    @Override
    public void saveInfoiceFiles(List<InvoiceFilesDTO> dtoList) throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        if (CollUtil.isEmpty(dtoList)) {
            throw new DataNotExistException("文件列表为空");
        }
        List<String> errnames = new ArrayList<>();
        List<String> exitnames = new ArrayList<>();
        for (InvoiceFilesDTO file : dtoList) {
            // 判断文件是否已经存在
            List<Twinvoicefiles> list = invoiceFilesServiceI.lambdaQuery().eq(Twinvoicefiles::getSfileid,
                    file.getSfileid()).list();
            if (CollUtil.isNotEmpty(list)) {
                exitnames.add(file.getSoriginalfile());
                continue;
            }
            String[] filenames = file.getSoriginalfile().split("_");
            if (filenames.length < 2) {
                errnames.add(file.getSoriginalfile());
                continue;
            }
            String invoicename = "";
            String invoice = "";
            String invoicecode = "";
            if (".pdf".equals(file.getSfileformat()) || ".ofd".equals(file.getSfileformat()) || ".PDF".equals(file.getSfileformat()) || ".OFD".equals(file.getSfileformat())) {
                invoicename = filenames[0];
            } else {
                invoicename = filenames[1];
            }
            invoicecode = invoicename.substring(0, 12);
            invoice = invoicename.substring(12, 20);
            Twinvoice twinvoice = this.lambdaQuery().eq(Twinvoice::getInvoice, invoice).eq(Twinvoice::getInvoicecode,
                    invoicecode).one();
            if (ObjUtil.isNull(twinvoice)) {
                errnames.add(file.getSoriginalfile());
                continue;
            }
            // 判断该类型文件是否已经上传
            Twinvoicefiles twinvoicefiles = invoiceFilesServiceI.lambdaQuery().eq(Twinvoicefiles::getInvoiceid,
                    twinvoice.getId()).one();
            if (ObjUtil.isNotNull(twinvoicefiles)) {
                exitnames.add(file.getSoriginalfile());
                continue;
            }
            file.setInvoiceid(twinvoice.getId());
            file.setDcreatetime(new Date());
            file.setCreateempid(user.getUserid());
            file.setCreateempname(user.getUsername());
            file.setInvoice(twinvoice.getInvoice());
            file.setInvoicecode(twinvoice.getInvoicecode());
            List<InvoiceFilesDTO> newFiles = new ArrayList<>();
            newFiles.add(file);
            invoiceFilesServiceI.saveInvoiceFiles(newFiles, user);
        }

        if (errnames.size() > 0 || exitnames.size() > 0) {
            String msg = "";
            if (errnames.size() > 0) {
                msg += "文件名称错误(未找到对应发票号): \r\n" + String.join("\r\n", errnames);
            }
            if (exitnames.size() > 0) {
                msg += "\r\n 文件已存在： \r\n" + String.join("\r\n", exitnames);
            }
            throw new DataNotExistException(msg);
        }
    }

    /**
     * 查询一段时间内的发票
     * 方法功能:认刊合同查询用。条件包括：时间范围、发票号码（queryKey）、客户名称（可填写可选择customername）
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.advertising.pagemodel.business.dto.OrderAndItemDTO>
     * @author yanz
     * @date 2024/1/5 9:03
     */
    @Override
    public IPage<InvoiceDTO> getInvoicesInPeriod(Page<Twinvoice> page, OrderAndItemVO query) {

        LambdaQueryWrapper<Twinvoice> lqw = Wrappers.lambdaQuery();
        // 日期限制
        lqw.ge(ObjUtil.isNotNull(query.getStartTime()), Twinvoice::getDcreatetime, query.getStartTime());
        if (ObjUtil.isNotNull(query.getEndTime())) {
            lqw.lt(Twinvoice::getDcreatetime, DateUtil.offsetDay(query.getEndTime(), 1));
        }
        // 限制条件： “有效”
        lqw.eq(Twinvoice::getIstatus, InvoiceStatus.VALID.getKey());
        // 关键字查询 - 发票号
        if (StrUtil.isNotBlank(query.getQueryKey())) {
            lqw.like(Twinvoice::getInvoice, query.getQueryKey());
        }

        Page<Twinvoice> twinvoicePage = this.page(page, lqw);
        Page<InvoiceDTO> twinvoiceDTOPage = new Page<>();

        if (CollUtil.isEmpty(twinvoicePage.getRecords())) {
            return twinvoiceDTOPage;
        }
        List<InvoiceDTO> result = twinvoicePage.getRecords().stream().map(i -> {
            // 返回列表包括：
            //发票号、发票代码、开票日期、发票类型、开票项目、发票金额、客户名称、客户类型、打印名称、业务员、发票样式、经营主体、发票状态、备注
            InvoiceDTO dto = InvoiceDTO.parseToDTO(i);
            LambdaQueryWrapper<Twcustomercharge> lqwTmp = Wrappers.lambdaQuery();
            lqwTmp.eq(Twcustomercharge::getInvoiceid, i.getId());
            List<Twcustomercharge> twcustomercharges = customerchargeServiceI.list(lqwTmp);
            BigDecimal received = null;
            if (CollUtil.isNotEmpty(twcustomercharges)) {
                // 已用金额（关联twcustomercharge，查询所有该发票号数据计算）

                // 剩余金额（预收款：关联twcustomercharge，查询该发票剩余金额）
                BigDecimal spent =
                        twcustomercharges.stream().map(Twcustomercharge::getNamounspent).reduce(BigDecimal.ZERO,
                                BigDecimal::add);

                received =
                        twcustomercharges.stream().map(Twcustomercharge::getNamountreceived).reduce(BigDecimal.ZERO,
                                BigDecimal::add);

                BigDecimal balance =
                        twcustomercharges.stream().map(Twcustomercharge::getNamountbalance).reduce(BigDecimal.ZERO,
                                BigDecimal::add);
                dto.setNamounspent(spent);
                dto.setNamountbalance(balance);
                dto.setNamountreceived(received);
            } else {
                dto.setNamounspent(BigDecimal.ZERO);
                dto.setNamountbalance(BigDecimal.ZERO);
                dto.setNamountreceived(BigDecimal.ZERO);
            }
            List<Twpreinvoiceapplication> twpreinvoiceapplications = preinvoapplyServiceI.lambdaQuery()
                    .eq(Twpreinvoiceapplication::getInvoiceid, i.getId())
                    .eq(Twpreinvoiceapplication::getIapprovestatus, ApproveStatus.APPROVE_PASS.getKey())
                    .list();
            if (CollUtil.isNotEmpty(twpreinvoiceapplications)) {
                // 欠款金额（预开发票：预开发票申请金额-twcustomercharge中所有入账金额，【如果找不到该发票数据则入账金额为0】）、
                BigDecimal arrearage =
                        twpreinvoiceapplications.stream().map(Twpreinvoiceapplication::getNamountapply).reduce(BigDecimal.ZERO, BigDecimal::add);
                arrearage = arrearage.subtract(received == null ? BigDecimal.ZERO : received);
                dto.setAmountarrearage(arrearage);
            } else {
                dto.setAmountarrearage(BigDecimal.ZERO);
            }
            return dto;
        }).collect(Collectors.toList());
        twinvoiceDTOPage.setRecords(result);
        twinvoiceDTOPage.setTotal(result.size());

        return twinvoiceDTOPage;
    }

    /**
     * 据发票号获取发票相关详情 - （数据台账/发票台账）
     * 方法功能:根据发票id，查看预开信息（预开类型）、预收款信息（预收款类型）、核销数据（已做核销）、分配明细（已有分配）
     *
     * @param invoiceid
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.advertising.pagemodel.finance.dto.InvoiceDetailsDTO>
     * @author yanz
     * @date 2024/1/6 16:15
     */
    @Override
    public InvoiceDetailsDTO getInvoiceDetailsByInvoice(String invoiceid) {
        // 查询的时候要添加判断，预开发票查询预开申请表，预收费的查询客户预收款情况，另外两个不需要判断，应该都有 - suny 01-06 16:13:14
        // 预开申请表 PreInvoiceApplicationDTO
        List<Twpreinvoiceapplication> twpreinvoiceapplications = preinvoapplyServiceI.lambdaQuery()
                .eq(Twpreinvoiceapplication::getInvoiceid, invoiceid)
                .eq(Twpreinvoiceapplication::getIapprovestatus, ApproveStatus.APPROVE_PASS.getKey())
                .list();
        List<PreInvoiceApplicationDTO> preInvoiceApplicationDTOs =
                convertPreInvoiceApplicationToDTOs(twpreinvoiceapplications);

        // 预收款信息/预收款类型（客户收费表） twcustomercharge
        List<Twcustomercharge> twcustomercharges = customerchargeServiceI.lambdaQuery()
                .eq(Twcustomercharge::getInvoiceid, invoiceid)
                .and(i -> i.eq(Twcustomercharge::getIstatus, AppStatus.APPRSTATUS_EDITING.getKey())
                        .or().eq(Twcustomercharge::getIstatus, AppStatus.APPRSTATUS_PASS.getKey())
                        .or().eq(Twcustomercharge::getIstatus, AppStatus.APPRSTATUS_VERIFIED.getKey()))
                .list();
        List<CustomerChargeBankDTO> prePaymentInfos =
                customerchargeServiceI.convertToCustomerChargeBankDtosForToPrePaymentInfo(
                        twcustomercharges.stream()
                                .filter(twcustomercharge -> twcustomercharge.getItype() == EcustomerChargeType.EntryType_AdPay.getKey()).collect(Collectors.toList())
                );
        // 核销数据 OrderApportiondetailDTO getInvoiceChargePageList？
        List<Tworderapportiondetail> tworderapportiondetails = orderApportiondetailServiceI.lambdaQuery()
                .eq(Tworderapportiondetail::getInvoiceid, invoiceid)
                .list();
        List<OrderApportiondetailDTO> apportiondetailDTOS =
                orderApportiondetailServiceI.convertApportiondetailToDTOs(tworderapportiondetails);
        // 银行流水分配明细（已有分配）
        List<CustomerChargeBankDTO> allocationDetails =
                customerchargeServiceI.convertToCustomerChargeBankDtosForAllocationDetail(
                        twcustomercharges.stream()
                                .filter(twcustomercharge -> twcustomercharge.getItype() == EcustomerChargeType.EntryType_BankPay.getKey()).collect(Collectors.toList())
                );
        InvoiceDetailsDTO invoiceDetailsDTO = new InvoiceDetailsDTO(preInvoiceApplicationDTOs, prePaymentInfos,
                apportiondetailDTOS, allocationDetails);

        return invoiceDetailsDTO;
    }

    /**
     * 预开发票申请POJO转DTO
     * 方法功能:有查询操作，涉及 发票表 Invoice
     *
     * @param twpreinvoiceapplications
     * @return java.util.List<com.hgzp.advertising.pagemodel.business.dto.PreInvoiceApplicationDTO>
     * @author yanz
     * @date 2024/1/6 16:20
     */
    @Override
    public List<PreInvoiceApplicationDTO> convertPreInvoiceApplicationToDTOs(List<Twpreinvoiceapplication> twpreinvoiceapplications) {
        List<PreInvoiceApplicationDTO> preInvoiceApplicationDTOList = twpreinvoiceapplications.stream()
                .map(a -> {
                    PreInvoiceApplicationDTO preInvoiceApplicationDTO = new PreInvoiceApplicationDTO();
                    BeanUtils.copyProperties(a, preInvoiceApplicationDTO);
                    if (ObjUtil.isNotNull(a.getInvoiceid())) {
                        preInvoiceApplicationDTO.setRealInvoiceDate(this.getInvoiceById(a.getInvoiceid()).getDcreatetime());
                    }
                    return preInvoiceApplicationDTO;
                }).collect(Collectors.toList());
        return preInvoiceApplicationDTOList;
    }

    @Override
    public void updateInvoiceCustomer(String customerIds, Long newcustomerId, String newcustomername) throws Exception {
        try {
            String[] splitids = customerIds.split(",");
            List<Twinvoice> lsInvoice = this.lambdaQuery()
                    .in(Twinvoice::getCustomerid, splitids)
                    .list();
            if (lsInvoice.size() > 0) {
                lsInvoice.forEach(order -> {
                    order.setCustomerid(newcustomerId);
                    order.setCustomername(newcustomername);
                });

                innerInterceptor.recoredLog();
                this.updateBatchById(lsInvoice);
            }
        } catch (Exception e0) {
            throw new RuntimeException("发票合并失败！" + e0.getMessage());
        }
    }

    @Override
    public IPage<InvoiceDTO> getInvoiceByCustomerid(Page<Twinvoice> page, InvoiceVO entity) throws Exception {
        if (entity.getCustomerid() == null) {
            throw new DataNotExistException("客户id不能为空");
        }
        if (entity.getEndTime() != null) {
            entity.setEndTime(DateUtil.offsetDay(entity.getEndTime(), 1));
        }
        LambdaQueryWrapper<Twinvoice> lqw = Wrappers.lambdaQuery();
        lqw.eq(Twinvoice::getCustomerid, entity.getCustomerid());
        lqw.ge(ObjUtil.isNotNull(entity.getStartTime()), Twinvoice::getDcreatetime, entity.getStartTime());
        lqw.lt(ObjUtil.isNotNull(entity.getEndTime()), Twinvoice::getDcreatetime, entity.getEndTime());
        Page<Twinvoice> paged = this.page(page, lqw);
        Page<InvoiceDTO> dtoPage = new Page<>();
        if (paged.getTotal() == 0) {
            return dtoPage;
        }
        List<InvoiceDTO> result = paged.getRecords().stream().map(i -> {
            InvoiceDTO dto = InvoiceDTO.parseToDTO(i);
            dto.setFileList(invoiceFilesServiceI.getFilesByInvoiceId(i.getId()));
            return dto;
        }).collect(Collectors.toList());
        dtoPage.setRecords(result);
        dtoPage.setTotal(result.size());
        return dtoPage;
    }

    @Override
    public IPage<InvoiceDTO> getMyInvoicePageList(Page<Twinvoice> page, InvoiceVO query) throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        //如果只查当前人员时，部门条件不生效
        if (query.getBcurflag() != null && query.getBcurflag()) {
            query.setEmployid(user.getUserid());
        }
        if (query.getEndTime() == null) {
            query.setEndTime(DateUtil.offsetDay(new Date(), 1));
        } else {
            query.setEndTime(DateUtil.offsetDay(query.getEndTime(), 1));
        }
        LambdaQueryWrapper<Twinvoice> lqw = Wrappers.lambdaQuery();
        lqw.ge(ObjUtil.isNotNull(query.getStartTime()), Twinvoice::getDcreatetime, query.getStartTime());
        lqw.lt(ObjUtil.isNotNull(query.getEndTime()), Twinvoice::getDcreatetime, query.getEndTime());
        lqw.eq(ObjUtil.isNotNull(query.getEmployid()), Twinvoice::getEmployid, query.getEmployid());
        // 关联预开发票表，查询有欠款的发票
        if (ObjUtil.isNotNull(query.getBarrearsflag()) && query.getBarrearsflag()) {
            lqw.apply("exists (select 1 from twpreinvoiceapplication as p " +
                    "where p.invoiceid = twinvoice.id and (p.iapprovestatus = 1 or p.iapprovestatus = 2) " +
                    "and p.namountapply > p.namountreceived)");
        }
        Page<Twinvoice> paged = this.page(page, lqw);
        Page<InvoiceDTO> dtoPage = new Page<>();
        if (paged.getTotal() == 0) {
            return dtoPage;
        }
        List<InvoiceDTO> result = paged.getRecords().stream().map(i -> {
            InvoiceDTO dto = InvoiceDTO.parseToDTO(i);
            dto.setNamountreceived(BigDecimal.ZERO);
            dto.setNamounspent(BigDecimal.ZERO);
            dto.setNamountbalance(BigDecimal.ZERO);
            dto.setAmountarrearage(BigDecimal.ZERO);
            if (i.getItype() == InvoiceType.Invoicetype_5.getKey()) {
                // 预收款
                Twcustomercharge twcustomercharge = customerchargeServiceI.lambdaQuery()
                        .eq(Twcustomercharge::getInvoiceid, i.getId())
                        .one();
                if (ObjUtil.isNotNull(twcustomercharge)) {
                    dto.setNamountreceived(twcustomercharge.getNamountreceived());
                    dto.setNamounspent(twcustomercharge.getNamounspent());
                    dto.setNamountbalance(twcustomercharge.getNamountbalance());
                }
            } else {
                // 预开发票
                Twpreinvoiceapplication twpreinvoiceapplication = preinvoapplyServiceI.lambdaQuery()
                        .eq(Twpreinvoiceapplication::getInvoiceid, i.getId())
                        .one();
                if (ObjUtil.isNotNull(twpreinvoiceapplication)) {
                    dto.setAmountarrearage(twpreinvoiceapplication.getNamountapply().subtract(twpreinvoiceapplication.getNamountreceived()));
                    dto.setNamountreceived(twpreinvoiceapplication.getNamountreceived());
                }
            }
            dto.setFileList(invoiceFilesServiceI.getFilesByInvoiceId(i.getId()));
            return dto;
        }).collect(Collectors.toList());
        dtoPage.setRecords(result);
        dtoPage.setTotal(page.getTotal());
        return dtoPage;
    }
}
