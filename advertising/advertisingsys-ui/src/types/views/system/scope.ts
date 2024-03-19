/*
 * @Author: songly
 * @Date: 2023-08-30 16:07:22
 * @LastEditTime: 2023-09-06 13:43:35
 * @LastEditors: songly
 * @Description:范围
 */
export type TScope = {
  /** 主键   */
  id: string | number
  /** 名称   */
  sname: string
  /** 范围表名(设置) */
  stablename: string
  /** 范围列名(设置) */
  scolumn: string
  /** 范围表名(查询sql) */
  squeryname: string
  /** 范围列名(查询sql) */
  squerycolumn: string
  /** 部门深度 */
  idepth: number
  /** 序号   */
  isort: number
  /** 是否有效   */
  buse?: boolean
}
