/**
 * Twempholidays假期数据对象
 */
export interface ITwempholiday {
  /**
   * 是否启用
   */
  buse?: boolean
  /**
   * 创建日期
   */
  dcreatedate?: string
  /**
   * 结束日期
   */
  denddate?: string
  /**
   * 开始日期
   */
  dstartdate?: string
  /**
   * 人员ID
   */
  employid?: string
  /**
   * 人员名称
   */
  employname?: string
  /**
   * 主键
   */
  id?: string
  /**
   * 接手人员ID
   */
  newemployid?: string
  /**
   * 接手人员名称
   */
  newemployname?: string
  /**
   * 操作员ID
   */
  operatorid?: string
  /**
   * 操作员名称
   */
  operatorname?: string
  /**
   * 备注
   */
  sremark?: string
  [property: string]: any
}
