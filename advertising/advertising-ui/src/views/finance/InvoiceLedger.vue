<!--
 * @Author: suny
 * @Date: 2024-01-04 15:32:38
 * @LastEditTime: 2024-03-13 11:12:54
 * @LastEditors: wanghl
 * @Description: 发票台账
-->
<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex">
        <HgDateIndex :initdate="timedata" @onresponse="ontime"></HgDateIndex>
        <el-input
          v-model="queryParams.queryKey"
          clearable
          placeholder="请输入发票号"
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
        :data="invoiceLedgerList"
        highlight-current-row
        :height="tableheight"
        :header-cell-style="tableHeaderColor"
        :border="true"
        @selection-change="handleSelectionChange"
      >
        <el-table-column label="发票号码" prop="invoice" width="140" show-overflow-tooltip />
        <el-table-column label="发票代码" prop="invoicecode" width="160" show-overflow-tooltip />
        <el-table-column label="开票日期" prop="dcreatetime" width="100">
          <template #default="scope">
            <span>{{ dayjs(scope.row.dcreatetime).format('YYYY-MM-DD') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="发票类型" prop="itype" width="100" :align="'center'">
          <template #default="scope">
            {{ preinvoiceApplyTypeCombo.find((item) => item.id === scope.row.itype)?.name }}
          </template>
        </el-table-column>
        <el-table-column label="开票项目" prop="printitemname" width="120" show-overflow-tooltip />
        <el-table-column label="发票金额" prop="namount" min-width="120" align="right">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.namount, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="入账金额" prop="namountreceived" min-width="120" align="right">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.namountreceived, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="已用金额" prop="namounspent" min-width="120" align="right">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.namounspent, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="欠款金额" prop="amountarrearage" min-width="120" align="right">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.amountarrearage, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="剩余金额" prop="namountbalance" min-width="120" align="right">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.namountbalance, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="客户" prop="customername" min-width="160" show-overflow-tooltip />
        <el-table-column label="客户类型" prop="icusttype" min-width="100" show-overflow-tooltip>
          <template #default="scope">
            {{ customerTypeCombo.find((item) => item.id === scope.row.icusttype)?.name }}
          </template>
        </el-table-column>
        <el-table-column label="打印名称" prop="sprintname" min-width="160" show-overflow-tooltip />
        <el-table-column label="主业务员" prop="employname" width="100" show-overflow-tooltip />
        <el-table-column label="发票样式" prop="istype" min-width="100" show-overflow-tooltip>
          <template #default="scope">
            {{ preinvoiceStyleCombo.find((item) => item.id === scope.row.istype)?.name }}
          </template>
        </el-table-column>
        <el-table-column
          label="经营主体"
          prop="businessentityname"
          min-width="140"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column label="发票状态" prop="istatus" min-width="100" show-overflow-tooltip>
          <template #default="scope">
            {{ invoiceStatusCombo.find((item) => item.id === scope.row.istatus)?.name }}
          </template>
        </el-table-column>
        <el-table-column label="备注" prop="sremark" min-width="120"></el-table-column>
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
      title="发票详情"
      align-center
      draggable
      width="1100"
      :close-on-click-modal="false"
      :destroy-on-close="true"
    >
      <div style="width: 900px">
        <el-form
          ref="orderDetailFormRef"
          :model="invoiceDetailForm"
          :inline="true"
          label-width="100px"
          class="demo-workReportForm"
          status-icon
        >
          <el-form-item label="预开信息">
            <div style="width: 100%">
              <el-table
                ref="fileTableRef"
                :data="invoiceDetailForm.preInvoiceApplicationDTOList"
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
          <el-form-item label="预收款信息">
            <el-table
              ref="advanceChargeTableRef"
              :data="invoiceDetailForm.customerChargeBankDTOList"
              highlight-current-row
              :header-cell-style="tableHeaderColor"
              :border="true"
              style="width: 900px"
            >
              <el-table-column label="客户编号" prop="icode" width="100" />
              <el-table-column label="客户名称" prop="customername" show-overflow-tooltip />
              <el-table-column label="客户属性" prop="customeritype" show-overflow-tooltip>
                <template #default="scope">
                  {{ customerTypeCombo.find((item) => item.id === scope.row.customeritype)?.name }}
                </template>
              </el-table-column>
              <el-table-column label="经营主体" prop="businessentityname" show-overflow-tooltip />
              <el-table-column label="业务员" prop="employname" width="100" show-overflow-tooltip />
              <el-table-column label="发票号" prop="invoice" show-overflow-tooltip />
              <el-table-column label="收款金额" prop="namountreceived" width="150" align="right">
                <template #default="scope">
                  <span>{{ formatMoney(scope.row.namountreceived, '2') }}</span>
                </template>
              </el-table-column>
              <el-table-column label="剩余金额" prop="namountbalance" width="150" align="right">
                <template #default="scope">
                  <span>{{ formatMoney(scope.row.namountbalance, '2') }}</span>
                </template>
              </el-table-column>
              <el-table-column label="开票名称" prop="printitemname" width="100" />
              <el-table-column label="开票日期" prop="dcreatetime" width="120">
                <template #default="scope">
                  <span>{{ dayjs(scope.row.dcreatetime).format('YYYY-MM-DD') }}</span>
                </template>
              </el-table-column>
              <el-table-column label="收费日期" prop="dpaydate" width="120">
                <template #default="scope">
                  <span>{{ dayjs(scope.row.dpaydate).format('YYYY-MM-DD') }}</span>
                </template>
              </el-table-column>
              <el-table-column label="状态" prop="istatus" width="120">
                <template #default="scope">
                  {{ statusCombo.find((item) => item.id === scope.row.istatus)?.name }}
                </template>
              </el-table-column>
            </el-table>
          </el-form-item>
          <el-form-item label="核销数据">
            <div style="width: 900px">
              <el-table
                ref="orderApportiondetailTableRef"
                :data="invoiceDetailForm.orderApportiondetailDTOList"
                highlight-current-row
                :header-cell-style="tableHeaderColor"
                :border="true"
              >
                <el-table-column label="合同号" prop="scontractnum" show-overflow-tooltip />
                <el-table-column label="经营主体" prop="businessentityname" show-overflow-tooltip />
                <el-table-column label="直接客户" prop="customername" show-overflow-tooltip />
                <el-table-column label="代理公司" prop="agencyname" show-overflow-tooltip />
                <el-table-column
                  label="内容生产方"
                  prop="agentname"
                  min-width="120"
                  show-overflow-tooltip
                />
                <el-table-column label="刊发日期" prop="prestartdate" show-overflow-tooltip>
                  <template #default="scope">
                    <span>{{ dayjs(scope.row.prestartdate).format('YYYY-MM-DD') }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="媒体" prop="medianame" show-overflow-tooltip />
                <el-table-column label="广告标题" prop="sadtitle" show-overflow-tooltip />
                <el-table-column
                  label="签订金额"
                  prop="amountreceivable"
                  min-width="120"
                  align="right"
                >
                  <template #default="scope">
                    <span>{{ formatMoney(scope.row.amountreceivable, '2') }}</span>
                  </template>
                </el-table-column>
                <el-table-column
                  label="已收金额"
                  prop="amountreceived"
                  min-width="120"
                  align="right"
                >
                  <template #default="scope">
                    <span>{{ formatMoney(scope.row.amountreceived, '2') }}</span>
                  </template>
                </el-table-column>
                <el-table-column
                  label="欠款金额"
                  prop="amountarrearage"
                  min-width="120"
                  align="right"
                >
                  <template #default="scope">
                    <span>{{ formatMoney(scope.row.amountarrearage, '2') }}</span>
                  </template>
                </el-table-column>
                <el-table-column
                  label="分摊金额"
                  prop="namountapportion"
                  min-width="120"
                  align="right"
                >
                  <template #default="scope">
                    <span>{{ formatMoney(scope.row.namountapportion, '2') }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="发票号" prop="invoice" show-overflow-tooltip />
                <el-table-column label="收费日期" prop="prestartdate" show-overflow-tooltip>
                  <template #default="scope">
                    <span>{{ dayjs(scope.row.prestartdate).format('YYYY-MM-DD') }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="分摊日期" prop="dapportiondate" show-overflow-tooltip>
                  <template #default="scope">
                    <span>{{ dayjs(scope.row.dapportiondate).format('YYYY-MM-DD') }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="收款种类" prop="sapportiontype" show-overflow-tooltip>
                  <template #default="scope">
                    {{ stypeCombo.find((item) => item.id === scope.row.sapportiontype)?.name }}
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </el-form-item>
          <el-form-item label="分配明细">
            <div style="width: 900px">
              <el-table
                ref="bankAccountChargeTableRef"
                :data="invoiceDetailForm.allocationDetails"
                highlight-current-row
                :header-cell-style="tableHeaderColor"
                :border="true"
              >
                <el-table-column label="客户名称" prop="customername" />
                <el-table-column label="业务员" prop="employname" />
                <el-table-column label="发票号" prop="invoice" />
                <el-table-column label="发票金额" prop="namountapply" width="120" align="right">
                  <template #default="scope">
                    <span>{{ formatMoney(scope.row.namountapply, '2') }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="分配金额" prop="namountreceived" width="120" align="right">
                  <template #default="scope">
                    <span>{{ formatMoney(scope.row.namountreceived, '2') }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="合同号" prop="scontractnum" width="150" />
                <el-table-column label="收款类型" prop="stype" width="150" />
                <el-table-column label="付款方式" prop="paymethodname" width="150" />
                <el-table-column label="创建者" prop="createempname" width="100" />
                <el-table-column label="创建日期" prop="dcreatetime" width="120">
                  <template #default="scope">
                    <span>{{ dayjs(scope.row.dcreatetime).format('YYYY-MM-DD') }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="状态" prop="istatus" width="120">
                  <template #default="scope">
                    {{ statusCombo.find((item) => item.id === scope.row.istatus)?.name }}
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </el-form-item>
        </el-form>
      </div>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import useUserStore from '@/store/modules/user'
import { TDateTimeType, TDateType } from '@/types/components/hgdateindex'
import { IAxios } from 'axios'
import {
  ESType,
  EStatus,
  EOrderkind,
  EPreinvoiceApplyType,
  ECustomerType,
  EPreinvoiceStyle,
  EInvoiceStatus
} from '@/types/common/enumindex'
import { TSelectZtree } from '@/types/common'
import { computTableHeight, tableHeaderColor, formatMoney } from '@/utils'
import { dayjs } from 'element-plus'
import { TOrderAndItemVO } from '@/types/views/business/orderitemcost'
import { TAdCustomer } from '@/types/views/customer'
import { IInvoiceDTO } from '@/types/views/finance/invoice'
import { getInvoicesInPeriodApi, getInvoiceDetailsByInvoiceApi } from '@/api/finance/dataledger'
import { InvoiceDetailsDTO } from '@/types/views/finance/dataledger'
import { IDataCombo } from '@/types/common/DataCombo'
import { getEnumCombo } from '@/api/common/index'
/** 预开发票类型下拉 */
const preinvoiceApplyTypeCombo: IDataCombo[] = getEnumCombo(EPreinvoiceApplyType)
/** 客户类型下拉 */
const customerTypeCombo: IDataCombo[] = getEnumCombo(ECustomerType)
/** 收款类型下拉 */
const stypeCombo: IDataCombo[] = getEnumCombo(ESType)
/** 状态下拉 */
const statusCombo: IDataCombo[] = getEnumCombo(EStatus)
/** 开票样式下拉 */
const preinvoiceStyleCombo: IDataCombo[] = getEnumCombo(EPreinvoiceStyle)
/** 发票状态下拉 */
const invoiceStatusCombo: IDataCombo[] = getEnumCombo(EInvoiceStatus)
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
  sort: 'dcreatetime',
  order: 'desc',
  page: 1,
  pageSize: 20,
  startTime: '',
  endTime: '',
  customername: ''
})
/** 列表对象 */
const invoiceLedgerList = ref<IInvoiceDTO[]>([])
/** Form选中的行 */
const multipleSelection = ref<IInvoiceDTO[]>([])
/** 详情对象 */
const invoiceDetailForm = ref<InvoiceDetailsDTO>({})

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
  getInvoicesInPeriodApi(queryParams)
    .then((res: IAxios<IInvoiceDTO[]>) => {
      if (res.success) {
        invoiceLedgerList.value = res.obj
        total.value = res.total
      }
    })
    .finally(() => {})
}
/**
 * 查看详情按钮事件
 * @param row
 */
const onDetail = (row: IInvoiceDTO) => {
  const data = {
    invoiceid: row.id!
  }
  getInvoiceDetailsByInvoiceApi(data)
    .then((res: IAxios<InvoiceDetailsDTO>) => {
      if (res.success) {
        dialogDetailVisible.value = true
        invoiceDetailForm.value = res.obj
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
const handleSelectionChange = (val: IInvoiceDTO[]) => {
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
</script>

<style lang="scss" scoped>
.search_box .el-button,
.search_box .el-select,
.search_box .el-input {
  margin: 0 0px 5px 0 !important;
}
</style>
