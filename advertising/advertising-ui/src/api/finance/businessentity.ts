/*
 * @Author: caogd
 * @Date: 2023-08-23 12:45:08
 * @LastEditTime: 2023-10-31 10:38:18
 * @LastEditors: caogd
 * @Description: 经营主体api
 */

import request from '@/utils/request'
import qs from 'qs'
import { IPageRequest } from '@/types/common/index'

/**
 * 经营主体下拉
 * @returns IBusinessentity 类型列表
 */
export const getBusinessentityComboApi = () => {
  return request({
    url: '/finance/businessentity/getBusinessentityCombo',
    method: 'get'
  })
}
