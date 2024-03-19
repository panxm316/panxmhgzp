/*
 * @Author: wanghl
 * @Date: 2023-08-16 15:11:03
 * @LastEditTime: 2023-09-18 11:29:09
 * @LastEditors: caogd
 * @Description:
 */
import request from '@/utils/request'
import { TQEmployByRoleVO, TQEmployVO, TSEmployVO } from '@/types/views/system/employ'
import qs from 'qs'
/**
 * @description:员管理，人员列表
 * @param {TQEmployVO} data
 * @return {*}
 */
export const getEmployPageListApi = (data: TQEmployVO) => {
  return request({
    url: '/system/emp/getEmployPageList',
    method: 'get',
    params: data
  })
}
/**
 * @description: 新增人员
 * @param {TSEmployVO} data
 * @return {*}
 */
export const saveEmployApi = (data: TSEmployVO) => {
  return request({
    url: '/system/emp/saveEmploy',
    method: 'post',
    data: data
  })
}

/**
 * @description: 更新人员
 * @param {TSEmployVO} data
 * @return {*}
 */
export const updateEmployApi = (data: TSEmployVO) => {
  return request({
    url: '/system/emp/updateEmploy',
    method: 'post',
    data: data
  })
}

/**
 * @description: 删除人员
 * @param {object} data
 * @return {*}
 */
export const deleteEmpApi = (data: string) => {
  return request({
    url: '/system/emp/deleteEmp',
    method: 'post',
    data: qs.stringify({ id: data })
  })
}
/**
 * 获取人员最大序号
 * @returns
 */
export const getMaxIsortApi = () => {
  return request({
    url: '/system/emp/getMaxIsort',
    method: 'get'
  })
}
/**
 * @description: 批量禁用启用
 * @param {string} data // 人员id，多个用英文逗号分隔
 * @param {boolean} buse
 * @return {*}
 */
export const disableEmpApi = (data: string, buse: boolean) => {
  return request({
    url: '/system/emp/disableEmp',
    method: 'post',
    data: qs.stringify({ ids: data, buse: buse })
  })
}
/**
 * @description: 批量修改添加人员
 * @param {string} empIds
 * @param {string} roleIds
 * @return {*}
 */
export const updateBatchEmpRolesApi = (empIds: string, roleIds: string) => {
  return request({
    url: '/system/emp/updateBatchEmpRoles',
    method: 'post',
    data: qs.stringify({ empIds: empIds, roleIds: roleIds })
  })
}
/**
 * 更新个人头像
 * @param data
 * @returns
 */
export const updateEmployImgApi = (data: string) => {
  return request({
    url: '/system/emp/updateEmployImg',
    method: 'post',
    data: qs.stringify({ imgBase64: data })
  })
}
/**
 * 修改个人密码
 * @param oldPassword
 * @param newPassword
 * @returns
 */
export const updatePassWordApi = (oldPassword: string, newPassword: string) => {
  return request({
    url: '/system/emp/updatePassWord',
    method: 'post',
    data: qs.stringify({ oldPassword: oldPassword, newPassword: newPassword })
  })
}
/**
 * 更新个人信息
 * @param data
 * @returns
 */
export const updatePersonSettingApi = (data: TSEmployVO) => {
  return request({
    url: '/system/emp/updatePersonSetting',
    method: 'post',
    data: data
  })
}
/**
 * 获取个人信息
 * @returns
 */
export const getEmployInfoApi = () => {
  return request({
    url: '/system/emp/getEmployInfo',
    method: 'get'
  })
}
/**
 * @description: 根据id获取人员信息
 * @param {string} data
 * @return {*}
 */
export const getEmployInfoByIdApi = (data: string) => {
  return request({
    url: '/system/emp/getEmployInfoById',
    method: 'get',
    params: { id: data }
  })
}
/**
 * @description: 根据角色获取角色上的人员
 * @param {TQEmployByRoleVO} data
 * @return {*}
 */
export const getEmployPageListByRoleIdApi = (data: TQEmployByRoleVO) => {
  return request({
    url: '/system/emp/getEmployPageListByRoleId',
    method: 'get',
    params: data
  })
}
/**
 * @description: 获取不包含角色上的人员列表
 * @param {TQEmployByRoleVO} data
 * @return {*}
 */
export const getEmployPageListNotByRoleIdApi = (data: TQEmployByRoleVO) => {
  return request({
    url: '/system/emp/getEmployPageListNotByRoleId',
    method: 'get',
    params: data
  })
}
/**
 * @description: 批量添加人员角色
 * @param {string} empIds
 * @param {string} roleId
 * @return {*}
 */
export const saveEmployByRoleApi = (empIds: string, roleId: string) => {
  return request({
    url: '/system/emp/saveEmployByRole',
    method: 'post',
    data: qs.stringify({ empIds: empIds, roleId: roleId })
  })
}
/**
 * @description: 批量删除人员角色关联
 * @param {string} empIds
 * @param {string} roleId
 * @return {*}
 */
export const deleteRoleByEmployApi = (empIds: string, roleId: string) => {
  return request({
    url: '/system/emp/deleteRoleByEmploy',
    method: 'post',
    data: qs.stringify({ empIds: empIds, roleId: roleId })
  })
}
/**
 * @description: 重置角色人员
 * @param {string} roleId
 * @return {*}
 */
export const deleteRoleByIdApi = (roleId: string) => {
  return request({
    url: '/system/emp/deleteRoleById',
    method: 'post',
    data: qs.stringify({ roleId: roleId })
  })
}
