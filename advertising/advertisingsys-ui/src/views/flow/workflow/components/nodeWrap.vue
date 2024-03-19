<template>
  <div>
    <!--			<approval :node-config="nodeConfig" v-if="nodeConfig.type==1"></approval>-->
    <div v-if="nodeConfig.type < 3" class="node-wrap">
      <div
        class="node-wrap-box"
        :class="
          (nodeConfig.type == 0 ? 'start-node ' : '') + (nodeConfig.error ? 'active error' : '')
        "
      >
        <div class="title" :style="`background: rgb(${bgColors[nodeConfig.type]});`">
          <span v-if="nodeConfig.type == 0">{{ nodeConfig.nodeName }}</span>
          <template v-else>
            <span class="iconfont">{{ nodeConfig.type == 1 ? '' : '' }}</span>
            <input
              v-if="isInput"
              v-model="nodeConfig.nodeName"
              v-focus
              type="text"
              class="ant-input editable-title-input"
              :placeholder="defaultText"
              @blur="blurEvent()"
              @focus="$event.currentTarget.select()"
            />
            <span v-else class="editable-title" @click="clickEvent()">{{
              nodeConfig.nodeName
            }}</span>
            <i class="anticon anticon-close close" @click="delNode"></i>
          </template>
        </div>
        <div class="content" @click="openConfigDrawer">
          <div class="text">
            {{ placeHolder?.length > 0 ? placeHolder : '请选择' + defaultText }}
          </div>
          <i class="anticon anticon-right arrow"></i>
        </div>
        <div v-if="nodeConfig.error" class="error_tip">
          <i class="anticon anticon-exclamation-circle"></i>
        </div>
      </div>
      <addNode v-model:childNodeP="nodeConfig.childNode" :current-node="nodeConfig" />
    </div>
    <div v-if="nodeConfig.type == 4" class="branch-wrap">
      <div class="branch-box-wrap">
        <div class="branch-box">
          <button class="add-branch" @click="addTerm">添加条件</button>
          <div v-for="(item, index) in nodeConfig.conditionNodes" :key="index" class="col-box">
            <div class="condition-node">
              <div class="condition-node-box">
                <div class="auto-judge" :class="item.error ? 'error active' : ''">
                  <div
                    v-if="index != 0 && index < nodeConfig.conditionNodes.length - 1"
                    class="sort-left"
                    @click="arrTransfer(index, -1)"
                  >
                    &lt;
                  </div>
                  <div class="title-wrapper">
                    <input
                      v-if="isInputList[index]"
                      v-model="item.nodeName"
                      v-focus
                      style="width: 50%"
                      type="text"
                      class="ant-input editable-title-input"
                      @blur="blurEvent(index)"
                      @focus="$event.currentTarget.select()"
                    />
                    <span v-else class="editable-title" @click="clickEvent(index)">{{
                      item.nodeName
                    }}</span>
                    <span
                      class="priority-title"
                      @click="
                        index < nodeConfig.conditionNodes.length - 1 &&
                          openConfigDrawer(item.priorityLevel)
                      "
                      >优先级{{ item.priorityLevel }}</span
                    >
                    <i
                      v-if="index < nodeConfig.conditionNodes.length - 1"
                      class="anticon anticon-close close"
                      @click="delTerm(index)"
                    ></i>
                  </div>
                  <div
                    v-if="
                      index != nodeConfig.conditionNodes.length - 1 &&
                      index != nodeConfig.conditionNodes.length - 2
                    "
                    class="sort-right"
                    @click="arrTransfer(index)"
                  >
                    &gt;
                  </div>

                  <el-tooltip
                    effect="dark"
                    :content="$func.conditionStr(nodeConfig, index)"
                    placement="left"
                  >
                    <div
                      v-if="index < nodeConfig.conditionNodes.length - 1"
                      class="content"
                      @click="openConfigDrawer(item.priorityLevel)"
                    >
                      {{ $func.conditionStr(nodeConfig, index) }}
                    </div>
                    <div v-else class="content">{{ $func.conditionStr(nodeConfig, index) }}</div>
                  </el-tooltip>

                  <div v-if="item.error" class="error_tip">
                    <i class="anticon anticon-exclamation-circle"></i>
                  </div>
                </div>
                <addNode v-model:childNodeP="item.childNode" :current-node="item" />
              </div>
            </div>
            <nodeWrap v-if="item.childNode" v-model:nodeConfig="item.childNode" />
            <template v-if="index == 0">
              <div class="top-left-cover-line"></div>
              <div class="bottom-left-cover-line"></div>
            </template>
            <template v-if="index == nodeConfig.conditionNodes.length - 1">
              <div class="top-right-cover-line"></div>
              <div class="bottom-right-cover-line"></div>
            </template>
          </div>
        </div>
        <addNode v-model:childNodeP="nodeConfig.childNode" :current-node="nodeConfig" />
      </div>
    </div>
    <div v-if="nodeConfig.type == 5" class="branch-wrap">
      <div class="branch-box-wrap">
        <div class="branch-box">
          <button class="add-branch" @click="addTerm">添加分支</button>
          <div v-for="(item, index) in nodeConfig.conditionNodes" :key="index" class="col-box">
            <div class="condition-node">
              <div class="condition-node-box">
                <div class="auto-judge" :class="item.error ? 'error active' : ''">
                  <div v-if="index != 0" class="sort-left" @click="arrTransfer(index, -1)">
                    &lt;
                  </div>
                  <div class="title-wrapper">
                    <input
                      v-if="isInputList[index]"
                      v-model="item.nodeName"
                      v-focus
                      style="width: 50%"
                      type="text"
                      class="ant-input editable-title-input"
                      @blur="blurEvent(index)"
                      @focus="$event.currentTarget.select()"
                    />
                    <span v-else class="editable-title" @click="clickEvent(index)">{{
                      item.nodeName
                    }}</span>

                    <!--										<span class="priority-title" @click="openConfigDrawer(item.priorityLevel)">优先级{{-->
                    <!--												item.priorityLevel-->
                    <!--											}}</span>-->
                    <i class="anticon anticon-close close" @click="delTerm(index)"></i>
                  </div>
                  <div
                    v-if="index != nodeConfig.conditionNodes.length - 1"
                    class="sort-right"
                    @click="arrTransfer(index)"
                  >
                    &gt;
                  </div>

                  <div class="content">{{ item.placeHolder }}</div>
                  <div v-if="item.error" class="error_tip">
                    <i class="anticon anticon-exclamation-circle"></i>
                  </div>
                </div>
                <addNode v-model:childNodeP="item.childNode" :current-node="item" />
              </div>
            </div>
            <nodeWrap v-if="item.childNode" v-model:nodeConfig="item.childNode" />
            <template v-if="index == 0">
              <div class="top-left-cover-line"></div>
              <div class="bottom-left-cover-line"></div>
            </template>
            <template v-if="index == nodeConfig.conditionNodes.length - 1">
              <div class="top-right-cover-line"></div>
              <div class="bottom-right-cover-line"></div>
            </template>
          </div>
        </div>
        <addNode v-model:childNodeP="nodeConfig.childNode" :current-node="nodeConfig" />
      </div>
    </div>
    <nodeWrap v-if="nodeConfig.childNode" v-model:nodeConfig="nodeConfig.childNode" />
  </div>
</template>
<script setup>
import addNode from './addNode.vue'
import Approval from './node/approval.vue'

import { onMounted, ref, watch, getCurrentInstance, computed } from 'vue'
import $func from '../utils/index'
import { useStore } from '../stores/index'
import { bgColors, placeholderList } from '../utils/const'
import { isBlank, isNotBlank, getRandomId } from '@/utils/index'

const _uid = getCurrentInstance().uid

const props = defineProps({
  nodeConfig: {
    type: Object,
    default: () => ({})
  }
})

const defaultText = computed(() => {
  return placeholderList[props.nodeConfig.type]
})

var placeHolder = computed(() => {
  if (props.nodeConfig.type === 0) {
    return $func.arrToStr(props.nodeConfig.nodeUserList) || '所有人'
  }
  if (props.nodeConfig.type === 1) {
    return $func.setApproverStr(props.nodeConfig)
  }
  if (props.nodeConfig.type === 2) {
    return $func.copyerStr(props.nodeConfig)
  }
  return ''
})

watch(placeHolder, (value, oldValue, onCleanup) => {
  props.nodeConfig.placeHolder = value
})

import { useFlowStore } from '../stores/flow'

const flowStore = useFlowStore()
const step2FormList = computed(() => {
  const step2 = flowStore.step2

  return step2
})

watch(
  () => step2FormList.value,
  (val) => {
    const nodeConfig = props.nodeConfig

    if (nodeConfig.type === 1) {
      // 审批人

      if (nodeConfig.assignedType === 8) {
        // 表单人员
        const formUserId = nodeConfig.formUserId
        const length = val.filter((res) => res.id === formUserId).length
        if (length === 0) {
          nodeConfig.formUserId = ''
          nodeConfig.formUserName = ''
          nodeConfig.error = true
        }
      }
    }
    if (nodeConfig.type === 4) {
      // 条件分支
      var index = 0
      var len = nodeConfig.conditionNodes.length
      for (var node of nodeConfig.conditionNodes) {
        if (index >= len - 1) {
          break
        }
        console.log('---conditionList', node.conditionList)
        for (var item1 of node.conditionList) {
          for (var item2 of item1.conditionList) {
            const length = val.filter((res) => res.id === item2.key).length
            if (length === 0) {
              item2.key = ''
              item2.expression = ''
              item2.keyType = ''
              item2.value = ''
              node.error = true
            }
          }
        }
        index++
      }
    }
  }
)
const isInputList = ref([])
const isInput = ref(false)
const resetConditionNodesErr = () => {
  if (props.nodeConfig.type === 5) {
    return
  }
  for (var i = 0; i < props.nodeConfig.conditionNodes.length; i++) {
    const conditionNode = props.nodeConfig.conditionNodes[i]

    conditionNode.error = false
    const conditionList = conditionNode.conditionList
    if (i !== props.nodeConfig.conditionNodes.length - 1) {
      var error = conditionList.length === 0

      for (var it of conditionList) {
        error = it.conditionList.length === 0
        for (var ite of it.conditionList) {
          const ele = step2FormList.value.filter((res) => res.id === ite.key) ?? []
          if (
            isBlank(ite.key) ||
            (isBlank(ite.expression) && ele[0].type !== 'ConditionRelation') ||
            isBlank(ite.value)
          ) {
            error = true
            break
          }
          if (ele[0].type === 'ConditionRelation') {
            for (const v of ite.value) {
              if (isBlank(v.key) || isBlank(v.expression) || isBlank(v.value)) {
                error = true
                break
              }
            }
          }
        }
      }
      conditionNode.error = error
    }
  }
}
onMounted(() => {
  if (props.nodeConfig.type === 1) {
    props.nodeConfig.error = !$func.checkApproval(props.nodeConfig)
  } else if (props.nodeConfig.type === 2) {
    props.nodeConfig.error = !$func.copyerStr(props.nodeConfig)
  } else if (props.nodeConfig.type === 4) {
    resetConditionNodesErr()
  }
})
const emits = defineEmits(['update:nodeConfig'])
const store = useStore()
const {
  setPromoter,
  setApprover,
  setCopyer,
  setCondition,
  setStarterConfig,
  setApproverConfig,
  setCopyerConfig,
  setConditionsConfig
} = store
const starterConfigData = computed(() => store.starterConfigData)
const approverConfigData = computed(() => store.approverConfigData)
const copyerConfig1 = computed(() => store.copyerConfig1)
const conditionsConfig1 = computed(() => store.conditionsConfig1)

watch(starterConfigData, (approver) => {
  if (approver.flag && approver.id === _uid) {
    emits('update:nodeConfig', approver.value)
  }
})
watch(approverConfigData, (approver) => {
  if (approver.flag && approver.id === _uid) {
    emits('update:nodeConfig', approver.value)
  }
})
watch(copyerConfig1, (copyer) => {
  if (copyer.flag && copyer.id === _uid) {
    emits('update:nodeConfig', copyer.value)
  }
})
watch(conditionsConfig1, (condition) => {
  if (condition.flag && condition.id === _uid) {
    console.log(condition.value)
    emits('update:nodeConfig', condition.value)
  }
})

const clickEvent = (index) => {
  if (index || index === 0) {
    isInputList.value[index] = true
  } else {
    isInput.value = true
  }
}
const blurEvent = (index) => {
  if (index || index === 0) {
    isInputList.value[index] = false
    props.nodeConfig.conditionNodes[index].nodeName =
      props.nodeConfig.conditionNodes[index].nodeName || '条件'
  } else {
    isInput.value = false
    props.nodeConfig.nodeName = props.nodeConfig.nodeName || defaultText
  }
}
const { proxy } = getCurrentInstance()

const delNode = () => {
  emits('update:nodeConfig', props.nodeConfig.childNode)
}
const addTerm = () => {
  if (props.nodeConfig.type === 5) {
    const len = props.nodeConfig.conditionNodes.length + 1
    props.nodeConfig.conditionNodes.push({
      nodeName: '分支' + len,
      type: 3,
      id: getRandomId(),
      placeHolder: '满足条件',
      parentId: props.nodeConfig.id,
      groupMode: true,
      priorityLevel: len,
      conditionList: [
        {
          conditionList: []
        }
      ],
      nodeUserList: [],
      childNode: null
    })
    emits('update:nodeConfig', props.nodeConfig)

    return
  }

  const len = props.nodeConfig.conditionNodes.length
  console.log(len)
  props.nodeConfig.conditionNodes.splice(len - 1, 0, {
    nodeName: '条件' + len,
    type: 3,
    parentId: props.nodeConfig.id,
    groupMode: true,
    id: getRandomId(),
    priorityLevel: len,
    conditionList: [
      {
        mode: true,
        conditionList: [
          {
            key: '',
            expression: ''
          }
        ]
      }
    ],
    nodeUserList: [],
    childNode: null
  })

  const newlen = props.nodeConfig.conditionNodes.length
  props.nodeConfig.conditionNodes[newlen - 1].priorityLevel = newlen
  resetConditionNodesErr()
  emits('update:nodeConfig', props.nodeConfig)
}
const delTerm = (index) => {
  props.nodeConfig.conditionNodes.splice(index, 1)
  props.nodeConfig.conditionNodes.map((item, index) => {
    item.priorityLevel = index + 1
    item.nodeName = `条件${index + 1}`
  })
  resetConditionNodesErr()
  emits('update:nodeConfig', props.nodeConfig)
  if (props.nodeConfig.conditionNodes.length === 1) {
    if (props.nodeConfig.childNode) {
      if (props.nodeConfig.conditionNodes[0].childNode) {
        reData(props.nodeConfig.conditionNodes[0].childNode, props.nodeConfig.childNode)
      } else {
        props.nodeConfig.conditionNodes[0].childNode = props.nodeConfig.childNode
      }
    }
    emits('update:nodeConfig', props.nodeConfig.conditionNodes[0].childNode)
  }
}
const reData = (data, addData) => {
  if (!data.childNode) {
    data.childNode = addData
  } else {
    reData(data.childNode, addData)
  }
}
const openConfigDrawer = (priorityLevel) => {
  var { type } = props.nodeConfig
  // 发起人默认所有人不进行操作
  if (type === 0) {
    // setPromoter(true)
    // setStarterConfig({
    //   value: JSON.parse(JSON.stringify(props.nodeConfig)),
    //   flag: false,
    //   id: _uid
    // })
  } else if (type === 1) {
    setApprover(true)
    setApproverConfig({
      value: {
        ...JSON.parse(JSON.stringify(props.nodeConfig)),
        ...{ assignedType: props.nodeConfig.assignedType ? props.nodeConfig.assignedType : 1 },
        ...{ multiple: false }
      },
      flag: false,
      id: _uid
    })
  } else if (type === 2) {
    setCopyer(true)
    setCopyerConfig({
      value: JSON.parse(JSON.stringify(props.nodeConfig)),
      flag: false,
      id: _uid
    })
  } else {
    setCondition(true)
    setConditionsConfig({
      value: JSON.parse(JSON.stringify(props.nodeConfig)),
      priorityLevel,
      flag: false,
      id: _uid
    })
  }
}
const arrTransfer = (index, type = 1) => {
  // 向左-1,向右1
  props.nodeConfig.conditionNodes[index] = props.nodeConfig.conditionNodes.splice(
    index + type,
    1,
    props.nodeConfig.conditionNodes[index]
  )[0]
  props.nodeConfig.conditionNodes.map((item, index) => {
    item.priorityLevel = index + 1
  })
  resetConditionNodesErr()
  emits('update:nodeConfig', props.nodeConfig)
}
</script>
<style scoped>
@import '@/views/flow/workflow/css/workflow.css';

.error_tip {
  position: absolute;
  top: 0px;
  right: 0px;
  transform: translate(150%, 0px);
  font-size: 24px;
}

.promoter_person .el-dialog__body {
  padding: 10px 20px 14px 20px;
}

.selected_list {
  margin-bottom: 20px;
  line-height: 30px;
}

.selected_list span {
  margin-right: 10px;
  padding: 3px 6px 3px 9px;
  line-height: 12px;
  white-space: nowrap;
  border-radius: 2px;
  border: 1px solid rgba(220, 220, 220, 1);
}

.selected_list img {
  margin-left: 5px;
  width: 7px;
  height: 7px;
  cursor: pointer;
}
</style>
