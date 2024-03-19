package com.hgzp.advertisingsys.service.media.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertisingsys.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertisingsys.pagemodel.media.vo.PageSizeVO;
import com.hgzp.advertisingsys.service.media.TbpagesizeServiceI;
import com.hgzp.core.model.Tbfoldpageplan;
import com.hgzp.core.model.Tbmedia;
import com.hgzp.core.model.Tbpagesize;
import com.hgzp.core.model.Tworderitem;
import com.hgzp.core.page.Json;
import com.hgzp.mapper.media.TbpagesizeMapper;
import com.hgzp.mapper.schedule.TbfoldpageplanMapper;
import com.hgzp.service.common.impl.MyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 报纸版心 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TbpagesizeServiceImpl extends MyServiceImpl<TbpagesizeMapper, Tbpagesize> implements TbpagesizeServiceI {

    @Resource
    private TbpagesizeMapper mapper;
    @Autowired
    private TbfoldpageplanMapper foldpageplanMapper;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;
    /**
     * getPageSizePageList
     * 方法功能:分页：获取报纸版心
     *
     * @param page
     * @param vo
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.core.model.Tbpagesize>
     * @author yanz
     * @date 2023/8/31 15:04
     */
    @Override
    public IPage<Tbpagesize> getPageSizePageList(Page<Tbpagesize> page, PageSizeVO vo) {
        LambdaQueryWrapper<Tbpagesize> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.hasText(vo.getQueryKey()), Tbpagesize::getSname, vo.getQueryKey())
                .eq(ObjectUtil.isNotNull(vo.getBuse()), Tbpagesize::getBuse, vo.getBuse());
        return mapper.selectPage(page, lqw);
    }

    /**
     * deletePageSizeByIds
     * 方法功能:据id批量删除
     *
     * @param ids
     * @return boolean
     * @author yanz
     * @date 2023/8/25 10:53
     */
    @Override
    public void deletePageSizeByIds(String ids) throws Exception{
//        mapper.deleteBatchIds(idList);
//        return true;
        String[] split = ids.split(",");
        List<Tbpagesize> lsMediaItems = this.lambdaQuery()
                .in(Tbpagesize::getId, split)
                .list();
        String sInfo="";
        if(lsMediaItems.size()>0){
            for(Tbpagesize item:lsMediaItems){
                // 判断业务
                Long count= new LambdaQueryChainWrapper<>(foldpageplanMapper)
                        .eq(Tbfoldpageplan::getPagesizeid, item.getId())
                        .count();
                if(count==0){
                    innerInterceptor.recoredLog();
                    if(!removeById(item)){
                        throw  new RuntimeException("删除失败");
                    }
                }else {
                    sInfo +=item.getSname()+"已被使用，不能删除！"+"\r\n";
                }
            }
        }
        if(StrUtil.isNotBlank(sInfo)){
            throw  new RuntimeException("删除失败:"+sInfo);
        }
    }

    @Override
    public Json doDefaultLogic(Tbpagesize tpagesize) {
        if (tpagesize.getBdefault()) {
            List<Tbpagesize> list = mapper.selectList(null);
            for (Tbpagesize t : list) {
                t.setBdefault(false);
                mapper.updateById(t);
            }
        } else {
            Long count = new LambdaQueryChainWrapper<>(mapper)
                    .ne(tpagesize.getId() != null, Tbpagesize::getId, tpagesize.getId())
                    .eq(Tbpagesize::getBdefault, true)
                    .count();
            if (count == 0) {
                return Json.fail("至少要有一条记录默认选中！");
            }
            if (count > 1) {
                return Json.fail("只能有一条记录默认选中！");
            }
        }
        return Json.success();
    }

    /**
     * isDuplicateSname
     * 方法功能:重名判断
     *
     * @param id
     * @param sname
     * @return boolean
     * @author yanz
     * @date 2024/2/27 14:27
     */
    @Override
    public boolean isDuplicateSname(Long id, String sname) {
        LambdaQueryWrapper<Tbpagesize> wrapper = Wrappers.lambdaQuery();
        wrapper.ne(id != null, Tbpagesize::getId, id);
        wrapper.eq(StringUtils.hasText(sname), Tbpagesize::getSname, sname);
        Long count = mapper.selectCount(wrapper);
        return count > 0;
    }
}
