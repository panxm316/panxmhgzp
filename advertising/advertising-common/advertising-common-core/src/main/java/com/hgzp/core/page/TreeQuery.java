package com.hgzp.core.page;

import lombok.Data;

/**
 * DeptVO
 * 创建人：wangwk
 * 类描述：部门查询对象
 * 创建日期：2023/8/31 15:03
 */
@Data
public class TreeQuery extends BaseQueryInfo {

    /**
     * 是否显示根节点【所有部门】
     */
    private boolean showRoot;

    /**
     * 包含显示的部门id字符串，多个用逗号分隔
     */
    private String containsIds;

    /**
     * 要排除不显示的id字符串，多个用逗号分隔
     */
    private String excludeIds;


}
