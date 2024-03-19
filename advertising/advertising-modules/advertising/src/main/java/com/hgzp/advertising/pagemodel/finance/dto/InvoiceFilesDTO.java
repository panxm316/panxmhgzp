package com.hgzp.advertising.pagemodel.finance.dto;

import com.hgzp.core.model.Twinvoicefiles;
import com.hgzp.core.page.BaseDTO;
import com.hgzp.utils.file.UfileUtil;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * <p>
 * 发票文件表(用于存放电子发票) DTO 数据传输对象
 * </p>
 *
 * @author wangxk
 * @since 2023-12-26
 */
@Data
public class InvoiceFilesDTO extends BaseDTO {
    /**
     * 文件统一文件地址
     */
    private String url;
    /**
     * 文件下载url  视频转码播放地址
     */
    private String durl;
    /**
     * 是否删除
     */
    private Boolean bdelete;
    /**
     * 主键
     */
    private Long id;
    /**
     * 发票表id
     */
    private Long invoiceid;
    /**
     * 发票编码
     */
    private String invoicecode;
    /**
     * 创建人id
     */
    private Long createempid;
    /**
     * 创建人名称
     */
    private String createempname;
    /**
     * 创建日期
     */
    private Date dcreatetime;
    /**
     * 文件格式
     */
    private String sfileformat;
    /**
     * 统一文件ID
     */
    private String sfileid;
    /**
     * 发票号
     */
    private String invoice;
    /**
     * 文件大小
     */
    private String sfilesize;
    /**
     * 源文件名
     */
    private String soriginalfile;
    /**
     * 文件格式类型(Photo、Video、Audio、Office、Application)
     */
    private String sfiletype;

    /**
     * Twinvoicefiles 转 InvoiceFilesDTO
     * url和bdelete 没有注入
     * url考虑： uWebURL + entity.getSfileid()
     * @param entity
     * @return
     */
    public static InvoiceFilesDTO parseToDTO(Twinvoicefiles entity) {
        InvoiceFilesDTO invoicefilesDTO = new InvoiceFilesDTO();
        BeanUtils.copyProperties(entity, invoicefilesDTO);
        String durl = UfileUtil.getStaticUrl(entity.getSfileid(), entity.getSfileformat());
        invoicefilesDTO.setDurl(durl);
        return invoicefilesDTO;
    }

    public static Twinvoicefiles parseToEntity(InvoiceFilesDTO dto) {
        Twinvoicefiles entity = new Twinvoicefiles();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }
}
