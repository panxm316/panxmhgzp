/*
 * @Date: 2022-08-25 14:13:11
 * @LastEditors: peij
 * @LastEditTime: 2023-10-13 09:59:41
 * @FilePath: /Workflow-Vue3/src/store/index.js
 */
import { defineStore } from 'pinia'
import { FormConfigUserVO, FormVO } from '@/types/views/flow/form'
import { number } from 'echarts'

const adminList: FormConfigUserVO[] = reactive([])
type TStep1 = {
  logo: string
  name: string
  flowId: string
  groupId: string | undefined
  admin: FormConfigUserVO[]
  remark: string
}

export const useFlowStore = defineStore('flow', {
  state: () => {
    return {
      step3: {},
      step1: {
        logo: '',
        name: '',
        flowId: '',
        groupId: undefined,
        admin: adminList,
        remark: ''
      } as TStep1,
      step2: [] as FormVO[]
    }
  },
  actions: {
    setStep3(p: any) {
      this.step3 = p
    },
    setStep2(p: FormVO[]) {
      this.step2 = p
    }
  }
})
