package com.hgzp.service.system;

import com.hgzp.core.model.Tbmenuaction;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 菜单行为表 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
public interface BaseTbmenuactionServiceI extends IService<Tbmenuaction> {


    /**
     * getMenuActionListByMenuIdList
     * 方法功能: 根据menuid 查询出所有的按钮 的信息
     * @author wangwk
     * @date 2023/8/25 14:11
     * @param menuIdList
     * @return java.util.List<com.hgzp.core.model.Tbmenuaction>
     */
    List<Tbmenuaction> getMenuActionListByMenuIdList(List<Long> menuIdList);

}
