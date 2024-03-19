import { TQueryInfo } from '@/types/common'

export type TLog = {
  /**
   * 操作时间
   */
  doperatordate?: string
  /**
   * 操作员id
   */
  employid?: number
  /**
   * 操作员姓名
   */
  employname?: string
  /**
   * 主键
   */
  id?: number
  /**
   * 日志类型（新增、修改或删除）
   */
  slogtype?: string
  /**
   * 操作员IP
   */
  soperatorip?: string
  /**
   * 操作员Mac
   */
  soperatormac?: string
  /**
   * 备注
   */
  sremark?: string
}

export type TLogQueryInfo = TQueryInfo & { slogtype?: string }
