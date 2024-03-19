package com.hgzp.advertisingsys.pagemodel.system.dto;

import com.hgzp.core.annotation.LogData;
import com.hgzp.core.model.Tbrole;
import com.hgzp.core.page.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.hgzp.core.constant.ValidateParam.add;
import static com.hgzp.core.constant.ValidateParam.edit;

/**
 * RoleModel
 * 创建人：wangwk
 * 类描述：角色保存、更新model
 * 创建日期：2023/8/23 16:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@LogData
public class RoleDTO extends BaseDTO {

    @NotNull(message = "id不能为空", groups =  {edit.class})
    @LogData(showColumn = "sname")
    private Long id;

    /**
     * 名称
     */
    @NotBlank(message = "角色名不能为空", groups =  {edit.class, add.class})
    @LogData(alias = "角色名称")
    private String sname;

    /**
     * 是否全部权限
     */
    @LogData(alias = "全部权限")
    private Boolean ball;

    /**
     * 说明
     */
    @LogData(alias = "描述")
    private String sdesc;

    /**
     * 角色类型：0-普通，1-子报管理员，2-超级管理员
     */
    private Integer iroletype;

    /**
     * 是否单独赋权
     */
    private Boolean bselfrole;


    /**
     * 是否有效
     */
    @LogData(alias = "是否有效")
    private Boolean buse;

    /**
     * 序号
     */
    @LogData(alias = "序号")
    private Integer isort;

    //=====================================

    /**
     * 单独赋权时的人员id
     */
    private Long employId;

    /**
     * 菜单id
     */
    @NotBlank(message = "菜单不能为空", groups = {edit.class, add.class})
    @LogData(alias = "菜单名称", mappedBy = "Tbmenu", mappedByColumn = "sname")
    private String menuIds;

    /**
     * 菜单名称
     */
    private String menuNames;


    public RoleDTO(Tbrole tbrole){
        BeanUtils.copyProperties(tbrole, this);
    }


}
