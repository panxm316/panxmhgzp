package com.hgzp.mapper.system;
import org.apache.ibatis.annotations.Param;

import com.hgzp.core.model.Tbdept;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 部门表 Mapper 接口
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
public interface TbdeptMapper extends BaseMapper<Tbdept> {



    /**
     * updateBuseByIdList
     * 方法功能:  批量修改部门的buse状态
     * @author wangwk
     * @date 2023/9/12 9:35
     * @param buse  启用标志
     * @param idList  id List
     * @return int
     */
    int updateBuseByIdList(@Param("buse") Boolean buse, @Param("idList") List<Long> idList);

    /**
     * updatePidIdepthByIdList
     * 方法功能: 批量更新父部门跟深度
     * @author wangwk
     * @date 2023/9/13 14:07
     * @param parentid  父部门id
     * @param idepth   深度
     * @param idList   部门id List
     * @return int
     */
    int updatePidIdepthByIdList(@Param("parentid") Long parentid, @Param("idepth") int idepth, @Param("idList") List<Long> idList );

}
