<template>
  <el-drawer
    v-model="visible"
    :append-to-body="true"
    title="发起人"
    class="set_promoter"
    :show-close="false"
    :size="550"
    :before-close="savePromoter"
    @open="openEvent"
  >
    <div class="demo-drawer__content">
      <el-tabs type="border-card">
        <el-tab-pane label="设置发起人">
          <select-show
            v-model:orgList="starterConfig.nodeUserList"
            type="org"
            :multiple="true"
          ></select-show>
        </el-tab-pane>
        <el-tab-pane label="表单权限">
          <form-perm :form-perm="starterConfig.formPerms"></form-perm>
        </el-tab-pane>
      </el-tabs>
    </div>
  </el-drawer>
</template>
<script setup>
import selectShow from '@/views/flow/workflow/components/dialog/selectAndShow.vue'
import { useFlowStore } from '../../stores/flow'

import { useStore } from '../../stores/index'
import { computed, ref, watch } from 'vue'

import FormPerm from './components/formPerm.vue'

const store = useStore()
const starterConfig = ref({})
const flowStore = useFlowStore()

const starterConfigData = computed(() => store.starterConfigData)
watch(starterConfigData, (val) => {
  starterConfig.value = val.value
})
const step2FormList = computed(() => {
  const step2 = flowStore.step2

  return step2
})

const openEvent = () => {
  const value = step2FormList.value
  var arr = {}
  const formPerms = starterConfig.value.formPerms
  console.log(formPerms)

  for (var item of value) {
    arr[item.id] = 'E'

    if (formPerms[item.id]) {
      arr[item.id] = formPerms[item.id]
    }
    if (item.type === 'Layout') {
      const value1 = item.props.value
      for (var it of value1) {
        arr[it.id] = 'E'
        if (formPerms[it.id]) {
          arr[it.id] = formPerms[it.id]
        }
      }
    }
  }
  starterConfig.value.formPerms = arr
}

const { setPromoter, setStarterConfig } = store
const promoterDrawer = computed(() => store.promoterDrawer)
const visible = computed({
  get() {
    return promoterDrawer.value
  },
  set() {
    closeDrawer()
  }
})
watch(starterConfigData, (val) => {
  starterConfig.value = val.value
})
const savePromoter = () => {
  setStarterConfig({
    value: starterConfig.value,
    flag: true,
    id: starterConfigData.value.id
  })
  closeDrawer()
}
const closeDrawer = () => {
  setPromoter(false)
}
</script>
<style lang="scss" scoped></style>
