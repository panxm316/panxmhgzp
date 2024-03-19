package com.hgzp.advertisingsys.service.media.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertisingsys.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertisingsys.pagemodel.media.dto.MediaDTO;
import com.hgzp.advertisingsys.pagemodel.media.vo.MediaTypeVO;
import com.hgzp.advertisingsys.service.media.TbmediatypeServiceI;
import com.hgzp.core.exception.DataExistException;
import com.hgzp.core.model.Tbmedia;
import com.hgzp.core.model.Tbmediatype;
import com.hgzp.core.page.DataCombo;
import com.hgzp.core.page.Json;
import com.hgzp.mapper.media.TbmediatypeMapper;
import com.hgzp.service.common.impl.MyServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;

    /**
     * getMediaTypePageList
     * 方法功能:分页：获取媒体类型
     *
     * @param page
     * @param vo
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.core.model.Tbmediatype>
     * @author yanz
     * @date 2023/8/31 15:05
     */
    @Override
    public IPage<Tbmediatype> getMediaTypePageList(Page<Tbmediatype> page, MediaTypeVO vo) {
        LambdaQueryWrapper<Tbmediatype> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.hasText(vo.getQueryKey()), Tbmediatype::getSname, vo.getQueryKey())
                .eq(ObjectUtil.isNotNull(vo.getBuse()), Tbmediatype::getBuse, vo.getBuse());
        return mapper.selectPage(page, lqw);
    }

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

    /**
     * deleteMediaTypeByIds
     * 方法功能:据id批量删除
     *
     * @param idList
     * @return boolean
     * @author yanz
     * @date 2023/8/25 10:33
     */
    @Override
    public boolean deleteMediaTypeByIds(List<String> idList) {
        mapper.deleteBatchIds(idList);
        return true;
    }

    @Override
    public Json doDefaultLogic(Tbmediatype tbmediatype) {
        if (tbmediatype.getBdefault()) {
            List<Tbmediatype> list = mapper.selectList(null);
            for (Tbmediatype t : list) {
                t.setBdefault(false);
                mapper.updateById(t);
            }
        } else {
            Long count = new LambdaQueryChainWrapper<>(mapper)
                    .ne(tbmediatype.getId() != null, Tbmediatype::getId, tbmediatype.getId())
                    .eq(Tbmediatype::getBdefault, true)
                    .count();
            if (count == 0||count>1) {
                return Json.fail("至少要有一条记录默认选中！");
            }
            if (count>1) {
                return Json.fail("只能有一条记录默认选中！");
            }
        }
        return Json.success();
    }

    @Override
    public void saveMediaType(Tbmediatype tbmediatype) {
        if (isExistFold(tbmediatype)) {
            throw new DataExistException("已存在同名类型或相同关键字");
        }
        tbmediatype.setId(IdUtil.getSnowflakeNextId());

        innerInterceptor.recoredLog();
        mapper.insert(tbmediatype);
    }

    @Override
    public void updateMediaType(Tbmediatype tbmediatype) {
        if (isExistFold(tbmediatype)) {
            throw new DataExistException("已存在同名类型或相同关键字");
        }
        innerInterceptor.recoredLog();
        mapper.updateById(tbmediatype);
    }

    /**
     * isExistFold
     * 方法功能: 判断是否有重名
     * @author suny
     * @date 2023/9/20 13:23
     * @param tbmediatype
     * @return boolean
     */
    public boolean isExistFold(Tbmediatype tbmediatype) {
        Long count = new LambdaQueryChainWrapper<>(mapper)
                .and(item->item
                        .eq(Tbmediatype::getSname, tbmediatype.getSname())
                        .or()
                        .eq(Tbmediatype::getSkey, tbmediatype.getSkey())
                )
                .ne(tbmediatype.getId() != null, Tbmediatype::getId, tbmediatype.getId())
                .count();
        return count > 0;
    }
}
