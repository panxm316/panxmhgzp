/*
 * @Author: wanghl
 * @Date: 2023-08-23 12:41:52
 * @LastEditTime: 2023-09-12 11:06:51
 * @LastEditors: songly
 * @Description:付款时间Api
 */
import request from '@/utils/request'
import qs from 'qs'
import { TPaysort } from '@/types/views/finance/Paysort'
/**
 * 查询付款时间设置，skey（刊前付款 bepub 刊后付款 afpub）
 * @returns
 */
export const getPaysortListApi = () => {
  return request({
    url: '/finance/paysort/getPaysortList',
    method: 'get'
  })
}
/**
 * 添加保存付款时间信息
 * @param data
 * @returns
 */
export const savePaysortApi = (data: TPaysort) => {
  return request({
    url: '/finance/paysort/savePaysort',
    method: 'post',
    data: data
  })
}
/**
 * 添加保存付款时间信息
 * @param data
 * @returns
 */
export const updatepaysortAPi = (data: TPaysort) => {
  return request({
    url: '/finance/paysort/updatepaysort',
    method: 'post',
    data: data
  })
}
/**
 * 添加保存付款时间信息
 * @param data
 * @returns
 */
export const deletepaysortApi = (data: string) => {
  return request({
    url: '/finance/paysort/deletepaysort',
    method: 'post',
    data: qs.stringify({ id: data })
  })
}
