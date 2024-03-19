package com.hgzp.advertisingsys.service.media.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgzp.advertisingsys.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertisingsys.pagemodel.media.dto.MediaPublicNumDto;
import com.hgzp.advertisingsys.service.media.TbmediapublicnumServiceI;
import com.hgzp.core.model.Tbfoldpageplan;
import com.hgzp.core.model.Tbmediapublicnum;
import com.hgzp.core.model.Twadproject;
import com.hgzp.core.model.Tworderitem;
import com.hgzp.core.page.BaseQueryInfo;
import com.hgzp.mapper.media.TbmediapublicnumMapper;
import com.hgzp.mapper.schedule.TbfoldpageplanMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 媒体刊期 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-09-07
 */
@Service
public class TbmediapublicnumServiceImpl extends ServiceImpl<TbmediapublicnumMapper, Tbmediapublicnum> implements TbmediapublicnumServiceI {

    @Autowired
    TbmediapublicnumMapper tbmediapublicnumMapper;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;
    @Autowired
    TbfoldpageplanMapper foldpageplanMapper;
    @Override
    public Page<Tbmediapublicnum> getMediaPublicNumPageList(Page<Tbmediapublicnum> page, BaseQueryInfo baseQueryInfo, Tbmediapublicnum mediapublicnum) {
        LambdaQueryChainWrapper<Tbmediapublicnum> tbmediapublicnumlqw = this.lambdaQuery();
        tbmediapublicnumlqw.ge(baseQueryInfo.getStartTime() != null, Tbmediapublicnum::getDpublishtime, baseQueryInfo.getStartTime());
        if (baseQueryInfo.getEndTime() != null) {
            tbmediapublicnumlqw.lt(Tbmediapublicnum::getDpublishtime, DateUtil.offsetDay(baseQueryInfo.getEndTime(), 1));
        }
        Page<Tbmediapublicnum> pageList = tbmediapublicnumlqw
                .eq(mediapublicnum.getMediaid() != null, Tbmediapublicnum::getMediaid, mediapublicnum.getMediaid())
                .eq(StrUtil.isNotBlank(mediapublicnum.getSpublishno()), Tbmediapublicnum::getSpublishno, mediapublicnum.getSpublishno())
                .page(page);
        return pageList;
    }

    @Override
    public boolean examinePublishno(Tbmediapublicnum mediapublicnum) {
        Long count = this.lambdaQuery()
                .eq(mediapublicnum.getMediaid() != null, Tbmediapublicnum::getMediaid, mediapublicnum.getMediaid())
               // .eq(StrUtil.isNotBlank(mediapublicnum.getSpublishno()), Tbmediapublicnum::getSpublishno, mediapublicnum.getSpublishno())
                .eq(Tbmediapublicnum::getDpublishtime, mediapublicnum.getDpublishtime())
                .ne(mediapublicnum.getId() != null, Tbmediapublicnum::getId, mediapublicnum.getId())
                .count();
        return count > 0;
    }

    @Override
    public void saveBatchMediaPublicNum(MediaPublicNumDto mediaPublicNumDto) {
        // 开始日期 也是当前变动日期
        Date dStart = mediaPublicNumDto.getStartTime();
        // 结束日期
        Date dEnd = mediaPublicNumDto.getEndTime();
        //初使号
        int iCount = Integer.parseInt(mediaPublicNumDto.getSpublishno());
        //计数器
        int iCounter = 0;
        // 发布日期
        String[] strTimes = mediaPublicNumDto.getSpublishnum().split(";");
        while (dStart.compareTo(dEnd) <= 0) {
            switch (mediaPublicNumDto.getMediapublictype()) {
                case "日报":
                    for (String s : strTimes) {
                        //muyn 231205增加了日报，按天循环录入 ，则先判断是否已存在相同的设置 ，如果没有，则新增
                        mediaPublicNumDto.setDpublishtime(dStart);
                        mediaPublicNumDto.setSpublishno(String.valueOf(iCount));
                        if (!examineBatchPublicNum(mediaPublicNumDto)) {
                            saveMediaPublicNumData(mediaPublicNumDto);
                        }
                        iCount++;
                    }
                    break;
                case "周刊":
                    for (String s : strTimes) {
                        //循环刊期规则，判断周数是否相等，如果相等 ，则先判断是否已存在相同的设置 ，如果没有，则新增
                        int iW = DateUtil.dayOfWeek(dStart) - 1 == 0 ? 7 : DateUtil.dayOfWeek(dStart) - 1;
                        if (Integer.parseInt(s) == iW) {
                            mediaPublicNumDto.setDpublishtime(dStart);
                            mediaPublicNumDto.setSpublishno(String.valueOf(iCount));
                            if (!examineBatchPublicNum(mediaPublicNumDto)) {
                                saveMediaPublicNumData(mediaPublicNumDto);
                            }
                            iCount++;
                        }
                    }
                    break;
                case "双周刊":
                    for (String s : strTimes) {
                        //循环刊期规则，判断周数是否相等，如果相等 ，则先判断是否已存在相同的设置 ，如果没有，则新增
                        int iW = DateUtil.dayOfWeek(dStart) - 1 == 0 ? 7 : DateUtil.dayOfWeek(dStart) - 1;
                        if (Integer.parseInt(s) == iW) {
                            mediaPublicNumDto.setDpublishtime(dStart);
                            mediaPublicNumDto.setSpublishno(String.valueOf(iCount));
                            if (!examineBatchPublicNum(mediaPublicNumDto)) {
                                saveMediaPublicNumData(mediaPublicNumDto);
                                // 因为是双周刊，所以下一期要加两周，因为最后面的循环还要增加一天，所以这儿只增加13天
                                dStart = DateUtil.offsetDay(dStart, 13);
                            }
                            iCount++;
                        }
                    }
                    break;
                case "旬刊":
                case "半月刊":
                case "月刊":
                    for (String s : strTimes) {
                        //循环刊期规则，判断周数是否相等，如果相等 ，则先判断是否已存在相同的设置 ，如果没有，则新增
                        int iW = DateUtil.dayOfMonth(dStart);
                        if (Integer.parseInt(s) == iW) {
                            mediaPublicNumDto.setDpublishtime(dStart);
                            mediaPublicNumDto.setSpublishno(String.valueOf(iCount));
                            if (!examineBatchPublicNum(mediaPublicNumDto)) {
                                saveMediaPublicNumData(mediaPublicNumDto);
                            }
                            iCount++;
                        }
                    }
                    break;
                case "双月刊":
                    for (String s : strTimes) {
                        // 月-日
                        String strTimeTmp[] = s.split("-");
                        //循环刊期规则，判断周数是否相等，如果相等 ，则先判断是否已存在相同的设置 ，如果没有，则新增
                        int iM = DateUtil.month(dStart) + 1;
                        int iD = DateUtil.dayOfMonth(dStart);
                        int iTmp = (Integer.parseInt(strTimeTmp[0]) + iCounter * 2) % 12;
                        iTmp = iTmp == 0 ? 12 : iTmp;
                        if (iTmp == iM && Integer.parseInt(strTimeTmp[1]) == iD) {
                            mediaPublicNumDto.setDpublishtime(dStart);
                            mediaPublicNumDto.setSpublishno(String.valueOf(iCount));
                            if (!examineBatchPublicNum(mediaPublicNumDto)) {
                                saveMediaPublicNumData(mediaPublicNumDto);
                                // 因为是双月刊，所以下一期要加两月，因为最后面的循环还要增加一天，所以这儿增加两月后要减一天
                                dStart = DateUtil.offsetMonth(dStart, 2);
                                dStart = DateUtil.offsetDay(dStart, -1);
                            }
                            iCount++;
                            iCounter++;
                        }
                    }
                    break;
                case "季刊":
                case "半年刊":
                case "年刊":
                    for(String s:strTimes){
                        // 月-日
                        String strTimeTmp[] =s.split("-");
                        //循环刊期规则，判断周数是否相等，如果相等 ，则先判断是否已存在相同的设置 ，如果没有，则新增
                        int iM = DateUtil.month(dStart) + 1;
                        int iD = DateUtil.dayOfMonth(dStart);
                        if(Integer.parseInt(strTimeTmp[0])==iM && Integer.parseInt(strTimeTmp[1])==iD){
                            mediaPublicNumDto.setDpublishtime(dStart);
                            if (!examineBatchPublicNum(mediaPublicNumDto)) {
                                saveMediaPublicNumData(mediaPublicNumDto);
                            }
                            iCount++;
                        }
                    }
                    break;
            }
            dStart = DateUtil.offsetDay(dStart, 1);
        }
    }

    @Override
    public void deleteMediaPublicNumByIds(String ids) throws Exception {
        String[] split = ids.split(",");
        List<Tbmediapublicnum> lspublicnumItems = this.lambdaQuery()
                .in(Tbmediapublicnum::getId, split)
                .list();
        String sInfo="";
        if(lspublicnumItems.size()>0){
            for(Tbmediapublicnum item:lspublicnumItems){
                // 判断业务
                Long count= new LambdaQueryChainWrapper<>(foldpageplanMapper)
                        .eq(Tbfoldpageplan::getMediaid, item.getMediaid())
                        .eq(Tbfoldpageplan::getPublishnum, Integer.valueOf(item.getSpublishno()))
                        .count();
                if(count==0){
                    innerInterceptor.recoredLog();
                   if(!removeById(item)){
                       throw  new RuntimeException("删除失败");
                   }
                }else {
                    sInfo +=item.getMedianame()+item.getSpublishno()+"已被使用，不能删除！"+"\r\n";
                }
            }
        }
        if(StrUtil.isNotBlank(sInfo)){
            throw  new RuntimeException("删除失败:"+sInfo);
        }
    }

    /**
     * examineBatchPublicNum
     * 方法功能:  判断刊期是否已存在
     * @author CGD
     * @date 2023/9/20 15:57
     * @param mediapublicnum
     * @return boolean
     */
    private boolean examineBatchPublicNum(MediaPublicNumDto mediapublicnum) {
        Long count = this.lambdaQuery()
                .eq(mediapublicnum.getMediaid() != null, Tbmediapublicnum::getMediaid, mediapublicnum.getMediaid())
                .eq(StrUtil.isNotBlank(mediapublicnum.getSpublishno()), Tbmediapublicnum::getSpublishno, mediapublicnum.getSpublishno())
                .count();
        return count > 0;
    }

    /**
     * saveMediaPublicNumData
     * 方法功能:  保存刊期
     * @author CGD
     * @date 2023/9/20 15:58
     * @param mediaPublicNumDto
     * @return boolean
     */
    private boolean saveMediaPublicNumData(MediaPublicNumDto mediaPublicNumDto) {
        Tbmediapublicnum mediapublicnum = new Tbmediapublicnum();
        BeanUtils.copyProperties(mediaPublicNumDto, mediapublicnum);
        innerInterceptor.recoredLog();
        int insert = tbmediapublicnumMapper.insert(mediapublicnum);
        return insert > 0;
    }
}
