package com.hgzp.service.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hgzp.core.model.Twlog;
import com.hgzp.core.page.BaseQueryInfo;

/**
 *
 * 方法功能: 日志
 * @author CGD
 * @date 2023/9/6 13:14
 * @param null
 * @return
 */
public interface BaseTwlogServiceI extends IService<Twlog> {

    /***
     * getLogPageList
     * 方法功能:  分页查询日志表
     * @author CGD
     * @date 2023/9/6 12:42
     * @param page
     * @param query
     * @param slogtype
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.core.model.Twlog>
     */
    Page<Twlog> getLogPageList(Page<Twlog> page, BaseQueryInfo query, String slogtype);
}
