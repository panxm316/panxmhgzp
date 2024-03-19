package com.hgzp.advertising.service.system;

import com.hgzp.core.model.Tbroleaction;
import com.hgzp.service.system.BaseTbroleactionServiceI;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 权限行为表 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
public interface TbroleactionServiceI extends BaseTbroleactionServiceI {


    /**
     * 根据角色id获取按钮权限
     * 方法功能: 根据角色id获取按钮权限
     * @author wangwk
     * @date 2023/11/2 16:59
     * @param roleIdList
     * @return java.util.List<com.hgzp.core.model.Tbroleaction>
     */
    List<Tbroleaction> getTbroleactionByRoleIds(List<Long> roleIdList);
}
