function All() {}
import { useFlowStore } from '../stores/flow'
import { TProcessNode, TMapString } from '@/types/views/flow/index'

const flowStore = useFlowStore()
const step2FormList = computed(() => {
  const step2 = flowStore.step2
  return step2
})
const formIdObj = computed(() => {
  const obj: TMapString = {}
  for (const item of step2FormList.value) {
    obj[item.id as string] = item
  }
  obj['root'] = {
    name: '发起人',
    type: 'SelectUser'
  }
  return obj
})
All.prototype = {
  timer: '',
  debounce(fn: any, delay = 500) {
    const _this = this
    return function (arg: any) {
      // 获取函数的作用域和变量
      // @ts-ignore
      const that = this
      const args = arg
      clearTimeout(_this.timer) // 清除定时器
      _this.timer = setTimeout(function () {
        fn.call(that, args)
      }, delay)
    }
  },
  setCookie(val: any) {
    // cookie设置[{key:value}]、获取key、清除['key1','key2']
    for (let i = 0, len = val.length; i < len; i++) {
      for (const key in val[i]) {
        document.cookie = key + '=' + encodeURIComponent(val[i][key]) + '; path=/'
      }
    }
  },
  getCookie(name: any) {
    const strCookie = document.cookie
    const arrCookie = strCookie.split('; ')
    for (let i = 0, len = arrCookie.length; i < len; i++) {
      const arr = arrCookie[i].split('=')
      if (name === arr[0]) {
        return decodeURIComponent(arr[1])
      }
    }
  },
  clearCookie(name: any) {
    const myDate = new Date()
    myDate.setTime(-1000) // 设置时间
    for (let i = 0, len = name.length; i < len; i++) {
      document.cookie = '' + name[i] + "=''; path=/; expires=" + myDate.toUTCString()
    }
  },
  arrToStr(arr: any[]) {
    if (arr) {
      return arr
        .map((item) => {
          return item.name
        })
        .toString()
    }
  },
  toggleClass(arr: any[], elem: any, key = 'id') {
    return arr.some((item) => {
      return item[key] === elem[key]
    })
  },
  toChecked(arr: any[], elem: any, key = 'id') {
    const isIncludes = this.toggleClass(arr, elem, key)
    !isIncludes ? arr.push(elem) : this.removeEle(arr, elem, key)
  },
  removeEle(arr: any[], elem: any, key = 'id') {
    let includesIndex: number | undefined
    arr.map((item, index) => {
      if (item[key] === elem[key]) {
        includesIndex = index
      }
    })
    arr.splice(includesIndex as number, 1)
  },
  checkApproval(nodeConfig: TProcessNode) {
    if (nodeConfig?.assignedType === 1 || nodeConfig?.assignedType === 10) {
      // 指定成员
      if (nodeConfig?.nodeUserList?.length === 0) {
        return false
      }
    } else if (nodeConfig?.assignedType === 3) {
      // 指定角色
      if (nodeConfig?.nodeUserList?.length === 0) {
        return false
      }
    } else if (nodeConfig?.assignedType === 8 && nodeConfig?.formUserId?.length === 0) {
      // 表单-用户
      return false
    } else if (nodeConfig?.assignedType === 9 && nodeConfig?.formUserId?.length === 0) {
      // 表单-部门
      return false
    }

    // 审批人为空
    if (
      nodeConfig?.nobody?.handler === 'TO_USER' &&
      nodeConfig?.nobody?.assignedUser?.length === 0
    ) {
      return false
    }
    // 审批人拒绝
    if (nodeConfig?.refuse?.handler === 'TO_NODE' && nodeConfig.refuse.nodeId?.length === 0) {
      return false
    }
    return true
  },
  setApproverStr(nodeConfig: TProcessNode) {
    if (nodeConfig?.assignedType === 1) {
      // 指定成员
      if (nodeConfig.nodeUserList && nodeConfig.nodeUserList.length >= 1) {
        return nodeConfig.nodeUserList.map((res) => res.name).join(',')
      } else {
        return ''
      }
    } else if (nodeConfig?.assignedType === 10) {
      // 指定部门主管
      if (nodeConfig.nodeUserList && nodeConfig?.nodeUserList?.length >= 1) {
        return nodeConfig.nodeUserList.map((res) => res.name).join(',')
      } else {
        return ''
      }
    } else if (nodeConfig?.assignedType === 11) {
      // 系统自动拒绝

      return '系统自动拒绝'
    } else if (nodeConfig?.assignedType === 3) {
      // 指定角色
      if (nodeConfig.nodeUserList && nodeConfig.nodeUserList.length >= 1) {
        return nodeConfig.nodeUserList.map((res) => res.name).join(',')
      } else {
        return ''
      }
    } else if (nodeConfig?.assignedType === 2) {
      return nodeConfig.deptLeaderLevel === 1
        ? '直接部门主管'
        : '第' + nodeConfig.deptLeaderLevel + '级部门主管'
    } else if (nodeConfig?.assignedType === 4) {
      // 发起人自选
      return '发起人自选'
    } else if (nodeConfig?.assignedType === 5) {
      return '发起人自己'
    } else if (nodeConfig?.assignedType === 7) {
      return '到第' + nodeConfig.deptLeaderLevel + '级部门主管终止'
    } else if (
      nodeConfig?.assignedType === 8 &&
      nodeConfig.formUserId &&
      nodeConfig.formUserId.length > 0
    ) {
      // 表单-人员
      return '表单人员：' + nodeConfig.formUserName
    } else if (
      nodeConfig?.assignedType === 9 &&
      nodeConfig.formUserId &&
      nodeConfig.formUserId.length > 0
    ) {
      const deptUserType = nodeConfig.deptUserType
      let deptUserTypeShow = '主管'
      if (deptUserType === 'allUser') {
        deptUserTypeShow = '所有人员'
      }

      // 表单-部门
      return '表单部门：' + nodeConfig.formUserName + ' 的' + deptUserTypeShow
    }
    return ''
  },
  dealStr(str: string, obj: any) {
    const arr: string[] = []
    const list = str.split(',')
    for (const elem in obj) {
      list.map((item) => {
        if (item === elem) {
          arr.push(obj[elem].value)
        }
      })
    }
    return arr.join('或')
  },
  conditionStr(nodeConfig: TProcessNode, index: number) {
    const { conditionList, groupMode } = (nodeConfig?.conditionNodes ?? [])[index] ?? {}
    // const { conditionList, groupMode } = nodeConfig?.conditionNodes[index] as Required<TProcessNode>
    if (conditionList?.length === 0) {
      return index === (nodeConfig?.conditionNodes && nodeConfig.conditionNodes.length - 1)
        ? '其他条件进入此流程'
        : '请设置条件'
    } else {
      const groupConArr = []
      if (conditionList) {
        for (const groupCondition of conditionList) {
          const mode = groupCondition.mode
          const conArr = []
          if (groupCondition.conditionList) {
            for (const con of groupCondition.conditionList) {
              const { key, expression, value } = con
              if (!key) {
                continue
              }
              const valueElement = formIdObj.value[key]
              const name = valueElement.name
              let valueShow = value as any
              if (
                valueElement.type === 'SelectUser' ||
                valueElement.type === 'SelectDept' ||
                valueElement.type === 'SelectMultiUser' ||
                valueElement.type === 'SelectMultiDept'
              ) {
                valueShow = value?.map((res: any) => res.name).join(',')
              } else if (valueElement.type === 'SingleSelect') {
                valueShow = value?.map((res: any) => res.value).join(',')
              } else {
                if (!valueShow) {
                  valueShow = '?'
                }
              }
              if (expression === '==') {
                conArr.push(name + ' 等于 ' + valueShow)
              } else if (expression === 'in') {
                conArr.push(name + ' 属于 ' + valueShow)
              } else if (expression === 'notin') {
                conArr.push(name + ' 不属于 ' + valueShow)
              } else if (expression === '!=') {
                conArr.push(name + ' 不等于 ' + valueShow)
              } else if (expression === '>') {
                conArr.push(name + ' 大于 ' + valueShow)
              } else if (expression === '>=') {
                conArr.push(name + ' 大于等于 ' + valueShow)
              } else if (expression === '<') {
                conArr.push(name + ' 小于 ' + valueShow)
              } else if (expression === '<=') {
                conArr.push(name + ' 小于等于 ' + valueShow)
              } else if (expression === 'contain') {
                conArr.push(name + ' 包含 ' + valueShow)
              } else if (expression === 'notcontain') {
                conArr.push(name + ' 不包含 ' + valueShow)
              }
            }
          }

          if (mode) {
            const s = conArr.join(' 且 ')
            if (conArr.length > 0) {
              groupConArr.push('(' + s + ')')
            }
          } else {
            const s = conArr.join(' 或 ')
            if (conArr.length > 0) {
              groupConArr.push('(' + s + ')')
            }
          }
        }
      }
      // prettier-ignore
      return groupConArr.length > 0 ? groupConArr.join(groupMode ? ' 且 ' : ' 或 ') : index === (nodeConfig?.conditionNodes ?? []).length - 1 ? '默认条件' : '请设置条件'
    }
  },
  copyerStr(nodeConfig: TProcessNode) {
    if (nodeConfig?.nodeUserList?.length !== 0) {
      return this.arrToStr(nodeConfig?.nodeUserList)
    }
  }
}
// @ts-ignore
export default new All()
