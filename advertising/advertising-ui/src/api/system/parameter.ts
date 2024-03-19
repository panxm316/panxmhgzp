/*
 * @Author: suny
 * @Date: 2024-03-08 09:05:26
 * @LastEditTime: 2024-03-08 09:12:39
 * @LastEditors: suny
 * @Description: 系统参数相关API
 */

import request from '@/utils/request'

/**
 * 根据key获取系统参数值
 */
export const getParameterByKeyApi = (data: { key: string }) => {
  return request({
    url: '/system/parameter/getParameterByKey',
    method: 'get',
    params: data
  })
}
