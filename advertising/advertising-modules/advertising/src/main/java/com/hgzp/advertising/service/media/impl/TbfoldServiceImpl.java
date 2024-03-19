package com.hgzp.advertising.service.media.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.pagemodel.media.dto.FoldDTO;
import com.hgzp.advertising.pagemodel.media.vo.FoldVO;
import com.hgzp.advertising.service.media.TbfoldServiceI;
import com.hgzp.core.exception.DataExistException;
import com.hgzp.core.model.*;
import com.hgzp.core.page.DataCombo;
import com.hgzp.core.page.TreeModel;
import com.hgzp.mapper.media.*;
import com.hgzp.service.common.impl.MyServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 叠次信息 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TbfoldServiceImpl extends MyServiceImpl<TbfoldMapper, Tbfold> implements TbfoldServiceI {
    @Autowired
    private TbfoldMapper tbfoldMapper;
    @Autowired
    private TbmediaMapper tbmediaMapper;
    @Autowired
    private TbmediatypeMapper tbmediatypeMapper;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;
    @Autowired
    private TbfoldareaverMapper tbfoldareaverMapper;
    @Autowired
    TbpagesortMapper tbpagesortMapper;

    @Override
    public Page<FoldVO> getFoldPageList(Page<Tbfold> page, FoldVO query) throws Exception {
        Page<FoldVO> reslutPage = new Page<>();
        List<Tbmedia> tbmediaList = new LambdaQueryChainWrapper<>(tbmediaMapper)
                .eq(Tbmedia::getBuse, true)
                .eq(StringUtils.hasText(query.getMediatypename()), Tbmedia::getMediatypekey, query.getMediatypename())
                .eq(query.getMediaid() != null, Tbmedia::getId, query.getMediaid())
                .orderByAsc(Tbmedia::getIsort)
                .list();
        List<Long> mediaIdList = tbmediaList
                .stream()
                .map(Tbmedia::getId)
                .distinct()
                .collect(Collectors.toList());
        Page<Tbfold> tbfoldPage = this.lambdaQuery()
                .in(mediaIdList.size() > 0, Tbfold::getMediaid, mediaIdList)
                .like(StringUtils.hasText(query.getQueryKey()), Tbfold::getSname, query.getQueryKey())
                .page(page);
        if (tbfoldPage.getTotal() == 0) {
            return reslutPage;
        }
        List<FoldVO> result = new ArrayList<>();
        for (Tbfold record : tbfoldPage.getRecords()) {
            FoldVO foldModel = new FoldVO(record);
            Tbmedia tbmedia = tbmediaMapper.selectById(record.getMediaid());
            foldModel.setMedianame(tbmedia.getSname());
            result.add(foldModel);
        }
        reslutPage.setRecords(result);
        reslutPage.setTotal(tbfoldPage.getTotal());
        return reslutPage;
    }

    @Override
    public List<TreeModel> getMediaFoldTree(String mediaType, String getType) throws Exception {
        String[] mediakeys = mediaType.split(",");
        //查询全部媒体类型
        List<Tbmediatype> tbmediatypeList = new LambdaQueryChainWrapper<>(tbmediatypeMapper)
                .eq(Tbmediatype::getBuse, true)
                .in(StringUtils.hasText(mediaType), Tbmediatype::getSkey, mediakeys)
                .orderByAsc(Tbmediatype::getIsort)
                .list();
        List<TreeModel> treeModelList = mediatypeConvertTreeModel(tbmediatypeList);
        if (treeModelList.size() == 0) {
            return treeModelList;
        }
        List<String> typeList = tbmediatypeList
                .stream()
                .map(Tbmediatype::getSkey)
                .distinct()
                .collect(Collectors.toList());
        //查询全部媒体
        List<Tbmedia> tbmediaList = new LambdaQueryChainWrapper<>(tbmediaMapper)
                .eq(Tbmedia::getBuse, true)
                .in(Tbmedia::getMediatypekey, typeList)
                .orderByAsc(Tbmedia::getIsort)
                .list();
        if (tbmediaList.size() == 0) {
            return treeModelList;
        }
        List<TreeModel> mediaTreeModelList = mediaConvertTreeModel(tbmediaList);
        treeModelList.addAll(mediaTreeModelList);
        if (!StringUtils.hasText(getType) || !"Media".equals(getType)) {
            List<Long> mediaIdList = tbmediaList
                    .stream()
                    .map(Tbmedia::getId)
                    .distinct()
                    .collect(Collectors.toList());
            //查询全部叠次
            List<Tbfold> tbfoldList = new LambdaQueryChainWrapper<>(tbfoldMapper)
                    .eq(Tbfold::getBuse, true)
                    .in(Tbfold::getMediaid, mediaIdList)
                    .orderByAsc(Tbfold::getIsort)
                    .list();

            if (tbfoldList.size() > 0) {
                List<TreeModel> foldTreeModelList = foldConvertTreeModel(tbfoldList);
                treeModelList.addAll(foldTreeModelList);
            }
        }
        return treeModelList;
    }

    private void treeIconSkinMediaType(TreeModel tree, String type) {
        //Title根据媒体类型显示图标
        switch (type) {
            case "微信":
                tree.setIconSkin("weixin");
                break;
            case "微博":
                tree.setIconSkin("weibo");
                break;
            case "客户端":
                tree.setIconSkin("phone");
                break;
            case "报刊":
                tree.setIconSkin("news");
                break;
            case "网站":
                tree.setIconSkin("website");
                break;
            case "电台":
                tree.setIconSkin("diantai");
                break;
            case "电视台":
                tree.setIconSkin("tv");
                break;
            case "头条号":
                tree.setIconSkin("toutiao");
                break;
            case "抖音":
                tree.setIconSkin("douyin");
                break;
            case "百家号":
                tree.setIconSkin("baijh");
                break;
            case "网易号":
                tree.setIconSkin("wangyi");
                break;
            case "一点号":
                tree.setIconSkin("yidian");
                break;
            default:
                tree.setIconSkin("catagory");
                break;
        }
    }

    /**
     * mediatypeConvertTreeModel
     * 方法功能:  将数据库媒体类型对象转换成树对象
     *
     * @param tbmediatypeList
     * @return java.util.List<com.hgzp.core.page.TreeModel>
     * @author suny
     * @date 2023/9/22 15:03
     */
    private List<TreeModel> mediatypeConvertTreeModel(List<Tbmediatype> tbmediatypeList) {
        List<TreeModel> treeModelList = new ArrayList<>();
        for (Tbmediatype tbmediatype : tbmediatypeList) {
            TreeModel treeModel = new TreeModel();
            treeModel.setId(tbmediatype.getId());
            treeModel.setSkey(tbmediatype.getSkey());
            treeModel.setParentId(Long.parseLong("0"));
            treeModel.setName(tbmediatype.getSname());
            treeModel.setStype("mediatype");
            treeIconSkinMediaType(treeModel, tbmediatype.getSname());
            treeModelList.add(treeModel);
        }
        return treeModelList;
    }

    /**
     * mediaConvertTreeModel
     * 方法功能:  将数据库媒体对象转换成嵌套的树对象
     *
     * @param tbmediaList
     * @return java.util.List<com.hgzp.core.page.TreeModel>
     * @author suny
     * @date 2023/8/23 14:32
     */
    private List<TreeModel> mediaConvertTreeModel(List<Tbmedia> tbmediaList) {
        List<TreeModel> treeModelList = new ArrayList<>();
        for (Tbmedia tbmedia : tbmediaList) {
            LambdaQueryWrapper<Tbmediatype> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(StringUtils.hasText(tbmedia.getMediatypekey()), Tbmediatype::getSname, tbmedia.getMediatypename());

            Tbmediatype tbmediatype = tbmediatypeMapper.selectOne(wrapper);
            TreeModel treeModel = new TreeModel();
            treeModel.setId(tbmedia.getId());
            treeModel.setParentId(tbmediatype.getId());
            treeModel.setName(tbmedia.getSname());
            treeModel.setStype("media");
            treeIconSkinMediaType(treeModel, tbmediatype.getSname());
            treeModelList.add(treeModel);
        }
        return treeModelList;
    }

    /**
     * foldConvertTreeModel
     * 方法功能: 将数据库叠次对象转换成嵌套的树对象
     *
     * @param tbfoldList
     * @return java.util.List<com.hgzp.core.page.TreeModel>
     * @author suny
     * @date 2023/8/23 14:32
     */
    private List<TreeModel> foldConvertTreeModel(List<Tbfold> tbfoldList) {
        List<TreeModel> treeModelList = new ArrayList<>();
        for (Tbfold tbfold : tbfoldList) {
            TreeModel treeModel = new TreeModel();
            treeModel.setId(tbfold.getId());
            treeModel.setParentId(tbfold.getMediaid());
            treeModel.setName(tbfold.getSname());
            treeModel.setIconSkin("cata");
            treeModel.setStype("fold");
            treeModelList.add(treeModel);
        }
        return treeModelList;
    }

    @Override
    public void saveFold(FoldDTO fold) {
        if (isExistFold(fold)) {
            throw new DataExistException("已存在同名叠次");
        }
        fold.setId(IdUtil.getSnowflakeNextId());

        Tbfold tbfold = new Tbfold();
        BeanUtils.copyProperties(fold, tbfold);

        innerInterceptor.recoredLog();
        tbfoldMapper.insert(tbfold);
    }

    @Override
    public void updateFold(FoldDTO fold) {
        if (isExistFold(fold)) {
            throw new DataExistException("已存在同名叠次");
        }
        Tbfold tbfold = new Tbfold();
        BeanUtils.copyProperties(fold, tbfold);
        innerInterceptor.recoredLog();
        tbfoldMapper.updateById(tbfold);
    }

    public boolean isExistFold(FoldDTO tbfold) {
        Long count = new LambdaQueryChainWrapper<>(tbfoldMapper)
                .eq(tbfold.getMediaid() != null, Tbfold::getMediaid, tbfold.getMediaid())
                .eq(Tbfold::getSname, tbfold.getSname())
                .ne(tbfold.getId() != null, Tbfold::getId, tbfold.getId())
                .count();
        return count > 0;
    }

    @Override
    public List<DataCombo> getFoldCombo(String mediaid) {
        List<Tbfold> foldList = this.lambdaQuery()
                .eq(Tbfold::getBuse, true)
                .and(StringUtils.hasText(mediaid), m -> {
                    m.eq(Tbfold::getMediaid, mediaid);
                })
                .orderByAsc(Tbfold::getIsort)
                .list();
        List<DataCombo> comboList = foldList.stream()
                .map(item -> new DataCombo(item.getId().toString(), item.getSname()))
                .collect(Collectors.toList());
        return comboList;
    }

    @Override
    public Map<Long, List<Tbfold>> getTbfoldListGroupByFoldId(List<Long> foldIdList) {
        List<Tbfold> tbfoldList = this.lambdaQuery()
                .in(Tbfold::getId, foldIdList)
                .list();
        Map<Long, List<Tbfold>> tbfoldMap = tbfoldList.stream()
                .collect(Collectors.groupingBy(Tbfold::getId));
        return tbfoldMap;
    }

    @Override
    public String getFoldChild(String ids) {
        String[] split = ids.split(",");
        LambdaQueryWrapper<Tbfoldareaver> lqw = Wrappers.lambdaQuery();
        lqw.in(Tbfoldareaver::getFoldid, split);
        Long foldareavers = tbfoldareaverMapper.selectCount(lqw);
        if (foldareavers > 0) {
            return "叠次下存在版本信息";
        }
        LambdaQueryWrapper<Tbpagesort> lambdaQuery = Wrappers.lambdaQuery();
        lambdaQuery.in(Tbpagesort::getFoldid, split);
        Long pagesorts = tbpagesortMapper.selectCount(lambdaQuery);
        if (pagesorts > 0) {
            return "叠次下存在版面类别";
        }
        return "";
    }

    @Override
    public List<Tbfold> getFoldListByMediaTypeForCJ(String type) {
        LambdaQueryWrapper<Tbfold> lqw = Wrappers.lambdaQuery();
        lqw.eq(Tbfold::getBuse, true);
        lqw.inSql(Tbfold::getMediaid, "select id from tbmedia where mediatypekey = '" + type + "' and buse = 1");
        return tbfoldMapper.selectList(lqw);
    }
}
