/*
 * @Author: peij
 * @Date: 2023-08-24 13:28:42
 * @LastEditTime: 2023-11-25 14:27:08
 * @LastEditors: wanghl
 * @Description: TAdcolo type
 */
export type TAdcolor = {
  /**
   * 是否启用
   */
  buse: boolean
  /**
   * id
   */
  id?: string
  /**
   * 媒体类型key
   */
  mediatypekey?: string
  /**
   * 媒体类型名
   */
  mediatypename?: string
  /**
   * 序号
   */
  isort: number
  /**
   * 名称
   */
  sname: string
  /**
   * 备注
   */
  sremark?: string
}
/**
 * 根据媒体获取色彩树
 */
export type TAdcolorList = {
  bleaf?: boolean
  checked?: boolean
  children?: TAdcolorList[]
  iconSkin?: string
  buse: boolean
  id?: string
  name?: string
  nocheck?: false
  parentId?: null
  stype?: ''
}
