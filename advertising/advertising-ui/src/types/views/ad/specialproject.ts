/*
 * @Author: lhl
 * @Date: 2024-03-14
 * @Description: 特刊项目数据
 */
export type TSpecialProject = {
  /** 主键   */
  id?: string

  /** 名称   */
  sname: string

  /** 开始日期   */
  dstartdate: Date | string

  /** 结束日期    */
  denddate: Date | string
  /** 创建人id */
  createempid: string
  /** 创建人名称    */
  screatename: string
  /** 创建时间    */
  dcreatedate: Date | string
  /** 项目说明     */
  sprojectcontent: string
  /** 结项标记  */
  bprojectcomplete: number
  /** 序号 */
  isort: number

  /** 备注 */
  sremark: string
}
