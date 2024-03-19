<!--
 * @Author: suny
 * @Date: 2023-12-06 14:43:03
 * @LastEditTime: 2024-03-15 08:43:26
 * @LastEditors: suny
 * @Description: 到款核对，银行账户分配
-->
<template>
  <div class="app-container">
    <el-row>
      <el-col :span="12">
        <div class="search_box">
          <div class="flex">
            <HgDateIndex :initdate="timedata" @onresponse="ontimebankAccount"></HgDateIndex>
            <el-input
              v-model="queryBankParams.queryKey"
              clearable
              placeholder="请输入银行流水关键字"
              class="input-with-select"
              style="width: 200px"
              @keyup.enter="bankAccountQuery"
              @clear="bankAccountQuery"
            >
            </el-input>
            <el-button type="primary" icon="Search" @click="bankAccountQuery">搜索</el-button>
          </div>
        </div>
        <div class="table_box">
          <el-table
            ref="bankAccountTableRef"
            :data="bankAccountList"
            highlight-current-row
            :height="tableheight"
            :header-cell-style="tableHeaderColor"
            :border="true"
            @row-click="onSelect"
          >
            <el-table-column width="50" :align="'center'">
              <template #default="scope">
                <el-checkbox
                  v-model="scope.row.checked"
                  @change="onSelect(scope.row)"
                ></el-checkbox>
              </template>
            </el-table-column>
            <el-table-column
              label="借方名称"
              prop="sborrowername"
              min-width="120"
              show-overflow-tooltip
            />
            <el-table-column label="贷方名称" prop="slendername" min-width="160">
              <template #default="scope">
                <el-button type="primary" link size="small" @click="onDetail(scope.row)">{{
                  scope.row.slendername
                }}</el-button>
                <!-- <span @click="onDetail(scope.row)">{{ scope.row.slendername }}</span> -->
              </template>
            </el-table-column>
            <el-table-column
              label="导入金额"
              prop="namount"
              min-width="100"
              show-overflow-tooltip
              align="right"
            >
              <template #default="scope">
                <span>{{ formatMoney(scope.row.namount, '2') }}</span>
              </template>
            </el-table-column>
            <el-table-column
              label="分配金额"
              prop="namountallocate"
              min-width="100"
              show-overflow-tooltip
              align="right"
            >
              <template #default="scope">
                <span>{{ formatMoney(scope.row.namountallocate, '2') }}</span>
              </template>
            </el-table-column>
            <el-table-column label="交易日期" prop="dtradedate" width="100">
              <template #default="scope">
                <span>{{ dayjs(scope.row.dtradedate).format('YYYY-MM-DD') }}</span>
              </template>
            </el-table-column>
            <el-table-column label="备注" prop="sremark" min-width="140" show-overflow-tooltip />
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
                  type="success"
                  icon="Edit"
                  size="small"
                  title="选择"
                  @click="onSelect(scope.row)"
                  >选择</el-button
                >
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
            v-model:current-page="queryBankParams.page"
            class="pagination"
            style="margin-left: 10px; width: 100%"
            :page-sizes="pageSizes"
            :page-size="queryBankParams.pageSize"
            small
            background
            layout="total, sizes, prev, pager, next, jumper"
            :total="banktotal"
            @size-change="handleSizeBankChange"
            @current-change="handleCurrentBankChange"
          />
        </div>
      </el-col>
      <el-col :span="12">
        <div class="search_box">
          <div class="flex">
            <HgDateIndex :initdate="timedata" @onresponse="ontimeInvoice"></HgDateIndex>
            <el-input
              v-model="queryInvoiceParams.queryKey"
              clearable
              placeholder="请输入发票关键字"
              class="input-with-select"
              style="width: 200px"
              @keyup.enter="debtInvoiceQuery"
              @clear="debtInvoiceQuery"
            >
            </el-input>
            <el-button type="primary" icon="Search" @click="debtInvoiceQuery">搜索</el-button>
          </div>
        </div>
        <div class="table_box">
          <el-table
            ref="debtInvoiceTableRef"
            :data="debtInvoiceList"
            highlight-current-row
            :height="tableheight"
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
                  @click="onAllocation(scope.row)"
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
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="24">
        <div class="search_box">
          <div class="flex">
            <HgDateIndex :initdate="timedata" @onresponse="ontimeAllocate"></HgDateIndex>
            <el-button type="primary" icon="Search" @click="bankAccountAllocateAllQuery"
              >搜索</el-button
            >
          </div>
        </div>
        <div class="table_box">
          <el-table
            ref="bankAccountAllocateAllTableRef"
            :data="bankAccountAllocateAllList"
            highlight-current-row
            :height="tableheight"
            :header-cell-style="tableHeaderColor"
            :border="true"
          >
            <el-table-column label="贷方名称" prop="slendername" />
            <el-table-column label="借方名称" prop="sborrowername" />
            <el-table-column label="发票号" prop="invoice" />
            <el-table-column
              label="发票金额"
              prop="namountapply"
              width="140"
              show-overflow-tooltip
              align="right"
            >
              <template #default="scope">
                <span>{{ formatMoney(scope.row.namountapply, '2') }}</span>
              </template>
            </el-table-column>
            <el-table-column
              label="分配金额"
              prop="namountreceived"
              width="140"
              show-overflow-tooltip
              align="right"
            >
              <template #default="scope">
                <span>{{ formatMoney(scope.row.namountreceived, '2') }}</span>
              </template>
            </el-table-column>
            <el-table-column label="合同号" prop="scontractnum" width="150" />
            <el-table-column label="创建者" prop="createempname" width="100" />
            <el-table-column label="创建日期" prop="dcreatetime" width="120">
              <template #default="scope">
                <span>{{ dayjs(scope.row.dcreatetime).format('YYYY-MM-DD') }}</span>
              </template>
            </el-table-column>
            <el-table-column label="状态" prop="istatus" width="120">
              <template #default="scope">
                {{ statusCombo.find((t) => t.id === scope.row.istatus)?.name }}
              </template>
            </el-table-column>
            <el-table-column
              label="操作"
              width="180"
              :align="'center'"
              class-name="small-padding fixed-width"
              fixed="right"
            >
              <template #default="scope">
                <el-button
                  v-if="
                    scope.row.istatus === EStatus.待提交 || scope.row.istatus === EStatus.已退回
                  "
                  link
                  type="primary"
                  icon="Edit"
                  size="small"
                  title="修改"
                  @click="onEdit(scope.row)"
                  >修改</el-button
                >
                <el-button
                  v-if="
                    scope.row.istatus === EStatus.待提交 || scope.row.istatus === EStatus.已退回
                  "
                  link
                  type="primary"
                  icon="Top"
                  size="small"
                  title="提交"
                  @click="onCheck(scope.row)"
                  >提交</el-button
                >
                <el-button
                  v-if="
                    scope.row.istatus === EStatus.待提交 || scope.row.istatus === EStatus.已退回
                  "
                  link
                  type="primary"
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
            v-model:current-page="queryAllocateAllParams.page"
            class="pagination"
            style="margin-left: 10px; width: 100%"
            :page-sizes="pageSizes"
            :page-size="queryAllocateAllParams.pageSize"
            small
            background
            layout="total, sizes, prev, pager, next, jumper"
            :total="bankAllocatetotal"
            @size-change="handleSizeAllocateAllChange"
            @current-change="handleCurrentAllocateAllChange"
          />
        </div>
      </el-col>
    </el-row>
    <!-- 金额分配 -->
    <el-dialog
      v-model="dialogAllocationVisible"
      title="到账分配金额"
      width="1200"
      height="500"
      append-to-body
      :destroy-on-close="true"
    >
      <div style="width: 92%; height: 400px">
        <el-row>
          <el-col :span="8">
            <el-row>
              <el-col :span="24">
                <el-space wrap>
                  <span>总额度</span>
                  <el-input v-model="unnamountreceived" style="width: 160px" disabled></el-input>
                </el-space>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-space wrap>
                  <span>发票号</span>
                  <el-input v-model="curInvoice!.invoice" style="width: 160px" disabled></el-input>
                </el-space>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-space wrap>
                  <span>付款方式</span>
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
                </el-space>
              </el-col>
            </el-row>
          </el-col>
          <el-col :span="16">
            <div>
              <el-space wrap>
                <span>发票金额</span>
                <el-input v-model="curInvoice!.namountapply" disabled style="width: 100px" />
                <span>已收金额</span>
                <el-input v-model="curInvoice!.namountreceived" disabled style="width: 100px" />
                <span>分配金额</span>
                <el-input
                  v-model="bankAccountAllocateForm.namountreceived"
                  style="width: 100px"
                  @blur="validateInvoice"
                />
              </el-space>
              <p>
                <el-checkbox
                  v-if="bankAccountAllocateForm.orders?.length! > 0"
                  v-model="bankAccountAllocateForm.bassignorder"
                  style="margin-right: 10px"
                  @change="clearAllocate"
                  >指定分配合同</el-checkbox
                >
              </p>
            </div>
            <div v-if="bankAccountAllocateForm.orders?.length! > 0">
              <span>分配目标</span>
              <el-table :data="bankAccountAllocateForm.orders">
                <el-table-column label="订单编号" prop="sordernum" />
                <el-table-column label="合同号" prop="scontractnum" />
                <el-table-column label="订单金额" prop="amountreceivable" align="right">
                  <template #default="scope">
                    <span>{{ formatMoney(scope.row.amountreceivable, '2') }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="欠款金额" prop="amountarrearage" align="right">
                  <template #default="scope">
                    <span>{{ formatMoney(scope.row.amountarrearage, '2') }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="分配金额" prop="amountreceived">
                  <template #default="scope">
                    <el-input
                      v-model="scope.row.amountreceived"
                      :disabled="!bankAccountAllocateForm.bassignorder"
                      placeholder="请输入分配金额"
                      @blur="validateAllcate(scope.row)"
                    ></el-input>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </el-col>
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
      <div style="position: absolute; right: 20px; bottom: 20px">
        <el-checkbox v-model="bflagUpdate" style="margin-right: 10px"
          >同时提交(提交后不允许修改)</el-checkbox
        >
        <el-button type="primary" icon="Check" @click="saveForm">保存</el-button>
        <el-button icon="Close" @click="dialogAllocationVisible = false">取消</el-button>
      </div>
    </el-dialog>
    <!-- 金额分配修改 -->
    <el-dialog
      v-model="dialogEditAllocationVisible"
      title="分配金额修改"
      width="1000"
      height="500"
      append-to-body
      :destroy-on-close="true"
    >
      <div style="width: 92%; height: 400px">
        <el-row style="padding: 20px 0">
          <el-col>
            <span style="padding: 10px 0">已分配列表</span>
            <el-table :data="bankAccountAllocateEditList">
              <el-table-column label="发票号" prop="invoice" />
              <el-table-column label="合同号" prop="scontractnum" />
              <el-table-column label="发票金额" prop="namountapply" align="right">
                <template #default="scope">
                  <span>{{ formatMoney(scope.row.namountapply, '2') }}</span>
                </template>
              </el-table-column>
              <el-table-column label="分配金额" prop="namountreceived">
                <template #default="scope">
                  <el-input
                    v-model="scope.row.namountreceived"
                    :disabled="
                      scope.row.id !== editbankAccountAllocate?.id &&
                      scope.row.istatus !== EStatus.待提交 &&
                      scope.row.istatus !== EStatus.已退回
                    "
                    placeholder="请输入分配金额"
                    @blur="validateEditAllcate(scope.row)"
                  ></el-input>
                </template>
              </el-table-column>
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
      <div style="position: absolute; right: 20px; bottom: 20px">
        <el-checkbox v-model="bflagUpdate" style="margin-right: 10px"
          >同时提交(提交后不允许修改)</el-checkbox
        >
        <el-button type="primary" icon="Check" @click="saveEditForm">保存</el-button>
        <el-button icon="Close" @click="dialogEditAllocationVisible = false">取消</el-button>
      </div>
    </el-dialog>
    <!-- 银行流水详情 -->
    <el-dialog
      v-model="dialogDetailVisible"
      title="银行流水详情"
      width="1000"
      height="500"
      append-to-body
      :destroy-on-close="true"
    >
      <el-form :model="curBankAccount" label-width="160px">
        <el-form-item label="账号">
          <span>{{ curBankAccount!.sborroweraccount }}</span>
        </el-form-item>
        <el-form-item label="账户名称">
          <span>{{ curBankAccount!.sborrowername }}</span>
        </el-form-item>
        <el-form-item label="对方账号">
          <span>{{ curBankAccount!.slenderaccount }}</span>
        </el-form-item>
        <el-form-item label="对方户名">
          <span>{{ curBankAccount!.slendername }}</span>
        </el-form-item>
        <el-form-item label="凭证种类">
          <span>{{ curBankAccount!.scredentialtype }}</span>
        </el-form-item>
        <el-form-item label="贷方发生额">
          <span>{{ formatMoney(curBankAccount!.namount, '2') }}</span>
        </el-form-item>
        <el-form-item label="已分配金额">
          <span>{{ formatMoney(curBankAccount!.namountallocate, '2') }}</span>
        </el-form-item>
        <el-form-item label="明细编号—交易流水号">
          <span>{{ curBankAccount!.stradecode }}</span>
        </el-form-item>
        <el-form-item label="明细类型">
          <span>{{ curBankAccount!.sdetailtype }}</span>
        </el-form-item>
        <el-form-item label="交易类别">
          <span>{{ curBankAccount!.stradetype }}</span>
        </el-form-item>
        <el-form-item label="交易日期">
          <span>{{ dayjs(curBankAccount!.dtradedate).format('YYYY-MM-DD') }}</span>
        </el-form-item>
        <el-form-item label="备注">
          <span>{{ curBankAccount!.sremark }}</span>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>
<script setup lang="ts">
import { EStatus } from '@/types/common/enumindex'
import { TDateTimeType, TDateType } from '@/types/components/hgdateindex'
import {
  IPreInvoiceApplicationDTO,
  TBankCustomerChargeQuery,
  TInvoiceApplicationVO,
  ICustomerChargeBankDTO,
  IOrderDebtDTO
} from '@/types/views/business/bankaccountallocate'
import { IAxios } from 'axios'
import { ITwbankaccounts } from '@/types/views/business/bankaccountallocate'
import { reactive, ref } from 'vue'
import { computTableHeight, tableHeaderColor, formatMoney } from '@/utils'
import { FormInstance, dayjs, ElTable } from 'element-plus'
import {
  deleteBankCustomerChargeApi,
  getBalanceBankAccountPageListApi,
  getBankwCustomerChargePageListApi,
  getDebtInvoiceApplicationPageListApi,
  saveBankCustomerChargeApi,
  updateBankCustomerChargeApi,
  updateCustomerChargeStatusApi
} from '@/api/business/bankaccountallocate'
import { IDataCombo } from '@/types/common/DataCombo'
import { getPaymethodComboApi } from '@/api/finance/paymethod'
import { getEnumCombo } from '@/api/common/index'

/** 状态下拉 */
const statusCombo: IDataCombo[] = getEnumCombo(EStatus)
const bankAccountTableRef = ref<InstanceType<typeof ElTable>>()
const bankAccountAllocateFormRef = ref<FormInstance>()
const total = ref(0)
/** 表格高度 */
const tableheight = ref(0)
/** 银行流水列表总数 */
const banktotal = ref(0)
/** 欠款发票列表总数 */
const invoicetotal = ref(0)
/** 已分配金额的发票列表总数 */
const bankAllocatetotal = ref(0)
/** 当前人员银行流水分配列表 */
const bankAllocateAlltotal = ref(0)
/** 分配窗口显示状态 */
const dialogAllocationVisible = ref(false)
/** 分配修改窗口显示状态 */
const dialogEditAllocationVisible = ref(false)
/** 银行流水详情窗口显示状态 */
const dialogDetailVisible = ref(false)
/** 每页选择下拉 */
const pageSizes = ref<number[]>([10, 20, 30, 40])

/** 银行流水查询条件 */
const queryBankParams = reactive<TBankCustomerChargeQuery>({
  page: 1,
  pageSize: 20,
  startTime: '',
  endTime: ''
})
/** 欠款发票查询条件 */
const queryInvoiceParams = reactive<TInvoiceApplicationVO>({
  page: 1,
  pageSize: 20,
  startTime: '',
  endTime: ''
})
/** 当前人员银行流水分配查询条件 */
const queryAllocateAllParams = reactive<TBankCustomerChargeQuery>({
  page: 1,
  pageSize: 20,
  startTime: '',
  endTime: ''
})
const bankAccountAllocateInfo: ICustomerChargeBankDTO = {
  id: '',
  bankaccountid: '',
  bassignorder: false,
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
/** 当前选择发票 */
const curInvoice = ref<IPreInvoiceApplicationDTO>()
/** 当前选择的银行流水 */
const curBankAccount = ref<ITwbankaccounts>()
/** 银行流水列表 */
const bankAccountList = ref<ITwbankaccounts[]>([])
/** 预开发票列表 */
const debtInvoiceList = ref<IPreInvoiceApplicationDTO[]>([])
/** 银行流水分配发票列表 */
const bankAccountAllocateList = ref<ICustomerChargeBankDTO[]>([])
/** 当前人员银行流水分配列表 */
const bankAccountAllocateAllList = ref<ICustomerChargeBankDTO[]>([])
/** 当前修改编辑分配列表 */
const bankAccountAllocateEditList = ref<ICustomerChargeBankDTO[]>([])
/** 当前修改编辑分配对象 */
const editbankAccountAllocate = ref<ICustomerChargeBankDTO>()
/** 银行流水可以分配的总额度 */
const unnamountreceived = ref(0)
/** 是否提交分配 */
const bflagUpdate = ref(false)
/** 当前发票欠款金额 */
const invoiceAmountArrearage = ref(0)
/** 付款方式下拉 */
const paymethodCombo = ref<IDataCombo[]>([])

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
  getPaymethodCombo()
  bankAccountQuery()
  debtInvoiceQuery()
  bankAccountAllocateAllQuery()
  /**
   * 计算表格高度
   */
  tableheight.value = computTableHeight() / 2
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
/**
 * 编辑金额分配
 * @param row
 */
const onEdit = (row: ICustomerChargeBankDTO) => {
  dialogEditAllocationVisible.value = true
  editbankAccountAllocate.value = { ...row }

  bankAccountAllocateEditList.value = []
  bankAccountAllocateEditList.value.push(editbankAccountAllocate.value)

  // const data: TBankCustomerChargeQuery = {
  //   page: 1,
  //   pageSize: 100,
  //   invoiceid: row.invoiceid
  // }
  // bankAccountAllocateQuery(data)
}
/**
 * 当前人员银行流水分配查询
 */
const bankAccountAllocateAllQuery = () => {
  getBankwCustomerChargePageListApi(queryAllocateAllParams)
    .then((res: IAxios<ICustomerChargeBankDTO[]>) => {
      if (res.success) {
        bankAccountAllocateAllList.value = res.obj
        bankAllocateAlltotal.value = res.total
      }
    })
    .finally(() => {})
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
 * 银行流水查询
 */
const bankAccountQuery = () => {
  getBalanceBankAccountPageListApi(queryBankParams)
    .then((res: IAxios<ITwbankaccounts[]>) => {
      if (res.success) {
        console.log(res.obj)
        bankAccountList.value = res.obj
        banktotal.value = res.total
      }
    })
    .finally(() => {})
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
 * 选择需要分配的银行流水
 * @param row
 */
const onSelect = (row: ITwbankaccounts) => {
  bankAccountList.value.forEach((item: ITwbankaccounts) => {
    item.checked = false
  })
  row.checked = !row.checked
  curBankAccount.value = { ...row }
  // 银行流水导入总额度
  const namount = row.namount ? row.namount : 0
  // 银行流水已分配额度
  const namountallocate = row.namountallocate ? row.namountallocate : 0
  // 银行流水还未分配额度
  unnamountreceived.value = namount - namountallocate
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
 * 取消指定订单时清空订单分配的额度
 */
const clearAllocate = () => {
  if (!bankAccountAllocateForm.value.bassignorder) {
    curInvoice.value?.orders!.forEach((item: IOrderDebtDTO) => {
      item.amountreceived = undefined
    })
  }
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
  let amount = 0
  curInvoice.value?.orders!.forEach((item: IOrderDebtDTO) => {
    amount += item.amountreceived ? Number(item.amountreceived) : 0
  })
  if (amount === 0) {
    ElMessage.warning('请设置分配金额')
    return false
  }
  if (amount > bankAccountAllocateForm.value.namountreceived!) {
    ElMessage.warning('分配金额不能大于发票分配的总额度')
    return false
  }
  return true
}
/**
 * 验证分配额度是否超过总额度
 * @param row
 */
const validateEditAllcate = (row?: ICustomerChargeBankDTO) => {
  if (row && row.namountreceived! > row.namountapply!) {
    ElMessage.warning('分配金额不能大于发票金额')
    row.namountreceived = row.namountapply
    return false
  }
  return true
}

/**
 * 分配金额
 * @param row
 */
const onAllocation = (row: IPreInvoiceApplicationDTO) => {
  if (!curBankAccount.value) {
    ElMessage.warning('请选择银行流水')
    return
  }
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
  debtInvoiceQuery()
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
    bankaccountid: curBankAccount.value.id,
    namountreceived: 0,
    bassignorder: false,
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
}
/**
 * 编辑保存
 * @param formEl
 */
const saveForm = () => {
  if (bflagUpdate.value) {
    bankAccountAllocateForm.value.istatus = EStatus.已提交
  }
  // if (curInvoice.value?.orders && curInvoice.value.orders.length > 0) {
  //   bankAccountAllocateForm.value.orders = curInvoice.value.orders
  // }
  const invoiceBflag = validateInvoice()
  if (!invoiceBflag) {
    return
  }
  if (bankAccountAllocateForm.value.bassignorder) {
    const bflag = validateAllcate()
    if (!bflag) {
      return
    }
  }
  if (
    bankAccountAllocateForm.value.paymethodid === '' ||
    bankAccountAllocateForm.value.paymethodid === null
  ) {
    ElMessage.warning('请选择付款方式')
    return
  }
  paymethodCombo.value.find((item: IDataCombo) => {
    if (item.id === bankAccountAllocateForm.value.paymethodid) {
      bankAccountAllocateForm.value.paymethodname = item.name
    }
  })
  bankAccountAllocateForm.value.versionBankAccount = curBankAccount.value!.version
  saveBankCustomerChargeApi(bankAccountAllocateForm.value)
    .then((res: IAxios) => {
      if (res.success) {
        ElMessage.success('保存成功')
        bankAccountQuery()
        debtInvoiceQuery()
        bankAccountAllocateAllQuery()
        bankAccountAllocateFormRef.value?.resetFields()
        dialogAllocationVisible.value = false
      }
    })
    .catch(() => {})
}
/**
 * 编辑保存
 * @param formEl
 */
const saveEditForm = () => {
  editbankAccountAllocate.value = { ...bankAccountAllocateEditList.value[0] }
  if (bflagUpdate.value) {
    editbankAccountAllocate.value!.istatus = EStatus.已提交
  }

  const bflag = validateEditAllcate(editbankAccountAllocate.value)
  if (!bflag) {
    return
  }

  updateBankCustomerChargeApi(editbankAccountAllocate.value!)
    .then((res: IAxios) => {
      if (res.success) {
        ElMessage.success('保存成功')
        bankAccountQuery()
        debtInvoiceQuery()
        bankAccountAllocateAllQuery()
        editbankAccountAllocate.value = {}
        dialogEditAllocationVisible.value = false
      }
    })
    .catch(() => {})
}
/**
 * 删除
 * @param row
 */
const onDelete = (row?: ICustomerChargeBankDTO) => {
  if (!row) {
    ElMessage.warning('请选择需要删除的分配')
    return
  }
  if (row.istatus !== EStatus.待提交 && row.istatus !== EStatus.已退回) {
    ElMessage.warning('该分配已提交，不允许删除')
    return
  }
  ElMessageBox.confirm('是否删除该分配？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(() => {
      const data = {
        ids: [row.id!].join(',')
      }
      deleteBankCustomerChargeApi(data)
        .then((res: IAxios) => {
          if (res.success) {
            ElMessage.success('删除成功')
            bankAccountQuery()
            debtInvoiceQuery()
            bankAccountAllocateAllQuery()
          }
        })
        .catch(() => {})
    })
    .catch(() => {})
}
/**
 * 查看银行流水详情
 * @param row
 */
const onDetail = (row: ITwbankaccounts) => {
  dialogDetailVisible.value = true
  curBankAccount.value = { ...row }
}
/**
 * 提交按钮事件
 * @param row
 */
const onCheck = (row: ICustomerChargeBankDTO) => {
  editbankAccountAllocate.value = { ...row }
  ElMessageBox.confirm('是否提交该分配？提交后不能再修改', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(() => {
      const data = {
        id: row.id!,
        istatus: EStatus.已提交
      }
      editbankAccountAllocate.value!.istatus = EStatus.已提交
      // updateCustomerChargeStatusApi(data)
      updateBankCustomerChargeApi(editbankAccountAllocate.value!)
        .then((res: IAxios) => {
          if (res.success) {
            ElMessage.success('操作成功')
            bankAccountQuery()
            debtInvoiceQuery()
            bankAccountAllocateAllQuery()
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
const ontimebankAccount = (val: TDateTimeType) => {
  let timev = false
  if (queryBankParams.startTime) {
    timev = true
  }
  queryBankParams.startTime = val.startTime
  queryBankParams.endTime = val.endTime
  if (timev) {
    bankAccountQuery()
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
 * 时间选择
 * @param val
 */
const ontimeAllocate = (val: TDateTimeType) => {
  let timev = false
  if (queryAllocateAllParams.startTime) {
    timev = true
  }
  queryAllocateAllParams.startTime = val.startTime
  queryAllocateAllParams.endTime = val.endTime
  if (timev) {
    const data: TBankCustomerChargeQuery = {
      page: 1,
      pageSize: 100
    }
    bankAccountAllocateQuery(data)
  }
}
/**
 * 改变页码总数时
 */
const handleSizeBankChange = (val: number) => {
  queryBankParams.pageSize = val
  bankAccountQuery()
}
/**
 * 改变页数时
 * @param val
 */
const handleCurrentBankChange = (val: number) => {
  queryBankParams.page = val
  bankAccountQuery()
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
/**
 * 改变页码总数时
 */
const handleSizeAllocateAllChange = (val: number) => {
  queryAllocateAllParams.pageSize = val
  bankAccountAllocateAllQuery()
}
/**
 * 改变页数时
 * @param val
 */
const handleCurrentAllocateAllChange = (val: number) => {
  queryAllocateAllParams.page = val
  bankAccountAllocateAllQuery()
}
</script>
<style lang="scss" scoped>
.inputwindht {
  width: 540px;
}
</style>
