/*
 * @Author: suny
 * @Date: 2023-10-26 11:22:36
 * @LastEditTime: 2024-03-12 14:51:57
 * @LastEditors: suny
 * @Description: 银行流水对象
 */

import { IBaseQueryInfo, IPageRequest } from '@/types/common'

/**
 * <p>
 * 银行流水单
 * </p>
 *
 * Twbankaccounts
 */
export interface ITwbankaccounts {
  /**
   * 导入历史ID
   */
  bankaccounthistoryid?: string
  /**
   * 导入时间
   */
  dcreatetime?: string
  /**
   * 交易日期
   */
  dtradedate?: string
  /**
   * 主键
   */
  id?: string
  /**
   * 最后操作员
   */
  lastoperator?: string
  /**
   * 最后操作员id
   */
  lastoperatorid?: string
  /**
   * 已分配金额
   */
  namountallocate?: number
  /**
   * 导入金额
   */
  namount?: number
  /**
   * 借方账号
   */
  sborroweraccount?: string
  /**
   * 借方名称
   */
  sborrowername?: string
  /**
   * 凭据种类
   */
  scredentialtype?: string
  /**
   * 明细类型
   */
  sdetailtype?: string
  /**
   * 贷方账号
   */
  slenderaccount?: string
  /**
   * 贷方名称
   */
  slendername?: string
  /**
   * 备注
   */
  sremark?: string
  /**
   * 交易流水号
   */
  stradecode?: string
  /**
   * 交易类别
   */
  stradetype?: string
  /**
   * 并发标志
   */
  version?: number
}
/**
 * 流水查询条件
 */
export type TBankAccountQuery = ITwbankaccounts & IPageRequest & IBaseQueryInfo

/**
 * <p>
 * 银行流水导入历史表
 * </p>
 *
 * Twbankaccounthistory
 */
export interface ITwbankaccounthistory {
  /**
   * 创建日期
   */
  dcreatetime?: string
  /**
   * 操作员id
   */
  employid?: string
  /**
   * 操作员名称
   */
  employname?: string
  /**
   * 主键
   */
  id?: string
  /**
   * 文件描述
   */
  sdescription?: string
  /**
   * 文件类型(银行流水bankaccount)
   */
  sfilecategory?: string
  /**
   * 文件格式
   */
  sfileformat?: string
  /**
   * 统一文件ID
   */
  sfileid?: string
  /**
   * 文件大小
   */
  sfilesize?: string
  /**
   * 源文件名
   */
  soriginalfile?: string
  /**
   * 预览地址
   */
  durl?: string
}
/**
 * 流水导入历史查询条件
 */
export type TBankAccountHistoryQuery = ITwbankaccounthistory & IPageRequest & IBaseQueryInfo
