<!--
 * @Author: wanghl
 * @Date: 2023-10-26 10:11:02
 * @LastEditTime: 2024-02-19 15:04:27
 * @LastEditors: wanghl
 * @Description:客户归属
-->
<template>
  <div class="app-container">
    <div style="width: 100%; margin: -10px 0 10px 5px">
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
          <el-table-column prop="customerName" label="名称" sortable="custom" min-width="130">
          </el-table-column>
          <el-table-column prop="bprimary" label="业务员" min-width="130">
            <template #default="scope">
              <span v-if="scope.row.bprimary === false"> 普通业务员 </span>
              <span v-if="scope.row.bprimary === true"> 主业务员 </span>
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
                @click="handleUpdate(adproject.row)"
                >设为主业务员</el-button
              >
              <el-button
                link
                type="danger"
                icon="Delete"
                size="small"
                @click="handleDelete(adproject.row)"
                >删除</el-button
              >
            </template>
          </el-table-column>
        </el-table>
      </el-row>
    </div>
    <el-dialog
      v-model="dialogVisible"
      title="客户编辑"
      width="500"
      align-center
      :close-on-click-modal="false"
      @close="handleCancel"
    >
      <el-form ref="CustomerFormRef" :model="WcustomerBelong" label-width="100px" :rules="rules">
        <el-row :gutter="20">
          <el-col :span="22">
            <el-form-item label="业务员" prop="employid">
              <HgZtreeSelect
                :scopeflag="EHgDeptZtreeUrl.EMP_GETDEPTEMPLOYPERSON"
                :selectids="WcustomerBelong.employid ? [WcustomerBelong.employid] : []"
                style="width: 100%"
                :filterable="true"
                :treeparams="{ bIgnorePermissions: true }"
                @selectionztree="onSelectionNewEmp"
              ></HgZtreeSelect>
            </el-form-item>
            <el-form-item v-if="bprimaryshow == true" label="是否是主业务员" prop="bprimary">
              <el-checkbox v-model="WcustomerBelong.bprimary"></el-checkbox>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" icon="Check" @click="handleSave">保存</el-button>
          <el-button icon="Close" @click="handleCancel">取消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { FormRules, FormInstance } from 'element-plus'
import type { Twcustomerbelong } from '@/types/views/customer'
import {
  getCustomerbelongListApi,
  getCustomerbelongByIdApi,
  saveCustomerbelongApi,
  updateCustomerbelongApi,
  deleteCustomerbelongByIdApi,
  deleteCustomerbelongByCustomerIdApi
} from '@/api/customer/index'
import { required, formatDate, tableHeaderColor } from '@/utils/index'
import type { IAxios } from 'axios'
import modal from '@/plugins/modal'
import { EHgDeptZtreeUrl } from '@/types/common/enumindex'
import { TSelectZtree } from '@/types/common'
defineOptions({
  name: 'CustomerBelong'
})
type THSelectCustomer = {
  /**
   * 传入客户id
   */
  customerid?: string
}
const props = withDefaults(defineProps<THSelectCustomer>(), { customerid: '' })
/** 导出返回数据 */
// const emit = defineEmits<{
//   onresponse: [data: Twcustomerbelong]
// }>()
/** 客户id */
const customerid = ref<string>(props.customerid)
/** 表格高度 */
const tableheight = ref(0)
/** 客户归属列表列表 */
const WcustomerBelongList = ref<Twcustomerbelong[]>([])
/** 判断是否存在主业务员 */
const bprimaryshow = ref<boolean | undefined>(true)
/** 客户归属列表表 */
const WcustomerBelong = ref<Twcustomerbelong>({
  deptid: '',
  deptname: '',
  id: '',
  customerid: props.customerid,
  employid: '',
  employname: '',
  applicationid: '',
  dcreatetime: '',
  bprimary: true,
  iapprovestatus: ''
})
/** 编辑 dialog */
const dialogVisible = ref(false)
const CustomerFormRef = ref<FormInstance>()
const AdProjectSelection = ref<Twcustomerbelong[]>()
/**
 * 校验设置参数
 */
const rules = reactive<FormRules<Twcustomerbelong>>({
  customerid: [{ required: true, message: '请选择业务员', trigger: 'change' }]
})
/** 监听传入数值变化 */
watch(
  () => props.customerid,
  (newValue, oldValue) => {
    console.log(newValue)
    customerid.value = newValue
    if (newValue !== '') {
      loaddata()
    }
  }
)
onMounted(() => {
  loaddata()
  console.log(props.customerid)
  /**
   * 计算表格高度
   */
  // tableheight.value = 300
})
/**
 * 获取归属列表
 */
const loaddata = () => {
  if (customerid.value !== '') {
    getCustomerbelongListApi(customerid.value).then(({ obj }: IAxios<Twcustomerbelong[]>) => {
      WcustomerBelongList.value = obj
    })
  }
}
/**
 * 新增
 */
const handleAdd = () => {
  resetbelong()
  dialogVisible.value = true
  var list = WcustomerBelongList.value.map((item) => item.bprimary).join(',')
  // var list = WcustomerBelongList.value.filter((item) => item.bprimary === true)
  if (list !== '') {
    bprimaryshow.value = false
    WcustomerBelong.value.bprimary = false
  } else {
    bprimaryshow.value = false
    WcustomerBelong.value.bprimary = true
  }
}
/**
 * 设为主业务员
 * @param row
 */
const handleUpdate = (row: Twcustomerbelong) => {
  WcustomerBelong.value = { ...row }
  WcustomerBelong.value.bprimary = true
  updateCustomerbelongApi(WcustomerBelong.value).then(({ success, msg }: IAxios) => {
    if (success) {
      modal.msgSuccess('主业务员')
    } else {
      modal.msgError(msg)
    }
    loaddata()
  })
}
/**
 * 保存
 */
const handleSave = () => {
  CustomerFormRef.value?.validate((valid) => {
    if (valid) {
      if (!WcustomerBelong.value.id) {
        saveCustomerbelongApi(WcustomerBelong.value).then(({ success, msg }: IAxios) => {
          if (success) {
            modal.msgSuccess('操作成功')
          } else {
            modal.msgError(msg)
          }
          loaddata()
        })
      }
      dialogVisible.value = false
      resetbelong()
    }
  })
}
/**
 * 取消
 */
const handleCancel = () => {
  CustomerFormRef.value?.resetFields()
  dialogVisible.value = false
  setTimeout(() => {
    CustomerFormRef.value?.clearValidate(['sname'])
  }, 10)
}
/**
 * 删除
 * @param row
 */
const handleDelete = (row?: Twcustomerbelong) => {
  modal.confirm('是否删除?').then(() => {
    const ids = []
    if (row) {
      ids.push(row.id)
    } else {
      AdProjectSelection.value?.forEach((color) => ids.push(color.id))
    }
    if (!ids.length) {
      modal.msgWarning('请选择删除记录')
      return
    }
    deleteCustomerbelongByIdApi(ids.join(',')).then(({ success, msg }: IAxios) => {
      if (success) {
        modal.msgSuccess('操作成功')
      } else {
        modal.msgError(msg)
      }
      loaddata()
    })
  })
}
/**
 * 编辑的时候选择业务人员
 * @param val
 */
const onSelectionNewEmp = (val: TSelectZtree[]) => {
  console.log(val)
  const employid = val.map((item) => item.id).join(',')
  WcustomerBelong.value.employid = employid
  WcustomerBelong.value.employname = val.map((item) => item.name).join(',')
  // encodeURIComponent(JSON.stringify(WcustomerBelong.value.deptname))
  WcustomerBelong.value.deptid = val.map((item) => item.parentId).join(',')
  WcustomerBelong.value.deptname = val.map((item) => item.parentName).join(',')
}
/**
 * 关闭是清空数据
 */
const resetbelong = () => {
  WcustomerBelong.value = {
    deptid: '',
    deptname: '',
    id: '',
    customerid: props.customerid,
    employid: '',
    employname: '',
    applicationid: '',
    dcreatetime: '',
    bprimary: true,
    iapprovestatus: ''
  }
  setTimeout(() => {
    CustomerFormRef.value?.clearValidate(['customerid'])
  }, 10)
}
</script>

<style scoped>
ul {
  padding-left: 10px;
}
</style>
