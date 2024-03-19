/*
 * @Author: suny
 * @Date: 2023-12-06 14:47:22
 * @LastEditTime: 2024-03-15 17:04:36
 * @LastEditors: suny
 * @Description: 到账核对相关接口
 */

import {
  ICustomerChargeBankDTO,
  TBankCustomerChargeQuery,
  TInvoiceApplicationVO
} from '@/types/views/business/bankaccountallocate'
import request from '@/utils/request'
import qs from 'qs'

/**
 * 获取未分配完金额的银行流水分页列表
 * @param param
 * @returns
 */
export const getBankwCustomerChargePageListApi = (param: TBankCustomerChargeQuery) => {
  return request({
    url: '/finance/customercharge/getBankwCustomerChargePageList',
    method: 'get',
    params: param
  })
}
/**
 * 获取未分配完金额的银行流水分页列表
 * @param param
 * @returns
 */
export const getBalanceBankAccountPageListApi = (param: TBankCustomerChargeQuery) => {
  return request({
    url: '/finance/customercharge/getBalanceBankAccountPageList',
    method: 'get',
    params: param
  })
}
/**
 * 获取有欠款的发票，以及相关订单分页列表
 * @param param
 * @returns
 */
export const getDebtInvoiceApplicationPageListApi = (param: TInvoiceApplicationVO) => {
  return request({
    url: '/finance/customercharge/getDebtInvoiceApplicationPageList',
    method: 'get',
    params: param
  })
}
/**
 * 保存银行流水分配表接口
 * @param data
 * @returns
 */
export const saveBankCustomerChargeApi = (data: ICustomerChargeBankDTO) => {
  return request({
    url: '/finance/customercharge/saveBankCustomerCharge',
    method: 'post',
    data: data
  })
}

/**
 * 更新银行流水分配表接口
 * @param data
 * @returns
 */
export const updateBankCustomerChargeApi = (data: ICustomerChargeBankDTO) => {
  return request({
    url: '/finance/customercharge/updateBankCustomerCharge',
    method: 'post',
    data: data
  })
}

/**
 * 根据id删除银行流水分配表接口
 * @param data
 * @returns
 */
export const deleteBankCustomerChargeApi = (data: { ids: string }) => {
  return request({
    url: '/finance/customercharge/deleteBankCustomerCharge',
    method: 'post',
    data: qs.stringify(data)
  })
}
/**
 * 根据id提交银行流水分配表接口
 * (该方法暂时不用，因为银行流水分配到客户收费表数据状态改变时需要对数据进行校验，所以改为调用updateBankCustomerCharge方法)
 * @param data
 * @returns
 */
export const updateCustomerChargeStatusApi = (data: { id: string; istatus: number }) => {
  return request({
    url: '/finance/customercharge/updateCustomerChargeStatus',
    method: 'post',
    data: qs.stringify(data)
  })
}

/**
 * 获取银行流水的分配发票列表
 * @param param
 * @returns
 */
export const getCustomerChargeByBankidApi = (param: { bankid: string }) => {
  return request({
    url: '/finance/customercharge/getCustomerChargeByBankid',
    method: 'get',
    params: param
  })
}

/**
 * 财务人员核销银行流水提交接口
 * @param data
 * @returns
 */
export const bankAcountWriteOffApi = (
  data: ICustomerChargeBankDTO & { itemids: string; dateString: string }
) => {
  return request({
    url: '/finance/customercharge/bankAcountWriteOff',
    method: 'post',
    data: data
  })
}
