package com.hgzp.advertisingsys.pagemodel.system.vo;

import com.hgzp.core.model.Tbmenuaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

/**
 * MenuactionVo
 * 创建人：wangwk
 * 类描述：菜单行为按钮展示
 * 创建日期：2023/8/30 12:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuactionVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 菜单id
     */
    private Long menuid;

    /**
     * 名称
     */
    private String sname;

    /**
     * 操作代码
     */
    private String skeycode;

    /**
     * 按钮url
     */
    private String sfunctionurl;

    /**
     * 设置中是否可以修改1可修改0不可修改
     */
    private Boolean bmodify;

    /**
     * 按钮组，用于同组赋权
     */
    private String sgroupkey;

    /**
     * 序号
     */
    private Integer isort;

    /**
     * 是否有效
     */
    private Boolean buse;

    //---------------------------

    /**
     * 范围名称
     */
    private String scopeName;

    /**
     * 范围id
     */
    private Long scopeId;

    public MenuactionVO(Tbmenuaction tbmenuaction){
        BeanUtils.copyProperties(tbmenuaction, this);
    }


}
