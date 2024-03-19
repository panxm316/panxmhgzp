package com.hgzp.advertising.controller.schedule;

import com.hgzp.advertising.pagemodel.schedule.vo.AdvDisplayFormVo;
import com.hgzp.advertising.pagemodel.schedule.vo.AdvOrderItemVo;
import com.hgzp.advertising.pagemodel.schedule.vo.MediaPagePlanVo;
import com.hgzp.advertising.pagemodel.schedule.vo.PageOrderVO;
import com.hgzp.advertising.service.schedule.ScheduleseekServiceI;
import com.hgzp.core.model.Tbfoldpageplan;
import com.hgzp.core.model.Tworderitem;
import com.hgzp.core.page.DataCombo;
import com.hgzp.core.page.Json;
import com.hgzp.core.web.BaseController;
import com.hgzp.pagemodel.schedule.OrderPublishQueryResultVo;
import com.hgzp.pagemodel.schedule.PageOrderItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ScheduleseekController
 * 创建人：peij
 * 类描述：广告排期查看
 * 创建日期：2023/11/25 13:19
 *
 * @modify lhl
 * @modify-date 2023-13-19
 * @folder schedule/ScheduleseekController
 */
@RestController
@RequestMapping("/schedule/scheduleseek")
public class ScheduleseekController extends BaseController {
    @Autowired
    ScheduleseekServiceI scheduleseekServiceI;

    /**
     * 根据叠次id获取叠次版本下拉框列表
     * 方法功能: 根据叠次id获取叠次版本下拉框列表
     *
     * @param foldid
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.page.DataCombo>>
     * @author lhl
     * @date 2023/11/29
     */
    @GetMapping("/getFoldAreaverCombo")
    public Json<List<DataCombo>> getFoldAreaverCombo(String foldid) {
        List<DataCombo> foldCombo = scheduleseekServiceI.getFoldAreaverCombo(foldid);
        return Json.success(foldCombo);
    }

    /**
     * 根据叠次id、叠次版本id、日期范围获取版面计划列表
     * 方法功能: 根据叠次id、叠次版本id、日期范围获取版面计划列表
     *
     * @param foldId、areaverId、startTome、endTime
     * @return MediaPagePlanVo
     * @author lhl
     * @date 2023/11/29
     */
    @GetMapping("/getFoldPagePlanList")
    public Json<List<MediaPagePlanVo>> getFoldPagePlanList(String mediaId, String foldId, String areaverId,
                                                           String startTome, String endTime) {
        List<MediaPagePlanVo> mediaPagePlanVoList = scheduleseekServiceI.getFoldPagePlanList(mediaId, foldId,
                areaverId, startTome, endTime);
        return Json.success(mediaPagePlanVoList);
    }

    /**
     * 根据媒体id,叠次id、叠次版本id、日期获取版面列表
     * 方法功能: 根据媒体id,叠次id、叠次版本id、日期获取版面列表
     *
     * @param mediaId，foldId、areaverId、publishTime
     * @return Tbfoldpageplan
     * @author lhl
     * @date 2023/11/30
     */
    @GetMapping("/getPageList")
    public Json<List<PageOrderVO>> getPageList(String mediaId, String foldId, String areaverId, String publishTime) {
        List<PageOrderVO> tbfoldpageplanList = scheduleseekServiceI.getPageList(mediaId, foldId, areaverId,
                publishTime);
        return Json.success(tbfoldpageplanList);
    }

    /**
     * 根据媒体id,叠次id、叠次版本id、日期获取广告订单
     * 方法功能: 根据媒体id,叠次id、叠次版本id、日期获取广告订单
     *
     * @param mediaId，foldId、areaverId、strartTime、endTime
     * @return AdvOrderItemVo
     * @author lhl
     * @date 2023/12/05
     */
    @GetMapping("/getAdvOrderList")
    public Json<List<PageOrderItemVo>> getAdvOrderList(String mediaId, String foldId, String areaverId,
                                                       String strartTime, String endTime) {
        List<PageOrderItemVo> advOrderItemVoList = scheduleseekServiceI.getAdvOrderList(mediaId, foldId, areaverId,
                strartTime, endTime);
        return Json.success(advOrderItemVoList);
    }

    /**
     * 根据媒体ID、时间范围、广告形式获取日广告订单
     * 方法功能: 根据媒体ID、时间范围、广告形式获取日广告订单
     *
     * @param mediaId,startTime,endTime
     * @return AdvOrderItemVo
     * @author lhl
     * @date 2023/12/07
     */
    @GetMapping("/getAdvOrderofDay")
    public Json<List<AdvDisplayFormVo>> getAdvOrderofDay(String mediaId, String foldId, String areaverId,
                                                         String strartTime, String endTime) {
        List<AdvDisplayFormVo> advDisplayFormVoList = scheduleseekServiceI.getAdvOrderofDay(mediaId, foldId,
                areaverId, strartTime, endTime);
        return Json.success(advDisplayFormVoList);
    }

    /**
     * 获取预约、预定的广告订单
     * 方法功能: 获取预约、预定的广告订单
     *
     * @param mediaId,foldId,areaverId,startTime,endTime
     * @return OrderPublishQueryResultVo
     * @author lhl
     * @date 2024/1/12
     */
    @GetMapping("/queryOredrPredetermineList")
    public Json<List<OrderPublishQueryResultVo>> queryOredrPredetermineList(String mediaId, String foldId, String areaverId,
                                                                            String strartTime, String endTime) {
      return Json.success(scheduleseekServiceI.queryOredrPredetermineList(mediaId,foldId,areaverId,strartTime,endTime));
    }

}
