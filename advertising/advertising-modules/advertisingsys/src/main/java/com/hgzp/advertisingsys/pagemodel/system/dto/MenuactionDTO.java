package com.hgzp.advertisingsys.pagemodel.system.dto;

import static com.hgzp.core.constant.ValidateParam.*;

import com.hgzp.core.constant.ValidateParam;
import com.hgzp.core.model.Tbmenuaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * MenuactionVo
 * 创建人：wangwk
 * 类描述：菜单行为按钮保存
 * 创建日期：2023/8/30 12:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuactionDTO {

    /**
     * 主键
     */
    @NotNull(message = "ID不可为空", groups = {edit.class})
    private Long id;

    /**
     * 菜单id
     */
    @NotNull(message = "菜单id不可为空", groups = {edit.class, add.class})
    private Long menuid;

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空", groups = {edit.class, add.class})
    private String sname;

    /**
     * 操作代码
     */
    @NotBlank(message = "操作代码不能为空", groups = {edit.class, add.class})
    private String skeycode;

    /**
     * 按钮url
     */
    private String sfunctionurl;

    /**
     * 设置中是否可以修改1可修改0不可修改
     */
    @NotNull(message = "可修改标志不能为空", groups = {edit.class, add.class})
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
    @NotNull(message = "有效标志不能为空", groups = {edit.class, add.class})
    private Boolean buse;

    //---------------------------


    /**
     * 范围id
     */
    private Long scopeId;

    public MenuactionDTO(Tbmenuaction tbmenuaction){
        BeanUtils.copyProperties(tbmenuaction, this);
    }


}
