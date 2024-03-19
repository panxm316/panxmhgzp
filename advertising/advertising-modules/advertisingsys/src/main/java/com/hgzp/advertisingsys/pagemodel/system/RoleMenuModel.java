package com.hgzp.advertisingsys.pagemodel.system;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

/**
 * RoleMenuModel
 * 创建人：wangwk
 * 类描述：角色菜单对应的按钮权限、范围 实体
 * 创建日期：2023/8/25 9:51
 */
@Data
@NoArgsConstructor
public class RoleMenuModel {

    /**
     * 菜单id
     */
    private Long menuid;

    /**
     * 菜单别名
     */
    private String menuname;

    /**
     * 菜单分组名称
     */
    private String menutype;

    /**
     * 菜单包含的按钮
     */
    private List<ActiveModel> actives;

    /**
     * 是否选中
     */
    private Boolean checked;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleMenuModel that = (RoleMenuModel) o;
        boolean fieldEqual = Objects.equals(menuid, that.menuid) && Objects.equals(menuname, that.menuname) && Objects.equals(menutype, that.menutype) && Objects.equals(checked, that.checked);
        boolean listEqual = true;
        if (actives == that.actives) {
            return fieldEqual;
        }

        if (actives != null && that.actives != null) {
            if (actives.size() != that.actives.size()) {
                return false;
            }
            for (ActiveModel active : actives) {
                listEqual = that.actives.contains(active);
                if (!listEqual) {
                    return false;
                }
            }
        } else {
            return false;
        }

        return fieldEqual && listEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(menuid, menuname, menutype, actives, checked);
    }
}
