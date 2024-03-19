/*
 * @Author: wanghl
 * @Date: 2023-11-15 09:37:41
 * @LastEditTime: 2023-12-19 14:32:05
 * @LastEditors: wanghl
 * @Description:版面计划
 */
/**
 * 编辑版面计划批量
 */
export type TAFoldPagePlan = {
  /**
   * 色彩id
   */
  adcolorid?: string
  /**
   * 色彩名称
   */
  adcolorname?: string
  /**
   * 添加类型：PageNum: 按版数批量增加（版数为1是单个版添加）
   * PageColor:按广告色彩批量增加、
   * Duplicate: 按时间段复制（是按天复制或按周复制）
   */
  addType?: string
  /**
   * 广告版标记
   */
  adflag?: boolean
  /**
   * 按周复制标志，true:按周复制，false:按天复制
   */
  bworkDup?: boolean
  /**
   * 复制结束时间
   */
  duplicateEndTime?: string
  /**
   * 复制开始时间
   */
  duplicateStartTime?: string
  /**
   * 查询结束时间
   */
  endTime?: string
  /**
   * 叠次版本id
   */
  foldareaverid?: string
  /**
   * 叠次版本名称
   */
  foldareavername?: string
  /**
   * 叠次ID
   */
  foldid?: string
  /**
   * 叠次名称
   */
  foldname?: string
  /**
   * id
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
   * 媒体类型key
   */
  mediatypekey?: string
  /**
   * 媒体类型
   */
  mediatypename?: string
  /**
   * 版面色彩id
   */
  pagecolorid?: string
  /**
   * 版号
   */
  pagenum?: number
  /**
   * 版面标题
   */
  pagetitle?: string
  /**
   * 刊登日期
   */
  publishdate?: string
  /**
   * 刊期
   */
  publishnum?: number
  /**
   * 备注
   */
  sremark?: string
  /**
   * 开始时间
   */
  startTime?: string
  /**
   * 截版标记
   */
  stopflag?: boolean
  /**
   * 截版日期
   */
  stoptime?: string
  /** 版数 */
  PageNum?: number
  /**
   * 版心id
   */
  pagesizeid?: string
  /**
   * 版心名称
   */
  pagesizename?: string
}
/**
 * 单个添加版面计划
 */
export type TAFoldPagePlanOne = {
  /**
   * 色彩id
   */
  adcolorid?: string
  /**
   * 色彩名称
   */
  adcolorname?: string
  /**
   * 广告版标记
   */
  adflag?: boolean
  /**
   * 叠次版本id
   */
  foldareaverid?: string
  /**
   * 叠次版本名称
   */
  foldareavername?: string
  /**
   * 叠次ID
   */
  foldid?: string
  /**
   * 叠次名称
   */
  foldname?: string
  /**
   * id
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
   * 媒体类型key
   */
  mediatypekey?: string
  /**
   * 媒体类型
   */
  mediatypename?: string
  /**
   * 版号
   */
  pagenum?: number
  /**
   * 版面标题
   */
  pagetitle?: string
  /**
   * 刊登日期
   */
  publishdate?: string | Date
  /**
   * 刊期
   */
  publishnum?: number
  /**
   * 备注
   */
  sremark?: string
  /**
   * 截版标记
   */
  stopflag?: boolean
  /**
   * 截版日期
   */
  stoptime?: string | Date
  /**
   * 版心id
   */
  pagesizeid?: string
  /**
   * 版心名称
   */
  pagesizename?: string
  /**
   * 高(mm)
   */
  ipageheight?: number | string
  /**
   * 宽(mm)
   */
  ipagewidth?: number | string
}
/**
 * 查询
 */
export type TQFoldPagePlan = {
  /**
   * 按周复制标志
   */
  bworkDup?: string
  /**
   * 查询结束时间
   */
  endTime?: string
  /**
   * 叠次版本id
   */
  foldareaverid?: string
  /**
   * 叠次版本名称
   */
  foldareavername?: string
  /**
   * 叠次ID
   */
  foldid?: string
  /**
   * 叠次名称
   */
  foldname?: string
  /**
   * 媒体id
   */
  mediaid?: string
  /**
   * 媒体名称
   */
  medianame?: string
  /**
   * 媒体类型key
   */
  mediatypekey?: string
  /**
   * 媒体类型
   */
  mediatypename?: string
  /**
   * asc/desc
   */
  order?: string
  /**
   * 当前页
   */
  page?: number
  /**
   * 版面色彩id
   */
  pagecolorid?: string
  /**
   * 每页显示记录数
   */
  pageSize?: number
  /**
   * 排序字段
   */
  sort?: string
  /**
   * 开始时间
   */
  startTime?: string
}
/**
 * 按媒体、叠、类别、色彩、日期等获取版面计划列表
 */
export type TQPageplanList = {
  /**
   * 色彩id
   */
  adcolorid?: string
  /**
   * 查询结束时间
   */
  endTime?: string
  /**
   * 叠次版本id
   */
  foldareaverid?: string
  /**
   * 叠次版本名称
   */
  foldareavername?: string
  /**
   * 叠次ID
   */
  foldid?: string
  /**
   * 媒体id
   */
  mediaid?: string
  /**
   * 媒体类型key
   */
  mediatypekey?: string
  /**
   * 刊登日期
   */
  publishdate?: string
  /**
   * 开始时间
   */
  startTime?: string
  /**
   * 媒体名称
   */
  foldname?: string
  /**
   * 叠次名称
   */
  medianame?: string
  /**
   * 媒体类型
   */
  mediatypename?: string
  /**
   * 版面id
   */
  foldpageplanid?: string
}
