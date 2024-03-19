package com.hgzp.advertising.service.ad.impl;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hgzp.advertising.service.ad.TbadfromServiceI;
import com.hgzp.core.model.Tbadfrom;
import com.hgzp.core.model.Tbadfromextend;
import com.hgzp.core.page.TreeModel;
import com.hgzp.core.page.TreeQueryVo;
import com.hgzp.mapper.ad.TbadfromMapper;
import com.hgzp.mapper.ad.TbadfromextendMapper;
import com.hgzp.service.common.impl.MyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 TbadfromServiceImpl
 创建人：songly
 类描述：广告来源
 创建日期：2024/3/7 13:36
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class TbadfromServiceImpl extends MyServiceImpl<TbadfromMapper, Tbadfrom> implements TbadfromServiceI {

    public static final int MAX_IDEPTH = 5;
    @Autowired
    private TbadfromextendMapper tbadfromextendMapper;
    @Autowired
    private TbadfromMapper tbadfromMapper;

    @Override
    public List<TreeModel> getTbAdFromTree(TreeQueryVo queryInfo) {
        //根据关键字查询出符合的来源id
        List<Long> queryAdFromId = new ArrayList<>();
        if (StrUtil.isNotBlank(queryInfo.getQueryKey())) {
            queryAdFromId = getQueryAdFromId(queryInfo.getQueryKey());
            queryAdFromId.add(0L);
        }
        // 根据ID列表查出部门数据
        List<Tbadfrom> adFromList =  this.lambdaQuery()
                .in(queryAdFromId.size() > 0, Tbadfrom::getId, queryAdFromId)
                .orderByAsc(Tbadfrom::getSname)
                .list();
        //数据库类型转为展示模型
        List<TreeModel> adFromTreeM = adFromList
                .stream()
                .distinct()
                .map(node -> {
                    TreeModel treeM = new TreeModel();
                    treeM.setId(node.getId());
                    treeM.setName(node.getSname());
                    treeM.setParentId(node.getParentid());
                    treeM.setBuse(node.getBuse());
                    return treeM;
                })
                .collect(Collectors.toList());
        return queryInfo.isShowRoot() ? addRootNode(adFromTreeM,queryInfo.getRootName()) : adFromTreeM;
    }
    /***
     * addRootNode
     * 给树列表增加根节点
     * @param adFromTreeM
     * @param sRootName
     * @return
     */
    private  List<TreeModel> addRootNode(List<TreeModel> adFromTreeM, String sRootName) {
        for (TreeModel treeModel : adFromTreeM) {
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
        adFromTreeM.add(root);
        return adFromTreeM;
    }
    /**
     * getQueryAdFromId
     * 方法功能:  通过关键字查询时查询出所有符合条件的节点以及父节点id
     *
     * @param querykey
     * @return java.util.List<java.lang.Long>
     * @author muyn
     * @date 2023/9/1 13:03
     */
    private List<Long> getQueryAdFromId(String querykey) {
        List<Long> resultIdList = new ArrayList<>();
        List<Long> idList = this.lambdaQuery()
                .like(Tbadfrom::getSname, querykey)
                .list()
                .stream()
                .map(Tbadfrom::getId)
                .collect(Collectors.toList());

        for (Long id : idList) {
            List<Long> parentDeptId = getParentAdFromId(id);
            resultIdList.addAll(parentDeptId);
            resultIdList.add(id);
        }

        return resultIdList.stream().distinct().collect(Collectors.toList());
    }

    /**
     * getParentDeptId
     * 方法功能:  获取某个部门的父部门id，不包含当前节点
     * @author muyn
     * @date 2023/9/1 14:31
     * @param adFromId
     * @return java.util.List<java.lang.Long>
     */
    @Override
    public List<Long> getParentAdFromId(Long adFromId){
        Tbadfromextend tbadfromextend = tbadfromextendMapper.selectById(adFromId);
        List<Long> parentIdList = new ArrayList<>();
        for (int i = 1; i <= MAX_IDEPTH; i++) {
            Long parentId = ReflectUtil.invoke(tbadfromextend, "getParentid" + i);
            if (parentId != 0 && !adFromId.equals(parentId)) {
                parentIdList.add(parentId);
            }
        }
        return parentIdList;
    }

    @Override
    public List<Long> getChildAdFromId(Long adFromId) {
        List<Long> childIdList = new ArrayList<>();
        Tbadfrom tbadfrom = tbadfromMapper.selectById(adFromId);
        LambdaQueryWrapper<Tbadfrom> lqw = Wrappers.lambdaQuery();
        lqw.eq(Tbadfrom::getBuse, true);
        lqw.gt(Tbadfrom::getIdepth,tbadfrom.getIdepth());
        List<Tbadfrom> list = tbadfromMapper.selectList(lqw);
        if( null == list && list.size() <= 0 ) {
            return  childIdList;
        }
        for( int i = 0; i < list.size(); i++  ) {
            Tbadfrom tempadFrom = list.get(i);
            List<Long> tempList =  getParentAdFromId(tempadFrom.getId());
            if (tempList.contains(tbadfrom.getId())) {
                childIdList.add(tempadFrom.getId());
            }
        }
        return childIdList;
    }

}