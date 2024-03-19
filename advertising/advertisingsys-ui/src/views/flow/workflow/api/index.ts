/*
 * @Date: 2022-08-25 14:06:59
 * @LastEditors: peij
 * @LastEditTime: 2023-10-14 16:43:38
 * @FilePath: /Workflow-Vue3/src/api/index.js
 */

import http from '@/utils/request'
const baseUrl = import.meta.env.BASE_URL

/**
 * 获取角色
 * @param {*} data
 * @returns
 */
export function getRoles(data: any) {
  return http.get(`${baseUrl}roles.json`, {
    params: data
  })
}

/**
 * 获取部门
 * @param {*} data
 * @returns
 */
export function getDepartments(data: any) {
  return http.get(`${baseUrl}departments.json`, {
    params: data
  })
}

/**
 * 获取职员
 * @param {*} data
 * @returns
 */
export function getEmployees(data: any) {
  return http.get(`${baseUrl}employees.json`, {
    params: data
  })
}
/**
 * 获取条件字段
 * @param {*} data
 * @returns
 */
export function getConditions(data: any) {
  return http.get(`${baseUrl}conditions.json`, {
    params: data
  })
}

/**
 * 获取审批数据
 * @param {*} data
 * @returns
 */
export function getWorkFlowData(data: any) {
  return http.get(`${baseUrl}data.json`, {
    params: data
  })
}
/**
 * 设置审批数据
 * @param {*} data
 * @returns
 */
export function setWorkFlowData(data: any) {
  return http.post(`${baseUrl}`, data)
}
