package com.hgzp.advertising.service.media.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.pagemodel.ad.vo.MediaFoldTree;
import com.hgzp.advertising.pagemodel.ad.vo.PreOrderExtVO;
import com.hgzp.advertising.pagemodel.media.vo.FoldAreaverVO;
import com.hgzp.advertising.service.media.TbfoldServiceI;
import com.hgzp.advertising.service.media.TbfoldareaverServiceI;
import com.hgzp.core.exception.DataExistException;
import com.hgzp.core.model.Tbfold;
import com.hgzp.core.model.Tbfoldareaver;
import com.hgzp.core.model.Tbmedia;
import com.hgzp.core.model.Tbmediatype;
import com.hgzp.core.page.DataCombo;
import com.hgzp.core.page.ElTreeNode;
import com.hgzp.core.page.TreeModel;
import com.hgzp.mapper.media.TbfoldMapper;
import com.hgzp.mapper.media.TbfoldareaverMapper;
import com.hgzp.mapper.media.TbmediaMapper;
import com.hgzp.mapper.media.TbmediatypeMapper;
import com.hgzp.service.common.impl.MyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 叠次版本信息 服务实现类
 * </p>
 *
 * @author songly
 * @since 2023-09-18
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TbfoldareaverServiceImpl extends MyServiceImpl<TbfoldareaverMapper, Tbfoldareaver> implements TbfoldareaverServiceI {
    @Autowired
    private TbfoldareaverMapper foldareaverMapper;
    @Autowired
    TbfoldServiceI tbfoldService;
    @Autowired
    TbmediaMapper mediaMapper;
    @Autowired
    TbfoldMapper tbfoldMapper;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;
    private @Autowired TbmediatypeMapper tbmediatypeMapper;

    @Override
    public IPage<FoldAreaverVO> getFoldAreaverPageList(Page<Tbfoldareaver> page, FoldAreaverVO query) {
        Page<FoldAreaverVO> reslutPage = new Page<>();
        if (ObjectUtil.isNull(query.getMediatypekey())) {
            query.setMediatypekey("paper");
        }
        //按媒体类型过滤
        List<Long> lsMediaIds = new ArrayList<>();
        if (query.getMediaid() == null) {
            lsMediaIds = new LambdaQueryChainWrapper<>(mediaMapper)
                    .select(Tbmedia::getId)
                    .eq(ObjectUtil.isNotNull(query.getMediatypekey()), Tbmedia::getMediatypekey, query.getMediatypekey())
                    .list()
                    .stream()
                    .map(Tbmedia::getId)
                    .collect(Collectors.toList());

        } else {
            lsMediaIds.add(query.getMediaid());
        }
        if (lsMediaIds.size() == 0) {
            return reslutPage;
        }
        //过滤全部
        List<Long> filterfoldIds = new ArrayList<>();
        if (query.getFoldid() == null) {
            List<Tbfold> foldInfos = new LambdaQueryChainWrapper<>(tbfoldMapper)
                    .in(Tbfold::getMediaid, lsMediaIds)
                    .list();

            filterfoldIds = foldInfos.stream()
                    .map(Tbfold::getId)
                    .distinct()
                    .collect(Collectors.toList());
        } else {
            if (query.getFoldid() != null) {
                filterfoldIds.add(query.getFoldid());
            }
        }

        if (filterfoldIds.size() == 0) {
            return reslutPage;
        }
        Page<Tbfoldareaver> tbfoldareaverPage = this.lambdaQuery()
                //.eq(query.getFoldid() != null, Tbfoldareaver::getFoldid, query.getFoldid())
                .in(Tbfoldareaver::getFoldid, filterfoldIds)
                .and(StrUtil.isNotBlank(query.getQueryKey()),
                        i -> i.like(Tbfoldareaver::getSname, query.getQueryKey())
                )
                .page(page);
        if (page.getTotal() == 0) {
            return reslutPage;
        }
        //叠次
        List<Long> foldIds = tbfoldareaverPage.getRecords()
                .stream()
                .map(Tbfoldareaver::getFoldid)
                .distinct()
                .collect(Collectors.toList());
        Map<Long, List<Tbfold>> foldById = tbfoldService.listByIds(foldIds).stream().collect(Collectors.groupingBy(Tbfold::getId));

        //媒体Id
        List<Long> mediaIds = tbfoldService.listByIds(foldIds).stream().map(Tbfold::getMediaid).collect(Collectors.toList());
        //媒体
        List<Tbmedia> mediaListByFoldId = new LambdaQueryChainWrapper<>(mediaMapper)
                .eq(Tbmedia::getBuse, true)
                .in(Tbmedia::getId, mediaIds)
                .orderByAsc(Tbmedia::getIsort)
                .list();

        List<FoldAreaverVO> result = new ArrayList<>();
        for (Tbfoldareaver record : tbfoldareaverPage.getRecords()) {
            FoldAreaverVO foldModel = new FoldAreaverVO(record);
            foldModel.setFoldname(foldById.get(foldModel.getFoldid()).get(0).getSname());
            long sMediaId = foldById.get(foldModel.getFoldid()).get(0).getMediaid();
            Optional<Tbmedia> cartOptional = mediaListByFoldId.stream().filter(item -> item.getId().equals(sMediaId)).findFirst();
            foldModel.setMedianame(cartOptional.get().getSname());
            foldModel.setMediaid(sMediaId);
            result.add(foldModel);
        }

        reslutPage.setRecords(result);
        reslutPage.setTotal(tbfoldareaverPage.getTotal());
        return reslutPage;
    }

    @Override
    public void saveFoldAreaver(Tbfoldareaver tbfoldareaver) {
        if (isExistFoldAreaver(tbfoldareaver)) {
            throw new DataExistException("已存在同名叠次版本信息");
        }
        tbfoldareaver.setId(IdUtil.getSnowflakeNextId());
        innerInterceptor.recoredLog();
        foldareaverMapper.insert(tbfoldareaver);
    }

    @Override
    public void updateFoldAreaver(Tbfoldareaver tbfoldareaver) {
        if (isExistFoldAreaver(tbfoldareaver)) {
            throw new DataExistException("已存在同名叠次");
        }
        innerInterceptor.recoredLog();
        foldareaverMapper.updateById(tbfoldareaver);
    }

    @Override
    public boolean isExistFoldAreaver(Tbfoldareaver tbfoldareaver) {
        Long count = new LambdaQueryChainWrapper<>(foldareaverMapper)
                .eq(tbfoldareaver.getFoldid() != null, Tbfoldareaver::getFoldid, tbfoldareaver.getFoldid())
                .eq(Tbfoldareaver::getSname, tbfoldareaver.getSname())
                .ne(tbfoldareaver.getId() != null, Tbfoldareaver::getId, tbfoldareaver.getId())
                .count();
        return count > 0;
    }

    @Override
    public void deleteFoldAreaverByIds(String ids) {
        String[] split = ids.split(",");
        //判断是否被用
//        LambdaQueryWrapper<Tbfoldareaver> lqw = Wrappers.lambdaQuery();
//        lqw.in(Tbfoldareaver::getFoldid,split);
//        long iCount = foldareaverMapper.selectCount(lqw);
//        if(iCount > 0) {
//            throw new DataExistException("存在子级行业");
//        }
        Arrays.stream(split).forEach(id -> {
            innerInterceptor.recoredLog();
            foldareaverMapper.deleteById(id);
        });
    }

    @Override
    public List<TreeModel> getFoldAreaverTreeList(Long foldId) {
        List<TreeModel> treeModels = new ArrayList<>();
        if (foldId != null) {
            List<Tbfoldareaver> foldareaverList = this.lambdaQuery()
                    .eq(Tbfoldareaver::getFoldid, foldId)
                    .eq(Tbfoldareaver::getBuse, true)
                    .orderByAsc(Tbfoldareaver::getIsort)
                    .list();
            foldareaverList.forEach(item -> {
                TreeModel treeModel = new TreeModel();
                treeModel.setId(item.getId());
                treeModel.setName(item.getSname());
                treeModels.add(treeModel);
            });
        }
        return treeModels;

    }

    @Override
    public List<ElTreeNode> getMediaFoldElTree() {
        List<ElTreeNode> nodes = new ArrayList<>();
        // 查询全部媒体类型
        List<Tbmediatype> mediaTypes = tbmediatypeMapper.selectList(Wrappers.<Tbmediatype>lambdaQuery()
                .in(Tbmediatype::getSkey, Arrays.asList("paper", "app", "wei"))
                .eq(Tbmediatype::getBuse, true)
                .orderByAsc(Tbmediatype::getIsort));
        if (CollectionUtil.isEmpty(mediaTypes)) {
            return nodes;
        }
        // 查询全部媒体
        List<Tbmedia> medias = mediaMapper.selectList(Wrappers.<Tbmedia>lambdaQuery()
                .eq(Tbmedia::getBuse, true)
                .in(Tbmedia::getMediatypekey, mediaTypes.stream().map(Tbmediatype::getSkey).collect(Collectors.toSet()))
                .orderByAsc(Tbmedia::getIsort));
        if (CollectionUtil.isEmpty(medias)) {
            return nodes;
        }
        // 查询全部叠次
        List<Tbfold> folds = tbfoldMapper.selectList(Wrappers.<Tbfold>lambdaQuery()
                .eq(Tbfold::getBuse, true)
                .in(Tbfold::getMediaid, medias.stream().map(Tbmedia::getId).collect(Collectors.toSet()))
                .orderByAsc(Tbfold::getIsort));
        for (Tbmediatype mediaType : mediaTypes) {
            List<ElTreeNode> mediaChildren = new ArrayList<>();
            String mediatypename = mediaType.getSname();
            String mediaTypeSkey = mediaType.getSkey();
            for (Tbmedia media : medias) {
                Long mediaid = media.getId();
                String medianame = media.getSname();
                if (Objects.equals(media.getMediatypekey(), mediaTypeSkey)) {
                    List<ElTreeNode> foldChildren = new ArrayList<>();
                    for (Tbfold fold : folds) {
                        if (Objects.equals(fold.getMediaid(), media.getId())) {
                            List<ElTreeNode> foldVerChildren = new ArrayList<>();
                            String foldname = fold.getSname();
                            Long foldid = fold.getId();
                            foldChildren.add(ElTreeNode.builder().value(foldid.toString()).label(foldname)
                                    .children(foldVerChildren).extObj(PreOrderExtVO.builder()
                                            .mediatypekey(mediaTypeSkey)
                                            .mediatypename(mediatypename)
                                            .mediaid(mediaid.toString())
                                            .medianame(medianame)
                                            .foldid(foldid.toString())
                                            .foldname(foldname)
                                            .build())
                                    .build());
                        }
                    }
                    mediaChildren.add(ElTreeNode.builder().value(mediaid.toString()).label(medianame)
                            .children(foldChildren).extObj(PreOrderExtVO.builder()
                                    .mediatypekey(mediaTypeSkey)
                                    .mediatypename(mediatypename)
                                    .mediaid(mediaid.toString())
                                    .medianame(medianame)
                                    .build())
                            .build());
                }
            }
            nodes.add(ElTreeNode.builder().value(mediaTypeSkey).label(mediatypename)
                    .children(mediaChildren).extObj(PreOrderExtVO.builder()
                            .mediatypekey(mediaTypeSkey)
                            .mediatypename(mediatypename)
                            .build())
                    .build());
        }
        return nodes;
    }

    @Override
    public List<MediaFoldTree> getMediaFoldTree() {
        List<MediaFoldTree> nodes = new ArrayList<>();
        // 查询全部媒体类型
        List<Tbmediatype> mediaTypes = tbmediatypeMapper.selectList(Wrappers.<Tbmediatype>lambdaQuery()
                .in(Tbmediatype::getSkey, Arrays.asList("paper", "app", "wei"))
                .eq(Tbmediatype::getBuse, true)
                .orderByAsc(Tbmediatype::getIsort));
        if (CollectionUtil.isEmpty(mediaTypes)) {
            return nodes;
        }
        // 查询全部媒体
        List<Tbmedia> medias = mediaMapper.selectList(Wrappers.<Tbmedia>lambdaQuery()
                .eq(Tbmedia::getBuse, true)
                .in(Tbmedia::getMediatypekey, mediaTypes.stream().map(Tbmediatype::getSkey).collect(Collectors.toSet()))
                .orderByAsc(Tbmedia::getIsort));
        if (CollectionUtil.isEmpty(medias)) {
            return nodes;
        }
        // 查询全部叠次
        List<Tbfold> folds = tbfoldMapper.selectList(Wrappers.<Tbfold>lambdaQuery()
                .eq(Tbfold::getBuse, true)
                .in(Tbfold::getMediaid, medias.stream().map(Tbmedia::getId).collect(Collectors.toSet()))
                .orderByAsc(Tbfold::getIsort));
        for (Tbmediatype mediaType : mediaTypes) {
            List<MediaFoldTree> mediaChildren = new ArrayList<>();
            Long mediaTypeId = mediaType.getId();
            String mediatypename = mediaType.getSname();
            String mediaTypeSkey = mediaType.getSkey();
            for (Tbmedia media : medias) {
                Long mediaid = media.getId();
                String medianame = media.getSname();
                if (Objects.equals(media.getMediatypekey(), mediaTypeSkey)) {
                    List<MediaFoldTree> foldChildren = new ArrayList<>();
                    for (Tbfold fold : folds) {
                        if (Objects.equals(fold.getMediaid(), media.getId())) {
                            List<MediaFoldTree> foldVerChildren = new ArrayList<>();
                            String foldname = fold.getSname();
                            Long foldid = fold.getId();
                            MediaFoldTree tree = new MediaFoldTree();
                            tree.setId(foldid);
                            tree.setName(foldname);
                            tree.setParentId(mediaid);
                            tree.setType("fold");
                            tree.setChildren(foldVerChildren);
                            tree.setChecked(false);
                            tree.setMediaTypeKey(mediaTypeSkey);
                            tree.setMediaTypeName(mediatypename);
                            tree.setMediaId(mediaid);
                            tree.setMediaName(medianame);
                            tree.setFoldId(foldid);
                            tree.setFoldName(foldname);
                            foldChildren.add(tree);
                        }
                    }
                    MediaFoldTree tree = new MediaFoldTree();
                    tree.setId(mediaid);
                    tree.setName(medianame);
                    tree.setParentId(mediaTypeId);
                    tree.setType("media");
                    tree.setChildren(foldChildren);
                    tree.setChecked(false);
                    tree.setMediaTypeKey(mediaTypeSkey);
                    tree.setMediaTypeName(mediatypename);
                    tree.setMediaId(mediaid);
                    tree.setMediaName(medianame);
                    mediaChildren.add(tree);
                }
            }
            MediaFoldTree tree = new MediaFoldTree();
            tree.setId(mediaTypeId);
            tree.setName(mediatypename);
            tree.setParentId(0L);
            tree.setType("mediatype");
            tree.setChildren(mediaChildren);
            tree.setChecked(false);
            tree.setMediaTypeKey(mediaTypeSkey);
            tree.setMediaTypeName(mediatypename);
            nodes.add(tree);
        }
        return nodes;
    }

    @Override
    public List<TreeModel> getMediaFoldAreaverTree() {
        List<TreeModel> treeModels = new ArrayList<>();
        // 查询全部有效媒体
        List<Tbmedia> medias = mediaMapper.selectList(Wrappers.<Tbmedia>lambdaQuery()
                .eq(Tbmedia::getBuse, true)
                .orderByAsc(Tbmedia::getIsort));
        if (CollectionUtil.isEmpty(medias)) {
            return treeModels;
        }
        for (Tbmedia media : medias) {
            TreeModel mediaTreeModel = new TreeModel();
            mediaTreeModel.setId(media.getId());
            mediaTreeModel.setName(media.getSname());
            mediaTreeModel.setStype("media");
            mediaTreeModel.setSkey(media.getMediatypekey());
            mediaTreeModel.setBleaf(false);
            treeModels.add(mediaTreeModel);
            // 查询全部有效叠次
            List<Tbfold> folds = tbfoldMapper.selectList(Wrappers.<Tbfold>lambdaQuery()
                    .eq(Tbfold::getBuse, true)
                    .eq(Tbfold::getMediaid, media.getId())
                    .orderByAsc(Tbfold::getIsort));
            if (CollectionUtil.isEmpty(folds)) {
                continue;
            }
            for (Tbfold fold : folds) {
                TreeModel foldTreeModel = new TreeModel();
                foldTreeModel.setId(fold.getId());
                foldTreeModel.setName(fold.getSname());
                foldTreeModel.setParentId(media.getId());
                foldTreeModel.setStype("fold");
                foldTreeModel.setSkey(media.getMediatypekey());
                treeModels.add(foldTreeModel);

                // 查询全部有效叠次版本
                List<Tbfoldareaver> foldareavers = foldareaverMapper.selectList(Wrappers.<Tbfoldareaver>lambdaQuery()
                        .eq(Tbfoldareaver::getBuse, true)
                        .eq(Tbfoldareaver::getFoldid, fold.getId())
                        .orderByAsc(Tbfoldareaver::getIsort));
                if (CollectionUtil.isEmpty(foldareavers)) {
                    continue;
                }
                for (Tbfoldareaver foldareaver : foldareavers) {
                    TreeModel foldAreaverTreeModel = new TreeModel();
                    foldAreaverTreeModel.setId(foldareaver.getId());
                    foldAreaverTreeModel.setName(foldareaver.getSname());
                    foldAreaverTreeModel.setParentId(fold.getId());
                    foldAreaverTreeModel.setStype("foldareaver");
                    foldAreaverTreeModel.setSkey(media.getMediatypekey());
                    treeModels.add(foldAreaverTreeModel);
                }
            }
        }
        return treeModels;
    }

    @Override
    public List<Tbfoldareaver> getFoldAreaverListByFoldIdForCJ(String foldId) {
        LambdaQueryWrapper<Tbfoldareaver> lqw = Wrappers.lambdaQuery();
        lqw.eq(Tbfoldareaver::getFoldid, foldId);
        lqw.eq(Tbfoldareaver::getBuse, true);
        return foldareaverMapper.selectList(lqw);
    }

    @Override
    public List<DataCombo> getFoldAreaverCombo(String foldId) {
        List<Tbfoldareaver> tbfoldareaverList = this.lambdaQuery()
                .eq(Tbfoldareaver::getBuse, true)
                .and(StringUtils.hasText(foldId), m -> {
                    m.eq(Tbfoldareaver::getFoldid, foldId);
                })
                .orderByAsc(Tbfoldareaver::getIsort)
                .list();
        List<DataCombo> comboList = tbfoldareaverList.stream()
                .map(item -> new DataCombo(item.getId().toString(), item.getSname()))
                .collect(Collectors.toList());
        return comboList;
    }
}