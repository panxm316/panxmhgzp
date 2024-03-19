package com.hgzp.service.system;

import com.hgzp.core.model.Tbroleactionscope;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色范围表 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
public interface BaseTbroleactionscopeServiceI extends IService<Tbroleactionscope> {


    public List<Tbroleactionscope> getRoleActionScopeList(List<Long> roleActionIdList);

}
