package com.hgzp.advertising.controller.business;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.pagemodel.business.dto.TaskQuotaDTO;
import com.hgzp.advertising.pagemodel.business.dto.TwtaskDTO;
import com.hgzp.advertising.pagemodel.business.dto.TwtaskMessageDTO;
import com.hgzp.advertising.pagemodel.business.vo.TaskReportsVO;
import com.hgzp.advertising.pagemodel.system.dto.EmpAuthorityDTO;
import com.hgzp.advertising.service.business.TwtasksCountServiceI;
import com.hgzp.core.constant.SecurityConstants;
import com.hgzp.core.model.Twtasks;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import com.hgzp.core.web.BaseController;
import com.hgzp.pagemodel.business.TaskCountVo;
import com.hgzp.pagemodel.schedule.OrderPublishQueryResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * TwtasksCountController
 * 创建人：lhl
 * 类描述：任务额度汇总统计 前端控制器
 * 创建日期：2023/12/26 12:58
 *
 * @folder business/TwtasksCountController
 */
@RestController
@RequestMapping("/business/twtaskscount")
public class TwtasksCountController extends BaseController {
    @Autowired
    TwtasksCountServiceI twtasksCountServiceI;

    /**
     * 任务额度部门汇总
     * 方法功能: 任务额度部门汇总
     *
     * @param mediaId,dateType,stratTime,endTime,pageNum,pageSize,publishstatus,depLevel
     * @return TaskCountVo
     * @author lhl
     * @date 2023/12/27
     */
    @GetMapping("/getDepartmentTaskCountList")
    public Json<List<TaskCountVo>> getDepartmentTaskCountList(String mediaId, String dateType, String stratTime, String endTime, int pageNum, int pageSize, String publishstatus,String depLevel, HttpServletRequest request) {
        EmpAuthorityDTO empAuthorityDTO = (EmpAuthorityDTO)request.getAttribute(SecurityConstants.ROLE_PERMISSION);
        List<Long> deptIdList = empAuthorityDTO.getDeptIdList();
        if( deptIdList.size() <= 0 )
        {
            deptIdList.add(1692806099696226304L);
            deptIdList.add(1692806568527138816L);
        }
        for( int i = 0; i < deptIdList.size(); i++ ) {
            System.out.println(String.valueOf(deptIdList.get(i)));
        }
        if( null != depLevel ) {
            deptIdList = twtasksCountServiceI.filterDepartmentLevel(deptIdList,depLevel);
        }
        IPage<TaskCountVo> list = twtasksCountServiceI.getDepartmentTaskCountList(mediaId,deptIdList,stratTime,endTime,pageNum,pageSize,publishstatus,dateType);
        return Json.success(list);
    }

    /**
     * 任务额度人员汇总
     * 方法功能: 任务额度人员汇总
     *
     * @param mediaId,dateType,stratTime,endTime,pageNum,pageSize,publishstatus,depLevel
     * @return TaskCountVo
     * @author lhl
     * @date 2023/12/28
     */
    @GetMapping("/getEmployeTaskCountList")
    public Json<List<TaskCountVo>> getEmployeTaskCountList(String mediaId, String dateType, String stratTime, String endTime, int pageNum, int pageSize, String publishstatus,String depLevel, HttpServletRequest request) {
        EmpAuthorityDTO empAuthorityDTO = (EmpAuthorityDTO)request.getAttribute(SecurityConstants.ROLE_PERMISSION);
        List<Long> deptIdList = empAuthorityDTO.getDeptIdList();
        if( deptIdList.size() <= 0 )
        {
            deptIdList.add(1692806099696226304L);
            deptIdList.add(1692806568527138816L);
        }
        for( int i = 0; i < deptIdList.size(); i++ ) {
            System.out.println(String.valueOf(deptIdList.get(i)));
        }
        IPage<TaskCountVo> list = twtasksCountServiceI.getEmployeTaskCountList(mediaId,deptIdList,stratTime,endTime,pageNum,pageSize,publishstatus,dateType);
        return Json.success(list);
    }

    /**
     * 发送通知
     * 方法功能: 发送通知
     *
     * @param twtaskMessageDTO
     * @author lhl
     * @date 2023/12/29
     */
    @PostMapping(value = "/sendTwtaskMessage")
    public Json sendTwtaskMessage(@RequestBody TwtaskMessageDTO twtaskMessageDTO) throws Exception {
        boolean bSucess = twtasksCountServiceI.sendTwtaskMessage(twtaskMessageDTO);
        if( bSucess )
            return Json.success("通知发送成功");
        return Json.success("通知发送失败");
    }

}


