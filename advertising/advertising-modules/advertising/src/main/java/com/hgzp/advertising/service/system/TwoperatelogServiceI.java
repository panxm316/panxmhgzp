package com.hgzp.advertising.service.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.core.model.Twoperatelog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hgzp.core.model.Twsysoperatelog;
import com.hgzp.core.page.BaseQueryInfo;
import org.springframework.scheduling.annotation.Async;

/**
 * <p>
 * 广告管理操作日志 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-09-18
 */
public interface TwoperatelogServiceI extends IService<Twoperatelog> {


    String compareTowObject(Object oldObj, Object newObj) throws Exception;

    void saveAsyncOperatelogWithoutUser(Twoperatelog twoperatelog) throws Exception;

    @Async
    void saveAsyncOperatelog(Twoperatelog twoperatelog);
    public Page<Twoperatelog> getOperatelogPageList(Page<Twoperatelog> page, BaseQueryInfo query, String slogtype);

}
