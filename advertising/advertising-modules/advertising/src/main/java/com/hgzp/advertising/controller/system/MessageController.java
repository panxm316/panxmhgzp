package com.hgzp.advertising.controller.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.pagemodel.system.vo.MessageVo;
import com.hgzp.advertising.service.system.TwmessageServiceI;
import com.hgzp.common.flowable.dto.MessageDto;
import com.hgzp.core.model.Twmessage;
import com.hgzp.core.model.Tworder;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import com.hgzp.core.page.WebSocketMessage;
import com.hgzp.core.web.BaseController;
import com.hgzp.pagemodel.system.MessageCountMapperVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * MessageController
 * 创建人：peij
 * 类描述：TODO
 * 创建日期：2023/11/10 12:27
 *
 * @folder system/MessageController
 */
@Validated
@RestController
@RequestMapping("/system/message")
public class MessageController extends BaseController {
    @Autowired
    private TwmessageServiceI messageService;

    /**
     * 更新消息状态
     * 方法功能: 更新消息状态
     *
     * @param messageVo
     * @return com.hgzp.core.page.Json
     * @author peij
     * @date 2023/11/16 15:49
     */
    @PostMapping("/updateMessageStatus")
    public Json updateMessageStatus(@RequestBody MessageVo messageVo) {
        return messageService.read(messageVo);
    }

    /**
     * 获取人员未读消息
     * 方法功能: 获取人员未读消息
     *
     * @param receiveEmpId
     * @return com.hgzp.core.page.Json
     * @author peij
     * @date 2023/11/16 15:49
     */
    @GetMapping("/getMessageCountByEmpId")
    public Json getMessageCountByEmpId(@NotBlank(message = "用户id不能为空") String receiveEmpId) {
        WebSocketMessage messageCount = messageService.getMessageCountByEmpId(receiveEmpId);
        return Json.success(messageCount.getMessageCount());
    }

    /**
     * 获取消息通知分页数据
     * 方法功能:
     *
     * @param pageVo
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2024/1/8 15:54
     */
    @GetMapping("/getMessagePageList")
    public Json getMessagePageList(PageRequest pageRequest, MessageVo pageVo) {
        try {
            Page<Twmessage> page = getPage(pageRequest);
            return messageService.queryList(page, pageVo);
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }
    }

    /**
     * 删除消息
     * 方法功能:
     *
     * @param ids
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2024/1/9 15:24
     */
    @PostMapping("/deleteMessageInfoById")
    public Json deleteMessageInfoById(@NotNull(message = "ID不可为空") String ids) {
        try {
            return messageService.delete(ids);
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }
    }

    /**
     * 根据Id标记已读
     * 方法功能:
     *
     * @param ids
     * @return com.hgzp.core.page.Json
     * @author hgsongly
     * @date 2024/1/10 14:27
     */
    @PostMapping("/updateMessageStatusById")
    public Json updateMessageStatusById(@NotNull(message = "ID不可为空") String ids) {
        try {
            return messageService.readById(ids);
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }
    }
}
