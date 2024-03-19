package com.hgzp.advertising.service.finance;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hgzp.advertising.pagemodel.finance.dto.CustomerChargeBankDTO;
import com.hgzp.advertising.pagemodel.finance.dto.CustomerChargeDTO;
import com.hgzp.advertising.pagemodel.finance.vo.CustomerChargeBankVO;
import com.hgzp.advertising.pagemodel.finance.vo.CustomerChargeVO;
import com.hgzp.core.model.Twcustomercharge;
import com.hgzp.core.page.BaseQueryInfo;
import com.hgzp.core.page.Json;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 客户收费表 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-10-27
 */
public interface TwcustomerchargeServiceI extends IService<Twcustomercharge> {


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
    IPage<CustomerChargeVO> getCustomerChargePageList(CustomerChargeVO query, IPage<Twcustomercharge> page);

    /**
     * 获取收款明细详情
     * 方法功能: 获取收款明细详情
     *
     * @param id
     * @return com.hgzp.advertising.pagemodel.finance.vo.CustomerChargeVO
     * @author wangwk
     * @date 2023/11/1 10:55
     */
    CustomerChargeVO getCustomerChargeDetail(Long id);

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
    Long saveCustomerCharge(CustomerChargeDTO customerChargeDTO) throws Exception;

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
    void updateCustomerCharge(CustomerChargeDTO customerChargeDTO) throws Exception;

    void deleteCustomerCharge(Long id) throws Exception;

    //------------------------以下是银行流水分配使用的客户收款查询------------------------

    /**
     * 方法功能: 获取银行流水分配到客户收费表数据分页列表
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.advertising.pagemodel.finance.dto.CustomerChargeBankDTO>
     * @author suny
     * @date 2023/12/20 13:42
     */
    IPage<CustomerChargeBankDTO> getBankwCustomerChargePageList(Page<Twcustomercharge> page, CustomerChargeBankVO query) throws Exception;

    /**
     * 银行流水分配明细POJO转DTO
     * 方法功能:有查询操作，涉及 银行流水单 Twbankaccounts 表、发票信息 Twpreinvoiceapplication 表（Twcustomercharge -> CustomerChargeBankDTO）
     *
     * @param twcustomercharges
     * @return java.util.List<com.hgzp.advertising.pagemodel.finance.dto.CustomerChargeBankDTO>
     * @author yanz
     * @date 2024/1/8 9:05
     */
    List<CustomerChargeBankDTO> convertToCustomerChargeBankDtosForAllocationDetail(List<Twcustomercharge> twcustomercharges);

    /**
     * 方法功能: 新增保存银行流水分配到客户收费表数据
     *
     * @param entity
     * @return void
     * @author suny
     * @date 2023/12/20 13:42
     */
    void saveBankCustomerCharge(CustomerChargeBankDTO entity) throws Exception;

    /**
     * 方法功能: 修改保存银行流水分配到客户收费表数据
     *
     * @param entity
     * @return void
     * @author suny
     * @date 2023/12/20 13:42
     */
    void updateBankCustomerCharge(CustomerChargeBankDTO entity) throws Exception;

    /**
     * 方法功能: 分配状态提交
     *
     * @param id
     * @param istatus
     * @return void
     * @author suny
     * @date 2023/12/20 13:42
     */
    void updateCustomerChargeStatus(String id, int istatus) throws Exception;

    /**
     * 方法功能:  根据发票id/订单id获取银行流水分配到客户收费表数据
     *
     * @param invoiceId
     * @param orderId
     * @return java.math.BigDecimal
     * @author suny
     * @date 2023/12/20 13:43
     */
    BigDecimal getCustomerChargeByInvoiceId(Long preinvoiceId, Long orderId);

    /**
     * 方法功能: 删除选择的银行流水分配到客户收费表数据，同时更新银行流水表的分配金额
     *
     * @param ids
     * @return void
     * @author suny
     * @date 2023/12/20 13:43
     */
    void deleteBankCustomerCharge(String ids) throws Exception;

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
    IPage<CustomerChargeBankDTO> getInvoiceChargePageList(Page<Twcustomercharge> page, BaseQueryInfo query) throws Exception;

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
    Boolean submitConfirmation(Long customerChargeId, Integer iStatus, String comments);

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
    void writeOffPreinvoiceapplication(Long customerChargeId, List<Long> orderitemIds, String dateString) throws Exception;

    /**
     * 查询收费表中 有余额的 客户预收费项目
     * 方法功能:根据客户名称或者发票号，查询收费表中 有余额的 客户预收费项目
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.advertising.pagemodel.finance.dto.CustomerChargeBankDTO>
     * @author yanz
     * @date 2023/12/25 10:29
     */
    IPage<CustomerChargeBankDTO> getCustomerChargeBankDTOList(Page<Twcustomercharge> page, CustomerChargeBankVO query) throws Exception;

    /**
     * 客户收费POJO转DTO
     * 方法功能:有查询操作，涉及invoice表（Twcustomercharge -> CustomerChargeBankDTO）
     *
     * @param twcustomercharges
     * @return java.util.List<com.hgzp.advertising.pagemodel.finance.dto.CustomerChargeBankDTO>
     * @author yanz
     * @date 2024/1/6 9:04
     */
    List<CustomerChargeBankDTO> convertToCustomerChargeBankDtosForToPrePaymentInfo(List<Twcustomercharge> twcustomercharges);

    /**
     * 更新客户收费表中的客户（用于合并客户）
     * 方法功能:
     *
     * @param customerIds
     * @param newcustomerId
     * @param newcustomername
     * @return void
     * @author songly
     * @date 2024/1/17 14:56
     */
    void updateChargeCustomer(String customerIds, Long newcustomerId, String newcustomername) throws Exception;

    /**
     * 根据客户id获取客户收费表中的客户收费记录
     * 方法功能:  根据客户id获取客户收费表中的客户收费记录
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.core.model.Twcustomercharge>
     * @author suny
     * @date 2024/1/30 15:11
     */
    IPage<Twcustomercharge> getCustomerChargeByCustomerid(IPage<Twcustomercharge> page, CustomerChargeVO query);

    /**
     * 根据客户id获取客户收费表中的客户收费记录汇总、剩余金额汇总
     * 方法功能: 根据客户id获取客户收费表中的客户收费记录汇总 、剩余金额汇总
     *
     * @param query
     * @return com.hgzp.core.model.Twcustomercharge
     * @author suny
     * @date 2024/1/30 15:22
     */
    Twcustomercharge getMyCustomerChargeCount(CustomerChargeVO query);

    /**
     * 获取银行流水分配情况
     * 方法功能:  获取银行流水分配情况
     *
     * @param bankid
     * @return java.util.List<com.hgzp.advertising.pagemodel.finance.dto.CustomerChargeBankDTO>
     * @author suny
     * @date 2024/3/6 18:41
     */
    List<CustomerChargeBankDTO> getCustomerChargeByBankid(Long bankid);

    /**
     * 财务人员直接对银行流水进行核销
     * 方法功能: 财务人员直接对银行流水进行核销，先写收款表，再写核销表
     *
     * @param entity
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2024/3/15 16:55
     */
    Json bankAcountWriteOff(CustomerChargeBankDTO entity) throws Exception;
}
