package com.hgzp.advertising.pagemodel.customer.vo;

import com.hgzp.core.page.BaseQueryInfo;
import lombok.Data;

/**
 * CustomerQueryVo
 * 创建人：suny
 * 类描述：客户查询条件对象
 * 创建日期：2024/1/26 16:40
 */
@Data
public class CustomerQueryVo extends BaseQueryInfo {
    /**
     * 部门id
     */
    private Long deptid;
    /**
     * 人员id
     */
    private Long employid;
    /**
     * 是否当前人员（0：否 1：是，如果是则表示只查询当前人员）
     */
    private Boolean bcurflag;
}

