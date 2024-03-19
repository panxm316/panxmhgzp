/*
 * @Date: 2022-08-25 14:13:11
 * @LastEditors: peij
 * @LastEditTime: 2023-10-14 16:33:55
 * @FilePath: /Workflow-Vue3/src/store/index.js
 */
import { defineStore } from 'pinia'

export const useStore = defineStore('store', {
  state: () => ({
    promoterDrawer: false,

    approverDrawer: false,
    approverConfigData: {},
    starterConfigData: {},
    copyerDrawer: false,
    copyerConfig1: {},
    conditionDrawer: false,
    conditionsConfig1: {
      conditionNodes: []
    }
  }),
  actions: {
    setPromoter(payload: any) {
      this.promoterDrawer = payload
    },

    setApprover(payload: any) {
      this.approverDrawer = payload
    },
    setApproverConfig(payload: any) {
      this.approverConfigData = payload
    },
    setStarterConfig(payload: any) {
      this.starterConfigData = payload
    },
    setCopyer(payload: any) {
      this.copyerDrawer = payload
    },
    setCopyerConfig(payload: any) {
      this.copyerConfig1 = payload
    },
    setCondition(payload: any) {
      this.conditionDrawer = payload
    },
    setConditionsConfig(payload: any) {
      this.conditionsConfig1 = payload
    }
  }
})
