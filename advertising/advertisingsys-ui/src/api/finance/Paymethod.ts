/*
 * @Author: wanghl
 * @Date: 2023-08-29 16:05:22
 * @LastEditTime: 2023-09-12 11:06:02
 * @LastEditors: songly
 * @Description:付款方式api
 */
import request from '@/utils/request'
import qs from 'qs'
import { IPageRequest } from '@/types/common/index'
import { IPaymethod } from '@/types/views/finance/Paymethod'
/**
 * @description: 付款方式分页查询
 * @param {IPageRequest} data
 * @return {*}
 */
export const getPaymethodPageListApi = (data: IPageRequest & { queryKey?: string }) => {
  return request({
    url: '/finance/paymethod/getPaymethodPageList',
    method: 'get',
    params: data
  })
}
/**
 * 付款方式新增保存
 * @param data
 * @returns
 */
export const savePaymethodApi = (data: IPaymethod) => {
  return request({
    url: '/finance/paymethod/savePaymethod',
    method: 'post',
    data: data
  })
}
/**
 * 付款方式修改
 * @param data
 * @returns
 */
export const updatePaymethodApi = (data: IPaymethod) => {
  return request({
    url: '/finance/paymethod/updatePaymethod',
    method: 'post',
    data: data
  })
}
/**
 * @description: 付款方式按id查询信息
 * @param {string} data
 * @return {*}
 */
export const getPaymethodByIdApi = (data: string) => {
  return request({
    url: '/finance/paymethod/getPaymethodById',
    method: 'get',
    params: qs.stringify({ id: data })
  })
}
/**
 * @description: 付款方式删除
 * @param {string} data
 * @return {*}
 */
export const deletePaymethodApi = (data: string) => {
  return request({
    url: '/finance/paymethod/deletePaymethod',
    method: 'post',
    data: qs.stringify({ ids: data })
  })
}
/**
 * 获取付款方式序号最大值
 * @returns
 */
export const getMaxSortApi = () => {
  return request({
    url: '/finance/paymethod/getMaxSort',
    method: 'get'
  })
}
