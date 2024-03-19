<!--
 * @Author: suny
 * @Date: 2023-10-24 10:53:29
 * @LastEditTime: 2024-03-15 18:25:24
 * @LastEditors: suny
 * @Description: 银行流水
-->
<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex">
        <el-button type="primary" icon="Plus" @click="importBankAccount">导入</el-button>
        <HgDateIndex :initdate="timedata" @onresponse="ontime"></HgDateIndex>
        <el-input
          v-model="queryParams.queryKey"
          clearable
          placeholder="请输入对方账户关键字"
          class="input-with-select"
          style="width: 200px"
          @keyup.enter="handleQuery"
          @clear="handleQuery"
        >
        </el-input>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button type="primary" icon="Clock" @click="onHistory">历史查看</el-button>
      </div>
    </div>
    <div class="table_box">
      <el-table
        ref="dataTableRef"
        :data="bankAccountList"
        highlight-current-row
        :height="tableheight"
        :header-cell-style="tableHeaderColor"
        border
      >
        <el-table-column label="账号" prop="sborroweraccount" min-width="190px" />
        <el-table-column label="账户名称" prop="sborrowername" width="200px" />
        <el-table-column label="对方账号" prop="slenderaccount" width="180" />
        <el-table-column label="对方户名" prop="slendername" width="200" show-overflow-tooltip>
          <template #default="scope">
            <el-button type="primary" link size="small" @click="onDetail(scope.row)">{{
              scope.row.slendername
            }}</el-button>
          </template>
        </el-table-column>
        <el-table-column label="交易日期" prop="dtradedate" width="160" />
        <el-table-column label="凭证种类" prop="scredentialtype" width="100" />
        <el-table-column label="贷方发生额" prop="namount" min-width="120" align="right">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.namount, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="已分配金额" prop="namountallocate" width="120" align="right">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.namountallocate, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="明细编号—交易流水号" prop="stradecode" width="240" />
        <el-table-column label="明细类型" prop="sdetailtype" width="80" />
        <el-table-column label="交易类别" prop="stradetype" width="80" />
        <el-table-column label="备注" prop="sremark" width="200" show-overflow-tooltip />
        <el-table-column label="导入时间" prop="dcreatetime" width="160" />
        <el-table-column label="最后操作员" prop="lastoperator" width="120" />
        <el-table-column
          label="操作"
          align="left"
          width="100"
          class-name="small-padding fixed-width"
          fixed="right"
        >
          <template #default="scope">
            <el-button
              v-if="!scope.row.bconfirm"
              link
              type="primary"
              icon="Top"
              size="small"
              title="核销"
              @click="onAllocation(scope.row)"
              >核销</el-button
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
    <!-- 导入 -->
    <el-dialog
      v-model="dialogImportVisible"
      title="银行流水导入"
      width="680px"
      append-to-body
      :destroy-on-close="true"
    >
      <div>
        <el-row>
          <el-col :span="24">
            <HgFileUpload
              :server="hgfileuploadparam.server"
              :accept="hgfileuploadparam.accept"
              :multiple="false"
              :filenumlimit="1"
              :storytypes="hgfileuploadparam.storytypes"
              @getupfile="getUpFile"
            ></HgFileUpload>
          </el-col>
        </el-row>
      </div>
    </el-dialog>
    <!-- 历史 -->
    <el-dialog
      v-model="dialogHistoryVisible"
      title="银行流水导入历史"
      width="90%"
      append-to-body
      :destroy-on-close="true"
    >
      <BankAccountHistory></BankAccountHistory>
    </el-dialog>
    <!-- 详情 -->
    <el-dialog
      v-model="dialogDetailVisible"
      title="银行流水分配发票详情"
      align-center
      :close-on-click-modal="false"
      :destroy-on-close="true"
    >
      <el-table :data="customerChargeList">
        <el-table-column label="发票号" prop="invoice" />
        <el-table-column label="发票金额" prop="namountapply" align="right">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.namountapply, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="分配金额" prop="namountreceived" align="right">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.namountreceived, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="合同号" prop="scontractnum" />
        <el-table-column label="创建者" prop="createempname" />
        <el-table-column label="创建日期" prop="dcreatetime">
          <template #default="scope">
            <span>{{ dayjs(scope.row.dcreatetime).format('YYYY-MM-DD') }}</span>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
    <!-- 金额分配 -->
    <el-dialog
      v-model="dialogAllocationVisible"
      title="到账分配金额"
      :draggable="true"
      width="1200"
      height="500"
      append-to-body
      :destroy-on-close="true"
    >
      <div style="width: 92%">
        <el-row>
          <el-form :inline="true" class="demo-form-inline">
            <el-form-item label="总额度">
              <el-input v-model="unnamountreceived" style="width: 160px" disabled></el-input>
            </el-form-item>
            <el-form-item label="付款方式">
              <el-select
                v-model="bankAccountAllocateForm.paymethodid"
                placeholder="请选择付款方式"
                filterable
                style="width: 160px"
              >
                <el-option
                  v-for="t in paymethodCombo"
                  :key="t.id"
                  :label="t.name"
                  :value="t.id"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="核销日期">
              <el-date-picker
                v-model="writeOffDate"
                type="date"
                placeholder="请选择核销日期"
                style="width: 200px"
                format="YYYY/MM/DD"
                value-format="YYYY-MM-DD"
              />
            </el-form-item>
          </el-form>
        </el-row>
        <el-row v-if="curInvoice">
          <el-col :span="24">
            <el-row>
              <el-form :inline="true" class="demo-form-inline">
                <el-form-item label="发票金额">
                  <el-input v-model="curInvoice!.namountapply" disabled style="width: 100px" />
                </el-form-item>
                <el-form-item label="已收金额">
                  <el-input v-model="curInvoice!.namountreceived" disabled style="width: 100px" />
                </el-form-item>
                <el-form-item label="分配金额">
                  <el-input
                    v-model="bankAccountAllocateForm.namountreceived"
                    style="width: 100px"
                    @blur="validateInvoice"
                  />
                </el-form-item>
              </el-form>
            </el-row>
            <div
              v-if="preInvoiceOrderItemList.length! > 0"
              style="width: 100%; height: 300px; overflow_y: auto"
            >
              <span>分配目标</span>
              <el-table
                :data="preInvoiceOrderItemList"
                style="width: 100%; max-height: 280px"
                @selection-change="handleSelectionChange"
              >
                <el-table-column type="selection" width="50" :align="'center'" />
                <el-table-column label="合同号" prop="scontractnum" width="120" />
                <el-table-column label="媒体" prop="medianame" width="100" />
                <el-table-column label="广告编号" prop="iitemcode" width="100" />
                <el-table-column label="广告标题" prop="sadtitle" show-overflow-tooltip />
                <el-table-column label="广告类型" prop="addisplayformname" width="100" />
                <el-table-column label="签订金额" prop="amountreceivable" width="100" />
                <el-table-column label="欠款金额" prop="amountarrearage" width="100" />
              </el-table>
            </div>
            <div style="width: 100%; text-align: right; margin-top: 10px">
              <el-button type="primary" icon="Check" @click="saveForm">保存</el-button>
              <el-button icon="Close" @click="onSelectCancel">取消</el-button>
            </div>
          </el-col>
        </el-row>
        <el-row>
          <div class="search_box">
            <div class="flex">
              <HgDateIndex :initdate="timedata" @onresponse="ontimeInvoice"></HgDateIndex>
              <el-input
                v-model="queryInvoiceParams.queryKey"
                clearable
                placeholder="请输入发票、客户或者合同关键字"
                class="input-with-select"
                style="width: 200px"
                @keyup.enter="debtInvoiceQuery"
                @clear="debtInvoiceQuery"
              >
              </el-input>
              <el-button type="primary" icon="Search" @click="debtInvoiceQuery">搜索</el-button>
            </div>
          </div>
          <div class="table_box" style="max-height: 400px">
            <el-table
              ref="debtInvoiceTableRef"
              :data="debtInvoiceList"
              highlight-current-row
              :header-cell-style="tableHeaderColor"
              :border="true"
            >
              <el-table-column label="发票号" prop="invoice" width="160" show-overflow-tooltip />
              <el-table-column label="部门" prop="deptname" min-width="120" show-overflow-tooltip />
              <el-table-column
                label="客户"
                prop="customername"
                min-width="120"
                show-overflow-tooltip
              />
              <el-table-column label="业务员" prop="employname" width="120" show-overflow-tooltip />
              <el-table-column label="申请时间" prop="dapplytime" width="100">
                <template #default="scope">
                  <span>{{ dayjs(scope.row.dapplytime).format('YYYY-MM-DD') }}</span>
                </template>
              </el-table-column>
              <el-table-column
                label="发票金额"
                prop="namountapply"
                min-width="120"
                show-overflow-tooltip
                align="right"
              >
                <template #default="scope">
                  <span>{{ formatMoney(scope.row.namountapply, '2') }}</span>
                </template>
              </el-table-column>
              <el-table-column
                label="已还金额"
                prop="namountreceived"
                min-width="120"
                show-overflow-tooltip
                align="right"
              >
                <template #default="scope">
                  <span>{{ formatMoney(scope.row.namountreceived, '2') }}</span>
                </template>
              </el-table-column>
              <el-table-column label="类型" prop="iapplytype" width="60" show-overflow-tooltip>
                <template #default="scope">
                  <span>{{ scope.row.iapplytype === 1 ? '预开' : '挂开' }}</span>
                </template>
              </el-table-column>
              <el-table-column
                label="操作"
                width="140"
                :align="'center'"
                class-name="small-padding fixed-width"
                fixed="right"
              >
                <template #default="scope">
                  <el-button
                    v-if="!scope.row.bconfirm"
                    link
                    type="primary"
                    icon="Top"
                    size="small"
                    title="分配金额"
                    @click="onSelectInvoice(scope.row)"
                    >分配</el-button
                  >
                </template>
              </el-table-column>
            </el-table>
            <el-pagination
              v-model:current-page="queryInvoiceParams.page"
              class="pagination"
              style="margin-left: 10px; width: 100%"
              :page-sizes="pageSizes"
              :page-size="queryInvoiceParams.pageSize"
              small
              background
              layout="total, sizes, prev, pager, next, jumper"
              :total="invoicetotal"
              @size-change="handleSizeInvoiceChange"
              @current-change="handleCurrentInvoiceChange"
            />
          </div>
        </el-row>
        <el-row style="padding: 20px 0">
          <el-col>
            <span style="padding: 10px 0">已分配列表</span>
            <el-table :data="bankAccountAllocateList">
              <el-table-column label="发票号" prop="invoice" />
              <el-table-column label="发票金额" prop="namountapply" align="right">
                <template #default="scope">
                  <span>{{ formatMoney(scope.row.namountapply, '2') }}</span>
                </template>
              </el-table-column>
              <el-table-column label="分配金额" prop="namountreceived" align="right">
                <template #default="scope">
                  <span>{{ formatMoney(scope.row.namountreceived, '2') }}</span>
                </template>
              </el-table-column>
              <el-table-column label="合同号" prop="scontractnum" />
              <el-table-column label="创建者" prop="createempname" />
              <el-table-column label="创建日期" prop="dcreatetime">
                <template #default="scope">
                  <span>{{ dayjs(scope.row.dcreatetime).format('YYYY-MM-DD') }}</span>
                </template>
              </el-table-column>
            </el-table>
          </el-col>
        </el-row>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { EStatus } from '@/types/common/enumindex'
import { getBankAccountPageListApi } from '@/api/finance'
import { TDateTimeType, TDateType } from '@/types/components/hgdateindex'
import { THgFileUploadParam, TUpFile } from '@/types/components/hgfileupload'
import { ITwbankaccounts, TBankAccountQuery } from '@/types/views/finance'
import { computTableHeight, tableHeaderColor, formatMoney } from '@/utils'
import { IAxios } from 'axios'
import BankAccountHistory from '@/views/finance/BankAccountHistory.vue'
import {
  ICustomerChargeBankDTO,
  IOrderDebtDTO,
  IPreInvoiceApplicationDTO,
  TBankCustomerChargeQuery,
  TInvoiceApplicationVO,
  Twcustomercharge
} from '@/types/views/business/bankaccountallocate'
import { dayjs } from 'element-plus'
import {
  bankAcountWriteOffApi,
  getBankwCustomerChargePageListApi,
  getCustomerChargeByBankidApi,
  getDebtInvoiceApplicationPageListApi,
  saveBankCustomerChargeApi
} from '@/api/business/bankaccountallocate'
import { IDataCombo } from '@/types/common/DataCombo'
import { getPaymethodComboApi } from '@/api/finance/paymethod'
import { getPreInvoiceApplyDtoByIdApi } from '@/api/finance/invoicecharge'
import { Tworderitem } from '@/types/views/ad/adtworder'

const total = ref(0)
/** 表格高度 */
const tableheight = ref(0)
/** 导入窗口显示状态 */
const dialogImportVisible = ref(false)
/** 流水历史显示状态 */
const dialogHistoryVisible = ref(false)
/** 查看详情窗口显示状态 */
const dialogDetailVisible = ref(false)
/** 分配窗口显示状态 */
const dialogAllocationVisible = ref(false)
/** 银行流水分配情况列表 */
const customerChargeList = ref<ICustomerChargeBankDTO[]>([])
/** 每页选择下拉 */
const pageSizes = ref<number[]>([10, 20, 30, 40])
/** 银行流水可以分配的总额度 */
const unnamountreceived = ref(0)
/** 是否提交分配 */
const bflagUpdate = ref(false)
/** 当前发票欠款金额 */
const invoiceAmountArrearage = ref(0)
/** 付款方式下拉 */
const paymethodCombo = ref<IDataCombo[]>([])
/** 银行流水分配发票列表 */
const bankAccountAllocateList = ref<ICustomerChargeBankDTO[]>([])
/** 当前选择的银行流水 */
const curBankAccount = ref<ITwbankaccounts>()
/** 当前选择发票 */
const curInvoice = ref<IPreInvoiceApplicationDTO>()
const bankAccountAllocateInfo: ICustomerChargeBankDTO = {
  id: '',
  bankaccountid: '',
  // bassignorder: false,
  invoice: '',
  invoiceid: '',
  istatus: 0,
  namountreceived: 0,
  oldnamountreceived: 0,
  orderid: '',
  scontractnum: '',
  sremark: '',
  employid: '',
  employname: '',
  customerid: '',
  customername: '',
  preinvoiceapplicationid: '',
  paymethodid: '',
  paymethodname: ''
}
const bankAccountAllocateForm = ref<ICustomerChargeBankDTO>({ ...bankAccountAllocateInfo })
/** 当前人员银行流水分配查询条件 */
const queryAllocateAllParams = reactive<TBankCustomerChargeQuery>({
  page: 1,
  pageSize: 20,
  startTime: '',
  endTime: ''
})
/** 已分配金额的发票列表总数 */
const bankAllocatetotal = ref(0)
/** 欠款发票查询条件 */
const queryInvoiceParams = reactive<TInvoiceApplicationVO>({
  page: 1,
  pageSize: 20,
  startTime: '',
  endTime: ''
})
/** 预开发票列表 */
const debtInvoiceList = ref<IPreInvoiceApplicationDTO[]>([])
/** 欠款发票列表总数 */
const invoicetotal = ref(0)
/** 预开发票对应的订单详情列表 */
const preInvoiceOrderItemList = ref<Tworderitem[]>([])
/** 选中的订单行 */
const multipleSelection = ref<Tworderitem[]>([])
/** 核销日期 */
const writeOffDate = ref(dayjs().format('YYYY-MM-DD'))

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
const queryParams = reactive<TBankAccountQuery>({
  page: 1,
  pageSize: 20,
  startTime: '',
  endTime: '',
  queryKey: ''
})
/** 上传组件参数 */
const hgfileuploadparam = ref<THgFileUploadParam>({
  server: import.meta.env.VITE_APP_BASE_API + '/finance/twbankaccounts/importBankAccount',
  accept: {
    title: 'Excel',
    extensions: 'xlsx,xls',
    mimeTypes: '.xlsx,.xls'
  },
  multiple: false,
  filenumlimit: 1,
  storytypes: []
})

/** 银行流水列表 */
const bankAccountList = ref<ITwbankaccounts[]>([])
onMounted(() => {
  getPaymethodCombo()
  handleQuery()
  /**
   * 计算表格高度
   */
  tableheight.value = computTableHeight()
})
/**
 * 付款方式下拉
 */
const getPaymethodCombo = () => {
  getPaymethodComboApi().then(({ success, obj }: IAxios<IDataCombo[]>) => {
    if (success) {
      paymethodCombo.value = obj
    }
  })
}
/** 导入按钮事件 */
const importBankAccount = () => {
  dialogImportVisible.value = true
}
/**
 * 查询
 */
const handleQuery = () => {
  getBankAccountPageListApi(queryParams)
    .then((res: IAxios<ITwbankaccounts[]>) => {
      if (res.success) {
        bankAccountList.value = res.obj
        total.value = res.total
      }
    })
    .finally(() => {})
}
/**
 * 获取上传后返回的文件列表
 * @param res
 */
const getUpFile = (res: IAxios<TUpFile>) => {
  console.log(res.success)
  if (res.success) {
    ElMessage.success('导入成功')
    handleQuery()
  }
}
/** 查看历史 */
const onHistory = () => {
  dialogHistoryVisible.value = true
}
/**
 * 查看详情按钮事件
 * @param row
 */
const onDetail = (row: Twcustomercharge) => {
  dialogDetailVisible.value = true
  const data = {
    bankid: row.id!
  }
  getCustomerChargeByBankidApi(data).then((res: IAxios<ICustomerChargeBankDTO[]>) => {
    if (res.success) {
      customerChargeList.value = res.obj
    }
  })
}
/**
 * 银行流水分配查询
 */
const bankAccountAllocateQuery = (data?: TBankCustomerChargeQuery) => {
  if (data === undefined) {
    data = {
      page: queryAllocateAllParams.page,
      pageSize: queryAllocateAllParams.pageSize
    }
  }
  getBankwCustomerChargePageListApi(data)
    .then((res: IAxios<ICustomerChargeBankDTO[]>) => {
      if (res.success) {
        bankAccountAllocateList.value = res.obj
        bankAllocatetotal.value = res.total
      }
    })
    .finally(() => {})
}
/**
 * 分配金额
 * @param row
 */
const onAllocation = (row: ITwbankaccounts) => {
  curBankAccount.value = { ...row }
  curInvoice.value = undefined
  preInvoiceOrderItemList.value = []
  debtInvoiceList.value = []
  // 银行流水导入总额度
  const namount = row.namount ? row.namount : 0
  // 银行流水已分配额度
  const namountallocate = row.namountallocate ? row.namountallocate : 0
  // 银行流水还未分配额度
  unnamountreceived.value = namount - namountallocate
  dialogAllocationVisible.value = true
  setTimeout(() => {
    debtInvoiceQuery()
  }, 100)
}
/**
 * 选择发票事件
 * @param row
 */
const onSelectInvoice = (row: IPreInvoiceApplicationDTO) => {
  if (row.namountapply === row.namountreceived) {
    ElMessage.warning('该发票已还清')
    return
  }
  curInvoice.value = { ...row }
  const orderids: string[] = []
  if (row.orders?.length! > 0) {
    row.orders?.forEach((item: IOrderDebtDTO) => {
      orderids.push(item.id!)
    })
  }
  const data: TBankCustomerChargeQuery = {
    page: 1,
    pageSize: 100,
    invoiceid: row.invoiceid,
    orderid: orderids.join(',')
  }
  bankAccountAllocateQuery(data)
  let amount = 0
  bankAccountAllocateList.value.forEach((item: ICustomerChargeBankDTO) => {
    if (item.invoiceid === row.id) {
      amount += item.namountreceived ? Number(item.namountreceived) : 0
    }
  })
  if (amount >= row.namountapply) {
    ElMessage.warning('该发票已还清')
    return
  }

  // 发票金额
  const namountapply = curInvoice.value?.namountapply ? curInvoice.value?.namountapply : 0
  // 已还金额
  const namountreceived = curInvoice.value?.namountreceived ? curInvoice.value?.namountreceived : 0
  // 欠款金额
  invoiceAmountArrearage.value = namountapply - namountreceived
  dialogAllocationVisible.value = true
  bankAccountAllocateForm.value = {
    ...bankAccountAllocateInfo,
    bankaccountid: curBankAccount.value!.id,
    namountreceived: 0,
    // bassignorder: false,
    istatus: 0,
    invoiceid: row.invoiceid,
    invoice: row.invoice,
    orders: row.orders,
    employid: row.employid,
    employname: row.employname,
    customerid: row.customerid,
    customername: row.customername,
    preinvoiceapplicationid: row.id
  }
  const data1 = {
    preinvoiceapplicationId: row.id!,
    orderId: ''
  }
  getPreInvoiceApplyDtoByIdApi(data1).then((res: IAxios<IPreInvoiceApplicationDTO>) => {
    if (res.success) {
      preInvoiceOrderItemList.value = res.obj!.orderItems!
      if (preInvoiceOrderItemList.value.length <= 0) {
        ElMessage.warning('该发票没有对应的订单')
        onSelectCancel()
      }
    }
  })
}
/**
 * 取消选择发票
 */
const onSelectCancel = () => {
  curInvoice.value = undefined
  preInvoiceOrderItemList.value = []
}
/**
 * 行选择事件
 * @param val
 */
const handleSelectionChange = (val: IOrderDebtDTO[]) => {
  multipleSelection.value = val
}
/**
 * 欠款发票查询
 */
const debtInvoiceQuery = () => {
  getDebtInvoiceApplicationPageListApi(queryInvoiceParams)
    .then((res: IAxios<IPreInvoiceApplicationDTO[]>) => {
      if (res.success) {
        debtInvoiceList.value = res.obj
        invoicetotal.value = res.total
      }
    })
    .finally(() => {})
}
/**
 * 验证分配额度是否超过总额度
 * @param row
 */
const validateAllcate = (row?: IOrderDebtDTO) => {
  if (bankAccountAllocateForm.value.namountreceived! === 0) {
    ElMessage.warning('请先设置发票分配总额度')
    return false
  }
  if (row && row.amountreceived! > row.amountarrearage!) {
    ElMessage.warning('分配金额不能大于欠款金额')
    row.amountreceived = row.amountarrearage
    return false
  }

  return true
}
/**
 * 验证发票分配额度的合理性
 */
const validateInvoice = () => {
  if (invoiceAmountArrearage.value < bankAccountAllocateForm.value.namountreceived!) {
    ElMessage.warning('分配金额不能大于欠款金额')
    bankAccountAllocateForm.value.namountreceived = invoiceAmountArrearage.value
    return false
  }
  if (unnamountreceived.value < bankAccountAllocateForm.value.namountreceived!) {
    ElMessage.warning('分配金额不能大于银行总金额')
    bankAccountAllocateForm.value.namountreceived = unnamountreceived.value
    return false
  }
  return true
}
/**
 * 编辑保存
 * @param formEl
 */
const saveForm = () => {
  if (bflagUpdate.value) {
    bankAccountAllocateForm.value.istatus = EStatus.已提交
  }
  const invoiceBflag = validateInvoice()
  if (!invoiceBflag) {
    return
  }
  // if (bankAccountAllocateForm.value.bassignorder) {
  const bflag = validateAllcate()
  if (!bflag) {
    return
  }
  // }
  if (
    bankAccountAllocateForm.value.paymethodid === '' ||
    bankAccountAllocateForm.value.paymethodid === null
  ) {
    ElMessage.warning('请选择付款方式')
    return
  }
  if (!writeOffDate.value) {
    ElMessage.warning('请选择核销日期')
    return
  }
  if (multipleSelection.value.length === 0) {
    ElMessage.warning('请选择订单')
    return
  }
  paymethodCombo.value.find((item: IDataCombo) => {
    if (item.id === bankAccountAllocateForm.value.paymethodid) {
      bankAccountAllocateForm.value.paymethodname = item.name
    }
  })
  bankAccountAllocateForm.value.versionBankAccount = curBankAccount.value!.version
  let amount = 0
  const ids: string[] = []
  multipleSelection.value.forEach((item: Tworderitem) => {
    amount += item.amountarrearage!
    ids.push(item.id!)
  })
  const namountbalance = bankAccountAllocateForm.value.namountreceived!
  if (namountbalance > amount) {
    ElMessage.warning('分配金额不能大于欠款金额')
    return
  }
  const message = `核销金额为${namountbalance}元,选择的订单总额为${amount}，是否确认核销？`
  ElMessageBox.confirm(message, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(() => {
      const data = {
        ...bankAccountAllocateForm.value,
        itemids: ids.join(','),
        dateString: writeOffDate.value
      }
      bankAcountWriteOffApi(data)
        .then((res: IAxios) => {
          if (res.success) {
            ElMessage.success('操作成功')
            handleQuery()
            onSelectCancel()
            debtInvoiceQuery()
          }
        })
        .catch(() => {})
    })
    .catch(() => {})
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
 * 时间选择
 * @param val
 */
const ontimeInvoice = (val: TDateTimeType) => {
  let timev = false
  if (queryInvoiceParams.startTime) {
    timev = true
  }
  queryInvoiceParams.startTime = val.startTime
  queryInvoiceParams.endTime = val.endTime
  if (timev) {
    debtInvoiceQuery()
  }
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
 * 改变页码总数时
 */
const handleSizeInvoiceChange = (val: number) => {
  queryInvoiceParams.pageSize = val
  debtInvoiceQuery()
}
/**
 * 改变页数时
 * @param val
 */
const handleCurrentInvoiceChange = (val: number) => {
  queryInvoiceParams.page = val
  debtInvoiceQuery()
}
</script>

<style scoped></style>
