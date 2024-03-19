/*
 * @Author: suny
 * @Date: 2023-11-30 15:15:10
 * @LastEditTime: 2023-11-30 15:21:31
 * @LastEditors: suny
 * @Description: 折扣降点实体对象
 */
import { IBaseQueryInfo, IPageRequest } from '@/types/common'
/** 折扣下点查询对象 */
export type TDiscountReduceQuery = { commissionrategroupid?: string } & IPageRequest &
  IBaseQueryInfo
/**
 * DiscountReduceDTO
 * 折扣下点实体对象
 */
export interface IDiscountReduceDTO {
  /**
   * 业务对象缓存key
   */
  cacheDTOKey?: string
  /**
   * 组id
   */
  commissionrategroupid?: string
  /**
   * 组名称
   */
  commissionrategroupname?: string
  /**
   * 主键
   */
  id?: string
  /**
   * 结束折扣（8折）%
   */
  ndiscountend?: number
  /**
   * 开始折扣（9折）%
   */
  ndiscountstart?: number
  /**
   * 代理公司下点值%
   */
  nrateofagency?: number
  /**
   * 内容生产方下点值%
   */
  nrateofagent?: number
  /**
   * 直接客户下点值%
   */
  nrateofcustomer?: number
}
