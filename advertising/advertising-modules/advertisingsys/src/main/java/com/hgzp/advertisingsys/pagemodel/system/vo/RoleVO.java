package com.hgzp.advertisingsys.pagemodel.system.vo;

import static com.hgzp.core.constant.ValidateParam.*;
import com.hgzp.core.model.Tbrole;
import com.hgzp.core.page.BaseQueryInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * RoleModel
 * 创建人：wangwk
 * 类描述：角色展示、查询model
 * 创建日期：2023/8/23 16:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleVO  {

    private Long id;

    /**
     * 名称
     */
    private String sname;

    /**
     * 是否全部权限
     */
    private Boolean ball;

    /**
     * 说明
     */
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
    private Boolean buse;

    /**
     * 序号
     */
    private Integer isort;

    //=====================================

    /**
     * 单服赋权时的人员id
     */
    private Long employId;

    /**
     * 菜单id
     */
    private String menuIds;

    /**
     * 菜单名称
     */
    private String menuNames;

    /**
     * 角色关联的部门名称
     */
    private String deptNames;

    public RoleVO(Tbrole tbrole){
        BeanUtils.copyProperties(tbrole, this);
    }


}
