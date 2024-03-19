/*
 * @Author: songly
 * @Date: 2023-10-12 13:40:39
 * @LastEditTime: 2024-03-16 09:10:29
 * @LastEditors: wanghl
 * @Description:订单增删改审核流程api
 */
import request from '@/utils/request'
import {
  TAddFlowOrder,
  TDEditFlowOrder,
  TDeleteFlowOrder,
  TQFlowOrderdata,
  TQFlowOrderlist,
  TQFlowOrderlistpage
} from '@/types/views/flow/floworder'
import qs from 'qs'
/**
 * 新增审批流程关联信息
 * @param data
 * @returns
 */
export const saveapplicationRelationsApi = (data: TAddFlowOrder) => {
  return request({
    url: '/flow/applicationrelations/saveapplicationRelations',
    method: 'post',
    data: data
  })
}
/**
 * 删除审批流程关联信息
 * @param data
 * @returns
 */
export const deleteapplicationRelationsApi = (data: TDeleteFlowOrder) => {
  return request({
    url: '/flow/applicationrelations/deleteapplicationRelations',
    method: 'post',
    data: data
  })
}
/**
 * 修改审批流程关联信息
 * @param data
 * @returns
 */
export const updateapplicationRelationsApi = (data: TDEditFlowOrder) => {
  return request({
    url: '/flow/applicationrelations/updateapplicationRelations',
    method: 'post',
    data: data
  })
}
/**
 * 根据某些值查询审批流程关联信息
 */
export const getapplicationRelationsApi = (data: TQFlowOrderdata) => {
  return request({
    url: '/flow/applicationrelations/getapplicationRelations',
    method: 'get',
    params: data
  })
}
/**
 * 根据的某些值查询审批流程关联信息列表
 */
export const getapplicationRelationslistApi = (data: TQFlowOrderlist) => {
  return request({
    url: '/flow/applicationrelations/getapplicationRelationslist',
    method: 'get',
    params: data
  })
}
/**
 * 根据某些值查询审批流程关联信息分页数据
 */
export const getapplicationRelationsPageListApi = (data: TQFlowOrderlistpage) => {
  return request({
    url: '/flow/applicationrelations/getapplicationRelationsPageList',
    method: 'post',
    data: qs.stringify({
      page: data.page,
      order: data.order,
      pageSize: data.pageSize,
      sort: data.sort
    })
  })
}
