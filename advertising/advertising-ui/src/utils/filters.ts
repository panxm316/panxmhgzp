export function parseTime(time: any) {
  var strTime
  var date = new Date(time)
  var year = date.getFullYear()
  var month = date.getMonth() + 1
  var days = date.getDate()
  strTime = year.toString() + '-' + month.toString() + '-' + days.toString()
  return strTime
}
