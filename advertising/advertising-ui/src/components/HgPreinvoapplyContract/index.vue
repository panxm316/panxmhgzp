<!--
 * @Author: yanz
 * @Date: 2023-11-30 13:04:58
 * @LastEditors: yanz
 * @LastEditTime: 2024-02-21 12:56:13
 * @Description: 预开发票选择合同
-->
<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex" style="width: 94%">
        <el-input
          v-model="queryInfo.queryKey"
          placeholder="请输入查询的合同号"
          clearable
          style="width: 260px"
          @input="loaddata"
          @clear="loaddata"
        />
        <el-button type="primary" icon="Search" @click="loaddata">搜索</el-button>
        <el-button type="primary" icon="Plus" @click="handleSubmit">选择合同</el-button>
      </div>
    </div>
    <div class="table_box">
      <el-row>
        <el-table
          :data="contractVOList"
          row-key="id"
          :height="tableheight"
          :border="true"
          stripe
          :header-cell-style="tableHeaderColor"
          @selection-change="handleSelectionChange"
          @sort-change="handleSortChange"
        >
          <el-table-column type="selection" prop="ischeck" :width="50" align="center">
          </el-table-column>
          <el-table-column
            prop="customername"
            label="客户名称"
            sortable="custom"
            min-width="160"
            show-overflow-tooltip
          >
          </el-table-column>
          <el-table-column prop="scontractnum" label="合同编号" sortable="custom" min-width="120">
          </el-table-column>
          <el-table-column
            prop="sadtitle"
            label="广告标题"
            sortable="custom"
            min-width="120"
          ></el-table-column>
          <el-table-column prop="adtypename" label="广告分类" min-width="120">
            <template #default="scope">
              <!-- <span v-if="scope.row.adtypeid == '0'"> 直接客户 </span> -->
              <span> {{ scope.row.adtypename }} </span>
            </template>
          </el-table-column>
          <el-table-column
            prop="amountarrearage"
            label="欠款金额"
            sortable="custom"
            min-width="120"
            align="right"
          >
            <template #default="scope">
              <span>{{ formatMoney(scope.row.amountarrearage, '2') }}</span>
            </template>
          </el-table-column>
          <el-table-column
            prop="adindustryname"
            label="广告行业"
            sortable="custom"
            min-width="120"
          ></el-table-column>
          <el-table-column
            prop="businessentityname"
            label="经营主体"
            sortable="custom"
            min-width="120"
          ></el-table-column>
          <el-table-column
            label="操作"
            align="center"
            width="100"
            class-name="small-padding fixed-width"
            fixed="right"
          >
            <template #default="contractVO">
              <el-button
                link
                type="success"
                icon="Edit"
                size="small"
                @click="handleUpdate(contractVO.row)"
                >选择</el-button
              >
            </template>
          </el-table-column>
        </el-table>
      </el-row>
      <el-row>
        <el-pagination
          v-model:current-page="currentPage"
          class="pagination"
          style="margin-left: 10px; width: 100%"
          :page-sizes="pageSizes"
          :page-size="queryInfo.pageSize"
          small
          background
          layout="total, sizes, prev, pager, next, jumper"
          :total="pageTotal"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        >
        </el-pagination>
      </el-row>
    </div>
  </div>
</template>

<script setup lang="ts">
import { TContractforEmpCustomerQuery, TworderforPreinvoapplyVO } from '@/types/views/ad/adorder'
import { getContractNumByEmployIdAndCustomerIdApi } from '@/api/ad/adorder'
import { required, computTableHeight, tableHeaderColor, formatMoney } from '@/utils/index'
import type { IAxios } from 'axios'

defineOptions({
  name: 'Contract'
})
type THSelectContract = {
  /**
   * 显示是否有效的：true为有效，false为无效，''为全部
   */
  buse?: boolean
  /**
   * 传入当前客户id
   */
  customerid?: string
  /**
   * 传入当前业务员id
   */
  employeeid?: string
  /**
   * 是否显示有效查询选择框true为显示，false为不显示，
   */
  showbuse?: boolean
}
const props = withDefaults(defineProps<THSelectContract>(), {
  buse: true,
  employeeid: '',
  customerid: '',
  showbuse: false
})
/** 导出返回数据 */
const emit = defineEmits<{
  selectiondata: [data: TworderforPreinvoapplyVO[]]
}>()
/** 表格高度 */
const tableheight = ref(0)

/** 合同列表 */
const contractVOList = ref<TworderforPreinvoapplyVO[]>([])
/** 客户id */
const customerid = ref(props.customerid)
/** 业务员id */
const employeeid = ref(props.employeeid)
/** 树高度 */
const treeheight = ref('')
/** 合同表 */
/** 每页选择下拉 */
const pageSizes = ref<number[]>([10, 20, 30, 40])
/** 页数 */
const currentPage = ref<number>(1)
/** 每页条数 */
const pageSize = ref<number>(20)
/** 数据总条数 */
const pageTotal = ref<number>(0)
/** 查询对象 */
const queryInfo = reactive<TContractforEmpCustomerQuery>({
  page: 1,
  pageSize: 20,
  queryKey: '',
  sort: '',
  order: '',
  employeeid: '',
  customerid: ''
})
const contractSelection = ref<TworderforPreinvoapplyVO[]>([])
onMounted(() => {
  loaddata()
  /**
   * 计算表格高度
   */
  tableheight.value = computTableHeight() - 200
})
/**
 * 监听传入id
 */
function watchAndLoadData(propName: string, valueName: Ref<string>) {
  watch(
    () => props[propName as keyof typeof props],
    (newValue, oldValue) => {
      nextTick(() => {
        valueName.value = String(newValue)
        if (customerid.value && employeeid.value) {
          loaddata()
        }
      })
    }
  )
}

watchAndLoadData('customerid', customerid)
watchAndLoadData('employeeid', employeeid)
/**
 * 选择
 * @param rows
 */
const handleSelectionChange = (rows: TworderforPreinvoapplyVO[]) => {
  contractSelection.value = rows
}
/**
 * 排序
 * @param val
 */
const handleSortChange = (val: { prop: string; order: string }) => {
  queryInfo.sort = val.prop

  if (val.order === 'ascending') {
    queryInfo.order = 'asc'
  } else if (val.order === 'descending') {
    queryInfo.order = 'desc'
  } else {
    queryInfo.order = ''
  }
  loaddata()
}

/**
 * 获取合同列表
 */
const loaddata = () => {
  console.log('customerid && employeeid)', customerid, employeeid)
  queryInfo.customerid = customerid.value
  queryInfo.employeeid = employeeid.value
  console.log('queryInfo', queryInfo)
  getContractNumByEmployIdAndCustomerIdApi(queryInfo).then(
    ({ obj, total }: IAxios<TworderforPreinvoapplyVO[]>) => {
      pageTotal.value = total
      // contractList.value = obj.filter((item) => item.id !== contractid.value)
      contractVOList.value = obj
    }
  )
}
/**
 * 改变页码总数时
 */
const handleSizeChange = (val: number) => {
  queryInfo.pageSize = val
  loaddata()
}
/**
 * 改变页数时
 * @param val
 */
const handleCurrentChange = (val: number) => {
  queryInfo.page = val
  loaddata()
}

/**
 * 修改
 * @param row
 */
const handleUpdate = (row: TworderforPreinvoapplyVO) => {
  emit('selectiondata', [row])
}
/**
 * 选择合同（提交）
 */
const handleSubmit = () => {
  emit('selectiondata', contractSelection.value)
}
</script>

<style scoped>
ul {
  padding-left: 10px;
}
.left_box {
  height: v-bind(treeheight);
}
.showSearch span {
  color: #606266;
  font-size: 14px;
}
</style>
