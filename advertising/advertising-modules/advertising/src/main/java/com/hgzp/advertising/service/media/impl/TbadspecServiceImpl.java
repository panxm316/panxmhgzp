package com.hgzp.advertising.service.media.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.pagemodel.media.dto.AdspecDateModelDTO;
import com.hgzp.advertising.pagemodel.media.vo.AdspecModelVO;
import com.hgzp.advertising.service.media.TbadspecServiceI;
import com.hgzp.advertising.service.media.TbmediaServiceI;
import com.hgzp.core.exception.DataExistException;
import com.hgzp.core.model.Tbadspec;
import com.hgzp.core.model.Tbmedia;
import com.hgzp.core.page.TreeModel;
import com.hgzp.mapper.media.TbadspecMapper;
import com.hgzp.service.common.impl.MyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * <p>
 * 叠次版本信息 服务实现类
 * </p>
 *
 * @author CGD
 * @since 2023-09-18
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TbadspecServiceImpl extends MyServiceImpl<TbadspecMapper, Tbadspec> implements TbadspecServiceI {
    @Autowired
    private TbadspecMapper adspecMapper;
    @Autowired
    private TbmediaServiceI tbmediaService;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;

    @Override
    public Page<AdspecModelVO> getAdspecPageList(Page<Tbadspec> page, AdspecModelVO adspecModelVO) {
        Page<AdspecModelVO> reslutPage = new Page<>();
        List<Tbmedia> tbmediaList = tbmediaService.lambdaQuery()
                .eq(Tbmedia::getBuse, true)
                .eq(StringUtils.hasText(adspecModelVO.getMediatypekey()), Tbmedia::getMediatypekey, adspecModelVO.getMediatypekey())
                .eq(adspecModelVO.getMediaid() != null, Tbmedia::getId, adspecModelVO.getMediaid())
                .list();
        List<Long> mediaIdList = tbmediaList
                .stream()
                .map(Tbmedia::getId)
                .distinct()
                .collect(Collectors.toList());
        if (mediaIdList.size() < 1) {
            return reslutPage;
        }
        Page<Tbadspec> adspecPage = this.lambdaQuery()
                .in(Tbadspec::getMediaid, mediaIdList)
                .like(StrUtil.isNotBlank(adspecModelVO.getQueryKey()),
                        Tbadspec::getSname, adspecModelVO.getQueryKey()).page(page);

        if (adspecPage.getTotal() == 0) {
            return reslutPage;
        }
        List<AdspecModelVO> reslutList = new ArrayList<>();
        for (Tbadspec adspec : adspecPage.getRecords()) {
            AdspecModelVO adspecModel = new AdspecModelVO(adspec);
            Tbmedia byId = tbmediaService.getById(adspecModel.getMediaid());
            adspecModel.setMedianame(byId.getSname());
            reslutList.add(adspecModel);
        }
        reslutPage.setRecords(reslutList);
        reslutPage.setTotal(adspecPage.getTotal());
        return reslutPage;
    }

    @Override
    public void saveAdspec(Tbadspec adspec) {
        if (adspecNameExist(adspec.getSname(), adspec.getId())) {
            throw new DataExistException("广告规格名称已存在");
        }
        extractedNarea(adspec);
        innerInterceptor.recoredLog();
        this.save(adspec);
    }

    /**
     * 分类广告的面积可能是（长*宽+0.07*格子数）这个目前先这样固定死
     *
     * @param adspec
     */
    private static void extractedNarea(Tbadspec adspec) {
        if (adspec.getBclassified() != null && adspec.getBclassified()) {
            if (adspec.getNwidth() != null && adspec.getNheight() != null) {
                adspec.setNarea(adspec.getNwidth().multiply(adspec.getNheight()).add(new BigDecimal(0.07 * adspec.getIpknum())));
            }
        } else {
            if (adspec.getNwidth() != null && adspec.getNheight() != null) {
                adspec.setNarea(adspec.getNwidth().multiply(adspec.getNheight()));
            }

        }
    }

    @Override
    public void updateAdspec(Tbadspec adspec) {
        if (adspecNameExist(adspec.getSname(), adspec.getId())) {
            throw new DataExistException("广告规格名称已存在");
        }
        extractedNarea(adspec);
        innerInterceptor.recoredLog();
        this.lambdaUpdate()
                .eq(Tbadspec::getId, adspec.getId())
                .set(Tbadspec::getDstartdate, adspec.getDstartdate())
                .set(Tbadspec::getDenddate, adspec.getDenddate())
                .update(adspec);
//        this.updateById(adspec);
    }

    @Override
    public void deleteAdspecById(String ids) {
        innerInterceptor.recoredLog();
        this.removeByIds(Arrays.asList(ids.split(",")));
    }

    public boolean adspecNameExist(String name, Long id) {
        Long count = this.lambdaQuery()
                .eq(Tbadspec::getSname, name)
                .ne(id != null, Tbadspec::getId, id)
                .count();
        return count > 0;
    }

    @Override
    public void updateAdspecDate(AdspecDateModelDTO adspecDate) {
        if (adspecDate.getDenddate() != null) {
            // 根据老结束日期查询 或者更新整表结束日期
            if (adspecDate.getOlddenddate() != null) {
                List<Tbadspec> list = this.lambdaQuery().eq(Tbadspec::getDenddate, adspecDate.getOlddenddate()).list();
                if (list.size() > 0) {
                    List<Long> collect = list.stream().map(Tbadspec::getId).collect(Collectors.toList());
                    innerInterceptor.recoredLog();
                    adspecMapper.updateDenddateByIdList(adspecDate.getDenddate(), collect);
                }
            } else {
                innerInterceptor.recoredLog();
                adspecMapper.updateDenddateByAll(adspecDate.getDenddate());
            }
        }
        // 开始日期
        if (adspecDate.getDstartdate() != null) {
            innerInterceptor.recoredLog();
            adspecMapper.updateDstartdateByAll(adspecDate.getDstartdate());
        }

    }

    @Override
    public List<TreeModel> getAdspecTreeList(String mediaId) {
        List<TreeModel> treeModels = new ArrayList<>();
        if (StrUtil.isBlank(mediaId)) {
            return treeModels;
        }
        List<Tbadspec> adspecList = this.lambdaQuery()
                .eq(Tbadspec::getBuse, true)
                .in(Tbadspec::getMediaid, mediaId)
                .orderByAsc(Tbadspec::getIsort)
                .list();
        if (adspecList.size() < 1) {
            return treeModels;
        }
        adspecList.forEach(item -> {
            TreeModel treeModel = new TreeModel();
            treeModel.setId(item.getId());
            treeModel.setName(item.getSname());
            treeModels.add(treeModel);
        });

        return treeModels;
    }

    @Override
    public List<Tbadspec> listUsableAdSpec(Long mediaId) {
        return list(Wrappers.<Tbadspec>lambdaQuery()
                .eq(Tbadspec::getBuse, true)
                .in(mediaId != null, Tbadspec::getMediaid, mediaId)
                .orderByAsc(Tbadspec::getIsort));
    }

    @Override
    public List<Tbadspec> getAdspecListForCJ() {
        LambdaQueryWrapper<Tbadspec> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Tbadspec::getBuse, true);
        lqw.eq(Tbadspec::getBclassified, true);
        lqw.orderByAsc(Tbadspec::getIsort);
        List<Tbadspec> list = this.list(lqw);
        return list;
    }
}