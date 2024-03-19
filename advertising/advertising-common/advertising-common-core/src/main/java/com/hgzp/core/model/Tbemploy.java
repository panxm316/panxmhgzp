package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.hgzp.core.annotation.LogData;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 人员表
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Getter
@Setter
@LogData(alias = "人员")
public class Tbemploy implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @LogData(showColumn = "susername")
    private Long id;

    /**
     * 部门id
     */
    @LogData(alias = "部门", mappedBy = "tbdept", mappedByColumn = "sdeptname")
    private Long deptid;

    /**
     * 登录名
     */
    @LogData(alias = "登录名")
    private String sloginname;

    /**
     * 密码
     */
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
    @LogData(alias = "电话", encrypt = LogData.EncryptType.SM4)
    private String sphone;

    /**
     * 姓名
     */
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
     * 内外部人员标志内部为1
     */
    @LogData(alias = "内外部人员标志")
    private Boolean binner;

    /**
     * 个推id
     */
    private String sclientid;

    /**
     * 是否管理员0：一般人员；1：普通管理员；2：超级管理员; 3:兼职超管
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
    @LogData(alias = "是否启用")
    private Boolean buse;
}
