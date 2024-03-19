package com.hgzp.advertising.service.business.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.pagemodel.business.dto.TaskQuotaAddDTO;
import com.hgzp.advertising.pagemodel.business.dto.TaskQuotaDTO;
import com.hgzp.advertising.pagemodel.business.dto.TaskQuotaUpdateDTO;
import com.hgzp.advertising.pagemodel.business.dto.TwtaskDTO;
import com.hgzp.advertising.pagemodel.business.vo.OrderAndItemVO;
import com.hgzp.advertising.pagemodel.business.vo.TaskReportsVO;
import com.hgzp.advertising.pagemodel.personal.dto.MyTaskDTO;
import com.hgzp.advertising.service.ad.TworderitembelongServiceI;
import com.hgzp.advertising.service.business.TwtasksServiceI;
import com.hgzp.advertising.service.media.TbmediaServiceI;
import com.hgzp.advertising.service.system.TbemployServiceI;
import com.hgzp.core.emnus.ResultDefines;
import com.hgzp.core.model.*;
import com.hgzp.core.page.Json;
import com.hgzp.mapper.business.TwtasksMapper;
import com.hgzp.pagemodel.ad.AdSummaryVO;
import com.hgzp.service.system.BaseTbdeptServiceI;
import com.hgzp.service.system.BaseTbemployServiceI;
import com.hgzp.utils.WebUtil;
import com.hgzp.utils.model.LoginUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 任务额度表 服务实现类
 * </p>
 *
 * @author wwk
 * @modify lhl
 * @modify-date 2023-11-09
 * @since 2023-10-31
 */
@Service
public class TwtasksServiceImpl extends ServiceImpl<TwtasksMapper, Twtasks> implements TwtasksServiceI {

    @Autowired
    TwtasksMapper twtasksMapper;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;
    @Autowired
    BaseTbdeptServiceI baseTbdeptServiceI;
    @Autowired
    BaseTbemployServiceI baseTbemployServiceI;
    @Autowired
    TbmediaServiceI tbmediaServiceI;
    @Autowired
    TworderitembelongServiceI tworderitembelongServiceI;
    @Autowired
    TbemployServiceI tbemployServiceI;


    @Override
    public IPage<TaskQuotaDTO> getTaskReportPageList(Page<Twtasks> page, TaskReportsVO query) throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        HttpServletRequest request = WebUtil.getHttpServletRequest();
        LambdaQueryWrapper<Twtasks> lqw = Wrappers.lambdaQuery();
        // 任务额度类型 “0”：部门  “1”：人员
        lqw.eq(null != query.getSTaskType(), Twtasks::getSpersonaltype, query.getSTaskType());
        // 部门或人员名称筛选
        if (null != query.getQueryKey() && !query.getQueryKey().isEmpty()) {
            if ("0".equals(query.getSTaskType())) {
                lqw.like(null != query.getQueryKey(), Twtasks::getDeptname, query.getQueryKey());
            } else {
                lqw.like(null != query.getQueryKey(), Twtasks::getEmployname, query.getQueryKey());
            }
        }
        if (!"-1".equals(query.getSTaskCategory())) {
            // 任务额度类别 “0”：年度  “1”：月度
            lqw.eq(null != query.getSTaskCategory(), Twtasks::getStasktype, query.getSTaskCategory());
        }
        if (null != query.getMedidId()) {
            lqw.eq(null != query.getMedidId(), Twtasks::getMediaid, query.getMedidId());
        }
        // 日期范围筛选
        lqw.between(Twtasks::getDcreatetime, query.getStartTime(), query.getEndTime());
        // 排序
        if( query.getSortType() == 0 ) {
            lqw.orderByDesc(Twtasks::getDcreatetime);
        } else if( query.getSortType() == 1  ) {
            if( query.getSortRule() == 0 )
                lqw.orderByAsc(Twtasks::getDeptname);
            else
                lqw.orderByDesc(Twtasks::getDeptname);
        } else if( query.getSortType() == 1 ) {
            if( query.getSortRule() == 0 )
                lqw.orderByAsc(Twtasks::getEmployname);
            else
                lqw.orderByDesc(Twtasks::getEmployname);
        } else if( query.getSortType() == 2 ) {
            if( query.getSortRule() == 0 )
                lqw.orderByAsc(Twtasks::getMedianame);
            else
                lqw.orderByDesc(Twtasks::getMedianame);
        }
        IPage<Twtasks> pages = twtasksMapper.selectPage(page, lqw);
        IPage<TaskQuotaDTO> resulepage = new Page<>();
        List<TaskQuotaDTO> result = new ArrayList<>();
        for (Twtasks record : pages.getRecords()) {
            TaskQuotaDTO taskQuotaDTO = new TaskQuotaDTO(record);
            result.add(taskQuotaDTO);
        }
        resulepage.setRecords(result);
        resulepage.setTotal(pages.getTotal());
        return resulepage;
    }


    @Override
    public Json saveTaskQuota(TaskQuotaAddDTO taskQuotaAddDTO) throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        // 记录日志
        innerInterceptor.recoredLog();
        Integer nStartYear = Integer.valueOf(taskQuotaAddDTO.getSStartYear());
        Integer nEndYear = Integer.valueOf(taskQuotaAddDTO.getSEndYear());
        // 判断起始年度、终止年度由字符串转换为整数后是否符合要求F
        if (null == nStartYear || null == nEndYear || nStartYear > nEndYear) {
            return Json.fail(ResultDefines.IllegalArgument);
        }
        // 判断部门或人员列表是否为空
        List<Long> idList = taskQuotaAddDTO.getIdList();
        if (null == idList || idList.size() <= 0) {
            return Json.fail(ResultDefines.IllegalArgument);
        }
        String sMessge = "任务额度记录添加成功\n";
        for (int year = nStartYear; year <= nEndYear; year++) {
            // 年度任务
            if ("0".equals(taskQuotaAddDTO.getSCategoryType())) {
                for (int i = 0; i < idList.size(); i++) {
                    Twtasks twtasks = new Twtasks();
                    // 默认启用（无需配置）
                    twtasks.setBuse(true);
                    twtasks.setOperatorid(user.getUserid());
                    twtasks.setOperatorname(user.getUsername());
                    twtasks.setSpersonaltype(taskQuotaAddDTO.getSTaskType());
                    twtasks.setStasktype(taskQuotaAddDTO.getSCategoryType());
                    twtasks.setNtaskamount(taskQuotaAddDTO.getDQuota());
                    twtasks.setStaskdate(String.format("%d", year));
                    twtasks.setDcreatetime(DateUtil.parseDate(String.format("%d-01-01", year)));
                    twtasks.setSremark(taskQuotaAddDTO.getRemarks());
                    if (null != taskQuotaAddDTO.getMediaId() && !taskQuotaAddDTO.getMediaId().isEmpty()) {
                        twtasks.setMediaid(Long.valueOf(taskQuotaAddDTO.getMediaId()));
                        Tbmedia tbmedia = tbmediaServiceI.getById(taskQuotaAddDTO.getMediaId());
                        if (null != tbmedia) {
                            twtasks.setMediaid(tbmedia.getId());
                            twtasks.setMedianame(tbmedia.getSname());
                        } else {
                            //twtasks.setMediaid(-1L);
                            twtasks.setMedianame("");

                        }
                    } else {
                        //twtasks.setMediaid(-1L);
                        twtasks.setMedianame("");
                    }

                    // 部门任务额度
                    if ("0".equals(taskQuotaAddDTO.getSTaskType())) {
                        Tbdept tbdept = baseTbdeptServiceI.getById(idList.get(i));
                        if (null == tbdept) {
                            sMessge += String.format("添加部门任务额度失败,部门ID: %s\n", idList.get(i));
                            continue;
                        }
                        twtasks.setDeptid(tbdept.getId());
                        twtasks.setDeptname(tbdept.getSdeptname());
                        // -1表示id为空
                        //twtasks.setEmployid(-1L);
                        twtasks.setEmployname("");

                    } else { // 人员任务额度
                        Tbemploy tbemploy = baseTbemployServiceI.getById(idList.get(i));
                        if (null == tbemploy) {
                            sMessge += String.format("添加人员任务额度失败,人员ID: %s\n", idList.get(i));
                            continue;
                        }
                        twtasks.setEmployid(tbemploy.getId());
                        twtasks.setEmployname(tbemploy.getSusername());
                        if (null != tbemploy.getDeptid()) {
                            Tbdept tbdept = baseTbdeptServiceI.getById(tbemploy.getDeptid());
                            if (null != tbdept) {
                                twtasks.setDeptid(tbdept.getId());
                                twtasks.setDeptname(tbdept.getSdeptname());
                            } else {
                                twtasks.setDeptid(-1L);
                                twtasks.setDeptname("");
                            }
                        } else {
                            twtasks.setDeptid(-1L);
                            twtasks.setDeptname("");

                        }
                    }
                    long count = countTaskQuotaWithCondition(twtasks.getStaskdate(), twtasks.getMediaid() != null ? String.valueOf(twtasks.getMediaid()) : null, twtasks.getSpersonaltype(), twtasks.getDeptid() != null ? String.valueOf(twtasks.getDeptid()) : null, twtasks.getEmployid() != null ? String.valueOf(twtasks.getEmployid()) : null);
                    // 若无重复记录，则插入记录
                    if (count <= 0) {
                        twtasksMapper.insert(twtasks);
                    } else {
                        sMessge += String.format("任务额度重复---任务配额日期: %s; 任务额度类型: %s; 名称: %s; 媒体: %s \n", twtasks.getStaskdate(), ("0".equals(twtasks.getSpersonaltype())) ? "部门" : "人员", ("0".equals(twtasks.getSpersonaltype())) ? twtasks.getDeptname() : twtasks.getEmployname(), twtasks.getMedianame());
                    }
                }

            } else {
                // 月度任务
                for (int month = 1; month <= 12; month++) {
                    for (int i = 0; i < idList.size(); i++) {
                        Twtasks twtasks = new Twtasks();
                        // 默认启用（无需配置）
                        twtasks.setBuse(true);
                        twtasks.setOperatorid(user.getUserid());
                        twtasks.setOperatorname(user.getUsername());
                        twtasks.setSpersonaltype(taskQuotaAddDTO.getSTaskType());
                        twtasks.setStasktype(taskQuotaAddDTO.getSCategoryType());
                        twtasks.setNtaskamount(taskQuotaAddDTO.getDQuota());
                        twtasks.setStaskdate(String.format("%d-%02d", year, month));
                        twtasks.setDcreatetime(DateUtil.parseDate(String.format("%d-%02d-01", year, month)));
                        twtasks.setSremark(taskQuotaAddDTO.getRemarks());
                        if (null != taskQuotaAddDTO.getMediaId() && !taskQuotaAddDTO.getMediaId().isEmpty()) {
                            twtasks.setMediaid(Long.valueOf(taskQuotaAddDTO.getMediaId()));
                            Tbmedia tbmedia = tbmediaServiceI.getById(taskQuotaAddDTO.getMediaId());
                            if (null != tbmedia) {
                                twtasks.setMedianame(tbmedia.getSname());
                            }
                        } else {
                            // -1表示id为空
                            //twtasks.setMediaid(-1L);
                            twtasks.setMedianame("");
                        }

                        // 部门任务额度
                        if ("0".equals(taskQuotaAddDTO.getSTaskType())) {
                            Tbdept tbdept = baseTbdeptServiceI.getById(idList.get(i));
                            if (null == tbdept) {
                                sMessge += String.format("添加部门任务额度失败,部门ID: %s\n", idList.get(i));
                                continue;
                            }
                            twtasks.setDeptid(tbdept.getId());
                            twtasks.setDeptname(tbdept.getSdeptname());
                            //twtasks.setEmployid(-1L);
                            twtasks.setEmployname("");

                        } else {// 人员任务额度
                            Tbemploy tbemploy = baseTbemployServiceI.getById(idList.get(i));
                            if (null == tbemploy) {
                                sMessge += String.format("添加人员任务额度失败,人员ID: %s\n", idList.get(i));
                                continue;
                            }
                            twtasks.setEmployid(tbemploy.getId());
                            twtasks.setEmployname(tbemploy.getSusername());
                            if (null != tbemploy.getDeptid()) {
                                Tbdept tbdept = baseTbdeptServiceI.getById(tbemploy.getDeptid());
                                if (null != tbdept) {
                                    twtasks.setDeptid(tbdept.getId());
                                    twtasks.setDeptname(tbdept.getSdeptname());
                                } else {
                                    // -1表示id为空
                                    twtasks.setDeptid(-1L);
                                    twtasks.setDeptname("");
                                }
                            } else {
                                twtasks.setDeptid(-1L);
                                twtasks.setDeptname("");

                            }
                        }
                        long count = countTaskQuotaWithCondition(twtasks.getStaskdate(), twtasks.getMediaid() != null ? String.valueOf(twtasks.getMediaid()) : null, twtasks.getSpersonaltype(), twtasks.getDeptid() != null ? String.valueOf(twtasks.getDeptid()) : null, twtasks.getEmployid() != null ? String.valueOf(twtasks.getEmployid()) : null);
                        // 若无重复记录，则插入记录
                        if (count <= 0) {
                            twtasksMapper.insert(twtasks);
                        } else {
                            sMessge += String.format("任务额度重复---任务配额日期: %s; 任务额度类型: %s; 名称: %s; 媒体: %s \n", twtasks.getStaskdate(), ("0".equals(twtasks.getSpersonaltype())) ? "部门" : "人员", ("0".equals(twtasks.getSpersonaltype())) ? twtasks.getDeptname() : twtasks.getEmployname(), twtasks.getMedianame());

                        }

                    }
                }
            }
        }
        return Json.success(sMessge);
    }

    @Override
    public long countTaskQuotaWithCondition(String sTaskdate, String mediaIdL, String sPersonalType, String deptIdL, String employIdL) throws Exception {

        LambdaQueryWrapper<Twtasks> lqw = Wrappers.lambdaQuery();
        // 任务额度日期
        lqw.eq(sTaskdate != null, Twtasks::getStaskdate, sTaskdate);
        // 媒体ID
        lqw.eq(mediaIdL != null, Twtasks::getMediaid, mediaIdL);
        // 任务额度类型 0：部门  1：人员
        lqw.eq(sPersonalType != null, Twtasks::getSpersonaltype, sPersonalType);
        if ("0".equals(sPersonalType))
            lqw.eq(deptIdL != null, Twtasks::getDeptid, deptIdL);
        else
            lqw.eq(employIdL != null, Twtasks::getEmployid, employIdL);
        // 返回符合查询条件的记录数
        return twtasksMapper.selectCount(lqw);
    }

    @Override
    public Json updateTaskQuota(TaskQuotaUpdateDTO taskQuotaUpdateDTO) throws Exception {
        innerInterceptor.recoredLog();
        // 判断ID列表是否为空
        List<Long> idList = taskQuotaUpdateDTO.getIdList();
        if (null == idList || idList.size() <= 0) {
            return Json.fail(ResultDefines.IllegalArgument);
        }
        String sMessge = "任务额度记录更新成功\n";
        for (int i = 0; i < idList.size(); i++) {
            Twtasks twtasks = getById(idList.get(i));
            if (null == twtasks) {
                sMessge += String.format("配额记录更新失败,配额记录(ID:%d)不存在\n", idList.get(i));
                continue;
            }
            twtasks.setNtaskamount(taskQuotaUpdateDTO.getDQuota());
            twtasks.setSremark(taskQuotaUpdateDTO.getSremark());
            twtasksMapper.updateById(twtasks);

        }
        return Json.success(sMessge);
    }

    @Override
    public Json deleteTaskQuota(TaskQuotaUpdateDTO taskQuotaUpdateDTO) throws Exception {
        innerInterceptor.recoredLog();

        // 判断ID列表是否为空
        List<Long> idList = taskQuotaUpdateDTO.getIdList();
        if (null == idList || idList.size() <= 0) {
            return Json.fail(ResultDefines.IllegalArgument);
        }
        String sMessge = "任务额度记录删除成功\n";
        for (int i = 0; i < idList.size(); i++) {
            Twtasks twtasks = getById(idList.get(i));
            if (null == twtasks) {
                sMessge += String.format("任务额度记录删除失败,额度记录(ID:%d)不存在\n", idList.get(i));
                continue;
            }
            twtasksMapper.deleteById(twtasks.getId());
        }
        return Json.success(sMessge);
    }

    @Override
    public Json batchCopyTaskQuota(TaskQuotaUpdateDTO taskQuotaUpdateDTO) throws Exception {
        innerInterceptor.recoredLog();
        // 判断ID列表是否为空
        List<Long> idList = taskQuotaUpdateDTO.getIdList();
        if (null == idList || idList.size() <= 0) {
            return Json.fail(ResultDefines.IllegalArgument);
        }
        String sMessge = "任务额度批量复制成功\n";
        for (int i = 0; i < idList.size(); i++) {
            Twtasks twtasks = getById(idList.get(i));
            if (null == twtasks) {
                sMessge += String.format("任务额度记录复制失败,额度记录(ID:%d)不存在\n", idList.get(i));
                continue;
            }
            Twtasks newtwtasks = new Twtasks();
            newtwtasks.setBuse(true);
            newtwtasks.setOperatorid(twtasks.getOperatorid());
            newtwtasks.setOperatorname(twtasks.getOperatorname());
            newtwtasks.setSpersonaltype(twtasks.getSpersonaltype());
            newtwtasks.setStasktype(twtasks.getStasktype());
            newtwtasks.setNtaskamount(twtasks.getNtaskamount());
            newtwtasks.setStaskdate(taskQuotaUpdateDTO.getSremark() + twtasks.getStaskdate().substring(4));
            if ("0".equals(newtwtasks.getStasktype())) {
                newtwtasks.setDcreatetime(DateUtil.parseDate(String.format("%s-01-01", taskQuotaUpdateDTO.getSremark())));
            } else {
                newtwtasks.setDcreatetime(DateUtil.parseDate(String.format("%s-01", newtwtasks.getStaskdate())));
            }
            newtwtasks.setSremark(twtasks.getSremark());
            if (null != twtasks.getEmployname()) {
                newtwtasks.setEmployname(twtasks.getEmployname());
            }
            if (null != twtasks.getEmployid()) {
                newtwtasks.setEmployid(twtasks.getEmployid());
            }
            if (null != twtasks.getDeptname()) {
                newtwtasks.setDeptname(twtasks.getDeptname());
            }
            if (null != twtasks.getDeptid()) {
                newtwtasks.setDeptid(twtasks.getDeptid());
            } else {
                newtwtasks.setDeptid(-1L);
            }
            if (null != twtasks.getMediaid()) {
                newtwtasks.setMediaid(twtasks.getMediaid());
            }
            long count = countTaskQuotaWithCondition(newtwtasks.getStaskdate(), newtwtasks.getMediaid() != null ? String.valueOf(twtasks.getMediaid()) : null, newtwtasks.getSpersonaltype(), newtwtasks.getDeptid() != null ? String.valueOf(twtasks.getDeptid()) : null, newtwtasks.getEmployid() != null ? String.valueOf(twtasks.getEmployid()) : null);
            // 若无重复记录，则插入记录
            if (count <= 0) {
                twtasksMapper.insert(newtwtasks);
            } else {
                sMessge += String.format("任务额度重复---任务额度日期: %s; 任务额度类型: %s; 名称: %s; 媒体: %s \n", twtasks.getStaskdate(), ("0".equals(twtasks.getSpersonaltype())) ? "部门" : "人员", ("0".equals(twtasks.getSpersonaltype())) ? twtasks.getDeptname() : twtasks.getEmployname(), twtasks.getMedianame());
            }

        }
        return Json.success(sMessge);

    }

    @Override
    public Twtasks getTwtasksByDepartId(String departId, String sPersonType) {
        LambdaQueryWrapper<Twtasks> lqw = Wrappers.lambdaQuery();
        if (sPersonType.equals("0"))
            lqw.eq(null != departId, Twtasks::getDeptid, departId);
        else
            lqw.eq(null != departId, Twtasks::getEmployid, departId);
        lqw.eq(null != sPersonType, Twtasks::getSpersonaltype, sPersonType);
        List<Twtasks> list = twtasksMapper.selectList(lqw);
        if (list.size() > 0)
            return list.get(0);
        return null;
    }

    @Override
    public TwtaskDTO getTwtasksById(String taskid) {
        TwtaskDTO twtaskDTO = new TwtaskDTO();
        Twtasks twtasks = twtasksMapper.selectById(taskid);
        BeanUtils.copyProperties(twtasks, twtaskDTO);
        return twtaskDTO;
    }

    @Override
    public Json addTaskQuota(TwtaskDTO twtaskDTO) throws Exception {
        String sMessge = "任务额度添加成功\n";
        LoginUser user = WebUtil.getLoginUser();
        Long id = IdUtil.getSnowflakeNextId();
        twtaskDTO.setId(id);
        twtaskDTO.setOperatorid(user.getUserid());
        twtaskDTO.setOperatorname(user.getUsername());
        Twtasks twtasks = new Twtasks();

        BeanUtils.copyProperties(twtaskDTO, twtasks);
        twtasks.setDcreatetime(new Date());
        twtasks.setBuse(true);
        innerInterceptor.recoredLog();
        if (twtasksMapper.insert(twtasks) <= 0)
            sMessge = "任务额度添加失败\n";
        return Json.success(sMessge);
    }

    @Override
    public Json updateTwtask(TwtaskDTO twtaskDTO) throws Exception {
        String sMessge = "任务额度更新成功\n";
        LoginUser user = WebUtil.getLoginUser();
        Long id = twtaskDTO.getId();
        Twtasks twtasks = new Twtasks();
        BeanUtils.copyProperties(twtaskDTO, twtasks);
        innerInterceptor.recoredLog();
        if (twtasksMapper.updateById(twtasks) <= 0)
            sMessge = "任务额度更新失败\n";
        return Json.success(sMessge);

    }

    @Override
    public List<MyTaskDTO> getMyTaskStat(TaskReportsVO query) throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        List<MyTaskDTO> myTaskDTOList = new ArrayList<>();
        List<Tbemploy> tbemployList = new ArrayList<>();
        String sMessge = "";
        //如果只查当前人员时，部门条件不生效
        if (query.getBcurflag() != null && query.getBcurflag()) {
            query.setEmployid(user.getUserid());
            query.setDeptid(null);
        } else {
            if (ObjectUtil.isNotEmpty(query.getEmployid())) {
                // 人员id不为空时，部门条件不生效
                query.setDeptid(null);
            } else {
                if (query.getBdeptflag() != null && query.getBdeptflag()) {
                    // 部门id不为空时，查询当前部门下所有人员
                    query.setEmployid(null);
                    tbemployList = tbemployServiceI.getEmpsListByDeptIds(Arrays.stream(user.getAuthedDeptIds().split(",")).map(Long::parseLong).collect(Collectors.toList()), null, null);
                } else {
                    // 部门id不为空时，查询当前部门下所有人员
                    query.setDeptid(user.getDeptid());
                }
            }
        }
        int icount = 1;
        if (tbemployList.size() > 0) {
            icount = tbemployList.size();
        }
        for (int j = 0; j < icount; j++) {
            if (tbemployList.size() > 0) {
                query.setEmployid(tbemployList.get(j).getId());
                query.setDeptid(null);
            }

            OrderAndItemVO orderAndItemVO = new OrderAndItemVO();
            orderAndItemVO.setStartTime(query.getStartTime());
            orderAndItemVO.setEndTime(query.getEndTime());
            orderAndItemVO.setDeptid(query.getDeptid());
            orderAndItemVO.setEmployid(query.getEmployid());
            orderAndItemVO.setBcurflag(query.getBcurflag());
            BigDecimal sumCount = BigDecimal.ZERO;
            Tworderitem tworderitem = tworderitembelongServiceI.getMyAchievementCount(orderAndItemVO);
            if (tworderitem != null) {
                // 20240227 用户要求是签订额度，不是业绩数据
                sumCount = sumCount.add(tworderitem.getAmountreceivable());
            }

            Twtasks twtasks = this.lambdaQuery()
                    .and(i -> i.eq(query.getEmployid() != null, Twtasks::getEmployid, query.getEmployid())
                            .or()
                            .isNull(query.getEmployid() == null, Twtasks::getEmployid))
                    .eq(query.getStaskdate() != null, Twtasks::getStaskdate, query.getStaskdate())
                    .and(i -> i.eq(StringUtils.isNotEmpty(query.getMedidId()), Twtasks::getMediaid, query.getMedidId())
                            .or()
                            .isNull(StringUtils.isEmpty(query.getMedidId()), Twtasks::getMediaid))
                    .eq(query.getDeptid() != null, Twtasks::getDeptid, query.getDeptid())
                    .one();
            if (twtasks == null) {
                sMessge += (query.getStaskdate() + " \n");
                continue;
            }
            BigDecimal taskcount = twtasks.getNtaskamount().multiply(new BigDecimal(10000));
            BigDecimal finish = sumCount.divide(taskcount, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
            MyTaskDTO myTaskDTO = new MyTaskDTO();
            myTaskDTO.setAmountreceivable(sumCount);
            myTaskDTO.setNtaskamount(twtasks.getNtaskamount());
            myTaskDTO.setNfinishrate(finish);
            myTaskDTO.setSpersonaltype(twtasks.getSpersonaltype());
            myTaskDTO.setStaskdate(twtasks.getStaskdate());
            myTaskDTO.setStasktype(twtasks.getStasktype());
            myTaskDTO.setDeptid(twtasks.getDeptid());
            myTaskDTO.setDeptname(twtasks.getDeptname());
            myTaskDTO.setEmployid(twtasks.getEmployid());
            myTaskDTO.setEmployname(twtasks.getEmployname());

            myTaskDTOList.add(myTaskDTO);
        }
        return myTaskDTOList;
    }

    @Override
    public BigDecimal getDeptTask(String deptids,String mediaids,String taskdate,String sumtype) {
        List<String> depidsList = new ArrayList<String>();
        List<String> mediaList = new ArrayList<String>();
        if( null != deptids ) {
            String[] deptIdArray = deptids.split(",");
            for (int i = 0; i < deptIdArray.length; i++) {
                depidsList.add(deptIdArray[i]);
            }
        }
        if( null != mediaids ) {
            String[] deptIdArray = mediaids.split(",");
            for (int i = 0; i < deptIdArray.length; i++) {
                mediaList.add(deptIdArray[i]);
            }
        }
        AdSummaryVO vo = twtasksMapper.summaryDeptTask(depidsList,mediaList,taskdate,sumtype);
        if( null != vo ) {
            return vo.getAmountreceivable();
        }
        return BigDecimal.valueOf(0.0);
    }
}