package com.hgzp.advertising.service.finance;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hgzp.advertising.pagemodel.finance.dto.InvoiceFilesDTO;
import com.hgzp.core.model.Twinvoicefiles;
import com.hgzp.utils.model.LoginUser;

import java.util.List;

/**
 * <p>
 * 发票文件表(用于存放电子发票) 服务类
 * </p>
 *
 * @author muyn
 * @since 2023-12-26
 */
public interface InvoiceFilesServiceI extends IService<Twinvoicefiles> {

    /**
     * 保存发票文件
     * 方法功能:保存发票文件
     *
     * @param dtoList
     * @param user
     * @return void
     * @author yanz
     * @date 2023/12/28 15:24
     */
    void saveInvoiceFiles(List<InvoiceFilesDTO> dtoList, LoginUser user);

    /**
     * 据发票表id（InvoiceId）获取相关的文件
     * 方法功能:据发票表id（InvoiceId）获取相关的文件
     * @author yanz
     * @date 2023/12/28 16:14
     * @param invoiceId
     * @return java.util.List<com.hgzp.advertising.pagemodel.finance.dto.InvoiceFilesDTO>
     */
    List<InvoiceFilesDTO> getFilesByInvoiceId(Long invoiceId);
}
