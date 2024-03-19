package com.hgzp.advertising.service.system.impl;

import cn.hutool.core.util.ReflectUtil;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.service.system.TbadindustryServiceI;
import com.hgzp.core.model.Tbadindustry;
import com.hgzp.core.model.Tbadindustryextend;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.TreeModel;
import com.hgzp.mapper.ad.TbadindustryMapper;
import com.hgzp.mapper.ad.TbadindustryextendMapper;
import com.hgzp.service.common.impl.MyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TbadindustryServiceImpl
 * 创建人：songly
 * 类描述：TODO
 * 创建日期：2023/10/28 8:57
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TbadindustryServiceImpl extends MyServiceImpl<TbadindustryMapper, Tbadindustry> implements TbadindustryServiceI {
    public static final int MAX_IDEPTH = 5;
    @Autowired
    private TbadindustryextendMapper tbadindustryextendMapper;
    @Autowired
    private HgDataChangeRecorderInnerInterceptor innerInterceptor;

    /**
     * getTbAdIndustryTree
     * 方法功能： 广告行业树
     *
     * @param
     * @return
     * @author: muyn
     */
    @Override
    public List<TreeModel> getTbAdIndustryTree() {
        //检索有效行业
        // 根据ID列表查出行业数据
        List<Tbadindustry> adIndustryList = this.lambdaQuery()
                .eq(Tbadindustry::getBuse, true)
                .orderByAsc(Tbadindustry::getSname)
                .list();
        //数据库类型转为展示模型
        List<TreeModel> adIndustryTreeM = adIndustryList
                .stream()
                .distinct()
                .map(node -> {
                    TreeModel treeM = new TreeModel();
                    treeM.setId(node.getId());
                    treeM.setName(node.getSname());
                    treeM.setParentId(node.getParentid());
                    treeM.setBuse(node.getBuse());
                    treeM.setStype(node.getBclassified().toString());
                    return treeM;
                })
                .collect(Collectors.toList());
        return adIndustryTreeM;
    }

    /**
     * getQueryAdIndustryId
     * 方法功能:  通过关键字查询时查询出所有符合条件的节点以及父节点id
     *
     * @param querykey
     * @return java.util.List<java.lang.Long>
     * @author muyn
     * @date 2023/9/1 13:03
     */
    public List<Long> getQueryAdIndustryId(String querykey) {
        List<Long> resultIdList = new ArrayList<>();
        List<Long> idList = this.lambdaQuery()
                .like(Tbadindustry::getSname, querykey)
                .list()
                .stream()
                .map(Tbadindustry::getId)
                .collect(Collectors.toList());

        for (Long id : idList) {
            List<Long> parentDeptId = getParentAdIndustryId(id);
            resultIdList.addAll(parentDeptId);
            resultIdList.add(id);
        }

        return resultIdList.stream().distinct().collect(Collectors.toList());
    }

    /**
     * getParentDeptId
     * 方法功能:  获取某个行业的父行业id，不包含当前节点
     *
     * @param adIndustryId
     * @return java.util.List<java.lang.Long>
     * @author muyn
     * @date 2023/9/1 14:31
     */
    private List<Long> getParentAdIndustryId(Long adIndustryId) {
        Tbadindustryextend tbadindustryextend = tbadindustryextendMapper.selectById(adIndustryId);
        List<Long> parentIdList = new ArrayList<>();
        for (int i = 1; i <= MAX_IDEPTH; i++) {
            Long parentId = ReflectUtil.invoke(tbadindustryextend, "getParentid" + i);
            if (parentId != 0 && !adIndustryId.equals(parentId)) {
                parentIdList.add(parentId);
            }
        }
        return parentIdList;
    }

    /***
     * addRootNode
     * 给树列表增加根节点
     * @param adIndustryTreeM
     * @param sRootName
     * @return
     */
    private List<TreeModel> addRootNode(List<TreeModel> adIndustryTreeM, String sRootName) {
        for (TreeModel treeModel : adIndustryTreeM) {
            if (treeModel.getParentId() == null) {
                treeModel.setParentId(0L);
            }
        }
        //添加一个根节点
        TreeModel root = new TreeModel();
        root.setId(0L);
        root.setName("所有" + sRootName);
        root.setChecked(false);
        root.setNocheck(false);
        root.setBuse(true);
        root.setIconSkin(TreeModel.UNIT);
        adIndustryTreeM.add(root);
        return adIndustryTreeM;
    }

    @Override
    public List<Tbadindustry> getTbAdIndustryListForCJ() {
        return this.lambdaQuery()
                .eq(Tbadindustry::getBuse, true)
                .eq(Tbadindustry::getBclassified, true) //分类广告
                .orderByAsc(Tbadindustry::getSname)
                .list();
    }

    @Override
    public Json SaveSortAdSetForCJ(String id, String path, String filename) {
        Tbadindustry tbadindustry = this.getById(id);
        if (tbadindustry == null) {
            return Json.fail("未找到行业信息");
        }
        tbadindustry.setSfilepath(path);
        tbadindustry.setSfilename(filename);
        innerInterceptor.recoredLog();
        if (this.updateById(tbadindustry)) {
            return Json.success("保存成功");
        }
        return Json.fail("保存失败");
    }
}