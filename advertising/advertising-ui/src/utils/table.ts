/**
 * 表格排序
 * @param val
 * @param queryInfo
 * @param callback
 */
export const handleSortChange = (
  val: { prop: string; order: string },
  queryInfo: any,
  callback: Function
) => {
  queryInfo.sort = val.prop
  if (val.order === 'ascending') {
    queryInfo.order = 'asc'
  } else if (val.order === 'descending') {
    queryInfo.order = 'desc'
  } else {
    queryInfo.order = ''
  }
  callback()
}
