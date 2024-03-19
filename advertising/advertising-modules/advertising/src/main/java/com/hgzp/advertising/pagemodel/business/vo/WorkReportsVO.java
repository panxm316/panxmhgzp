package com.hgzp.advertising.pagemodel.business.vo;

import com.hgzp.core.annotation.LogData;
import com.hgzp.core.page.BaseQueryInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * WorkReportsVO
 * 创建人：suny
 * 类描述：工作报告查询对象
 * 创建日期：2023/10/28 16:12
 *
 * @测试：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkReportsVO extends BaseQueryInfo {
    /**
     * 部门id
     */
    private Long deptid;

    /**
     * 人员ID
     */
    private Long employid;
    /**
     * 报告类型(0-日报、1-周报、2-月报、3-年报、4-客户服务）
     */
    private Integer iworktype;

    /**
     * 审批状态
     */
    private Integer iapprovestatus;
}
