package com.hgzp.advertising.service.price.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hgzp.advertising.service.price.TbweeksettingServiceI;
import com.hgzp.core.model.Tbmediatype;
import com.hgzp.core.model.Tbweeksetting;
import com.hgzp.core.page.TreeModel;
import com.hgzp.mapper.ad.TbweeksettingMapper;
import com.hgzp.service.common.impl.MyServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 星期设置 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TbweeksettingServiceImpl extends MyServiceImpl<TbweeksettingMapper, Tbweeksetting> implements TbweeksettingServiceI {
    @Resource
    private TbweeksettingMapper mapper;

    @Override
    public List<TreeModel> getWeekSettingTree() {
        List<Tbweeksetting> list = this.lambdaQuery().eq(Tbweeksetting::getBuse, true).orderByAsc(Tbweeksetting::getIsort).list();
        List<TreeModel> treeModelList = new ArrayList<>();
        for (Tbweeksetting weeksetting : list) {
            TreeModel treeModel = new TreeModel();
            treeModel.setId(weeksetting.getId());
            treeModel.setName(weeksetting.getSname());
            treeModelList.add(treeModel);
        }
        return treeModelList;
    }

    @Override
    public List<Tbweeksetting> listUsableWeekSetting() {
        return list(Wrappers.<Tbweeksetting>lambdaQuery()
                .eq(Tbweeksetting::getBuse, true)
                .orderByAsc(Tbweeksetting::getIsort));
    }
}
