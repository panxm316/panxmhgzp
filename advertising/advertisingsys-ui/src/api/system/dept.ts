/*
 * @Author: wanghl suny
 * @Date: 2023-08-16 15:11:03
 * @LastEditTime: 2023-09-02 12:53:43
 * @LastEditors: suny
 * @Description:
 */
import request from '@/utils/request'
import { TbatchDeptData, TsystemDept } from '@/types/views/system/dept'
import qs from 'qs'
// 部门管理查询所有部门树
export const getSysDeptTreeApi = () => {
  return request({
    url: '/system/dept/getSysDeptTree',
    method: 'get'
  })
}
/**
 * 新增部门
 * @param data
 * @returns
 */
export function saveDeptApi(data: TsystemDept) {
  return request({
    url: '/system/dept/saveDept',
    method: 'post',
    data: data
  })
}

/**
 * 更新部门
 * @param data
 * @returns
 */
export function updateDeptApi(data: TsystemDept) {
  return request({
    url: '/system/dept/updateDept',
    method: 'post',
    data: data
  })
}

/**
 * 删除部门
 * @param data
 * @returns
 */
export const deleteDeptApi = (data: { ids: string }) => {
  return request({
    url: '/system/dept/deleteDept',
    method: 'post',
    data: qs.stringify(data)
  })
}
/**
 * 获取部门信息
 * @param data
 * @returns
 */
export const getDeptInfoByIdApi = (data: { id: string }) => {
  return request({
    url: '/system/dept/getDeptInfoById',
    method: 'get',
    params: data
  })
}

/**
 * 获取媒体最大isort值
 * @returns
 */
export const getMaxIsortApi = () => {
  return request({
    url: '/system/dept/getMaxIsort',
    method: 'get'
  })
}

/**
 * 获取父部门的buse
 * @returns
 */
export const getParentDeptBuseApi = (data: { id: string }) => {
  return request({
    url: '/system/dept/getParentDeptBuse',
    method: 'get',
    params: data
  })
}
/**
 * 批量调整部门
 * @param data
 * @returns
 */
export const batchUdpateDeptApi = (data: TbatchDeptData) => {
  return request({
    url: '/system/dept/batchUdpateDept',
    method: 'post',
    data: qs.stringify(data)
  })
}
