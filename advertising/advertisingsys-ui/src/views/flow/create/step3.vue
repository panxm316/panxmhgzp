<!--
 * @Author: caogd
 * @Date: 2023-10-12 14:05:00
 * @LastEditTime: 2023-10-20 12:59:44
 * @LastEditors: peij
 * @Description: 流程设计器
-->
<template>
  <div class="app-container">
    <div class="fd-nav-content">
      <section class="dingflow-design left_box" style="overflow: auto">
        <div class="zoom">
          <div class="zoom-out" :class="nowVal == 50 && 'disabled'" @click="zoomSize(1)"></div>
          <span>{{ nowVal }}%</span>
          <div class="zoom-in" :class="nowVal == 300 && 'disabled'" @click="zoomSize(2)"></div>
        </div>
        <div class="box-scale" :style="`transform: scale(${nowVal / 100});`">
          <nodeWrap v-model:nodeConfig="nodeConfig" />
          <div class="end-node">
            <div class="end-node-circle"></div>
            <div class="end-node-text">流程结束</div>
          </div>
        </div>
      </section>
    </div>
    <promoterDrawer />
    <approverDrawer />
    <copyerDrawer />
    <conditionDrawer />
  </div>
</template>

<script lang="ts" setup>
import nodeWrap from '../workflow/components/nodeWrap.vue'
import promoterDrawer from '../workflow/components/drawer/promoterDrawer.vue'
import approverDrawer from '../workflow/components/drawer/approverDrawer.vue'
import copyerDrawer from '../workflow/components/drawer/copyerDrawer.vue'
import conditionDrawer from '../workflow/components/drawer/conditionDrawer.vue'
import { useFlowStore } from '../workflow/stores/flow'
import { deepCopy } from '@/utils/index'
import { TProcessNode } from '@/types/views/flow/index'
const store = useFlowStore()
const tipList = ref<string[]>([])
const nowVal = ref(100)
/** 树高度 */
const treeheight = ref('')
/** 流程初始化数据 */
const processData: TProcessNode = {
  nodeName: '发起人',
  type: 0,
  id: 'root',
  formPerms: {},
  nodeUserList: [],
  childNode: {} as TProcessNode
}
const nodeConfig = ref(deepCopy(processData))

const props = withDefaults(defineProps<{ nodeConfigObj: TProcessNode }>(), {
  nodeConfigObj: () => {
    return {
      nodeName: '发起人',
      type: 0,
      id: 'root',
      formPerms: {},
      nodeUserList: [],
      childNode: {} as TProcessNode
    }
  }
})
/** 监听props流程初始设置 */
watch(
  () => props.nodeConfigObj,
  (val) => {
    nodeConfig.value = val
  }
)
/** 监听节点变化 */
watch(
  () => nodeConfig.value,
  (v) => {
    store.setStep3(v)
  },
  { deep: true }
)
onMounted(() => {
  treeheight.value = window.innerHeight - 150 + 'px'
})
const reErr = (childNode: TProcessNode) => {
  if (childNode) {
    const { type, error, nodeName, conditionNodes } = childNode
    if (type === 1 || type === 2) {
      if (error) {
        tipList.value.push(nodeName + ' 未设置人员')
      }
      reErr(childNode)
    } else if (type === 3) {
      reErr(childNode)
    } else if (type === 4) {
      reErr(childNode)
      if (conditionNodes) {
        for (let i = 0; i < conditionNodes?.length; i++) {
          if (conditionNodes[i]?.error) {
            tipList.value.push('请设置' + conditionNodes[i]?.nodeName + '的条件')
          }
          reErr(conditionNodes[i])
        }
      }
    }
  } else {
    childNode = null
  }
}

/**
 * 流程初始化
 */
const initProcessData = () => {
  nodeConfig.value = processData
}
/**
 * 获取流程数据
 */
const getProcessData = async () => {
  return nodeConfig.value
}
const zoomSize = (type: number) => {
  if (type === 1) {
    if (nowVal.value === 50) {
      return
    }
    nowVal.value -= 10
  } else {
    if (nowVal.value === 300) {
      return
    }
    nowVal.value += 10
  }
}
const validate = (f: (valid: boolean, arr?: string[]) => void) => {
  tipList.value = []

  if (nodeConfig.value.childNode === undefined || nodeConfig.value.childNode.id === undefined) {
    tipList.value = ['请完善流程节点']
  }
  reErr(nodeConfig.value)
  if (tipList.value.length !== 0) {
    f(false, tipList.value)
    return
  }
  f(true)
}
defineExpose({ validate, getProcessData, initProcessData })
</script>
<style scoped>
@import '../workflow/css/workflow.css';

.error-modal-list {
  width: 455px;
}
.left_box {
  height: v-bind(treeheight);
}
</style>
