package com.hgzp.advertising.service.customer;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hgzp.advertising.pagemodel.customer.dto.CustomerServiceDTO;
import com.hgzp.advertising.pagemodel.customer.vo.CustomerServiceVO;
import com.hgzp.core.model.Twcustomerservice;
import com.hgzp.core.page.Json;

/**
 * <p>
 * 客户服务表 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-11-09
 */
public interface TwcustomerserviceServiceI extends IService<Twcustomerservice> {
    /**
     * 方法功能: 根据条件查询客户服务列表
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.core.model.Twcustomerservice>
     * @author suny
     * @date 2023/11/9 10:58
     */
    IPage<Twcustomerservice> getCustomerServicePageList(Page<Twcustomerservice> page, CustomerServiceVO query) throws Exception;

    /**
     * 方法功能: 新增保存客户服务信息
     *
     * @param entity
     * @return void
     * @author suny
     * @date 2023/11/9 10:58
     */
    void saveCustomerService(CustomerServiceDTO entity) throws Exception;

    /**
     * 方法功能: 修改保存客户服务信息
     *
     * @param entity
     * @return void
     * @author suny
     * @date 2023/11/9 10:58
     */
    void updateCustomerService(CustomerServiceDTO entity) throws Exception;

    /**
     * 方法功能: 删除客户服务信息(逻辑删除)
     *
     * @param ids
     * @return void
     * @author suny
     * @date 2023/11/9 10:59
     */
    void deleteCustomerService(String ids) throws Exception;

    /**
     * 更新客户服务表中的客户id和客户名称
     * 方法功能:
     *
     * @param customerIds
     * @param newcustomerId
     * @param newcustomername
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2024/1/18 10:06
     */
    void updateCustomerServiceCustomer(String customerIds, Long newcustomerId, String newcustomername) throws Exception;
}
