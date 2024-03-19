/*
 * @Author: caogd
 * @Date: 2023-11-01 10:53:57
 * @LastEditTime: 2023-11-01 10:55:19
 * @LastEditors: caogd
 * @Description: 付款方式api接口
 */

import request from '@/utils/request'
/**
 * 付款方式下拉
 * @returns
 */
export const getPaymethodComboApi = () => {
  return request({
    url: '/finance/paymethod/getPaymethodCombo',
    method: 'get'
  })
}
