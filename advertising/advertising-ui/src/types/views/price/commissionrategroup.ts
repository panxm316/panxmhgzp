/*
 * @Author: suny
 * @Date: 2023-11-25 14:01:23
 * @LastEditTime: 2023-11-29 12:37:49
 * @LastEditors: suny
 * @Description: 计提总表实体对象
 */

import { IBaseQueryInfo, IPageRequest } from '@/types/common'
/**
 * 计提总表查询对象
 */
export type TCommissionRateGroupQuery = IPageRequest & IBaseQueryInfo
/**
 * <p>
 * 计提比例总表
 * </p>
 *
 * Tbcommissionrategroup
 */
export interface ITbcommissionrategroup {
  /**
   * 是否默认
   */
  bdefault?: boolean
  /**
   * 是否有效
   */
  buse?: boolean
  /**
   * 业务对象缓存key
   */
  cacheDTOKey?: string
  /**
   * 名称
   */
  commissionrategroupname: string
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
   * 开始日期
   */
  dstartdate?: string
  /**
   * 主键
   */
  id: string
  /**
   * 内容生产方默认提成点%
   */
  nrateofagent: number
  /**
   * 代理公司默认提成点%
   */
  nrateofagency: number
  /**
   * 直接客户默认提成点%
   */
  nrateofcustomer: number
  /**
   * 风险金比例%
   */
  nrateofrisk?: number
  /**
   * 备注
   */
  sremark?: string
}
