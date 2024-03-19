package com.hgzp.advertising.service.customer;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.pagemodel.customer.vo.CustomerBelongVo;
import com.hgzp.advertising.pagemodel.customer.vo.CustomerQueryVo;
import com.hgzp.core.model.Twcustomer;
import com.hgzp.core.model.Twcustomerbelong;
import com.hgzp.core.page.Json;
import com.hgzp.service.common.IMyService;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * <p>
 * 客户归属业务员表 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-10-25
 */
public interface TwcustomerbelongServiceI extends IMyService<Twcustomerbelong> {

    /**
     * getCustomerbelong
     * 方法功能: 获取归属业务员
     *
     * @param sCustomerId
     * @return java.util.List<com.hgzp.core.model.Twcustomerbelong>
     * @author songly
     * @date 2023/10/26 10:33
     */
    List<CustomerBelongVo> getCustomerbelong(String sCustomerId);

    /**
     * getCustomerBelongById
     * 方法功能: 根据Id获取归属
     *
     * @param id
     * @return com.hgzp.advertising.pagemodel.customer.vo.CustomerBelongVo
     * @author songly
     * @date 2023/10/26 10:51
     */
    CustomerBelongVo getCustomerBelongById(String id);

    /**
     * getCustomerByEmpIds
     * 方法功能: 根据业务员获取客户ID
     *
     * @param empIds
     * @return java.util.List<java.lang.Long>
     * @author hgsongly
     * @date 2023/11/3 16:01
     */
    List<Long> getCustomerIdByEmpIds(List<Long> empIds);

    /**
     * saveCustomerBelong
     * 方法功能: 添加业务员归属
     *
     * @param twCustomerBelong
     * @return com.hgzp.core.page.Json
     * @author hgsongly
     * @date 2023/10/26 10:34
     */
    Json saveCustomerBelong(@RequestBody Twcustomerbelong twCustomerBelong);

    /**
     * updateCustomerBelong
     * 方法功能: 更新业务员归属
     *
     * @param twCustomerBelong
     * @return com.hgzp.core.page.Json
     * @author hgsongly
     * @date 2023/10/26 10:36
     */
    void updateCustomerBelong(@RequestBody Twcustomerbelong twCustomerBelong) throws Exception;

    /**
     * deleteCustomerBelongByIds
     * 方法功能: 删除
     *
     * @param ids
     * @param bCustomerId
     * @return void
     * @author hgsongly
     * @date 2023/10/26 10:36
     */
    void deleteCustomerBelongByIds(String ids, boolean bCustomerId) throws Exception;

    /**
     * isDuplicateInfo
     * 方法功能: 重名
     *
     * @param customerBelong
     * @return boolean
     * @author hgsongly
     * @date 2023/10/26 10:36
     */
    boolean isDuplicateInfo(Twcustomerbelong customerBelong);

    /**
     * combineCustomerBelong
     * 方法功能:合并客户归属
     *
     * @param sMainId
     * @param subIds
     * @return void
     * @author hgsongly
     * @date 2023/10/30 10:36
     */
    void combineCustomerBelong(String sMainId, String subIds) throws Exception;

    /**
     * 根据applicationid获取客户Id
     * 方法功能:根据applicationid获取客户Id
     *
     * @param applicationid
     * @return java.util.List<java.lang.Long>
     * @author hgsongly
     * @date 2023/11/8 16:58
     */
    List<Long> getCustomerIdByApplicationId(String applicationid);

    /**
     * 获取我的客户分页列表
     * 方法功能: 获取我的客户分页列表
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.core.model.Twcustomerbelong>
     * @throws Exception
     */
    IPage<Twcustomer> getMyCustomerPageList(Page<Twcustomer> page, CustomerQueryVo query) throws Exception;
}
