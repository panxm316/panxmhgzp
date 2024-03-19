<template>
  <el-dialog
    v-model="visibleDialog"
    title="选择成员"
    :width="700"
    append-to-body
    class="promoter_person"
  >
    <div class="person_body clear">
      <div class="person_tree l">
        <selectBox
          ref="selectBoxRef"
          v-model:selectedList="selectedList"
          :select-self="selectSelf"
          :list="list"
          :multiple="multiple"
          :type="type"
        />
      </div>
      <selectResult :total="total" :list="resList" @del="delList" />
    </div>
    <template #footer>
      <el-button type="primary" icon="Check" @click="saveDialog">确 定</el-button>
      <el-button icon="Close" @click="$emit('update:visible', false)">取 消</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import selectBox from '../selectBox.vue'
import selectResult from '../selectResult.vue'
import { computed, watch, ref, onMounted } from 'vue'
import { departments, getDebounceData, searchVal } from './common'
import { deepCopy } from '@/utils/index'

const selectBoxRef = ref()

const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  data: {
    type: Array,
    default: () => []
  },
  type: {
    type: String,
    default: 'org'
  },
  multiple: {
    type: Boolean,
    default: true
  },
  selectSelf: {
    type: Boolean,
    default: true
  }
})

// 已选择的集合
const selectedList = ref([])

const emits = defineEmits(['update:visible', 'change'])
const visibleDialog = computed({
  get() {
    return props.visible
  },
  set() {
    closeDialog()
  }
})
const isChecked = (id, type) => {
  return selectedList.value.filter((res) => res.id === id && res.type === type).length > 0
}
const list = computed(() => {
  const value = departments.value
  return [
    {
      type: 'dept',
      data: value === undefined ? [] : value.childDepartments
    },
    {
      type: 'role',
      data: value === undefined ? [] : value.roleList
    },
    {
      type: 'user',
      data: value === undefined ? [] : value.employees,
      change: (item) => {
        if (!isChecked(item.id, item.type)) {
          if (!props.multiple) {
            // 单选
            selectedList.value = []
          }

          selectedList.value.push(item)
        } else {
          selectedList.value = selectedList.value.filter(
            (res) => !(res.id === item.id && res.type === item.type)
          )
        }
      }
    }
  ]
})
const resList = computed(() => {
  const userData = selectedList.value.filter((res) => res.type === 'user')
  const deptData = selectedList.value.filter((res) => res.type === 'dept')
  const roleData = selectedList.value.filter((res) => res.type === 'role')
  const data = [
    {
      type: 'user',
      data: userData,
      cancel: (item) => {
        item.selected = false
        selectBoxRef.value.changeEvent(item)
      }
    }
  ]
  if (props.type === 'org' || props.type === 'dept') {
    data.unshift({
      type: 'dept',
      data: deptData,
      cancel: (item) => {
        item.selected = false
        selectBoxRef.value.changeEvent(item)
      }
    })
  }
  if (props.type === 'role') {
    data.unshift({
      type: 'role',
      data: roleData,
      cancel: (item) => {
        item.selected = false
        selectBoxRef.value.changeEvent(item)
      }
    })
  }
  return data
})

watch(
  () => props.visible,
  (val) => {
    if (val) {
      selectedList.value = props.data

      searchVal.value = ''
    }
  }
)
const closeDialog = () => {
  emits('update:visible', false)
}

const total = computed(() => {
  const v = departments.value
  if (!v) {
    return 0
  }
  return selectedList.value.length
})

const { proxy } = getCurrentInstance()
const saveDialog = () => {
  var v = selectedList.value

  const checkedList = deepCopy(v).map((item) => ({
    type: item.type,
    id: item.id,
    name: item.name,
    avatar: '' // item.avatar peij 20231024 去掉头像
  }))
  emits('change', checkedList)
  // selectedList.value=[]
}
const delList = () => {
  for (var item of deepCopy(selectedList.value)) {
    item.selected = false
    selectBoxRef.value.changeEvent(item)
  }
  selectedList.value = []
}

onMounted(() => {})
</script>
<style scoped>
@import '../../css/dialog.css';
</style>
