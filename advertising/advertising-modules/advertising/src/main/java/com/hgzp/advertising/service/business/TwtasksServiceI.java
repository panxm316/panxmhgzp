package com.hgzp.advertising.service.business;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hgzp.advertising.pagemodel.business.dto.TaskQuotaAddDTO;
import com.hgzp.advertising.pagemodel.business.dto.TaskQuotaDTO;
import com.hgzp.advertising.pagemodel.business.dto.TaskQuotaUpdateDTO;
import com.hgzp.advertising.pagemodel.business.dto.TwtaskDTO;
import com.hgzp.advertising.pagemodel.business.vo.TaskReportsVO;
import com.hgzp.advertising.pagemodel.personal.dto.MyTaskDTO;
import com.hgzp.core.model.Twtasks;
import com.hgzp.core.page.Json;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 任务额度表 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-10-31
 * modify lhl
 * modify-date 2023-11-09
 */
public interface TwtasksServiceI extends IService<Twtasks> {
    /**
     * 方法功能: 根据查询条件获取任务额度分页列表
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.advertising.pagemodel.business.dto.TaskQuotaDTO>
     * @author lhl
     * @date 2023/11/9
     */
    IPage<TaskQuotaDTO> getTaskReportPageList(Page<Twtasks> page, TaskReportsVO query) throws Exception;

    /**
     * 方法功能: 批量新增配额记录
     *
     * @param taskQuotaAddDTO
     * @return void
     * @return Json
     * @author lhl
     * @date 2023/11/10
     */
    Json saveTaskQuota(TaskQuotaAddDTO taskQuotaAddDTO) throws Exception;

    /**
     * 方法功能: 查询是否存在符合条件的记录
     *
     * @param sTaskdate,mediaId,sPersonalType
     * @return void
     * @author lhl
     * @date 2023/11/11
     */
    long countTaskQuotaWithCondition(String sTaskdate, String mediaIdL, String sPersonalType, String deptIdL, String employIdL) throws Exception;


    /**
     * 方法功能: 修改配额记录
     *
     * @param taskQuotaUpdateDTO
     * @return void
     * @return Json
     * @author lhl
     * @date 2023/11/11
     */
    Json updateTaskQuota(TaskQuotaUpdateDTO taskQuotaUpdateDTO) throws Exception;

    /**
     * 方法功能: 删除配额记录
     *
     * @param taskQuotaUpdateDTO
     * @return void
     * @author lhl
     * @date 2023/11/11
     */
    Json deleteTaskQuota(TaskQuotaUpdateDTO taskQuotaUpdateDTO) throws Exception;

    /**
     * 方法功能: 批量复制
     *
     * @param taskQuotaUpdateDTO
     * @return void
     * @author lhl
     * @date 2023/11/24
     */
    Json batchCopyTaskQuota(TaskQuotaUpdateDTO taskQuotaUpdateDTO) throws Exception;

    /**
     * 方法功能: 返回部门任务额度记录
     *
     * @param departId
     * @return void
     * @author lhl
     * @date 2023/12/27
     */
    Twtasks getTwtasksByDepartId(String departId, String sPersonType);

    /**
     * 方法功能: 返回任务额度记录
     *
     * @param taskid
     * @return Twtasks
     * @author lhl
     * @date 2023/12/29
     */
    TwtaskDTO getTwtasksById(String taskid);

    /**
     * 方法功能: 添加任务额度记录
     *
     * @param twtaskDTO
     * @return Twtasks
     * @author lhl
     * @date 2023/12/29
     */
    public Json addTaskQuota(TwtaskDTO twtaskDTO) throws Exception;

    /**
     * 方法功能: 修改任务额度记录
     *
     * @param twtaskDTO
     * @return
     * @author lhl
     * @date 2023/12/29
     */
    public Json updateTwtask(TwtaskDTO twtaskDTO) throws Exception;

    /**
     * 获取任务完成比例情况
     * 方法功能: 获取任务完成比例情况
     *
     * @param query
     * @return com.hgzp.advertising.pagemodel.personal.dto.MyTaskDTO
     * @author suny
     * @date 2024/1/24 9:53
     */
    List<MyTaskDTO> getMyTaskStat(TaskReportsVO query) throws Exception;

    BigDecimal getDeptTask(String deptids, String mediaids, String taskdate, String sumtype);
}