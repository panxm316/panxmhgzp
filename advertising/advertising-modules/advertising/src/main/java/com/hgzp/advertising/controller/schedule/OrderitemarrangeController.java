package com.hgzp.advertising.controller.schedule;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.pagemodel.ad.vo.OrderItemMarrangePosReq;
import com.hgzp.advertising.pagemodel.schedule.dto.OrderItemBatchArrangeDTO;
import com.hgzp.advertising.pagemodel.schedule.dto.OrderitemarrangeReq;
import com.hgzp.advertising.pagemodel.schedule.vo.OrderitemarrangeVO;
import com.hgzp.advertising.pagemodel.schedule.vo.PageOrderItemArrangeVo;
import com.hgzp.advertising.service.schedule.OrderitemarrangeServiceI;
import com.hgzp.core.model.Tworderitem;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

import java.util.List;

import static com.hgzp.core.constant.ValidateParam.*;

/**
 * OrderitemarrangeController
 * 创建人：muyn
 * 类描述：广告安排表 前端控制器
 * 创建日期：2023-12-06
 *
 * @folder schedule/OrderitemarrangeController
 */
@Validated
@RestController
@RequestMapping("/schedule/orderitemarrange")
public class OrderitemarrangeController extends BaseController {

    @Autowired
    private OrderitemarrangeServiceI tworderitemarrangeService;

    /**
     * 修改广告订单明细安排
     *
     * @param orderItemBatchArrangeDTO 广告安排表数据传输对象
     * @return {@link Json}
     * @author muyn
     * @since 2023-12-06
     */
    @PostMapping("/saveOrderitemarrange")
    public Json saveOrderitemarrange(@RequestBody @Validated(edit.class) OrderItemBatchArrangeDTO orderItemBatchArrangeDTO) {
        try {
            tworderitemarrangeService.bacthOrderitemarrange(orderItemBatchArrangeDTO);
            return Json.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }
    }

    /**
     * 根据Id查询广告订单明细安排
     *
     * @param id
     * @return {@link Json}
     * @author muyn
     * @since 2023-12-06
     */
    @GetMapping("/getOrderitemarrangeById")
    public Json<OrderitemarrangeVO> getOrderitemarrangeById(@NotNull(message = "ID不可为空") String id) {
        return Json.success(tworderitemarrangeService.getOrderitemarrangeById(id));
    }

    /**
     * 根据条件查询广告订单明细安排列表
     *
     * @param query
     * @return {@link Json}
     * @author muyn
     * @since 2023-12-06
     */
    @GetMapping("/getOrderitemarrangeList")
    public Json<List<OrderitemarrangeVO>> list(OrderitemarrangeReq query) {
        List<OrderitemarrangeVO> lsResult = tworderitemarrangeService.getOrderitemarrangelist(query);
        return Json.success(lsResult);
    }

    /**
     * 根据条件查询广告订单明细安排分页数据
     *
     * @param pageRequest 分页请求参数
     * @param query
     * @return {@link Json}
     * @author muyn
     * @since 2023-12-06
     */
    @GetMapping("/getOrderitemarrangePageList")
    public Json<List<OrderitemarrangeVO>> getOrderitemarrangePageList(PageRequest pageRequest,
                                                                      OrderitemarrangeReq query) {
        Page<Tworderitem> page = getPage(pageRequest);
        return Json.success(tworderitemarrangeService.getOrderitemarrangePageList(page, query));
    }

    /**
     * 获取版面已安排订单信息
     * 方法功能:
     *
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.advertising.pagemodel.schedule.vo.PageOrderItemArrangeVo>>
     * @author songly
     * @date 2024/1/23 15:27
     */
    @GetMapping("/getPageOrderItemArrangeInfo")
    public Json<List<PageOrderItemArrangeVo>> getPageOrderItemArrangeInfoList(OrderitemarrangeReq query) {
        return Json.success(tworderitemarrangeService.getPageOrderItemArrangeVoList(query));
    }

    /**
     * 取消安排（更改状态及恢复预安排版面）
     * 方法功能:
     *
     * @param id
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.advertising.pagemodel.schedule.vo.PageOrderItemArrangeVo>>
     * @author songly
     * @date 2024/1/29 14:32
     */
    @PostMapping("/updateOrderitemarrangeStatus")
    public Json updateOrderitemarrangeStatus(@NotNull(message = "ID不可为空") String id) {
        try {
            tworderitemarrangeService.updateOrderitemarrangeStatus(id);
            return Json.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }
    }

    /**
     * 更新订单明细安排坐标
     * 方法功能:
     *
     * @param marrangePosReq
     * @return com.hgzp.core.page.Json
     * @author hgsongly
     * @date 2024/1/30 9:57
     */
    @PostMapping("/updateOrderitemarrangePos")
    public Json updateOrderitemarrangePos(@RequestBody OrderItemMarrangePosReq marrangePosReq) {
        try {
            tworderitemarrangeService.updateOrderitemarrangePos(marrangePosReq);
            return Json.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }
    }

    /**
     * 修改订单明细的发布状态
     * 方法功能:
     *
     * @param id
     * @param bPublish
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2024/1/30 16:17
     */
    @PostMapping("/updateOrderitemPublishStatus")
    public Json updateOrderitemPublishStatus(@NotNull(message = "ID不可为空") String id, boolean bPublish) {
        try {
            tworderitemarrangeService.updateOrderitemPublishStatus(id, bPublish);
            return Json.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }
    }
}
