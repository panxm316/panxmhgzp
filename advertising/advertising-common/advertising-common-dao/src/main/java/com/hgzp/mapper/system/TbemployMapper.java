package com.hgzp.mapper.system;

import com.hgzp.core.model.Tbemploy;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 人员表 Mapper 接口
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
public interface TbemployMapper extends BaseMapper<Tbemploy> {


    public void updateBuseByIds(@Param("idList") List<Long> idList, @Param("buse")Boolean buse);

}
