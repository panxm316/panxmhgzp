package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.hgzp.core.annotation.LogData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 人员角色表
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Tbemployrole implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 角色权限结束日期
     */
    private Date ddestroydate;

    /**
     * 员工id
     */
    @LogData(alias = "人员", mappedBy = "tbemploy", mappedByColumn = "susername")
    private Long employid;

    /**
     * 角色id
     */
    @LogData(alias = "角色", mappedBy = "tbrole", mappedByColumn = "sname")
    private Long roleid;

    public Tbemployrole(Long employid, Long roleid){
        this.roleid = roleid;
        this.employid = employid;
    }

}
