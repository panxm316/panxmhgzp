package com.hgzp.advertising.service.business.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.pagemodel.business.vo.PreinvoiceApplicationFilesVO;
import com.hgzp.advertising.service.business.TwpreinvoiceapplicationfileServiceI;
import com.hgzp.advertising.service.resource.TwresourcesServiceI;
import com.hgzp.core.model.Twpreinvoiceapplicationfile;
import com.hgzp.mapper.business.TwpreinvoiceapplicationfileMapper;
import com.hgzp.utils.file.UfileUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 预开发票申请文件表 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-11-08
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TwpreinvoiceapplicationfileServiceImpl extends ServiceImpl<TwpreinvoiceapplicationfileMapper, Twpreinvoiceapplicationfile> implements TwpreinvoiceapplicationfileServiceI {

    @Autowired
    TwresourcesServiceI sourceServiceI;

    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;

    /**
     * 按预开申请id删除文件
     * 方法功能:按预开申请id删除文件
     *
     * @param ids Long型List
     * @return boolean
     * @author yanz
     * @date 2023/11/17 13:27
     */
    @Override
    public boolean deleteByApplyIds(List<Long> ids) {
        Long count = this.lambdaQuery().in(Twpreinvoiceapplicationfile::getPreinvoiceapplicationid, ids).count();
        if (count == 0) {
            return true;
        }
        innerInterceptor.recoredLog();
        return this.lambdaUpdate().in(Twpreinvoiceapplicationfile::getPreinvoiceapplicationid, ids).remove();
    }

    /**
     * 按申请id获取文件
     * 方法功能:按申请id获取文件，仅返回删除标记为false的
     *
     * @param applyId
     * @return java.util.List<com.hgzp.core.model.Twpreinvoiceapplicationfile>
     * @author yanz
     * @date 2023/11/28 16:41
     */
    @Override
    public List<Twpreinvoiceapplicationfile> getFilesByApplicationId(Long applyId) {
        return this.lambdaQuery()
                .eq(Twpreinvoiceapplicationfile::getPreinvoiceapplicationid, applyId)
                .eq(Twpreinvoiceapplicationfile::getBdelete, false)
                .list();
    }


    /**
     * 按申请id获取文件
     * 方法功能:按申请id获取文件
     *
     * @param preInvoiceApplicationId
     * @return java.util.List<com.hgzp.advertising.pagemodel.business.vo.PreinvoiceApplicationFilesVO>
     * @date 2023/11/17 13:27
     */
    @Override
    public List<PreinvoiceApplicationFilesVO> getFilesVOByApplicationId(Long preInvoiceApplicationId) {
        return getFilesByApplicationId(preInvoiceApplicationId).stream().map(file -> {
            PreinvoiceApplicationFilesVO vo = new PreinvoiceApplicationFilesVO();
            BeanUtils.copyProperties(file, vo);
            String url = UfileUtil.getStaticUrl(file.getSfileid(), file.getSfileformat());
            vo.setDurl(url);
            return vo;
        }).collect(java.util.stream.Collectors.toList());
    }
}
