/**
 * ${table.comment!} 前端请求
 *
 * @author ${author}
 * @since ${date}
 */
import request from '@/utils/request'
import { ${entity} } from '@/types/views/${table.name}'
import qs from 'qs'
import type { TQueryInfo } from '@/types/common'

/**
 * 新增${table.comment!}
 * @param {${entity}} 数据对象
 */
export const save${entity}Api = (data: ${entity}) => {
  return request({
    url: '/${table.name}/save',
    method: 'post',
    data: data
  })
}

/**
 * 删除${table.comment!}
 * @param {${entity}} 数据对象
 */
export const delete${entity}Api = (data: ${entity}) => {
  return request({
    url: '/${table.name}/delete',
    method: 'post',
    data: data
  })
}

/**
 * 修改${table.comment!}
 * @param {${entity}} 数据对象
 */
export const update${entity}Api = (data: ${entity}) => {
  return request({
    url: '/${table.name}/update',
    method: 'post',
    data: data
  })
}

/**
 * 获取${table.comment!}
 * @param {${entity}} 数据对象
 */
export const get${entity}Api = (data: ${entity}) => {
  return request({
    url: '/${table.name}/get',
    method: 'get'
  })
}

/**
 * 获取${table.comment!}列表
 * @param {${entity}} 数据对象
 */
export const list${entity}Api = (data: ${entity}) => {
  return request({
    url: '/${table.name}/list',
    method: 'get'
  })
}

/**
 * 获取${table.comment!}分页
 * @param {${entity}} 数据对象
 */
export const get${entity}PageListApi = (data: TQueryInfo) => {
  return request({
    url: '/${table.name}/get${entity}PageList',
    method: 'get',
    params: data
  })
}
