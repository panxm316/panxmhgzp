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
import com.hgzp.advertisingsys.pagemodel.ad.vo.AdindustryVO;
import com.hgzp.advertisingsys.service.ad.TbadindustryServiceI;
import com.hgzp.advertisingsys.service.ad.TbadindustryextendServiceI;
import com.hgzp.core.exception.DataExistException;
import com.hgzp.core.model.Tbadindustry;
import com.hgzp.core.model.Tbadindustryextend;
import com.hgzp.core.model.Twadproject;
import com.hgzp.core.page.TreeModel;
import com.hgzp.core.page.TreeQueryVo;
import com.hgzp.mapper.ad.TbadindustryMapper;
import com.hgzp.mapper.ad.TbadindustryextendMapper;
import com.hgzp.service.common.impl.MyServiceImpl;
import com.hgzp.utils.file.FileUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
 * 广告行业信息 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TbadindustryServiceImpl extends MyServiceImpl<TbadindustryMapper, Tbadindustry> implements TbadindustryServiceI {
    public static final int MAX_IDEPTH = 5;
    @Autowired
    private TbadindustryMapper tbadindustryMapper;
    @Autowired
    private TbadindustryextendMapper tbadindustryextendMapper;
    @Autowired
    private TbadindustryextendServiceI adindustryextendServiceI;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;

    @Value("${ufile.uWebURL}")
    private String uWebURL;

    @Override
    public void deleteAdIndustry (String tbAdIndustryIds){
        String[] split = tbAdIndustryIds.split(",");
        LambdaQueryWrapper<Tbadindustry> lqw = Wrappers.lambdaQuery();
        lqw.in(Tbadindustry::getParentid,split);
        long adIndustryCount = tbadindustryMapper.selectCount(lqw);
        if(adIndustryCount > 0) {
            throw new DataExistException("存在子级行业");
        }
        Arrays.stream(split).forEach(id->{
            tbadindustryMapper.deleteById(id);
        });
    }

    /***
     * getAdIndustryPageList
     * 方法功能:  广告行业
     * @author muyn
     * @date 2023/8/17 10:04
     * @param page
     * @param queryInfo
     * @return Json
     */
    @Override
    public IPage<AdindustryVO> getAdIndustryPageList(Page<Tbadindustry> page, TreeQueryVo queryInfo) {
        LambdaQueryWrapper<Tbadindustry> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.hasText(queryInfo.getQueryKey()),Tbadindustry::getSname,queryInfo.getQueryKey());
        IPage<Tbadindustry> tbAdIndustryPage = tbadindustryMapper.selectPage(page, lqw);

        IPage<AdindustryVO> adIndustryPageVo = new Page<>();
        BeanUtils.copyProperties(tbAdIndustryPage,adIndustryPageVo);
        List<AdindustryVO> adIndustryVOList = tbAdIndustryPage.getRecords().stream().map(tbAdIndustry -> {
            AdindustryVO adindustryVO = new AdindustryVO();
            BeanUtils.copyProperties(tbAdIndustry,adindustryVO);
            if(StrUtil.isNotBlank(adindustryVO.getPicturepath())){
                String[] sfileNames = adindustryVO.getPicturepath().replace(",",";").split(";");
                if(sfileNames.length > 0) {
                    String sUrlStr="";
                    for(String sFileName:sfileNames){
                        sUrlStr+=";"+uWebURL + sFileName.substring(0, sFileName.lastIndexOf("."));
                    }
                    if(sUrlStr.length()>0){
                        sUrlStr=sUrlStr.substring(1);
                    }
                    adindustryVO.setSdurl(sUrlStr);
                }
            }
            return adindustryVO;
        }).collect(Collectors.toList());

        adIndustryPageVo.setRecords(adIndustryVOList);
        adIndustryPageVo.setTotal(tbAdIndustryPage.getTotal());

        return adIndustryPageVo;
    }

    @Override
    public AdindustryVO getTbAdIndustryById(String id) {
        AdindustryVO adindustryVO = new AdindustryVO();
        Tbadindustry tbAdIndustry = tbadindustryMapper.selectById(id);
        BeanUtils.copyProperties(tbAdIndustry,adindustryVO);
        if(StrUtil.isNotBlank(adindustryVO.getPicturepath())){
            String[] sfileNames = adindustryVO.getPicturepath().replace(",",";").split(";");
            if(sfileNames.length > 0) {
                String sUrlStr="";
                for(String sFileName:sfileNames){
                    sUrlStr+=";"+uWebURL + sFileName.substring(0, sFileName.lastIndexOf("."));
                }
                if(sUrlStr.length()>0){
                    sUrlStr=sUrlStr.substring(1);
                }
                String fileName = sfileNames[0];                ;
                adindustryVO.setSdurl(sUrlStr);
            }
        }

        return adindustryVO;
    }

    /**
     * getTbAdIndustryTree
     * 方法功能： 广告行业树
     * @author: muyn
     * @param queryInfo
     * @return
     */
    @Override
    public List<TreeModel> getTbAdIndustryTree(TreeQueryVo queryInfo) {
        //根据关键字查询出符合的行业id
        List<Long> queryAdIndustryId = new ArrayList<>();
        if (StrUtil.isNotBlank(queryInfo.getQueryKey())) {
            queryAdIndustryId = getQueryAdIndustryId(queryInfo.getQueryKey());
            queryAdIndustryId.add(0L);
        }

        // 根据ID列表查出行业数据
        List<Tbadindustry> adIndustryList =  this.lambdaQuery()
                .in(queryAdIndustryId.size() > 0, Tbadindustry::getId, queryAdIndustryId)
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
        return queryInfo.isShowRoot() ? addRootNode(adIndustryTreeM,queryInfo.getRootName()) : adIndustryTreeM;
    }

    /***
     * addRootNode
     * 给树列表增加根节点
     * @param adIndustryTreeM
     * @param sRootName
     * @return
     */
    private  List<TreeModel> addRootNode(List<TreeModel> adIndustryTreeM, String sRootName) {
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
     * @author muyn
     * @date 2023/9/1 14:31
     * @param adIndustryId
     * @return java.util.List<java.lang.Long>
     */
    private List<Long> getParentAdIndustryId(Long adIndustryId){
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
     * saveAdIndustry
     * 方法功能:  保存行业
     * @author muyn
     * @date 2023/8/17 10:04
     * @param tbAdIndustry
     * @return void
     */
    @Override
    public void saveAdIndustry(Tbadindustry tbAdIndustry) {
        if(isExistAdIndustry(tbAdIndustry)){
            throw new DataExistException("已存在同名行业");
        }
        innerInterceptor.recoredLog();
        save(tbAdIndustry);
        saveIndustryExtend(tbAdIndustry);
    }

    /***
     * updateAdIndustry
     * 方法功能:  更新行业
     * @author muyn
     * @date 2023/8/17 10:04
     * @param tbAdIndustry
     * @return void
     */
    @Override
    public void updateAdIndustry(Tbadindustry tbAdIndustry) {
        if(isExistAdIndustry(tbAdIndustry)){
            throw new DataExistException("已存在同名行业");
        }
        innerInterceptor.recoredLog();
        updateById(tbAdIndustry);
        saveIndustryExtend(tbAdIndustry);
    }

    /***
     * isExistAdIndustry
     * 方法功能:  是否存在同名的行业
     * @author muyn
     * @date 2023/8/17 10:04
     * @param tbAdIndustry
     * @return boolean
     */
    private boolean isExistAdIndustry(Tbadindustry tbAdIndustry) {
        Long count = new LambdaQueryChainWrapper<>(tbadindustryMapper)
                .eq(tbAdIndustry.getParentid() != null, Tbadindustry::getParentid, tbAdIndustry.getParentid())
                .eq(Tbadindustry::getSname, tbAdIndustry.getSname())
                .ne(tbAdIndustry.getId() != null, Tbadindustry::getId, tbAdIndustry.getId())
                .count();
        return count > 0;
    }

    /***
     * saveIndustryExtend
     * 方法功能:  保存广告行业扩展
     * @author muyn
     * @date 2023/8/17 10:04
     * @param adIndustry
     * @return Json
     */
    @Override
    public void saveIndustryExtend(Tbadindustry adIndustry) {
        // 首先删除原来的扩展记录，再新增
        // 首先判断是否存在上级行业，如果没有，则直接插入扩展表中，如果有，则先查出上级行业，然后再将本级行业的ID写入到扩展表对象 中保存
        adindustryextendServiceI.removeById(adIndustry.getId());
        Tbadindustryextend adIndustryExtend = new Tbadindustryextend();
        if(adIndustry.getParentid() == 0|| adIndustry.getParentid() == null) {
            // 如果没有上级，直接写入1级扩展表
            adIndustryExtend.setId(adIndustry.getId());
            adIndustryExtend.setTiadindustryid(adIndustry.getId());
            adIndustryExtend.setParentid1(adIndustry.getId());
            adIndustryExtend.setParentname1(adIndustry.getSname());
        } else {
            // 如果存在上级，先把上级的扩展表查出来,将上级扩展表的信息复制到新的扩展表，然后将新扩展表的ID改成新的，然后判断本级深度，再把本级的id与name赋值
            Tbadindustryextend adIndustryExtendParent = adindustryextendServiceI.getById(adIndustry.getParentid());
            BeanUtils.copyProperties(adIndustryExtendParent,adIndustryExtend);
            adIndustryExtend.setId(adIndustry.getId());
            adIndustryExtend.setTiadindustryid(adIndustry.getId());
            switch (adIndustry.getIdepth()) {
                case 2:
                    adIndustryExtend.setParentid2(adIndustry.getId());
                    adIndustryExtend.setParentname2(adIndustry.getSname());
                    break;
                case 3:
                    adIndustryExtend.setParentid3(adIndustry.getId());
                    adIndustryExtend.setParentname3(adIndustry.getSname());
                    break;
                case 4:
                    adIndustryExtend.setParentid4(adIndustry.getId());
                    adIndustryExtend.setParentname4(adIndustry.getSname());
                    break;
                case 5:
                    adIndustryExtend.setParentid5(adIndustry.getId());
                    adIndustryExtend.setParentname5(adIndustry.getSname());
                    break;
                default:
                    break;
            }
        }
        // 修改扩展表中所有父级为此行业的名称
        UpdateWrapper<Tbadindustryextend> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("parentname" + adIndustry.getIdepth(), adIndustry.getSname()).eq("parentid" + adIndustry.getIdepth(), adIndustry.getId());
        tbadindustryextendMapper.update(null,updateWrapper);
        adindustryextendServiceI.save(adIndustryExtend);
    }


    /**
     * resetAdIndustryExtend
     * 方法功能： 重置行业扩展表
     * @author: muyn
     * @return void
     */
    @Override
    public void resetAdIndustryExtend() {
        tbadindustryextendMapper.delete(null);
        List<Tbadindustry> tbAdIndustryList = tbadindustryMapper.selectList(null);
        Map<Integer, List<Tbadindustry>> listMap = tbAdIndustryList.stream().collect(Collectors.groupingBy(Tbadindustry::getIdepth));
        List<Tbadindustryextend> resultList = new ArrayList<>();
        for (Map.Entry<Integer, List<Tbadindustry>> entry : listMap.entrySet()) {
            Integer idepth = entry.getKey();
            List<Tbadindustry> tbAdIndustryIdepthList = entry.getValue();
            for (Tbadindustry tbadindustry : tbAdIndustryIdepthList) {
                Tbadindustryextend tbadindustryextend = new Tbadindustryextend();
                tbadindustryextend.setId(tbadindustry.getId());
                tbadindustryextend.setTiadindustryid(tbadindustry.getId());
                if (idepth == 1) {
                    tbadindustryextend.setParentid1(tbadindustry.getId());
                    tbadindustryextend.setParentname1(tbadindustry.getSname());
                }
                if (idepth == 2) {
                    Tbadindustry tbadindustry1 = listMap.get(1).stream().filter(d -> d.getId().equals(tbadindustry.getParentid())).findFirst().get();
                    tbadindustryextend.setParentid1(tbadindustry.getParentid());
                    tbadindustryextend.setParentname1(tbadindustry1.getSname());
                    tbadindustryextend.setParentid2(tbadindustry.getId());
                    tbadindustryextend.setParentname2(tbadindustry.getSname());
                }
                if (idepth == 3) {
                    Tbadindustry tbadindustry2 = listMap.get(2).stream().filter(d -> d.getId().equals(tbadindustry.getParentid())).findFirst().get();
                    Tbadindustry tbadindustry1 = listMap.get(1).stream().filter(d -> d.getId().equals(tbadindustry2.getParentid())).findFirst().get();
                    tbadindustryextend.setParentid1(tbadindustry2.getParentid());
                    tbadindustryextend.setParentname1(tbadindustry1.getSname());
                    tbadindustryextend.setParentid2(tbadindustry.getParentid());
                    tbadindustryextend.setParentname2(tbadindustry2.getSname());
                    tbadindustryextend.setParentid3(tbadindustry.getId());
                    tbadindustryextend.setParentname3(tbadindustry.getSname());
                }
                if (idepth == 4) {
                    Tbadindustry tbadindustry3 = listMap.get(3).stream().filter(d -> d.getId().equals(tbadindustry.getParentid())).findFirst().get();
                    Tbadindustry tbadindustry2 = listMap.get(2).stream().filter(d -> d.getId().equals(tbadindustry3.getParentid())).findFirst().get();
                    Tbadindustry tbadindustry1 = listMap.get(1).stream().filter(d -> d.getId().equals(tbadindustry2.getParentid())).findFirst().get();

                    tbadindustryextend.setParentid1(tbadindustry2.getParentid());
                    tbadindustryextend.setParentname1(tbadindustry1.getSname());
                    tbadindustryextend.setParentid2(tbadindustry3.getParentid());
                    tbadindustryextend.setParentname2(tbadindustry2.getSname());
                    tbadindustryextend.setParentid3(tbadindustry.getParentid());
                    tbadindustryextend.setParentname3(tbadindustry3.getSname());
                    tbadindustryextend.setParentid4(tbadindustry.getId());
                    tbadindustryextend.setParentname4(tbadindustry.getSname());
                }
                if (idepth == 5) {
                    Tbadindustry tbadindustry4 = listMap.get(4).stream().filter(d -> d.getId().equals(tbadindustry.getParentid())).findFirst().get();
                    Tbadindustry tbadindustry3 = listMap.get(3).stream().filter(d -> d.getId().equals(tbadindustry4.getParentid())).findFirst().get();
                    Tbadindustry tbadindustry2 = listMap.get(2).stream().filter(d -> d.getId().equals(tbadindustry3.getParentid())).findFirst().get();
                    Tbadindustry tbadindustry1 = listMap.get(1).stream().filter(d -> d.getId().equals(tbadindustry2.getParentid())).findFirst().get();

                    tbadindustryextend.setParentid1(tbadindustry2.getParentid());
                    tbadindustryextend.setParentname1(tbadindustry1.getSname());
                    tbadindustryextend.setParentid2(tbadindustry3.getParentid());
                    tbadindustryextend.setParentname2(tbadindustry2.getSname());
                    tbadindustryextend.setParentid3(tbadindustry4.getParentid());
                    tbadindustryextend.setParentname3(tbadindustry3.getSname());
                    tbadindustryextend.setParentid4(tbadindustry.getParentid());
                    tbadindustryextend.setParentname4(tbadindustry4.getSname());
                    tbadindustryextend.setParentid5(tbadindustry.getId());
                    tbadindustryextend.setParentname5(tbadindustry.getSname());
                }
                resultList.add(tbadindustryextend);
            }
        }
        adindustryextendServiceI.saveBatch(resultList);
    }
}
