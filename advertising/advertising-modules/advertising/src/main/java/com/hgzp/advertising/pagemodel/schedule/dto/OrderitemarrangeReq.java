package com.hgzp.advertising.pagemodel.schedule.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.hgzp.core.model.Tworderitemarrange;
import com.hgzp.core.page.BaseQueryInfo;
import lombok.*;
import com.hgzp.core.annotation.LogData;
import com.hgzp.core.page.BaseDTO;
import static com.hgzp.core.constant.ValidateParam.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.BeanUtils;

/**
 * <p>
 * 广告安排表 DTO 数据传输对象
 * </p>
 *
 * @author muyn
 * @since 2023-12-06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderitemarrangeReq {
    /**
     * 媒体类型key
     */
    private String mediatypekey;
    /**
     * 编辑人员id
     */
    private Long editorid;
    /**
     * 刊发日期
     */
    private Date dpublishdate;
    /**
     * 媒体id
     */
    private Long mediaid;
    /**
     * 叠次id
     */
    private Long foldid;
    /**
     * 叠次版本id
     */
    private Long foldareaverid;
    /**
     * 版号
     */
    private Integer pagenum;
    /**
     * 广告类型id
     */
    private Long adtypeid;
    /** 查询关键字*/
    private String queryKey;
    /**
     * 录入方式 0-正常 1-广告预约 2-快速预约 3-补刊
     */
    private Integer ibooktype;

}
