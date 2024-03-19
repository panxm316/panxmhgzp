/*
 * @Author: caogd
 * @Date: 2023-11-01 13:25:47
 * @LastEditTime: 2023-11-01 13:26:32
 * @LastEditors: caogd
 * @Description: 开票项目api
 */

import request from '@/utils/request'
/**
 * 开票项目下拉
 * @returns
 */
export const getAdPrintItemComboApi = () => {
  return request({
    url: '/finance/adprintitem/getAdPrintItemCombo',
    method: 'get'
  })
}
