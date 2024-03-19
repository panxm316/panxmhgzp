<template>
  <el-drawer
    v-model="visible"
    :append-to-body="true"
    title="条件设置"
    :show-close="false"
    :size="550"
    :before-close="saveCondition"
    @open="openEvent"
  >
    <template #header="{ titleId, titleClass }">
      <h3 :id="titleId" :class="titleClass">条件设置</h3>
    </template>
    <el-form label-width="120px">
      <el-form-item label="条件组关系">
        <el-switch v-model="conditionConfig.groupMode" active-text="且" inactive-text="或" />
      </el-form-item>
    </el-form>

    <el-card
      v-for="(item, index) in conditionConfig.conditionList"
      :key="index"
      class="box-card"
      style="margin-bottom: 20px"
    >
      <template #header>
        <div class="card-header">
          <span>条件组{{ index + 1 }}</span>
          <el-switch v-model="item.mode" active-text="且" inactive-text="或" />
          <el-button
            v-if="conditionConfig.conditionList.length > 1"
            text
            :icon="'Delete'"
            @click="deleteGroup(index)"
          ></el-button>
        </div>
      </template>
      <div v-for="(item1, index1) in item.conditionList" :key="index1">
        <div style="display: flex; flex-direction: row; justify-content: space-between">
          <div>
            {{ index1 == 0 ? '当' : item.mode ? '且' : '或' }}
          </div>
          <div>
            <el-button
              v-if="item.conditionList.length > 1"
              text
              :icon="'Delete'"
              @click="deleteCondition(index, index1)"
            ></el-button>
          </div>
        </div>
        <condition :condition="item1"></condition>
      </div>

      <el-button dark type="success" style="margin-top: 20px" @click="addOneCondition(item, index)"
        >添加条件</el-button
      >
    </el-card>
    <el-button dark type="primary" @click="addOneConditionGroup">添加条件组</el-button>
  </el-drawer>
</template>
<script setup lang="ts">
import { ref, watch, computed, getCurrentInstance } from 'vue'
import $func from '../../utils/index'
import { useStore } from '../../stores/index'
import Condition from './components/condition.vue'
import { useFlowStore } from '../../stores/flow'
import { isBlank } from '@/utils/index'
import { TFlowConditionEx } from '@/types/views/flow/flowcondition'
import { IAxios } from 'axios'
import { getFlowConditionListApi } from '@/api/flow/flowcondition'
import { getMediaDataComboApi } from '@/api/media/media'
import { IDataCombo } from '@/types/common/DataCombo'

const conditionsConfig = ref({
  conditionNodes: []
})
const conditionConfig = ref<any>({})
const PriorityLevel = ref(0)

const store = useStore()
const { setCondition, setConditionsConfig } = store
const conditionsConfig1: any = computed(() => store.conditionsConfig1)
const conditionDrawer = computed(() => store.conditionDrawer)
const visible = computed({
  get() {
    return conditionDrawer.value
  },
  set() {
    closeDrawer()
  }
})
// 删除条件组
const deleteGroup = (index: number) => {
  conditionConfig.value?.conditionList.splice(index, 1)
}
// 刪除单个条件
const deleteCondition = (index: number, index1: number) => {
  conditionConfig.value?.conditionList[index].conditionList.splice(index1, 1)
}

// 添加一个条件组
const addOneConditionGroup = () => {
  conditionConfig.value?.conditionList.push({
    mode: true,
    conditionList: [{}]
  })
}
// 添加组内一个条件
const addOneCondition = (item: any, index: number) => {
  let conditionList = item.conditionList
  if (!conditionList) {
    conditionList = []
  }
  conditionList.push({})
  item.conditionList = conditionList
}
const flowStore = useFlowStore()

const step2FormList = computed(() => {
  const step2 = flowStore.step2

  return step2
})
/**
 * 策划展开时
 */
const openEvent = async () => {
  const groupId = flowStore.step1.groupId ?? 'flowcustomer'

  getFlowConditionListApi(groupId).then(async ({ obj }: IAxios<TFlowConditionEx[]>) => {
    console.log(obj)
    const dataList: any[] = []
    obj.forEach((item) => {
      const optionsdata = item.data?.map((v) => {
        return { key: v.id, value: v.name }
      })
      const data = {
        id: item.sconditionkey, // 条件的key值不可重复
        name: item.sname,
        type: item.sconditiontype, // 组件类型
        props: { value: item.sconditionkey, options: optionsdata }
      }
      dataList.push(data)
    })
    flowStore.setStep2(dataList)
  })
}
watch(conditionsConfig1, (val: any) => {
  const conditionNodes = val.value.conditionNodes

  for (var item of conditionNodes) {
    const groupList = item.conditionList
    for (var group of groupList) {
      const conditionList = group.conditionList
      for (var con of conditionList) {
        const key = con.key
        if (key === 'root') {
          con.keyType = 'SelectUser'
        } else {
          const ele = step2FormList.value.filter((res) => res.id === key)
          if (ele.length > 0) {
            con.keyType = ele[0].type
            // 如果是复杂的组类型也需要吧value里的type加上
            if (ele[0].type === 'ConditionRelation' && con.value) {
              for (const v of con.value) {
                const vele = step2FormList.value.filter((res) => res.id === v.key)
                if (vele.length > 0) {
                  v.keyType = vele[0].type
                }
              }
            }
          }
        }
      }
    }
  }
  conditionsConfig.value = val.value
  PriorityLevel.value = val.priorityLevel
  conditionConfig.value = val.priorityLevel
    ? conditionsConfig.value.conditionNodes[val.priorityLevel - 1]
    : { nodeUserList: [], conditionList: [] }
})
const saveCondition = () => {
  closeDrawer()
  var a = conditionsConfig.value.conditionNodes.splice(PriorityLevel.value - 1, 1) // 截取旧下标
  conditionsConfig.value.conditionNodes.splice(conditionConfig.value.priorityLevel - 1, 0, a[0]) // 填充新下标
  conditionsConfig.value.conditionNodes.map((item: any, index) => {
    item.priorityLevel = index + 1
  })

  for (var i = 0; i < conditionsConfig.value.conditionNodes.length; i++) {
    const conditionNode: any = conditionsConfig.value.conditionNodes[i]

    conditionNode.error = false
    const conditionList = conditionNode.conditionList
    if (i !== conditionsConfig.value.conditionNodes.length - 1) {
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
    conditionNode.placeHolder = $func.conditionStr(conditionsConfig.value, i)
  }
  setConditionsConfig({
    value: conditionsConfig.value,
    flag: true,
    id: conditionsConfig1.value.id
  })
}

const closeDrawer = () => {
  setCondition(false)
}
</script>
<style lang="scss" scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
