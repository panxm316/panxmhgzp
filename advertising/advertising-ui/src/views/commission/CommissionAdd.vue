<!--
 * @Author: suny
 * @Date: 2024-03-16 13:58:53
 * @LastEditTime: 2024-03-18 10:44:39
 * @LastEditors: suny
 * @Description: 手动添加佣金计提
-->
<template>
  <div>
    <div class="search_box">
      <div class="flex">
        <el-button type="primary" icon="Plus" @click="handleAdd">新增</el-button>
        <HgDateIndex :initdate="timedata" @onresponse="ontime"></HgDateIndex>
        <HgZtreeSelect
          :scopeflag="EHgDeptZtreeUrl.DEPT_GETDEPTTREE"
          :selectids="queryParams.deptid ? [queryParams.deptid] : []"
          style="width: 160px; height: 32px"
          :filterable="false"
          clearable
          :treeparams="{ showInactiveDepts: false }"
          @selectionztree="onSelectionDept"
        ></HgZtreeSelect>
        <el-checkbox
          v-model="queryParams.bcountflag"
          label="包含已计算"
          size="large"
          style="margin-right: 20px; margin-top: -5px"
          @change="handleQuery"
        />
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
      </div>
    </div>
    <div class="table_box">
      <el-table
        ref="dataTableRef"
        :data="orderItemList"
        highlight-current-row
        :height="tableheight"
        :header-cell-style="tableHeaderColor"
        :border="true"
      >
        <el-table-column label="客户" prop="customername" min-width="160" show-overflow-tooltip />
        <el-table-column label="部门" prop="deptname" width="120" show-overflow-tooltip />
        <el-table-column label="主业务员" prop="employname" width="120" show-overflow-tooltip />
        <el-table-column label="业务员类型" prop="employtype" width="120" show-overflow-tooltip
          ><template #default="scope">
            {{ customerTypeCombo.find((item) => item.id === scope.row.employtype)?.name }}
          </template></el-table-column
        >
        <el-table-column label="创建日期" prop="dcreatetime" width="120">
          <template #default="scope">
            <span>{{ dayjs(scope.row.dcreatetime).format('YYYY-MM-DD') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="业绩金额" prop="amountachievement" min-width="120" align="right">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.amountachievement, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="业绩比例" prop="archievementrate" min-width="120" align="center">
          <template #default="scope">
            <span>{{ scope.row.archievementrate }}%</span>
          </template>
        </el-table-column>
        <el-table-column label="成本金额" prop="namountcost" min-width="120" align="right">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.namountcost, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="风险金比例" prop="nrateofrisk" min-width="100" align="center">
          <template #default="scope">
            <span>{{ scope.row.nrateofrisk }}%</span>
          </template>
        </el-table-column>
        <el-table-column
          label="计提比例"
          prop="ncommissionrate"
          min-width="140"
          align="center"
          fixed="right"
        >
          <template #default="scope">
            <div>{{ scope.row.ncommissionrate }}%</div>
          </template>
        </el-table-column>
        <el-table-column
          label="计提金额"
          prop="ncommission"
          min-width="120"
          align="right"
          fixed="right"
        >
          <template #default="scope">
            <span>{{ formatMoney(scope.row.ncommission, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
          align="center"
          width="140"
          fixed="right"
          class-name="small-padding fixed-width"
        >
          <template #default="scope">
            <el-button
              v-if="scope.row.icommissionstatus === ECommissionStatus.待提交"
              link
              type="success"
              icon="Edit"
              size="small"
              title="修改"
              @click="onEdit(scope.row)"
              >修改</el-button
            >
            <el-button
              v-if="scope.row.icommissionstatus === ECommissionStatus.待提交"
              link
              type="primary"
              icon="Top"
              size="small"
              title="提交审核"
              @click="onToCheck(scope.row)"
              >提交</el-button
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
    <!-- 编辑窗口 -->
    <el-dialog
      v-model="editShow"
      title="佣金录入"
      width="800"
      append-to-body
      destroy-on-close
      :close-on-click-modal="false"
      @close="onCancel"
    >
      <el-form
        ref="CommissionFormRef"
        :model="CommissionForm"
        label-width="120px"
        :inline="true"
        style="padding-right: 30px"
      >
        <el-form-item label="合同号" prop="scontractnum">
          <el-input v-model="CommissionForm!.scontractnum" class="inputwindht" resize="none" />
        </el-form-item>
        <el-form-item label="业务员" prop="employid">
          <HgZtreeSelect
            :scopeflag="EHgDeptZtreeUrl.EMP_GETDEPTEMPLOYPERSON"
            :selectids="CommissionForm!.employid ? [CommissionForm!.employid] : []"
            style="width: 200px"
            :filterable="true"
            :treeparams="{ bIgnorePermissions: true }"
            @selectionztree="onSelectionNewEmp"
          ></HgZtreeSelect>
        </el-form-item>
        <el-form-item label="业务员类型" prop="employtype">
          <el-select v-model="CommissionForm!.employtype" placeholder="请选择" style="width: 200px">
            <el-option
              v-for="item in employeeTypeList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="是否主业务员" prop="bprimary">
          <el-checkbox v-model="CommissionForm.bprimary"></el-checkbox>
        </el-form-item>
        <el-form-item label="客户选择" prop="customerid">
          <el-row style="width: 300px">
            <el-col :span="23">
              <el-input
                v-model="CommissionForm!.customername"
                placeholder="请选择客户信息"
                class="inputwindht"
                @input="CommissionForm!.customerid = ''"
              >
                <template #append>
                  <el-button
                    icon="Search"
                    circle
                    title="选择客户"
                    @click="dialogCustomerVisible = true"
                  />
                </template>
                <template #suffix>
                  <el-button
                    v-show="CommissionForm!.customername"
                    link
                    icon="CircleClose"
                    @click="SeeCustomerdelete()"
                  ></el-button>
                </template>
              </el-input>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item label="业绩金额" prop="amountachievement">
          <el-input-number
            v-model="CommissionForm!.amountachievement"
            :precision="2"
            :step="0.1"
            :min="0"
            :controls="false"
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="业绩比例" prop="archievementrate">
          <el-input-number
            v-model="CommissionForm!.archievementrate"
            :precision="1"
            :step="0.1"
            :min="0"
            :max="100"
            :controls="false"
            style="width: 200px"
          />
          <span style="margin-left: 10px">%</span>
        </el-form-item>
        <el-form-item label="成本金额" prop="namountcost">
          <el-input-number
            v-model="CommissionForm!.namountcost"
            :precision="2"
            :step="0.1"
            :min="0"
            :controls="false"
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="风险金比例" prop="nrateofrisk">
          <el-input-number
            v-model="CommissionForm!.nrateofrisk"
            :precision="1"
            :step="0.1"
            :min="0"
            :max="100"
            :controls="false"
            style="width: 200px"
          />
          <span style="margin-left: 10px">%</span>
        </el-form-item>
        <el-form-item label="计提金额" prop="ncommission">
          <el-input-number
            v-model="CommissionForm!.ncommission"
            :precision="2"
            :step="0.1"
            :min="0"
            :controls="false"
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="计提比例" prop="ncommissionrate">
          <el-input-number
            v-model="CommissionForm!.ncommissionrate"
            :precision="1"
            :step="0.1"
            :min="0"
            :max="100"
            :controls="false"
            style="width: 200px"
          />
          <span style="margin-left: 10px">%</span>
        </el-form-item>
        <div style="width: 100%; text-align: right; margin-top: 10px">
          <el-button type="primary" style="margin-left: 20px" @click="onAddSave">保存</el-button>
          <el-button style="margin-left: 20px" @click="onCancel">取消</el-button>
        </div>
      </el-form>
      <!-- 选择客户 -->
      <el-dialog
        v-model="dialogCustomerVisible"
        title="选择客户"
        width="1000"
        align-center
        :close-on-click-modal="false"
        :destroy-on-close="true"
      >
        <el-row :gutter="50">
          <el-col :span="24">
            <HgSalesCustomer @selectiondata="selectCustomer"></HgSalesCustomer>
          </el-col>
        </el-row>
      </el-dialog>
    </el-dialog>
  </div>
</template>
<script setup lang="ts">
import modal from '@/plugins/modal'
import { TDateTimeType, TDateType } from '@/types/components/hgdateindex'
import { IAxios } from 'axios'
import {
  ECommissionStatus,
  EPublishstatus,
  ECustomerType,
  EHgDeptZtreeUrl,
  EEmployeeType
} from '@/types/common/enumindex'
import { computTableHeight, tableHeaderColor, formatMoney } from '@/utils'
import { FormInstance, dayjs } from 'element-plus'
import { IDataCombo } from '@/types/common/DataCombo'
import { getMediaDataComboApi } from '@/api/media/media'
import { getEnumCombo } from '@/api/common/index'
import { TSelectZtree } from '@/types/common'
import { TOrderItemCommissionVO, ICommissionDTO } from '@/types/views/commission/commissioncalcu'
import { TAdCustomer } from '@/types/views/customer'
import { getAddCommissionListApi, saveAddCommissionApi } from '@/api/commission/commissioncalcu'

const CommissionFormRef = ref<FormInstance>()
/** 客户类型列表 */
const customerTypeCombo: IDataCombo[] = getEnumCombo(ECustomerType)
/** 业务员类型下拉列表 */
const employeeTypeList: IDataCombo[] = getEnumCombo(EEmployeeType)
// const user = useUserStore().user
const total = ref(0)
/** 编辑显示状态 */
const editShow = ref(false)
/** 表格高度 */
const tableheight = ref(0)
/** 每页选择下拉 */
const pageSizes = ref<number[]>([10, 20, 30, 40])
/** 时间 */
const timedata = reactive<TDateType>({
  timetype: '6',
  timeoption: [
    {
      label: '今天',
      value: '0'
    },
    {
      label: '三天内',
      value: '2'
    },
    {
      label: '一周内',
      value: '6'
    },
    {
      label: '二周内',
      value: '13'
    },
    {
      label: '一月内',
      value: '30'
    },
    {
      label: '自定义',
      value: '00'
    }
  ],
  startTime: '',
  endTime: ''
})
/** 查询条件 */
const queryParams = reactive<TOrderItemCommissionVO>({
  sort: 'dcreatetime',
  order: 'desc',
  page: 1,
  pageSize: 20,
  startTime: '',
  endTime: '',
  deptid: '',
  employid: '',
  icommissionstatus: undefined
})
/** 列表对象 */
const orderItemList = ref<ICommissionDTO[]>([])
/** 媒体列表 */
const mediaCombo = ref<IDataCombo[]>([])
const CommissionInfo: ICommissionDTO = {
  deptid: '',
  deptname: '',
  employid: '',
  employname: '',
  employtype: EEmployeeType.直客业务员,
  customerid: '',
  customername: '',
  amountachievement: 0,
  archievementrate: 0,
  namountcost: 0,
  nrateofrisk: 0,
  ncommissionrate: 100,
  ncommission: 0,
  icommissionstatus: 0,
  bprimary: false
} as ICommissionDTO
/** 编辑窗口对象 */
const CommissionForm = ref<ICommissionDTO>({ ...CommissionInfo })
const dialogCustomerVisible = ref(false)

onMounted(() => {
  getMediaDataCombo()
  handleQuery()
  /**
   * 计算表格高度
   */
  tableheight.value = computTableHeight()
})
/**
 * 获取媒体下拉数据
 */
const getMediaDataCombo = () => {
  getMediaDataComboApi().then((res: IAxios<IDataCombo[]>) => {
    if (res.success) {
      mediaCombo.value = res.obj
    }
  })
}
/**
 * 查询
 */
const handleQuery = () => {
  getAddCommissionListApi(queryParams)
    .then((res: IAxios<ICommissionDTO[]>) => {
      if (res.success) {
        orderItemList.value = res.obj
        console.log('orderItemList', orderItemList.value)
        total.value = res.total
      }
    })
    .finally(() => {})
  tableheight.value = computTableHeight()
}
/**
 * 新增
 */
const handleAdd = () => {
  resetForm(CommissionFormRef.value)
  CommissionForm.value = { ...CommissionInfo }
  editShow.value = true
}
/**
 * 编辑
 * @param val
 */
const onEdit = (row: ICommissionDTO) => {
  CommissionForm.value = { ...row }
  editShow.value = true
}
/**
 * 取消编辑
 */
const onCancel = () => {
  editShow.value = false
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
  CommissionForm.value!.employid = employid
  CommissionForm.value!.employname = employname
  CommissionForm.value!.deptid = deptid
  CommissionForm.value!.deptname = deptname
}
/**
 * 选择客户信息后返回接收
 * @param val
 */
const selectCustomer = (val: TAdCustomer) => {
  CommissionForm.value!.customerid = val.id
  CommissionForm.value!.customername = val.sname
  dialogCustomerVisible.value = false
}
/** 修改时删除客户列表信息 */
const SeeCustomerdelete = () => {
  CommissionForm.value!.customername = ''
  CommissionForm.value!.customerid = ''
}
/**
 * 保存新增
 */
const onAddSave = () => {
  if (CommissionForm.value.employid === '') {
    ElMessage.error('请选择业务员')
    return false
  }
  if (CommissionForm.value.ncommission === 0) {
    ElMessage.error('请添加计提金额')
    return false
  }
  saveAddCommissionApi(CommissionForm.value)
    .then((res: IAxios) => {
      if (res.success) {
        editShow.value = false
        handleQuery()
      }
    })
    .finally(() => {})
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
 * 选择部门
 * @param val
 */
const onSelectionDept = (val: TSelectZtree[]) => {
  const deptid = val.map((item) => item.id).join(',')
  queryParams.deptid = deptid
}
/**
 * 提交数据
 * @param row
 */
const onToCheck = (row: ICommissionDTO) => {
  CommissionForm.value = { ...row }
  modal
    .confirm('是否提交审核？')
    .then(() => {
      CommissionForm.value.icommissionstatus = ECommissionStatus.计算
      saveAddCommissionApi(CommissionForm.value)
        .then((res: IAxios) => {
          if (res.success) {
            handleQuery()
          }
        })
        .catch(() => {})
    })
    .catch(() => {})
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
/**
 * 时间选择
 * @param val
 */
const ontime = (val: TDateTimeType) => {
  let timev = false
  if (queryParams.startTime) {
    timev = true
  }
  queryParams.startTime = val.startTime
  queryParams.endTime = val.endTime
  if (timev) {
    handleQuery()
  }
}
</script>
<style lang="scss" scoped></style>
