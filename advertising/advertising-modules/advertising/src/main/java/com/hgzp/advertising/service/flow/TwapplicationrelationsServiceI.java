package com.hgzp.advertising.service.flow;

import com.hgzp.advertising.pagemodel.flow.TwapplicationrelationsVO;
import com.hgzp.advertising.pagemodel.flow.dto.TwapplicationrelationsDTO;
import com.hgzp.core.model.Twapplicationrelations;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.service.common.IMyService;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 审批流程关联表 服务类
 * </p>
 *
 * @author muyn
 * @since 2023-12-05
 */
public interface TwapplicationrelationsServiceI extends IMyService<Twapplicationrelations> {

    /**
     * 新增审批流程关联信息
     *
     * @param twapplicationrelationsVO 审批流程关联表数据传输对象
     * @return void
     * @author muyn
     * @since 2023-12-05
     */
    void saveApplicationRelations(TwapplicationrelationsVO twapplicationrelationsVO);

    /**
     * 新增审批流程关联信息
     * 方法功能:
     *
     * @param sapplicationid
     * @param sMainId
     * @param sChildId
     * @param iApproveStatus
     * @return void
     * @author songly
     * @date 2024/3/14 13:08
     */
    void saveApplicationRelations(String sapplicationid, Long sMainId, Long sChildId, Integer iApproveStatus,String sFlowType,String approveType);

    /**
     * 删除 审批流程关联信息
     *
     * @param twapplicationrelationsDTO 审批流程关联表数据传输对象
     * @return void
     * @author muyn
     * @since 2023-12-05
     */
    void deleteApplicationRelations(TwapplicationrelationsDTO twapplicationrelationsDTO);

    /**
     * 修改审批流程关联信息
     *
     * @param twapplicationrelationsDTO 审批流程关联表数据传输对象
     * @return void
     * @author muyn
     * @since 2023-12-05
     */
    void updateApplicationRelations(TwapplicationrelationsDTO twapplicationrelationsDTO);

    /**
     * 根据ID查询流程审批关联信息
     *
     * @param id Twapplicationrelations.id
     * @return {@link Twapplicationrelations}
     * @author muyn
     * @since 2023-12-05
     */
    Twapplicationrelations getApplicationRelationsById(String id);

    /**
     * 根据某些值查询审批流程关联信息
     *
     * @param twapplicationrelationsDTO 审批流程关联表数据传输对象
     * @return {@link TwapplicationrelationsVO}
     * @author muyn
     * @since 2023-12-05
     */
    TwapplicationrelationsVO getApplicationRelations(TwapplicationrelationsDTO twapplicationrelationsDTO);

    /**
     * 根据的某些值查询审批流程关联信息列表
     *
     * @param twapplicationrelationsDTO 审批流程关联表数据传输对象
     * @return {@link List<TwapplicationrelationsVO>}
     * @author muyn
     * @since 2023-12-05
     */
    List<TwapplicationrelationsVO> getApplicationRelationsList(TwapplicationrelationsDTO twapplicationrelationsDTO);

    /**
     * 根据的某些值查询审批流程关联信息分页数据
     *
     * @param page  分页请求参数
     * @param twapplicationrelationsDTO 审批流程关联表数据传输对象
     * @return {@link IPage<TwapplicationrelationsVO>}
     * @author muyn
     * @since 2023-12-05
     */
    IPage<TwapplicationrelationsVO> getApplicationrelationsPageList(IPage<Twapplicationrelations> page,
                                                                    TwapplicationrelationsDTO twapplicationrelationsDTO);


}
