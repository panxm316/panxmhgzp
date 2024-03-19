<!--
 * @Author: suny
 * @Date: 2024-01-04 15:32:06
 * @LastEditTime: 2024-01-17 11:28:13
 * @LastEditors: wanghl
 * @Description: 订单台账
-->
<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex">
        <HgDateIndex :initdate="timedata" @onresponse="ontime"></HgDateIndex>
        <el-input
          v-model="queryParams.queryKey"
          clearable
          placeholder="请输入订单合同号"
          class="input-with-select"
          style="width: 200px"
          @keyup.enter="handleQuery"
          @clear="handleQuery"
        >
        </el-input>
        <span style="width: 10px"></span>
        <el-input
          v-model="queryParams.customername"
          placeholder="请选择客户信息"
          class="inputwindht"
          clearable
          style="width: 200px"
          @input="queryParams.customerid = ''"
        >
          <template #append>
            <el-button
              icon="Search"
              circle
              title="选择客户"
              @click="dialogCustomerVisible = true"
            />
          </template>
        </el-input>
        <span style="width: 10px"></span>

        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
      </div>
    </div>
    <div class="table_box">
      <el-table
        ref="dataTableRef"
        :data="orderLedgerList"
        highlight-current-row
        :height="tableheight"
        :header-cell-style="tableHeaderColor"
        :border="true"
        @selection-change="handleSelectionChange"
      >
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
        <el-table-column label="项目名称" prop="adprojectname" width="140" show-overflow-tooltip />
        <el-table-column
          label="经营主体"
          prop="businessentityname"
          min-width="140"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column label="客户" prop="customername" min-width="160" show-overflow-tooltip />
        <el-table-column label="主业务员" prop="employname" width="100" show-overflow-tooltip />
        <el-table-column label="代理公司" prop="agencyname" width="120" show-overflow-tooltip />
        <el-table-column label="内容生产方" prop="agentname" width="120" show-overflow-tooltip />
        <el-table-column label="广告行业" prop="adindustryname" width="100" show-overflow-tooltip />
        <el-table-column label="广告标题" prop="sadtitle" width="160" show-overflow-tooltip />
        <el-table-column
          label="应收"
          prop="amountreceivable"
          width="120"
          show-overflow-tooltip
          align="right"
        >
          <template #default="scope">
            <span>{{ formatMoney(scope.row.amountreceivable, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="已收"
          prop="amountreceived"
          width="120"
          show-overflow-tooltip
          align="right"
        >
          <template #default="scope">
            <span>{{ formatMoney(scope.row.amountreceived, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="欠款"
          prop="amountarrearage"
          width="120"
          show-overflow-tooltip
          align="right"
        >
          <template #default="scope">
            <span>{{ formatMoney(scope.row.amountarrearage, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="业绩金额"
          prop="amountachievement"
          width="120"
          align="right"
          show-overflow-tooltip
        >
          <template #default="scope">
            <span>{{ formatMoney(scope.row.amountachievement, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="关联发票号" prop="invoices" min-width="160" show-overflow-tooltip>
          <template #default="scope">
            <span
              v-if="scope.row.invoices"
              v-html="scope.row.invoices.replaceAll(',', '<br />')"
            ></span>
            <span v-else>无</span>
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
          align="center"
          width="80"
          class-name="small-padding fixed-width"
          fixed="right"
        >
          <template #default="scope">
            <el-button
              link
              type="primary"
              icon="view"
              size="small"
              title="查看详情"
              @click="onDetail(scope.row)"
              >详情</el-button
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
    <!-- 选择客户 -->
    <el-dialog
      v-model="dialogCustomerVisible"
      title="选择客户"
      width="800"
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
    <!-- 详情 -->
    <el-dialog
      v-model="dialogDetailVisible"
      title="订单详情"
      align-center
      draggable
      width="1100"
      :close-on-click-modal="false"
      :destroy-on-close="true"
    >
      <div>
        <el-form
          ref="orderDetailFormRef"
          :model="orderDetailForm"
          :inline="true"
          label-width="100px"
          class="demo-workReportForm"
          status-icon
        >
          <el-form-item label="合同号">
            <span>{{ orderDetailForm.order!.scontractnum }}</span>
          </el-form-item>
          <el-form-item label="订单类别">
            {{
              orderkindCombo.find((item) => (item.id as any) === orderDetailForm.order!.iorderkind)
                ?.name
            }}
          </el-form-item>
          <el-form-item label="广告类型">
            <span>{{ orderDetailForm.order!.adtypename }}</span>
          </el-form-item>
          <el-form-item label="业务员">
            <span>{{ orderDetailForm.order!.employname }}</span>
          </el-form-item>
          <el-form-item label="客户">
            <span>{{ orderDetailForm.order!.customername }}</span>
          </el-form-item>
          <el-form-item label="代理公司">
            <span>{{ orderDetailForm.order!.agencyname }}</span>
          </el-form-item>
          <el-form-item label="内容生产方">
            <span>{{ orderDetailForm.order!.agentname }}</span>
          </el-form-item>
          <el-form-item label="广告明细">
            <div style="width: 900px">
              <el-table
                ref="fileTableRef"
                :data="orderDetailForm.orderitemList"
                highlight-current-row
                :header-cell-style="tableHeaderColor"
                :border="true"
              >
                <el-table-column
                  label="广告号"
                  prop="iitemcode"
                  min-width="100"
                  show-overflow-tooltip
                />
                <el-table-column label="预刊日期" prop="prestartdate" width="100">
                  <template #default="scope">
                    <span>{{ dayjs(scope.row.prestartdate).format('YYYY-MM-DD') }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="广告标题" prop="sadtitle" show-overflow-tooltip />
                <el-table-column label="媒体" prop="medianame" show-overflow-tooltip />
                <el-table-column
                  label="签订金额"
                  prop="amountreceivable"
                  show-overflow-tooltip
                  align="right"
                >
                  <template #default="scope">
                    <span>{{ formatMoney(scope.row.amountreceivable, '2') }}</span>
                  </template>
                </el-table-column>
                <el-table-column
                  label="已收金额"
                  prop="amountreceived"
                  show-overflow-tooltip
                  align="right"
                >
                  <template #default="scope">
                    <span>{{ formatMoney(scope.row.amountreceived, '2') }}</span>
                  </template>
                </el-table-column>
                <el-table-column
                  label="欠款金额"
                  prop="amountarrearage"
                  show-overflow-tooltip
                  width="150"
                  align="right"
                >
                  <template #default="scope">
                    <span>{{ formatMoney(scope.row.amountarrearage, '2') }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="成本" prop="namountcost" show-overflow-tooltip />
                <el-table-column label="" prop="foldname" show-overflow-tooltip />
                <el-table-column label="" prop="foldareavername" show-overflow-tooltip />
                <el-table-column label="" prop="addisplayformname" show-overflow-tooltip />
                <el-table-column label="" prop="pagesortname" show-overflow-tooltip />
                <el-table-column label="" prop="adcolorname" show-overflow-tooltip />
                <el-table-column label="" prop="adspecname" show-overflow-tooltip />
              </el-table>
            </div>
          </el-form-item>
          <el-form-item label="预开发票">
            <div style="width: 900px">
              <el-table
                ref="fileTableRef"
                :data="orderDetailForm.preInvoiceApplicationDTOList"
                highlight-current-row
                :header-cell-style="tableHeaderColor"
                :border="true"
              >
                <el-table-column
                  label="部门"
                  prop="deptname"
                  min-width="150"
                  show-overflow-tooltip
                />
                <el-table-column
                  label="业务员"
                  prop="employname"
                  min-width="100"
                  show-overflow-tooltip
                />
                <el-table-column label="申请日期" prop="dapplytime" width="100">
                  <template #default="scope">
                    <span>{{ dayjs(scope.row.dapplytime).format('YYYY-MM-DD') }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="开票日期" prop="realInvoiceDate" width="100">
                  <template #default="scope">
                    <span v-if="scope.row.realInvoiceDate">{{
                      dayjs(scope.row.realInvoiceDate).format('YYYY-MM-DD')
                    }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="发票号" prop="invoice" width="150" />
                <el-table-column label="开票金额" prop="namountapply" width="150" align="right">
                  <template #default="scope">
                    <span>{{ formatMoney(scope.row.namountapply, '2') }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="已还金额" prop="namountreceived" width="150" align="right">
                  <template #default="scope">
                    <span>{{ formatMoney(scope.row.namountreceived, '2') }}</span>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </el-form-item>
          <el-form-item label="分摊明细">
            <el-table
              ref="fileTableRef"
              :data="orderDetailForm.orderApportiondetailDTOList"
              highlight-current-row
              style="width: 900px"
              :header-cell-style="tableHeaderColor"
              :border="true"
            >
              <el-table-column label="广告号" prop="orderitemcode" min-width="100" />
              <el-table-column label="预刊日期" prop="prestartdate" width="100">
                <template #default="scope">
                  <span>{{ dayjs(scope.row.preenddate).format('YYYY-MM-DD') }}</span>
                </template>
              </el-table-column>
              <el-table-column label="广告标题" prop="sadtitle" width="200" show-overflow-tooltip />
              <el-table-column label="签订金额" prop="amountreceivable" width="120" align="right">
                <template #default="scope">
                  <span>{{ formatMoney(scope.row.amountreceivable, '2') }}</span>
                </template>
              </el-table-column>
              <el-table-column label="已收金额" prop="amountreceived" width="120" align="right">
                <template #default="scope">
                  <span>{{ formatMoney(scope.row.amountreceived, '2') }}</span>
                </template>
              </el-table-column>
              <el-table-column label="欠款金额" prop="amountarrearage" width="120" align="right">
                <template #default="scope">
                  <span>{{ formatMoney(scope.row.amountarrearage, '2') }}</span>
                </template>
              </el-table-column>
              <el-table-column label="发票号" prop="invoice" min-width="150" />
              <el-table-column label="分摊日期" prop="dapportiondate" width="100">
                <template #default="scope">
                  <span>{{ dayjs(scope.row.dapportiondate).format('YYYY-MM-DD') }}</span>
                </template>
              </el-table-column>
              <el-table-column label="分摊金额" prop="namountapportion" width="120" align="right">
                <template #default="scope">
                  <span>{{ formatMoney(scope.row.namountapportion, '2') }}</span>
                </template>
              </el-table-column>
            </el-table>
          </el-form-item>
          <el-form-item label="预收费">
            <el-row style="width: 100%">
              <el-col :span="24">
                <el-table
                  ref="fileTableRef"
                  :data="orderDetailForm.customerChargeBankDTOList"
                  highlight-current-row
                  :header-cell-style="tableHeaderColor"
                  :border="true"
                >
                  <el-table-column
                    label="客户名称"
                    prop="customername"
                    width="150"
                    show-overflow-tooltip
                  />
                  <el-table-column
                    label="客户类型"
                    prop="customeritype"
                    min-width="100"
                    show-overflow-tooltip
                  >
                    <template #default="scope">
                      {{
                        customerTypeCombo.find((item) => item.id === scope.row.customeritype)?.name
                      }}
                    </template>
                  </el-table-column>
                  <el-table-column
                    label="发票号"
                    prop="invoice"
                    width="150"
                    show-overflow-tooltip
                  />
                  <el-table-column label="开票时间" prop="dinvoicecreatetime" width="100">
                    <template #default="scope">
                      <span>{{ dayjs(scope.row.dinvoicecreatetime).format('YYYY-MM-DD') }}</span>
                    </template>
                  </el-table-column>
                  <el-table-column label="收款时间" prop="dcreatetime" width="100">
                    <template #default="scope">
                      <span>{{ dayjs(scope.row.dcreatetime).format('YYYY-MM-DD') }}</span>
                    </template>
                  </el-table-column>
                  <el-table-column
                    label="入账金额"
                    prop="namountreceived"
                    width="150"
                    align="right"
                  >
                    <template #default="scope">
                      <span>{{ formatMoney(scope.row.namountreceived, '2') }}</span>
                    </template>
                  </el-table-column>
                  <el-table-column label="已用金额" prop="namounspent" width="150" align="right">
                    <template #default="scope">
                      <span>{{ formatMoney(scope.row.namounspent, '2') }}</span>
                    </template>
                  </el-table-column>
                </el-table>
              </el-col>
            </el-row>
          </el-form-item>
        </el-form>
      </div>
    </el-dialog>
    <!-- 根据合同号查看详情 -->
    <AdTworderdetails ref="viewImageRef"></AdTworderdetails>
  </div>
</template>

<script lang="ts" setup>
import useUserStore from '@/store/modules/user'
import { TDateTimeType, TDateType } from '@/types/components/hgdateindex'
import { IAxios } from 'axios'
import { EOrderkind } from '@/types/common/enumindex'
import { TSelectZtree } from '@/types/common'
import { computTableHeight, tableHeaderColor, formatMoney } from '@/utils'
import { dayjs } from 'element-plus'
import { TOrderAndItemVO } from '@/types/views/business/orderitemcost'
import { TAdCustomer } from '@/types/views/customer'
import { IOrderAndItemDTO } from '@/types/views/business/orderitemcost'
import { getOrdersInPeriodApi, getOrderContractDetailApi } from '@/api/finance/dataledger'
import { IOrderContractDetailDTO } from '@/types/views/finance/dataledger'
import { ECustomerType } from '@/types/common/enumindex'
import { IDataCombo } from '@/types/common/DataCombo'
import { getEnumCombo } from '@/api/common/index'

/** 订单类别下拉 */
const orderkindCombo: IDataCombo[] = getEnumCombo(EOrderkind)
/** 客户类型下拉 */
const customerTypeCombo: IDataCombo[] = getEnumCombo(ECustomerType)
const user = useUserStore().user
const total = ref(0)
/** 选择客户显示状态 */
const dialogCustomerVisible = ref(false)
/** 查看详情窗口显示状态 */
const dialogDetailVisible = ref(false)
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
const queryParams = reactive<TOrderAndItemVO>({
  sort: 'createtime',
  order: 'desc',
  page: 1,
  pageSize: 20,
  startTime: '',
  endTime: '',
  customername: ''
})
/** 列表对象 */
const orderLedgerList = ref<IOrderAndItemDTO[]>([])
/** Form选中的行 */
const multipleSelection = ref<IOrderAndItemDTO[]>([])
/** 详情对象 */
const orderDetailForm = ref<IOrderContractDetailDTO>({})

onMounted(() => {
  handleQuery()
  /**
   * 计算表格高度
   */
  tableheight.value = computTableHeight() - 50
})
/**
 * 查询
 */
const handleQuery = () => {
  getOrdersInPeriodApi(queryParams)
    .then((res: IAxios<IOrderAndItemDTO[]>) => {
      if (res.success) {
        orderLedgerList.value = res.obj
        total.value = res.total
      }
    })
    .finally(() => {})
}
/**
 * 查看详情按钮事件
 * @param row
 */
const onDetail = (row: IOrderAndItemDTO) => {
  const data = {
    orderId: row.orderid!
  }
  getOrderContractDetailApi(data)
    .then((res: IAxios<IOrderContractDetailDTO>) => {
      if (res.success) {
        dialogDetailVisible.value = true
        orderDetailForm.value = res.obj
      }
    })
    .finally(() => {})
}
/**
 * 选择客户信息后返回接收
 * @param val
 */
const selectCustomer = (val: TAdCustomer) => {
  queryParams.customerid = val.id!
  queryParams.customername = val.sname!

  dialogCustomerVisible.value = false
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
/**
 * 行选择事件
 * @param val
 */
const handleSelectionChange = (val: IOrderAndItemDTO[]) => {
  multipleSelection.value = val
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

<style lang="scss" scoped>
.el-form-item {
  margin-bottom: 10px !important;
}
.search_box .el-button,
.search_box .el-select,
.search_box .el-input {
  margin: 0 0px 5px 0 !important;
}
</style>
