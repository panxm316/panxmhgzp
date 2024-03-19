/*
 * @Author: songly
 * @Date: 2023-12-25 13:30:15
 * @LastEditTime: 2024-03-13 15:31:20
 * @LastEditors: wanghl
 * @Description:
 */
import { TQueryInfo, IPageRequest } from '@/types/common'
export type TOrderitemsupplementpublishSearch = {
  /** 录入方式 0-正常 1-广告预约 2-快速预约 3-补刊*/
  ibooktype?: undefined | number
  /** 媒体id*/
  mediaid?: string
  /** 叠次id*/
  foldid?: string
  /** 叠次版本id*/
  foldareaverid?: string
  /**
   * 业务对象缓存key
   */
  cacheDTOKey?: string
  loginUserId?: string
  /**
   * 媒体类型key
   */
  mediatypekey?: string
  /**
   * 合同号
   */
  scontractnum?: string
} & TQueryInfo

/**
 * 停刊类型
 */
export type TStopType = {
  flowId?: string
  ids?: string
}
