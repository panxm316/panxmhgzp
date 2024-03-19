/*
 * @Author: songly
 * @Date: 2023-11-22 14:53:32
 * @LastEditTime: 2023-12-12 16:48:33
 * @LastEditors: wanghl
 * @Description: 折扣组类型定义
 */
import { IBaseQueryInfo, IPageRequest } from '@/types/common'
/** 组查询对象 */
export type TDiscountGroupQuery = IPageRequest & IBaseQueryInfo & { syear: string }

export type TDiscountGroup = {
  /**
   * 主键
   */
  id: string
  /**
   * 名称
   */
  discountgroupname: string

  /**
   * 年份
   */
  syear: string

  /**
   * 直接客户折扣
   */
  ncustomerdiscount: number

  /**
   * 代理公司折扣
   */
  nagencydiscount: number

  /**
   * 内容生产方折扣
   */
  nagentdiscount: number

  /**
   * 是否按最高
   */
  bhighest: boolean

  /**
   * 是否有效
   */
  buse: boolean

  /**
   * 备注
   */
  sremark: string

  /**
   * 创建者id
   */
  createempid: string

  /**
   * 创建者名称
   */
  createempname: string

  /**
   * 创建日期
   */
  createdate: string
}
