/*
 * @Author: lhl
 * @Date: 2023-01-18
 * @Description: 报表配置
 */
import {
  IReportFormGroupDTO,
  IReportmodelItemDTO,
  IReportSumDTO
} from '@/types/views/statistic/reportforms'
import request from '@/utils/request'

/**
 * 保存配置记录
 * @param data
 * @returns
 */
export const saveReportFormGroupApi = (data: IReportFormGroupDTO) => {
  return request({
    url: '/statistic/reportmodelgroup/save',
    method: 'post',
    data: data
  })
}

/**
 * 更新配置记录
 * @param data
 * @returns
 */
export const updateReportFormGroupApi = (data: IReportFormGroupDTO) => {
  return request({
    url: '/statistic/reportmodelgroup/update',
    method: 'post',
    data: data
  })
}

/**
 * 删除配置记录
 * @param data
 * @returns
 */
export const deleteReportFormGroupApi = (data: IReportFormGroupDTO) => {
  return request({
    url: '/statistic/reportmodelgroup/delete',
    method: 'post',
    data: data
  })
}
/**
 * 查询报表配置记录
 * @param
 * @returns
 */
export const getReportFormListApi = (data: { reportFormId: string }) => {
  return request({
    url: '/statistic/reportmodelgroup/getReportFormList',
    method: 'get',
    params: data
  })
}

/**
 * 启用配置记录
 * @param
 * @returns
 */
export const enableReportFormApi = (data: { reportId: string; reportFormId: string }) => {
  return request({
    url: '/statistic/reportmodelgroup/enableReportForm',
    method: 'get',
    params: data
  })
}

/**
 * 添加汇总项
 * @param data
 * @returns
 */
export const saveReportmodelItemApi = (data: IReportmodelItemDTO) => {
  return request({
    url: '/statistic/reportmodelgroup/saveReportmodelItem',
    method: 'post',
    data: data
  })
}

/**
 * 更新汇总项
 * @param data
 * @returns
 */
export const updateReportmodelItemApi = (data: IReportmodelItemDTO) => {
  return request({
    url: '/statistic/reportmodelgroup/updateReportmodelItem',
    method: 'post',
    data: data
  })
}

/**
 * 查询报表汇总记录
 * @param
 * @returns
 */
export const getReportSumItemListApi = (data: { reportGroupId: string; reportType: string }) => {
  return request({
    url: '/statistic/reportmodelgroup/getReportSumItemList',
    method: 'get',
    params: data
  })
}

/**
 * 删除汇总项
 * @param data
 * @returns
 */
export const deleteReportmodelItemApi = (data: IReportmodelItemDTO) => {
  return request({
    url: '/statistic/reportmodelgroup/deleteReportmodelItem',
    method: 'post',
    data: data
  })
}
/**
 * 配置部门
 * @param data
 * @returns
 */
export const addSumObjectApi = (data: IReportSumDTO) => {
  return request({
    url: '/statistic/reportmodelgroup/addSumObject',
    method: 'post',
    data: data
  })
}
