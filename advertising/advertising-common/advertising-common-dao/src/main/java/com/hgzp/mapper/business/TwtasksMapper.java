package com.hgzp.mapper.business;

import com.hgzp.core.model.Twtasks;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hgzp.pagemodel.ad.AdSummaryVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 任务额度表 Mapper 接口
 * </p>
 *
 * @author wwk
 * @since 2023-10-31
 */
public interface TwtasksMapper extends BaseMapper<Twtasks> {
    /**
     * 汇总部门任务金额
     * 方法功能:  汇总部门任务金额
     *
     * @return AdSummaryVO
     * @author lhl
     * @date 2024/2/1
     * */
    AdSummaryVO summaryDeptTask(@Param("deptids") List<String> deptids, @Param("mediaids")List<String> mediaids, @Param("taskdate")String taskdate,@Param("sumtype")String sumtype);
}
