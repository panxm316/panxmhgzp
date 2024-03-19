import { TQueryInfo } from '@/types/common'
/**
 * @description: 系统管理操作日志对象类型
 * @return {*}
 */
export type TOperatelog = {
  /**
   * 操作员id
   */
  employid?: string
  /**
   * 操作员名称
   */
  employname?: string
  /**
   * 主键
   */
  id?: string
  /**
   * 操作记录主表id
   */
  recordmasterid?: string
  /**
   * 操作记录从表id
   */
  recordslaveid?: string
  /**
   * 日志操作名称
   */
  slogname?: string
  /**
   * 日志类型(新增、删除、修改)
   */
  slogtype?: string
  /**
   * 新值
   */
  snewvalue?: string
  /**
   * 旧值
   */
  soldvalue?: string
  /**
   * 操作ip
   */
  soperateip?: string
  /**
   * 操作mac
   */
  soperatemac?: string
  /**
   * 操作sql
   */
  soperatesql?: string
  /**
   * 操作sql参数
   */
  soperatesqlparam?: string
  /**
   * 操作日期
   */
  soperatetime?: string
  /**
   * 备注
   */
  sremark?: string
  /**
   * 操作表名
   */
  stablename?: string
}
/**
 * @description: 系统管理操作日志查询传参类型
 * @return {*}
 */
export type TOperatelogQueryInfo = TQueryInfo & { slogtype?: string }
