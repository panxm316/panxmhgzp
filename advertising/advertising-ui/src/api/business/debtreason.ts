/*
 * @Author: suny
 * @Date: 2023-11-22 14:16:52
 * @LastEditTime: 2023-11-23 09:56:30
 * @LastEditors: suny
 * @Description: 欠款原因管理相关API
 */
import { ITwdebtreason, TDebtReasonVO } from '@/types/views/business/debtreason'
import request from '@/utils/request'

/**
 * 获取欠款原因分页列表
 * @param param
 * @returns
 */
export const getDebtReasonPageListApi = (param: TDebtReasonVO) => {
  return request({
    url: '/business/debtreason/getDebtReasonPageList',
    method: 'get',
    params: param
  })
}
/**
 * 更新欠款原因接口
 * @param data
 * @returns
 */
export const updateDebtReasonApi = (data: ITwdebtreason) => {
  return request({
    url: '/business/debtreason/updateDebtReason',
    method: 'post',
    data: data
  })
}
