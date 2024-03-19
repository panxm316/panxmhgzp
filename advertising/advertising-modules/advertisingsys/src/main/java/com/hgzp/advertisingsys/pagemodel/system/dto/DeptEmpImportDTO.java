package com.hgzp.advertisingsys.pagemodel.system.dto;

import cn.hutool.core.annotation.Alias;
import lombok.Data;

/**
 * DeptEmpImportDTO
 * 创建人：wangwk
 * 类描述：部门人员导入
 * 创建日期：2023/9/2 13:12
 */
@Data
public class DeptEmpImportDTO {

    @Alias(value = "所在媒体")
    private String secondDeptName;
    @Alias(value = "部门名称")
    private String thirdDeptName;
    @Alias(value = "姓名")
    private String empName;
    @Alias(value = "手机号")
    private String phone;
    @Alias(value = "登录账号")
    private String loginName;
    @Alias(value = "登录密码")
    private String password;
    @Alias(value = "集团单位")
    private String rootDeptName;





}
