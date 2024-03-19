package com.hgzp.advertising.controller.personal;

import com.hgzp.advertising.pagemodel.flow.dto.MytodoDTO;
import com.hgzp.advertising.service.flow.MyToDoServiceI;
import com.hgzp.common.flowable.dto.PageResultDto;
import com.hgzp.common.flowable.dto.TaskDto;
import com.hgzp.core.page.Json;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * MyToDoController
 * 创建人：songly
 * 类描述：我的待办 移动端用
 * 创建日期：2024/3/6 14:55
 *
 * @folder personal/MyToDoController
 */
@RestController
@RequestMapping("/personal/mytodo")
public class MyToDoController extends BaseController {

    @Autowired
    private MyToDoServiceI myToDoService;

    /**
     * 获取我的待办（包括审批流程、快速预约审批、工作报告审批）
     * 方法功能:
     *
     * @param pageDto
     * @return com.hgzp.core.page.Json<com.hgzp.common.flowable.dto.PageResultDto < com.hgzp.common.flowable.dto.TaskDto>>
     * @author songly
     * @date 2024/3/6 15:00
     */
    @PostMapping("getmyTask")
    public Json<PageResultDto<TaskDto>> getMyToDo(@RequestBody MytodoDTO pageDto) {
        try {
            if(pageDto.getLoginUserId() == null){
                    return Json.fail("用户id不能为空");
            }
            return myToDoService.queryMyToDo(pageDto);
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }

    }
}