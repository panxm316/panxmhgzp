/*
 * @Author: suny
 * @Date: 2023-11-10 14:25:01
 * @LastEditTime: 2024-01-10 14:51:46
 * @LastEditors: wanghl
 * @Description: 价格组相关实体对象
 */
import { IBaseQueryInfo, IPageRequest } from '@/types/common'
/** 价格组查询对象 */
export type TPriceGroupQuery = IPriceGroupDTO & IPageRequest & IBaseQueryInfo
/**
 * 价格组实体对象
 */
export interface IPriceGroupDTO {
  /**
   * 是否默认
   */
  bdefault?: boolean
  /**
   * 启用标志
   */
  buse?: boolean
  /**
   * 主键
   */
  id?: string
  /**
   * 序号
   */
  isort?: number
  /**
   * 媒体类型key
   */
  mediatypekey?: string
  /**
   * 媒体类型名称
   */
  mediatypename?: string
  /**
   * 价格表名称
   */
  sname?: string
  /**
   * 备注
   */
  sremark?: string
  /**
   * 年份
   */
  syear?: string
}
/**
 * 价格组实体对象
 */
export interface TQIPriceGroupD {
  /**
   * 是否默认
   */
  bdefault?: boolean | string
  /**
   * 启用标志
   */
  buse?: boolean
  /**
   * 业务对象缓存key
   */
  cacheDTOKey?: string
  /**
   * 查询结束时间
   */
  endTime?: string
  loginUserId?: string
  /**
   * 媒体类型key
   */
  mediatypekey?: string
  /**
   * 媒体类型名称
   */
  mediatypename?: string
  /**
   * 查询关键字
   */
  queryKey?: string
  startTime?: string
  /**
   * 年份
   */
  syear?: string
}
