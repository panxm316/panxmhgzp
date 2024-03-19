/*
 * @Author: caogd
 * @Date: 2023-10-31 10:46:36
 * @LastEditTime: 2024-01-05 14:43:09
 * @LastEditors: suny
 * @Description:客户预收款类型
 */

import { IBaseQueryInfo, IPageRequest } from '@/types/common'

/**
 * 客户预收款查询类型
 */
export type TCustomerChargeVO = {
  /**
   * 银行流水id
   */
  bankaccountid?: string
  /**
   * 是否指定订单
   */
  bassignorder?: boolean
  /**
   * 创建者id
   */
  createempid?: string
  /**
   * 创建者名称
   */
  createempname?: string
  /**
   * 状态(0-待提交 1-已提交 2-已确认 3-已退回 4-已核销 ）
   */
  istatus?: number
  /**
   * 0-预收款 1-直接收款 2-银行流水
   */
  itype?: number
  id?: string
  /**
   * 经营主体id
   */
  businessentityid?: string
  /**
   * 经营主体名称
   */
  businessentityname?: string
  /**
   * 客户id
   */
  customerid?: string
  /**
   * 客户名称
   */
  customername?: string
  /**
   * 客户类型名称
   */
  custtypeName?: string
  /**
   * 创建时间
   */
  dcreatetime?: string
  /**
   * 付款时间
   */
  dpaydate?: string
  /**
   * 业务员id
   */
  employid?: string
  /**
   * 业务员名称
   */
  employname?: string
  /**
   * 0-直接客户、1-代理公司、2-内容生产方
   */
  icusttype?: number
  /**
   * 发票号
   */
  invoice?: string
  /**
   * 发票编码
   */
  invoicecode?: string
  /**
   * 发票id
   */
  invoiceid?: string
  /**
   * 发票并发标志
   */
  invoiceversion?: number
  /**
   * 0-手开，1-预开 2-挂开 3-冲红 4-预收费 5-预开完成
   */
  itypeinvoice?: number
  /**
   * 入账金额
   */
  namountreceived?: string
  /**
   * 付款方式ID
   */
  paymethodid?: string
  /**
   * 付款方式
   */
  paymethodname?: string
  /**
   * 备注
   */
  sremark?: string
  /**
   * 直接收款、预收款、银行流水
   */
  stype?: string
  /**
   * 开票项目id
   */
  printitemid?: string
  /**
   * 开票项目
   */
  printitemname?: string
  /**
   * 收款明细并发标志
   */
  version?: number
  /**
   * 金额范围大于等于，最低金额
   */
  namountreceivedge?: number
  /**
   * 金额范围小于等于，最高金额
   */
  namountreceivedle?: number
  /**
   * 已用金额
   */
  namounspent?: number

  /**
   * 剩余金额
   */
  namountbalance?: number
}

export type TCustomerChargeQuery = TCustomerChargeVO & IPageRequest & IBaseQueryInfo
/**
 * 客户预收款提交类型
 */
export interface ICustomerChargeDTO {
  /**
   * 创建者id
   */
  createempid?: string
  /**
   * 创建者名称
   */
  createempname?: string
  /**
   * 状态(0-待提交 1-已提交 2-已确认 3-已退回 4-已核销 ）
   */
  istatus?: number
  /**
   * 0-预收款 1-直接收款 2-银行流水
   */
  itype?: number
  /**
   * 直接收款、预收款
   */
  stype?: string
  /**
   * 经营主体id
   */
  businessentityid: string
  /**
   * 经营主体名称
   */
  businessentityname: string
  /**
   * 客户id
   */
  customerid: string
  /**
   * 客户名称
   */
  customername: string
  /**
   * 付款时间
   */
  dpaydate?: string
  /**
   * 业务员id
   */
  employid: string
  /**
   * 业务员名称
   */
  employname: string
  /**
   * 0-直接客户、1-代理公司、2-内容生产方
   */
  icusttype: number
  /**
   * 收费表id
   */
  id: string
  /**
   * 发票并发标志
   */
  invoiceversion?: number
  /**
   * 0-手开，1-预开 2-挂开 3-冲红 4-预收费 5-预开完成
   */
  itypeinvoice: number
  /**
   * 入账金额
   */
  namountreceived: number
  /**
   * 付款方式ID
   */
  paymethodid: string
  /**
   * 付款方式
   */
  paymethodname: string
  /**
   * 开票项目id
   */
  printitemid: string
  /**
   * 开票项目
   */
  printitemname: string
  /**
   * 备注
   */
  sremark?: string
  /**
   * 收款明细并发标志
   */
  version?: number
}
