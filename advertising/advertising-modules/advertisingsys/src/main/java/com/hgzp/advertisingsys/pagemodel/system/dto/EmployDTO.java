package com.hgzp.advertisingsys.pagemodel.system.dto;

import com.hgzp.core.annotation.LogData;
import com.hgzp.core.constant.ValidateParam;
import com.hgzp.core.model.Tbemploy;
import com.hgzp.core.page.BaseDTO;
import com.hgzp.core.page.BaseQueryInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

import static com.hgzp.core.constant.ValidateParam.*;

/**
 * EmployModel
 * 创建人：wangwk
 * 类描述：人员保存更新model
 * 创建日期：2023/8/21 16:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@LogData(alias = "人员")
public class EmployDTO  extends BaseDTO {

    @NotNull(message = "人员id不能为空", groups = {edit.class, detail.class})
    @LogData(showColumn = "susername")
    private Long id;

    /**
     * 部门id
     */
    @NotNull(message = "部门id不能为空", groups = {edit.class, add.class})
    @LogData(alias = "部门", mappedBy = "tbdept", mappedByColumn = "sdeptname")
    private Long deptid;

    /**
     * 登录名
     */
    @NotBlank(message = "登录名不能为空", groups = {edit.class, add.class})
    @LogData(alias = "登录名")
    private String sloginname;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空", groups = {edit.class, add.class})
    private String spassword;

    /**
     * 人员类型
     */
    @LogData(alias = "人员类型")
    private String semploytype;

    /**
     * 头像
     */
    private String simg;

    /**
     * 电话
     */
    @NotBlank(message = "电话不能为空", groups = {edit.class, add.class, detail.class})
    @Pattern(regexp = "^(0|86|17951)?(13[0-9]|14[0-9]|15[0-9]|16[0-9]|17[0-9]|18[0-9]|19[0-9]|14[57])[0-9]{8}$", message = "电话号码不符合规范")
    @LogData(alias = "电话", encrypt = LogData.EncryptType.SM4)
    private String sphone;

    /**
     * 姓名
     */
    @NotBlank(message = "姓名不能为空", groups = {edit.class, add.class, detail.class})
    @LogData(alias = "姓名")
    private String susername;

    /**
     * 微信号
     */
    private String sweixin;

    /**
     * 兼职部门id
     */
    private String parttimedeptid;

    /**
     * 兼职部门
     */
    @LogData(alias = "兼职部门")
    private String snameparttimedept;

    /**
     * 内外部我员标志内部为1
     */
    private Boolean binner;

    /**
     * 个推id
     */
    private String sclientid;

    /**
     * 是否管理员0：一般人员；1：普通管理员；2：超级管理员
     */
    @LogData(alias = "是否管理员")
    private Integer badminflag;

    /**
     * 是否业务员
     */
    @LogData(alias = "是否业务员")
    private Boolean bsalesman;

    /**
     * 子管理员管理部门
     */
    @LogData(alias = "子管理员管理部门", mappedBy = "tbdept", mappedByColumn = "sdeptname")
    private String smanagedepts;

    /**
     * 是否单独赋权
     */
    private Boolean bselfrole;

    /**
     * 上次密码修改时间
     */
    private Date dpasslastmodtime;

    /**
     * cas人员id
     */
    private String sguidcas;

    /**
     * cas人员是否推送
     */
    private Boolean bcaspush;

    /**
     * 是否负责人
     */
    @LogData(alias = "是否负责人")
    private Boolean blead;

    /**
     * 职务ID
     */
    private Long dutiesid;

    /**
     * 职务名称
     */
    @LogData(alias = "职务名称")
    private String sdutiesname;

    /**
     * 性别
     */
    @NotNull(message = "性别不能为空", groups = {edit.class, add.class, detail.class})
    private Boolean bsex;

    /**
     * 邮箱
     */
    @LogData(alias = "邮箱", encrypt = LogData.EncryptType.SM4)
    private String semail;


    /**
     * 序号
     */
    private Long isort;

    /**
     * 是否启用
     */
    @NotNull(message = "人员是否启用标志不能为空", groups = {edit.class, add.class})
    @LogData(alias = "是否启用")
    private Boolean buse;

    /**
     * 角色id
     */
    @LogData(alias = "角色名称", mappedBy = "tbrole", mappedByColumn = "sname")
    private String roleIds;

    //======================================

    /**
     * 角色名称
     */
    private String roleNames;


    public EmployDTO(Tbemploy tbemploy){
        BeanUtils.copyProperties(tbemploy, this);
    }

}
