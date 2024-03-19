/*
 * @Author: wanghl
 * @Date: 2023-08-23 12:41:52
 * @LastEditTime: 2023-09-06 13:27:44
 * @LastEditors: songly
 * @Description:付款时间
 */
export type TPaysort = {
  /**
   * 是否启用
   */
  buse: boolean
  /**
   * id
   */
  id: string
  /**
   * 序号
   */
  isort: number
  /**
   * 描述
   */
  sdesc: string
  /**
   * 付款时间关键字 刊前付款 bepub 刊后付款 afpub
   */
  skey: string
  /**
   * 类别名称
   */
  sname: string
  /**
   * 备注
   */
  sremark?: string
}
