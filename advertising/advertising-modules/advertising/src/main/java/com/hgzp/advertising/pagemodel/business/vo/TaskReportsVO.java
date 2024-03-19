package com.hgzp.advertising.pagemodel.business.vo;

import com.hgzp.core.page.BaseQueryInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * TaskReportsVO
 * 创建人：lhl
 * 类描述：任务设置查询对象
 * 创建日期：2023/11/8 15:10
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TaskReportsVO extends BaseQueryInfo {
    /**
     * 任务额度类型 0：部门 1：人员
     */
    private String sTaskType;

    /**
     * 任务额度类别 0：年度 1：月度
     */
    private String sTaskCategory;
    /**
     * 任务日期 年：2023 月2023-01
     */
    private String staskdate;

    /**
     * 媒体ID
     */
    private String medidId;

    /**
     * 媒体ID
     */
    private String queryKey;
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
    /**
     * 按部门/人员查询（0：部门 1：人员，如果是人员则按照选择的部门查找所有部门下人员分别统计）
     */
    private Boolean bdeptflag;

    /**
     * 排序字段 0：日期 1：部门名称 2：人员名称 3：媒体名称
     */
    private int sortType;
    /**
     * 排序规则 0：升序  1：降序
     */
    private int sortRule;

}


