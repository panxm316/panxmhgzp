package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 广告资源广告订单关联表
 * </p>
 *
 * @author wwk
 * @since 2023-11-10
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Accessors(chain = true)
@Getter
@Setter
public class Twadresourceadorder implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 资源申请id
     */
    private Long adresourceapplicationid;

    /**
     * 广告订单id
     */
    private Long adorderid;

    /**
     * 创建者id
     */
    private Long createempid;

    /**
     * 创建者名称
     */
    private String createempname;

    /**
     * 创建日期
     */
    private Date dcreatetime;

    /**
     * 备注
     */
    private String sremark;
}
