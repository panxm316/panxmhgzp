/*
 * @Author: caogd
 * @Date: 2023-09-12 16:00:44
 * @LastEditTime: 2023-09-12 16:05:28
 * @LastEditors: caogd
 * @Description:系统管理操作日志
 */
import { TSysoperatelogQueryInfo } from '@/types/views/system/sysoperatelog'
import request from '@/utils/request'
/**
 * @description: 系统管理操作日志分页查询
 * @param {TSysoperatelogQueryInfo} data
 * @return {*}
 */
export const getSysoperatelogPageListApi = (data: TSysoperatelogQueryInfo) => {
  return request({
    url: '/system/sysoperatelog/getSysoperatelogPageList',
    method: 'get',
    params: data
  })
}
