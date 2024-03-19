<!--
 * @Author: suny
 * @Date: 2023-12-21 15:03:19
 * @LastEditTime: 2024-03-06 13:35:37
 * @LastEditors: wanghl
 * @Description: 发票核销实现
 1、查询银行流水分配表：（全部）确认/核销/退回
 查询只显示预开发票的分配情况，需要关联预开发票表进行过滤，挂开的情况归属到预收款进行核销
2、审核数据分配正确性：确认/退回（需要填写原因）
3、退回后修改分配表状态，同时记录原因
4、确认后进行核销：列出发票订单明细以供选择（必选，订单明细选择后依次进行自动核销，确认前需要提示分配金额和发票分配的金额是否一致）
5、已确认后：核销/退回
6、核销时：如果关联合同（订单），需要判断是否超过欠款额度，如果超过则需要退回业务员，重新进行分配

核销过程：参数（twcustomercharge表的id，选择的orderitems的ids）
1、orderitem根据时间正序进行分配，在twordercapportiondetail表中添加分配记录，同时更新orderitem表中对应的数据额度，统计每个item分配总额
2、如果所有item分配的总额低于twcustomercharge表当前数据分配的总额：将已用金额、剩余金额都回写到twcustomercharge表
3、如果item分配总额大于等于twcustomercharge表当前数据分配的总额：按照时间正序给item进行分配，分配结束后更新已用金额、剩余金额都回写到twcustomercharge表，同时将标志改为已核销

以下情况需要退回业务员重新分配
4、如果分配表指定了订单，则需要判断分配的剩余额度是否超过了订单总欠款余额
5、如果没有指定订单，需要判断分配的剩余额度是否超过了当前预开发票关联的所有订单总欠款金额

1、根据twcustomercharge表的id，找到对应的预开发票id和订单id（允许为空）
2、根据预开发票id和订单id，查询相关的所有订单详情列表
3、根据上述4、5，判断额度是否超标，如果超标，返回false，提示退回业务员重新进行分配
4、根据提交的orderitems的ids，查询orderitem的list，按照时间正序排序
5、便利orderitem的list表，根据欠款情况将分配额度更新到orderitem表，同时添加一条twordercapportiondetail记录，同时计算分配额度剩余情况
6、如果还有剩余则继续下一条记录，如果少于orderitem当前记录的欠款额度，将额度全部分配给当前item，更新收款和欠款，同时更新twcustomercharge表的分配和已用，同时状态改为已核销
7、如果orderitem全部处理完，仍有剩余，则将已用和剩余更新到twcustomercharge表，状态不变
-->
<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex">
        <HgDateIndex :initdate="timedata" @onresponse="ontimeCharge"></HgDateIndex>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
      </div>
    </div>
    <div class="table_box">
      <el-table
        ref="bankAccountChargeTableRef"
        :data="bankAccountChargeList"
        highlight-current-row
        :height="tableheight"
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
        <el-table-column
          label="操作"
          width="180"
          :align="'center'"
          class-name="small-padding fixed-width"
          fixed="right"
        >
          <template #default="scope">
            <el-button
              link
              title="详情"
              type="primary"
              size="small"
              icon="View"
              @click="onDetail(scope.row)"
              >详情</el-button
            >
            <el-button
              v-if="scope.row.istatus === EStatus.已提交"
              link
              type="primary"
              icon="Check"
              size="small"
              title="审核"
              @click="onCheck(scope.row)"
              >审核</el-button
            >
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
            <el-button
              v-if="scope.row.istatus === EStatus.已确认"
              link
              type="primary"
              icon="Back"
              size="small"
              title="退回"
              @click="handleBack(scope.row)"
              >退回</el-button
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
    <!-- 查看详情 -->
    <el-dialog
      v-model="dialogDetailshow"
      title="查看详情"
      width="1000"
      append-to-body
      destroy-on-close
      draggable
      :close-on-click-modal="false"
    >
      <HgPreInvoiceApplicationDetail
        :selfdetailid="curPreInvioceCharge!.preinvoiceapplicationid"
        style="padding: 20px"
      ></HgPreInvoiceApplicationDetail>
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
      <el-row style="padding: 0 20px">
        <el-col :span="24">
          <el-form label-width="100px" label-position="right">
            <el-form-item label="审核状态">
              <el-radio-group
                v-model="curIstatus"
                :disabled="curPreInvioceCharge!.istatus === EStatus.已确认"
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
            <div style="width: 100%; text-align: right">
              <el-button type="primary" icon="Check" @click="saveCheckForm">确认</el-button>
              <el-button icon="Close" @click="dialogCheckshow = false">取消</el-button>
            </div>
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
            <el-form-item label="发票金额">
              <el-input v-model="curPreInvioceCharge!.namountapply" disabled style="width: 400px" />
            </el-form-item>
            <el-form-item label="已收金额">
              <el-input
                v-model="curPreInvioceCharge!.namountreceived"
                disabled
                style="width: 400px"
              />
            </el-form-item>
            <el-form-item label="分配金额">
              <el-input
                v-model="curPreInvioceCharge!.namountreceived"
                disabled
                style="width: 400px"
              />
            </el-form-item>
            <el-form-item label="核销日期">
              <el-date-picker
                v-model="writeOffDate"
                type="date"
                placeholder="请选择核销日期"
                style="width: 400px"
                format="YYYY/MM/DD"
                value-format="YYYY-MM-DD"
              />
            </el-form-item>
            <el-form-item label="订单详情">
              <div style="width: 100%">
                <!-- <span>分配目标</span> -->
                <el-table
                  :data="preInvoiceOrderItemList"
                  style="width: 100%; max-height: 300px"
                  @selection-change="handleSelectionChange"
                >
                  <el-table-column type="selection" width="50" :align="'center'" />
                  <el-table-column label="合同号" prop="scontractnum" width="120" />
                  <el-table-column label="媒体" prop="medianame" width="100" />
                  <el-table-column label="广告编号" prop="iitemcode" width="100" />
                  <el-table-column
                    label="广告标题"
                    prop="sadtitle"
                    width="140"
                    show-overflow-tooltip
                  />
                  <el-table-column label="广告类型" prop="addisplayformname" width="100" />
                  <el-table-column label="签订金额" prop="amountreceivable" width="100" />
                  <el-table-column label="欠款金额" prop="amountarrearage" width="100" />
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
    </el-dialog>
    <!-- 根据合同号查看详情 -->
    <AdTworderdetails ref="viewImageRef"></AdTworderdetails>
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
import { reactive, ref } from 'vue'
import { computTableHeight, tableHeaderColor, formatMoney } from '@/utils'
import { FormInstance, dayjs, ElTable } from 'element-plus'
import {
  getInvoiceChargePageListApi,
  submitConfirmationApi,
  getPreInvoiceApplyDtoByIdApi
} from '@/api/finance/invoicecharge'
import { Tworderitem } from '@/types/views/ad/adtworder'
import { writeOffPreinvoiceapplicationApi } from '@/api/finance/invoicecharge'
import { IDataCombo } from '@/types/common/DataCombo'
import { getEnumCombo } from '@/api/common/index'
/** 状态下拉 */
const statusCombo: IDataCombo[] = getEnumCombo(EStatus)
const total = ref(0)
/** 表格高度 */
const tableheight = ref(0)
/** 当前人员银行流水分配列表 */
const bankAllocateAlltotal = ref(0)
/** 每页选择下拉 */
const pageSizes = ref<number[]>([10, 20, 30, 40])
/** 核销日期 */
const writeOffDate = ref(dayjs().format('YYYY-MM-DD'))

/** 当前人员银行流水分配查询条件 */
const queryChargeParams = reactive<TBankCustomerChargeQuery>({
  page: 1,
  pageSize: 20,
  startTime: '',
  endTime: ''
})
/** 当前人员银行流水分配列表 */
const bankAccountChargeList = ref<ICustomerChargeBankDTO[]>([])
/** 详情窗口显示状态 */
const dialogDetailshow = ref(false)
/** 审核窗口显示状态 */
const dialogCheckshow = ref(false)
/** 核销窗口显示状态 */
const dialogChargeshow = ref(false)
/** 当前选择的金额分配对象 */
const curPreInvioceCharge = ref<ICustomerChargeBankDTO>()
/** 退回原因 */
const backreason = ref('')
/** 当前审核状态 */
const curIstatus = ref(EStatus.已确认)
/** 预开发票对应的订单详情列表 */
const preInvoiceOrderItemList = ref<Tworderitem[]>([])
/** 选中的订单行 */
const multipleSelection = ref<Tworderitem[]>([])

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
 * 银行流水分配预开发票结果查询
 */
const handleQuery = () => {
  getInvoiceChargePageListApi(queryChargeParams)
    .then((res: IAxios<ICustomerChargeBankDTO[]>) => {
      if (res.success) {
        bankAccountChargeList.value = res.obj
        bankAllocateAlltotal.value = res.total
      }
    })
    .finally(() => {})
}
/**
 * 显示详情
 * @param val
 */
const onDetail = (val: ICustomerChargeBankDTO) => {
  curPreInvioceCharge.value = val
  dialogDetailshow.value = true
}
/**
 * 审核
 * @param row
 */
const onCheck = (row: ICustomerChargeBankDTO) => {
  curPreInvioceCharge.value = row
  backreason.value = ''
  dialogCheckshow.value = true
}
/**
 * 退回：审核后仍能退回，必须填写退回原因
 * @param row
 */
const handleBack = (row: ICustomerChargeBankDTO) => {
  curPreInvioceCharge.value = row
  curIstatus.value = EStatus.已退回
  backreason.value = ''
  dialogCheckshow.value = true
}
/**
 * 核销
 * @param row
 */
const onCharge = (row: ICustomerChargeBankDTO) => {
  curPreInvioceCharge.value = row
  const data = {
    preinvoiceapplicationId: row.preinvoiceapplicationid!,
    orderId: row.orderid!
  }
  getPreInvoiceApplyDtoByIdApi(data).then((res: IAxios<IPreInvoiceApplicationDTO>) => {
    if (res.success) {
      preInvoiceOrderItemList.value = res.obj!.orderItems!
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
    id: curPreInvioceCharge.value?.id!,
    iStatus: curIstatus.value,
    comments: backreason.value
  }
  submitConfirmationApi(data).then((res: IAxios) => {
    if (res.success) {
      curPreInvioceCharge.value = {}
      backreason.value = ''
      dialogCheckshow.value = false
      handleQuery()
    }
  })
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
  // 判断分配的剩余额度是否超过欠款额度
  let orderAmount = 0
  preInvoiceOrderItemList.value.forEach((item: Tworderitem) => {
    orderAmount += item.amountarrearage!
  })
  if (curPreInvioceCharge.value?.namountbalance! > orderAmount) {
    ElMessage.warning('分配额度高于订单欠款额度，需要退回业务员重新进行额度分配')
    return
  }
  let amount = 0
  const ids: string[] = []
  multipleSelection.value.forEach((item: Tworderitem) => {
    amount += item.amountarrearage!
    ids.push(item.id!)
  })
  const namountbalance = curPreInvioceCharge.value!.namountbalance!
  const message = `核销金额为${namountbalance}元,选择的订单总额为${amount}，是否确认核销？`
  ElMessageBox.confirm(message, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(() => {
      const data = {
        customerChargeId: curPreInvioceCharge!.value?.id!,
        orderitemIds: ids.join(','),
        dateString: writeOffDate.value
      }
      writeOffPreinvoiceapplicationApi(data)
        .then((res: IAxios) => {
          if (res.success) {
            ElMessage.success('操作成功')
            dialogChargeshow.value = false
            curPreInvioceCharge.value = {}
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
<style lang="scss" scoped></style>
