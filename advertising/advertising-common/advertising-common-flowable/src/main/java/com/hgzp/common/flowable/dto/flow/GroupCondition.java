package com.hgzp.common.flowable.dto.flow;

import lombok.Data;

import java.util.List;

/**
 * 组条件
 */
@Data
public class GroupCondition {
    /**
     * 条件组内关系 真且假或
     */
    private Boolean mode;

    private List<Condition> conditionList;



}
