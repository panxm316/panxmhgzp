package com.hgzp.advertising.service.schedule;


import com.hgzp.advertising.pagemodel.schedule.vo.AdvDisplayFormVo;
import com.hgzp.advertising.pagemodel.schedule.vo.AdvOrderItemVo;
import com.hgzp.advertising.pagemodel.schedule.vo.MediaPagePlanVo;
import com.hgzp.advertising.pagemodel.schedule.vo.PageOrderVO;
import com.hgzp.core.model.Tbfoldpageplan;
import com.hgzp.core.model.Tworderitem;
import com.hgzp.core.page.DataCombo;
import com.hgzp.core.page.Json;
import com.hgzp.pagemodel.schedule.OrderPublishQueryResultVo;
import com.hgzp.pagemodel.schedule.PageOrderItemVo;

import java.util.List;

/**
 * ScheduleseekServicel
 * 创建人：LHL
 * 类描述：排期查看服务类
 * 创建日期：2023/11/27 9:39
 */
public interface ScheduleseekServiceI {
    /**
     * 方法功能: 根据叠次ID获取叠次版本下拉框
     *
     * @param foldid
     * @return java.util.List<com.hgzp.core.page.DataCombo>
     * @author lhl
     * @date 2023/11/29
     */
    List<DataCombo> getFoldAreaverCombo(String foldid);

    /**
     * 方法功能: 根据叠次ID、叠次版本ID、日期范围获取刊期计划
     *
     * @param foldId,areaverId,startTome,endTime
     * @return java.util.List<com.hgzp.core.page.DataCombo>
     * @author lhl
     * @date 2023/11/29
     */

    List<MediaPagePlanVo> getFoldPagePlanList(String mediaId, String foldId, String areaverId, String startTime,
                                              String endTime);

    /**
     * 方法功能: 根据媒体id,叠次id、叠次版本id、日期获取版面列表
     *
     * @param mediaId，foldId、areaverId、startTome、endTime
     * @return PageOrderVO
     * @author lhl
     * @date 2023/11/30
     */

    List<PageOrderVO> getPageList(String mediaId, String foldId, String areaverId, String publishTime);

    /**
     * 方法功能: 根据媒体id,叠次id、叠次版本id、日期获取广告订单
     *
     * @param mediaId，foldId、areaverId、startTome、endTime
     * @return Tworderitem
     * @author lhl
     * @date 2023/12/05
     */
    List<PageOrderItemVo> getAdvOrderList(String mediaId, String foldId, String areaverId, String startTime,
                                          String endTime);

    /**
     * 方法功能: 根据媒体ID、时间范围、广告形式获取日广告订单
     *
     * @param mediaId，startTome、endTime
     * @return Tworderitem
     * @author lhl
     * @date 2023/12/07
     */

    public List<AdvDisplayFormVo> getAdvOrderofDay(String mediaId, String foldId, String areaverId, String strartTime
            , String endTime);

    /**
     * 获取预约、预定的广告订单
     * 方法功能: 获取预约、预定的广告订单
     *
     * @param mediaId,foldId,areaverId,startTime,endTime
     * @return OrderPublishQueryResultVo
     * @author lhl
     * @date 2024/1/12
     */
    List<OrderPublishQueryResultVo> queryOredrPredetermineList(String mediaId, String foldId, String areaverId,
                                                                     String strartTime, String endTime);
}
