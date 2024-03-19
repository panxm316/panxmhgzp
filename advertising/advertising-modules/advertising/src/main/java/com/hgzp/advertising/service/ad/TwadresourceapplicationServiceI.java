package com.hgzp.advertising.service.ad;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hgzp.advertising.pagemodel.ad.dto.AdResourceApplicationDTO;
import com.hgzp.advertising.pagemodel.ad.vo.AdCustomerResourceVO;
import com.hgzp.advertising.pagemodel.ad.vo.AdResourceApplicationVO;
import com.hgzp.advertising.pagemodel.flow.dto.ProcessInstanceDTO;
import com.hgzp.core.model.Twadresourceapplication;
import com.hgzp.core.model.Twadresourcefiles;
import com.hgzp.core.page.Json;

import java.util.List;

/**
 * <p>
 * 广告资源申请表 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-11-10
 */
public interface TwadresourceapplicationServiceI extends IService<Twadresourceapplication> {
    /**
     * 方法功能: 根据条件查询广告资源申请表分页列表
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.advertising.pagemodel.resource.dto.AdResourceApplicationDTO>
     * @author suny
     * @date 2023/11/11 15:14
     */
    IPage<AdResourceApplicationDTO> getAdResourceApplicationPageList(Page<Twadresourceapplication> page,
                                                                     AdResourceApplicationVO query) throws Exception;

    /**
     * 广告资源申请POJO转DTO
     * 方法功能:有查询操作，涉及Twadresourcefiles广告资源文件表
     *
     * @param adresourceapplicationList
     * @return java.util.List<com.hgzp.advertising.pagemodel.ad.dto.AdResourceApplicationDTO>
     * @author yanz
     * @date 2024/1/5 16:24
     */
    List<AdResourceApplicationDTO> convertAdResourceApplicationToDTOs(List<Twadresourceapplication> adresourceapplicationList);

    /**
     * 方法功能: 新增保存广告资源申请表
     *
     * @param entity
     * @return void
     * @author suny
     * @date 2023/11/11 15:14
     */
    Long saveAdResourceApplication(AdResourceApplicationDTO entity) throws Exception;

    /**
     * 方法功能: 修改保存广告资源申请表
     *
     * @param entity
     * @return void
     * @author suny
     * @date 2023/11/11 15:15
     */
    void updateAdResourceApplication(AdResourceApplicationDTO entity) throws Exception;

    /**
     * 方法功能: 删除广告资源申请表
     *
     * @param ids
     * @return void
     * @author suny
     * @date 2023/11/11 15:15
     */
    void deleteAdResourceApplication(String ids) throws Exception;

    /**
     * 方法功能: 保存审核信息
     *
     * @param entity
     * @return void
     * @author suny
     * @date 2023/11/13 10:28
     */
    void saveCheckInfo(AdResourceApplicationDTO entity) throws Exception;

    /**
     * 广告资源提交审核, 合并updateCheck和addFlowApplication
     *
     * @param ids    广告资源ids 以,隔开
     * @param flowid 流程id
     * @return void
     * @author wangxk
     * @date 2024/01/11 10:47
     */
    void submitCheck(String ids, String flowid) throws Exception;

    /**
     * 方法功能: 更新审核信息为在审核
     *
     * @param ids
     * @param flowid
     * @return void
     * @author suny
     * @date 2023/11/15 10:39
     */
    void updateCheck(String ids, String flowid) throws Exception;

    /**
     * 方法功能: 新增流程申请信息，将流程id存入数据表
     *
     * @param ids
     * @param flowid
     * @return void
     * @author suny
     * @date 2023/11/15 10:40
     */
    void addFlowApplication(String ids, String flowid) throws Exception;

    /**
     * 方法功能: 审批流程结束后回调方法，修改审批状态及审批意见
     *
     * @param applicationid
     * @param businessId
     * @param result
     * @param approveDesc
     * @return void
     * @author suny
     * @date 2023/11/15 12:53
     */
    void updateCheckInfo(String applicationid, String businessId, boolean result, String approveDesc);

    /**
     * 方法功能:  根据id查询广告资源申请表
     *
     * @param id
     * @return com.hgzp.core.page.Json<com.hgzp.advertising.pagemodel.ad.dto.AdResourceApplicationDTO>
     * @author suny
     * @date 2023/11/16 9:18
     */
    Json<AdResourceApplicationDTO> getAdResourceApplicationById(String id) throws Exception;

    /**
     * 方法功能: 根据申请id获取审批历史流程列表
     *
     * @param id
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.advertising.pagemodel.flow.dto.ProcessInstanceDTO>>
     * @author suny
     * @date 2023/11/16 14:19
     */
    Json<List<ProcessInstanceDTO>> getProcessInstanceById(String id) throws Exception;

    /**
     * 检索当前账户或客户的资源列表
     * 方法功能:
     *
     * @param query
     * @return java.util.List<com.hgzp.advertising.pagemodel.ad.dto.AdResourceApplicationDTO>
     * @author songly
     * @date 2023/12/12 16:49
     */
    IPage<AdResourceApplicationDTO> getAdResourceApplicationList(Page<Twadresourceapplication> page,
                                                                 AdCustomerResourceVO query) throws Exception;

    /**
     * 更新广告资源申请表的客户id和客户名称
     * 方法功能:
     *
     * @param customerIds
     * @param newcustomerId
     * @param newcustomername
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2024/1/18 9:17
     */
    void updateAdResourceCustomer(String customerIds, Long newcustomerId, String newcustomername) throws Exception;

    /**
     * 根据订单id获取广告资源文件
     * 方法功能:   根据订单id获取广告资源文件
     *
     * @param orderid
     * @return java.util.List<com.hgzp.core.model.Twadresourcefiles>
     * @author suny
     * @date 2024/3/6 14:08
     */
    List<Twadresourcefiles> getResourceFilesByOrderIdForCJ(String orderid);
}
