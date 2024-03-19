/*
 * @Author: caogd
 * @Date: 2023-09-06 14:49:05
 * @LastEditTime: 2023-09-06 14:49:35
 * @LastEditors: caogd
 * @Description: 日志api
 */
import { TLogQueryInfo } from '@/types/views/system/log'
import request from '@/utils/request'
/**
 * @description: 日志分页查询
 * @param {TLogQueryInfo} data
 * @return {*}
 */
export const getLogPageListApi = (data: TLogQueryInfo) => {
  return request({
    url: '/system/log/getLogPageList',
    method: 'get',
    params: data
  })
}
