/*
 * @Author: songly
 * @Date: 2023-09-01 10:52:01
 * @LastEditTime: 2023-09-12 10:22:59
 * @LastEditors: songly
 * @Description:日期类型 从汇畅复制
 */
export type TDateType = {
  timetype: string
  timeoption: TTimeOptionType
} & TDateTimeType

export type TTimeOptionType = {
  label: string
  value: string
}[]

export type TDateTimeType = {
  startTime: string
  endTime: string
}
