/*
 * @Author: songly
 * @Date: 2023-11-27 13:31:16
 * @LastEditTime: 2023-11-27 13:38:01
 * @LastEditors: songly
 * @Description:版心
 */
export type TPageSize = {
  /** 主键   */
  id?: number
  /** 名称   */
  sname: string
  /** 宽度     */
  ipagewidth: number
  /** 高度     */
  ipageheight: number
  /** 单行显示个数   */
  isinglescale: number
  /** 标准显示比例     */
  iscale: number
  /** 版位安排版面规则    */
  smoveflag: string

  /** * 栏宽宽度     */
  icolwidth: number

  /** 字号   */
  nfontsize: number

  /** 序号   */
  isort: number
  /** 是否有效   */
  buse?: boolean
  /** 是否默认     */
  bdefault: boolean
}
