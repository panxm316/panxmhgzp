package com.hgzp.advertising.service.price;


import com.hgzp.core.model.Tbweeksetting;
import com.hgzp.core.page.TreeModel;
import com.hgzp.service.common.IMyService;

import java.util.List;


/**
 * <p>
 * 星期设置 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
public interface TbweeksettingServiceI extends IMyService<Tbweeksetting> {


    public List<TreeModel> getWeekSettingTree();

    /**
     * 获取可用的星期设置,已排序
     *
     * @return {@link List<Tbweeksetting>}
     * @author wangxk
     * @since 2023-12-13
     */
    List<Tbweeksetting> listUsableWeekSetting();
}
