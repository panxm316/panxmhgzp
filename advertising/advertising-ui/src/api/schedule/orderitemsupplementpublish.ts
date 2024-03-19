/*
 * @Author: wanghl
 * @Date: 2023-12-25 16:44:44
 * @LastEditTime: 2024-03-13 15:41:56
 * @LastEditors: songly
 * @Description:补刊接口
 */
import request from '@/utils/request'
import { TOrderitemarrange, TOrderitemarrangedata } from '@/types/views/schedule/orderitemarrange'
import {
  TOrderitemsupplementpublishSearch,
  TStopType
} from '@/types/views/schedule/orderitemsupplementpublish'
import qs from 'qs'
/**
 * 按条件获取订单明细安排
 */
export const getOrderitemsupplementpublishListApi = (data: TOrderitemsupplementpublishSearch) => {
  return request({
    url: '/schedule/orderitemsupplementpublish/getOrderitemSupplementPublishList',
    method: 'get',
    params: data
  })
}
/**
 * 获取订单明细安排分页列表
 */
export const getOrderitemsupplementpublishPageListApi = (
  data: TOrderitemsupplementpublishSearch
) => {
  return request({
    url: '/schedule/orderitemsupplementpublish/getOrderitemSupplementPublishPageList',
    method: 'get',
    params: data
  })
}
/**
 * 根据Id获取订单明细安排
 */
export const getOrderitemsupplementpublishByIdApi = (data: string) => {
  return request({
    url: '/schedule/orderitemsupplementpublish/getOrderitemSupplementPublishById',
    method: 'get',
    params: { id: data }
  })
}
/**
 * 方法功能:保存订单明细安排
 */
export const saveOrderitemarrangeApi = (data: TOrderitemarrangedata) => {
  return request({
    url: '/schedule/orderitemsupplementpublish/saveOrderitemSupplementPublish',
    method: 'post',
    data: data
  })
}

/**
 * 根据Id删除订单明细
 */
export const deleteorderItemByIdApi = (data: string) => {
  return request({
    url: '/ad/tworderitem/deleteorderItemById',
    method: 'post',
    data: qs.stringify({ ids: data })
  })
}

/**
 * 撤回补刊确认
 */
export const orderitemsupplementRecallApi = (data: string) => {
  return request({
    url: '/schedule/orderitemsupplementpublish/orderitemsupplementRecall',
    method: 'post',
    data: qs.stringify({ id: data })
  })
}
/**
 * 加刊
 */
export const addOrderitemByItemIdApi = (data: string) => {
  return request({
    url: '/schedule/orderitemsupplementpublish/addOrderitemByItemId',
    method: 'post',
    data: qs.stringify({ id: data })
  })
}
/**
 * 改刊
 */
export const changeOrderitemByItemIdApi = (data: string) => {
  return request({
    url: '/schedule/orderitemsupplementpublish/changeOrderitemByItemId',
    method: 'post',
    data: qs.stringify({ id: data })
  })
}
/**
 * 停刊
 */
export const stopOrderitemByItemIdApi = (data: TStopType) => {
  return request({
    url: '/schedule/orderitemsupplementpublish/stopOrderitemByItemId',
    method: 'post',
    data: qs.stringify({ ids: data.ids, flowId: data.flowId })
  })
}
