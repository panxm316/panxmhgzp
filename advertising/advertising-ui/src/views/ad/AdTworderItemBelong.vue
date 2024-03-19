<!--
 * @Author: wanghl
 * @Date: 2023-11-30 09:44:40
 * @LastEditTime: 2024-03-07 13:58:17
 * @LastEditors: wanghl
 * @Description:AdTworder广告预定--归属
-->
<template>
  <div class="app-container">
    <div>
      <!-- <div v-if="props.addshow === false" style="width: 100%; margin: -10px 0 10px 5px">
        <el-button type="primary" icon="Plus" @click="handleAdd">新增归属</el-button>
      </div> -->
      <el-form
        v-if="props.bottomshow !== false && userStore.user?.blead === true"
        ref="TwordwrFormRef"
        :model="worderitembelong"
        :rules="rules"
      >
        <el-row>
          <el-col :span="8">
            <el-form-item label="业务员" prop="employid">
              <HgZtreeSelect
                :scopeflag="EHgDeptZtreeUrl.EMP_GETDEPTEMPLOYPERSON"
                :selectids="worderitembelong.employid ? [worderitembelong.employid] : []"
                style="width: 95%"
                :filterable="true"
                :treeparams="{ bIgnorePermissions: true }"
                @selectionztree="onSelectionNewEmp"
              ></HgZtreeSelect>
            </el-form-item>
            <el-form-item label="类型" prop="employtype">
              <el-radio-group v-model="worderitembelong.employtype">
                <el-radio :label="0">直客</el-radio>
                <el-radio :label="1">代理业务员</el-radio>
                <el-radio :label="2">内容生产方</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item prop="archievementrate" label="业绩比例">
              <el-input-number
                v-model="worderitembelong.archievementrate"
                :precision="1"
                :step="0.1"
                :min="0"
                :max="100"
                :controls="false"
                style="width: 90%"
              />
              <span style="margin-left: 10px">%</span>
            </el-form-item>

            <el-form-item label="是否主业务员" prop="bprimary">
              <el-checkbox
                v-model="worderitembelong.bprimary"
                :disabled="worderitembelong.bprimary === true"
              ></el-checkbox>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="备注" prop="sremark">
              <el-input v-model="worderitembelong.sremark" :maxlength="200" />
            </el-form-item>

            <div style="width: 100%; text-align: center; margin-bottom: 20px">
              <el-button type="primary" icon="Check" @click="handleSave">确定</el-button>
              <el-button icon="Close" @click="handleCancel">取消</el-button>
            </div>
          </el-col>
        </el-row>
      </el-form>
      <el-row :span="22">
        <el-table
          :data="worderitembelonglsit"
          row-key="id"
          :border="true"
          stripe
          :header-cell-style="tableHeaderColor"
        >
          <el-table-column prop="employname" label="业务员"> </el-table-column>
          <el-table-column prop="employtype" label="类型">
            <template #default="scope">
              <span v-if="scope.row.employtype === 0">直客业务员</span>
              <span v-if="scope.row.employtype === 1">代理业务员</span>
              <span v-if="scope.row.employtype === 2">内容生产方业务员</span>
            </template>
          </el-table-column>
          <el-table-column prop="archievementrate" label="业绩比例" sortable="custom">
            <template #default="scope">
              <span>{{ parseFloat(scope.row.archievementrate).toFixed(2) }}%</span>
            </template>
          </el-table-column>
          <el-table-column prop="bprimary" label="是否主业务员" align="center">
            <template #default="scope">
              <el-checkbox v-model="scope.row.bprimary" disabled></el-checkbox>
              <!-- <span>{{ scope.row.bprimary }}%</span> -->
            </template>
          </el-table-column>
          <el-table-column prop="deptname" label="所属部门" show-overflow-tooltip>
          </el-table-column>
          <!-- <el-table-column prop="sremark" label="备注" min-width="200" show-overflow-tooltip>
          </el-table-column> -->
          <el-table-column
            v-if="props.bottomshow !== false"
            label="操作"
            align="left"
            width="120"
            class-name="small-padding fixed-width"
            fixed="right"
          >
            <template #default="scope">
              <el-button
                link
                type="success"
                icon="Edit"
                size="small"
                @click="handleUpdate(scope.$index, scope.row)"
                >修改</el-button
              >
              <el-button
                v-if="scope.row.bprimary === false"
                link
                type="danger"
                icon="Delete"
                size="small"
                @click="handleDelete(scope.$index, scope.row)"
                >删除</el-button
              >
            </template>
          </el-table-column>
        </el-table>
      </el-row>
    </div>
    <el-dialog
      v-model="filesshow"
      title="新增归属"
      width="700"
      align-center
      :close-on-click-modal="false"
      @close="handleCancel"
    >
      <div style="width: 100%">
        <el-form ref="TwordwrFormRef" :model="worderitembelong" label-width="120px" :rules="rules">
          <el-row :gutter="24">
            <el-col :span="22">
              <el-form-item label="业务员" prop="employid">
                <HgZtreeSelect
                  :scopeflag="EHgDeptZtreeUrl.EMP_GETDEPTEMPLOYPERSON"
                  :selectids="worderitembelong.employid ? [worderitembelong.employid] : []"
                  style="width: 100%"
                  :filterable="true"
                  :treeparams="{ bIgnorePermissions: true }"
                  @selectionztree="onSelectionNewEmp"
                ></HgZtreeSelect>
              </el-form-item>
              <el-form-item label="备注" prop="sremark">
                <el-input v-model="worderitembelong.sremark" :maxlength="200" />
              </el-form-item>
              <el-form-item label="类型" prop="employtype">
                <el-radio-group v-model="worderitembelong.employtype">
                  <el-radio :label="0">直客</el-radio>
                  <el-radio :label="1">代理业务员</el-radio>
                  <el-radio :label="2">内容生产方</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item prop="archievementrate" label="业绩比例">
                <el-input-number
                  v-model="worderitembelong.archievementrate"
                  :precision="1"
                  :step="0.1"
                  :min="0"
                  :max="100"
                  :controls="false"
                  style="width: 200px"
                />
                <span style="margin-left: 10px">%</span>
              </el-form-item>
              <!-- <el-form-item prop="taskrate" label="任务比例">
                <el-input-number
                  v-model="worderitembelong.taskrate"
                  :precision="1"
                  :step="0.1"
                  :min="0"
                  :max="100"
                  :controls="false"
                  style="width: 200px"
                />
                <span style="margin-left: 10px">%</span>
              </el-form-item>
              <el-form-item prop="commissionrate" label="佣金比例">
                <el-input-number
                  v-model="worderitembelong.commissionrate"
                  :precision="1"
                  :step="0.1"
                  :min="0"
                  :max="100"
                  :controls="false"
                  style="width: 200px"
                />
                <span style="margin-left: 10px">%</span>
              </el-form-item> -->
              <el-row :gutter="10">
                <el-col :span="6">
                  <el-form-item label="是否主业务员" prop="bprimary">
                    <el-checkbox v-model="worderitembelong.bprimary" disabled></el-checkbox>
                  </el-form-item>
                </el-col>
              </el-row>
            </el-col>
          </el-row>
          <div style="width: 100%; text-align: center; margin-bottom: 20px">
            <el-button type="primary" icon="Check" @click="handleSave">确定</el-button>
            <el-button icon="Close" @click="handleCancel">取消</el-button>
          </div>
        </el-form>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { FormRules, FormInstance, menuItemEmits } from 'element-plus'
import { TFlowType } from '@/types/views/flow/flow'
import type {
  TAdCustomer,
  TQueryCustomer,
  Twcustomerfiles,
  Twcustomerbelong,
  TCustomerApprove
} from '@/types/views/customer'
import type {
  TworderCustomer,
  Tworderitembelong,
  Tworderitem,
  Tworder
} from '@/types/views/ad/adtworder'
import {
  getorderByIdApi,
  getContractNumByEmployIdAndCustomerIdApi,
  saveorderApi,
  updateorderApi,
  deleteorderApi,
  getApproveTypeComboApi,
  getorderItemBelonglistApi
} from '@/api/ad/adtworder'
import {
  required,
  computTableHeight,
  isNumberStr,
  formatDate,
  tableHeaderColor,
  validatePhone
} from '@/utils/index'
import type { IAxios } from 'axios'

import modal from '@/plugins/modal'
import useUserStore from '@/store/modules/user'
import { EHgDeptZtreeUrl } from '@/types/common/enumindex'
import { TSelectZtree } from '@/types/common'
import { lowerCase } from 'lodash'
const userStore = useUserStore()
defineOptions({
  name: 'AdTworderItemBelong'
})
const props = defineProps<{
  data?: Tworderitembelong[]
  /**
   * 新增按钮是否显示，true显示，false不显示
   */
  addshow?: boolean
  /**
   * 保存按钮是否显示，true显示，false不显示
   */
  bottomshow?: boolean
}>()
/** 导出返回数据 */
const emit = defineEmits<{
  worderitembelongdata: [data: Tworderitembelong[]]
}>()
/** 表格高度 */
const tableheight = ref(0)
/** 订单刊期归属表 */
const worderitembelong = ref<Tworderitembelong>({
  id: '',
  orderitemid: '',
  sordernum: '',
  scontractnum: '',
  createempid: userStore.user?.userid.toString(),
  createempname: userStore.user?.username.toString(),
  createtime: '',
  deptid: '',
  deptname: '',
  employid: '',
  employname: '',
  employtype: 0,
  archievementrate: 100,
  taskrate: 100,
  commissionrate: 100,
  bprimary: false,
  sremark: ''
})
const worderitembelonglsit = ref<Tworderitembelong[]>([])
const TwordwrFormRef = ref<FormInstance>()
/** 客户选择 */
const filesshow = ref(false)
/**
 * 修改行数据
 */
const handleUpdateRowindex = ref(0)
/**
 * 判断是修改还是删除：false是新增
 */
const addorup = ref(false)
/**
 * 校验设置参数
 */
const rules = reactive<FormRules<Tworderitembelong>>({
  employid: [{ required: true, message: '选择业务员', trigger: 'change' }]
})
/** 监听传入数值变化 */
watch(
  () => props.data?.length,
  (newValue, oldValue) => {
    console.log(newValue)
    if (newValue) {
      worderitembelonglsit.value = props.data!
    } else {
      worderitembelonglsit.value = []
    }
    employrepeat()
  },
  { deep: true }
)
onMounted(() => {
  if (props.data) {
    worderitembelonglsit.value = props.data!
  } else {
    worderitembelonglsit.value = []
  }
  console.log(props.data!)
  employrepeat()
  /**
   * 计算表格高度
   */
  tableheight.value = computTableHeight()
})
/** 新增------------------------------------ */
const handleAdd = () => {
  filesshow.value = true
}

/**
 * 两个数组对象合并去重
 */

function mergeAndRemoveDuplicates(arr1: [], arr2: []) {
  // 合并两个数组
  const mergedArr = [...arr1, ...arr2]

  // 去重
  const result = [...new Set(mergedArr)]

  return result
}
/**
 * 编辑的时候选择业务人员
 * @param val
 */
const onSelectionNewEmp = (val: TSelectZtree[]) => {
  console.log(val)
  /** 叠次版本 */
  const employname = val.map((item) => item.name).join(',')
  const employid = val.map((item) => item.id).join(',')
  const deptid = val.map((item) => item.parentId).join(',')
  const deptname = val.map((item) => item.parentName).join(',')
  worderitembelong.value.employid = employid
  worderitembelong.value.employname = employname
  worderitembelong.value.deptid = deptid
  worderitembelong.value.deptname = deptname
}
/**
 * 修改
 * @param row
 */
const handleUpdate = (index: number, row?: Tworderitembelong) => {
  handleUpdateRowindex.value = index
  addorup.value = true
  worderitembelong.value = row ? { ...row } : {}
}
/**
 * 保存
 */
const handleSave = () => {
  TwordwrFormRef.value?.validate((valid) => {
    if (valid) {
      if (addorup.value === false) {
        for (let i = 0; i < worderitembelonglsit.value.length; i++) {
          if (
            worderitembelonglsit.value[i].employid === worderitembelong.value.employid &&
            worderitembelonglsit.value[i].employtype === worderitembelong.value.employtype
          ) {
            modal.msgWarning('该业务员已存在')
            return
          }
        }
        if (worderitembelong.value.employtype === 0 && worderitembelong.value.bprimary === true) {
          worderitembelonglsit.value.forEach((item, index) => {
            if (item.bprimary === true && item.employtype === 0) {
              item.bprimary = false
            }
          })
          worderitembelonglsit.value.push(worderitembelong.value)
        } else if (
          worderitembelong.value.employtype === 1 &&
          worderitembelong.value.bprimary === true
        ) {
          worderitembelonglsit.value.forEach((item, index) => {
            if (item.bprimary === true && item.employtype === 1) {
              item.bprimary = false
            }
          })
          worderitembelonglsit.value.push(worderitembelong.value)
        } else if (
          worderitembelong.value.employtype === 2 &&
          worderitembelong.value.bprimary === true
        ) {
          worderitembelonglsit.value.forEach((item, index) => {
            if (item.bprimary === true && item.employtype === 2) {
              item.bprimary = false
            }
          })
          worderitembelonglsit.value.push(worderitembelong.value)
        } else {
          worderitembelonglsit.value.push(worderitembelong.value)
        }
        // worderitembelonglsit.value.push(worderitembelong.value)
        console.log(worderitembelonglsit.value)
        employrepeat()
        resetbelong()
        // filesshow.value = false
        emit('worderitembelongdata', worderitembelonglsit.value)

        console.log(worderitembelonglsit.value)
      } else if (addorup.value === true) {
        // worderitembelonglsit.value.splice(handleUpdateRowindex.value, 1, {
        //   ...worderitembelong.value
        // })
        if (worderitembelong.value.employtype === 0 && worderitembelong.value.bprimary === true) {
          worderitembelonglsit.value.forEach((item, index) => {
            if (item.bprimary === true && item.employtype === 0) {
              item.bprimary = false
            }
          })
          worderitembelonglsit.value.splice(handleUpdateRowindex.value, 1, {
            ...worderitembelong.value
          })
        } else if (
          worderitembelong.value.employtype === 1 &&
          worderitembelong.value.bprimary === true
        ) {
          worderitembelonglsit.value.forEach((item, index) => {
            if (item.bprimary === true && item.employtype === 1) {
              item.bprimary = false
            }
          })
          worderitembelonglsit.value.splice(handleUpdateRowindex.value, 1, {
            ...worderitembelong.value
          })
        } else if (
          worderitembelong.value.employtype === 2 &&
          worderitembelong.value.bprimary === true
        ) {
          worderitembelonglsit.value.forEach((item, index) => {
            if (item.bprimary === true && item.employtype === 2) {
              item.bprimary = false
            }
          })
          worderitembelonglsit.value.splice(handleUpdateRowindex.value, 1, {
            ...worderitembelong.value
          })
        } else {
          worderitembelonglsit.value.splice(handleUpdateRowindex.value, 1, {
            ...worderitembelong.value
          })
        }
        handleUpdateRowindex.value = 0
        // filesshow.value = false
        // employrepeat()
        resetbelong()
      }
    }
    // addorup.value = false
  })
}
/**
 * 遍历数组直客业务员时平均分配改业绩比例，除不尽的给主业务员
 */
const employrepeat = () => {
  const data = ref<Tworderitembelong[]>([])
  const datalost = ref<Tworderitembelong[]>([])
  for (let i = 0; i < worderitembelonglsit.value.length; i++) {
    if (worderitembelonglsit.value[i].employtype === 0) {
      data.value.push(worderitembelonglsit.value[i])
    } else {
      datalost.value.push(worderitembelonglsit.value[i])
    }
  }
  for (let j = 0; j < data.value.length; j++) {
    if (data.value.length >= 1) {
      data.value[j].archievementrate = parseFloat((100 / data.value.length).toFixed(2))
      // data.value[j].commissionrate = 100 / data.value.length
      // data.value[j].taskrate = 100 / data.value.length
    }
  }
  // 给最后一个
  // if (data.value.length >= 3) {
  //   data.value[data.value.length - 1].archievementrate =
  //     100 - data.value[0].archievementrate! * (data.value.length - 1)
  // }
  // 给主业务员
  if (data.value.length >= 3) {
    data.value.forEach((item, index) => {
      if (item.bprimary === true) {
        item.archievementrate = 100 - data.value[0].archievementrate! * (data.value.length - 1)
      }
    })
  }
  worderitembelonglsit.value = data.value.concat(datalost.value)
}
/**
 * 取消
 */
const handleCancel = () => {
  TwordwrFormRef.value?.resetFields()
  resetbelong()
  setTimeout(() => {
    TwordwrFormRef.value?.clearValidate(['employid'])
  }, 10)
  addorup.value = false
  filesshow.value = false
}
/**
 * 删除
 * @param row
 */
const handleDelete = (index: number, row?: Tworderitembelong) => {
  worderitembelonglsit.value.splice(index, 1)
  employrepeat()
  emit('worderitembelongdata', worderitembelonglsit.value)
}
/**
 * 关闭是清空数据
 */
const resetbelong = () => {
  worderitembelong.value = {
    id: '',
    orderitemid: '',
    sordernum: '',
    scontractnum: '',
    createempid: userStore.user?.userid.toString(),
    createempname: userStore.user?.username.toString(),
    createtime: '',
    deptid: '',
    deptname: '',
    employid: '',
    employname: '',
    employtype: 0,
    archievementrate: 100,
    taskrate: 100,
    commissionrate: 100,
    bprimary: false,
    sremark: ''
  }
  setTimeout(() => {
    TwordwrFormRef.value?.clearValidate(['employid'])
  }, 10)
}
</script>

<style scoped>
ul {
  padding-left: 10px;
}
.showSearch span {
  color: #606266;
  font-size: 14px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.text {
  font-size: 14px;
}

.item {
  margin-bottom: 0px;
}

.box-card {
  width: 280px;
  max-height: 600px;
  overflow-y: auto;
}
.el-form-item {
  margin-bottom: 10px;
}
</style>
