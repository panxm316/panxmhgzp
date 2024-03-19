<!--
 * @Author: peij
 * @Date: 2023-10-12 09:01:05
 * @LastEditTime: 2023-10-20 13:11:41
 * @LastEditors: peij
 * @Description: 基础信息
-->
<template>
  <div>
    <div class="container-div">
      <el-card class="box-card" style="padding: 20px">
        <el-form
          ref="ruleFormRef"
          :model="form"
          :rules="rules"
          label-position="top"
          status-icon
          label-width="120px"
          @submit.prevent
        >
          <el-form-item label="名称" prop="name">
            <el-input v-model="form.name" />
          </el-form-item>
          <el-form-item label="说明" prop="remark">
            <el-input v-model="form.remark" />
          </el-form-item>
          <el-form-item label="分组" prop="groupId">
            <el-select v-model="form.groupId" placeholder="请选择流程组" @change="handleChange">
              <el-option
                v-for="item in groupList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="管理员" prop="admin">
            <select-show v-model:orgList="form.admin" type="user" :multiple="false"></select-show>
          </el-form-item>
        </el-form>
      </el-card>
    </div>
  </div>
</template>

<script lang="ts" setup>
import type { FormInstance, FormRules } from 'element-plus'
import selectShow from '@/views/flow/workflow/components/dialog/selectAndShow.vue'
import { useFlowStore } from '../workflow/stores/flow'
import { useRoute } from 'vue-router'
import { getFlowTypesComboApi } from '@/api/flow/flowcondition'
import { IAxios } from 'axios'
import { IDataCombo } from '@/types/common/DataCombo'
const props = defineProps({
  groupId: {
    type: String,
    default: undefined
  }
})

const flowStore = useFlowStore()
const route = useRoute()
const groupList = ref<IDataCombo[]>([])
const ruleFormRef = ref<FormInstance>()
const form = computed(() => {
  return flowStore.step1
})

const emit = defineEmits(['clearStep3'])
/** 触发清除流程设置器数据 */
const handleChange = () => {
  emit('clearStep3')
}

onMounted(() => {
  getFlowTypesComboApi().then(({ success, obj }: IAxios) => {
    if (success) {
      groupList.value = obj
    }
  })
})

watch(
  () => props.groupId,
  (val) => {
    if (val) {
      form.value.groupId = val
    }
  }
)

const validate = (f: (valid: boolean, arr: string[]) => void) => {
  ruleFormRef.value?.validate((valid, fields) => {
    const arr: string[] = []
    if (!valid) {
      for (var err in fields) {
        arr.push(fields[err][0].message ?? '')
      }
    }
    f(valid, arr)
  })
}
const rules = reactive<FormRules>({
  name: [
    { required: true, message: '请填写名称', trigger: 'blur' },
    { min: 2, max: 10, message: '2-10个字符', trigger: 'blur' }
  ],
  remark: [
    { required: false, message: '请填写描述', trigger: 'blur' },
    { min: 2, max: 20, message: '2-20个字符', trigger: 'blur' }
  ],
  groupId: [
    {
      required: true,
      message: '请选择分组',
      trigger: 'change'
    }
  ],
  admin: [
    {
      required: true,
      message: '请选择管理员',
      trigger: 'change'
    }
  ]
})

// 暴露方法和属性给父组件
defineExpose({ validate })
</script>
<style scoped lang="scss">
.container-div {
  width: 800px;
  margin-left: calc(50% - 400px);
  text-align: center;
}
</style>
