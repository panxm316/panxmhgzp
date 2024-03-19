/*
 * @Author: songly
 * @Date: 2023-12-12 09:31:53
 * @LastEditTime: 2024-01-31 13:20:21
 * @LastEditors: songly
 * @Description:订单明细安排接口
 */
import request from '@/utils/request'
import {
  TOrderitemarrange,
  TOrderitemarrangeSearch,
  TOrderitemarrangedata,
  TOrderItemArrangeInfo,
  TupdateOrderitemarrangePos,
  TupdateOrderitemPublishStatus
} from '@/types/views/schedule/orderitemarrange'
import qs from 'qs'
/**
 * 按条件获取订单明细安排
 */
export const getOrderitemarrangeListApi = (data: TOrderitemarrangeSearch) => {
  return request({
    url: '/schedule/orderitemarrange/getOrderitemarrangeList',
    method: 'get',
    params: data
  })
}
/**
 * 获取订单明细安排分页列表
 */
export const getOrderitemarrangePageListApi = (data: TOrderitemarrangeSearch) => {
  return request({
    url: '/schedule/orderitemarrange/getOrderitemarrangePageList',
    method: 'get',
    params: data
  })
}
/**
 * 根据Id获取订单明细安排
 */
export const getOrderitemarrangeByIdApi = (data: string) => {
  return request({
    url: '/schedule/orderitemarrange/getOrderitemarrangeById',
    method: 'get',
    params: { id: data }
  })
}
/**
 * 方法功能:保存订单明细安排
 */
export const saveOrderitemarrangeApi = (data: TOrderitemarrangedata) => {
  return request({
    url: '/schedule/orderitemarrange/saveOrderitemarrange',
    method: 'post',
    data: data
  })
}
/**
 * 修改订单明细安排
 */
export const updateOrderitemarrangeApi = (data: TOrderitemarrange) => {
  return request({
    url: '/schedule/orderitemarrange/updateOrderitemarrange',
    method: 'post',
    data: data
  })
}
/**
 * 删除订单明细安排
 */
export const deleteOrderitemarrangeApi = (data: string) => {
  return request({
    url: '/schedule/orderitemarrange/deleteOrderitemarrange',
    method: 'post',
    data: qs.stringify({ ids: data })
  })
}
/**
 * 获取订单明细安排分页列表
 */
export const getPageOrderItemArrangeInfoApi = (data: TOrderItemArrangeInfo) => {
  return request({
    url: '/schedule/orderitemarrange/getPageOrderItemArrangeInfo',
    method: 'get',
    params: data
  })
}
/**
 * 取消安排
 */
export const updateOrderitemarrangeStatusApi = (data: string) => {
  return request({
    url: '/schedule/orderitemarrange/updateOrderitemarrangeStatus',
    method: 'post',
    data: qs.stringify({ id: data })
  })
}
/**
 * 更新订单安排坐标
 */
export const updateOrderitemarrangePosApi = (data: TupdateOrderitemarrangePos) => {
  return request({
    url: '/schedule/orderitemarrange/updateOrderitemarrangePos',
    method: 'post',
    data: data
  })
}
/**
 * 修改订单明细发布状态
 */
export const updateOrderitemPublishStatusApi = (data: TupdateOrderitemPublishStatus) => {
  return request({
    url: '/schedule/orderitemarrange/updateOrderitemPublishStatus',
    method: 'post',
    data: qs.stringify({ id: data.id, bPublish: data.bPublish })
  })
}
