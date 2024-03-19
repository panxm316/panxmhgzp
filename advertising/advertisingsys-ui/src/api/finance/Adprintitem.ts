/*
 * @Author: wanghl
 * @Date: 2023-08-29 16:05:22
 * @LastEditTime: 2023-09-12 10:55:02
 * @LastEditors: songly
 * @Description:开票项目api
 */
import request from '@/utils/request'
import qs from 'qs'
import { IAdprintitem, TAdprintitemQ } from '@/types/views/finance/Adprintitem'
/**
 * 据id获取开票项目
 * @param data
 * @returns
 */
export const getAdPrintItemByIdApi = (data: string) => {
  return request({
    url: '/finance/adprintitem/getAdPrintItemById',
    method: 'get',
    params: qs.stringify({ id: data })
  })
}
/**
 * 据名称(sname)、启用状态(buse)获取开票项目
 * @param data
 * @returns
 */
export const getAdPrintItemPageListApi = (data: TAdprintitemQ) => {
  return request({
    url: '/finance/adprintitem/getAdPrintItemPageList',
    method: 'get',
    params: data
  })
}
/**
 * 添加新开票项目
 * @param data
 * @returns
 */
export const saveAdPrintItemApi = (data: IAdprintitem) => {
  return request({
    url: '/finance/adprintitem/saveAdPrintItem',
    method: 'post',
    data: data
  })
}
/**
 * 修改开票项目
 * @param data
 * @returns
 */
export const updateAdPrintItemApi = (data: IAdprintitem) => {
  return request({
    url: '/finance/adprintitem/updateAdPrintItem',
    method: 'post',
    data: data
  })
}
/**
 * 开票项目删除
 * @param data
 * @returns
 */
export const deleteAdPrintItemByIdApi = (data: string) => {
  return request({
    url: '/finance/adprintitem/deleteAdPrintItemById',
    method: 'post',
    data: qs.stringify({ ids: data })
  })
}
/**
 * 获取开票项目最大序号
 * @returns
 */
export const getAdPrintItemMaxSortApi = () => {
  return request({
    url: '/finance/adprintitem/getAdPrintItemMaxSort',
    method: 'get'
  })
}
