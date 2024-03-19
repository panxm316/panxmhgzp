/*
 * @Author: suny
 * @Date: 2023-09-19 13:31:12
 * @LastEditTime: 2023-10-28 10:22:57
 * @LastEditors: suny
 * @Description:银行流水API
 */
import { TBankAccountHistoryQuery, TBankAccountQuery } from '@/types/views/finance'
import request from '@/utils/request'

/**
 * 获取银行流水分页列表
 * @param param
 * @returns
 */
export const getBankAccountPageListApi = (param: TBankAccountQuery) => {
  return request({
    url: '/finance/twbankaccounts/getBankAccountPageList',
    method: 'get',
    params: param
  })
}
/**
 * 获取银行流水导入历史分页列表
 * @param param
 * @returns
 */
export const getBankAccountHistoryPageListApi = (param: TBankAccountHistoryQuery) => {
  return request({
    url: '/finance/twbankaccounthistory/getBankAccountHistoryPageList',
    method: 'get',
    params: param
  })
}
