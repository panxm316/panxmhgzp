/*
 * @Author: muyn
 * @Date: 2023-08-24 15:59:09
 * @LastEditTime: 2024-03-18 15:59:32
 * @LastEditors: wanghl
 * @Description: 广告行业
 */
import { IBaseQueryInfo, IPageRequest } from '@/types/common/index'
export type TAdIndustry = {
  /**
   * id
   */
  id: string
  /**
   * 父id
   */
  parentid: string
  /**
   * 类别名称
   */
  sname: string
  /**
   * 本级编号
   */
  slocalid: string
  /**
   * 类别级别
   */
  idepth: number
  /**
   * 类别解释
   */
  sdesc: string
  /**
   * 备注
   */
  sremark: string
  /**
   * 文件路径
   */
  sfilepath: string
  /**
   * 文件名称
   */
  sfilename: string
  /**
   * 序号
   */
  isort: number
  /**
   * 是否分类广告
   */
  bclassified: boolean
  /**
   * 节点禁用
   */
  buse: boolean
  /**
   * 链接
   */
  sdurl: string
  /**
   * 上传文件
   */
  picturepath: string
}
export type TAdIndustrySearch = {
  sname: string
  buse: number | string
} & IPageRequest
