/*
 * @Author: songly
 * @Date: 2023-10-12 13:40:39
 * @LastEditTime: 2023-10-27 10:06:12
 * @LastEditors: songly
 * @Description:工作流程条件
 */
import request from '@/utils/request'
import { TFlowCondition, TFlowConditionSearch } from '@/types/views/flow/flowcondition'
import type { TQueryInfo } from '@/types/common/index'
import qs from 'qs'
/**
 * 获取工作流程条件列表
 */
export const getFlowConditionPageListApi = (data: TFlowConditionSearch) => {
  return request({
    url: '/flow/flowcondition/getFlowConditionPageList',
    method: 'get',
    params: data
  })
}
/**
 * 获取工作流程列表
 */
export const getFlowTypesComboApi = () => {
  return request({
    url: '/flow/flowcondition/getFlowTypesCombo',
    method: 'get'
  })
}
/**
 * 新增工作流程条件
 * @param data
 * @returns
 */
export const saveFlowConditionApi = (data: TFlowCondition) => {
  return request({
    url: '/flow/flowcondition/saveFlowCondition',
    method: 'post',
    data: data
  })
}
/**
 * 根据Id获取工作流程条件信息
 */
export const getFlowConditionByIdApi = (data: string) => {
  return request({
    url: '/flow/flowcondition/getFlowConditionById',
    method: 'post',
    params: { id: data }
  })
}
/**
 * 修改工作流程条件
 * @param data
 * @returns
 */
export const updateFlowConditionApi = (data: TFlowCondition) => {
  return request({
    url: '/flow/flowcondition/updateFlowCondition',
    method: 'post',
    data: data
  })
}
/**
 * 删除工作流程条件
 */
export const deleteFlowConditionByIdApi = (data: string) => {
  return request({
    url: '/flow/flowcondition/deleteFlowConditionById',
    method: 'post',
    data: qs.stringify({ ids: data })
  })
}

/**
 * 获取工作流程条件最大序号
 * @returns
 */
export const getFlowConditionMaxSortApi = () => {
  return request({
    url: '/flow/flowcondition/getFlowConditionMaxSort',
    method: 'get'
  })
}
/**
 * 根据类型组获取条件信息
 * @param data
 * @returns
 */
export const getFlowConditionListApi = (data: string) => {
  return request({
    url: '/flow/flowcondition/getFlowConditionPageList',
    method: 'get',
    params: { sflowtype: data }
  })
}
