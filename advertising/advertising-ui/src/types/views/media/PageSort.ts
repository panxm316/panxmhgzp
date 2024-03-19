/*
 * @Author: wanghl
 * @Date: 2023-09-19 16:18:59
 * @LastEditTime: 2023-11-10 11:19:01
 * @LastEditors: wanghl
 * @Description:版面类别type
 */

import { IBaseQueryInfo, IPageRequest } from '@/types/common'
/**
 *查询版面类别
 */
export type TPageSortQueryInfo = {
  /**
   * 业务对象缓存key
   */
  cacheDTOKey?: string
  loginUserId?: string
  mediaid?: string
  mediatypekey?: string
  foldid?: string
} & IPageRequest &
  IBaseQueryInfo
/**
 *版面类别修改
 */
export type TPageSortRequest = {
  /**
   * id
   */
  id?: string
  /**
   * 类别名称
   */
  sname?: string
  /**
   * 媒体类型key
   */
  mediatypekey?: string
  /**
   * 媒体类型
   */
  mediatypename?: string
  /**
   * 叠次id
   */
  foldid?: string | undefined
  /**
   * 叠次名称
   */
  foldname?: string | undefined
  /**
   * 是否启用
   */
  buse?: boolean
  /**
   * 序号
   */
  isort?: number
  /**
   * 备注
   */
  sremark?: null | string
  mediaid: string
}

/**
 * 版面类别
 *
 * @author wangxk
 * @since 2023-12-12
 */
export interface Tbpagesort {
  /**
   * id
   */
  id?: number
  /**
   * 版面类别名称
   */
  sname?: string
  /**
   * 媒体类型key
   */
  mediatypekey?: string
  /**
   * 媒体类型
   */
  mediatypename?: string
  /**
   * 叠次ID
   */
  foldid?: number
  /**
   * 叠次名称
   */
  foldname?: string
  /**
   * 备注
   */
  sremark?: string
  /**
   * 序号
   */
  isort?: number
  /**
   * 是否启用
   */
  buse?: boolean
}
