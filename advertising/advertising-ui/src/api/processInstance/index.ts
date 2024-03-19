/*
 * @Author: suny
 * @Date: 2023-10-17 10:53:28
 * @LastEditTime: 2023-10-17 13:35:18
 * @LastEditors: suny
 * @Description:
 */
import request from '@/utils/request'
import { AxiosPromise } from 'axios'

//  流程详情
export function detail(param: any) {
  return request({
    url: '/flow/process-instance/detail',
    method: 'get',
    params: param
  })
}
