/*
 * @Author: suny
 * @Date: 2023-11-22 14:07:36
 * @LastEditTime: 2024-03-07 19:26:09
 * @LastEditors: suny suny@hgzp.com.cn
 * @Description: 欠款原因管理相关对象
 */

import { IBaseQueryInfo, IPageRequest } from '@/types/common'

/** 欠款原因管理查询对象 */
export type TDebtReasonVO = {
  /**
   * 代理公司业务员id
   */
  agencyemployid?: string
  /**
   * 部门id
   */
  deptid?: string
  /**
   * 主业务员id
   */
  employid?: string
  /**
   * 发票号
   */
  invoice?: string
  /**
   * 合同号
   */
  scontractnum?: string
  /**
   * 是否填报欠款原因(是否推送) 默认0 否 1是
   */
  breportreason?: boolean
  /**
   * 是否财务人员查询，财务人员只能查询已提交并通过的数据
   */
  bfinance?: boolean
} & IPageRequest &
  IBaseQueryInfo

/** 欠款原因表 */
export interface ITwdebtreason {
  /**
   * 代理公司业务员id
   */
  agencyemployid?: string
  /**
   * 代理公司业务员名称
   */
  agencyemployname?: string
  /**
   * 代理公司
   */
  agencyname?: string
  /**
   * 内容生产方业务员id
   */
  agentemployid?: string
  /**
   * 内空生产方业务员名称
   */
  agentemployname?: string
  /**
   * 内容生产方名称
   */
  agentname?: string
  /**
   * 欠款金额
   */
  amountarrearage?: string
  /**
   * 应收金额
   */
  amountreceivable?: string
  /**
   * 已收金额
   */
  amountreceived?: string
  /**
   * 是否确认 默认0 否 1是
   */
  bconfirm?: boolean
  /**
   * 法务介入 默认0 否 1是
   */
  blegal?: boolean
  /**
   * 创建日期
   */
  createdate?: string
  /**
   * 创建者id
   */
  createempid?: string
  /**
   * 创建者名称
   */
  createempname?: string
  /**
   * 客户名称
   */
  customername?: string
  /**
   * 部门id
   */
  deptid?: string
  /**
   * 部门名称
   */
  deptname?: string
  /**
   * 刊发日期
   */
  dpublishdate?: string
  /**
   * 填报日期
   */
  dreportdate?: string
  /**
   * 主业务员id
   */
  employid?: string
  /**
   * 主业务员名称
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
   * 发票金额
   */
  namountinvoice?: string
  /**
   * 广告表id
   */
  orderid?: string
  /**
   * 刊期表id
   */
  orderitemid?: string
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
   * 计划回款时间
   */
  srepaymentdate?: string
  /**
   * 风险等级
   */
  srisklevel?: string
  /**
   * 并发标志
   */
  version?: number
}
