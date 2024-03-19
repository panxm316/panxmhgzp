/*
 * @Author: suny
 * @Date: 2023-11-22 14:21:30
 * @LastEditTime: 2024-03-07 18:16:43
 * @LastEditors: suny suny@hgzp.com.cn
 * @Description: 欠款统计管理相关API
 */
import { TDebtReasonVO } from '@/types/views/business/debtreason'
import { TOrderDebtStatQuery } from '@/types/views/finance/orderdebtstat'
import request from '@/utils/request'
import qs from 'qs'

/**
 * 获取欠款统计分页列表
 * @param param
 * @returns
 */
export const getOrderDebtPageListApi = (param: TDebtReasonVO) => {
  return request({
    url: '/business/debtreason/getOrderDebtPageList',
    method: 'get',
    params: param
  })
}
/**
 * 根据id推送欠款原因表接口
 * @param data
 * @returns
 */
export const pushOrderDebtApi = (data: { ids: string }) => {
  return request({
    url: '/business/debtreason/pushOrderDebt',
    method: 'post',
    data: qs.stringify(data)
  })
}
