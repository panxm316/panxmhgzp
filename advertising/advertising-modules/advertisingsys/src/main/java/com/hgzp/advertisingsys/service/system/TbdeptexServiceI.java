package com.hgzp.advertisingsys.service.system;

import com.hgzp.core.model.Tbdept;
import com.hgzp.service.system.BaseTbdeptexServiceI;

/**
 * <p>
 * 部门扩展表 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
public interface TbdeptexServiceI extends BaseTbdeptexServiceI {



    /**
     * updateDeptExName
     * 方法功能:  更新某个部门的名称
     * @author wangwk
     * @date 2023/8/21 13:23
     * @param tbdept
     * @return void
     */
    void updateDeptExName(Tbdept tbdept);

    /**
     * addDeptEx
     * 方法功能: 新增部门时添加部门扩展表
     * @author wangwk
     * @date 2023/8/21 12:51
     * @param tbdept
     * @return void
     */
    void addDeptEx(Tbdept tbdept);

    /**
     * resetDeptEx
     * 方法功能: 重置部门扩展表
     * @author wangwk
     * @date 2023/8/19 14:51
     * @param
     * @return void
     */
    void resetDeptEx();
}
