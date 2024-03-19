package com.hgzp.mapper.media;

import com.hgzp.core.model.Tbadspec;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 广告规格 Mapper 接口
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
public interface TbadspecMapper extends BaseMapper<Tbadspec> {

    /**
     * updateDstartdateByAll
     * 方法功能:  修改全部开始日期
     * @author CGD
     * @date 2023/9/20 14:54
     * @param dstartdate
     * @return int
     */
    int updateDstartdateByAll(@Param("dstartdate") Date dstartdate);
    /**
     * updateDenddateByAll
     * 方法功能: 修改全部结束日期
     * @author CGD
     * @date 2023/9/20 14:54
     * @param denddate
     * @return int
     */
    int updateDenddateByAll(@Param("denddate") Date denddate);
    /**
     * updateDenddateByIdList
     * 方法功能:   根据id修改结束日期
     * @author CGD
     * @date 2023/9/20 15:41
     * @param denddate
     * @param idList
     * @return int
     */
    int updateDenddateByIdList(@Param("denddate") Date denddate, @Param("idList") List<Long> idList);
}
