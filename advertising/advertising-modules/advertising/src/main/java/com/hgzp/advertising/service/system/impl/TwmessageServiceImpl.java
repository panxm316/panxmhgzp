package com.hgzp.advertising.service.system.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgzp.advertising.pagemodel.ad.dto.OrderDTO;
import com.hgzp.advertising.pagemodel.ad.vo.OrderItemVO;
import com.hgzp.advertising.pagemodel.system.vo.MessageDataVo;
import com.hgzp.advertising.pagemodel.system.vo.MessageVo;
import com.hgzp.advertising.service.flow.IProcessInstanceRecordService;
import com.hgzp.advertising.service.system.TwmessageServiceI;
import com.hgzp.configuration.WebSocket;
import com.hgzp.core.emnus.MessageTypeEnum;
import com.hgzp.common.flowable.dto.third.MessageDto;
import com.hgzp.common.flowable.dto.third.UserDto;
import com.hgzp.common.flowable.factory.ApiStrategyFactory;
import com.hgzp.core.model.*;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import com.hgzp.core.page.WebSocketMessage;
import com.hgzp.mapper.system.TwmessageMapper;
import com.hgzp.pagemodel.system.MessageCountMapperVo;
import com.hgzp.utils.WebUtil;
import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 消息表 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-10-25
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TwmessageServiceImpl extends ServiceImpl<TwmessageMapper, Twmessage> implements TwmessageServiceI {

    @Autowired
    WebSocket socket;
    @Resource
    private IProcessInstanceRecordService processInstanceRecordService;
    @Autowired
    private TwmessageMapper messageMapper;

    /**
     * 查询未读数量
     *
     * @return
     */
    @Override
    public Json<Long> queryUnreadNum() throws Exception {
        String userId = WebUtil.getLoginUser().getUserid().toString();
        Long num = this.lambdaQuery().eq(Twmessage::getBreaded, false)
                .eq(Twmessage::getReceiveempid, userId).count();
        return Json.success(num);
    }

    /**
     * 保存消息
     *
     * @param messageDto
     * @return
     */
    @Override
    public Json saveMessage(MessageDto messageDto) {
        Twmessage message = new Twmessage();
        message.setId(IdUtil.getSnowflakeNextId());
        message.setStitle(messageDto.getTitle());
        message.setScontent(messageDto.getContent());
        message.setSparam(messageDto.getParam());
        message.setStype(messageDto.getType());
        message.setDcreatetime(new Date());
        message.setCreateempid(messageDto.getCreateempid());
        message.setReceiveempid(Long.parseLong(messageDto.getUserId()));
        message.setBreaded(false);
        message.setBdeleted(false);
        message.setSflowid(messageDto.getFlowId());
        message.setSprocessinstanceid(messageDto.getProcessInstanceId());
        message.setDprocessinstancecreate(messageDto.getProcessInstanceCreate());
        message.setSuniqueid(messageDto.getUniqueId());

        if (StrUtil.equals(message.getStype(), MessageTypeEnum.TODO_TASK.getType())) {
            ProcessInstanceRecord processInstanceRecord =
                    processInstanceRecordService.lambdaQuery().eq(ProcessInstanceRecord::getProcessInstanceId,
                            message.getSprocessinstanceid()).one();

            String userId = processInstanceRecord.getUserId();
            UserDto user = ApiStrategyFactory.getStrategy().getUser(userId);
            //待办
            message.setStitle("您有一条待办任务");
            message.setScontent(StrUtil.format("[\uD83D\uDE38{}]提交的任务[\uD83D\uDC8C{}]需要您来处理，请及时查看", user.getName(),
                    processInstanceRecord.getName()));
        }

        this.save(message);

        //发消息
        try {
            sendWebSocketMsg(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Json.success();
    }

    /**
     * sendWebSocketMsg
     * 方法功能: 发送websocket消息
     *
     * @param
     * @return void
     * @author wangwk
     * @date 2023/10/25 13:52
     */
    public void sendWebSocketMsg(Twmessage message) {
        WebSocketMessage webSocketMessage = new WebSocketMessage();
        webSocketMessage.setMessageId(message.getId() + "");
        webSocketMessage.setFromUser(message.getCreateempid() + "");
        webSocketMessage.setToUser(message.getReceiveempid() + "");
        webSocketMessage.setType(message.getStype().equals("TodoTask") ?
                WebSocketMessage.FLOW_TodoTask_MSG : message.getStype().equals("ApprovePass") ?
                WebSocketMessage.FLOW_ApprovePass_MSG : WebSocketMessage.FLOW_ApproveReject_MSG);
        webSocketMessage.setMessageTitle(message.getStitle());
        webSocketMessage.setMessageContent(message.getScontent());
        webSocketMessage.setProcessInstanceId(message.getSprocessinstanceid());
        webSocketMessage.setDate(message.getDcreatetime());
        webSocketMessage.setProcessInstanceCreate(message.getDprocessinstancecreate());
        socket.sendOneMessage(message.getReceiveempid() + "", webSocketMessage);
    }


    /**
     * 查询列表
     *
     * @param pageVo
     * @return
     */
    @Override
    public Json queryList(IPage<Twmessage> page, MessageVo pageVo) throws Exception {
        String userId = WebUtil.getLoginUser().getUserid().toString();
        Date EndTime = pageVo.getEndTime() != null ? DateUtil.offsetDay(pageVo.getEndTime(), 1) : new Date();
        LambdaQueryWrapper<Twmessage> lqw = Wrappers.lambdaQuery();
        lqw.eq(Twmessage::getReceiveempid, userId)
                .eq(StrUtil.isNotBlank(pageVo.getStype()), Twmessage::getStype, pageVo.getStype())
                .eq(pageVo.getBreaded() != null, Twmessage::getBreaded, pageVo.getBreaded())
                .eq(Twmessage::getBdeleted, false)
                .ge(pageVo.getStartTime() != null, Twmessage::getDcreatetime, pageVo.getStartTime());
        if (pageVo.getEndTime() != null) {
            lqw.le(Twmessage::getDcreatetime, EndTime);
        }
        //lqw.orderByDesc(Twmessage::getDcreatetime);
        IPage<Twmessage> lsResult = messageMapper.selectPage(page, lqw);
        Page<MessageDataVo> reslutPage = new Page<MessageDataVo>();
        if (lsResult.getTotal() == 0) {
            return Json.success(reslutPage);
        }
        List<MessageDataVo> lsData = new ArrayList<>();
        for (Twmessage record : lsResult.getRecords()) {
            MessageDataVo messageData = new MessageDataVo();
            BeanUtils.copyProperties(record, messageData);
            messageData.setStype(MessageTypeEnum.getNameByKey(record.getStype()));

            lsData.add(messageData);
        }
        reslutPage.setRecords(lsData);
        reslutPage.setTotal(lsResult.getTotal());

        return Json.success(reslutPage);
    }

    /**
     * 删除消息
     *
     * @param ids
     * @return
     */
    @Override
    public Json delete(String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        //this.removeBatchByIds(idList);
        this.lambdaUpdate().set(Twmessage::getBdeleted, true)
                .in(Twmessage::getId, idList)
                .eq(Twmessage::getBdeleted, false)
                .update(new Twmessage());
        return Json.success();
    }

    /**
     * 置为已读
     *
     * @param messageVo
     * @return
     */
    @Override
    public Json read(MessageVo messageVo) {
        this.lambdaUpdate().set(Twmessage::getBreaded, true)
                .eq(Twmessage::getId, messageVo.getMessageId())
                .eq(Twmessage::getBreaded, false)
                .update(new Twmessage());
        return Json.success();
    }

    @Override
    public List<MessageCountMapperVo> getMessageCount() {
        List<MessageCountMapperVo> messageCountList = messageMapper.getMessageCount(null);
        return messageCountList;
    }

    @Override
    public WebSocketMessage getMessageCountByEmpId(String receiveEmpId) {
        List<MessageCountMapperVo> messageCountList = messageMapper.getMessageCount(receiveEmpId);
        return Optional.ofNullable(messageCountList).map(message -> {
            return message.stream().findFirst().map(m -> {
                WebSocketMessage webSocketMessage = new WebSocketMessage();
                webSocketMessage.setMessageId(m.getMessageId());
                webSocketMessage.setToUser(m.getReceiveEmpId());
                webSocketMessage.setFromUser(m.getCreateEmpId());
                webSocketMessage.setMessageTitle(m.getMessageTitle());
                webSocketMessage.setMessageContent(m.getMessageContent());
                webSocketMessage.setMessageCount(m.getMessageCount());
                webSocketMessage.setType(m.getMessageType().equals("TodoTask") ?
                        WebSocketMessage.FLOW_TodoTask_MSG : m.getMessageType().equals("ApprovePass") ?
                        WebSocketMessage.FLOW_ApprovePass_MSG : m.getMessageType().equals("ApproveReject") ?
                        WebSocketMessage.FLOW_ApproveReject_MSG : WebSocketMessage.SYSTEM_MSG);
                webSocketMessage.setDate(m.getMessageCreateTime());
                webSocketMessage.setProcessInstanceId(m.getMessageProcessInstanceId());
                webSocketMessage.setProcessInstanceCreate(m.getMessageProcessInstanceCreate());
                return webSocketMessage;
            }).orElse((new WebSocketMessage(receiveEmpId, WebSocketMessage.SYSTEM_MSG, 0L)));
        }).orElse((new WebSocketMessage(receiveEmpId, WebSocketMessage.SYSTEM_MSG, 0L)));
    }

    @Override
    public Json readById(String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        List<Twmessage> lsmessages = this.lambdaQuery().in(Twmessage::getId, idList).list();
        if(lsmessages.size()>0){
            lsmessages.forEach(message -> {
                if(!message.getBreaded()){
                    message.setBreaded(true);
                }
            });
            this.updateBatchById(lsmessages);
        }
//        this.lambdaUpdate().set(Twmessage::getBreaded, true)
//                .in(Twmessage::getId, idList)
//                .eq(Twmessage::getBreaded, false)
//                .update(new Twmessage());
        return Json.success();
    }
}
