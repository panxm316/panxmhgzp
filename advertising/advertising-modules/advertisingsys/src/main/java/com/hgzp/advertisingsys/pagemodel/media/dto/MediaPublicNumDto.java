package com.hgzp.advertisingsys.pagemodel.media.dto;

import com.hgzp.core.constant.ValidateParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * MediaPublicNumDto
 * 创建人：CGD
 * 类描述：TODO
 * 创建日期：2023/9/8 12:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MediaPublicNumDto {

    /**
     * 媒体id
     */
    @NotNull(message = "媒体ID不能为空")
    private Long mediaid;
    /**
     * 媒体名称
     */
    private String medianame;
    /**
     * 刊期类型
     */
    @NotNull(message = "刊期类型不能为空")
    private String mediapublictype;

    /**
     * 发布日期
     */
    @NotNull(message = "发布日期不能为空")
    private String spublishnum;

    /**
     * 后端需要存的发布时间
     */
    @Transient
    private Date dpublishtime;
    /**
     * 起始期号
     */
    @NotNull(message = "起始期号不能为空")
    private String spublishno;
    /**
     * 开始日期
     */
    @NotNull(message = "开始日期不能为空")
    private Date startTime;
    /**
     * 结束日期
     */
    @NotNull(message = "结束日期不能为空")
    private Date endTime;

    /**
     * 是否有效
     */
    private Boolean buse;
}

