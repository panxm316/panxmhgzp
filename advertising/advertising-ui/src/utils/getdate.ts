/*
 * @Author: suny
 * @Date: 2023-10-31 14:10:32
 * @LastEditTime: 2024-03-08 10:49:43
 * @LastEditors: suny
 * @Description: 获取周、月、年份对应日期
 */
/**
 * 获取本周第一天和最后一天
 */
export const getWeekDates = () => {
  // 获取当前日期
  var today = new Date()
  // 获取当前是星期几
  var day = today.getDay()
  // 计算本周第一天的日期
  var startDate = new Date(today.getFullYear(), today.getMonth(), today.getDate() - day)
  // 计算本周最后一天的日期
  var endDate = new Date(today.getFullYear(), today.getMonth(), today.getDate() - day + 6)
  // 返回本周的日期范围

  return { start: startDate, end: endDate }
}
/**
 * 获取本月第一天和最后一天
 */
export const getMonthDates = () => {
  const date = new Date()
  date.setDate(1)
  const startDate = new Date(new Date(date).toLocaleDateString())
  let month = date.getMonth()
  const nextMonth = ++month
  const nextMonthFirstDay = new Date(date.getFullYear(), nextMonth, 1)
  const endDate = new Date(new Date(nextMonthFirstDay).toLocaleDateString())
  endDate.setDate(0)
  // 返回本周的日期范围
  return { start: startDate, end: endDate }
}
/**
 * 获取本年度第一天和最后一天
 */
export const getYearDates = () => {
  const date = new Date()
  const startDate = new Date(new Date(date.getFullYear(), 0, 1).toLocaleDateString())

  let year = date.getFullYear()
  const nextYear = ++year
  const nextstartDate = new Date(new Date(nextYear, 0, 1))
  nextstartDate.setDate(0)
  const endDate = new Date(new Date(nextstartDate).toLocaleDateString())

  // 返回本周的日期范围
  return { start: startDate, end: endDate }
}
/**
 * 获取指定月数前的日期
 */
export const getMonthNumDate = (monthNum: Number) => {
  var now = new Date()
  var year = now.getFullYear() // 得到年份
  var month = now.getMonth() + 1 // 得到月份
  var date = now.getDate() // 得到日期
  var hour = ' 00:00:00' // 默认时分秒 如果传给后台的格式为年月日时分秒，就需要加这个，如若不需要，此行可忽略
  month = month.toString().padStart(2, '0')
  date = date.toString().padStart(2, '0')
  var defaultDate = `${year}-${month}-${date}` // 当前日期

  var defaultDate1 = GetPreMonthDay(defaultDate, monthNum) // 指定的数据 月前
  console.log(defaultDate1)
  return { start: '', end: defaultDate1 }
  // this.QueryTime.push(defaultDate1, defaultDate)
}
const GetPreMonthDay = (date, monthNum) => {
  var dateArr = date.split('-')
  var year = dateArr[0] // 获取当前日期的年份
  var month = dateArr[1] // 获取当前日期的月份
  var day = dateArr[2] // 获取当前日期的日
  var days = new Date(year, month, 0)
  days = days.getDate() // 获取当前日期中月的天数
  var year2 = year
  var month2 = parseInt(month) - monthNum
  if (month2 <= 0) {
    var absM = Math.abs(month2)
    year2 = parseInt(year2) - Math.ceil(absM / 12 === 0 ? 1 : parseInt(absM) / 12)
    month2 = 12 - (absM % 12)
  }
  var day2 = day
  var days2 = new Date(year2, month2, 0)
  days2 = days2.getDate()
  if (day2 > days2) {
    day2 = days2
  }
  if (month2 < 10) {
    month2 = '0' + month2
  }
  var t2 = year2 + '-' + month2 + '-' + day2
  return t2
}

/**
 * 日期下拉初始值
 * Returns an object containing initial data for a date combo component.
 *
 * @param {Number} type - The type of date combo.
 * @param {string} [startTime=''] - The start time.
 * @param {string} [endTime=''] - The end time.
 * @returns {Object} - An object with timetype, timeoption, startTime and endTime properties.
 */
export const getInitDateCombo = (type: string, startTime: string = '', endTime: string = '') => {
  return {
    timetype: type,
    timeoption: [
      { label: '今天', value: '0' },
      { label: '三天内', value: '2' },
      { label: '一周内', value: '6' },
      { label: '二周内', value: '13' },
      { label: '一月内', value: '30' },
      { label: '自定义', value: '00' }
    ],
    startTime: startTime,
    endTime: endTime
  }
}
