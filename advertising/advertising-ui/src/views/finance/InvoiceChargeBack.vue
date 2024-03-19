<!--
 * @Author: suny
 * @Date: 2023-12-23 15:28:50
 * @LastEditTime: 2024-03-06 13:37:22
 * @LastEditors: wanghl
 * @Description: 核销退回
 核销回退
1、根据查询条件：时间、发票号等，查询分摊列表（与预收款核销中历史一个方法也可）
（合同号、经营主体、直接客户、代理公司、内容生产方、刊发日期、媒体、广告标题、签订金额、分摊金额、发票号、收费日期、分摊日期、收款种类、业务员）

2、回退
参数：分摊表ids
流程：根据分摊表ids，找到需要回退的分摊表list——》每一条分摊需要根据orderitemid，找到对应的订单详情，将分摊结果回退，修改已收、欠款字段——》根据收费表id，找到收费表数据，将状态、已收金额、剩余金额分别回退——》删除分摊表该记录，同时插入分摊历史表twinvoicebackhistory
-->
<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex">
        <!-- <el-button type="primary" icon="Back" @click="handleBack()">撤销</el-button> 需要提示相关佣金信息，所以不允许批量 -->
        <HgDateIndex :initdate="timedata" @onresponse="ontimeCharge"></HgDateIndex>
        <el-input
          v-model="queryChargeParams.queryKey"
          clearable
          placeholder="请输入发票号"
          class="input-with-select"
          style="width: 200px"
          @keyup.enter="handleQuery"
          @clear="handleQuery"
        >
        </el-input>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
      </div>
    </div>
    <div class="table_box">
      <el-table
        ref="orderApportiondetailTableRef"
        :data="orderApportiondetailList"
        highlight-current-row
        :height="tableheight"
        :header-cell-style="tableHeaderColor"
        :border="true"
        @selection-change="handleSelectionChange"
      >
        <!-- <el-table-column type="selection" width="50" :align="'center'" /> -->
        <el-table-column
          label="合同号"
          prop="scontractnum"
          min-width="140"
          align="center"
          show-overflow-tooltip
        >
          <template #default="scope">
            <el-button
              link
              type="primary"
              icon="view"
              size="small"
              @click="handleSee(scope.row, scope.$index)"
              >{{ scope.row.scontractnum }}</el-button
            >
          </template>
        </el-table-column>
        <el-table-column
          label="经营主体"
          prop="businessentityname"
          min-width="140"
          show-overflow-tooltip
        />
        <el-table-column
          label="直接客户"
          prop="customername"
          min-width="120"
          show-overflow-tooltip
        />
        <el-table-column label="代理公司" prop="agencyname" min-width="120" show-overflow-tooltip />
        <el-table-column
          label="内容生产方"
          prop="agentname"
          min-width="120"
          show-overflow-tooltip
        />
        <el-table-column label="刊发日期" prop="prestartdate" width="100">
          <template #default="scope">
            <span>{{ dayjs(scope.row.prestartdate).format('YYYY-MM-DD') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="媒体" prop="medianame" min-width="100" show-overflow-tooltip />
        <el-table-column label="广告标题" prop="sadtitle" min-width="120" show-overflow-tooltip />
        <el-table-column
          label="签订金额"
          prop="amountreceivable"
          align="right"
          min-width="120"
          show-overflow-tooltip
        >
          <template #default="scope">
            <span>{{ formatMoney(scope.row.amountreceivable, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="分摊金额"
          prop="namountapportion"
          align="right"
          min-width="120"
          show-overflow-tooltip
        >
          <template #default="scope">
            <span>{{ formatMoney(scope.row.namountapportion, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="发票号" prop="invoice" min-width="140" show-overflow-tooltip />
        <el-table-column label="收费日期" prop="dpaydate" width="120">
          <template #default="scope">
            <span>{{ dayjs(scope.row.dpaydate).format('YYYY-MM-DD') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="分摊日期" prop="dapportiondate" width="120">
          <template #default="scope">
            <span>{{ dayjs(scope.row.dapportiondate).format('YYYY-MM-DD') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="收款种类" prop="sapportiontype" width="120">
          <template #default="scope">
            {{ stypeCombo.find((item) => item.id === scope.row.sapportiontype)?.name }}
          </template>
        </el-table-column>
        <el-table-column label="欠款金额" prop="amountarrearage" width="100" align="right">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.amountarrearage, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
          width="100"
          :align="'center'"
          class-name="small-padding fixed-width"
          fixed="right"
        >
          <template #default="scope">
            <el-button
              link
              type="primary"
              icon="Back"
              size="small"
              title="撤销"
              @click="handleBeforeBack(scope.row)"
              >撤销</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        v-model:current-page="queryChargeParams.page"
        class="pagination"
        style="margin-left: 10px; width: 100%"
        :page-sizes="pageSizes"
        :page-size="queryChargeParams.pageSize"
        small
        background
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
    <!-- 撤销 -->
    <el-dialog
      v-model="dialogBackshow"
      title="撤销"
      width="800"
      append-to-body
      destroy-on-close
      draggable
      :close-on-click-modal="false"
    >
      <el-row style="padding: 0 20px">
        <el-col :span="24">
          <el-form label-width="80px" label-position="right">
            <el-form-item label="撤销原因">
              <el-input
                v-model="backreason"
                type="textarea"
                :rows="4"
                maxlength="200"
                show-word-limit
                placeholder="请输入撤销原因"
              ></el-input>
            </el-form-item>
            <div style="width: 100%; text-align: right">
              <el-button type="primary" icon="Check" @click="saveBackForm">撤销</el-button>
              <el-button icon="Close" @click="dialogBackshow = false">取消</el-button>
            </div>
          </el-form>
        </el-col>
      </el-row>
    </el-dialog>
    <!-- 佣金信息 -->
    <el-dialog
      v-model="dialogCommissionshow"
      title="佣金信息"
      width="1200"
      append-to-body
      destroy-on-close
      draggable
      :close-on-click-modal="false"
    >
      <el-row style="padding: 0 20px">
        <el-col :span="24">
          <el-table
            ref="commissionListTableRef"
            :data="commissionList"
            style="min-height: 200px"
            highlight-current-row
            :header-cell-style="tableHeaderColor"
            :border="true"
          >
            <el-table-column label="部门" prop="deptname" min-width="120" show-overflow-tooltip />
            <el-table-column
              label="业务员"
              prop="employname"
              min-width="140"
              show-overflow-tooltip
            />
            <el-table-column label="业务员类型" prop="employtype" width="120" show-overflow-tooltip>
              <template #default="scope">
                {{ employTypeCombo.find((item) => item.id === scope.row.employtype)?.name }}
              </template></el-table-column
            >
            <el-table-column
              label="客户"
              prop="customername"
              min-width="120"
              show-overflow-tooltip
            />
            <el-table-column
              label="业绩金额"
              prop="amountachievement"
              min-width="120"
              align="right"
            >
              <template #default="scope">
                <span>{{ formatMoney(scope.row.amountachievement, '2') }}</span>
              </template>
            </el-table-column>
            <el-table-column
              label="业绩比例"
              prop="archievementrate"
              min-width="120"
              align="center"
            >
              <template #default="scope">
                <span>{{ scope.row.archievementrate }}%</span>
              </template>
            </el-table-column>
            <el-table-column label="成本金额" prop="namountcost" min-width="120" align="right">
              <template #default="scope">
                <span>{{ formatMoney(scope.row.namountcost, '2') }}</span>
              </template>
            </el-table-column>
            <el-table-column label="计提比例" prop="ncommissionrate" min-width="140" align="center">
              <template #default="scope">
                <div>{{ scope.row.ncommissionrate }}%</div>
              </template>
            </el-table-column>
            <el-table-column label="计提金额" prop="ncommission" min-width="120" align="right">
              <template #default="scope">
                <span>{{ formatMoney(scope.row.ncommission, '2') }}</span>
              </template>
            </el-table-column>
            <el-table-column label="计提状态" prop="icommissionstatus" width="120">
              <template #default="scope">
                {{
                  commissionStatusCombo.find((item) => item.id === scope.row.icommissionstatus)
                    ?.name
                }}
              </template>
            </el-table-column>
          </el-table>
          <div style="width: 100%; text-align: right">
            <el-button type="primary" icon="Back" @click="handleBack()">确认撤销</el-button>
            <el-button icon="Close" @click="dialogCommissionshow = false">取消</el-button>
          </div>
        </el-col>
      </el-row>
    </el-dialog>
    <!-- 根据合同号查看详情 -->
    <AdTworderdetails ref="viewImageRef"></AdTworderdetails>
  </div>
</template>
<script setup lang="ts">
import { ESType, ECustomerType, ECommissionStatus } from '@/types/common/enumindex'
import { TDateTimeType, TDateType } from '@/types/components/hgdateindex'
import {
  IOrderApportiondetailDTO,
  TOrderApportiondetailVO
} from '@/types/views/finance/orderapportiondetail'
import { IAxios } from 'axios'
import { reactive, ref } from 'vue'
import { computTableHeight, tableHeaderColor, formatMoney } from '@/utils'
import { FormInstance, dayjs, ElTable } from 'element-plus'
import {
  getOrderApportiondetailPageListApi,
  revertWriteOffApi,
  getCommissionListByItemIdApi
} from '@/api/finance/advancecharge'
import { IDataCombo } from '@/types/common/DataCombo'
import { getEnumCombo } from '@/api/common/index'
import { TOrderItemCommissionDTO } from '@/types/views/commission/commissioncalcu'
/** 收款类型下拉 */
const stypeCombo: IDataCombo[] = getEnumCombo(ESType)
/** 业务员类型列表 */
const employTypeCombo: IDataCombo[] = getEnumCombo(ECustomerType)
/** 计提状态下拉列表 */
const commissionStatusCombo: IDataCombo[] = getEnumCombo(ECommissionStatus)
const total = ref(0)
/** 表格高度 */
const tableheight = ref(0)
/** 每页选择下拉 */
const pageSizes = ref<number[]>([10, 20, 30, 40])
/** 撤销原因 */
const backreason = ref('')
/** 撤销原因窗口显示状态 */
const dialogBackshow = ref(false)
/** 佣金信息列表显示状态 */
const dialogCommissionshow = ref(false)
/** 当前已分摊列表详情ids */
const orderApportiondetailIds = ref('')

/** 查询条件 */
const queryChargeParams = reactive<TOrderApportiondetailVO>({
  page: 1,
  pageSize: 20,
  startTime: '',
  endTime: ''
})
/** 当前已分摊列表详情数组 */
const orderApportiondetailList = ref<IOrderApportiondetailDTO[]>([])
/** 选中的行 */
const multipleSelection = ref<IOrderApportiondetailDTO[]>([])
/** 佣金计提表信息 */
const commissionList = ref<TOrderItemCommissionDTO[]>([])

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

onMounted(() => {
  handleQuery()
  /**
   * 计算表格高度
   */
  tableheight.value = computTableHeight() - 50
})
/**
 * 分配结果查询
 */
const handleQuery = () => {
  getOrderApportiondetailPageListApi(queryChargeParams)
    .then((res: IAxios<IOrderApportiondetailDTO[]>) => {
      if (res.success) {
        orderApportiondetailList.value = res.obj
        total.value = res.total
      }
    })
    .finally(() => {})
}
/**
 * 撤销前显示佣金信息
 * @param row
 */
const handleBeforeBack = (row: IOrderApportiondetailDTO) => {
  const ids: string[] = []
  ids.push(row.id?.toString() as string)
  if (ids.length === 0) {
    ElMessage.warning('请选择要撤销的数据')
    return
  }
  multipleSelection.value.push(row)
  const data = {
    orderitemid: row.orderitemid!
  }
  getCommissionListByItemIdApi(data)
    .then((res: IAxios<TOrderItemCommissionDTO[]>) => {
      if (res.success) {
        if (res.obj.length === 0) {
          handleBack(row)
        } else {
          commissionList.value = res.obj
          dialogCommissionshow.value = true
        }
      }
    })
    .catch(() => {})
}
/**
 * 分摊撤销
 * @param row
 */
const handleBack = (row?: IOrderApportiondetailDTO) => {
  dialogCommissionshow.value = false
  const ids: string[] = []
  if (row === undefined) {
    multipleSelection.value.forEach((item) => {
      ids.push(item.id?.toString() as string)
    })
    multipleSelection.value = []
  } else {
    ids.push(row.id?.toString() as string)
  }

  if (ids.length === 0) {
    ElMessage.warning('请选择要撤销的数据')
    return
  }
  orderApportiondetailIds.value = ids.join(',')
  backreason.value = ''
  dialogBackshow.value = true
}
/**
 * 保存撤销分配结果
 * @param row
 */
const saveBackForm = () => {
  if (backreason.value === '') {
    ElMessage.warning('请输入撤销原因')
    return
  }
  ElMessageBox.confirm(
    '如果已经计算佣金，则将对佣金数据进行回退，确定要撤回所选核销数据吗？',
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  )
    .then(() => {
      const data = {
        orderApportiondetailIds: orderApportiondetailIds.value,
        sdesc: backreason.value
      }
      revertWriteOffApi(data)
        .then((res: IAxios) => {
          if (res.success) {
            orderApportiondetailIds.value = ''
            ElMessage.success('操作成功')
            dialogBackshow.value = false
            handleQuery()
          }
        })
        .catch(() => {})
    })
    .catch(() => {})
}
/**
 * 行选择事件
 * @param val
 */
const handleSelectionChange = (val: IOrderApportiondetailDTO[]) => {
  multipleSelection.value = val
}
/**
 * 时间选择
 * @param val
 */
const ontimeCharge = (val: TDateTimeType) => {
  let timev = false
  if (queryChargeParams.startTime) {
    timev = true
  }
  queryChargeParams.startTime = val.startTime
  queryChargeParams.endTime = val.endTime
  if (timev) {
    handleQuery()
  }
}
/**
 * 改变页码总数时
 */
const handleSizeChange = (val: number) => {
  queryChargeParams.pageSize = val
  handleQuery()
}
/**
 * 改变页数时
 * @param val
 */
const handleCurrentChange = (val: number) => {
  queryChargeParams.page = val
  handleQuery()
}
/**
 * 根据合同号查看订单项目--------------------------------------------------
 * @param row
 * @param index
 */
import AdTworderdetails from '../ad/AdTworderdetails.vue'
const viewImageRef = ref()
const handleSee = (row?: any, number?: number) => {
  viewImageRef.value.view(row.scontractnum)
}
// ---------------------------------------------------------------------
</script>
<style lang="scss" scoped></style>
