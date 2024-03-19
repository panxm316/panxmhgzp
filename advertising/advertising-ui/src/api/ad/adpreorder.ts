import request from '@/utils/request'
import { PreOrderQueryDTO } from '@/types/views/ad/pre-order-query-dto'

/**
 * 获取广告预约分页
 */
export const getPreOrderPageListApi = (data: PreOrderQueryDTO) => {
  return request({
    url: '/ad/tworder/getPreOrderPageList',
    method: 'get',
    params: data
  })
}

/**
 * 根据订单id获取订单详情,包含订单明细和订单归属
 */
export const getPreOrderApi = ({ orderId }: { orderId: string }) => {
  return request({
    url: '/ad/tworder/getPreOrder',
    method: 'get',
    params: { orderId }
  })
}
