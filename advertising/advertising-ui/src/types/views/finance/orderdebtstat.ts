/*
 * @Author: suny
 * @Date: 2023-11-22 14:07:36
 * @LastEditTime: 2024-03-07 19:46:46
 * @LastEditors: suny suny@hgzp.com.cn
 * @Description: 欠款统计查询相关对象
 */
import { IBaseQueryInfo, IPageRequest } from '@/types/common'

/** 订单欠款统计查询对象 */
export type TOrderDebtStatQuery = { deptid: string; breportreason: boolean } & IPageRequest &
  IBaseQueryInfo
/** 欠款统计对象 */
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
   * 申请金额/开票金额
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
  /**
   * 是否确认 默认0 否 1是
   */
  bconfirm?: boolean
  /**
   * 并发标志
   */
  version?: number
  /**
   * 部门名称
   */
  deptname?: string
  /**
   * 欠款原因表id
   */
  debtreasonid?: string
  /**
   * 刊期表id
   */
  orderitemid?: string
}
