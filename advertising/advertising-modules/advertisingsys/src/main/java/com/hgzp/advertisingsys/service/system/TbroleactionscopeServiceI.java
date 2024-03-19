package com.hgzp.advertisingsys.service.system;

import com.hgzp.core.model.Tbroleactionscope;
import com.hgzp.service.system.BaseTbroleactionscopeServiceI;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色范围表 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
public interface TbroleactionscopeServiceI extends BaseTbroleactionscopeServiceI {


    /**
     * getRoleActionScopeGroupByActionId
     * 方法功能: 根据角色所拥有的按钮查询相应的范围
     * @author wangwk
     * @date 2023/8/28 9:27
     * @param roleActionIdList
     * @return java.util.Map<java.lang.Long,java.util.List<com.hgzp.core.model.Tbroleactionscope>>
     */
    Map<Long, List<Tbroleactionscope>> getRoleActionScopeGroupByActionId(List<Long> roleActionIdList);
}
