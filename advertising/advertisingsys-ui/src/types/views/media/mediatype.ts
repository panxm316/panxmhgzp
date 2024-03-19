/*
 * @Author: suny
 * @Date: 2023-08-23 12:53:26
 * @LastEditTime: 2023-09-19 17:23:50
 * @LastEditors: suny
 * @Description: 媒体类型管理相关实体类型
 */

import { IBaseQueryInfo, IPageRequest } from '@/types/common'

/** 媒体类型类型 */
export interface IMediaType {
  /**
   * 是否默认
   */
  bdefault?: boolean
  /**
   * 是否启用
   */
  buse?: boolean
  /**
   * id
   */
  id: string
  /**
   * 排序
   */
  isort?: number
  /**
   * 描述
   */
  sdesc?: string
  /**
   * 媒体类型关键字
   */
  skey: string
  /**
   * 媒体类型名称
   */
  sname: string
}

/** 媒体查询实体类 */
export type IMediaTypeQuery = IPageRequest & IBaseQueryInfo
