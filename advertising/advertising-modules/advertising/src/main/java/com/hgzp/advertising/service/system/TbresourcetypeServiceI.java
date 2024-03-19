package com.hgzp.advertising.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hgzp.core.model.Tbresourcetype;

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
     * 根据文件format获取资源类型
     * 方法功能:根据文件format获取资源类型
     * @author CGD
     * @date 2023/10/30 16:25
     * @param format
     * @return com.hgzp.core.model.Tbresourcetype
     */
    public Tbresourcetype getResourcetypeByFormat(String format);
}
