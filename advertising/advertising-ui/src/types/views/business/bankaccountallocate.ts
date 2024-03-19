/*
 * @Author: suny
 * @Date: 2023-12-06 14:47:07

 * @LastEditTime: 2024-01-03 13:55:30
 * @LastEditors: suny
 * @Description: 到账核对相关对象
 */
import { IBaseQueryInfo, IPageRequest } from '@/types/common'
import { Tworderitem } from '../ad/adtworder'

/** 发票查询对象 */
export type TInvoiceApplicationVO = IPageRequest & IBaseQueryInfo
/**
 * <p>
 * 银行流水单
 * </p>
 *
 * Twbankaccounts
 */
export interface ITwbankaccounts {
  /**
   * 选择状态
   */
  checked: boolean
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
   * 导入金额
   */
  namount?: number
  /**
   * 已分配金额
   */
  namountallocate?: number
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
 * PreInvoiceApplicationDTO
 * 创建人：yanz
 * 类描述：预开发票申请表DTO
 * 创建日期：2023/11/11 10:24
 *
 * PreInvoiceApplicationDTO
 */
export interface IPreInvoiceApplicationDTO {
  /**
   * 申请表id
   */
  applicationid?: string
  /**
   * 经营主体id
   */
  businessentityid?: string
  /**
   * 经营主体名称
   */
  businessentityname?: string
  /**
   * 业务对象缓存key
   */
  cacheDTOKey?: string
  /**
   * 合同id
   */
  orderid?: string
  /**
   * 客户id
   */
  customerid?: string
  /**
   * 客户名称
   */
  customername?: string
  /**
   * 申请时间
   */
  dapplytime?: string
  /**
   * 部门id
   */
  deptid?: string
  /**
   * 部门名称
   */
  deptname?: string
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
   * 申请表id
   */
  id?: string
  /**
   * 发票表id
   */
  invoiceid?: string
  /**
   * 发票号
   */
  invoice?: string
  /**
   * 发票编号
   */
  invoicecode?: string
  /**
   * 开票类型 1-预开 2-挂开
   */
  itype?: number
  /**
   * 申请金额
   */
  namountapply: number
  /**
   * 已还金额
   */
  namountreceived?: number
  /**
   * 订单列表
   */
  orders?: IOrderDebtDTO[]
  /**
   * 开票项目id
   */
  printitemid?: string
  /**
   * 开票项目
   */
  printitemname?: string
  /**
   * 客户地址电话
   */
  spayeraddr?: string
  /**
   * 客户银行账户
   */
  spayerbank?: string
  /**
   * 客户识别号
   */
  spayercreditcode?: string
  /**
   * 开票名称
   */
  sprintname?: string
  /**
   * 备注
   */
  sremark?: string
  /**
   * 预开发票申请并发标志
   */
  version?: number
  /**
   * 关联的订单刊期 - 预开发票核销用
   */
  orderItems?: Tworderitem[]
}
/**
 * OrderDebtDTO
 * 创建人：suny
 * 类描述：欠款统计对象
 * 创建日期：2023/11/30 9:56
 *
 * OrderDebtDTO
 */
export interface IOrderDebtDTO {
  /**
   * 广告项目id
   */
  adprojectid?: string
  /**
   * 广告项目名称
   */
  adprojectname?: string
  /**
   * 规格id
   */
  adspecid?: string
  /**
   * 规格名称
   */
  adspecname?: string
  /**
   * 代理公司业务员id
   */
  agencyemployid?: string
  /**
   * 代理公司业务员名称
   */
  agencyemployname?: string
  /**
   * 代理公司名称
   */
  agencyname?: string
  /**
   * 代理公司id
   */
  agencytid?: string
  /**
   * 内容生产方业务员id
   */
  agentemployid?: string
  /**
   * 内空生产方业务员名称
   */
  agentemployname?: string
  /**
   * 内容生产方id
   */
  agentid?: string
  /**
   * 内容生产方
   */
  agentname?: string
  /**
   * 欠款金额
   */
  amountarrearage?: number
  /**
   * 应收金额
   */
  amountreceivable?: number
  /**
   * 已收金额
   */
  amountreceived?: number
  /**
   * 法务介入 默认0 否 1是
   */
  blegal?: boolean
  /**
   * 是否填报欠款原因(是否推送) 默认0 否 1是
   */
  breportreason?: boolean
  /**
   * 业务对象缓存key
   */
  cacheDTOKey?: string
  /**
   * 创建日期
   */
  createtime?: string
  /**
   * 客户名称
   */
  customername?: string
  /**
   * 主业务员名称
   */
  employname?: string
  /**
   * orderitem主键
   */
  id?: string
  /**
   * 发票号
   */
  invoice?: string
  /**
   * 媒体id
   */
  mediaid?: string
  /**
   * 媒体名称
   */
  medianame?: string
  /**
   * 申请金额
   */
  napplyamount?: number
  /**
   * 订单id
   */
  orderid?: string
  /**
   * 预见报结束日期
   */
  preenddate?: string
  /**
   * 预见报开始日期
   */
  prestartdate?: string
  /**
   * 广告标题
   */
  sadtitle?: string
  /**
   * 归类原因
   */
  scategory?: string
  /**
   * 合同号
   */
  scontractnum?: string
  /**
   * 欠款原因
   */
  sdebtreason?: string
  /**
   * 催告情况
   */
  snoticecontent?: string
  /**
   * 订单编号
   */
  sordernum?: string
  /**
   * 计划回款时间
   */
  srepaymentdate?: string
  /**
   * 风险等级
   */
  srisklevel?: string
}

/** 银行流水分配查询对象 */
export type TBankCustomerChargeQuery = ICustomerChargeBankVO & IPageRequest & IBaseQueryInfo
export interface ICustomerChargeBankVO {
  /**
   * 主键
   */
  id?: string
  /**
   * 预开发票申请表id
   */
  preinvoiceapplicationid?: string
  /**
   * 代理公司业务员id
   */
  agencyemployid?: string
  /**
   * 流水表id
   */
  bankaccountid?: string
  /**
  /**
   * 主业务员id
   */
  employid?: string
  /**
   * 发票号
   */
  invoice?: string
  /**
   * 发票表id
   */
  invoiceid?: string
  /**
   * 指定订单id
   */
  orderid?: string
  /**
   * 合同号
   */
  scontractnum?: string
  /**
   * 客户id
   */
  customerid?: string
  /**
   * 客户名称
   */
  customername?: string
}
/**
 * CustomerChargeBankDTO
 * 创建人：suny
 * 类描述：银行流水分配使用的客户收款DTO
 * 创建日期：2023/12/20 13:07
 *
 * CustomerChargeBankDTO
 */
export interface ICustomerChargeBankDTO {
  /**
   * 银行流水id
   */
  bankaccountid?: string
  /**
   * 是否指定订单
   */
  bassignorder?: boolean
  /**
   * 业务对象缓存key
   */
  cacheDTOKey?: string
  /**
   * 创建者id
   */
  createempid?: string
  /**
   * 创建者名称
   */
  createempname?: string
  /**
   * 客户id
   */
  customerid?: string
  /**
   * 客户名称
   */
  customername?: string
  /**
   * 创建时间
   */
  dcreatetime?: string
  /**
   * 最后修改日期
   */
  dlastmodifydate?: string
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
   * 主键
   */
  id?: string
  /**
   * 发票号
   */
  invoice?: string
  /**
   * 发票id
   */
  invoiceid?: string
  /**
   * 状态(0-待提交 1-已提交 2-已确认 3-已退回 4-已核销 ）
   */
  istatus?: number
  /**
   * 0-预收款 1-直接收款 2-银行流水
   */
  itype?: number
  /**
   * 最后操作员
   */
  lastoperator?: string
  /**
   * 最后操作员id
   */
  lastoperatorid?: string
  /**
   * 已用金额
   */
  namounspent?: number
  /**
   * 发票申请金额
   */
  namountapply?: number
  /**
   * 剩余金额
   */
  namountbalance?: number
  /**
   * 入账金额
   */
  namountreceived?: number
  /**
   * 原有分配金额（分配修改使用）
   */
  oldnamountreceived?: number
  /**
   * 指定订单id
   */
  orderid?: string
  /**
   * 欠款订单列表
   */
  orders?: IOrderDebtDTO[]
  /**
   * 付款方式ID
   */
  paymethodid?: string
  /**
   * 付款方式
   */
  paymethodname?: string
  /**
   * 预开发票申请表id
   */
  preinvoiceapplicationid?: string
  /**
   * 借方名称
   */
  sborrowername?: string
  /**
   * 指定订单合同号
   */
  scontractnum?: string
  /**
   * 描述
   */
  sdescription?: string
  /**
   * 贷方名称
   */
  slendername?: string
  /**
   * 备注
   */
  sremark?: string
  /**
   * 直接收款、预收款、银行流水
   */
  stype?: string
  /**
   * 并发标志
   */
  version?: number
  /**
   * 银行流水并发标志
   */
  versionBankAccount?: number

  /**
   * 经营主体名称（关联发票表找到）
   */
  businessentityname?: string
  /**
   * 开票时间（关联发票创建时间）
   */
  dinvoicecreatetime?: string
  /**
   * 开票项目（关联发票表找到）
   */
  printitemname?: string
  /**
   * 编号(自增列，关联客户表获取)
   */
  icode?: number
  /**
   * 类型(0-直接客户、1-代理公司、2-内容生产方，关联客户表获取)
   */
  customeritype?: number
}

/**
 * 客户收费表
 * 创建人：yanz
 * 类描述：客户收费表
 * 创建日期：22024-1-30 15:31
 * Twcustomercharge
 */
export interface Twcustomercharge {
  /**
   * 银行流水id
   */
  bankaccountid?: string
  /**
   * 是否指定订单
   */
  bassignorder?: boolean | null
  /**
   * 创建者id
   */
  createempid?: string
  /**
   * 创建者名称
   */
  createempname?: null | string
  /**
   * 客户id
   */
  customerid?: string
  /**
   * 客户名称
   */
  customername?: null | string
  /**
   * 创建时间
   */
  dcreatetime?: null | string
  /**
   * 最后修改日期
   */
  dlastmodifydate?: null | string
  /**
   * 付款时间
   */
  dpaydate?: null | string
  /**
   * 业务员id
   */
  employid?: string
  /**
   * 业务员名称
   */
  employname?: null | string
  /**
   * 主键
   */
  id?: string
  /**
   * 发票号
   */
  invoice?: null | string
  /**
   * 发票id
   */
  invoiceid?: string
  /**
   * 状态(0-待提交 1-已提交 2-已确认 3-已退回 4-已核销 ）
   */
  istatus?: number | null
  /**
   * 0-预收款 1-直接收款 2-银行流水
   */
  itype?: number | null
  /**
   * 最后操作员
   */
  lastoperator?: null | string
  /**
   * 最后操作员id
   */
  lastoperatorid?: string
  /**
   * 已用金额
   */
  namounspent?: number | null
  /**
   * 剩余金额
   */
  namountbalance?: number | null
  /**
   * 入账金额
   */
  namountreceived?: number | null
  /**
   * 指定订单id
   */
  orderid?: string
  /**
   * 付款方式ID
   */
  paymethodid?: string
  /**
   * 付款方式
   */
  paymethodname?: null | string
  /**
   * 预开发票申请表id
   */
  preinvoiceapplicationid?: string
  /**
   * 指定订单合同号
   */
  scontractnum?: null | string
  /**
   * 描述
   */
  sdescription?: null | string
  /**
   * 备注
   */
  sremark?: null | string
  /**
   * 直接收款、预收款、银行流水
   */
  stype?: null | string
  /**
   * 并发标志
   */
  version?: number | null
}
