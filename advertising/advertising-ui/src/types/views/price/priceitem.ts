/*
 * @Author: caogd
 * @Date: 2023-11-13 13:00:43
 * @LastEditTime: 2023-12-13 13:37:53
 * @LastEditors: wanghl
 * @Description:价格明细表类型定义
 */
import { IBaseQueryInfo, IPageRequest } from '@/types/common'
/** 价格明细查询对象 */
export type TPriceItemQuery = TPriceItemVO & IPageRequest & IBaseQueryInfo
/** 批量复制价格明细保存对象 */
export type TPriceItemCopy = { ids: string } & IBaseQueryInfo

export type TPriceItemVO = {
  /**
   * 主键
   */
  id?: string
  /**
   * 色彩id
   */
  adcolorid?: string
  /**
   * 色彩名称
   */
  adcolorname?: string
  /**
   * 广告形式id
   */
  addisplayformid?: string
  /**
   * 广告形式名称
   */
  addisplayformname?: string
  /**
   * 规格id
   */
  adspecid?: string
  /**
   * 规格名称
   */
  adspecname?: string
  /**
   * 价格
   */
  baseprice?: number
  /**
   * 是否有效
   */
  buse?: boolean

  /**
   * 结束日期
   */
  denddate?: string
  /**
   * 开始日期
   */
  dstartdate?: string

  /**
   * 叠次版本ID
   */
  foldareaid?: string
  /**
   * 叠次版本名称
   */
  foldareaname?: string
  /**
   * 叠次id
   */
  foldid?: string
  /**
   * 叠次名称
   */
  foldname?: string

  /**
   * 序号
   */
  isort?: number
  /**
   * 媒体id
   */
  mediaid?: string
  /**
   * 媒体名称
   */
  medianame?: string
  /**
   * 媒体类型id
   */
  mediatypekey?: string
  /**
   * 媒体类型
   */
  mediatypename?: string
  /**
   * 版面类别id
   */
  pagesortid?: string
  /**
   * 版面类别名称
   */
  pagesortname?: string
  /**
   * 价格表id
   */
  pricegroupid?: string
  /**
   * 价格表名称
   */
  pricegroupname?: string

  /**
   * 备注
   */
  sremark?: string
  /**
   * 星期id
   */
  weeksettingid?: string
  /**
   * 星期名称
   */
  weeksettingname?: string
}
export type TPriceItemDTO = {
  /**
   * 色彩id
   */
  adcolorid?: string
  /**
   * 色彩名称
   */
  adcolorname?: string
  /**
   * 广告形式id
   */
  addisplayformid?: string
  /**
   * 广告形式名称
   */
  addisplayformname?: string
  /**
   * 规格id
   */
  adspecid?: string
  /**
   * 规格名称
   */
  adspecname?: string
  /**
   * 价格
   */
  baseprice?: number
  /**
   * 是否有效
   */
  buse?: boolean
  /**
   * 结束日期
   */
  denddate?: string
  /**
   * 开始日期
   */
  dstartdate?: string
  /**
   * 叠次版本ID
   */
  foldareaid?: string
  /**
   * 叠次版本名称
   */
  foldareaname?: string
  /**
   * 叠次id
   */
  foldid?: string
  /**
   * 叠次名称
   */
  foldname?: string
  /**
   * 主键
   */
  id?: string
  /**
   * 序号
   */
  isort?: number
  /**
   * 媒体id
   */
  mediaid?: string
  /**
   * 媒体名称
   */
  medianame?: string
  /**
   * 媒体类型id
   */
  mediatypekey?: string
  /**
   * 媒体类型
   */
  mediatypename?: string
  /**
   * 版面类别id
   */
  pagesortid?: string
  /**
   * 版面类别名称
   */
  pagesortname?: string
  /**
   * 价格表id
   */
  pricegroupid?: string
  /**
   * 价格表名称
   */
  pricegroupname?: string
  /**
   * 备注
   */
  sremark?: string
  /**
   * 星期id
   */
  weeksettingid?: string
  /**
   * 星期名称
   */
  weeksettingname?: string
}
/**
 * 列表查询对象
 */
export type TPriceItemQuerylist = {
  /**
   * 色彩id
   */
  adcolorid?: string
  /**
   * 色彩名称
   */
  adcolorname?: string
  /**
   * 广告形式id
   */
  addisplayformid?: string
  /**
   * 广告形式名称
   */
  addisplayformname?: string
  /**
   * 规格id
   */
  adspecid?: string
  /**
   * 规格名称
   */
  adspecname?: string
  /**
   * 价格
   */
  baseprice?: string
  /**
   * 是否有效
   */
  buse?: boolean
  /**
   * 业务对象缓存key
   */
  cacheDTOKey?: string
  /**
   * 结束日期
   */
  denddate?: string
  /**
   * 开始日期
   */
  dstartdate?: string
  /**
   * 查询结束时间
   */
  endTime?: string
  /**
   * 叠次版本ID
   */
  foldareaid?: string
  /**
   * 叠次版本名称
   */
  foldareaname?: string
  /**
   * 叠次id
   */
  foldid?: string
  /**
   * 叠次名称
   */
  foldname?: string
  /**
   * 主键
   */
  id?: string
  /**
   * 序号
   */
  isort?: string
  loginUserId?: string
  /**
   * 媒体id
   */
  mediaid?: string
  /**
   * 媒体名称
   */
  medianame?: string
  /**
   * 媒体类型id
   */
  mediatypekey?: string
  /**
   * 媒体类型
   */
  mediatypename?: string
  /**
   * 版面类别id
   */
  pagesortid?: string
  /**
   * 版面类别名称
   */
  pagesortname?: string
  /**
   * 价格表id
   */
  pricegroupid?: string
  /**
   * 价格表名称
   */
  pricegroupname?: string
  /**
   * 查询关键字
   */
  queryKey?: string
  /**
   * 备注
   */
  sremark?: string
  startTime?: string
  /**
   * 年份
   */
  syear?: string
  /**
   * 星期id
   */
  weeksettingid?: string
  /**
   * 星期名称
   */
  weeksettingname?: string
}
