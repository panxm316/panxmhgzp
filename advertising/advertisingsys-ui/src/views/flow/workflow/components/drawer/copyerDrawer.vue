<template>
  <el-drawer
    v-model="visible"
    :append-to-body="true"
    title="抄送人设置"
    class="set_copyer"
    :show-close="false"
    :size="550"
    :before-close="saveCopyer"
    @open="openEvent"
  >
    >
    <el-tabs type="border-card">
      <el-tab-pane label="设置抄送人">
        <select-show
          v-model:orgList="copyerConfig.nodeUserList"
          type="org"
          :multiple="true"
        ></select-show>
      </el-tab-pane>
      <el-tab-pane label="表单权限">
        <form-perm :hide-key="['E']" :form-perm="copyerConfig.formPerms"></form-perm>
      </el-tab-pane>
    </el-tabs>
  </el-drawer>
</template>
<script setup>
import selectShow from '@/views/flow/workflow/components/dialog/selectAndShow.vue'

import $func from '../../utils/index'
import { useStore } from '../../stores/index'
import { ref, watch, computed } from 'vue'
const copyerConfig = ref({})

import { useFlowStore } from '../../stores/flow'
import { ElTable } from 'element-plus'
const flowStore = useFlowStore()

import FormPerm from './components/formPerm.vue'

const step2FormList = computed(() => {
  const step2 = flowStore.step2
  return step2
})

const store = useStore()
const { setCopyerConfig, setCopyer } = store
const copyerDrawer = computed(() => store.copyerDrawer)
const copyerConfig1 = computed(() => store.copyerConfig1)
const visible = computed({
  get() {
    return copyerDrawer.value
  },
  set() {
    closeDrawer()
  }
})
watch(copyerConfig1, (val) => {
  copyerConfig.value = val.value
})

const openEvent = () => {
  const value = step2FormList.value
  var arr = {}
  const formPerms = copyerConfig.value.formPerms

  for (var item of value) {
    arr[item.id] = 'R'
    // if (item.type === 'Layout') {
    //   arr[item.id] = "R"
    // }
    if (formPerms[item.id]) {
      arr[item.id] = formPerms[item.id]
    }
    if (item.type === 'Layout') {
      const value1 = item.props.value
      for (var it of value1) {
        arr[it.id] = 'R'
        if (formPerms[it.id]) {
          arr[it.id] = formPerms[it.id]
        }
      }
    }
  }
  copyerConfig.value.formPerms = arr
}

const saveCopyer = () => {
  copyerConfig.value.error = !$func.copyerStr(copyerConfig.value)
  setCopyerConfig({
    value: copyerConfig.value,
    flag: true,
    id: copyerConfig1.value.id
  })
  closeDrawer()
}
const closeDrawer = () => {
  setCopyer(false)
}
</script>

<style lang="scss" scoped></style>
