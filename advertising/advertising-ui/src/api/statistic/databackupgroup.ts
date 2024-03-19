/*
 * @Author: yanz
 * @Date: 2024-01-17 09:06:08
 * @LastEditors: yanz
 * @LastEditTime: 2024-01-22 16:17:59
 * @Description: 数据轧账相关API
 */
import {
  TDatabackupGroupVO,
  TDataBackupDetailVO,
  IDatabackupGroupDTO
} from '@/types/views/statistic/databackupgroup'
import { IBaseQueryInfo } from '@/types/common'
import request from '@/utils/request'

/**
 * 获取数据轧账总表分页列表
 * @param data
 * @returns
 */
export const getDataBackupGroupPageListApi = (data: TDatabackupGroupVO) => {
  return request({
    url: '/statistic/databackupgroup/getDataBackupGroupPageList',
    method: 'get',
    params: data
  })
}

/**
 * 获取数据轧账明细分页列表
 * @param data
 * @returns
 */
export const getDataBackupDetailPageListApi = (data: TDataBackupDetailVO) => {
  return request({
    url: '/statistic/databackupgroup/getDataBackupDetailPageList',
    method: 'get',
    params: data
  })
}
/**
 * 根据时间范围查询定向刊期总数
 * @param data
 * @returns
 */
export const getOrderItemCountApi = (data: IBaseQueryInfo) => {
  return request({
    url: '/statistic/databackupgroup/getOrderItemCount',
    method: 'get',
    params: data
  })
}

/**
 * 保存数据轧账明细
 * @param data
 * @returns
 */
export const saveDataBackupDetailApi = async (data?: IDatabackupGroupDTO) => {
  return request({
    url: '/statistic/databackupgroup/saveDataBackupDetail',
    method: 'post',
    data: data
  })
}
