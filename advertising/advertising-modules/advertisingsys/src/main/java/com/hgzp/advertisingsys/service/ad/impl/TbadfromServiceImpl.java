package com.hgzp.advertisingsys.service.ad.impl;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertisingsys.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertisingsys.service.ad.TbadfromServiceI;
import com.hgzp.advertisingsys.service.ad.TbadfromextendServiceI;
import com.hgzp.core.exception.DataExistException;
import com.hgzp.core.model.Tbadfrom;
import com.hgzp.core.model.Tbadfromextend;
import com.hgzp.core.page.TreeModel;
import com.hgzp.core.page.TreeQueryVo;
import com.hgzp.mapper.ad.TbadfromMapper;
import com.hgzp.mapper.ad.TbadfromextendMapper;
import com.hgzp.service.common.impl.MyServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 广告来源信息 服务实现类
 * </p>
 *
 * @author muyn
 * @since 2023-08-18
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TbadfromServiceImpl extends MyServiceImpl<TbadfromMapper, Tbadfrom> implements TbadfromServiceI {
    public static final int MAX_IDEPTH = 5;
    @Autowired
    private TbadfromMapper tbadfromMapper;
    @Autowired
    private TbadfromextendMapper tbadfromextendMapper;
    @Autowired
    private TbadfromextendServiceI adfromextendServiceI;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;

    @Override
    public void deleteAdFrom (String tbAdFromIds){
        String[] split = tbAdFromIds.split(",");
        LambdaQueryWrapper<Tbadfrom> lqw = Wrappers.lambdaQuery();
        lqw.in(Tbadfrom::getParentid,split);
        long adFromCount = tbadfromMapper.selectCount(lqw);
        if(adFromCount > 0) {
            throw new DataExistException("存在子级来源");
        }
        innerInterceptor.recoredLog();
        Arrays.stream(split).forEach(id->{
            tbadfromMapper.deleteById(id);
        });
    }

    /***
     * getAdFromPageList
     * 方法功能:  广告来源
     * @author muyn
     * @date 2023/8/17 10:04
     * @param page
     * @param queryInfo
     * @return Json
     */
    @Override
    public IPage<Tbadfrom> getAdFromPageList(Page<Tbadfrom> page, TreeQueryVo queryInfo) {
        LambdaQueryWrapper<Tbadfrom> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.hasText(queryInfo.getQueryKey()),Tbadfrom::getSname,queryInfo.getQueryKey());
        IPage<Tbadfrom> tbAdFromPage = tbadfromMapper.selectPage(page, lqw);
        return tbAdFromPage;

    }
    /**
     * getTbAdFromTree
     * 方法功能： 广告来源树
     * @author: muyn
     * @param queryInfo
     * @return
     */
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
    public List<Long> getQueryAdFromId(String querykey) {
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
    private List<Long> getParentAdFromId(Long adFromId){
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
    /***
     * saveAdFrom
     * 方法功能:  保存来源
     * @author muyn
     * @date 2023/8/17 10:04
     * @param tbAdFrom
     * @return void
     */
    @Override
    public void saveAdFrom(Tbadfrom tbAdFrom) {
        if(isExistAdFrom(tbAdFrom)){
            throw new DataExistException("已存在同名来源");
        }
        innerInterceptor.recoredLog();
        save(tbAdFrom);
        saveFromExtend(tbAdFrom);
    }

    /***
     * updateAdFrom
     * 方法功能:  更新来源
     * @author muyn
     * @date 2023/8/17 10:04
     * @param tbAdFrom
     * @return void
     */
    @Override
    public void updateAdFrom(Tbadfrom tbAdFrom) {
        if(isExistAdFrom(tbAdFrom)){
            throw new DataExistException("已存在同名来源");
        }
        innerInterceptor.recoredLog();
        updateById(tbAdFrom);
        saveFromExtend(tbAdFrom);
    }

    /***
     * isExistAdFrom
     * 方法功能:  是否存在同名的来源
     * @author muyn
     * @date 2023/8/17 10:04
     * @param tbAdFrom
     * @return boolean
     */
    private boolean isExistAdFrom(Tbadfrom tbAdFrom) {
        Long count = new LambdaQueryChainWrapper<>(tbadfromMapper)
                .eq(tbAdFrom.getParentid() != null, Tbadfrom::getParentid, tbAdFrom.getParentid())
                .eq(Tbadfrom::getSname, tbAdFrom.getSname())
                .ne(tbAdFrom.getId() != null, Tbadfrom::getId, tbAdFrom.getId())
                .count();
        return count > 0;
    }

    /***
     * saveFromExtend
     * 方法功能:  保存广告来源扩展
     * @author muyn
     * @date 2023/8/17 10:04
     * @param adFrom
     * @return Json
     */
    @Override
    public void saveFromExtend(Tbadfrom adFrom) {
        // 首先删除原来的扩展记录，再新增
        // 首先判断是否存在上级来源，如果没有，则直接插入扩展表中，如果有，则先查出上级来源，然后再将本级来源的ID写入到扩展表对象 中保存
        adfromextendServiceI.removeById(adFrom.getId());
        Tbadfromextend adFromExtend = new Tbadfromextend();
        if(adFrom.getParentid() == 0 || adFrom.getParentid() == null) {
            // 如果没有上级，直接写入1级扩展表
            adFromExtend.setId(adFrom.getId());
            adFromExtend.setAdfromid(adFrom.getId());
            adFromExtend.setParentid1(adFrom.getId());
            adFromExtend.setParentname1(adFrom.getSname());
        } else {
            // 如果存在上级，先把上级的扩展表查出来,将上级扩展表的信息复制到新的扩展表，然后将新扩展表的ID改成新的，然后判断本级深度，再把本级的id与name赋值
            Tbadfromextend adFromExtendParent = adfromextendServiceI.getById(adFrom.getParentid());
            BeanUtils.copyProperties(adFromExtendParent,adFromExtend);
            adFromExtend.setId(adFrom.getId());
            adFromExtend.setAdfromid(adFrom.getId());
            switch (adFrom.getIdepth()) {
                case 2:
                    adFromExtend.setParentid2(adFrom.getId());
                    adFromExtend.setParentname2(adFrom.getSname());
                    break;
                case 3:
                    adFromExtend.setParentid3(adFrom.getId());
                    adFromExtend.setParentname3(adFrom.getSname());
                    break;
                case 4:
                    adFromExtend.setParentid4(adFrom.getId());
                    adFromExtend.setParentname4(adFrom.getSname());
                    break;
                case 5:
                    adFromExtend.setParentid5(adFrom.getId());
                    adFromExtend.setParentname5(adFrom.getSname());
                    break;
                default:
                    break;
            }
        }
        // 修改扩展表中所有父级为此来源的名称
        UpdateWrapper<Tbadfromextend> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("parentname" + adFrom.getIdepth(), adFrom.getSname()).eq("parentid" + adFrom.getIdepth(), adFrom.getId());
        tbadfromextendMapper.update(null, updateWrapper);
        innerInterceptor.recoredLog();
        adfromextendServiceI.save(adFromExtend);
    }
    /**
     * resetAdFromExtend
     * 方法功能： 重置来源扩展表
     * @author: muyn
     * @return void
     */
    @Override
    public void resetAdFromExtend() {
        tbadfromextendMapper.delete(null);
        List<Tbadfrom> tbAdFromList = tbadfromMapper.selectList(null);
        Map<Integer, List<Tbadfrom>> listMap = tbAdFromList.stream().collect(Collectors.groupingBy(Tbadfrom::getIdepth));
        List<Tbadfromextend> resultList = new ArrayList<>();
        for (Map.Entry<Integer, List<Tbadfrom>> entry : listMap.entrySet()) {
            Integer idepth = entry.getKey();
            List<Tbadfrom> tbAdFromIdepthList = entry.getValue();
            for (Tbadfrom tbadfrom : tbAdFromIdepthList) {
                Tbadfromextend tbadfromextend = new Tbadfromextend();
                tbadfromextend.setId(tbadfrom.getId());
                tbadfromextend.setAdfromid(tbadfrom.getId());
                if (idepth == 1) {
                    tbadfromextend.setParentid1(tbadfrom.getId());
                    tbadfromextend.setParentname1(tbadfrom.getSname());
                }
                if (idepth == 2) {
                    Tbadfrom tbadfrom1 = listMap.get(1).stream().filter(d -> d.getId().equals(tbadfrom.getParentid())).findFirst().get();
                    tbadfromextend.setParentid1(tbadfrom.getParentid());
                    tbadfromextend.setParentname1(tbadfrom1.getSname());
                    tbadfromextend.setParentid2(tbadfrom.getId());
                    tbadfromextend.setParentname2(tbadfrom.getSname());
                }
                if (idepth == 3) {
                    Tbadfrom tbadfrom2 = listMap.get(2).stream().filter(d -> d.getId().equals(tbadfrom.getParentid())).findFirst().get();
                    Tbadfrom tbadfrom1 = listMap.get(1).stream().filter(d -> d.getId().equals(tbadfrom2.getParentid())).findFirst().get();
                    tbadfromextend.setParentid1(tbadfrom2.getParentid());
                    tbadfromextend.setParentname1(tbadfrom1.getSname());
                    tbadfromextend.setParentid2(tbadfrom.getParentid());
                    tbadfromextend.setParentname2(tbadfrom2.getSname());
                    tbadfromextend.setParentid3(tbadfrom.getId());
                    tbadfromextend.setParentname3(tbadfrom.getSname());
                }
                if (idepth == 4) {
                    Tbadfrom tbadfrom3 = listMap.get(3).stream().filter(d -> d.getId().equals(tbadfrom.getParentid())).findFirst().get();
                    Tbadfrom tbadfrom2 = listMap.get(2).stream().filter(d -> d.getId().equals(tbadfrom3.getParentid())).findFirst().get();
                    Tbadfrom tbadfrom1 = listMap.get(1).stream().filter(d -> d.getId().equals(tbadfrom2.getParentid())).findFirst().get();

                    tbadfromextend.setParentid1(tbadfrom2.getParentid());
                    tbadfromextend.setParentname1(tbadfrom1.getSname());
                    tbadfromextend.setParentid2(tbadfrom3.getParentid());
                    tbadfromextend.setParentname2(tbadfrom2.getSname());
                    tbadfromextend.setParentid3(tbadfrom.getParentid());
                    tbadfromextend.setParentname3(tbadfrom3.getSname());
                    tbadfromextend.setParentid4(tbadfrom.getId());
                    tbadfromextend.setParentname4(tbadfrom.getSname());
                }
                if (idepth == 5) {
                    Tbadfrom tbadfrom4 = listMap.get(4).stream().filter(d -> d.getId().equals(tbadfrom.getParentid())).findFirst().get();
                    Tbadfrom tbadfrom3 = listMap.get(3).stream().filter(d -> d.getId().equals(tbadfrom4.getParentid())).findFirst().get();
                    Tbadfrom tbadfrom2 = listMap.get(2).stream().filter(d -> d.getId().equals(tbadfrom3.getParentid())).findFirst().get();
                    Tbadfrom tbadfrom1 = listMap.get(1).stream().filter(d -> d.getId().equals(tbadfrom2.getParentid())).findFirst().get();

                    tbadfromextend.setParentid1(tbadfrom2.getParentid());
                    tbadfromextend.setParentname1(tbadfrom1.getSname());
                    tbadfromextend.setParentid2(tbadfrom3.getParentid());
                    tbadfromextend.setParentname2(tbadfrom2.getSname());
                    tbadfromextend.setParentid3(tbadfrom4.getParentid());
                    tbadfromextend.setParentname3(tbadfrom3.getSname());
                    tbadfromextend.setParentid4(tbadfrom.getParentid());
                    tbadfromextend.setParentname4(tbadfrom4.getSname());
                    tbadfromextend.setParentid5(tbadfrom.getId());
                    tbadfromextend.setParentname5(tbadfrom.getSname());
                }
                resultList.add(tbadfromextend);
            }
        }
        adfromextendServiceI.saveBatch(resultList);
    }
}
