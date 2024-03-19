/*
 * @Author: suny
 * @Date: 2023-11-25 14:09:23
 * @LastEditTime: 2023-11-30 09:04:53
 * @LastEditors: suny
 * @Description: 计提明细实体对象
 */
import { IBaseQueryInfo, IPageRequest } from '../../common/index'
/**
 * 计提明细查询对象
 */
export type TCommissionRateItemQuery = ICommissionRateItemVO & IPageRequest & IBaseQueryInfo
export interface ICommissionRateItemVO {
  /**
   * 行业id
   */
  adindustryid?: string
  /**
   * 业务对象缓存key
   */
  cacheDTOKey?: string
  /**
   * 组id
   */
  commissionrategroupid?: string
  /**
   * 部门id
   */
  deptid?: string
  /**
   * 人员id
   */
  employid?: string
  /**
   * 媒体id
   */
  mediaid?: string
}
/**
 * <p>
 * 计提比例明细表
 * </p>
 *
 * Tbcommissionrateitem
 */
export interface ITbcommissionrateitem {
  /**
   * 行业id
   */
  adindustryid?: string
  /**
   * 行业名称
   */
  adindustryname?: string
  /**
   * 组id
   */
  commissionrategroupid?: string
  /**
   * 组名称
   */
  commissionrategroupname?: string
  /**
   * 部门id
   */
  deptid?: string
  /**
   * 部门名称
   */
  deptname?: string
  /**
   * 人员id
   */
  employid?: string
  /**
   * 人员名称
   */
  employname?: string
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
   * 内容生产方默认提成点%
   */
  nrateofagent?: number
  /**
   * 代理公司默认提成点%
   */
  nrateofagency?: number
  /**
   * 直接客户默认提成点%
   */
  nrateofcustomer?: number
}
