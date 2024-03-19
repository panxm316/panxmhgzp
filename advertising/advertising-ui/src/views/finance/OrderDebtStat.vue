<!--
 * @Author: suny
 * @Date: 2023-11-22 13:39:44
 * @LastEditTime: 2024-03-07 20:02:00
 * @LastEditors: suny suny@hgzp.com.cn
 * @Description: 欠款统计查询
-->
<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex">
        <HgDateIndex :initdate="timedata" @onresponse="ontime"></HgDateIndex>
        <HgZtreeSelect
          :scopeflag="EHgDeptZtreeUrl.DEPT_GETDEPTTREE"
          :selectids="queryParams.deptid ? [queryParams.deptid] : []"
          style="width: 100px"
          :filterable="true"
          :treeparams="{ bIgnorePermissions: true }"
          @selectionztree="onSelectionDept"
        ></HgZtreeSelect>
        <el-input
          v-model="queryParams.queryKey"
          clearable
          placeholder="请输入客户关键字"
          class="input-with-select"
          style="width: 200px"
          @keyup.enter="handleQuery"
          @clear="handleQuery"
        >
        </el-input>
        <el-checkbox
          v-model="queryParams.breportreason"
          inline-prompt
          style="margin-top: 5px; margin-right: 10px"
          label="已填报欠款原因"
          @change="handleQuery"
        />
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
      </div>
    </div>
    <div class="table_box">
      <el-table
        ref="dataTableRef"
        :data="orderDebtStatList"
        highlight-current-row
        :height="tableheight"
        :header-cell-style="tableHeaderColor"
        border
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column label="客户" prop="customername" min-width="160" show-overflow-tooltip />
        <el-table-column label="代理公司" prop="agencyname" width="120" show-overflow-tooltip />
        <el-table-column
          label="内容生产方"
          prop="agentemployname"
          width="100"
          show-overflow-tooltip
        />
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
        <el-table-column label="主业务员" prop="employname" width="100" show-overflow-tooltip />
        <el-table-column
          label="代理公司主业务员"
          prop="agencyemployname"
          width="160"
          show-overflow-tooltip
        />
        <el-table-column
          label="内容生产方业务员"
          prop="agentemployname"
          min-width="160"
          show-overflow-tooltip
        />
        <el-table-column label="广告标题" prop="sadtitle" width="160" show-overflow-tooltip />
        <el-table-column label="开始日期" prop="prestartdate" width="100">
          <template #default="scope">
            <span>{{ dayjs(scope.row.prestartdate).format('YYYY-MM-DD') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="媒体" prop="medianame" width="160" show-overflow-tooltip />
        <el-table-column label="规格" prop="adspecname" width="100" show-overflow-tooltip />
        <el-table-column
          label="应收"
          prop="amountreceivable"
          width="100"
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
          width="100"
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
          width="100"
          show-overflow-tooltip
          align="right"
        >
          <template #default="scope">
            <span>{{ formatMoney(scope.row.amountarrearage, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="开票金额"
          prop="napplyamount"
          width="100"
          show-overflow-tooltip
          align="right"
        >
          <template #default="scope">
            <span>{{ formatMoney(scope.row.napplyamount, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="关联发票号" prop="invoice" width="160" show-overflow-tooltip />
        <el-table-column label="催告情况" prop="snoticecontent" width="160" show-overflow-tooltip />
        <el-table-column label="欠款原因" prop="sdebtreason" width="160" show-overflow-tooltip />
        <el-table-column label="归类原因" prop="scategory" width="160" show-overflow-tooltip />
        <el-table-column
          label="计划回款时间"
          prop="srepaymentdate"
          width="120"
          show-overflow-tooltip
        />
        <el-table-column label="风险等级" prop="srisklevel" width="160" show-overflow-tooltip />
        <el-table-column label="法务介入" prop="blegal" width="160" align="center">
          <template #default="scope">
            <el-checkbox v-model="scope.row.blegal" disabled></el-checkbox>
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
    <!-- 根据合同号查看详情 -->
    <AdTworderdetails ref="viewImageRef"></AdTworderdetails>
  </div>
</template>

<script lang="ts" setup>
import { getOrderDebtPageListApi, pushOrderDebtApi } from '@/api/finance/orderdebtstat'
import useUserStore from '@/store/modules/user'
import { TDateTimeType, TDateType } from '@/types/components/hgdateindex'
import { IOrderDebtDTO, TOrderDebtStatQuery } from '@/types/views/finance/orderdebtstat'
import { IAxios } from 'axios'
import { EHgDeptZtreeUrl } from '@/types/common/enumindex'
import { TSelectZtree } from '@/types/common'
import { computTableHeight, tableHeaderColor, formatMoney } from '@/utils'
import { dayjs } from 'element-plus'

const user = useUserStore().user
const total = ref(0)
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
const queryParams = reactive<TDebtReasonVO>({
  sort: 'createtime',
  order: 'desc',
  page: 1,
  pageSize: 20,
  startTime: '',
  endTime: '',
  deptid: '',
  breportreason: false,
  bfinance: true
})
/** 列表对象 */
const orderDebtStatList = ref<IOrderDebtDTO[]>([])
/** Form选中的行 */
const multipleSelection = ref<IOrderDebtDTO[]>([])
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
  getOrderDebtPageListApi(queryParams)
    .then((res: IAxios<IOrderDebtDTO[]>) => {
      if (res.success) {
        orderDebtStatList.value = res.obj
        total.value = res.total
      }
    })
    .finally(() => {})
}
/**
 * 推送欠款数据到欠款原因表
 * (此方法已不用，用户要求不再主动推送，改为业务员主动直接查询，填写原因后推送)
 * @param row
 */
const handlePush = (row?: IOrderDebtDTO) => {
  const ids: string[] = []
  if (row === undefined) {
    multipleSelection.value.every((item) => {
      if (item.breportreason) {
        ElMessage.warning('已推送的数据不允许再次推送')
        return false
      }
      ids.push(item.id?.toString() as string)
    })
  } else {
    if (row.breportreason) {
      ElMessage.warning('已推送的数据不允许再次推送')
      return
    }
    ids.push(row.id?.toString() as string)
  }

  if (ids.length === 0) {
    ElMessage.warning('请选择要推送的数据')
    return
  }
  const data = {
    ids: ids.join(',')
  }
  ElMessageBox.confirm('是否推送选择的数据？', '提示', {
    type: 'warning'
  })
    .then(() => {
      pushOrderDebtApi(data)
        .then((res: IAxios) => {
          if (res.success) {
            ElMessage.success('推送成功!')
            handleQuery()
          }
        })
        .catch(() => {})
    })
    .catch(() => {})
}

/**
 * 选择人员
 * @param val
 */
const onSelectionDept = (val: TSelectZtree[]) => {
  const deptid = val.map((item) => item.id).join(',')
  queryParams.deptid = deptid
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
const handleSelectionChange = (val: IOrderDebtDTO[]) => {
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
import { TDebtReasonVO } from '@/types/views/business/debtreason'
const viewImageRef = ref()
const handleSee = (row?: any, number?: number) => {
  viewImageRef.value.view(row.scontractnum)
}
// ---------------------------------------------------------------------
</script>

<style lang="scss" scoped></style>
