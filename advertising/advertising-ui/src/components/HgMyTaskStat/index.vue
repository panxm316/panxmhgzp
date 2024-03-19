<!--
 * @Author: yanz
 * @Date: 2024-01-24 15:44:45
 * @LastEditors: suny suny@hgzp.com.cn
 * @LastEditTime: 2024-02-27 16:10:40
 * @Description: 个人中心 - 任务统计
 *
-->
<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex">
        <el-radio-group
          v-model="queryParams.sTaskCategory"
          style="margin-top: -5px; margin-right: 10px"
          @change="handleCategoryChange"
        >
          <el-radio-button label="0">年度</el-radio-button>
          <el-radio-button label="1">月度</el-radio-button>
        </el-radio-group>
        <span v-show="queryParams.sTaskCategory?.toString() === '0'">
          <el-date-picker v-model="queryParams.staskdate" type="year" placeholder="请选择年份" />
        </span>
        <span v-show="queryParams.sTaskCategory?.toString() === '1'">
          <el-date-picker v-model="queryParams.staskdate" type="month" placeholder="请选择月份" />
        </span>
        <HgZtreeSelect
          v-if="user?.blead || user?.adminflag !== 0"
          :scopeflag="EHgDeptZtreeUrl.DEPT_GETDEPTTREE"
          :selectids="queryParams.deptid ? [queryParams.deptid] : []"
          style="width: 120px"
          :filterable="true"
          clearable
          :treeparams="{ showInactiveDepts: false }"
          placeholder="请选择部门"
          @selectionztree="onSelectionDept"
        ></HgZtreeSelect>
        <HgZtreeSelect
          v-if="user?.blead || user?.adminflag !== 0"
          :scopeflag="EHgDeptZtreeUrl.EMP_GETDEPTEMPLOYPERSON"
          :selectids="queryParams.employid ? [queryParams.employid] : []"
          style="width: 120px"
          :filterable="true"
          clearable
          :treeparams="{ bIgnorePermissions: false }"
          placeholder="请选择业务员"
          @selectionztree="onSelectionEmp"
          @change="handleQuery"
        ></HgZtreeSelect>
        <el-checkbox
          v-model="queryParams.bdeptflag"
          :disabled="queryParams.employid !== ''"
          inline-prompt
          style="margin-top: 5px; margin-right: 10px"
          label="按人员"
          @change="handleQuery"
        />
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
      </div>
    </div>
    <div class="table_box">
      <el-table
        ref="dataTableRef"
        :data="resultList"
        highlight-current-row
        :height="tableheight"
        :header-cell-style="tableHeaderColor"
        :border="true"
      >
        <!-- 广告项目名称 -->
        <!-- String adprojectname -->
        <!-- <el-table-column
          label="广告项目名称"
          prop="adprojectname"
          width="120"
          show-overflow-tooltip
        /> -->

        <!-- 签订金额（20240227用户要求是签订金额） -->
        <!-- BigDecimal amountreceivable -->
        <el-table-column label="签订金额" prop="amountreceivable" :align="'right'" width="120">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.amountreceivable, '2') }}</span>
          </template>
        </el-table-column>

        <!-- 任务额度（万元） -->
        <!-- BigDecimal ntaskamount -->
        <el-table-column label="任务额度（万元）" prop="ntaskamount" :align="'right'" width="150">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.ntaskamount, '2') }}</span>
          </template>
        </el-table-column>
        <!-- 完成比例(业绩金额/任务额度) -->
        <!-- BigDecimal nfinishrate -->
        <el-table-column label="完成比例" prop="nfinishrate" :align="'right'" width="120">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.nfinishrate, '2') }}%</span>
          </template>
        </el-table-column>
        <!-- 任务日期 年：2023 月2023-01 -->
        <!-- String staskdate -->
        <el-table-column label="任务日期" prop="staskdate" width="160">
          <template #default="scope">
            <span v-if="scope.row.staskdate?.length === 4">{{ scope.row.staskdate }}年</span>
            <span v-if="scope.row.staskdate?.length === 7">{{ scope.row.staskdate }}月</span>
          </template>
        </el-table-column>
        <!-- 任务分类：年度、月度 -->
        <!-- String stasktype -->
        <el-table-column label="任务分类" prop="stasktype" width="120" show-overflow-tooltip>
          <template #default="scope">
            <span v-if="scope.row.stasktype === '0'">年度</span>
            <span v-if="scope.row.stasktype === '1'">月度</span>
          </template>
        </el-table-column>
        <!-- 人员分类：部门、人员 -->
        <!-- String spersonaltype -->
        <el-table-column label="人员分类" prop="spersonaltype" width="120" show-overflow-tooltip>
          <template #default="scope">
            <span v-if="scope.row.spersonaltype === '0'">部门</span>
            <span v-if="scope.row.spersonaltype === '1'">人员</span>
          </template>
        </el-table-column>
        <!-- 部门 -->
        <!-- String deptname -->
        <el-table-column label="部门" prop="deptname" min-width="120" show-overflow-tooltip />
        <!-- 人员 -->
        <!-- String employname -->
        <el-table-column label="人员" prop="employname" min-width="120" show-overflow-tooltip />

        <!-- <el-table-column
          label="操作"
          align="center"
          width="220"
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
              style="margin: 0 5px"
              @click="onDetail(scope.row)"
              >详情
            </el-button>
          </template>
        </el-table-column> -->
      </el-table>
      <!-- <el-pagination
        v-model:current-page="queryParams.page"
        class="pagination"
        style="margin-left: 10px; width: 100%"
        :page-sizes="pageSizes"
        :page-size="queryParams.pageSize"
        small
        background
        layout="total, sizes, prev, pager, next, jumper"
        :total="pageTotal"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      /> -->
    </div>
  </div>
</template>

<script setup lang="ts">
import { computTableHeight, formatMoney, tableHeaderColor } from '@/utils'
import { dayjs } from 'element-plus'
import { EHgDeptZtreeUrl, EPublishstatus } from '@/types/common/enumindex'
import { TDateType } from '@/types/components/hgdateindex'
import { TSelectZtree } from '@/types/common'
import { getMyTaskStatApi } from '@/api/personal/PersonalStat'
import { IAxios } from 'axios'
import { IDataCombo } from '@/types/common/DataCombo'
import { getEnumCombo } from '@/api/common'
import { IMyTaskDTO, ITaskReportQuery } from '@/types/views/business/tasks'
import useUserStore from '@/store/modules/user'

const global = getCurrentInstance()?.appContext.config.globalProperties
const user = useUserStore().user
/** 表格高度 */
const tableheight = ref(0)
/** 查询总页数 */
const pageTotal = ref(0)
/** 每页选择下拉 */
const pageSizes = ref<number[]>([15, 20, 30, 40])
/** 查询时间 */
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
/** 查询任务参数 */
const queryParams = reactive<ITaskReportQuery>({
  sTaskCategory: '0',
  bcurflag: '',
  staskdate: new Date().toISOString().slice(0, 4),
  deptid: '',
  employid: '',
  medidId: '',
  querykey: '',
  bdeptflag: false
} as ITaskReportQuery)
/**
 * 查询结果列表对象
 */
const resultList = ref<IMyTaskDTO[]>([])
/** 刊发状态列表 */
const publishStatusCombo: IDataCombo[] = getEnumCombo(EPublishstatus)
onMounted(() => {
  if (user?.blead || user?.adminflag !== 0) {
    queryParams.bcurflag = 'false'
  } else {
    queryParams.bcurflag = 'true'
  }
  handleQuery()
  nextTick(() => {
    /**
     * 计算表格高度 - 50为页面上方导航条Navbar的高度
     */
    tableheight.value = computTableHeight(false) - 50
  })
})
/**
 * @description: 页码总数变更
 * @param {*} val
 * @return {*}
 */
const handleSizeChange = (val: number) => {
  queryParams.pageSize = val
  handleQuery()
}
/**
 * @description: 页码变更
 * @param {*} val
 * @return {*}
 */
const handleCurrentChange = (val: number) => {
  queryParams.page = val
  handleQuery()
}

/**
 * @description: 任务分类变更
 * @param val
 */
const handleCategoryChange = () => {
  if (queryParams.sTaskCategory?.toString() === '0') {
    queryParams.staskdate = dayjs().format('YYYY')
  }
  if (queryParams.sTaskCategory?.toString() === '1') {
    queryParams.staskdate = dayjs().format('YYYY-MM')
  }
}

/**
 * 查询
 */
const handleQuery = () => {
  // 0：年度 1：月度
  if (queryParams.sTaskCategory?.toString() === '0') {
    queryParams.staskdate = dayjs(queryParams.staskdate).format('YYYY')
    queryParams.startTime = dayjs(queryParams.staskdate).startOf('year').format('YYYY-MM-DD')
    queryParams.endTime = dayjs(queryParams.staskdate).endOf('year').format('YYYY-MM-DD')
  }
  if (queryParams.sTaskCategory?.toString() === '1') {
    queryParams.staskdate = dayjs(queryParams.staskdate).format('YYYY-MM')
    queryParams.startTime = dayjs(queryParams.staskdate).startOf('month').format('YYYY-MM-DD')
    queryParams.endTime = dayjs(queryParams.staskdate).endOf('month').format('YYYY-MM-DD')
  }
  getMyTaskStatApi(queryParams)
    .then((res: IAxios) => {
      if (res.success) {
        resultList.value = res.obj
        // pageTotal.value = res.total
        pageTotal.value = 1
      } else {
        resultList.value = []
      }
    })
    .finally(() => {})
}
/**
 * 选择部门
 * @param val
 */
const onSelectionDept = (val: TSelectZtree[]) => {
  const deptid = val.map((item) => item.id).join(',')
  queryParams.deptid = deptid
  if (deptid !== '') {
    queryParams.employid = ''
  }
}
/**
 * 选择人员
 * @param val
 */
const onSelectionEmp = (val: TSelectZtree[]) => {
  const employid = val.map((item) => item.id).join(',')
  queryParams.employid = employid
  if (employid !== '') {
    queryParams.deptid = ''
    queryParams.bdeptflag = false
  }
}
</script>

<style scoped lang="scss"></style>
