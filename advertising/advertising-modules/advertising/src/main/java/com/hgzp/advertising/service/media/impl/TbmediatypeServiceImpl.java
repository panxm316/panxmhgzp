package com.hgzp.advertising.service.media.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hgzp.advertising.service.media.TbmediatypeServiceI;
import com.hgzp.core.model.Tbmediatype;
import com.hgzp.core.page.DataCombo;
import com.hgzp.mapper.media.TbmediatypeMapper;
import com.hgzp.service.common.impl.MyServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 媒体类型 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TbmediatypeServiceImpl extends MyServiceImpl<TbmediatypeMapper, Tbmediatype> implements TbmediatypeServiceI {
    @Resource
    private TbmediatypeMapper mapper;

    @Override
    public List<DataCombo> getMediaTypeCombo() throws Exception {
        LambdaQueryWrapper<Tbmediatype> lqw = Wrappers.lambdaQuery();
        lqw.ge(Tbmediatype::getBuse, true);
        lqw.orderByAsc(Tbmediatype::getIsort);
        List<Tbmediatype> list = mapper.selectList(lqw);
        List<DataCombo> combo = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (Tbmediatype type : list) {
                DataCombo d = new DataCombo();
                d.setId(type.getSkey());
                d.setName(type.getSname());
                d.setBdefault(type.getBdefault());
                combo.add(d);
            }
        }
        return combo;
    }

    @Override
    public List<Tbmediatype> listUsableMediaType() {
        return list(Wrappers.<Tbmediatype>lambdaQuery()
                .eq(Tbmediatype::getBuse, true).orderByAsc(Tbmediatype::getIsort));
    }
}
