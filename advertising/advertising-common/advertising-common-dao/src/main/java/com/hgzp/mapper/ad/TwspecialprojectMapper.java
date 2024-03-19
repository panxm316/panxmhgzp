package com.hgzp.mapper.ad;

import com.hgzp.core.model.Twspecialproject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hgzp.pagemodel.ad.AdProjectCountVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 特刊项目 Mapper 接口
 * </p>
 *
 * @author muyn
 * @since 2024-03-13
 */
public interface TwspecialprojectMapper extends BaseMapper<Twspecialproject> {
    List<AdProjectCountVO> querySpecialProjectCount(@Param("ids")List<Long> ids, @Param("pageNum")Integer pageNum, @Param("pageSize")Integer pageSize, @Param("publishstatus")String publishstatus);

}
