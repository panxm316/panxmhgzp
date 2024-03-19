package com.hgzp.mapper.ad;

import com.hgzp.core.model.Twadproject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hgzp.pagemodel.ad.AdProjectCountVO;
import com.hgzp.pagemodel.schedule.OrderPublishQueryResultVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 广告项目 Mapper 接口
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
public interface TwadprojectMapper extends BaseMapper<Twadproject> {
    List<AdProjectCountVO> queryAdProjectCount(@Param("ids")List<Long> ids, @Param("pageNum")Integer pageNum, @Param("pageSize")Integer pageSize, @Param("publishstatus")String publishstatus);
}
