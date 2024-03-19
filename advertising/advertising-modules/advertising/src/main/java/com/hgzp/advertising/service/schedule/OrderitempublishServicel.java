package com.hgzp.advertising.service.schedule;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hgzp.advertising.pagemodel.business.dto.TaskQuotaUpdateDTO;
import com.hgzp.advertising.pagemodel.schedule.dto.ArrangeOrderItemDTO;
import com.hgzp.advertising.pagemodel.schedule.dto.CheckOrderItemDTO;
import com.hgzp.advertising.pagemodel.schedule.dto.ModifyOrderItemStatusDTO;
import com.hgzp.advertising.pagemodel.schedule.vo.PendPublishOrderVo;
import com.hgzp.core.page.DataCombo;
import com.hgzp.core.page.Json;
import com.hgzp.pagemodel.schedule.OrderPublishQueryResultVo;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * OrderitempublishServicel
 * 创建人：LHL
 * 类描述：广告刊发服务类
 * 创建日期：2023/11/27 9:39
 */
public interface OrderitempublishServicel {
    /**
     * 查询日期范围内待处理刊发的广告明细目
     * 方法功能: 查询日期范围内待处理刊发的广告明细目
     *
     * @param mediaId,foldId,areaverId,stratTime,endTime
     * @return PendPublishOrderVo
     * @author lhl
     * @date 2023/12/14
     */
    IPage<OrderPublishQueryResultVo> getPendPublishOrderItemList(String mediaId, String foldId, String areaverId, String stratTime, String endTime,int pageNum,int pageSize,String publishstatus);

    /**
     * 方法功能: 修改订单状态
     *
     * @param modifyOrderItemStatusDTO
     * @return void
     * @author lhl
     * @date 2023/12/18
     * @return  Json
     */
    Json modifyOrderItemStatus(ModifyOrderItemStatusDTO modifyOrderItemStatusDTO) throws Exception;

    /**
     * 方法功能: 核查广告订单明细
     *
     * @param checkOrderItemDTO
     * @return void
     * @author lhl
     * @date 2023/12/18
     * @return  Json
     */
    Json checkOrderItem(CheckOrderItemDTO checkOrderItemDTO) throws Exception;

    /**
     * 查询日期范围内核查报告
     * 方法功能: 查询日期范围内核查报告
     *
     * @param mediaId,foldId,areaverId,stratTime,endTime
     * @return PendPublishOrderVo
     * @author lhl
     * @date 2023/12/21
     */
    IPage<OrderPublishQueryResultVo> getCheckOrderList(String mediaId, String stratTime, String endTime,int pageNum,int pageSize);

    /**
     * 导出日期范围内核查报告
     * 方法功能: 导出日期范围内核查报告
     *
     * @param mediaId,foldId,areaverId,stratTime,endTime
     * @return PendPublishOrderVo
     * @author lhl
     * @date 2023/12/22
     */
    XSSFWorkbook exportCheckOrderExcel(String mediaId, String stratTime, String endTime, int pageNum, int pageSize);

    /**
     * 获取导出的EXCEL文件名称
    */
    String getExcelFileName();

    /**
     * 安排广告订单
     * 方法功能: 安排广告订单
     *
     * @param arrangeOrderItemDTO
     * @return
     * @author lhl
     * @date 2024/03/07
     */
    Json arrangeOrderItem(@RequestBody ArrangeOrderItemDTO arrangeOrderItemDTO) throws Exception;

    /**
     * 安排广告订单
     * 方法功能: 安排广告订单
     *
     * @param
     * @return
     * @author lhl
     * @date 2024/03/07
     */
    String getAdUrlParameter(String mediaId);

}
