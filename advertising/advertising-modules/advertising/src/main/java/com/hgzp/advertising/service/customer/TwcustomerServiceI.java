package com.hgzp.advertising.service.customer;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.pagemodel.customer.vo.CustomerReq;
import com.hgzp.advertising.pagemodel.customer.vo.CustomerVo;
import com.hgzp.core.model.Twcustomer;
import com.hgzp.core.page.DataCombo;
import com.hgzp.core.page.Json;
import com.hgzp.service.common.IMyService;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 客户表 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-10-25
 */
public interface TwcustomerServiceI extends IMyService<Twcustomer> {
    /**
     * getCustomerPageList
     * 方法功能:获取客户信息列表
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.advertising.pagemodel.customer.vo.CustomerVo>
     * @author songly
     * @date 2023/10/25 15:33
     */
    IPage<CustomerVo> getCustomerPageList(Page<Twcustomer> page, CustomerReq query);

    /**
     * getCustomerCombo
     * 方法功能:根据名称相似的下拉客户信息
     *
     * @param name
     * @return java.util.List<com.hgzp.core.page.DataCombo>
     * @author songly
     * @date 2023/11/1 9:06
     */
    List<DataCombo> getCustomerCombo(String name);

    /**
     * getCustomerById
     * 方法功能:  根据Id获取信息
     *
     * @param id
     * @return com.hgzp.advertising.pagemodel.customer.vo.CustomerVo
     * @author hgsongly
     * @date 2023/10/26 9:22
     */
    CustomerVo getCustomerById(@NotNull(message = "ID不可为空") String id);

    /**
     * saveCustomer
     * 方法功能:保存新增客户信息
     *
     * @param customerVo
     * @return boolean
     * @author songly
     * @date 2023/10/25 15:16
     */
    void saveCustomer(CustomerVo customerVo) throws Exception;

    /**
     * updateCustomer
     * 方法功能:更新
     *
     * @param customerVo
     * @return boolean
     * @author songly
     * @date 2023/10/25 15:36
     */
    void updateCustomer(CustomerVo customerVo) throws Exception;

    /**
     * deleteCustomerByIds
     * 方法功能: 删除
     *
     * @param ids
     * @return boolean
     * @author songly
     * @date 2023/10/25 15:37
     */
    Json deleteCustomerByIds(String ids);

    /**
     * isExistCustomer
     * 方法功能:是否存在
     *
     * @param twcustomer
     * @return boolean
     * @author hgsongly
     * @date 2023/10/25 15:37
     */
    boolean isExistCustomer(Twcustomer twcustomer);

    /**
     * 客户合并
     * 方法功能:  客户合并
     *
     * @param sMainId
     * @param subIds
     * @return void
     * @author hgsongly
     * @date 2023/10/30 17:06
     */
    void combineCustomer(String sMainId, String subIds) throws Exception;

}
