/*
 * @Author: suny
 * @Date: 2023-10-12 14:01:05
 * @LastEditTime: 2024-03-08 10:43:48
 * @LastEditors: wanghl
 * @Description:
 */
import request from '@/utils/request'
import qs from 'qs'
/**
 * 创建流程
 *
 * @param data
 */
export function addFlow(data: any) {
  return request({
    url: '/flow/process/create',
    method: 'post',
    data: data
  })
}

/**
 * 获取流程详细信息
 *
 * @param data
 */
export function getFlowDetail(flowId: string) {
  return request({
    url: '/flow/process/getDetail?flowId=' + flowId,
    method: 'get'
  })
}
/**
 * 停用流程
 *
 * @param data
 */
export function disableFlow(flowId: string) {
  return request({
    url: '/flow/process/update/' + flowId + '?type=stop',
    method: 'put'
  })
}
/**
 * 删除流程
 *
 * @param data
 */
export function deleteFlow(flowId: string) {
  return request({
    url: '/flow/process/update/' + flowId + '?type=delete',
    method: 'put'
  })
}
/**
 * 启用流程
 *
 * @param data
 */
export function enableFlow(flowId: string) {
  return request({
    url: '/flow/process/update/' + flowId + '?type=using',
    method: 'put'
  })
}

/**
 * 发起流程
 *
 * @param data
 */
export function startFlow(obj: any) {
  return request({
    url: '/flow/process-instance/startProcessInstance',
    method: 'post',
    data: obj
  })
}
export function getFormCondition() {
  return request({
    url: '/flow/form/getFormCondition',
    method: 'post'
  })
}

/**
 * 获取工作流程列表
 * @param data
 * @returns
 */
export function getListProcessApi(data: { groupId: string }) {
  return request({
    url: '/flow/process/listProcess',
    method: 'get',
    params: data
  })
}
/**
 *流程下拉列表（按流程分组）
 */
export function getFlowlistComboByFlowTypeApi(data: string) {
  return request({
    url: '/flow/process-instance/getFlowlistComboByFlowType',
    method: 'get',
    params: { flowType: data }
  })
}
/**
 * 根据发起的流程的变量及变量值查询历史流程
 */
export const queryHistoryByBussinessIdApi = (data: string | undefined) => {
  return request({
    url: '/flow/process-instance/queryHistoryByBussinessId',
    method: 'get',
    params: { variableValue: data }
  })
}
/**
 * 获取流程设置列表
 */
export const getFlowTypeListApi = () => {
  return request({
    url: '/flowset/getFlowTypeList',
    method: 'get'
  })
}
