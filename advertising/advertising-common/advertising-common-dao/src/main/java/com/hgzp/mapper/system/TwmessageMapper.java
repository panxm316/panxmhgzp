package com.hgzp.mapper.system;

import com.hgzp.core.model.Twmessage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hgzp.pagemodel.system.MessageCountMapperVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 消息表 Mapper 接口
 * </p>
 *
 * @author wwk
 * @since 2023-10-25
 */
public interface TwmessageMapper extends BaseMapper<Twmessage> {
    /**
     * 获取消息提醒
     * @return
     */
    public List<MessageCountMapperVo> getMessageCount(@Param("receiveEmpId") String receiveEmpId);
}
