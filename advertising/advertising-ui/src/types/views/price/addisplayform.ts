/*
 * @Author: suny
 * @Date: 2023-11-06 10:47:48
 * @LastEditTime: 2023-11-06 10:54:22
 * @LastEditors: suny
 * @Description: 广告形式相关实体对象
 */
import { IBaseQueryInfo, IPageRequest } from '@/types/common'

/** 广告形式查询实体类 */
export type TAdDisplayFormQuery = ITbaddisplayform & IPageRequest & IBaseQueryInfo

/**
 * <p>
 * 广告形式
 * </p>
 *
 * Tbaddisplayform
 */
export interface ITbaddisplayform {
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
   * 广告形式名称
   */
  sname?: string
  /**
   * 备注
   */
  sremark?: string
}
