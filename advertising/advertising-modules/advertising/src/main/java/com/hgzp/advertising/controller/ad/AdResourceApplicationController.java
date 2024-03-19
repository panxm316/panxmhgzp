package com.hgzp.advertising.controller.ad;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.pagemodel.ad.dto.AdResourceApplicationDTO;
import com.hgzp.advertising.pagemodel.ad.vo.AdCustomerResourceVO;
import com.hgzp.advertising.pagemodel.ad.vo.AdResourceApplicationVO;
import com.hgzp.advertising.pagemodel.flow.dto.ProcessInstanceDTO;
import com.hgzp.advertising.service.ad.TwadresourceadorderServiceI;
import com.hgzp.advertising.service.ad.TwadresourceapplicationServiceI;
import com.hgzp.core.constant.ValidateParam;
import com.hgzp.core.model.Twadresourceadorder;
import com.hgzp.core.model.Twadresourceapplication;
import com.hgzp.core.page.BaseQueryInfo;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.hgzp.advertising.emnus.approve.ApproveStatus.APPROVE_EDITING;
import static com.hgzp.advertising.emnus.approve.ApproveStatus.APPROVE_PASS;

/**
 * AdResourceApplicationController
 * 创建人：suny
 * 类描述：广告资源申请表 前端控制器
 * 创建日期：2023/11/11 13:12
 *
 * @folder ad/AdResourceApplicationController
 */
@RestController
@RequestMapping("/ad/adresourceapplication")
public class AdResourceApplicationController extends BaseController {
    @Autowired
    TwadresourceapplicationServiceI twadresourceapplicationServiceI;
    @Autowired
    TwadresourceadorderServiceI twadresourceadorderServiceI;

    /**
     * 根据条件查询广告资源申请表分页列表
     * 方法功能: 根据条件查询广告资源申请表分页列表
     *
     * @param pageRequest
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.advertising.pagemodel.ad.dto.AdResourceApplicationDTO>>
     * @author suny
     * @date 2023/11/11 15:40
     */
    @GetMapping("/getAdResourceApplicationPageList")
    public Json<List<AdResourceApplicationDTO>> getAdResourceApplicationPageList(PageRequest pageRequest,
                                                                                 AdResourceApplicationVO query) throws Exception {
        Page<Twadresourceapplication> page = getPage(pageRequest);
        IPage<AdResourceApplicationDTO> pageList =
                twadresourceapplicationServiceI.getAdResourceApplicationPageList(page, query);
        return Json.success(pageList);
    }

    /**
     * 新增保存广告资源申请表
     * 方法功能: 新增保存广告资源申请表
     *
     * @param entity
     * @return com.hgzp.core.page.Json
     * @throws Exception
     */
    @PostMapping(value = "/saveAdResourceApplication")
    public Json saveAdResourceApplication(@Validated(value = {ValidateParam.add.class}) @RequestBody AdResourceApplicationDTO entity) throws Exception {
        Long adresourceapplicationid = twadresourceapplicationServiceI.saveAdResourceApplication(entity);
        if (StringUtils.hasText(entity.getFlowid())) {
            twadresourceapplicationServiceI.addFlowApplication(entity.getId().toString(),
                    entity.getFlowid().toString());
        }
        return Json.success(adresourceapplicationid);
    }

    /**
     * 修改保存广告资源申请表
     * 方法功能: 修改保存广告资源申请表
     *
     * @param entity
     * @return com.hgzp.core.page.Json
     * @throws Exception
     */
    @PostMapping(value = "/updateAdResourceApplication")
    public Json updateAdResourceApplication(@Validated(value = {ValidateParam.edit.class}) @RequestBody AdResourceApplicationDTO entity) throws Exception {
        twadresourceapplicationServiceI.updateAdResourceApplication(entity);
        if (StringUtils.hasText(entity.getFlowid())) {
            twadresourceapplicationServiceI.addFlowApplication(entity.getId().toString(),
                    entity.getFlowid().toString());
        }
        return Json.success();
    }

    /**
     * 删除广告资源申请表
     * 方法功能: 删除广告资源申请表
     *
     * @param ids
     * @return com.hgzp.core.page.Json
     * @throws Exception
     */
    @PostMapping(value = "/deleteAdResourceApplicationById")
    public Json deleteAdResourceApplicationById(@NotNull(message = "ID不可为空") String ids) throws Exception {
        twadresourceapplicationServiceI.deleteAdResourceApplication(ids);
        return Json.success();
    }

    /**
     * 保存审核信息
     * 方法功能:  保存审核信息
     *
     * @param entity
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2023/11/13 10:28
     */
    @PostMapping(value = "/saveCheckInfo")
    public Json saveCheckInfo(@Validated(value = {ValidateParam.edit.class}) @RequestBody AdResourceApplicationDTO entity) throws Exception {
        if (!StringUtils.hasText(entity.getSapprovalopinions())) {
            return Json.fail("审核意见不可为空");
        }
        twadresourceapplicationServiceI.saveCheckInfo(entity);
        return Json.success();
    }

    /**
     * 提交审核
     * 方法功能: 提交审核
     *
     * @param ids
     * @param flowid
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2023/11/16 13:08
     */
    @PostMapping(value = "/updateCheck")
    public Json updateCheck(@NotNull(message = "ID不可为空") String ids, String flowid) throws Exception {
        twadresourceapplicationServiceI.submitCheck(ids, flowid);
        return Json.success();
    }

    /**
     * 根据id查询广告资源申请表详情
     * 方法功能:  根据id查询广告资源申请表详情
     *
     * @param id
     * @return com.hgzp.core.page.Json<com.hgzp.advertising.pagemodel.ad.dto.AdResourceApplicationDTO>
     * @author suny
     * @date 2023/11/16 9:18
     */
    @GetMapping("/getAdResourceApplicationById")
    public Json<AdResourceApplicationDTO> getAdResourceApplicationById(@NotNull(message = "ID不可为空") String id) throws Exception {
        return twadresourceapplicationServiceI.getAdResourceApplicationById(id);
    }

    /**
     * 获取快速预约订单对应的广告资源集合
     *
     * @param orderId 订单id
     * @return {@link Json}
     * @auther wangxk
     * @date 2024/01/15 16:07
     */
    @GetMapping("/listAdResourceApplicationByQuickOrderId")
    public Json listAdResourceApplicationByQuickOrderId(
            @NotNull(message = "ID不可为空") String orderId) throws Exception {
        // 查找此快速预约订单对应的广告资源申请id(有可能包含已经审批通过的数据+未审批通过的数据)
        List<Twadresourceadorder> twadresourceadorders = twadresourceadorderServiceI.list(
                Wrappers.<Twadresourceadorder>lambdaQuery().eq(Twadresourceadorder::getAdorderid, orderId));
        Assert.isTrue(twadresourceadorders.size() <= 2, "快速预约订单对应的广告资源记录数最大为2条");
        if (CollectionUtil.isEmpty(twadresourceadorders)) {
            return Json.success("订单不存在", null);
        }
        List<Object> resourceApplications = new ArrayList<>();
        for (Twadresourceadorder twadresourceadorder : twadresourceadorders) {
            resourceApplications.add(
                    twadresourceapplicationServiceI
                            .getAdResourceApplicationById(twadresourceadorder.getAdresourceapplicationid() + "")
                            .getObj());
        }
        return Json.success(resourceApplications);
    }

    /**
     * 根据资源id查询审核流程历史记录
     * 方法功能: 根据资源id查询审核流程历史记录
     *
     * @param id
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.advertising.pagemodel.flow.dto.ProcessInstanceDTO>>
     * @author suny
     * @date 2023/11/16 14:45
     */
    @GetMapping("/getProcessInstanceById")
    public Json<List<ProcessInstanceDTO>> getProcessInstanceById(@NotNull(message = "ID不可为空") String id) throws Exception {
        return twadresourceapplicationServiceI.getProcessInstanceById(id);
    }

    /**
     * 获取账户和客户的广告资源申请列表
     * 方法功能:
     *
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.advertising.pagemodel.ad.dto.AdResourceApplicationDTO>>
     * @author songly
     * @date 2023/12/12 16:57
     */
    @GetMapping("/getAdCustomerResourceApplicationPageList")
    public Json<List<AdResourceApplicationDTO>> getAdCustomerResourceApplicationPageList(PageRequest pageRequest,
                                                                             AdCustomerResourceVO  query) throws Exception {
        Page<Twadresourceapplication> page = getPage(pageRequest);
        IPage<AdResourceApplicationDTO> pageList = twadresourceapplicationServiceI.getAdResourceApplicationList(page,query);
        return Json.success(pageList);
    }
}
