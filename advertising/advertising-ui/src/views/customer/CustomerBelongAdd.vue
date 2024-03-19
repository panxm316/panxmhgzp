<!--
 * @Author: wanghl
 * @Date: 2023-10-26 10:11:02
 * @LastEditTime: 2024-03-08 13:17:01
 * @LastEditors: yanz
 * @Description:客户归属新增
-->
<template>
  <div class="app-container">
    <div style="width: 100%; margin: -10px 0 10px 5px">
      <HgZtreeSelect
        :scopeflag="EHgDeptZtreeUrl.EMP_GETDEPTEMPLOYPERSON"
        :selectids="WcustomerBelong.employid ? [WcustomerBelong.employid] : []"
        style="width: 300px; margin-right: 10px"
        :filterable="true"
        :treeparams="{ bIgnorePermissions: true }"
        @selectionztree="onSelectionNewEmp"
      ></HgZtreeSelect>
      <el-button type="primary" icon="Plus" @click="handleAdd">新增</el-button>
    </div>
    <div class="table_box">
      <el-row>
        <el-table
          :data="WcustomerBelongList"
          row-key="id"
          :border="true"
          stripe
          :header-cell-style="tableHeaderColor"
          style="width: 100%; min-height: 140px; max-height: 300px"
        >
          <!-- <el-table-column type="selection" prop="ischeck" :width="50" align="center">
          </el-table-column> -->
          <el-table-column prop="employname" label="名称" sortable="custom" min-width="130">
          </el-table-column>
          <el-table-column prop="bprimary" label="是否主业务员" min-width="130">
            <template #default="scope">
              <el-checkbox v-model="scope.row.bprimary" disabled></el-checkbox>
            </template>
          </el-table-column>
          <el-table-column
            prop="deptname"
            label="所属部门"
            sortable="custom"
            min-width="140"
            show-overflow-tooltip
          >
          </el-table-column>
          <el-table-column prop="dcreatetime" label="添加时间" min-width="160">
            <template #default="scope">
              <span>{{ formatDate(scope.row.dcreatetime) }}</span>
            </template>
          </el-table-column>

          <el-table-column
            label="操作"
            align="center"
            width="160"
            class-name="small-padding fixed-width"
            fixed="right"
          >
            <template #default="adproject">
              <el-button
                v-if="adproject.row.bprimary === false"
                link
                type="success"
                icon="Edit"
                size="small"
                @click="handleUpdate(adproject.row, adproject.$index)"
                >设为主业务员</el-button
              >
              <el-button
                link
                type="danger"
                icon="Delete"
                size="small"
                @click="handleDelete(adproject.row, adproject.$index)"
                >删除</el-button
              >
            </template>
          </el-table-column>
        </el-table>
      </el-row>
    </div>
  </div>
</template>

<script setup lang="ts">
import { FormRules, FormInstance } from 'element-plus'
import type { Twcustomerbelong } from '@/types/views/customer'
import { required, formatDate, tableHeaderColor } from '@/utils/index'
import type { IAxios } from 'axios'
import modal from '@/plugins/modal'
import { EHgDeptZtreeUrl } from '@/types/common/enumindex'
import { TSelectZtree } from '@/types/common'
import { fa, tr } from 'element-plus/es/locale'
defineOptions({
  name: 'CustomerBelong'
})
const props = defineProps<{
  CustomerData?: Twcustomerbelong[]
}>()
/** 导出返回数据 */
const emit = defineEmits<{
  WcustomerBelong: [data: Twcustomerbelong[]]
}>()
/** 客户id */
const customerid = ref<string>('')
/** 客户归属列表列表 */
const WcustomerBelongList = ref<Twcustomerbelong[]>([])
/** 判断是否存在主业务员 */
const bprimaryshow = ref<boolean | undefined>(true)
/** 客户归属列表表 */
const WcustomerBelong = ref<Twcustomerbelong>({
  deptid: '',
  deptname: '',
  id: '',
  customerid: '',
  employid: '',
  employname: '',
  applicationid: '',
  dcreatetime: '',
  bprimary: true,
  iapprovestatus: ''
})
const CustomerFormRef = ref<FormInstance>()
/**
 * 校验设置参数
 */
const rules = reactive<FormRules<Twcustomerbelong>>({
  customerid: [{ required: true, message: '请选择业务员', trigger: 'change' }]
})
/** 监听传入数值变化 */
watch(
  () => props.CustomerData,
  (newValue, oldValue) => {
    // WcustomerBelongList.value = props.CustomerData as Twcustomerbelong[]
    WcustomerBelongList.value = newValue as Twcustomerbelong[]
  },
  { deep: true }
)
onMounted(() => {
  WcustomerBelongList.value = props.CustomerData as Twcustomerbelong[]
  console.log(WcustomerBelongList.value)
  if (props.CustomerData!.length > 0) {
    WcustomerBelong.value.customerid = props.CustomerData![0].id
  }
})

/**
 * 新增
 */
const handleAdd = () => {
  if (WcustomerBelong.value.employid === '') {
    modal.msgWarning('请选择业务员')
    return
  }
  if (WcustomerBelongList.value.length >= 1) {
    const bprimary = ref(false)
    WcustomerBelongList.value.forEach((item) => {
      if (item.employid === WcustomerBelong.value.employid) {
        modal.msgWarning('该业务员已存在')
        bprimary.value = true
        return
      }
    })
    if (bprimary.value === false) {
      WcustomerBelongList.value.push(WcustomerBelong.value)
    }
  } else if (WcustomerBelongList.value.length === 0) {
    WcustomerBelongList.value.push(WcustomerBelong.value)
  }
  emit('WcustomerBelong', WcustomerBelongList.value)
  resetbelong()
}
/**
 * 设为主业务员
 * @param row
 */
const handleUpdate = (row: Twcustomerbelong, indexdata?: number) => {
  WcustomerBelong.value = { ...row }
  WcustomerBelong.value.bprimary = true
  WcustomerBelongList.value.splice(indexdata as number, 1, WcustomerBelong.value)
  WcustomerBelongList.value.forEach((item, index) => {
    if (index !== indexdata) {
      item.bprimary = false
    }
  })
  resetbelong()
}
/**
 * 删除
 * @param row
 */
const handleDelete = (row?: Twcustomerbelong, index?: number) => {
  WcustomerBelongList.value.splice(index as number, 1)
  if (WcustomerBelongList.value.length > 0) {
    WcustomerBelongList.value[0].bprimary = true
  }
  resetbelong()
}
/**
 * 编辑的时候选择业务人员
 * @param val
 */
const onSelectionNewEmp = (val: TSelectZtree[]) => {
  console.log(val)
  WcustomerBelong.value.employid = val.map((item) => item.id).join(',')
  WcustomerBelong.value.employname = val.map((item) => item.name).join(',')
  WcustomerBelong.value.deptid = val.map((item) => item.parentId).join(',')
  WcustomerBelong.value.deptname = val.map((item) => item.parentName).join(',')
  WcustomerBelong.value.dcreatetime = nowTime()
  if (WcustomerBelongList.value.length >= 1) {
    WcustomerBelong.value.bprimary = false
  } else {
    WcustomerBelong.value.bprimary = true
  }
}
/**
 * 获取当前时间
 */
const nowTime = () => {
  const now = new Date()
  const year = now.getFullYear()
  const month = (now.getMonth() + 1).toString().padStart(2, '0')
  const day = now.getDate().toString().padStart(2, '0')
  return year + '-' + month + '-' + day
}
/**
 * 关闭是清空数据
 */
const resetbelong = () => {
  WcustomerBelong.value = {
    deptid: '',
    deptname: '',
    id: '',
    customerid: '',
    employid: '',
    employname: '',
    applicationid: '',
    dcreatetime: '',
    bprimary: true,
    iapprovestatus: ''
  }
  setTimeout(() => {
    CustomerFormRef.value?.clearValidate(['sname'])
  }, 10)
}
</script>

<style scoped>
ul {
  padding-left: 10px;
}
</style>
