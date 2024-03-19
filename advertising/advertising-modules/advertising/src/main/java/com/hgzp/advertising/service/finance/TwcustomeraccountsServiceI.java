package com.hgzp.advertising.service.finance;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hgzp.core.model.Twcustomeraccounts;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 客户账户表 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-10-27
 */
public interface TwcustomeraccountsServiceI extends IService<Twcustomeraccounts> {


    /**
     * 更新客户账户余额
     * 方法功能:
     * 1、首先判断客户是否存在账户，如果不存在则创建账户
     * 2、更新客户账户余额、操作员
     *
     * @param customerid      客户id
     * @param namountreceived 入账金额
     * @param logSlaveId      记录日志的子数据id
     * @return void
     * @author wangwk
     * @date 2023/10/28 15:06
     */
    void updateCustomeraccountsNamountbalance(Long customerid, BigDecimal namountreceived, Long logSlaveId) throws Exception;

    /**
     * 更新
     * 方法功能:
     *
     * @param customerIds
     * @param newcustomerId
     * @param newcustomername
     * @return com.hgzp.core.page.Json
     * @author hgsongly
     * @date 2024/1/18 10:18
     */
    void updateCustomeraccountsCustomer(String customerIds, Long newcustomerId, String newcustomername) throws Exception;

    /**
     * 根据客户id获取客户账户列表
     * 方法功能: 根据客户id获取客户账户列表
     *
     * @param customerId
     * @param startTime
     * @param endTime
     * @return java.util.List<com.hgzp.core.model.Twcustomeraccounts>
     * @author suny
     * @date 2024/1/26 16:26
     */
    List<Twcustomeraccounts> getCustomerAccountsByCustomerid(String customerId, String startTime, String endTime) throws Exception;
}
