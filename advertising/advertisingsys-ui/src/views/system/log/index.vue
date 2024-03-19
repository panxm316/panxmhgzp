<!--
 * @Author: caogd
 * @Date: 2023-09-06 13:46:36
 * @LastEditTime: 2023-10-23 15:40:30
 * @LastEditors: wanghl
 * @Description: 操作日志展示
-->

<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex">
        <HgDateIndex @onresponse="handleTimeChange"></HgDateIndex>
        <el-select
          v-model="queryInfo.slogtype"
          placeholder="日志类型"
          clearable
          style="width: 160px"
          @change="loaddata"
        >
          <el-option v-for="item in slogTypeCombo" :key="item" :value="item" :label="item" />
        </el-select>
        <el-input
          v-model="queryInfo.queryKey"
          placeholder="请输关键字或操作员"
          clearable
          style="width: 200px"
          @clear="loaddata"
          @keyup.enter="loaddata"
        />
        <el-button type="primary" icon="Search" @click="loaddata">搜索</el-button>
        <el-tooltip class="item" effect="dark" content="帮助" placement="top">
          <el-button class="help" type="text" @click="showhelp()">?</el-button>
        </el-tooltip>
      </div>
    </div>
    <div class="table_box">
      <el-table
        :data="logList"
        row-key="id"
        :height="tableheight"
        :border="true"
        stripe
        :header-cell-style="tableHeaderColor"
        @sort-change="handleSortChange"
      >
        <el-table-column
          prop="employname"
          label="操作员"
          sortable="custom"
          :min-width="120"
          align="center"
        >
        </el-table-column>
        <el-table-column
          prop="slogtype"
          label="操作类型"
          sortable="custom"
          align="center"
          :min-width="110"
        >
          <template #default="scope">
            <el-tag v-if="scope.row.slogtype === '用户登录'" type="success">登录</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="sremark" label="操作内容" :width="800">
          <template #default="scope">
            <el-tooltip
              v-if="toBreak(scope.row.sremark) > 100"
              class="item"
              effect="dark"
              placement="top"
            >
              <template #content>
                <div style="max-width: 700px">
                  {{ scope.row.sremark }}
                </div>
              </template>
              <div class="oneLine">{{ scope.row.sremark }}</div>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column prop="doperatordate" label="日期" sortable="custom" :min-width="180">
        </el-table-column>
        <el-table-column prop="soperatorip" label="IP地址" sortable="custom" :min-width="180">
        </el-table-column>
      </el-table>
      <el-pagination
        v-model:current-page="queryInfo.page"
        class="pagination"
        :page-sizes="pageSizes"
        :page-size="queryInfo.pageSize"
        small
        layout="total, sizes, prev, pager, next, jumper"
        :total="pageTotal"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { computTableHeight, tableHeaderColor, toBreak } from '@/utils/index'
import type { IAxios } from 'axios'
import { TDateTimeType } from '@/types/components/hgdateindex'
import { TLog, TLogQueryInfo } from '@/types/views/system/log'
import { getLogPageListApi } from '@/api/system/log'
import { useRouter } from 'vue-router'
const router = useRouter()
defineOptions({
  name: 'Log'
})
/** 表格高度 */
const tableheight = ref(0)
/** 每页选择下拉 */
const pageSizes = ref<number[]>([10, 20, 30, 40])
/** 数据总条数 */
const pageTotal = ref<number>(0)
/** 日志列表 */
const logList = ref<TLog[]>([])
/** 查询对象 */
const queryInfo = ref<TLogQueryInfo>({
  sort: 'doperatordate',
  order: 'desc',
  queryKey: '',
  startTime: '',
  endTime: '',
  slogtype: '',
  page: 1,
  pageSize: 20
})
/** 日志类型下拉 */
const slogTypeCombo = ['新增', '修改', '删除', '用户登录', '异常']
onMounted(() => {
  // loaddata()
  /**
   * 计算表格高度
   */
  tableheight.value = computTableHeight()
})
/**
 * 获取日志列表
 */
const loaddata = () => {
  const data: TLogQueryInfo = queryInfo.value
  getLogPageListApi(data).then(({ obj, total }: IAxios<TLog[]>) => {
    logList.value = obj
    pageTotal.value = total
  })
}
/**
 * 排序
 * @param val
 */
const handleSortChange = (val: { prop: string; order: string }) => {
  queryInfo.value.sort = val.prop
  if (val.order === 'ascending') {
    queryInfo.value.order = 'asc'
  } else if (val.order === 'descending') {
    queryInfo.value.order = 'desc'
  } else {
    queryInfo.value.order = 'datdoperatordatee'
  }
  loaddata()
}

/**
 * @description: 日期组件返回
 * @param {*} val
 * @return {*}
 */
const handleTimeChange = (val: TDateTimeType) => {
  queryInfo.value.startTime = val.startTime
  queryInfo.value.endTime = val.endTime
  loaddata()
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
  queryInfo.value.page = val
  loaddata()
}
/**
 * 页面跳转
 */
const showhelp = () => {
  const to = '/template/Help?name=' + 'log'
  const routeData = router.resolve(to)
  window.open(routeData.href, '_blank')
}
</script>

<style scoped></style>
