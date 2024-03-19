<!--
 * @Author: suny
 * @LastEditTime: 2024-03-06 14:08:45
 * @LastEditors: wanghl
 * @Description: 预收款核销实现
1、根据客户或者发票号（可以不用这些查询条件），查询收费表中 有余额的 客户预收费项目（不需要关联其他，返回的对象中包含以下字段）
（客户编号、客户名称、客户属性、经营主体、业务员、发票号、开票日期、收费日期、收款金额、剩余金额、开票名称）我在CustomerChargeBankDTO对象中添加以下字段，可以直接用此DTO
2、按照刊发时间或者合同号，根据传递的客户id，查询订单表中的直接客户、代理公司或者内容生产方包customerid的所有 有欠款 的订单的广告明细（包含以下字段）
(合同号、经营主体、直接客户、代理公司、内容生产方、刊发日期、媒体、广告标题、应收金额、已收金额、欠款金额)
3、查看分摊详情列表
根据收费表id，查询广告分摊表详情，使用OrderApportiondetailDTO，包括以下字段：
合同号、经营主体、直接客户、代理公司、内容生产方、刊发日期、媒体、广告标题、签订金额、分摊金额、发票号、收费日期、分摊日期、收款种类、业务员）

4、保存分摊
参数：Twcustomercharge表的id，选择的orderitem数组的ids

保存流程：
1、按照时间正序对指定的ids中的广告详情分别进行分配
2、每条分配添加一条tworderapportiondetail广告分摊明细记录，同时将分摊情况会写到orderitem表对应的记录，同时记录好分配出去的额度和剩余额度
3、如果所有orderitem全部分配结束，仍有剩余，更新Twcustomercharge中的已用和剩余金额，不改状态标记
4、如果Twcustomercharge表中的额度全部分配完，则更新Twcustomercharge中的已用和剩余金额，同时将标记更新为已核销
-->
<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex">
        <HgDateIndex :initdate="timedata" @onresponse="ontimeCharge"></HgDateIndex>
        <el-input
          v-model="queryChargeParams.customername"
          placeholder="请选择客户信息"
          style="width: 200px"
          clearable
          @input="queryChargeParams.customerid = ''"
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
        <span style="width: 10px"></span>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
      </div>
    </div>
    <div class="table_box">
      <el-table
        ref="advanceChargeTableRef"
        :data="advanceChargeList"
        highlight-current-row
        :height="tableheight"
        :header-cell-style="tableHeaderColor"
        :border="true"
      >
        <el-table-column label="客户编号" prop="icode" width="100" />
        <el-table-column label="客户名称" prop="customername" />
        <el-table-column label="客户属性" prop="customeritype">
          <template #default="scope">
            {{
              customerTypeCombo.find((item: IDataCombo) => item.id === scope.row.customeritype)!
                .name
            }}
          </template>
        </el-table-column>
        <el-table-column label="经营主体" prop="businessentityname" />
        <el-table-column label="业务员" prop="employname" width="100" />
        <el-table-column label="发票号" prop="invoice" />
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
            {{ statusCombo.find((item: IDataCombo) => item.id === scope.row.istatus)!.name }}
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
          width="120"
          :align="'center'"
          class-name="small-padding fixed-width"
          fixed="right"
        >
          <template #default="scope">
            <el-button
              link
              title="已分摊详情"
              type="primary"
              size="small"
              icon="View"
              @click="onDetail(scope.row)"
              >详情</el-button
            >
            <!-- <el-button
              v-if="scope.row.istatus === EStatus.已提交"
              link
              type="primary"
              icon="Check"
              size="small"
              title="审核"
              @click="onCheck(scope.row)"
              >审核</el-button
            > -->
            <el-button
              v-if="scope.row.istatus === EStatus.已确认"
              link
              type="primary"
              icon="Finished"
              size="small"
              title="核销"
              @click="onCharge(scope.row)"
              >核销</el-button
            >
            <!-- <el-button
              v-if="scope.row.istatus === EStatus.已确认"
              link
              type="primary"
              icon="Back"
              size="small"
              title="退回"
              @click="handleBack(scope.row)"
              >退回</el-button
            > -->
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

    <!-- 查看详情 -->
    <el-dialog
      v-model="dialogDetailshow"
      title="查看详情"
      width="1000"
      append-to-body
      destroy-on-close
      draggable
      :close-on-click-modal="false"
      :z-index="2000"
    >
      <!-- <HgPreInvoiceApplicationDetail
        :selfdetailid="curAdvanceCharge!.preinvoiceapplicationid"
        style="max-width: 100%; overflow: auto"
      ></HgPreInvoiceApplicationDetail> -->
      <el-table
        :data="orderApportiondetailList"
        style="width: 100%; min-height: 200px; padding: 0 20px; max-height: 500px"
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
        <el-table-column
          label="经营主体"
          prop="businessentityname"
          width="120"
          show-overflow-tooltip
        />
        <el-table-column label="直接客户" prop="customername" width="120" show-overflow-tooltip />
        <el-table-column label="代理公司" prop="agencyname" width="120" show-overflow-tooltip />
        <el-table-column label="内容生产方" prop="agentname" width="120" show-overflow-tooltip />
        <el-table-column label="刊发日期" prop="prestartdate" width="120">
          <template #default="scope">
            <span>{{ dayjs(scope.row.prestartdate).format('YYYY-MM-DD') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="媒体" prop="medianame" width="120" show-overflow-tooltip />
        <el-table-column label="广告标题" prop="sadtitle" width="120" show-overflow-tooltip />
        <el-table-column label="签订金额" prop="amountreceivable" width="120" align="right">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.amountreceivable, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="分摊金额" prop="namountapportion" width="120" align="right">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.namountapportion, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="发票号" prop="invoice" width="120" show-overflow-tooltip />
        <el-table-column label="收费日期" prop="prestartdate" width="120">
          <template #default="scope">
            <span>{{ dayjs(scope.row.prestartdate).format('YYYY-MM-DD') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="分摊日期" prop="dapportiondate" width="120">
          <template #default="scope">
            <span>{{ dayjs(scope.row.dapportiondate).format('YYYY-MM-DD') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="收款种类" prop="sapportiontype" width="120">
          <template #default="scope">
            {{ sTypeCombo.find((item: IDataCombo) => item.id === scope.row.sapportiontype)!.name }}
          </template>
        </el-table-column>
        <el-table-column label="欠款金额" prop="amountarrearage" width="100" />
      </el-table>
      <!-- 根据合同号查看详情 -->
      <AdTworderdetails ref="viewImageRef"></AdTworderdetails>
    </el-dialog>
    <!-- 确认 -->
    <el-dialog
      v-model="dialogCheckshow"
      title="审核意见"
      width="800"
      append-to-body
      destroy-on-close
      draggable
      :close-on-click-modal="false"
    >
      <el-row>
        <el-col :span="24">
          <el-form label-width="100px" label-position="right">
            <el-form-item label="审核状态">
              <el-radio-group
                v-model="curIstatus"
                :disabled="curAdvanceCharge!.istatus === EStatus.已确认"
              >
                <el-radio :label="EStatus.已确认">确认</el-radio>
                <el-radio :label="EStatus.已退回">退回</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="审核说明">
              <el-input
                v-model="backreason"
                type="textarea"
                :rows="4"
                maxlength="200"
                show-word-limit
                placeholder="请输入审核说明"
              ></el-input>
            </el-form-item>
            <el-form-item>
              <div style="position: absolute; right: 5px">
                <el-button type="primary" icon="Check" @click="saveCheckForm">确认</el-button>
                <el-button icon="Close" @click="dialogCheckshow = false">取消</el-button>
              </div>
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
    </el-dialog>
    <!-- 核销 -->
    <el-dialog
      v-model="dialogChargeshow"
      title="核销"
      width="900"
      append-to-body
      destroy-on-close
      draggable
      :close-on-click-modal="false"
    >
      <el-row style="padding: 0 20px">
        <el-col :span="24">
          <el-form label-width="80px" label-position="right">
            <el-form-item label="收款金额">
              <el-input v-model="curAdvanceCharge!.namountreceived" disabled style="width: 400px" />
            </el-form-item>
            <el-form-item label="剩余金额">
              <el-input v-model="curAdvanceCharge!.namountbalance" disabled style="width: 400px" />
            </el-form-item>
            <el-form-item label="核销日期">
              <el-date-picker
                v-model="writeOffDate"
                type="date"
                placeholder="请选择核销日期"
                format="YYYY/MM/DD"
                style="width: 400px"
                value-format="YYYY-MM-DD"
              />
            </el-form-item>
            <el-form-item label="广告明细">
              <div style="width: 100%">
                <!-- <span>分配目标</span> -->
                <el-table
                  :data="orderItemList"
                  style="width: 100%"
                  @selection-change="handleSelectionChange"
                >
                  <el-table-column type="selection" width="50" :align="'center'" />
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
                    width="100"
                    show-overflow-tooltip
                  />
                  <el-table-column
                    label="直接客户"
                    prop="customername"
                    width="100"
                    show-overflow-tooltip
                  />
                  <el-table-column
                    label="代理公司"
                    prop="agencyname"
                    width="100"
                    show-overflow-tooltip
                  />
                  <el-table-column
                    label="内容生产方"
                    prop="agentname"
                    width="100"
                    show-overflow-tooltip
                  />
                  <el-table-column
                    label="刊发日期"
                    prop="prestartdate"
                    width="120"
                    show-overflow-tooltip
                  >
                    <template #default="scope">
                      <span>{{ dayjs(scope.row.prestartdate).format('YYYY-MM-DD') }}</span>
                    </template>
                  </el-table-column>
                  <el-table-column label="媒体" prop="medianame" width="100" />
                  <el-table-column label="广告标题" prop="sadtitle" show-overflow-tooltip />
                  <el-table-column
                    label="应收金额"
                    prop="amountreceivable"
                    width="120"
                    align="right"
                  >
                    <template #default="scope">
                      <span>{{ formatMoney(scope.row.amountreceivable, '2') }}</span>
                    </template>
                  </el-table-column>
                  <el-table-column label="已收金额" prop="amountreceived" width="120" align="right">
                    <template #default="scope">
                      <span>{{ formatMoney(scope.row.amountreceived, '2') }}</span>
                    </template>
                  </el-table-column>
                  <el-table-column
                    label="欠款金额"
                    prop="amountarrearage"
                    width="120"
                    align="right"
                  >
                    <template #default="scope">
                      <span>{{ formatMoney(scope.row.amountarrearage, '2') }}</span>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </el-form-item>
            <div style="width: 100%; text-align: center">
              <el-button type="primary" icon="Check" @click="saveChargeForm">确认</el-button>
              <el-button icon="Close" @click="dialogChargeshow = false">取消</el-button>
            </div>
          </el-form>
        </el-col>
      </el-row>
      <!-- 根据合同号查看详情 -->
      <AdTworderdetails ref="viewImageRef"></AdTworderdetails>
    </el-dialog>
  </div>
</template>
<script setup lang="ts">
import { ECustomerType, EStatus, ESType } from '@/types/common/enumindex'
import { TDateTimeType, TDateType } from '@/types/components/hgdateindex'
import {
  IPreInvoiceApplicationDTO,
  TBankCustomerChargeQuery,
  TInvoiceApplicationVO,
  ICustomerChargeBankDTO,
  IOrderDebtDTO
} from '@/types/views/business/bankaccountallocate'
import { IOrderApportiondetailDTO } from '@/types/views/finance/orderapportiondetail'
import { IAxios } from 'axios'
import { reactive, ref } from 'vue'
import { computTableHeight, tableHeaderColor, formatMoney } from '@/utils'
import { FormInstance, dayjs, ElTable } from 'element-plus'
import { IOrderAndItemDTO } from '@/types/views/business/orderitemcost'
import { TAdCustomer } from '@/types/views/customer'
import {
  getCustomerChargeBankDTOListApi,
  getOrderAndItemByCustomeridApi,
  getOrderApportiondetailPageListApi,
  saveOrderApportiondetailApi
} from '@/api/finance/advancecharge'
import { IDataCombo } from '@/types/common/DataCombo'
import { getEnumCombo } from '@/api/common/index'

/** 客户类型 */
const customerTypeCombo: IDataCombo[] = getEnumCombo(ECustomerType)
/** 状态下拉 */
const statusCombo: IDataCombo[] = getEnumCombo(EStatus)
/** 收款种类下拉 */
const sTypeCombo: IDataCombo[] = getEnumCombo(ESType)
const total = ref(0)
/** 表格高度 */
const tableheight = ref(0)
/** 预收款分配列表 */
const advancetotal = ref(0)
/** 每页选择下拉 */
const pageSizes = ref<number[]>([10, 20, 30, 40])

/** 当前人员银行流水分配查询条件 */
const queryChargeParams = reactive<TBankCustomerChargeQuery>({
  page: 1,
  pageSize: 20,
  startTime: '',
  endTime: '',
  customerid: '',
  invoice: '',
  queryKey: ''
})
/** 当前预收款分配列表 */
const advanceChargeList = ref<ICustomerChargeBankDTO[]>([])
/** 客户选择窗口显示状态 */
const dialogCustomerVisible = ref(false)
/** 详情窗口显示状态 */
const dialogDetailshow = ref(false)
/** 审核窗口显示状态 */
const dialogCheckshow = ref(false)
/** 核销窗口显示状态 */
const dialogChargeshow = ref(false)
/** 当前选择的金额分配对象 */
const curAdvanceCharge = ref<ICustomerChargeBankDTO>()
/** 退回原因 */
const backreason = ref('')
/** 当前审核状态 */
const curIstatus = ref(EStatus.已确认)
/** 订单详情列表 */
const orderItemList = ref<IOrderAndItemDTO[]>([])
/** 选中的订单行 */
const multipleSelection = ref<IOrderAndItemDTO[]>([])
/** 当前已分摊列表详情数组 */
const orderApportiondetailList = ref<IOrderApportiondetailDTO[]>([])
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

onMounted(() => {
  handleQuery()
  /**
   * 计算表格高度
   */
  tableheight.value = computTableHeight() - 50
})
/**
 * 选择客户信息后返回接收
 * @param val
 */
const selectCustomer = (val: TAdCustomer) => {
  queryChargeParams.customerid = val.id
  queryChargeParams.customername = val.sname
  dialogCustomerVisible.value = false
}
/**
 * 银行流水分配预开发票结果查询
 */
const handleQuery = () => {
  getCustomerChargeBankDTOListApi(queryChargeParams)
    .then((res: IAxios<ICustomerChargeBankDTO[]>) => {
      if (res.success) {
        advanceChargeList.value = res.obj
        advancetotal.value = res.total
      }
    })
    .finally(() => {})
}
/**
 * 显示详情
 * @param val
 */
const onDetail = (val: ICustomerChargeBankDTO) => {
  curAdvanceCharge.value = val
  getOrderApportiondetailPageListApi({
    page: 1,
    pageSize: 100,
    customerchargeid: val.id
  })
    .then((res: IAxios<IOrderApportiondetailDTO[]>) => {
      if (res.success) {
        orderApportiondetailList.value = res.obj
        dialogDetailshow.value = true
      }
    })
    .catch(() => {})
  dialogDetailshow.value = true
}
/**
 * 审核
 * @param row
 */
const onCheck = (row: ICustomerChargeBankDTO) => {
  curAdvanceCharge.value = row
  backreason.value = ''
  dialogCheckshow.value = true
}
/**
 * 退回：审核后仍能退回，必须填写退回原因
 * @param row
 */
const handleBack = (row: ICustomerChargeBankDTO) => {
  curAdvanceCharge.value = row
  curIstatus.value = EStatus.已退回
  backreason.value = ''
  dialogCheckshow.value = true
}
/**
 * 核销
 * @param row
 */
const onCharge = (row: ICustomerChargeBankDTO) => {
  curAdvanceCharge.value = row
  const data = {
    page: 1,
    pageSize: 100,
    customerid: row.customerid
  }
  getOrderAndItemByCustomeridApi(data).then((res: IAxios<IOrderAndItemDTO[]>) => {
    if (res.success) {
      orderItemList.value = res.obj!
      dialogChargeshow.value = true
    }
  })
}
/**
 * 保存审核意见
 */
const saveCheckForm = () => {
  if (curIstatus.value === EStatus.已退回 && backreason.value === '') {
    ElMessage.warning('请填写退回原因')
    return
  }
  const data = {
    id: curAdvanceCharge.value?.id!,
    iStatus: curIstatus.value,
    comments: backreason.value
  }
  // submitConfirmationApi(data).then((res: IAxios) => {
  //   if (res.success) {
  //     curAdvanceCharge.value = {}
  //     backreason.value = ''
  //     dialogCheckshow.value = false
  //     handleQuery()
  //   }
  // })
}
/**
 * 保存核销结果
 */
const saveChargeForm = () => {
  if (multipleSelection.value.length === 0) {
    ElMessage.warning('请选择需要核销的订单详情')
    return
  }
  if (!writeOffDate.value) {
    ElMessage.warning('请选择核销日期')
    return
  }
  let amount = 0
  const ids: string[] = []
  multipleSelection.value.forEach((item: IOrderAndItemDTO) => {
    amount += item.amountarrearage!
    ids.push(item.orderitemid!)
  })
  const namountbalance = curAdvanceCharge.value!.namountbalance!
  const message = `核销金额为${namountbalance}元,选择的订单总额为${amount}，是否确认核销？`
  ElMessageBox.confirm(message, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(() => {
      const data = {
        customerchargeId: curAdvanceCharge!.value?.id!,
        orderitemIds: ids.join(','),
        dateString: writeOffDate.value
      }
      saveOrderApportiondetailApi(data)
        .then((res: IAxios) => {
          if (res.success) {
            ElMessage.success('操作成功')
            dialogChargeshow.value = false
            curAdvanceCharge.value = {}
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
const handleSelectionChange = (val: IOrderDebtDTO[]) => {
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
    const data: TBankCustomerChargeQuery = {
      page: 1,
      pageSize: 100
    }
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
<style lang="scss" scoped>
.search_box .el-button,
.search_box .el-select,
.search_box .el-input {
  margin: 0 0px 5px 0 !important;
}
</style>
