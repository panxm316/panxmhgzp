/*
 * @Author: suny
 * @Date: 2023-09-19 15:47:48
 * @LastEditTime: 2023-09-22 14:44:22
 * @LastEditors: suny
 * @Description: 叠次实体对象
 */
import { IBaseQueryInfo, IPageRequest } from '@/types/common'

/**
 * 叠次对象
 */
export type IFoldVO = {
  /**
   * 启用标志
   */
  buse?: boolean
  id?: string
  /**
   * 序号
   */
  isort?: number
  /**
   * 媒体id
   */
  mediaid: string
  /**
   * 媒体名称
   */
  medianame?: string
  /**
   * 版数
   */
  pagecount?: number
  /**
   * 叠次名称
   */
  sname: string
  /**
   * 备注
   */
  sremark?: string
}
/** 叠次查询实体类 */
export type TFoldQuery = { mediatypename: string; mediaid: string } & IPageRequest & IBaseQueryInfo
