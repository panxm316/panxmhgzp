package com.hgzp.advertisingsys.service.system;

import com.hgzp.core.model.Tbdept;
import com.hgzp.service.system.BaseTbroledeptServiceI;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色部门表 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-08-24
 */
public interface TbroledeptServiceI extends BaseTbroledeptServiceI {


    /**
     * getDeptListGroupByRoleIds
     * 方法功能:  按照角色id 进行分组查询角色的所有关联部门
     * @author wangwk
     * @date 2023/8/24 12:48
     * @param roleIdList 角色id list
     * @return java.util.Map<java.lang.Long,java.util.List<com.hgzp.core.model.Tbdept>>
     */
    Map<Long, List<Tbdept>> getDeptListGroupByRoleIds(List<Long> roleIdList);
}
