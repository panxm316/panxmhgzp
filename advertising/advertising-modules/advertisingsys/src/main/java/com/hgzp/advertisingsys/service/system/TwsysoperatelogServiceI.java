package com.hgzp.advertisingsys.service.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.core.model.Twsysoperatelog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hgzp.core.page.BaseQueryInfo;
import org.springframework.scheduling.annotation.Async;

/**
 * <p>
 * 系统管理操作日志 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-09-08
 */
public interface TwsysoperatelogServiceI extends IService<Twsysoperatelog> {

    String compareTowObject(Object oldObj, Object newObj) throws Exception;

    void saveAsyncSysoperatelogWithoutUser(Twsysoperatelog twsysoperatelog) throws Exception;


    @Async
    void saveAsyncSysoperatelog(Twsysoperatelog twsysoperatelog);


    /***
     * getSysoperatelogPageList
     * 方法功能:   分页查询系统管理操作日志
     * @author CGD
     * @date 2023/9/12 15:33
     * @param page
     * @param query
     * @param slogtype
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.hgzp.core.model.Twsysoperatelog>
     */
    public Page<Twsysoperatelog> getSysoperatelogPageList(Page<Twsysoperatelog> page, BaseQueryInfo query, String slogtype);

}
