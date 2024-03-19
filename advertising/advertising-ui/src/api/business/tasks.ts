/*
 * @Author: lhl
 * @Date: 2023-11-08
 * @LastEditTime: 2023-12-29 13:41:43
 * @LastEditors: lhl
 * @Description: 任务设置相关API
 */

import request from '@/utils/request'
import {
  ITaskReportQuery,
  ITaskQuotaAddDTO,
  ITaskQuotaUpdateDTO,
  TwtaskDTO
} from '@/types/views/business/tasks'

/**
 * 查询任务额度记录
 * @param param
 * @returns
 */
export const getTaskQuotaPageListtApi = (param: ITaskReportQuery) => {
  return request({
    url: '/business/twtasks/getTaskQuotaPageList',
    method: 'get',
    params: param
  })
}

/**
 * 批量添加任务额度记录
 * @param param
 * @returns
 */
export const saveTaskQuotaApi = (data: ITaskQuotaAddDTO) => {
  return request({
    url: '/business/twtasks/saveTaskQuota',
    method: 'post',
    data: data
  })
}

/**
 * 批量更新任务额度记录
 * @param param
 * @returns
 */
export const updateTaskQuotaApi = (data: ITaskQuotaUpdateDTO) => {
  return request({
    url: '/business/twtasks/updateTaskQuota',
    method: 'post',
    data: data
  })
}

/**
 * 批量删除任务额度记录
 * @param param
 * @returns
 */
export const deleteTaskQuotaApi = (data: ITaskQuotaUpdateDTO) => {
  return request({
    url: '/business/twtasks/deleteTaskQuota',
    method: 'post',
    data: data
  })
}

/**
 * 批量复制任务额度记录
 * @param param
 * @returns
 */
export const batchCopyTaskQuotaApi = (data: ITaskQuotaUpdateDTO) => {
  return request({
    url: '/business/twtasks/batchCopyTaskQuota',
    method: 'post',
    data: data
  })
}

/**
 * 获取额度记录
 * @param taskid
 * @returns
 */
export const getTwtaskBodyApi = (data: { taskid: string }) => {
  return request({
    url: '/business/twtasks/getTwtaskBody',
    method: 'get',
    params: data
  })
}
/**
 * 添加额度记录
 * @param param
 * @returns
 */
export const addTwtaskApi = (data: TwtaskDTO) => {
  return request({
    url: '/business/twtasks/addTwtask',
    method: 'post',
    data: data
  })
}
/**
 * 调整额度记录
 * @param param
 * @returns
 */
export const updateTwtaskApi = (data: TwtaskDTO) => {
  return request({
    url: '/business/twtasks/updateTwtask',
    method: 'post',
    data: data
  })
}
