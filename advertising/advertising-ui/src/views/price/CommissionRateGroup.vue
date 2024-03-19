<!--
 * @Author: suny
 * @Date: 2023-11-29 12:49:54
 * @LastEditTime: 2024-01-17 15:01:38
 * @LastEditors: wanghl
 * @Description: 计提比例总表
-->
<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex">
        <el-button type="primary" icon="Plus" @click="handleAdd">新增</el-button>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
      </div>
    </div>
    <div class="table_box">
      <el-table
        ref="dataTableRef"
        :data="commissionRateGroupList"
        highlight-current-row
        :height="tableheight"
        :header-cell-style="tableHeaderColor"
        border
      >
        <el-table-column
          label="名称"
          prop="commissionrategroupname"
          min-width="160"
          show-overflow-tooltip
        />
        <el-table-column label="开始日期" prop="dstartdate" min-width="100">
          <template #default="scope">
            <span>{{ dayjs(scope.row.dstartdate).format('YYYY-MM-DD') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="直接客户" prop="nrateofcustomer" align="center" min-width="100">
          <template #default="scope"> {{ scope.row.nrateofcustomer }}% </template>
        </el-table-column>
        <el-table-column label="代理公司" prop="nrateofagency" align="center" min-width="100">
          <template #default="scope"> {{ scope.row.nrateofagency }}% </template>
        </el-table-column>
        <el-table-column label="内容生产方" prop="nrateofagent" align="center" min-width="100">
          <template #default="scope"> {{ scope.row.nrateofagent }}% </template>
        </el-table-column>
        <el-table-column label="风险金比例" prop="nrateofrisk" align="center" min-width="100">
          <template #default="scope"> {{ scope.row.nrateofrisk }}% </template>
        </el-table-column>
        <el-table-column label="创建者名称" prop="createempname" align="center" min-width="100" />
        <el-table-column label="创建日期" prop="createdate" align="center" min-width="100">
          <template #default="scope">
            <span>{{ dayjs(scope.row.createdate).format('YYYY-MM-DD') }}</span>
          </template>
        </el-table-column>

        <el-table-column label="启用" prop="buse" :width="60" align="center">
          <template #default="scope">
            <el-checkbox v-model="scope.row.buse" disabled></el-checkbox>
          </template>
        </el-table-column>
        <el-table-column prop="bdefault" label="默认" :width="60" align="center">
          <template #default="scope">
            <el-checkbox v-model="scope.row.bdefault" disabled></el-checkbox>
          </template>
        </el-table-column>
        <el-table-column
          label="备注"
          prop="sremark"
          align="center"
          min-width="100"
          show-overflow-tooltip
        />

        <el-table-column
          label="操作"
          align="left"
          width="280"
          class-name="small-padding fixed-width"
          fixed="right"
        >
          <template #default="scope">
            <el-button
              link
              style="margin-left: 10px"
              type="success"
              icon="Edit"
              size="small"
              title="修改"
              @click="onEdit(scope.row)"
              >修改</el-button
            >
            <el-button
              link
              style="margin-left: 10px"
              type="success"
              icon="Setting"
              size="small"
              title="明细设置"
              @click="onSet(scope.row)"
              >明细</el-button
            >
            <el-button
              link
              style="margin-left: 10px"
              type="success"
              icon="Money"
              size="small"
              title="折扣降点"
              @click="onDiscount(scope.row)"
              >降点</el-button
            >
            <el-button
              link
              style="margin-left: 10px"
              type="primary"
              icon="DocumentCopy"
              size="small"
              title="复制"
              @click="onCopy(scope.row)"
              >复制</el-button
            >
            <el-button
              link
              type="danger"
              icon="Delete"
              size="small"
              title="删除"
              @click="onDelete(scope.row)"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        v-model:current-page="queryParams.page"
        class="pagination"
        style="margin-left: 10px; width: 100%"
        :page-sizes="pageSizes"
        :page-size="queryParams.pageSize"
        small
        background
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
    <!-- 编辑 -->
    <el-dialog
      v-model="dialogEditVisible"
      title="计提比例"
      style="width: 800px"
      append-to-body
      :destroy-on-close="true"
    >
      <div>
        <el-row style="padding-right: 50px">
          <el-col>
            <el-form
              ref="commissionRateGroupRef"
              :model="commissionRateGroupForm"
              :rules="rules"
              label-width="160px"
              class="demo-workReportForm"
              status-icon
            >
              <el-form-item label="名称" prop="commissionrategroupname">
                <el-input
                  v-model="commissionRateGroupForm.commissionrategroupname"
                  class="inputwindht"
                />
              </el-form-item>
              <el-form-item prop="dstartdate" label="开始日期" required>
                <el-date-picker
                  v-model="commissionRateGroupForm.dstartdate"
                  type="date"
                  placeholder="开始日期"
                  :clearable="false"
                  value-format="YYYY-MM-DD"
                  style="width: 262px"
                />
              </el-form-item>
              <el-form-item label="直接客户默认提成" prop="nrateofcustomer">
                <el-input-number
                  v-model="commissionRateGroupForm.nrateofcustomer"
                  :controls="false"
                  :min="0"
                  :max="100"
                  style="width: 245px; padding-right: 10px"
                />%
              </el-form-item>
              <el-form-item label="代理公司默认提成" prop="nrateofagency">
                <el-input-number
                  v-model="commissionRateGroupForm.nrateofagency"
                  :controls="false"
                  :min="0"
                  :max="100"
                  style="width: 245px; padding-right: 10px"
                />%
              </el-form-item>
              <el-form-item label="内容生产方默认提成" prop="nrateofagent">
                <el-input-number
                  v-model="commissionRateGroupForm.nrateofagent"
                  :controls="false"
                  :min="0"
                  :max="100"
                  style="width: 245px; padding-right: 10px"
                />%
              </el-form-item>
              <el-form-item label="风险金默认比例" prop="nrateofrisk">
                <el-input-number
                  v-model="commissionRateGroupForm.nrateofrisk"
                  :controls="false"
                  :min="0"
                  :max="100"
                  style="width: 245px; padding-right: 10px"
                />%
              </el-form-item>
              <el-form-item label="备注" prop="sremark">
                <el-input
                  v-model="commissionRateGroupForm.sremark"
                  :rows="3"
                  type="textarea"
                  class="inputwindht"
                  resize="none"
                  placeholder="请输入..."
                />
              </el-form-item>
              <el-row :gutter="50">
                <el-col :span="4">
                  <el-form-item label="启用" prop="buse">
                    <el-checkbox v-model="commissionRateGroupForm.buse"></el-checkbox>
                  </el-form-item>
                </el-col>
                <el-col :span="4">
                  <el-form-item label="默认" prop="buse">
                    <el-checkbox v-model="commissionRateGroupForm.bdefault"></el-checkbox>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-form-item>
                <div style="position: absolute; right: 5px">
                  <el-button
                    type="primary"
                    icon="Check"
                    @click="saveCommissionRateGroupForm(commissionRateGroupRef)"
                    >保存</el-button
                  >
                  <el-button icon="Close" @click="dialogEditVisible = false">取消</el-button>
                </div>
              </el-form-item>
            </el-form>
          </el-col>
        </el-row>
      </div>
    </el-dialog>
    <!-- 明细设置 -->
    <el-dialog
      v-model="dialogSetVisible"
      title="明细设置"
      style="width: 1300px"
      append-to-body
      :close-on-click-modal="false"
      :destroy-on-close="true"
    >
      <div>
        <CommissionRateItem :commissionrategroup="currentrow!"></CommissionRateItem>
      </div>
    </el-dialog>
    <!-- 折扣降点设置 -->
    <el-dialog
      v-model="dialogReduceVisible"
      title="折扣降点设置"
      style="width: 1160px"
      append-to-body
      :close-on-click-modal="false"
      :destroy-on-close="true"
    >
      <div>
        <DiscountReduce :commissionrategroup="currentrow!"></DiscountReduce>
      </div>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { computTableHeight, required, tableHeaderColor } from '@/utils'
import { IAxios } from 'axios'
import dayjs from 'dayjs'
import { FormInstance, FormRules } from 'element-plus'
import {
  ITbcommissionrategroup,
  TCommissionRateGroupQuery
} from '@/types/views/price/commissionrategroup'
import {
  deleteCommissionRateGroupApi,
  getCommissionRateGroupPageListApi,
  saveCommissionRateGroupApi,
  updateCommissionRateGroupApi
} from '@/api/price/commissionrategroup'
import CommissionRateItem from './CommissionRateItem.vue'
import DiscountReduce from './DiscountReduce.vue'

const total = ref(0)
const sort = ref('createdate')
const order = ref('desc')
/** 编辑窗口显示状态 */
const dialogEditVisible = ref(false)
/** 设置窗口显示状态 */
const dialogSetVisible = ref(false)
/** 折扣降点窗口显示状态 */
const dialogReduceVisible = ref(false)
/** 每页选择下拉 */
const pageSizes = ref<number[]>([20, 40, 60, 100])
/** Form名称 */
const commissionRateGroupRef = ref<FormInstance>()
/** 查询条件 */
const queryParams = reactive<TCommissionRateGroupQuery>({
  page: 1,
  pageSize: 20,
  startTime: '',
  endTime: '',
  queryKey: ''
})
/** Form表单数据 */
const commissionRateGroupFormInfo: ITbcommissionrategroup = {
  id: '',
  commissionrategroupname: '',
  nrateofagent: 0,
  nrateofagency: 0,
  nrateofcustomer: 0,
  nrateofrisk: 0,
  buse: true,
  bdefault: true,
  dstartdate: dayjs().format('YYYY-MM-DD')
}
/** Form表单数据 */
const commissionRateGroupForm = ref<ITbcommissionrategroup>({ ...commissionRateGroupFormInfo })
/** 表格高度 */
const tableheight = ref(0)

/** 列表对象 */
const commissionRateGroupList = ref<ITbcommissionrategroup[]>([])
/** Form选中的行 */
const multipleSelection = ref<ITbcommissionrategroup[]>([])
/** 当前行 */
const currentrow = ref<ITbcommissionrategroup>()
/** 复制标识 */
const copyFlag = ref(false)
/**
 * 校验设置参数
 */
const rules = reactive<FormRules<ITbcommissionrategroup>>({
  commissionrategroupname: [
    { validator: required, required: true, message: '请填写名称', trigger: 'blur' }
  ],
  dstartdate: [
    { validator: required, required: true, message: '请选择开始日期', trigger: 'change' }
  ]
})
onMounted(() => {
  handleQuery()
  /**
   * 计算表格高度
   */
  tableheight.value = computTableHeight()
})

/**
 * 查询
 */
const handleQuery = () => {
  const data = {
    sort: sort.value,
    order: order.value,
    ...queryParams
  }
  getCommissionRateGroupPageListApi(data)
    .then((res: IAxios<ITbcommissionrategroup[]>) => {
      if (res.success) {
        commissionRateGroupList.value = res.obj
        total.value = res.total
      }
    })
    .finally(() => {})
}
/**
 * 新增
 */
const handleAdd = () => {
  resetForm(commissionRateGroupRef.value)
  commissionRateGroupForm.value = { ...commissionRateGroupFormInfo }
  dialogEditVisible.value = true
  copyFlag.value = false
}
/**
 * 编辑按钮事件
 * @param row
 */
const onEdit = (row: ITbcommissionrategroup) => {
  dialogEditVisible.value = true
  commissionRateGroupForm.value = { ...row }
  copyFlag.value = false
}
/**
 * 明细设置按钮事件
 * @param row
 */
const onSet = (row: ITbcommissionrategroup) => {
  dialogSetVisible.value = true
  currentrow.value = { ...row }
}
/**
 * 折扣降点设置按钮事件
 * @param row
 */
const onDiscount = (row: ITbcommissionrategroup) => {
  dialogReduceVisible.value = true
  currentrow.value = { ...row }
}
/**
 * 复制按钮事件
 * @param row
 */
const onCopy = (row: ITbcommissionrategroup) => {
  dialogEditVisible.value = true
  copyFlag.value = true
  commissionRateGroupForm.value = { ...row }
}
/**
 * 删除按钮事件
 * @param row
 */
const onDelete = (row: ITbcommissionrategroup | undefined) => {
  const ids: string[] = []
  let bdelflag = false
  if (row === undefined) {
    multipleSelection.value.forEach((item) => {
      if (item.bdefault) {
        bdelflag = true
        return
      }
      ids.push(item.id?.toString() as string)
    })
  } else {
    if (row.bdefault) {
      ElMessage.warning('默认数据不能删除')
      return
    }
    ids.push(row.id?.toString() as string)
  }
  if (bdelflag) {
    ElMessage.warning('默认数据不能删除')
    return
  }
  if (ids.length === 0) {
    ElMessage.warning('请选择要删除的数据')
    return
  }
  const data = {
    ids: ids.join(',')
  }
  ElMessageBox.confirm('是否删除选择的数据？', '提示', {
    type: 'warning'
  })
    .then(() => {
      deleteCommissionRateGroupApi(data)
        .then((res: IAxios) => {
          if (res.success) {
            ElMessage.success('删除成功!')
            handleQuery()
          }
        })
        .catch(() => {})
    })
    .catch(() => {})
}
/**
 * 编辑保存
 * @param formEl
 */
const saveCommissionRateGroupForm = async (formEl: FormInstance | undefined) => {
  if (!formEl) return
  await formEl.validate((valid, fields) => {
    if (valid) {
      if (commissionRateGroupForm.value.bdefault && !commissionRateGroupForm.value.buse) {
        ElMessage.warning('默认数据不能禁用')
        return
      }
      if (commissionRateGroupForm.value.id === '' || copyFlag.value) {
        const data = {
          ...commissionRateGroupForm.value,
          copyFlag: copyFlag.value
        }
        saveCommissionRateGroupApi(data)
          .then((res: IAxios) => {
            if (res.success) {
              ElMessage.success('保存成功')
              handleQuery()
              commissionRateGroupRef.value?.resetFields()
              dialogEditVisible.value = false
            }
          })
          .catch(() => {})
      } else {
        updateCommissionRateGroupApi(commissionRateGroupForm.value)
          .then((res: IAxios) => {
            if (res.success) {
              ElMessage.success('更新保存成功')
              handleQuery()
              commissionRateGroupRef.value?.resetFields()
              dialogEditVisible.value = false
            }
          })
          .catch(() => {})
      }
    } else {
      console.log('error submit!', fields)
    }
  })
}
/**
 * 重置form表单
 * @param formEl
 */
const resetForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.resetFields()
}
/**
 * 改变页码总数时
 */
const handleSizeChange = (val: number) => {
  queryParams.pageSize = val
  handleQuery()
}
/**
 * 改变页数时
 * @param val
 */
const handleCurrentChange = (val: number) => {
  queryParams.page = val
  handleQuery()
}
</script>

<style lang="scss" scoped></style>
