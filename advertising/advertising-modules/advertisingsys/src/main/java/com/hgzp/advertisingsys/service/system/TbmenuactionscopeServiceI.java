package com.hgzp.advertisingsys.service.system;

import com.hgzp.core.model.Tbscope;
import com.hgzp.service.system.BaseTbmenuactionscopeServiceI;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 菜单范围关联表 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
public interface TbmenuactionscopeServiceI extends BaseTbmenuactionscopeServiceI {



    /**
     * getScopeGroupByMenuActionId
     * 方法功能: 根据menuactionid 分组查询出 按钮关联的范围
     * @author wangwk
     * @date 2023/8/28 8:54
     * @param menuActionIdList
     * @return java.util.Map<java.lang.Long,java.util.List<com.hgzp.core.model.Tbscope>>
     */
    Map<Long, List<Tbscope>> getScopeGroupByMenuActionId(List<Long> menuActionIdList);
}
