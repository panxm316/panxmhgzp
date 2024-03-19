package com.hgzp.advertisingsys.pagemodel.system.dto;

import com.hgzp.advertisingsys.pagemodel.system.RoleMenuModel;
import com.hgzp.core.page.BaseDTO;
import lombok.Data;

import java.util.List;

/**
 * RoleActionDTO
 * 创建人：wangwk
 * 类描述：保存角色行为、范围model
 * 创建日期：2023/9/1 16:50
 */
@Data
public class RoleActionDTO  extends BaseDTO {

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 菜单关联的按钮、范围
     */
    private List<RoleMenuModel> roleMenuModelList;


}
