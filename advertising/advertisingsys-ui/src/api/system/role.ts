/*
 * @Author: wanghl
 * @Date: 2023-08-16 15:11:03
 * @LastEditTime: 2023-09-12 15:26:56
 * @LastEditors: peij
 * @Description:
 */
import request from '@/utils/request'
import { TRoleActionDTO, TRoleDTO, TRoleMenuModel } from '@/types/views/system/role'
import qs from 'qs'
import { TQueryInfo } from '@/types/common'

/**
 * 查询 角色分页列表
 * @param data
 * @returns
 */
export const getRolePageListApi = (data: TQueryInfo) => {
  return request({
    url: '/system/role/getRolePageList',
    method: 'get',
    params: data
  })
}

/**
 * 保存角色
 * @param data
 * @returns
 */
export const saveRoleApi = async (data: TRoleDTO) => {
  return await request({
    url: '/system/role/saveRole',
    method: 'post',
    data: data
  })
}

/**
 * 更新角色
 * @param data
 * @returns
 */
export const updateRoleApi = async (data: TRoleDTO) => {
  return await request({
    url: '/system/role/updateRole',
    method: 'post',
    data: data
  })
}

/**
 * 删除菜单
 * @param ids
 * @returns
 */
export const deleteRoleIdApi = (ids: string) => {
  return request({
    url: '/system/role/deleteRoleById',
    method: 'post',
    data: qs.stringify({ ids })
  })
}

/**
 * 最大序号
 * @returns
 */
export const getMaxSortApi = () => {
  return request({
    url: '/system/role/getMaxIsort',
    method: 'get'
  })
}
/**
 * 根据角色获取按钮及范围权限
 * @param roleId
 * @returns
 */
export const getRoleActionScopeByRoleIdApi = (roleId: string) => {
  return request({
    url: '/system/role/getRoleActionScopeByRoleId',
    method: 'get',
    params: { roleId }
  })
}

/**
 *  保存角色 菜单行为、范围
 * @param data
 * @returns
 */
export const saveRoleActionApi = (data: TRoleActionDTO) => {
  return request({
    url: '/system/role/saveRoleAction',
    method: 'post',
    data: data
  })
}

/**
 * 角色复制
 */
export const saveCopyRoleApi = (data: { roleId: string; roleName: string }) => {
  return request({
    url: '/system/role/saveCopyRole',
    method: 'post',
    data: qs.stringify(data)
  })
}

/**
 * 根据roleid 获取角色
 * @param roleId
 * @returns
 */
export const getRoleByIdApi = (roleId: string) => {
  return request({
    url: '/system/role/getRoleById',
    method: 'get',
    params: { roleId }
  })
}

/**
 * @description: 角色下拉
 * @return {*}
 */
export const getRoleComboApi = () => {
  return request({
    url: '/system/role/getRoleCombo',
    method: 'get'
  })
}
