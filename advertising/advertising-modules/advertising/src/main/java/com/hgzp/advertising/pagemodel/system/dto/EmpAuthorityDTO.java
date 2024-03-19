package com.hgzp.advertising.pagemodel.system.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * EmpAuthorityDTO
 * 创建人：wangwk
 * 类描述：员工权限DTO
 * 创建日期：2023/11/3 9:48
 */
@Data
public class EmpAuthorityDTO {


    /**
     * 是否有权限
     */
    private boolean hasAuthority;

    /**
     * 媒体范围id列表
     */
    private List<Long> mediaIdList = new ArrayList<>();

    /**
     * 部门范围id列表
     */
    private List<Long> deptIdList = new ArrayList<>();

    /**
     * 员工范围id列表
     */
    private List<Long> empIdList = new ArrayList<>();

}
