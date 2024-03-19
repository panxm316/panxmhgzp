/*
 * @Author: wanghl
 * @Date: 2023-08-16 15:11:03
 * @LastEditTime: 2023-09-06 13:41:51
 * @LastEditors: songly
 * @Description:
 */
import request from '@/utils/request'
import { TMenu, TMenuAction, TCheckActionKey } from '@/types/views/system/menu'
import qs from 'qs'
/**
 * 获取菜单列表
 * @returns
 */
export const getMenuListApi = () => {
  return request({
    url: '/system/menu/getMenuList',
    method: 'get'
  })
}
/**
 * 新增菜单
 * @param data
 * @returns
 */
export const saveMenuApi = (data: TMenu) => {
  return request({
    url: '/system/menu/saveMenu',
    method: 'post',
    data: data
  })
}

/**
 * 修改菜单
 * @param data
 * @returns
 */
export const updateMenuApi = (data: TMenu) => {
  return request({
    url: '/system/menu/updateMenu',
    method: 'post',
    data: data
  })
}

/**
 * 删除菜单
 * @param data
 * @returns
 */
export const deleteMenuByIdApi = (data: { id: string | null | number }) => {
  return request({
    url: '/system/menu/deleteMenuById',
    method: 'post',
    data: qs.stringify(data)
  })
}

/**
 * 获取菜单tree
 * @returns
 */
export const getZTreeObjApi = () => {
  return request({
    url: '/system/menu/getMenuTree',
    method: 'get'
  })
}
// /////////////////////////////////////////////////////////////////////////菜单行为
/**
 *根据菜单获取 菜单按钮信息行为
 * @param data
 * @returns
 */
export const getMenuActionListdApi = (data: string) => {
  return request({
    url: '/system/menuaction/getMenuActionListByMenuId',
    method: 'get',
    params: { menuId: data }
  })
}
/**
 * 保存菜单按钮行为
 * @param data
 * @returns
 */
export const saveMenuActionApi = (data: TMenuAction) => {
  return request({
    url: '/system/menuaction/saveMenuAction',
    method: 'post',
    data: data
  })
}
/**
 * 更新菜单按钮行为
 * @param data
 * @returns
 */
export const updateMenuActionApi = (data: TMenuAction) => {
  return request({
    url: '/system/menuaction/updateMenuAction',
    method: 'post',
    data: data
  })
}
/**
 * 菜单行为删除
 * @param data
 * @returns
 */
export const deleteMenuActionApi = (data: string) => {
  return request({
    url: '/system/menuaction/deleteMenuAction',
    method: 'post',
    data: qs.stringify({ ids: data })
  })
}
/**
 *根据菜单获取 菜单按钮信息行为
 * @param data
 * @returns
 */
export const checkActionKeyCodeOnlyApi = (data: TCheckActionKey) => {
  return request({
    url: '/system/menuaction/checkActionKeyCodeOnly',
    method: 'get',
    params: data
  })
}
