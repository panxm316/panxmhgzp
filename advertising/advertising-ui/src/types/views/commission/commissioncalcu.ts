/*
 * @Author: suny
 * @Date: 2024-01-15 10:03:07

 * @LastEditTime: 2024-03-16 12:43:57
 * @LastEditors: suny
 * @Description: 订单佣金计算相关对象
 */
import { IBaseQueryInfo, IPageRequest } from '@/types/common'
import { IOrderAndItemDTO } from '../business/orderitemcost'
/** 佣金计算查询条件对象 */
export type TOrderItemCommissionVO = IOrderItemCommissionVO & IPageRequest & IBaseQueryInfo
export interface IOrderItemCommissionVO {
  /**
   * 计算状态（是否查询已计算过的佣金的数据）
   */
  bcountflag?: boolean
  /**
   * 业务对象缓存key
   */
  cacheDTOKey?: string
  /**
   * 业务员部门id
   */
  deptid?: string
  /**
   * 媒体id
   */
  mediaid?: string
  /**
   * 业务员id
   */
  employid?: string
  /**
   * 标记(0-计算1-确认 2-发放 3-撤销)
   */
  icommissionstatus?: number
  /**
   * 是否统计查询（统计查询时只查询确认/发放的数据）
   */
  bstatflag?: boolean
  /**
   * 是否当前人员（0：否 1：是，如果是则表示只查询当前人员）
   */
  bcurflag?: boolean
}
/**
 * OrderItemCommissionDTO
 * 创建人：suny
 * 类描述：佣金计提对象
 * 创建日期：2024/1/12 10:54
 *
 * OrderItemCommissionDTO
 */
export type TOrderItemCommissionDTO = IOrderAndItemDTO & {
  /**
   *
   * 佣金计算标记（twcommission表中orderitemid与employid最新的一条，没有记录设置为false，有记录且bcancel=0且icommissionstatus=1【计算】的设置为true）
   */
  bcountflag?: boolean
  /**
   * 佣金参数组id - 默认的tbcommissionrategroup计提总表id
   */
  commissionrategroupid?: string
  /**
   * 佣金参数明细id - 查询到的要使用的佣金参数明细表id
   */
  commissionrateitemid?: string
  /**
   * 折扣降点 - 默认的tbcommissionrategroup计提总表对应的tbdiscountreduce折扣降点表中根据计提比例找到对应范围数据的折扣降点
   */
  discountreduce?: number
  /**
   * 计提金额 - 计算得出【提成金额=业绩金额*（提成比例-折扣下点）】
   */
  ncommission?: number
  /**
   * 计提比例 - 查询到的tbcommissionrategroup或者tbcommissionrateitem中要用到的那条记录的计提比例
   */
  ncommissionrate?: number
  /**
   * 风险金比例% - tbcommissionrategroup计提总表默认值
   */
  nrateofrisk?: number
  /**
   * 风险金 - 计算得出 风险金=业绩金额*风险金比例
   */
  riskfund?: number
  /**
   * 佣金计提表主键（如果bcountflag=false，则为null，如果为true，则为Twcommission表的id）
   */
  twcommissionid?: string
  /**
   * 标记(0-计算1-确认 2-发放 3-撤销)
   */
  icommissionstatus?: number
  /**
   * 确认说明
   */
  sconfirmremark?: string
  /**
   * 确认日期
   */
  dconfirmdate?: string
  /**
   * 发放说明
   */
  spayremark?: string
  /**
   * 发放日期
   */
  dpaydate?: string
  /**
   * 并发标记
   */
  version?: number
  /**
   * 业绩比例
   */
  archievementrate?: number
  /**
   * 部门id
   */
  deptid?: string
  /**
   * 部门名称
   */
  deptname?: string
  /**
   * 业务员类型
   */
  employtype?: number
}

/**
 * CommissionDTO
 */
export interface ICommissionDTO {
  /**
   * 业绩金额
   */
  amountachievement?: number
  /**
   * 业绩比例
   */
  archievementrate?: number
  /**
   * 是否冲抵0否 1是
   */
  bcancel?: boolean
  /**
   * 是否主业务员
   */
  bprimary?: boolean
  /**
   * 业务对象缓存key
   */
  cacheDTOKey?: string
  /**
   * 佣金参数组id
   */
  commissionrategroupid?: string
  /**
   * 佣金参数明细id
   */
  commissionrateitemid?: string
  /**
   * 确认者
   */
  confirmempid?: string
  confirmempname?: string
  /**
   * 创建者id
   */
  createempid?: string
  /**
   * 创建者名称
   */
  createempname?: string
  /**
   * 客户id(与业务员相关的)
   */
  customerid?: string
  /**
   * 客户名称
   */
  customername?: string
  /**
   * 确认日期
   */
  dconfirmdate?: string
  /**
   * 创建日期
   */
  dcreatetime?: string
  /**
   * 业务员部门id
   */
  deptid?: string
  /**
   * 部门名称
   */
  deptname?: string
  /**
   * 发放日期
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
   * 类型（0-直客业务员 1-代理业务员 2-内容生产方业务员）
   */
  employtype?: number
  /**
   * 标记(0-计算1-确认 2-发放 3-撤销)
   */
  icommissionstatus?: number
  /**
   * 主键
   */
  id?: string
  /**
   * 成本金额
   */
  namountcost?: number
  /**
   * 计提金额
   */
  ncommission?: number
  /**
   * 计提比例
   */
  ncommissionrate?: number
  /**
   * 风险金比例%
   */
  nrateofrisk?: number
  /**
   * 订单id
   */
  orderid?: string
  /**
   * 刊期编码
   */
  orderitemcode?: number
  /**
   * 刊期id
   */
  orderitemid?: string
  /**
   * 发放者
   */
  payempid?: string
  payempname?: string
  /**
   * 如果冲抵，则冲抵记录的id
   */
  relatedid?: number
  /**
   * 确认说明
   */
  sconfirmremark?: string
  /**
   * 合同号
   */
  scontractnum?: string
  /**
   * 发放说明
   */
  spayremark?: string
  /**
   * 备注
   */
  sremark?: string
  /**
   * 并发标记
   */
  version?: number
  [property: string]: any
}
