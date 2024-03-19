/*
 * @Author: songly
 * @Date: 2023-11-27 13:33:04
 * @LastEditTime: 2023-11-27 13:42:33
 * @LastEditors: songly
 * @Description:版心
 */
import request from '@/utils/request'
export const getPageSizeListApi = () => {
  return request({
    url: '/media/pagesize/getPageSizeList',
    method: 'get'
  })
}
