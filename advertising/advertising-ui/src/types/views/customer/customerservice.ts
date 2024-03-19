/*
 * @Author: suny
 * @Date: 2023-11-09 11:07:06
 * @LastEditTime: 2023-11-09 12:54:43
 * @LastEditors: suny
 * @Description: 客户服务相关实体对象
 */

import { IBaseQueryInfo, IPageRequest } from '@/types/common'

/** 客户服务查询对象 */
export type Tcustomerservicequery = IcustomerserviceDTO & IBaseQueryInfo & IPageRequest
/**
 * <p>
 * 客户服务表
 * </p>
 *
 * Twcustomerservice
 */
export interface IcustomerserviceDTO {
  /**
   * 是否删除标记
   */
  bdelete?: boolean
  /**
   * 客户id
   */
  customerid?: string
  /**
   * 客户名称
   */
  customername?: string
  /**
   * 创建日期
   */
  dcreatetime?: string
  /**
   * 结束日期
   */
  denddate?: string
  /**
   * 最后修改日期
   */
  dlasttime?: string
  /**
   * 开始日期
   */
  dstartdate?: string
  /**
   * 人员id
   */
  employid?: string
  /**
   * 人员名称
   */
  employname?: string
  /**
   * id
   */
  id?: string
  /**
   * 0-客户投诉 1-客户建议 2-客户调查
   */
  iservicetype?: number | string
  /**
   * 最后修改者
   */
  lastoperator?: string
  /**
   * 最后修改者id
   */
  lastoperatorid?: string
  /**
   * 操作员id
   */
  operatorid?: string
  /**
   * 操作员
   */
  operatorname?: string
  /**
   * 联系人
   */
  scontacts?: string
  /**
   * 内容
   */
  scontent?: string
  /**
   * 联系方式
   */
  sphone?: string
  /**
   * 结果
   */
  sresult?: string
  /**
   * 状态(可选可填写：已记录、已解决、已结束等)
   */
  sstatus?: string
  /**
   * 标题
   */
  stitle?: string
}
