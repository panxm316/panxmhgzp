package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 资源表
 * </p>
 *
 * @author wwk
 * @since 2023-10-26
 */
@Getter
@Setter
public class Twresources implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 文件格式
     */
    private String sfileformat;

    /**
     * 统一文件ID
     */
    private String sfileid;

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
     * 创建日期
     */
    private Date dcreatetime;

    /**
     * 创建者ID
     */
    private Long employid;

    /**
     * 文件描述
     */
    private String sdescription;

    /**
     * Twinvoicefiles 没有 sdescription 列
     * Twinvoicefiles 的 setEmployid 考虑设置当前用户
     *
     * @param twinvoicefiles
     * @return com.hgzp.core.model.Twresources
     * @author yanz
     * @date 2023/12/28 15:51
     */
    public static Twresources from(Twinvoicefiles twinvoicefiles) {
        if (twinvoicefiles == null) {
            return null;
        }
        // Twinvoicefiles 没有 sdescription 列
        // Twinvoicefiles 的 setEmployid 考虑设置当前用户
        Twresources twresources = new Twresources();
        twresources.setId(twinvoicefiles.getId());
        twresources.setSfileformat(twinvoicefiles.getSfileformat());
        twresources.setSfileid(twinvoicefiles.getSfileid());
        twresources.setSfilesize(twinvoicefiles.getSfilesize());
        twresources.setSoriginalfile(twinvoicefiles.getSoriginalfile());
        twresources.setSfiletype(twinvoicefiles.getSfiletype());
        twresources.setDcreatetime(new Date());
        twresources.setEmployid(twinvoicefiles.getCreateempid());

        return twresources;
    }
}
