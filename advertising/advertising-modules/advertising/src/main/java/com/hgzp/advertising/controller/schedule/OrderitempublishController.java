package com.hgzp.advertising.controller.schedule;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hgzp.advertising.pagemodel.business.dto.TaskQuotaUpdateDTO;
import com.hgzp.advertising.pagemodel.schedule.dto.ArrangeOrderItemDTO;
import com.hgzp.advertising.pagemodel.schedule.dto.CheckOrderItemDTO;
import com.hgzp.advertising.pagemodel.schedule.dto.ModifyOrderItemStatusDTO;
import com.hgzp.advertising.pagemodel.schedule.dto.OrderitemarrangeReq;
import com.hgzp.advertising.pagemodel.schedule.vo.PendPublishOrderVo;
import com.hgzp.advertising.service.schedule.OrderitempublishServicel;
import com.hgzp.core.constant.ValidateParam;
import com.hgzp.core.model.Tworderitemarrange;
import com.hgzp.core.page.DataCombo;
import com.hgzp.core.page.Json;
import com.hgzp.core.web.BaseController;
import com.hgzp.pagemodel.schedule.OrderPublishQueryResultVo;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import static com.hgzp.core.constant.ValidateParam.*;

/**
 * <p>
 * 广告刊发 前端控制器
 * </p>
 *
 * @author lhl
 * @since 2023-12-14
 */
@Validated
@RestController
@RequestMapping("/schedule/orderitempublish")
public class OrderitempublishController extends BaseController {
    @Autowired
    OrderitempublishServicel orderitempublishServicel;
    /**
     * 查询日期范围内待处理刊发的广告明细目
     * 方法功能: 查询日期范围内待处理刊发的广告明细目
     *
     * @param mediaId,foldId,areaverId,stratTime,endTime,pageNum,pageSize
     * @return PendPublishOrderVo
     * @author lhl
     * @date 2023/12/14
     */
    @GetMapping("/getPendPublishOrderItemList")
    public Json<List<OrderPublishQueryResultVo>> getPendPublishOrderItemList(String mediaId, String foldId, String areaverId, String stratTime, String endTime,int pageNum,int pageSize,String publishstatus) {
        IPage<OrderPublishQueryResultVo> list = orderitempublishServicel.getPendPublishOrderItemList(mediaId,foldId,areaverId,stratTime,endTime,pageNum,pageSize,publishstatus);
        return Json.success(list);
    }

    /**
     * 修改广告订单明细状态
     * 方法功能: 修改广告订单明细状态
     *
     * @param modifyOrderItemStatusDTO
     * @return
     * @author lhl
     * @date 2023/12/18
     */
    @PostMapping(value = "/modifyOrderItemStatus")
    public Json modifyOrderItemStatus(@RequestBody ModifyOrderItemStatusDTO modifyOrderItemStatusDTO) throws Exception {

        return orderitempublishServicel.modifyOrderItemStatus(modifyOrderItemStatusDTO);
    }

    /**
     * 核查广告订单明细
     * 方法功能: 核查广告订单明细
     *
     * @param checkOrderItemDTO
     * @return
     * @author lhl
     * @date 2023/12/18
     */
    @PostMapping(value = "/checkOrderItem")
    public Json checkOrderItem(@RequestBody CheckOrderItemDTO checkOrderItemDTO) throws Exception {

        return orderitempublishServicel.checkOrderItem(checkOrderItemDTO);
    }

    /**
     * 查询日期范围内核查报告
     * 方法功能: 查询日期范围内核查报告
     *
     * @param mediaId,stratTime,endTime,pageNum,pageSize
     * @return PendPublishOrderVo
     * @author lhl
     * @date 2023/12/14
     */
    @GetMapping("/getCheckOrderList")
    public Json<List<OrderPublishQueryResultVo>> getCheckOrderList(String mediaId, String stratTime, String endTime,int pageNum,int pageSize) {
        IPage<OrderPublishQueryResultVo> list = orderitempublishServicel.getCheckOrderList(mediaId,stratTime,endTime,pageNum,pageSize);
        return Json.success(list);
    }

    /**
     * 导出日期范围内核查报告
     * 方法功能: 导出日期范围内核查报告
     *
     * @param mediaId,stratTime,endTime,pageNum,pageSize
     * @return JSON
     * @author lhl
     * @date 2023/12/22
     */
    @GetMapping("/exportCheckOrderExcel")
    public void exportCheckOrderExcel(String mediaId, String stratTime, String endTime, int pageNum, int pageSize, HttpServletResponse response) {
        try {
            XSSFWorkbook xssfWorkbook = orderitempublishServicel.exportCheckOrderExcel(mediaId,stratTime,endTime,pageNum,pageSize);
            // 设置返回类型为excel
            response.setContentType("application/vnd.ms-excel; charset=UTF-8");
            response.setHeader("Content-Disposition", orderitempublishServicel.getExcelFileName());
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            OutputStream output = response.getOutputStream();
            xssfWorkbook.write(output);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 安排广告订单
     * 方法功能: 安排广告订单
     *
     * @param arrangeOrderItemDTO
     * @return
     * @author lhl
     * @date 2024/03/07
     */
    @PostMapping(value = "/arrangeOrderItem")
    public Json arrangeOrderItem(@RequestBody ArrangeOrderItemDTO arrangeOrderItemDTO) throws Exception {

        return orderitempublishServicel.arrangeOrderItem(arrangeOrderItemDTO);
    }

    /**
     * 查询媒体广告是否配置URL参数
     * 方法功能: 查询媒体广告是否配置URL参数
     *
     * @param mediaId,stratTime,endTime,pageNum,pageSize
     * @return PendPublishOrderVo
     * @author lhl
     * @date 2024/03/07
     */
    @GetMapping("/getAdUrlParameter")
    public Json<String> getAdUrlParameter(String mediaId) {
        return Json.success(orderitempublishServicel.getAdUrlParameter(mediaId));
    }


}
