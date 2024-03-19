package com.hgzp.advertising.service.statistic;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hgzp.advertising.pagemodel.statistic.dto.DatabackupGroupDTO;
import com.hgzp.advertising.pagemodel.statistic.vo.DataBackupDetailVO;
import com.hgzp.advertising.pagemodel.statistic.vo.DatabackupGroupVO;
import com.hgzp.core.model.Twdatabackupdetail1;
import com.hgzp.core.model.Twdatabackupgroup;
import com.hgzp.core.page.BaseQueryInfo;

/**
 * <p>
 * 数据轧账总表 服务类
 * </p>
 *
 * @author muyn
 * @since 2024-01-19
 */
public interface DatabackupGroupServiceI extends IService<Twdatabackupgroup> {
    /**
     * 获取数据轧账总表分页列表
     * 方法功能: 获取数据轧账总表分页列表
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.core.model.Twdatabackupgroup>
     * @author suny
     * @date 2024/1/19 16:20
     */
    IPage<Twdatabackupgroup> getDataBackupGroupPageList(Page<Twdatabackupgroup> page, DatabackupGroupVO query) throws Exception;

    /**
     * 获取数据轧账明细分页列表
     * 方法功能:  获取数据轧账明细分页列表
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.core.model.Twdatabackupdetail1>
     * @author suny
     * @date 2024/1/19 16:35
     */
    IPage<Twdatabackupdetail1> getDataBackupDetailPageList(Page<Twdatabackupdetail1> page, DataBackupDetailVO query) throws Exception;

    /**
     * 根据时间范围查询定向刊期总数
     * 方法功能: 根据时间范围查询定向刊期总数
     *
     * @param query
     * @return long
     * @author suny
     * @date 2024/1/19 16:44
     */
    long getOrderItemCount(BaseQueryInfo query) throws Exception;

    /**
     * 根据时间范围查询定向刊期数据，写入数据轧账明细表
     * 方法功能:  根据时间范围查询定向刊期数据，写入数据轧账明细表
     *
     * @param entity
     * @return void
     * @author suny
     * @date 2024/1/19 16:44
     */
    void saveDataBackupDetail(DatabackupGroupDTO entity) throws Exception;
}
