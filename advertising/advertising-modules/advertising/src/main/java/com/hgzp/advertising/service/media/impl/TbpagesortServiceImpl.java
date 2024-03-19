package com.hgzp.advertising.service.media.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.pagemodel.media.vo.PagesortVO;
import com.hgzp.advertising.service.media.TbfoldServiceI;
import com.hgzp.advertising.service.media.TbpagesortServiceI;
import com.hgzp.core.exception.DataExistException;
import com.hgzp.core.model.Tbfold;
import com.hgzp.core.model.Tbmedia;
import com.hgzp.core.model.Tbmediatype;
import com.hgzp.core.model.Tbpagesort;
import com.hgzp.core.page.TreeModel;
import com.hgzp.mapper.media.TbmediaMapper;
import com.hgzp.mapper.media.TbmediatypeMapper;
import com.hgzp.mapper.media.TbpagesortMapper;
import com.hgzp.service.common.impl.MyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 版面类别 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TbpagesortServiceImpl extends MyServiceImpl<TbpagesortMapper, Tbpagesort> implements TbpagesortServiceI {

    @Autowired
    TbpagesortMapper tbpagesortMapper;
    @Autowired
    TbfoldServiceI tbfoldService;
    @Autowired
    private TbmediaMapper tbmediaMapper;
    @Autowired
    private TbmediatypeMapper tbmediatypeMapper;
    @Autowired
    HgDataChangeRecorderInnerInterceptor logInterceptor;


    @Override
    public IPage<PagesortVO> getPagesortPageList(IPage<Tbpagesort> page, PagesortVO query){
        Page<PagesortVO> reslutPage = new Page<>();
        //根据mediatypekey、foldid、mediaid、sname查询
        if(ObjectUtil.isNull(query.getMediatypekey())){
           return reslutPage;
        }
        List<Long> queryFoldIdList=new ArrayList<>();
        //如果媒体叠次为空但媒体Id不为空就找媒体下的所有叠次
        if(ObjectUtil.isNotNull(query.getFoldid())){
            queryFoldIdList.add(query.getFoldid());
        }else {
            if (ObjectUtil.isNotNull(query.getMediaid()) ) {
                queryFoldIdList = tbfoldService.lambdaQuery()
                        .eq(Tbfold::getMediaid, query.getMediaid())
                        .list()
                        .stream().map(Tbfold::getId).collect(Collectors.toList());
            }
        }
        IPage<Tbpagesort> iPage = this.lambdaQuery()
                .eq(Tbpagesort::getMediatypekey, query.getMediatypekey())
                .in(queryFoldIdList.size() > 0, Tbpagesort::getFoldid, queryFoldIdList)
                .like(StrUtil.isNotBlank(query.getQueryKey()), Tbpagesort::getSname, query.getQueryKey())
                .page(page);

        long total = iPage.getTotal();
        if(total == 0){
            return reslutPage;
        }

        //查出关联的叠次
        List<Tbpagesort> list = iPage.getRecords();
        List<Long> foldIdList = list.stream().map(Tbpagesort::getFoldid).collect(Collectors.toList());
        Map<Long, List<Tbfold>> tbfoldListGroupByFoldId = tbfoldService.getTbfoldListGroupByFoldId(foldIdList);

        //媒体信息
        List<Tbmedia> mediaList = new LambdaQueryChainWrapper<>(tbmediaMapper)
                .eq(Tbmedia::getBuse, true)
                .eq( Tbmedia::getMediatypekey, query.getMediatypekey())
                .eq(query.getMediaid() != null, Tbmedia::getId, query.getMediaid())
                .orderByAsc(Tbmedia::getIsort)
                .list();

        //媒体类型
        Tbmediatype tbmediatype =new LambdaQueryChainWrapper<>(tbmediatypeMapper).eq(Tbmediatype::getSkey, query.getMediatypekey()).one();
        String  sMediatypeName = "";
        if(tbmediatype != null){
            sMediatypeName=tbmediatype.getSname();
        }

        List<PagesortVO> resultList = new ArrayList<>();
        for (Tbpagesort tbpagesort : list) {
            PagesortVO vo = new PagesortVO(tbpagesort);
            if(tbfoldListGroupByFoldId.size()>0) {
                if(tbpagesort.getFoldid()!=null) {
                    Tbfold tbfold = tbfoldListGroupByFoldId.get(tbpagesort.getFoldid()).get(0);
                    vo.setFoldname(tbfold.getSname());
                    vo.setMediaid(tbfold.getMediaid());
                    Tbmedia mediaInfo = mediaList.stream().filter(w -> w.getId().equals(tbfold.getMediaid())).findAny().orElse(null);
                    if (mediaInfo != null) {
                        vo.setMediaName(mediaInfo.getSname());
                    }
                }
            }
            vo.setMediatypename(sMediatypeName);
            resultList.add(vo);
        }

        reslutPage.setRecords(resultList);
        reslutPage.setTotal(total);

        return reslutPage;
    }

    @Override
    public void savePagesort(Tbpagesort tbpagesort){
        //判重
        if(isExistPagesort(tbpagesort.getSname(), tbpagesort.getId())){
            throw new DataExistException("已存在同名版面类别");
        }
        //媒体类型
        Tbmediatype tbmediatype =new LambdaQueryChainWrapper<>(tbmediatypeMapper).eq(Tbmediatype::getSkey, tbpagesort.getMediatypekey()).one();
        if(tbmediatype != null){
            tbpagesort.setMediatypename(tbmediatype.getSname());
        }
        logInterceptor.recoredLog();
        this.save(tbpagesort);
    }

    @Override
    public void updatePagesort(Tbpagesort tbpagesort){
        //判重
        if(isExistPagesort(tbpagesort.getSname(), tbpagesort.getId())){
            throw new DataExistException("已存在同名版面类别");
        }

        logInterceptor.recoredLog();
        this.updateById(tbpagesort);
    }


    /**
     * isExistPagesort
     * 方法功能:  是否存在同名版面类别
     * @author wangwk
     * @date 2023/9/20 8:45
     * @param sname  名称
     * @param id  id
     * @return boolean
     */
    public boolean isExistPagesort(String sname, Long id){
        return this.lambdaQuery()
                .eq(Tbpagesort::getSname, sname)
                .ne(id != null, Tbpagesort::getId, id)
                .count() > 0;
    }

    @Override
    public List<TreeModel> getPageSortTreeList(String mediaType, Long foldId){

        List<TreeModel> treeModels = new ArrayList<>();
        if(StrUtil.isBlank(mediaType) && foldId == null){
            return treeModels;
        }
        List<Tbpagesort> pagesortList = this.lambdaQuery()
                .eq(Tbpagesort::getBuse, true)
                .eq(StrUtil.isNotBlank(mediaType), Tbpagesort::getMediatypekey, mediaType)
                .and(w -> w.isNull(Tbpagesort::getFoldid).or().eq(Tbpagesort::getFoldid, ""))
                .or()
                .eq(foldId != null, Tbpagesort::getFoldid, foldId)
                .orderByAsc(Tbpagesort::getIsort)
                .list();

        if(pagesortList.size() < 1){
            return treeModels;
        }
        pagesortList.forEach(item -> {
            TreeModel treeModel = new TreeModel();
            treeModel.setId(item.getId());
            treeModel.setName(item.getSname());
            treeModels.add(treeModel);
        });

        return treeModels;
    }

    @Override
    public List<Tbpagesort> listUsablePageSort(String mediaType, Long foldId) {
        return list(Wrappers.<Tbpagesort>lambdaQuery()
                .eq(Tbpagesort::getBuse, true)
                .eq(StrUtil.isNotBlank(mediaType), Tbpagesort::getMediatypekey, mediaType)
                .and(StrUtil.isNotBlank(mediaType), w -> w.isNull(Tbpagesort::getFoldid).or().eq(Tbpagesort::getFoldid, ""))
                .or()
                .eq(foldId != null, Tbpagesort::getFoldid, foldId)
                .orderByAsc(Tbpagesort::getIsort));
    }
}
