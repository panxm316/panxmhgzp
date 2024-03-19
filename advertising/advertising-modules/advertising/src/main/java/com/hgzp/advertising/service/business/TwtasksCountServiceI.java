package com.hgzp.advertising.service.business;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hgzp.advertising.pagemodel.business.dto.TwtaskMessageDTO;
import com.hgzp.pagemodel.business.TaskCountVo;
import com.hgzp.pagemodel.schedule.OrderPublishQueryResultVo;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * <p>
 * 任务额度汇总统计 服务类
 * </p>
 *
 * @author lhl
 * @since 2023-12-26
 */
public interface TwtasksCountServiceI {
    /**
     * 任务额度部门汇总
     * 方法功能: 任务额度部门汇总
     *
     * @param mediaId,foldId,areaverId,stratTime,endTime
     * @return TaskCountVo
     * @author lhl
     * @date 2023/12/27
     */
    IPage<TaskCountVo> getDepartmentTaskCountList(String mediaId, List<Long> ids, String stratTime, String endTime, int pageNum, int pageSize, String publishstatus,String dateType);

    /**
     * 过滤部门级别
     * 方法功能: 过滤部门级别
     *
     * @param ids
     * @return TaskCountVo,level
     * @author lhl
     * @date 2023/12/26
     */
    List<Long> filterDepartmentLevel(List<Long> ids,String level);

    /**
     * 任务额度人员汇总
     * 方法功能: 任务额度人员汇总
     *
     * @param mediaId,foldId,areaverId,stratTime,endTime
     * @return TaskCountVo
     * @author lhl
     * @date 2023/12/28
     */
    IPage<TaskCountVo> getEmployeTaskCountList(String mediaId, List<Long> ids, String stratTime, String endTime, int pageNum, int pageSize, String publishstatus,String dateType);

    /**
     * 任务额度消息通知
     * 方法功能: 任务额度消息通知
     *
     * @param twtaskMessageDTO
     * @return
     * @author lhl
     * @date 2023/12/29
     */
    boolean sendTwtaskMessage(@RequestBody TwtaskMessageDTO twtaskMessageDTO) throws Exception;

    /**
     * 获取部门负责人
     * 方法功能: 获取部门负责人
     *
     * @param departId
     * @return
     * @author lhl
     * @date 2023/12/29
     */
    Long getDepartLeader(Long departId);

}
