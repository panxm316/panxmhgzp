<!--
 * @Date: 2022-08-25 14:05:59
 * @LastEditors: peij
 * @LastEditTime: 2023-10-14 17:22:44
 * @FilePath: /Workflow-Vue3/src/components/dialog/roleDialog.vue
-->
<template>
  <el-dialog
    v-model="visibleDialog"
    title="选择角色"
    :width="700"
    append-to-body
    class="promoter_person"
  >
    <div class="person_body clear">
      <div class="person_tree l">
        <input
          v-model="searchVal"
          type="text"
          placeholder="搜索角色"
          @input="getDebounceData($event, 2)"
        />
        <selectBox :list="list" />
      </div>
      <selectResult :total="total" :list="resList" @del="delList" />
    </div>
    <template #footer>
      <el-button @click="closeDialog">取 消</el-button>
      <el-button type="primary" @click="saveDialog">确 定</el-button>
    </template>
  </el-dialog>
</template>
<script setup>
import selectBox from '../selectBox.vue'
import selectResult from '../selectResult.vue'
import { computed, watch, ref } from 'vue'
import $func from '../../utils/index'
import { roles, getDebounceData, getRoleList, searchVal } from './common'
const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  data: {
    type: Array,
    default: () => []
  }
})
const checkedRoleList = ref([])
const emits = defineEmits(['update:visible', 'change'])
const list = computed(() => {
  return [
    {
      type: 'role',
      not: true,
      data: roles.value,
      isActive: (item) => $func.toggleClass(checkedRoleList.value, item, 'roleId'),
      change: (item) => {
        checkedRoleList.value = [item]
      }
    }
  ]
})
const resList = computed(() => {
  return [
    {
      type: 'role',
      data: checkedRoleList.value,
      cancel: (item) => $func.removeEle(checkedRoleList.value, item, 'roleId')
    }
  ]
})
const visibleDialog = computed({
  get() {
    return props.visible
  },
  set(val) {
    closeDialog()
  }
})
watch(
  () => props.visible,
  (val) => {
    if (val) {
      getRoleList()
      searchVal.value = ''
      checkedRoleList.value = props.data.map(({ name, targetId }) => ({
        roleName: name,
        roleId: targetId
      }))
    }
  }
)
const total = computed(() => checkedRoleList.value.length)
const saveDialog = () => {
  const checkedList = checkedRoleList.value.map((item) => ({
    type: 2,
    targetId: item.roleId,
    name: item.roleName
  }))
  emits('change', checkedList)
}
const delList = () => {
  checkedRoleList.value = []
}

const closeDialog = () => {
  emits('update:visible', false)
}
</script>

<style>
@import '../../css/dialog.css';
</style>
