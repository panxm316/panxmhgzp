/*
 * @Date: 2022-08-29 14:00:42
 * @LastEditors: peij
 * @LastEditTime: 2023-10-14 16:36:56
 * @FilePath: /Workflow-Vue3/src/components/dialog/common.js
 */

import { getRoles, getEmployees } from '../../api/index'
import { orgTree, orgTreeSearcheUser } from '@/api/flow/dept'
import $func from '../../utils/index'

export const searchVal = ref('')
export const departments = ref({
  titleDepartments: [],
  childDepartments: [],
  roleList: [],
  employees: []
})
export const roles = ref({})
export const getRoleList = async () => {
  const {
    data: { list }
  } = await getRoles({})
  roles.value = list
}
export const getDepartmentList = async (parentId = 0, type = 'org') => {
  // let { data } = await getDepartments({ parentId })
  const { obj } = await orgTree(type, parentId)
  departments.value = obj
}
export const getDebounceData = (event: string, type = 1) => {
  $func.debounce(async () => {
    if (event) {
      const data = {
        userName: event,
        pageNum: 1,
        pageSize: 30
      }
      if (type === 1) {
        departments.value.childDepartments = []
        const res = await orgTreeSearcheUser(data)
        departments.value.employees = res.obj
      } else {
        const res = await getRoles(data)
        roles.value = res.data.list
      }
    } else {
      type === 1 ? await getDepartmentList() : await getRoleList()
    }
  })()
}
