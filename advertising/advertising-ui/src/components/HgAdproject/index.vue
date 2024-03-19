<!--
 * @Author: yanz
 * @Date: 2024-03-11 11:01:12
 * @LastEditors: yanz
 * @LastEditTime: 2024-03-18 14:55:40
 * @Description: 选择广告项目
 *
-->
<template>
  <div>
    <el-input
      v-model="adprojectData.sname"
      placeholder="请选项目"
      style="width: 100%"
      :disabled="disabledshow"
      readonly
    >
      <template #append>
        <el-button
          icon="Search"
          circle
          title="选择客户"
          :disabled="disabledshow"
          @click="Customershow = true"
        />
      </template>
      <!-- <template #suffix>
        <el-button link icon="CircleClose" @click="closeadproject()"></el-button>
      </template> -->
    </el-input>
    <el-dialog
      v-model="Customershow"
      title="选择项目"
      width="1000"
      align-center
      :destroy-on-close="true"
      :draggable="fenbianlv() > 1440 ? true : false"
      @close="handleCancelfiles"
    >
      <div class="app-container">
        <div class="search_box">
          <div class="flex" style="width: 94%">
            <el-date-picker
              v-model="startTime"
              :editable="false"
              type="date"
              placeholder="选择日期"
              :clearable="false"
              style="width: 130px; margin-right: 5px"
              @change="ontimechange"
            >
            </el-date-picker>
            <span style="margin-right: 10px">至</span>
            <el-date-picker
              id="endTime"
              v-model="endTime"
              :editable="false"
              type="date"
              placeholder="选择日期"
              :clearable="false"
              style="width: 130px; margin-left: 5px"
              @change="ontimechange"
            >
            </el-date-picker>

            <el-input
              v-model="queryKey"
              placeholder="请输入关键字"
              clearable
              style="width: 160px"
              @clear="handleQuery"
            />
            <el-checkbox v-model="orderpublish" style="margin-top: 5px" @change="handleQuery"
              >仅已刊发</el-checkbox
            >
            <el-checkbox v-model="projectend" style="margin-top: 5px" @change="handleQuery"
              >包含已结项</el-checkbox
            >
            <span style="margin-left: 10px"></span>
            <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
          </div>
        </div>
        <div class="table_box">
          <el-table
            :data="adprojectList"
            highlight-current-row
            :height="tableheight"
            :header-cell-style="tableHeaderColor"
            border
          >
            <el-table-column
              type="index"
              label="序号"
              :index="table_index"
              width="60"
              align="center"
            >
            </el-table-column>
            <el-table-column label="项目名称" prop="sname" min-width="200" show-overflow-tooltip />
            <el-table-column label="预算" prop="nprojectbudget" min-width="160" align="right">
              <template #default="scope">
                <span>{{ formatMoney(scope.row.nprojectbudget, '2') }} </span>
              </template>
            </el-table-column>
            <el-table-column label="应收金额" prop="amountreceivable" min-width="160" align="right">
              <template #default="scope">
                <span>{{ formatMoney(scope.row.amountreceivable, '2') }} </span>
              </template>
            </el-table-column>
            <el-table-column label="已收金额" prop="amountreceived" min-width="160" align="right">
              <template #default="scope">
                <span>{{ formatMoney(scope.row.amountreceived, '2') }} </span>
              </template>
            </el-table-column>
            <el-table-column label="欠款金额" prop="amountarrearage" min-width="160" align="right">
              <template #default="scope">
                <span>{{ formatMoney(scope.row.amountarrearage, '2') }} </span>
              </template>
            </el-table-column>
            <el-table-column
              label="项目说明"
              prop="sremark"
              min-width="300"
              show-overflow-tooltip
            />

            <el-table-column label="开始日期" prop="dstartdate" min-width="120">
              <template #default="scope">
                <span>{{ formatDatesm(scope.row.dstartdate) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="结束日期" prop="denddate" min-width="120">
              <template #default="scope">
                <span>{{ formatDatesm(scope.row.denddate) }}</span>
              </template>
            </el-table-column>

            <el-table-column
              label="操作"
              align="center"
              width="140"
              class-name="small-padding fixed-width"
              fixed="right"
            >
              <template #default="adproject">
                <el-button
                  link
                  type="success"
                  icon="Edit"
                  size="small"
                  @click="handleSelect(adproject.row)"
                  >选择</el-button
                >
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
            v-model:current-page="queryInfo.pageNum"
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
          />
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import {
  computTableHeight,
  formatMoney,
  tableHeaderColor,
  fenbianlv,
  formatDatesm
} from '@/utils/index'
import useUserStore from '@/store/modules/user'
import { TAdProject } from '@/types/views/ad/adproject'
import { getAdProjectCountListApi } from '@/api/ad/adprojectcount'
import { dayjs } from 'element-plus'
import modal from '@/plugins/modal'
import { parseTime } from '@/utils/filters'

const userStore = useUserStore()
/** 导出返回数据 */
const emit = defineEmits<{
  selectiondata: [data: TAdProject]
}>()
/** 表格高度 */
const tableheight = ref(0)
/** 查询关键词 */
const queryKey = ref('')
/** 客户列表 */
const adprojectList = ref<TAdProject[]>([])
/** 广告项目id */
const adprojectid = ref('')
/** 刊发 */
const orderpublish = ref(false)
/** 结项 */
const projectend = ref(false)
/** 起始日期 */
const startTime = ref('')
/** 结束日期 */
const endTime = ref('')
/** 树高度 */
const treeheight = ref('')
/** 每页选择下拉 */
const pageSizes = ref<number[]>([10, 20, 30, 40])
/** 页数 */
const currentPage = ref<number>(1)
/** 每页条数 */
const pageSize = ref<number>(20)
/** 数据总条数 */
const pageTotal = ref<number>(0)
/** 查询对象 */
const queryInfo = ref({
  stratTime: '',
  endTime: '',
  adProjectId: '',
  pageNum: 0,
  pageSize: 20,
  publistStatus: '',
  queryKey: '',
  projectEnd: ''
})
/** 广告项目 */
const adprojectData = ref<TAdProject>({
  id: undefined,
  sname: '',
  projectcode: '',
  dstartdate: '',
  denddate: '',
  createempid: '',
  screatename: '',
  dcreatedate: '',
  sprojectcontent: '',
  nprojectbudget: 0,
  nprojectcost: 0,
  bprojectcomplete: 0,
  iapprovestatus: 0,
  sremark: '',
  isort: 0
})
/** 存储起始日期 */
const historystartTime = ref('')
/** 存储结束日期 */
const historyendTime = ref('')
/** 项目显示 */
const Customershow = ref(false)
const disabledshow = ref(false)
const view = (row: string, disabled?: boolean) => {
  adprojectData.sname = row
  disabledshow.value = disabled as boolean
}
defineExpose({ view })

/**
 * 登录用户id
 */
// const loginUserIdshow = ref(true)
// const loginUserId = ref(useUserStore().user?.userid.toString()!)
const adprojectSelection = ref<TAdProject[]>()
onMounted(() => {
  loaddata()
  handleQuery()
  /**
   * 计算表格高度
   */
  tableheight.value = computTableHeight() - 200
})
/**
 * 获取序号
 */
const table_index = (val: number) => {
  // eslint-disable-next-line eqeqeq
  const pagenum =
    queryInfo.value.pageSize! * (queryInfo.value.pageNum === 0 ? 0 : queryInfo.value.pageNum! - 1) +
    1
  return val + pagenum
}
/**
 * 时间初始化赋值
 */
const inittime = () => {
  const end = new Date()
  const start = new Date()
  start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
  startTime.value = dayjs(start).format('YYYY-MM-DD')
  endTime.value = dayjs(end).format('YYYY-MM-DD')
}
const ontimechange = () => {
  const curDate = new Date(dayjs(startTime.value).format('YYYY-MM-DD 00:00:00')).getTime()
  const curendTime = new Date(dayjs(endTime.value).format('YYYY-MM-DD 00:00:00')).getTime()
  if (curDate > curendTime) {
    modal.msgWarning('开始日期不能大于结束日期')
    return false
  }
  startTime.value = dayjs(startTime.value).format('YYYY-MM-DD')
  endTime.value = dayjs(endTime.value).format('YYYY-MM-DD')
  historystartTime.value = startTime.value
  historyendTime.value = endTime.value
  handleQuery()
}
/**
 * 选择
 * @param rows
 */
const handleSelectionChange = (rows: TAdProject[]) => {
  adprojectSelection.value = rows
}

/**
 * 获取项目列表
 */
const loaddata = () => {
  inittime()
}
/**
 * 改变页码总数时
 */
const handleSizeChange = (val: number) => {
  queryInfo.value.pageSize = val
  loaddata()
}
/**
 * 改变页数时
 * @param val
 */
const handleCurrentChange = (val: number) => {
  queryInfo.value.pageNum = val
  loaddata()
}

/**
 * 查询
 */
const handleQuery = () => {
  queryInfo.value.stratTime = startTime.value
  queryInfo.value.endTime = endTime.value
  queryInfo.value.adProjectId = adprojectid.value
  queryInfo.value.queryKey = queryKey.value
  if (orderpublish.value) queryInfo.value.publistStatus = '1'
  else {
    queryInfo.value.publistStatus = ''
  }
  if (projectend.value) queryInfo.value.projectEnd = '1'
  else {
    queryInfo.value.projectEnd = '0'
  }
  getAdProjectCountListApi(queryInfo.value).then((res) => {
    if (res.success) {
      pageTotal.value = res.total
      adprojectList.value = res.obj
      console.log(adprojectList.value)
    }
  })
}
/**
 * 选中
 * @param row
 */
const handleSelect = (row: TAdProject) => {
  adprojectData.value = { ...row }
  emit('selectiondata', row)
  Customershow.value = false
}
/**
 * 关闭附件弹窗
 */
const handleCancelfiles = () => {
  Customershow.value = false
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
