package com.hgzp.advertising.service.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hgzp.advertising.pagemodel.customer.vo.CustomerReq;
import com.hgzp.advertising.pagemodel.customer.vo.CustomerVo;
import com.hgzp.advertising.pagemodel.system.vo.OpinionsReq;
import com.hgzp.advertising.pagemodel.system.vo.OpinionsVO;
import com.hgzp.core.model.Twcustomer;
import com.hgzp.core.model.Twopinions;
import com.hgzp.service.common.IMyService;

import java.util.List;

/**
 * <p>
 * 常用审批意见 服务类
 * </p>
 *
 * @author songly
 * @since 2024-03-07
 */
public interface OpinionsServiceI extends IMyService<Twopinions> {
    /**
     * 检索常用审批意见分页列表
     * 方法功能:
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.advertising.pagemodel.system.vo.OpinionsVO>
     * @author songly
     * @date 2024/3/7 19:29
     */
    IPage<OpinionsVO> getOpinionsPageList(Page<Twopinions> page, OpinionsReq query) throws Exception;

    /**
     * 检索常用审批意见列表
     * 方法功能: 检索常用审批意见列表
     *
     * @param query
     * @return java.util.List<com.hgzp.advertising.pagemodel.system.vo.OpinionsVO>
     * @author songly
     * @date 2024/3/7 19:33
     */
    List<OpinionsVO> getOpinionsList(OpinionsReq query) throws Exception;

    /**
     * 保存常用审批意见
     * 方法功能:
     *
     * @param twopinions
     * @return void
     * @author songly
     * @date 2024/3/7 19:30
     */
    void saveOpinions(Twopinions twopinions) throws Exception;

    /**
     * 删除常用审批意见
     * 方法功能: 删除常用审批意见
     *
     * @param ids
     * @return void
     * @author songly
     * @date 2024/3/7 19:32
     */
    void deleteOpinions(String ids) throws Exception;

    /**
     * 根据ID查询常用审批意见
     * 方法功能: 根据ID查询常用审批意见
     *
     * @param id
     * @return com.hgzp.advertising.pagemodel.system.vo.OpinionsVO
     * @author songly
     * @date 2024/3/7 19:31
     */
    OpinionsVO getById(Long id);

    /**
     * 更新常用审批意见
     * 方法功能: 更新常用审批意见
     *
     * @param twopinions
     * @return void
     * @author songly
     * @date 2024/3/7 19:31
     */
    void updateOpinions(Twopinions twopinions) throws Exception;

}
