package com.hgzp.advertising.service.business;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hgzp.advertising.pagemodel.business.dto.BankAccountAllocateDTO;
import com.hgzp.advertising.pagemodel.business.vo.BankAccountAllocateVO;
import com.hgzp.core.model.Twbankaccountallocate;

import java.math.BigDecimal;

/**
 * <p>
 * 银行流水分配表 服务类（银行流水分配表已经作废，功能全部作废）
 * </p>
 *
 * @author wwk
 * @since 2023-12-04
 */
public interface TwbankaccountallocateServiceI extends IService<Twbankaccountallocate> {

    /**
     * 方法功能: 获取银行流水分配表分页列表
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.core.model.Twbankaccountallocate>
     * @author suny
     * @date 2023/12/9 9:01
     */
    IPage<BankAccountAllocateDTO> getBankAccountAllocatePageList1(Page<Twbankaccountallocate> page, BankAccountAllocateVO query) throws Exception;

    /**
     * 方法功能: 新增保存银行流水分配表
     *
     * @param entity
     * @return void
     * @author suny
     * @date 2023/12/9 9:01
     */
    void saveBankAccountAllocate1(BankAccountAllocateDTO entity) throws Exception;

    /**
     * 方法功能: 修改保存银行流水分配表
     *
     * @param entity
     * @return void
     * @author suny
     * @date 2023/12/9 9:01
     */
    void updateBankAccountAllocate1(BankAccountAllocateDTO entity) throws Exception;

    /**
     * 方法功能: 分配状态提交
     *
     * @param id
     * @param istatus
     * @return void
     * @author suny
     * @date 2023/12/12 16:54
     */
    void updateBankAccountAllocateStatus1(String id, int istatus) throws Exception;


    /**
     * 方法功能:  根据发票id/订单id获取银行流水分配的额度总和
     *
     * @param invoiceId
     * @param orderId
     * @return java.math.BigDecimal
     * @author suny
     * @date 2023/12/9 10:41
     */
    BigDecimal getInvoiceAllocateByInvoiceId1(Long invoiceId, Long orderId);

    /**
     * 方法功能:  删除选择的银行流水分配表，同时更新银行流水表的分配金额
     *
     * @param ids
     * @return void
     * @author suny
     * @date 2023/12/12 10:36
     */
    void deleteBankAccountAllocate1(String ids) throws Exception;
}
