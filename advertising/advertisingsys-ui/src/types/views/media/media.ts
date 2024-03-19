import { IBaseQueryInfo, IPageRequest } from '@/types/common'

/*
 * @Author: suny
 * @Date: 2023-08-23 12:53:26
 * @LastEditTime: 2023-11-06 10:24:08
 * @LastEditors: peij
 * @Description: 媒体管理相关实体类型
 */
/** 媒体信息实体类 */
export interface IMedia {
  /**
   * 启用标志
   */
  buse: boolean
  /**
   * id
   */
  id: string
  /**
   * 媒体序列号
   */
  isort: number
  /**
   * 媒体编码
   */
  scode: string
  /**
   * 媒体名称
   */
  sname: string
  /**
   * 媒体类型
   */
  mediatypename: string
  /**
   * 媒体类型key
   */
  mediatypekey: string
}

/** 媒体查询实体类 */
export type IMediaQuery = IPageRequest & IBaseQueryInfo
