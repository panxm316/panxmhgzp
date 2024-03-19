/*
 * @Author: caogd
 * @Date: 2023-08-23 12:45:08
 * @LastEditTime: 2023-08-24 09:49:26
 * @LastEditors: caogd
 * @Description: 经营主体api
 */

import request from '@/utils/request'
import qs from 'qs'
import { IPageRequest } from '@/types/common/index'
import { IBusinessentity } from '@/types/views/finance/businessentity'
/**
 * @description: 经营主体分页查询
 * @param {IPageRequest} data
 * @return {*}
 */
export const getBusinessentityPageListApi = (data: IPageRequest & { queryKey?: string }) => {
  return request({
    url: '/finance/businessentity/getBusinessentityPageList',
    method: 'get',
    params: data
  })
}
/**
 * 新增经营主体
 * @param data
 * @returns
 */
export const saveBusinessentityApi = (data: IBusinessentity) => {
  return request({
    url: '/finance/businessentity/saveBusinessentity',
    method: 'post',
    data: data
  })
}
/**
 * 修改经营主体
 * @param data
 * @returns
 */
export const updateBusinessentityApi = (data: IBusinessentity) => {
  return request({
    url: '/finance/businessentity/updateBusinessentity',
    method: 'post',
    data: data
  })
}
/**
 * @description: 经营主体删除
 * @param {string} data
 * @return {*}
 */
export const deleteBusinessentityApi = (data: string) => {
  return request({
    url: '/finance/businessentity/deleteBusinessentity',
    method: 'post',
    data: qs.stringify({ ids: data })
  })
}

/**
 * 获取经营主体最大序号
 * @returns
 */
export const getMaxSortApi = () => {
  return request({
    url: '/finance/businessentity/getMaxSort',
    method: 'get'
  })
}
