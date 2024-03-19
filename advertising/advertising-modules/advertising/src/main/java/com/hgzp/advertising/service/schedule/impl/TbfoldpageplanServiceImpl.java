package com.hgzp.advertising.service.schedule.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.pagemodel.schedule.dto.FoldPageplanDTO;
import com.hgzp.advertising.pagemodel.schedule.vo.FoldPagePlaneTreeReq;
import com.hgzp.advertising.pagemodel.schedule.vo.FoldPageplanReq;
import com.hgzp.advertising.service.media.TbfoldServiceI;
import com.hgzp.advertising.service.media.TbfoldareaverServiceI;
import com.hgzp.advertising.service.media.TbmediapublicnumServiceI;
import com.hgzp.advertising.service.media.TbpagesizeServiceI;
import com.hgzp.advertising.service.price.TbadcolorServiceI;
import com.hgzp.advertising.service.schedule.TbfoldpageplanServiceI;
import com.hgzp.advertising.service.schedule.TbpagecolorServiceI;
import com.hgzp.core.exception.DataExistException;
import com.hgzp.core.model.*;
import com.hgzp.core.page.DataCombo;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.TreeModel;
import com.hgzp.mapper.ad.TworderitemMapper;
import com.hgzp.mapper.media.TbmediaMapper;
import com.hgzp.mapper.schedule.TbfoldpageplanMapper;
import com.hgzp.service.common.impl.MyServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 版面计划 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-11-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TbfoldpageplanServiceImpl extends MyServiceImpl<TbfoldpageplanMapper, Tbfoldpageplan> implements TbfoldpageplanServiceI {

    @Autowired
    private TbfoldpageplanMapper foldpageplanMapper;
    @Autowired
    private TworderitemMapper orderitemMapper;
    @Autowired
    private TbpagecolorServiceI pagecolorService;
    @Autowired
    private TbadcolorServiceI adcolorService;
    @Autowired
    private TbmediaMapper mediaMapper;
    @Autowired
    private TbmediapublicnumServiceI mediapublicnumServiceI;
    @Autowired
    private TbpagesizeServiceI pagesizeService;
    @Autowired
    private TbfoldServiceI tbfoldService;
    @Autowired
    private TbfoldareaverServiceI tbfoldareaverService;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;

    @Override
    public IPage<Tbfoldpageplan> getMeidaPagePlaneList(Page<Tbfoldpageplan> page, FoldPageplanReq queryInfo) {
        QueryWrapper<Tbfoldpageplan> query = new QueryWrapper<>();

        //序按照刊登日期倒序和叠次正序，版号正序
        query.select(" DISTINCT foldareaverid", "mediatypekey", "mediatypename", "mediaid", "medianame", "foldid",
                        "foldname", "foldareavername", "publishdate", "pagesizeid", "ipagewidth", "ipageheight",
                        "publishnum").lambda()
                .eq(ObjectUtil.isNotNull(queryInfo.getMediaid()), Tbfoldpageplan::getMediaid, queryInfo.getMediaid())
                .eq(ObjectUtil.isNotNull(queryInfo.getFoldid()), Tbfoldpageplan::getFoldid, queryInfo.getFoldid())
                .eq(ObjectUtil.isNotNull(queryInfo.getFoldareaverid()), Tbfoldpageplan::getFoldareaverid,
                        queryInfo.getFoldareaverid())
                .ge(ObjectUtil.isNotNull(queryInfo.getStartTime()), Tbfoldpageplan::getPublishdate,
                        queryInfo.getStartTime())
                .le(ObjectUtil.isNotNull(queryInfo.getEndTime()), Tbfoldpageplan::getPublishdate,
                        queryInfo.getEndTime())
                .orderByDesc(Tbfoldpageplan::getPublishdate)
                .orderByAsc(Tbfoldpageplan::getFoldname)
                .orderByAsc(Tbfoldpageplan::getPagenum);
        IPage<Tbfoldpageplan> pagePlanList = foldpageplanMapper.selectPage(page, query);
        if (pagePlanList.getRecords().size() > 0) {
            for (Tbfoldpageplan item : pagePlanList.getRecords()) {
                Long iCount = new LambdaQueryChainWrapper<>(foldpageplanMapper)
                        .eq(Tbfoldpageplan::getMediaid, item.getMediaid())
                        .eq(Tbfoldpageplan::getFoldid, item.getFoldid())
                        .eq(Tbfoldpageplan::getFoldareaverid, item.getFoldareaverid())
                        .eq(Tbfoldpageplan::getPublishdate, item.getPublishdate())
                        .count();
                item.setPagenum(iCount.intValue());
            }
        }
        return pagePlanList;
    }

    @Override
    public IPage<Tbfoldpageplan> getFoldPagePlanePageList(Page<Tbfoldpageplan> page, FoldPageplanReq queryInfo) {
        IPage<Tbfoldpageplan> pagePlanList = new LambdaQueryChainWrapper<>(foldpageplanMapper)
                .eq(ObjectUtil.isNotNull(queryInfo.getMediaid()), Tbfoldpageplan::getMediaid, queryInfo.getMediaid())
                .eq(ObjectUtil.isNotNull(queryInfo.getFoldid()), Tbfoldpageplan::getFoldid, queryInfo.getFoldid())
                .eq(ObjectUtil.isNotNull(queryInfo.getFoldareaverid()), Tbfoldpageplan::getFoldareaverid,
                        queryInfo.getFoldareaverid())
                .ge(ObjectUtil.isNotNull(queryInfo.getStartTime()), Tbfoldpageplan::getPublishdate,
                        queryInfo.getStartTime())
                .le(ObjectUtil.isNotNull(queryInfo.getEndTime()), Tbfoldpageplan::getPublishdate,
                        queryInfo.getEndTime())
                .orderByDesc(Tbfoldpageplan::getPublishdate)
                .orderByAsc(Tbfoldpageplan::getFoldname)
                .orderByAsc(Tbfoldpageplan::getPagenum)
                .page(page);

        return pagePlanList;
    }

    @Override
    public List<Tbfoldpageplan> getFoldPagePlaneList(FoldPageplanReq tbfoldpageplan) {
        LambdaQueryWrapper<Tbfoldpageplan> lqw = Wrappers.lambdaQuery();
        if (tbfoldpageplan.getFoldpageplanid() != null) {
            lqw.eq(Tbfoldpageplan::getId, tbfoldpageplan.getFoldpageplanid());
        } else {
            Integer iPageNum = 0;
            if (ObjectUtil.isNotNull(tbfoldpageplan.getPagenum())) {
                iPageNum = tbfoldpageplan.getPagenum();
            }
            lqw.eq(ObjectUtil.isNotNull(tbfoldpageplan.getMediaid()), Tbfoldpageplan::getMediaid,
                            tbfoldpageplan.getMediaid())
                    .eq(ObjectUtil.isNotNull(tbfoldpageplan.getFoldid()), Tbfoldpageplan::getFoldid,
                            tbfoldpageplan.getFoldid())
                    .eq(ObjectUtil.isNotNull(tbfoldpageplan.getFoldareaverid()), Tbfoldpageplan::getFoldareaverid,
                            tbfoldpageplan.getFoldareaverid())
                    .eq(ObjectUtil.isNotNull(tbfoldpageplan.getPublishdate()), Tbfoldpageplan::getPublishdate,
                            tbfoldpageplan.getPublishdate())
                    .eq(iPageNum > 0, Tbfoldpageplan::getPagenum,
                            tbfoldpageplan.getPagenum());

        }
        lqw.orderByAsc(Tbfoldpageplan::getPagenum);

        List<Tbfoldpageplan> pagePlanList = foldpageplanMapper.selectList(lqw);
        return pagePlanList;
    }

    //按日期查询版面计划树
    @Override
    public List<TreeModel> getFoldPagePlaneTree(FoldPagePlaneTreeReq foldPagePlaneTreeReq) {
        //获取媒体列表
        List<Tbmedia> mediaList = mediaMapper.selectList(new LambdaQueryWrapper<Tbmedia>()
                .eq(Tbmedia::getMediatypekey, foldPagePlaneTreeReq.getMediatypekey())
                .eq(ObjectUtil.isNotNull(foldPagePlaneTreeReq.getMediaid()), Tbmedia::getId,
                        foldPagePlaneTreeReq.getMediaid())
                .eq(Tbmedia::getBuse, true)
                .orderByAsc(Tbmedia::getIsort));
        if (mediaList.size() == 0) {
            return new ArrayList<>();
        }
        //树结构数据平级摆放，前台树自动处理
        List<TreeModel> treeList = new ArrayList<>();
        for (Tbmedia media : mediaList) {
            TreeModel mediaTree = new TreeModel();
            mediaTree.setId(Long.valueOf(media.getId()));
            mediaTree.setName(media.getSname());
            mediaTree.setStype("media");
            treeList.add(mediaTree);
            //只有报刊才能从媒体计划中获取
            if (media.getMediatypekey().equals("paper")) {
                //获取版面计划列表
                List<Tbfoldpageplan> pagePlanList = new LambdaQueryChainWrapper<>(foldpageplanMapper)
                        .eq(Tbfoldpageplan::getPublishdate, foldPagePlaneTreeReq.getPublishdate())
                        .eq(Tbfoldpageplan::getMediaid, media.getId())
                        .list();
                if (pagePlanList.size() == 0) {
                    continue;
                }

                //获取叠次列表
                List<DataCombo> foldList = pagePlanList.stream()
                        .filter(item -> item.getMediaid().equals(Long.valueOf(media.getId())))
                        .map(item -> new DataCombo(item.getFoldid().toString(), item.getFoldname()))
                        .distinct()
                        .collect(Collectors.toList());
                if (foldList.size() == 0) {
                    continue;
                }
                for (DataCombo folditem : foldList) {
                    TreeModel foldTree = new TreeModel();
                    foldTree.setId(Long.valueOf(folditem.getId()));
                    foldTree.setName(folditem.getName());
                    foldTree.setStype("fold");
                    foldTree.setParentId(Long.valueOf(media.getId()));
                    treeList.add(foldTree);

                    //获取叠次版本列表
                    List<DataCombo> foldAreaList = pagePlanList.stream()
                            .filter(item -> item.getMediaid().equals(Long.valueOf(media.getId())))
                            .filter(item -> item.getFoldid().equals(Long.valueOf(folditem.getId())))
                            .map(item -> new DataCombo(item.getFoldareaverid().toString(),
                                    item.getFoldareavername()))
                            .distinct()
                            .collect(Collectors.toList());
                    if (foldAreaList.size() == 0) {
                        continue;
                    }
                    for (DataCombo foldAreaitem : foldAreaList) {
                        TreeModel foldAreaTree = new TreeModel();
                        foldAreaTree.setId(Long.valueOf(foldAreaitem.getId()));
                        foldAreaTree.setName(foldAreaitem.getName());
                        foldAreaTree.setStype("foldarea");
                        foldAreaTree.setParentId(Long.valueOf(folditem.getId()));

                        //获取版面列表
                        List<DataCombo> pagenumList = pagePlanList.stream()
                                .filter(item -> item.getMediaid().equals(Long.valueOf(media.getId())))
                                .filter(item -> item.getFoldid().equals(Long.valueOf(folditem.getId())))
                                .filter(item -> item.getFoldareaverid().equals(Long.valueOf(foldAreaitem.getId())))
                                .map(item -> new DataCombo(item.getId().toString(),
                                        item.getPagenum().toString()))
                                .distinct()
                                .collect(Collectors.toList());
                        if (pagenumList.size() > 0 && foldPagePlaneTreeReq.getIsShowPagenum()) {
                            foldAreaTree.setBleaf(false);
                        } else {
                            foldAreaTree.setBleaf(true);
                        }
                        if (pagenumList.size() == 0) {
                            treeList.add(foldAreaTree);
                            continue;
                        }
                        if (foldPagePlaneTreeReq.getIsShowPagenum()) {
                            treeList.add(foldAreaTree);
                            for (DataCombo pagenumItem : pagenumList) {
                                TreeModel pagenumListTree = new TreeModel();
                                pagenumListTree.setId(Long.valueOf(pagenumItem.getId()));
                                pagenumListTree.setName(pagenumItem.getName());
                                pagenumListTree.setStype("pagenum");
                                pagenumListTree.setBleaf(true);
                                pagenumListTree.setParentId(Long.valueOf(foldAreaitem.getId()));
                                treeList.add(pagenumListTree);
                            }
                        } else {
                            //子节点为版面
                            List<TreeModel> subTreeList = pagenumConvertTreeModel(pagenumList,
                                    Long.valueOf(foldAreaitem.getId()));
                            foldAreaTree.setChildren(subTreeList);
                            treeList.add(foldAreaTree);
                        }
                    }
                }

            } else {
                //获取叠次信息
                List<DataCombo> tbfoldList = tbfoldService.getFoldCombo(media.getId().toString());
                if (tbfoldList.size() > 0) {
                    for (DataCombo folditem : tbfoldList) {
                        //获取叠次版本
                        List<TreeModel> foldAreaTreeList =
                                tbfoldareaverService.getFoldAreaverTreeList(Long.valueOf(folditem.getId()));

                        TreeModel foldTree = new TreeModel();
                        foldTree.setId(Long.valueOf(folditem.getId()));
                        foldTree.setName(folditem.getName());
                        foldTree.setStype("fold");
                        if (foldAreaTreeList.size() > 0 && foldPagePlaneTreeReq.getIsShowPagenum()) {
                            foldTree.setBleaf(false);
                        } else {
                            foldTree.setBleaf(true);
                        }
                        foldTree.setParentId(Long.valueOf(media.getId()));
                        treeList.add(foldTree);

                        if (foldAreaTreeList.size() == 0) {
                            continue;
                        }
                        if (foldPagePlaneTreeReq.getIsShowPagenum()) {
                            for (TreeModel foldAreaitem : foldAreaTreeList) {
                                TreeModel foldAreaTree = new TreeModel();
                                foldAreaTree.setId(foldAreaitem.getId());
                                foldAreaTree.setName(foldAreaitem.getName());
                                foldAreaTree.setStype("foldarea");
                                foldAreaTree.setBleaf(true);
                                foldAreaTree.setParentId(Long.valueOf(folditem.getId()));
                                treeList.add(foldAreaTree);
                            }
                        }

                    }
                }
            }
        }
        return treeList;
    }

    private List<TreeModel> pagenumConvertTreeModel(List<DataCombo> pagenumList, Long lParentId) {
        List<TreeModel> treeModelList = new ArrayList<>();
        for (DataCombo pagenum : pagenumList) {
            TreeModel treeModel = new TreeModel();
            treeModel.setId(Long.valueOf(pagenum.getId()));
            treeModel.setParentId(lParentId);
            treeModel.setName(pagenum.getName());
            treeModel.setStype("pagenum");
            treeModelList.add(treeModel);
        }
        return treeModelList;
    }

    @Override
    public Json saveBatchPagePlane(FoldPageplanDTO foldPageplanDTO) throws Exception {
        if ("".equals(foldPageplanDTO.getAddType())) {
            foldPageplanDTO.setAddType("PageNum");
        }
        if (foldPageplanDTO.getDuplicateStartTime().after(foldPageplanDTO.getDuplicateEndTime())) {
            return Json.fail("开始时间不能大于结束时间!");
        }
        //获取版心信息
        if (ObjectUtil.isNotNull(foldPageplanDTO.getPagesizeid())) {
            Tbpagesize tbpagesize = pagesizeService.getById(foldPageplanDTO.getPagesizeid());
            if (tbpagesize != null) {
                foldPageplanDTO.setPagesizename(tbpagesize.getSname());
                foldPageplanDTO.setIpageheight(tbpagesize.getIpageheight());
                foldPageplanDTO.setIpagewidth(tbpagesize.getIpagewidth());
            }
        }
        //先删除目标记录
        FoldPageplanReq desReq = new FoldPageplanReq();
        BeanUtils.copyProperties(foldPageplanDTO, desReq);
        desReq.setStartTime(foldPageplanDTO.getDuplicateStartTime());
        desReq.setEndTime(foldPageplanDTO.getDuplicateEndTime());
        deletePagePlane(desReq);

        if ("PageNum".equals(foldPageplanDTO.getAddType())) {
            return saveBatchPagePlaneByPageNum(foldPageplanDTO);
        } else if ("PageColor".equals(foldPageplanDTO.getAddType())) {
            return saveBatchPagePlaneByPageColor(foldPageplanDTO);
        } else if ("Duplicate".equals(foldPageplanDTO.getAddType())) {
            return duplicatePagePlane(foldPageplanDTO);
        }
        return Json.success();
    }

    //按版数批量添加版面计划
    private Json saveBatchPagePlaneByPageNum(FoldPageplanDTO foldPageplanDto) {
        if (foldPageplanDto.getPagenum() == null) {
            foldPageplanDto.setPagenum(1);
        }

        String sMediaName = foldPageplanDto.getMedianame();
        Tbmedia tbmedia = new Tbmedia();
        if (ObjectUtil.isNull(foldPageplanDto.getMedianame())) {
            tbmedia = mediaMapper.selectById(foldPageplanDto.getMediaid());
            sMediaName = tbmedia.getSname();
        }
        //开始日期
        Date startDate = foldPageplanDto.getDuplicateStartTime();
        String sInfo = "";
        Date endDate = DateUtil.offsetDay(foldPageplanDto.getDuplicateEndTime(), 1);

        while (startDate.before(endDate)) {
            //查询刊期
//            Tbmediapublicnum tbPublicNum = new LambdaQueryChainWrapper<>(tbmediapublicnumMapper)
//                    .eq(Tbmediapublicnum::getMediaid, foldPageplanDto.getMediaid())
//                    .eq(Tbmediapublicnum::getDpublishtime, startDate)
//                    .one();
            String sPublishNo = mediapublicnumServiceI.getMediaPublishNO(foldPageplanDto.getMediaid(), startDate);
            if (StrUtil.isBlank(sPublishNo)) {
                sInfo += foldPageplanDto.getMedianame() + DateUtil.formatDateTime(startDate) + " 刊期不存在!";
                startDate = DateUtil.offsetDay(startDate, 1);
                continue;
            }

            for (int i = 0; i < foldPageplanDto.getPagenum(); i++) {
                Tbfoldpageplan tbfoldpageplan = new Tbfoldpageplan();
                BeanUtils.copyProperties(foldPageplanDto, tbfoldpageplan);
                tbfoldpageplan.setId(IdUtil.getSnowflakeNextId());
                tbfoldpageplan.setPagenum(i + 1);
                tbfoldpageplan.setId(IdUtil.getSnowflakeNextId());
                tbfoldpageplan.setPublishnum(Integer.valueOf(sPublishNo));
                tbfoldpageplan.setPublishdate(startDate);
                tbfoldpageplan.setAdflag(foldPageplanDto.getAdflag());
                //tbfoldpageplan.setStopflag(foldPageplanDto.getStopflag());
                tbfoldpageplan.setStoptime(DateUtil.offsetDay(tbfoldpageplan.getPublishdate(), -1));
                tbfoldpageplan.setMedianame(sMediaName);
                innerInterceptor.recoredLog();
                save(tbfoldpageplan);
            }
            startDate = DateUtil.offsetDay(startDate, 1);
        }
        if (!StrUtil.isBlank(sInfo)) {
            return Json.fail(sInfo);
        }
        return Json.success();
    }

    //按广告色彩批量添加版面计划
    private Json saveBatchPagePlaneByPageColor(FoldPageplanDTO foldPageplanDto) throws Exception {
        if (foldPageplanDto.getPagecolorid() == null) {
            return Json.fail("版面色彩Id不能为空!");
        }
        Tbmedia tbmedia = new Tbmedia();
        if (ObjectUtil.isNull(foldPageplanDto.getMedianame())) {
            tbmedia = mediaMapper.selectById(foldPageplanDto.getMediaid());
        }
        //查询版面色彩信息
        Tbpagecolor pagecolor = pagecolorService.getById(foldPageplanDto.getPagecolorid());
        if (pagecolor == null) {
            return Json.fail("版面色彩不存在!");
        }
        //查询色彩信息
        List<Tbadcolor> adcolors = adcolorService.getAdColorListValid(foldPageplanDto.getMediatypekey());
        if (adcolors == null) {
            return Json.fail("广告色彩不存在!");
        }
        Date startDate = foldPageplanDto.getDuplicateStartTime();
        String sInfo = "";

        Date endDate = DateUtil.offsetDay(foldPageplanDto.getDuplicateEndTime(), 1);

        while (startDate.before(endDate)) {
            //查询刊期
//            Tbmediapublicnum tbPublicNum = new LambdaQueryChainWrapper<>(tbmediapublicnumMapper)
//                    .eq(Tbmediapublicnum::getMediaid, foldPageplanDto.getMediaid())
//                    .eq(Tbmediapublicnum::getDpublishtime, startDate)
//                    .one();
            String sPublishNo = mediapublicnumServiceI.getMediaPublishNO(foldPageplanDto.getMediaid(), startDate);
            if (StrUtil.isBlank(sPublishNo)) {
                sInfo += foldPageplanDto.getMedianame() + DateUtil.formatDateTime(startDate) + " 刊期不存在!";
                startDate = DateUtil.offsetDay(startDate, 1);
                continue;
            }
            if (!StrUtil.isBlank(pagecolor.getColorlist())) {
                String[] colorList = pagecolor.getColorlist().split(",");
                int iIndex = 1;
                for (String color : colorList) {
                    String sColorName = getColorName(color.trim());
                    Tbadcolor tbadColor =
                            adcolors.stream().filter(d -> d.getSname().equals(sColorName)).findFirst().get();

                    Tbfoldpageplan pagePlan = new Tbfoldpageplan();
                    BeanUtils.copyProperties(foldPageplanDto, pagePlan);
                    pagePlan.setPublishdate(startDate);
                    pagePlan.setPublishnum(Integer.valueOf(sPublishNo));
                    pagePlan.setPagenum(iIndex);
                    pagePlan.setId(IdUtil.getSnowflakeNextId());
                    pagePlan.setPagetitle(foldPageplanDto.getPagetitle());
                    pagePlan.setAdcolorid(tbadColor.getId());
                    pagePlan.setAdcolorname(tbadColor.getSname());
                    pagePlan.setStoptime(DateUtil.offsetDay(startDate, -1));
                    pagePlan.setAdflag(foldPageplanDto.getAdflag());
                    // pagePlan.setStopflag(false);
                    innerInterceptor.recoredLog();
                    save(pagePlan);

                    iIndex++;
                }
            }
            startDate = DateUtil.offsetDay(startDate, 1);
        }
        if (!StrUtil.isBlank(sInfo)) {
            return Json.fail(sInfo);
        }
        return Json.success();

    }

    @Override
    public Json duplicatePagePlane(FoldPageplanDTO foldPageplanDto) throws Exception {
        //查询被复制的对象集合
        List<Tbfoldpageplan> pagePlanSourceList = new LambdaQueryChainWrapper<>(foldpageplanMapper)
                .eq(ObjectUtil.isNotNull(foldPageplanDto.getMediaid()), Tbfoldpageplan::getMediaid,
                        foldPageplanDto.getMediaid())
                .eq(ObjectUtil.isNotNull(foldPageplanDto.getFoldid()), Tbfoldpageplan::getFoldid,
                        foldPageplanDto.getFoldid())
                .eq(ObjectUtil.isNotNull(foldPageplanDto.getFoldareaverid()), Tbfoldpageplan::getFoldareaverid,
                        foldPageplanDto.getFoldareaverid())
                .ge(ObjectUtil.isNotNull(foldPageplanDto.getStartTime()), Tbfoldpageplan::getPublishdate,
                        foldPageplanDto.getStartTime())
                .le(ObjectUtil.isNotNull(foldPageplanDto.getEndTime()), Tbfoldpageplan::getPublishdate,
                        foldPageplanDto.getEndTime()).list();
        if (pagePlanSourceList.size() == 0) {
            return Json.fail("没有符合条件的版面计划，不能复制!");
        }
        //检索复制的时间范围内的数据
        List<Tbfoldpageplan> lsDesPlans = new LambdaQueryChainWrapper<>(foldpageplanMapper)
                .eq(ObjectUtil.isNotNull(foldPageplanDto.getMediaid()), Tbfoldpageplan::getMediaid,
                        foldPageplanDto.getMediaid())
                .eq(ObjectUtil.isNotNull(foldPageplanDto.getFoldid()), Tbfoldpageplan::getFoldid,
                        foldPageplanDto.getFoldid())
                .eq(ObjectUtil.isNotNull(foldPageplanDto.getFoldareaverid()), Tbfoldpageplan::getFoldareaverid,
                        foldPageplanDto.getFoldareaverid())
                .ge(ObjectUtil.isNotNull(foldPageplanDto.getDuplicateStartTime()), Tbfoldpageplan::getPublishdate,
                        foldPageplanDto.getDuplicateStartTime())
                .le(ObjectUtil.isNotNull(foldPageplanDto.getEndTime()), Tbfoldpageplan::getPublishdate,
                        foldPageplanDto.getDuplicateEndTime()).list();
        //复制
        String sInfo = "";
        Date endDate = DateUtil.offsetDay(foldPageplanDto.getDuplicateEndTime(), 1);
        //复制方式
        int iCopyDay = 1;
        if (foldPageplanDto.getBworkDup()) {
            iCopyDay = 7;
        }
        for (Tbfoldpageplan item : pagePlanSourceList) {
            if (item.getPublishdate() == null) {
                continue;
            }
            //开始日期
            Date startDate = foldPageplanDto.getDuplicateStartTime();
            int iCount = 0;
            while (startDate.before(endDate)) {
                //判断目标是否存在
                DateTime dateTime = DateUtil.offsetDay(item.getPublishdate(), iCopyDay * iCount);
                if(dateTime.isAfter(endDate)){
                    break;
                }
                //判断循环时间差
                while(dateTime.isBefore(startDate) || dateTime.isAfter(endDate)) {
                    iCount++;
                    dateTime = DateUtil.offsetDay(item.getPublishdate(), iCopyDay * iCount);
                    continue;
                }
                if(dateTime.isAfter(endDate)){
                    break;
                }
//                //判断复制的时间是否在复制的时间范围内
//                if (dateTime.isBefore(startDate) || dateTime.isAfter(endDate)) {
//                    startDate = DateUtil.offsetDay(startDate, iCopyDay*1);
//                    iCount++;
//                    continue;
//                }
                if (isExist(item, lsDesPlans, dateTime)) {
                    startDate = DateUtil.offsetDay(startDate, iCopyDay*1);
                    iCount++;
                    continue;
                }
                //查询刊期
                String sPublishNo = mediapublicnumServiceI.getMediaPublishNO(item.getMediaid(), dateTime);
                if (StrUtil.isBlank(sPublishNo)) {
                    sInfo += item.getMedianame() + DateUtil.formatDateTime(dateTime) + " 刊期不存在!";
                    startDate = DateUtil.offsetDay(startDate, iCopyDay*1);
                    iCount++;
                    continue;
                }

                DateTime endDateTime;
                if (item.getStoptime() == null) {
                    endDateTime = DateUtil.offsetDay(dateTime, -1);
                } else {
                    endDateTime = DateUtil.offsetDay(item.getStoptime(), iCopyDay * iCount);
                }
                Tbfoldpageplan newItem = new Tbfoldpageplan();
                BeanUtils.copyProperties(item, newItem);
                newItem.setId(IdUtil.getSnowflakeNextId());
                newItem.setPublishnum(Integer.valueOf(sPublishNo));
                newItem.setPublishdate(dateTime);
                newItem.setStoptime(endDateTime);

                innerInterceptor.recoredLog();
                save(newItem);

                startDate = DateUtil.offsetDay(startDate, iCopyDay *1);
                iCount++;
            }

        }
        if (!StrUtil.isBlank(sInfo)) {
            return Json.fail(sInfo);
        }
        return Json.success();
    }

    //获取待复制的范围内已有版面计划最大版号
    private Integer getMaxPageNums(String sMeidaId, String sFoldId, String sFoldAreaVerId, Date PublishDate) {
        Date endDate = DateUtil.offsetDay(PublishDate, 1);
        //检索复制的时间范围内的数据
        List<Tbfoldpageplan> lsDesPlans = new LambdaQueryChainWrapper<>(foldpageplanMapper)
                .eq(ObjectUtil.isNotNull(sMeidaId), Tbfoldpageplan::getMediaid, sMeidaId)
                .eq(ObjectUtil.isNotNull(sFoldId), Tbfoldpageplan::getFoldid, sFoldId)
                .eq(ObjectUtil.isNotNull(sFoldAreaVerId), Tbfoldpageplan::getFoldareaverid,
                        sFoldAreaVerId)
                .ge(ObjectUtil.isNotNull(PublishDate), Tbfoldpageplan::getPublishdate,
                        PublishDate)
                .le(Tbfoldpageplan::getPublishdate, endDate).list();
        Integer maxNum = lsDesPlans.stream().map(Tbfoldpageplan::getPagenum).max(Integer::compareTo).orElse(0);
        return maxNum;
    }

    //添加版面计划版数
    @Override
    public void savePagePlaneByPageNum(Tbfoldpageplan tbfoldpageplan) throws Exception {
        if (tbfoldpageplan.getPagenum() == null) {
            tbfoldpageplan.setPagenum(1);
        }
        //检索已有版数
        Integer maxPageNum = getMaxPageNums(tbfoldpageplan.getMediaid().toString(),
                tbfoldpageplan.getFoldid().toString(), tbfoldpageplan.getFoldareaverid().toString(),
                tbfoldpageplan.getPublishdate());
        for (int i = 0; i < tbfoldpageplan.getPagenum(); i++) {
            Tbfoldpageplan foldpageplan = new Tbfoldpageplan();
            BeanUtils.copyProperties(tbfoldpageplan, foldpageplan);
            foldpageplan.setId(IdUtil.getSnowflakeNextId());
            foldpageplan.setPagenum(i + 1 + maxPageNum);
            if (tbfoldpageplan.getStoptime() == null) {
                foldpageplan.setStoptime(DateUtil.offsetDay(tbfoldpageplan.getPublishdate(), -1));
            }
            innerInterceptor.recoredLog();
            save(foldpageplan);
        }
    }

    @Override
    public void savePagePlane(Tbfoldpageplan tbfoldpageplan) throws Exception {
        if (isExistPageNum(tbfoldpageplan)) {
            throw new DataExistException("版号已存在!");
        }

        DateTime dateTime = DateUtil.offsetDay(tbfoldpageplan.getPublishdate(), -1);
        tbfoldpageplan.setStoptime(dateTime);
        if (ObjectUtil.isNull(tbfoldpageplan.getMedianame())) {
            Tbmedia tbmedia = mediaMapper.selectById(tbfoldpageplan.getMediaid());
            tbfoldpageplan.setMedianame(tbmedia.getSname());
        }
        innerInterceptor.recoredLog();
        save(tbfoldpageplan);
    }

    @Override
    public void updatePagePlane(Tbfoldpageplan tbfoldpageplan) throws Exception {
        if (isExistPageNum(tbfoldpageplan)) {
            throw new DataExistException("版号已存在!");
        }
        innerInterceptor.recoredLog();
        updateById(tbfoldpageplan);
    }

    @Override
    public void updatePagePlanePageSize(Tbfoldpageplan tbfoldpageplan) throws Exception {
        List<Tbfoldpageplan> pagePlanSourceList = new LambdaQueryChainWrapper<>(foldpageplanMapper)
                .eq(ObjectUtil.isNotNull(tbfoldpageplan.getMediatypekey()), Tbfoldpageplan::getMediatypekey,
                        tbfoldpageplan.getMediatypekey())
                .eq(ObjectUtil.isNotNull(tbfoldpageplan.getMediaid()), Tbfoldpageplan::getMediaid,
                        tbfoldpageplan.getMediaid())
                .eq(ObjectUtil.isNotNull(tbfoldpageplan.getFoldid()), Tbfoldpageplan::getFoldid,
                        tbfoldpageplan.getFoldid())
                .eq(ObjectUtil.isNotNull(tbfoldpageplan.getFoldareaverid()), Tbfoldpageplan::getFoldareaverid,
                        tbfoldpageplan.getFoldareaverid())
                .eq(ObjectUtil.isNotNull(tbfoldpageplan.getPublishdate()), Tbfoldpageplan::getPublishdate,
                        tbfoldpageplan.getPublishdate())
                .list();
        //获取版心设置
        Tbpagesize tbpagesize = pagesizeService.getById(tbfoldpageplan.getPagesizeid());

        if (pagePlanSourceList.size() >= 0) {
            for (Tbfoldpageplan item : pagePlanSourceList) {
                item.setPagesizeid(tbpagesize.getId());
                item.setPagesizename(tbpagesize.getSname());
                item.setIpageheight(tbpagesize.getIpageheight());
                item.setIpagewidth(tbpagesize.getIpagewidth());
                innerInterceptor.recoredLog();
                updateById(item);
            }
        }
    }

    //获取色彩名称  1表示彩色，0表示黑白，2表示套红
    private String getColorName(String sColorValue) {
        switch (sColorValue) {
            case "0":
                return "黑白";
            case "1":
                return "彩色";
            case "2":
                return "套红";
            default:
                return "黑白";
        }
    }

    @Override
    public void deletePagePlaneById(String ids) throws Exception {
        String[] split = ids.split(",");
        List<Long> deleteIdList = Arrays.stream(split).map(Long::parseLong).collect(Collectors.toList());
        //执行删除记录数
        int iDelCount = 0;

        //判断tworderitem表中foldpageid 存在 则不允许设置  需待tworderitem建好后增加判断
        innerInterceptor.recoredLog();
        for (Long id : deleteIdList) {
            Long count = new LambdaQueryChainWrapper<>(orderitemMapper)
                    .eq(Tworderitem::getFoldpageplanid, id)
                    .count();
            if (count > 0) {
                continue;
            }
            Tbfoldpageplan pagePlan = getById(id);
            foldpageplanMapper.deleteById(id);
//            //版号重新排序
//            List<Tbfoldpageplan> pagePlanList = new LambdaQueryChainWrapper<>(foldpageplanMapper)
//                    .eq(Tbfoldpageplan::getMediaid, pagePlan.getMediaid())
//                    .eq(Tbfoldpageplan::getFoldid, pagePlan.getFoldid())
//                    .eq(Tbfoldpageplan::getFoldareaverid, pagePlan.getFoldareaverid())
//                    .eq(Tbfoldpageplan::getPublishdate, pagePlan.getPublishdate())
//                    .orderByAsc(Tbfoldpageplan::getPublishdate).list();
//
//            int iInex = 1;
//            for (Tbfoldpageplan item : pagePlanList) {
//                item.setPagenum(iInex);
//                iInex++;
//            }
//            updateBatchById(pagePlanList);

            iDelCount++;
        }
        if (iDelCount != deleteIdList.size()) {
            throw new DataExistException("版面计划有已经被使用，不能删除!");
        }
    }

    @Override
    public void deletePagePlane(FoldPageplanReq desReq) throws Exception {
        List<Tbfoldpageplan> pagePlanList = new LambdaQueryChainWrapper<>(foldpageplanMapper)
                .eq(ObjectUtil.isNotNull(desReq.getMediaid()), Tbfoldpageplan::getMediaid, desReq.getMediaid())
                .eq(ObjectUtil.isNotNull(desReq.getFoldid()), Tbfoldpageplan::getFoldid, desReq.getFoldid())
                .eq(ObjectUtil.isNotNull(desReq.getFoldareaverid()), Tbfoldpageplan::getFoldareaverid,
                        desReq.getFoldareaverid())
                .ge(ObjectUtil.isNotNull(desReq.getStartTime()), Tbfoldpageplan::getPublishdate,
                        desReq.getStartTime())
                .le(ObjectUtil.isNotNull(desReq.getEndTime()), Tbfoldpageplan::getPublishdate,
                        desReq.getEndTime())
                .eq(ObjectUtil.isNotNull(desReq.getPublishdate()), Tbfoldpageplan::getPublishdate, desReq.getPublishdate())
                .list();

        if (pagePlanList.size() > 0) {
            for (Tbfoldpageplan item : pagePlanList) {
                Long count = new LambdaQueryChainWrapper<>(orderitemMapper)
                        .eq(Tworderitem::getFoldpageplanid, item.getId())
                        .count();
                if (count > 0) {
                    //   sInfo+=item.getMedianame()+item.getFoldname()+item.getFoldareavername()+ " 版面计划有已经被使用，不能删除! ";
                    continue;
                }
                innerInterceptor.recoredLog();
                foldpageplanMapper.deleteById(item.getId());
            }
        }
    }

    @Override
    public Set<String> getPlanCombo() {
        List<Tbfoldpageplan> list = list(Wrappers.<Tbfoldpageplan>lambdaQuery()
                        .eq(Tbfoldpageplan::getStopflag, false)
                // 截版日期晚于今天
                /*.gt(Tbfoldpageplan::getStoptime, new Date())*/);
        if (CollectionUtil.isEmpty(list)) {
            return new HashSet<>();
        }
        return list.stream().map(
                i -> i.getMedianame() + "-" + i.getFoldname() + "-" + i.getFoldareavername()
        ).collect(Collectors.toSet());
    }

    @Override
    public List<TreeModel> getMediaFoldPagePlaneTree(FoldPagePlaneTreeReq foldPagePlaneTreeReq) {
        List<TreeModel> treeModels = new ArrayList<>();
        // 查询全部有效媒体
        List<Tbmedia> mediaList = mediaMapper.selectList(Wrappers.<Tbmedia>lambdaQuery()
                .eq(Tbmedia::getBuse, true)
                .orderByAsc(Tbmedia::getIsort));
        if (mediaList.size() == 0) {
            return new ArrayList<>();
        }
        //树结构数据平级摆放，前台树自动处理
        List<TreeModel> treeList = new ArrayList<>();
        for (Tbmedia media : mediaList) {
            TreeModel mediaTree = new TreeModel();
            mediaTree.setId(Long.valueOf(media.getId()));
            mediaTree.setName(media.getSname());
            mediaTree.setSkey(media.getMediatypekey());
            mediaTree.setStype("media");
            treeList.add(mediaTree);
            //只有报刊才能从媒体计划中获取
            if ("paper".equals(media.getMediatypekey())) {
                //获取版面计划列表
                List<Tbfoldpageplan> pagePlanList = new LambdaQueryChainWrapper<>(foldpageplanMapper)
                        .eq(Tbfoldpageplan::getPublishdate, foldPagePlaneTreeReq.getPublishdate())
                        .eq(Tbfoldpageplan::getMediaid, media.getId())
                        .list();
                if (pagePlanList.size() == 0) {
                    continue;
                }

                //获取叠次列表
                List<DataCombo> foldList = pagePlanList.stream()
                        .filter(item -> item.getMediaid().equals(Long.valueOf(media.getId())))
                        .map(item -> new DataCombo(item.getFoldid().toString(), item.getFoldname()))
                        .distinct()
                        .collect(Collectors.toList());
                if (foldList.size() == 0) {
                    continue;
                }
                for (DataCombo folditem : foldList) {
                    TreeModel foldTree = new TreeModel();
                    foldTree.setId(Long.valueOf(folditem.getId()));
                    foldTree.setName(folditem.getName());
                    foldTree.setSkey(media.getMediatypekey());
                    foldTree.setStype("fold");
                    foldTree.setParentId(Long.valueOf(media.getId()));
                    treeList.add(foldTree);

                    //获取叠次版本列表
                    List<DataCombo> foldAreaList = pagePlanList.stream()
                            .filter(item -> item.getMediaid().equals(Long.valueOf(media.getId())))
                            .filter(item -> item.getFoldid().equals(Long.valueOf(folditem.getId())))
                            .map(item -> new DataCombo(item.getFoldareaverid().toString(),
                                    item.getFoldareavername()))
                            .distinct()
                            .collect(Collectors.toList());
                    if (foldAreaList.size() == 0) {
                        continue;
                    }
                    for (DataCombo foldAreaitem : foldAreaList) {
                        TreeModel foldAreaTree = new TreeModel();
                        foldAreaTree.setId(Long.valueOf(foldAreaitem.getId()));
                        foldAreaTree.setName(foldAreaitem.getName());
                        foldAreaTree.setStype("foldarea");
                        foldAreaTree.setSkey(media.getMediatypekey());
                        foldAreaTree.setParentId(Long.valueOf(folditem.getId()));

                        //获取版面列表
                        List<DataCombo> pagenumList = pagePlanList.stream()
                                .filter(item -> item.getMediaid().equals(Long.valueOf(media.getId())))
                                .filter(item -> item.getFoldid().equals(Long.valueOf(folditem.getId())))
                                .filter(item -> item.getFoldareaverid().equals(Long.valueOf(foldAreaitem.getId())))
                                .map(item -> new DataCombo(item.getId().toString(),
                                        item.getPagenum().toString()))
                                .distinct()
                                .collect(Collectors.toList());
                        if (pagenumList.size() > 0 && foldPagePlaneTreeReq.getIsShowPagenum()) {
                            foldAreaTree.setBleaf(false);
                        } else {
                            foldAreaTree.setBleaf(true);
                        }
                        if (pagenumList.size() == 0) {
                            treeList.add(foldAreaTree);
                            continue;
                        }
                        if (foldPagePlaneTreeReq.getIsShowPagenum()) {
                            treeList.add(foldAreaTree);
                            for (DataCombo pagenumItem : pagenumList) {
                                TreeModel pagenumListTree = new TreeModel();
                                pagenumListTree.setId(Long.valueOf(pagenumItem.getId()));
                                pagenumListTree.setName(pagenumItem.getName());
                                pagenumListTree.setStype("pagenum");
                                pagenumListTree.setSkey(media.getMediatypekey());
                                pagenumListTree.setBleaf(true);
                                pagenumListTree.setParentId(Long.valueOf(foldAreaitem.getId()));
                                treeList.add(pagenumListTree);
                            }
                        } else {
                            //子节点为版面
                            List<TreeModel> subTreeList = pagenumConvertTreeModel(pagenumList,
                                    Long.valueOf(foldAreaitem.getId()));
                            foldAreaTree.setChildren(subTreeList);
                            treeList.add(foldAreaTree);
                        }
                    }
                }

            } else {
                //获取叠次信息
                List<DataCombo> tbfoldList = tbfoldService.getFoldCombo(media.getId().toString());
                if (tbfoldList.size() > 0) {
                    for (DataCombo folditem : tbfoldList) {
                        //获取叠次版本
                        List<TreeModel> foldAreaTreeList =
                                tbfoldareaverService.getFoldAreaverTreeList(Long.valueOf(folditem.getId()));

                        TreeModel foldTree = new TreeModel();
                        foldTree.setId(Long.valueOf(folditem.getId()));
                        foldTree.setName(folditem.getName());
                        foldTree.setSkey(media.getMediatypekey());
                        foldTree.setStype("fold");
                        if (foldAreaTreeList.size() > 0) {
                            foldTree.setBleaf(false);
                        } else {
                            foldTree.setBleaf(true);
                        }
                        foldTree.setParentId(Long.valueOf(media.getId()));
                        treeList.add(foldTree);

                        if (foldAreaTreeList.size() == 0) {
                            continue;
                        }
                        if ("app".equals(media.getMediatypekey())) {
                            if (foldPagePlaneTreeReq.getIsShowPagenum()) {
                                for (TreeModel foldAreaitem : foldAreaTreeList) {
                                    TreeModel foldAreaTree = new TreeModel();
                                    foldAreaTree.setId(foldAreaitem.getId());
                                    foldAreaTree.setName(foldAreaitem.getName());
                                    foldAreaTree.setStype("foldarea");
                                    foldAreaTree.setSkey(media.getMediatypekey());
                                    foldAreaTree.setBleaf(true);
                                    foldAreaTree.setParentId(Long.valueOf(folditem.getId()));
                                    treeList.add(foldAreaTree);
                                }
                            } else {
                                foldTree.setChildren(foldAreaTreeList);
                            }
                        } else {
                            for (TreeModel foldAreaitem : foldAreaTreeList) {
                                TreeModel foldAreaTree = new TreeModel();
                                foldAreaTree.setId(foldAreaitem.getId());
                                foldAreaTree.setName(foldAreaitem.getName());
                                foldAreaTree.setStype("foldarea");
                                foldAreaTree.setSkey(media.getMediatypekey());
                                foldAreaTree.setBleaf(true);
                                foldAreaTree.setParentId(Long.valueOf(folditem.getId()));
                                treeList.add(foldAreaTree);
                            }
                        }
                    }
                }
            }
        }
        return treeList;
    }

    @Override
    public List<DataCombo> getFoldPagePlanePageNum(FoldPageplanReq foldPageplanReq) {
        List<DataCombo> result = new ArrayList<>();
        if (!"paper".equals(foldPageplanReq.getMediatypekey())) {
            return result;
        }
        //获取版面计划列表
        List<Tbfoldpageplan> pagePlanList = new LambdaQueryChainWrapper<>(foldpageplanMapper)
                .eq(Tbfoldpageplan::getMediaid, foldPageplanReq.getMediaid())
                .eq(Tbfoldpageplan::getFoldid, foldPageplanReq.getFoldid())
                .eq(Tbfoldpageplan::getFoldareaverid, foldPageplanReq.getFoldareaverid())
                .eq(Tbfoldpageplan::getPublishdate, foldPageplanReq.getPublishdate())
                .orderByAsc(Tbfoldpageplan::getPagenum)
                .list();
        if (pagePlanList.size() > 0) {
            result = pagePlanList.stream()
                    .map(item -> new DataCombo(item.getId().toString(), item.getPagenum().toString()))
                    .collect(Collectors.toList());
        }

        return result;
    }

    //判断记录在目标时间范围内是否存在
    private boolean isExist(Tbfoldpageplan foldPageplan, List<Tbfoldpageplan> lsDesPlans, DateTime newDateTime) {
        return lsDesPlans.stream().anyMatch(i -> i.getPublishdate().equals(newDateTime)
                && Objects.equals(i.getMediaid(), foldPageplan.getMediaid())
                && Objects.equals(i.getFoldid(), foldPageplan.getFoldid())
                && Objects.equals(i.getFoldareaverid(), foldPageplan.getFoldareaverid())
                && !Objects.equals(i.getId(), foldPageplan.getId())
        );
    }

    //判断版号是否存在
    private boolean isExistPageNum(Tbfoldpageplan foldPageplan) {
        Long count = new LambdaQueryChainWrapper<>(foldpageplanMapper)
                .eq(Tbfoldpageplan::getMediaid, foldPageplan.getMediaid())
                .eq(Tbfoldpageplan::getFoldareaverid, foldPageplan.getFoldareaverid())
                .eq(Tbfoldpageplan::getPublishdate, foldPageplan.getPublishdate())
                .eq(Tbfoldpageplan::getPagenum, foldPageplan.getPagenum())
                .eq(Tbfoldpageplan::getFoldid, foldPageplan.getFoldid())
                .eq(Tbfoldpageplan::getMediatypekey, foldPageplan.getMediatypekey())
                .ne(foldPageplan.getId() != null, Tbfoldpageplan::getId, foldPageplan.getId())
                .count();
        return count > 0;
    }

    @Override
    public List<Tbfoldpageplan> getFoldPagePlaneListByFoldIdForCJ(String FoldId, String foldver, String XDate) {
        LambdaQueryWrapper<Tbfoldpageplan> lqw = Wrappers.lambdaQuery();
        lqw.eq(Tbfoldpageplan::getFoldid, FoldId);
        lqw.eq(Tbfoldpageplan::getFoldareaverid, foldver);
        lqw.eq(Tbfoldpageplan::getPublishdate, XDate);
        return list(lqw);
    }
}
