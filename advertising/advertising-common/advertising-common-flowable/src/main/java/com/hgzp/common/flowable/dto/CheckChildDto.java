package com.hgzp.common.flowable.dto;

import lombok.Data;

import java.util.List;

/**
 * 检查是否是给定的子级
 *
 */
@Data
public class CheckChildDto {

    private String childId;

    private List<String> deptIdList;

}
