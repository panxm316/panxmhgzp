package com.hgzp.advertising.service.business;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hgzp.advertising.pagemodel.business.dto.DebtReasonDTO;
import com.hgzp.advertising.pagemodel.business.vo.DebtReasonVO;
import com.hgzp.core.model.Twdebtreason;

/**
 * <p>
 * 欠款原因表 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-11-17
 */
public interface TwdebtreasonServiceI extends IService<Twdebtreason> {
    /**
     * 方法功能:  根据条件查询欠款原因分页列表
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.core.model.Twdebtreason>
     * @author suny
     * @date 2023/11/22 9:59
     */
    IPage<Twdebtreason> getDebtReasonPageList(Page<Twdebtreason> page, DebtReasonVO query) throws Exception;

    /**
     * 方法功能: 提交欠款原因信息
     *
     * @param entity
     * @return void
     * @author suny
     * @date 2023/11/22 9:59
     */
    void updateDebtReason(DebtReasonDTO entity) throws Exception;
}
