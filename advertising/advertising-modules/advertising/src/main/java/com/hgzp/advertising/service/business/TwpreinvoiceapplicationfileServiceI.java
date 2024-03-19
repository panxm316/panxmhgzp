package com.hgzp.advertising.service.business;

import com.hgzp.advertising.pagemodel.business.vo.PreinvoiceApplicationFilesVO;
import com.hgzp.core.model.Twpreinvoiceapplicationfile;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 预开发票申请文件表 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-11-08
 */
public interface TwpreinvoiceapplicationfileServiceI extends IService<Twpreinvoiceapplicationfile> {

    /**
     * 按申请id删除文件
     * 方法功能:按申请id删除文件
     *
     * @param ids Long型List
     * @return boolean
     * @author yanz
     * @date 2023/11/17 13:27
     */
    boolean deleteByApplyIds(List<Long> ids);

    /**
     * 按申请id获取文件
     * 方法功能:按申请id获取文件
     *
     * @param applyId
     * @return java.util.List<com.hgzp.core.model.Twpreinvoiceapplicationfile>
     * @author yanz
     * @date 2023/11/28 16:41
     */
    List<Twpreinvoiceapplicationfile> getFilesByApplicationId(Long applyId);

    /**
     * 按申请id获取文件VO
     * 方法功能:按申请id获取文件
     *
     * @param preInvoiceApplicationId
     * @return java.util.List<com.hgzp.advertising.pagemodel.business.vo.PreinvoiceApplicationFilesVO>
     * @date 2023/11/17 13:27
     */
    List<PreinvoiceApplicationFilesVO> getFilesVOByApplicationId(Long preInvoiceApplicationId);
}
