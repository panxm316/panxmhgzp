package com.hgzp.advertisingsys.service.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.core.model.Tbresourcetype;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hgzp.core.page.BaseQueryInfo;

/**
 * <p>
 * 资源文件类型 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-10-27
 */
public interface TbresourcetypeServiceI extends IService<Tbresourcetype> {

    /**
     * getResourcetypePageList
     * 方法功能: 获取资源文件类型分页列表
     * @author CGD
     * @date 2023/10/27 9:37
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.core.model.Tbresourcetype>
     */
    public IPage<Tbresourcetype> getResourcetypePageList(Page<Tbresourcetype> page, BaseQueryInfo query);
    /**
     * 根据文件format获取资源类型
     * 方法功能:根据文件format获取资源类型
     * @author CGD
     * @date 2023/10/30 16:25
     * @param format
     * @return com.hgzp.core.model.Tbresourcetype
     */
    public Tbresourcetype getResourcetypeByFormat(String format);
}
