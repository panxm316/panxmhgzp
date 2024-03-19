package com.hgzp.advertising.service.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hgzp.advertising.pagemodel.system.vo.MessageVo;
import com.hgzp.common.flowable.dto.third.MessageDto;
import com.hgzp.core.model.Twmessage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.WebSocketMessage;
import com.hgzp.pagemodel.system.MessageCountMapperVo;

import java.util.List;

/**
 * <p>
 * 消息表 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-10-25
 */
public interface TwmessageServiceI extends IService<Twmessage> {

    /**
     * 查询未读数量
     * @return
     */
    Json<Long> queryUnreadNum() throws Exception;

    /**
     * 保存消息
     * @param messageDto
     * @return
     */
    Json saveMessage(MessageDto messageDto);

    /**
     * 查询列表
     * @param pageVo
     * @return
     */
    Json queryList(IPage<Twmessage> page, MessageVo pageVo) throws Exception;

    /**
     * 删除消息
     * @param ids
     * @return
     */
    Json delete(String ids );

    /**
     * 置为已读
     * @param messageVo
     * @return
     */
    Json read(MessageVo messageVo );

    /**
     * 获取消息提醒
     * @return
     */
    List<MessageCountMapperVo> getMessageCount();

    /**
     * 获取用户消息提醒
     * @return
     */
    WebSocketMessage getMessageCountByEmpId(String receiveEmpId);
    /**
     * 标记已读
     * 方法功能:
     * @author hgsongly
     * @date 2024/1/10 14:15
     * @param ids
     * @return com.hgzp.core.page.Json
     */
    Json readById(String ids );
}
