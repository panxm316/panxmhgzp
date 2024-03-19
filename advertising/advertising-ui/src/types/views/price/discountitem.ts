/*
 * @Author: songly
 * @Date: 2023-11-22 15:18:30
 * @LastEditTime: 2023-12-13 09:56:57
 * @LastEditors: wanghl
 * @Description:折扣明细类型定义
 */
import { IBaseQueryInfo, IPageRequest } from '@/types/common'
/** 价格组查询对象 */
export type TDiscountItemQuery = TDiscountItem & IPageRequest & IBaseQueryInfo

export type TDiscountItem = {
  id: string

  /**
   * 名称
   */
  discountgroupname: string
  /**
   * 折扣组id
   */
  discountgroupid: string

  /**
   * 媒体id
   */
  mediaid: string

  /**
   * 媒体名称
   */
  medianame: string | undefined

  /**
   * 行业id
   */
  adindustryid: string

  /**
   * 行业名称
   */
  adindustryname: string

  /**
   * 直接客户
   */
  bcustomer: boolean

  /**
   * 代理公司
   */
  bagency: boolean

  /**
   * 内容生产方
   */
  bagent: boolean

  /**
   * 是否大客户
   */
  bvip: boolean

  /**
   * 折扣
   */
  ndiscount: number

  /**
   * 备注
   */
  sremark: string
}
/**
 * 折扣明细下拉列表
 */
export interface TQDiscountGroup {
  /**
   * 行业id
   */
  adindustryid?: string
  /**
   * 行业名称
   */
  adindustryname?: string
  /**
   * 代理公司
   */
  bagency?: boolean
  /**
   * 内容生产方
   */
  bagent?: boolean
  /**
   * 直接客户
   */
  bcustomer?: boolean
  /**
   * 是否大客户
   */
  bvip?: boolean
  /**
   * 折扣组id
   */
  discountgroupid?: string
  /**
   * 主键
   */
  id?: string
  /**
   * 媒体id
   */
  mediaid?: string
  /**
   * 媒体名称
   */
  medianame?: string
  /**
   * 折扣
   */
  ndiscount?: number
  /**
   * 备注
   */
  sremark?: string
  /**
   * 年份
   */
  syear?: string
}
