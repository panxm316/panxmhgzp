package com.hgzp.advertising.service.ad;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.pagemodel.ad.dto.TwadprojectDTO;
import com.hgzp.advertising.pagemodel.ad.vo.AdprojectVO;
import com.hgzp.core.model.Twadproject;
import com.hgzp.core.page.BaseQueryInfo;
import com.hgzp.core.page.Json;
import com.hgzp.service.common.IMyService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 广告项目 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
public interface TwadprojectServiceI extends IMyService<Twadproject> {

    /**
     * getAdProjectPageList
     * 方法功能:  分页查询广告项目
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.core.model.Twadproject>
     * @author CGD
     * @date 2023/8/18 10:30
     */
    IPage<AdprojectVO> getAdProjectPageList(Page<Twadproject> page, BaseQueryInfo query);

    /**
     * 获取广告项目列表
     * 方法功能:
     *
     * @param
     * @return java.util.List<com.hgzp.core.model.Twadproject>
     * @author songly
     * @date 2023/12/5 15:38
     */
    List<AdprojectVO> getAdProjectList();

    /**
     * 根据id获取广告项目
     * 方法功能:
     *
     * @param id
     * @return com.hgzp.core.page.Json<com.hgzp.advertising.pagemodel.ad.vo.AdprojectVO>
     * @author songly
     * @date 2024/3/15 20:09
     */
    AdprojectVO getByAdProjectId(String id);

    /**
     * deleteAdProject
     * 方法功能: 根据id删除广告项目信息，支持","多选分割
     *
     * @param ids
     * @return void
     * @author CGD
     * @date 2023/8/18 10:29
     */
    String deleteAdProject(String ids);

    /**
     * saveAdProject
     * 方法功能: 保存
     *
     * @param adprojectDTO
     * @return void
     * @author songly
     * @date 2023/9/21 15:40
     */
    void saveAdProject(TwadprojectDTO adprojectDTO) throws Exception;
    ///
    void updateAdProject(TwadprojectDTO adprojectDTO) throws Exception;

    /**
     * endAdProject
     * 方法功能: 项目结项
     *
     * @param ids
     * @return boolean
     * @author songly
     * @date 2023/9/21 16:46
     */
    Json endAdProject(String ids);

    /**
     * 是否存在名称相同的广告项目
     * 方法功能:
     *
     * @param twadproject
     * @return boolean
     * @author songly
     * @date 2024/2/19 9:34
     */
    boolean isExistAdProject(Twadproject twadproject);

    /**
     * getAdProjectBudget
     * 方法功能:获取项目预算
     *
     * @param adprojectId
     * @return java.math.BigDecimal
     * @author yanz
     * @date 2024/3/11 13:14
     */
    BigDecimal getAdProjectBudget(Long adprojectId);

    /**
     * 根据项目编号获取广告项目
     * 方法功能:
     *
     * @param sprojectcode
     * @return com.hgzp.core.model.Twadproject
     * @author songly
     * @date 2024/3/11 19:50
     */
    AdprojectVO getAdProjectByCode(String sprojectcode);

    /**
     * 项目审批
     * 方法功能:
     *
     * @param projectId
     * @param flowId
     * @return com.hgzp.core.page.Json<java.lang.String>
     * @author songly
     * @date 2024/3/15 14:09
     */
    Json<String> approveAdProject(String projectId, String flowId);

    /**
     * 更新审批意见及状态
     * 方法功能:
     *
     * @param applicationid
     * @param sProjectId
     * @param bUpdatepinion
     * @param approveDesc
     * @param iapproveStatus
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2024/3/15 14:12
     */
    Json updateAdprojectApprovalopinions(String applicationid, String sProjectId, boolean bUpdatepinion,
                                         String approveDesc, Integer iapproveStatus);


}
