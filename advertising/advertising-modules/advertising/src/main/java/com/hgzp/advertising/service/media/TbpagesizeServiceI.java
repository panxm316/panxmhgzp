package com.hgzp.advertising.service.media;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.core.model.Tbpagesize;
import com.hgzp.service.common.IMyService;

import java.util.List;

/**
 TbpagesizeServiceI
 创建人：songly
 类描述：版心设置 服务类
 创建日期：2023/11/25 10:10
 */
public interface TbpagesizeServiceI extends IMyService<Tbpagesize> {
    /**
     * 获取有效版心列表
     * 方法功能:
     * @author hgsongly
     * @date 2023/11/25 10:20
     * @param
     * @return java.util.List<com.hgzp.core.model.Tbpagesize>
     */
    List<Tbpagesize> getPageSizeList();
}