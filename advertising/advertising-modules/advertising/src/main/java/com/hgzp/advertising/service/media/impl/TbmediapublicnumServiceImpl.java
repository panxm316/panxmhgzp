package com.hgzp.advertising.service.media.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.hgzp.advertising.service.media.TbmediapublicnumServiceI;
import com.hgzp.core.model.Tbmediapublicnum;
import com.hgzp.mapper.media.TbmediapublicnumMapper;
import com.hgzp.service.common.impl.MyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 TbmediapublicnumServiceImpl
 创建人：songly
 类描述：媒体刊期
 创建日期：2023/12/21 10:03
 */
@Service
public class TbmediapublicnumServiceImpl extends MyServiceImpl<TbmediapublicnumMapper, Tbmediapublicnum> implements TbmediapublicnumServiceI {
    @Autowired
    private TbmediapublicnumMapper tbmediapublicnumMapper;
    @Override
    public String  getMediaPublishNO(Long mediaId, Date spublishDate) {
        String sResult="";
        try {
            List<Tbmediapublicnum> tbPublicNum = new LambdaQueryChainWrapper<>(tbmediapublicnumMapper)
                    .eq(Tbmediapublicnum::getMediaid, mediaId)
                    .eq(Tbmediapublicnum::getDpublishtime, spublishDate)
                    .orderByDesc(Tbmediapublicnum::getSpublishno)
                    .list();
            if (tbPublicNum.size()>0) {
                sResult=tbPublicNum.get(0).getSpublishno();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sResult;
    }
}