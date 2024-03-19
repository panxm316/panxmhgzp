<!--
 * @Author: wanghl
 * @Date: 2023-10-26 10:11:02
 * @LastEditTime: 2024-03-14 08:51:29
 * @LastEditors: wanghl
 * @Description:客户管理
-->
<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex" style="width: 94%">
        <el-select
          v-if="props.showbuse === true"
          v-model="queryInfo.buse"
          placeholder="客户类型"
          style="width: 160px"
          @change="loaddata"
        >
          <el-option label="有效" :value="true"></el-option>
          <el-option label="全部" value=""></el-option>
          <el-option label="无效" :value="false"></el-option>
        </el-select>
        <el-select
          v-model="queryInfo.itype"
          placeholder="客户类型"
          clearable
          style="width: 160px"
          @clear="loaddata"
          @change="loaddata"
        >
          <el-option label="直接客户" :value="0"></el-option>
          <el-option label="代理公司" :value="1"></el-option>
          <el-option label="内容生产方" :value="2"></el-option>
        </el-select>

        <el-input
          v-model="queryInfo.queryKey"
          placeholder="支持客户名称，简码，编码，地址检索"
          clearable
          style="width: 260px"
          @keyup.enter="loaddata"
          @clear="loaddata"
        />
        <el-button type="primary" icon="Search" @click="loaddata">搜索</el-button>
        <el-checkbox
          v-model="loginUserIdshow"
          style="margin-top: 5px"
          :disabled="isEmploy"
          @change="selectshowself"
          >只看自己</el-checkbox
        >
      </div>
    </div>
    <div class="table_box">
      <el-row>
        <el-table
          :data="customerList"
          row-key="id"
          :height="tableheight"
          :border="true"
          stripe
          :header-cell-style="tableHeaderColor"
          @selection-change="handleSelectionChange"
          @sort-change="handleSortChange"
        >
          <!-- <el-table-column type="selection" prop="ischeck" :width="50" align="center">
          </el-table-column> -->
          <el-table-column
            prop="sname"
            label="名称"
            sortable="custom"
            min-width="200"
            show-overflow-tooltip
          >
          </el-table-column>
          <el-table-column
            prop="icode"
            label="编码"
            sortable="custom"
            width="100"
            show-overflow-tooltip
          >
          </el-table-column>
          <el-table-column prop="itype" label="客户类型" min-width="100">
            <template #default="scope">
              <span v-if="scope.row.itype == '0'"> 直接客户 </span>
              <span v-if="scope.row.itype == '1'"> 代理公司 </span>
              <span v-if="scope.row.itype == '2'"> 内容生产方 </span>
            </template>
          </el-table-column>
          <el-table-column
            prop="employname"
            label="主业务员"
            sortable="custom"
            min-width="150"
            show-overflow-tooltip
          >
          </el-table-column>
          <el-table-column
            prop="parentName"
            label="父级名称"
            min-width="150"
            align="center"
            show-overflow-tooltip
          >
          </el-table-column>
          <!-- <el-table-column prop="iapprovestatus" label="审批状态" width="90" align="center">
            <template #default="scope">
              <span v-if="scope.row.iapprovestatus == 0">
                <el-tag>待审</el-tag>
              </span>
              <span v-if="scope.row.iapprovestatus == 1">
                <el-tag type="warning">在审</el-tag>
              </span>
              <span v-if="scope.row.iapprovestatus == 2">
                <el-tag type="success">通过</el-tag>
              </span>
              <span v-if="scope.row.iapprovestatus == 3">
                <el-tag type="danger">否决</el-tag>
              </span>
              <span v-if="scope.row.iapprovestatus == 4">
                <el-tag>撤销</el-tag>
              </span>
              <span v-if="scope.row.iapprovestatus == 5">
                <el-tag type="danger">无效</el-tag>
              </span>
            </template>
          </el-table-column> -->
          <el-table-column prop="bvip" label="是否大客户" width="100" align="center">
            <template #default="scope">
              <span v-if="scope.row.bvip == false">
                <el-tag>普通客户</el-tag>
              </span>
              <span v-if="scope.row.bvip == true">
                <el-tag type="success">大客户</el-tag>
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="buse" label="是否有效" width="90" align="center">
            <template #default="scope">
              <span v-if="scope.row.buse == false">
                <el-tag type="danger">无效</el-tag>
              </span>
              <span v-if="scope.row.buse == true">
                <el-tag type="success">有效</el-tag>
              </span>
            </template>
          </el-table-column>
          <el-table-column
            label="操作"
            align="center"
            width="140"
            class-name="small-padding fixed-width"
            fixed="right"
          >
            <template #default="customer">
              <el-button
                link
                type="success"
                icon="Edit"
                size="small"
                @click="handleUpdate(customer.row)"
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
import type { TAdCustomer, TQueryCustomer } from '@/types/views/customer'
import { getCustomerPageListApi } from '@/api/customer/index'
import { getCustomerPageListloginApi } from '@/api/customer/customerfiling'
import { required, computTableHeight, tableHeaderColor } from '@/utils/index'
import type { IAxios } from 'axios'
import useUserStore from '@/store/modules/user'
const userStore = useUserStore()
defineOptions({
  name: 'Customer'
})
type THSelectCustomer = {
  /**
   * 显示是否有效的：true为有效，false为无效，''为全部
   */
  buse?: boolean
  /**
   * 传入用于遍历的客户id
   */
  selfid?: string
  /**
   * 是否显示有效查询选择框true为显示，false为不显示，
   */
  showbuse?: boolean
  /**
   * 客户类型客户类型（直接客户、代理公司、内容生产方）
   */
  itype?: number | string
  /**
   * 显示是否通过的订单
   */
  iapprovestatus?: string
}
const props = withDefaults(defineProps<THSelectCustomer>(), {
  buse: true,
  selfid: '',
  showbuse: false,
  itype: '',
  iapprovestatus: ''
})
/**
 * 判断是否是业务员
 */
const isEmploy = ref(false)
/** 导出返回数据 */
const emit = defineEmits<{
  selectiondata: [data: TAdCustomer]
}>()
/** 表格高度 */
const tableheight = ref(0)

/** 客户列表 */
const customerList = ref<TAdCustomer[]>([])
/** 客户id */
const customerid = ref(props.selfid)
/** 树高度 */
const treeheight = ref('')
/** 客户归属业务员表 */
/** 每页选择下拉 */
const pageSizes = ref<number[]>([10, 20, 30, 40])
/** 页数 */
const currentPage = ref<number>(1)
/** 每页条数 */
const pageSize = ref<number>(20)
/** 数据总条数 */
const pageTotal = ref<number>(0)
/** 查询对象 */
const queryInfo = reactive<TQueryCustomer>({
  sort: 'isort',
  order: 'asc',
  icode: '', // 编号(自增列)
  startTime: '',
  endTime: '',
  pageSize: 20,
  page: 1,
  queryKey: '', // 查询关键字
  employid: '', // 主业务员id
  adindustryid: '', // 行业id
  buse: props.buse, // 是否启用
  bvip: '', // 是否大客户 0-否 1-是
  sstatus: '', // 客户状态（活跃，不活跃）。。待定
  iapprovestatus: '2', // 审批状态 0-待审 1-在审 2-通过 3-否决  4-撤销 5-无效
  bdelete: false, // 是否删除 0-否 1-是
  bindividual: '', // 是否是个人 0-否 1-是
  itype: '', // 客户类型客户类型（直接客户、代理公司、内容生产方）
  loginUserId: userStore.user?.userid.toString(), // 登录id
  // loginUserId: '', // 登录id
  cacheDTOKey: '', // 业务对象缓存key
  /**  * 仅我的客户  */
  bmycustomer: false
})
/**
 * 登录用户id
 */
const loginUserIdshow = ref(true)
// const loginUserId = ref(useUserStore().user?.userid.toString()!)
const customerSelection = ref<TAdCustomer[]>()
onMounted(() => {
  /**
   * 获取登录用户id
   */
  userStore.user?.blead === false ? (isEmploy.value = true) : (isEmploy.value = false)
  queryInfo.itype = props.itype!
  if (props.iapprovestatus) {
    queryInfo.iapprovestatus = props.iapprovestatus
  }
  loaddata()
  /**
   * 计算表格高度
   */
  tableheight.value = computTableHeight() - 200
})
/**
 * 监听传入id
 */
watch(
  () => props.selfid,
  (newValue, oldValue) => {
    nextTick(() => {
      customerid.value = newValue
      loaddata()
    })
  }
)
/**
 * 监听传入客户类型
 */
watch(
  () => props.itype,
  (newValue, oldValue) => {
    nextTick(() => {
      queryInfo.itype = props.itype!
      loaddata()
    })
  }
)
/**
 * 监听传入客户通过标志
 */
watch(
  () => props.iapprovestatus,
  (newValue, oldValue) => {
    nextTick(() => {
      if (props.iapprovestatus) {
        queryInfo.iapprovestatus = props.iapprovestatus
      }
      loaddata()
    })
  }
)
/**
 * 选择
 * @param rows
 */
const handleSelectionChange = (rows: TAdCustomer[]) => {
  customerSelection.value = rows
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
 * 获取客户列表iapprovestatus
 */
const loaddata = () => {
  if (loginUserIdshow.value === true) {
    loaddataloginUserId()
  } else {
    getCustomerPageListApi(queryInfo).then(({ obj, total }: IAxios<TAdCustomer[]>) => {
      pageTotal.value = total
      customerList.value = obj
      // customerList.value = obj.filter((item) => item.iapprovestatus === 2)
    })
  }
}
/**
 * 按登录名获取客户列表
 */

const loaddataloginUserId = () => {
  getCustomerPageListloginApi(queryInfo).then(({ obj, total }: IAxios<TAdCustomer[]>) => {
    pageTotal.value = total
    customerList.value = obj
    // customerList.value = obj.filter((item) => item.id !== customerid.value)
    // customerList.value = obj.filter((item) => item.iapprovestatus === 2)
  })
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
const handleUpdate = (row: TAdCustomer) => {
  emit('selectiondata', row)
}
/**
 * 只看自己
 */
const selectshowself = () => {
  if (loginUserIdshow.value === false) {
    queryInfo.loginUserId = ''
    queryInfo.bmycustomer = false
    loaddata()
  } else {
    queryInfo.loginUserId = userStore.user?.userid.toString()
    queryInfo.bmycustomer = true
    loaddataloginUserId()
  }
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
