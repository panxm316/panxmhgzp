/*
 * @Author: suny
 * @Date: 2023-10-12 14:01:05
 * @LastEditTime: 2023-12-22 14:20:27
 * @LastEditors: songly
 * @Description:
 */
import request from '@/utils/request'

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
