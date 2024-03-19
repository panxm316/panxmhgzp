import { TOperatelogQueryInfo } from '@/types/views/system/operatelog'
import request from '@/utils/request'
/**
 * @description: 系统管理操作日志分页查询
 * @param {TSysoperatelogQueryInfo} data
 * @return {*}
 */
export const getOperatelogPageListApi = (data: TOperatelogQueryInfo) => {
  return request({
    url: '/system/operatelog/getoperatelogPageList',
    method: 'get',
    params: data
  })
}
