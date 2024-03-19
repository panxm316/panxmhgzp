/*
 * @Author: yanz
 * @Date: 2023-11-30 10:41:09
 * @LastEditors: yanz
 * @LastEditTime: 2023-12-01 13:19:41
 * @Description:订单表相关API
 *
 */
import { TContractforEmpCustomerQuery } from '@/types/views/ad/adorder'
import request from '@/utils/request'
import qs from 'qs'

/**
 * 获取订单表指定业务员和客户的所有合同号
 * @returns
 */
export const getContractNumByEmployIdAndCustomerIdApi = (data: TContractforEmpCustomerQuery) => {
  console.log('api data', data)
  return request({
    url: '/ad/tworder/getContractNumByEmployIdAndCustomerId',
    method: 'get',
    params: data
  })
}
