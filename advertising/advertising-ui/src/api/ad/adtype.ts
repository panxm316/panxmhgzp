/*
 * @Author: wanghl
 * @Date: 2023-12-06 10:19:19
 * @LastEditTime: 2023-12-06 10:20:32
 * @LastEditors: wanghl
 * @Description:广告类别列表
 */

import request from '@/utils/request'

/**
 * 获取广告类型列表
 * @returns
 */
export const getadtypelistApi = () => {
  return request({
    url: '/ad/adtype/getadtypelist',
    method: 'get'
  })
}
