package com.hgzp.advertising.service.media.impl;

import cn.hutool.core.util.StrUtil;
import com.hgzp.advertising.service.media.TbfoldServiceI;
import com.hgzp.advertising.service.media.TbmediaServiceI;
import com.hgzp.advertising.service.schedule.ScheduleseekServiceI;
import com.hgzp.core.model.Tbmedia;
import com.hgzp.core.page.DataCombo;
import com.hgzp.core.page.TreeModel;
import com.hgzp.core.page.TreeQuery;
import com.hgzp.mapper.media.TbmediaMapper;
import com.hgzp.service.common.impl.MyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 媒体信息 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TbmediaServiceImpl extends MyServiceImpl<TbmediaMapper, Tbmedia> implements TbmediaServiceI {
    @Autowired
    private TbmediaMapper tbmediaMapper;
    @Autowired
    TbfoldServiceI tbfoldServiceI;
    @Autowired
    ScheduleseekServiceI  scheduleseekServiceI;

    @Override
    public List<DataCombo> getMediaDataCombo() {
        List<Tbmedia> mediaList = this.lambdaQuery()
                .eq(Tbmedia::getBuse, true)
                .orderByAsc(Tbmedia::getIsort)
                .list();
        return convert2DataCombos(mediaList);
    }


    @Override
    public List<DataCombo> getMediaDataComboByType(String type) {
        List<Tbmedia> mediaList = this.lambdaQuery()
                .eq(Tbmedia::getBuse, true)
                .eq(Tbmedia::getMediatypekey, type)
                .orderByAsc(Tbmedia::getIsort)
                .list();
        return convert2DataCombos(mediaList);
    }

    /**
     * convert2DataCombos
     * 方法功能: medialist 转换为 DataComboList
     *
     * @param mediaList
     * @return java.util.List<com.hgzp.core.page.DataCombo>
     * @author wangwk
     * @date 2023/9/19 15:51
     */
    private List<DataCombo> convert2DataCombos(List<Tbmedia> mediaList) {
        List<DataCombo> comboList = mediaList.stream()
                .map(item -> new DataCombo(item.getId().toString(), item.getSname()))
                .collect(Collectors.toList());
        return comboList;
    }


    @Override
    public List<TreeModel> getSysMediaTree(TreeQuery query, boolean showUnuseDept) {
        List<Tbmedia> mediaList = this.lambdaQuery()
                .and(StrUtil.isNotBlank(query.getQueryKey()), m ->
                        m.like(Tbmedia::getSname, query.getQueryKey())
                                .or()
                                .in(StrUtil.isNotBlank(query.getContainsIds()), Tbmedia::getId, query.getContainsIds().split(",")))
                .eq(!showUnuseDept, Tbmedia::getBuse, true)
                .orderByAsc(Tbmedia::getIsort)
                .list();
        List<TreeModel> treeModelList = mediaList.stream().map(m -> {
            TreeModel treeModel = new TreeModel();
            treeModel.setId(m.getId());
            treeModel.setIconSkin(TreeModel.UNIT);
            treeModel.setName(m.getSname());
            treeModel.setNocheck(false);
            return treeModel;
        }).collect(Collectors.toList());
        return treeModelList;
    }

    @Override
    public List<Tbmedia> getMediaList() {
        List<Tbmedia> mediaList = this.lambdaQuery()
                .eq(Tbmedia::getBuse, true)
                .orderByAsc(Tbmedia::getIsort)
                .list();
        return mediaList;
    }

    @Override
    public List<TreeModel> getMediaFloadTree(TreeQuery query) {
        List<Tbmedia> tbmediaList =  getMediaList();
        List<TreeModel> treeModelList = new ArrayList<>();
        for( int i = 0; i < tbmediaList.size(); i++ ) {
            Tbmedia tbmedia = tbmediaList.get(i);
            TreeModel treeModel = new TreeModel();
            treeModel.setId(tbmedia.getId());
            treeModel.setIconSkin(TreeModel.UNIT);
            treeModel.setName(tbmedia.getSname());
            treeModel.setNocheck(true);
            treeModel.setParentId(0L);
            treeModelList.add(treeModel);
            List<DataCombo> dataComboList = tbfoldServiceI.getFoldCombo( String.valueOf(tbmedia.getId()));
            for( int j = 0; j < dataComboList.size(); j++ ) {
                TreeModel foladtreeModel = new TreeModel();
                foladtreeModel.setId(Long.valueOf(dataComboList.get(j).getId()));
                foladtreeModel.setIconSkin(TreeModel.UNIT);
                foladtreeModel.setName(dataComboList.get(j).getName());
                foladtreeModel.setNocheck(false);
                foladtreeModel.setParentId(tbmedia.getId());
                treeModelList.add(foladtreeModel);
            }
        }
        return treeModelList;
    }

    @Override
    public List<TreeModel> getMediaFloadAreaverTree(TreeQuery query) {
        List<Tbmedia> tbmediaList =  getMediaList();
        List<TreeModel> treeModelList = new ArrayList<>();
        for( int i = 0; i < tbmediaList.size(); i++ ) {
            Tbmedia tbmedia = tbmediaList.get(i);
            TreeModel treeModel = new TreeModel();
            treeModel.setId(tbmedia.getId());
            treeModel.setIconSkin(TreeModel.UNIT);
            treeModel.setName(tbmedia.getSname());
            treeModel.setNocheck(true);
            treeModel.setParentId(0L);
            treeModelList.add(treeModel);
            List<DataCombo> dataComboList = tbfoldServiceI.getFoldCombo( String.valueOf(tbmedia.getId()));
            for( int j = 0; j < dataComboList.size(); j++ ) {
                TreeModel foladtreeModel = new TreeModel();
                foladtreeModel.setId(Long.valueOf(dataComboList.get(j).getId()));
                foladtreeModel.setIconSkin(TreeModel.UNIT);
                foladtreeModel.setName(dataComboList.get(j).getName());
                foladtreeModel.setNocheck(true);
                foladtreeModel.setParentId(tbmedia.getId());
                treeModelList.add(foladtreeModel);
                List<DataCombo> arComboList = scheduleseekServiceI.getFoldAreaverCombo( dataComboList.get(j).getId()) ;
                for( int m = 0; m < arComboList.size(); m++ ) {
                    TreeModel artreeModel = new TreeModel();
                    artreeModel.setId(Long.valueOf(arComboList.get(m).getId()));
                    artreeModel.setIconSkin(TreeModel.UNIT);
                    artreeModel.setName(arComboList.get(m).getName());
                    artreeModel.setNocheck(false);
                    artreeModel.setParentId(Long.valueOf(dataComboList.get(j).getId()));
                    treeModelList.add(artreeModel);

                }
            }
        }
        return treeModelList;
    }
}
