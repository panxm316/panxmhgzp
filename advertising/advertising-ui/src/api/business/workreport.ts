/*
 * @Author: suny
 * @Date: 2023-10-30 12:40:22
 * @LastEditTime: 2024-03-07 17:48:13
 * @LastEditors: wanghl
 * @Description: 工作报告相关API
 */
import {
  IWorkReportsDTO,
  TWorkReportQuery,
  TpICustomerWorkReportQuery
} from '@/types/views/business/workreports'
import request from '@/utils/request'
import qs from 'qs'

/**
 * 获取工作报告分页列表
 * @param param
 * @returns
 */
export const getWorkReportPageListApi = (param: TWorkReportQuery) => {
  return request({
    url: '/business/twworkreports/getWorkReportPageList',
    method: 'get',
    params: param
  })
}
/**
 * 保存工作报告接口
 * @param data
 * @returns
 */
export const saveWorkReportApi = (data: IWorkReportsDTO) => {
  return request({
    url: '/business/twworkreports/saveWorkReport',
    method: 'post',
    data: data
  })
}

/**
 * 更新工作报告接口
 * @param data
 * @returns
 */
export const updateWorkReportApi = (data: IWorkReportsDTO) => {
  return request({
    url: '/business/twworkreports/updateWorkReport',
    method: 'post',
    data: data
  })
}
/**
 *  提交保存审核信息
 * @param data
 * @returns
 */
export const saveCheckInfoApi = (data: IWorkReportsDTO) => {
  return request({
    url: '/business/twworkreports/saveCheckInfo',
    method: 'post',
    data: data
  })
}
/**
 * 根据id删除工作报告接口
 * @param data
 * @returns
 */
export const deleteWorkReportByIdApi = (data: { ids: string }) => {
  return request({
    url: '/business/twworkreports/deleteWorkReportById',
    method: 'post',
    data: qs.stringify(data)
  })
}
/**
 * 工作报告提交审核
 * @param data
 * @returns
 */
export const updateCheckApi = (data: { ids: string }) => {
  return request({
    url: '/business/twworkreports/updateCheck',
    method: 'post',
    data: qs.stringify(data)
  })
}
/**
 * 获取工作报告分页列表
 * @param param
 * @returns
 */
export const getCustomerWorkReportPageListApi = (param: TpICustomerWorkReportQuery) => {
  return request({
    url: '/business/twworkreports/getCustomerWorkReportPageList',
    method: 'get',
    params: param
  })
}
