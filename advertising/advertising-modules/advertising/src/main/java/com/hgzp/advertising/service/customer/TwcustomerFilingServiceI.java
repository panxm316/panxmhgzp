package com.hgzp.advertising.service.customer;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.pagemodel.customer.vo.CustomerReq;
import com.hgzp.advertising.pagemodel.customer.vo.CustomerVo;
import com.hgzp.core.model.Twcustomer;
import com.hgzp.core.page.Json;
import com.hgzp.service.common.IMyService;

/**
 * 客户备案服务
 *
 * @author songly
 * @date 2023/11/6 9:00
 */
public interface TwcustomerFilingServiceI extends IMyService<Twcustomer> {
    /**
     * 获取我的（我负责的部门）客户
     * 方法功能:客户备案检索
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.advertising.pagemodel.customer.vo.CustomerVo>
     * @author songly
     * @date 2023/11/3 14:12
     */
    IPage<CustomerVo> getMyCustomerPageList(Page<Twcustomer> page, CustomerReq query) throws Exception;

    /**
     * saveCustomer
     * 方法功能:保存新增客户信息
     *
     * @param customerVo
     * @return boolean
     * @author songly
     * @date 2023/11/06 09:16
     */
    void saveCustomer(CustomerVo customerVo) throws Exception;

    /**
     * updateCustomer
     * 方法功能:更新
     *
     * @param customerVo
     * @return boolean
     * @author songly
     * @date 2023/11/06 09:16
     */
    void updateCustomer(CustomerVo customerVo) throws Exception;

    /**
     * approveCustomer
     * 方法功能: 申请审批
     *
     * @param customerId
     * @param flowId
     * @return void
     * @author hgsongly
     * @date 2023/11/8 15:00
     */
    Json<String> approveCustomer(String customerId, String flowId);

    /**
     * 根据审批结果修改审批状态及意见
     * 方法功能: 根据审批结果修改审批状态
     *
     * @param applicationid
     * @param result
     * @param approveDesc
     * @return void
     * @author songly
     * @date 2023/11/8 16:54
     */
    //Json updateCustomerApprovalopinions(String applicationid, String sCustomerId, boolean result, String approveDesc);
    Json updateCustomerApprovalopinions(String applicationid, String sCustomerId, boolean bUpdatepinion,String approveDesc,Integer iapproveStatus);
    /**
     * 修改客户及归属状态
     * 方法功能:
     *
     * @param applicationid
     * @param sCustomerId
     * @return com.hgzp.core.page.Json
     * @author hgsongly
     * @date 2023/11/13 17:27
     */
    //Json updateCustomerStatus00(String applicationid, String sCustomerId, Integer iApprovestatus) throws Exception;
}
