<!--
 * @Author: caogd
 * @Date: 2023-09-12 15:50:19
 * @LastEditTime: 2023-10-23 15:39:53
 * @LastEditors: wanghl
 * @Description: 系统管理操作日志
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
        :data="sysoperatelogList"
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
          :min-width="150"
          sortable="custom"
          align="center"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          prop="slogname"
          label="日志操作名称"
          :width="130"
          sortable="custom"
          align="center"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          prop="slogtype"
          label="操作类型"
          :min-width="110"
          sortable="custom"
          align="center"
        >
          <template #default="scope">
            <el-tag v-if="scope.row.slogtype === '修改'" type="success">{{
              scope.row.slogtype
            }}</el-tag>
            <el-tag v-if="scope.row.slogtype === '删除'" type="danger">{{
              scope.row.slogtype
            }}</el-tag>
            <el-tag v-if="scope.row.slogtype === '新增'">{{ scope.row.slogtype }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="snewvalue" label="变化值" :width="800">
          <template #default="scope">
            <el-tooltip
              v-if="toBreak(scope.row.snewvalue) > 110"
              class="item"
              effect="dark"
              placement="top"
            >
              <template #content>
                <div style="max-width: 700px" class="pre_box_1">
                  <!-- {{ JSON.parse(scope.row.snewvalue) }} -->
                  <pre v-html="syntaxHighlight(JSON.parse(scope.row.snewvalue))"></pre>
                </div>
              </template>
              <div class="oneLine">{{ JSON.parse(scope.row.snewvalue) }}</div>
            </el-tooltip>
          </template>
        </el-table-column>
        <!-- <el-table-column prop="soldvalue" label="旧值" :width="500">
          <template #default="scope">
            <el-tooltip
              v-if="toBreak(scope.row.soldvalue) > 60"
              class="item"
              effect="dark"
              placement="top"
            >
              <template #content>
                <div style="max-width: 700px">
                  {{ scope.row.soldvalue }}
                </div>
              </template>
              <div class="oneLine">{{ scope.row.soldvalue }}</div>
            </el-tooltip>
          </template>
        </el-table-column> -->
        <el-table-column prop="soperatetime" label="日期" :width="200" sortable="custom">
        </el-table-column>
        <el-table-column prop="soperateip" label="IP地址" :width="150" sortable="custom">
        </el-table-column>
        <el-table-column label="操作" align="center" :width="120" fixed="right">
          <template #default="scope">
            <el-button link type="primary" icon="View" @click="handleView(scope.row, scope.index)"
              >详细</el-button
            >
          </template>
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
    <!-- 操作日志详细 -->
    <el-dialog
      v-model="dialogVisible"
      title="操作日志详细"
      width="800px"
      append-to-body
      :draggable="fenbianlv() > 1440 ? true : false"
    >
      <div style="width: 100%; text-align: right">
        <el-button type="primary" icon="back" @click="upstoryinfo">上一篇</el-button>
        <el-button type="warning" @click="nextstoryinfo"
          >下一篇<el-icon class="el-icon--right"><Right /></el-icon
        ></el-button>
      </div>

      <el-form :model="logForm" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="操作类型："
              ><el-tag v-if="logForm.slogtype === '修改'" type="success">{{
                logForm.slogtype
              }}</el-tag>
              <el-tag v-if="logForm.slogtype === '删除'" type="danger">{{
                logForm.slogtype
              }}</el-tag>
              <el-tag v-if="logForm.slogtype === '新增'">{{
                logForm.slogtype
              }}</el-tag></el-form-item
            >
            <el-form-item label="操作名称：">{{ logForm.slogname }} </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="操作员：">{{ logForm.employname }}</el-form-item>
            <el-form-item label="日期：">{{ logForm.soperatetime }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <!-- <el-form-item label="新值："> {{ logForm_new }} </el-form-item> -->
            <el-form-item label="变化值：">
              <div class="pre_box">
                <pre v-html="logForm_new"></pre>
              </div>
            </el-form-item>
          </el-col>
          <!-- <el-col :span="24">
            <el-form-item label="旧值：">{{ logForm.soldvalue }}</el-form-item>
          </el-col> -->
          <el-col :span="24">
            <el-form-item label="操作表名：">{{ logForm.stablename }}</el-form-item>
          </el-col>
          <!-- <el-col :span="24">
            <el-form-item label="操作sql：">{{ logForm.soperatesql }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="sql参数：">{{ logForm.soperatesqlparam }}</el-form-item>
          </el-col> -->
          <el-col :span="24">
            <el-form-item label="操作ip：">{{ logForm.soperateip }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注：">
              {{ logForm.sremark }}
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button icon="close" @click="dialogVisible = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import {
  computTableHeight,
  tableHeaderColor,
  toBreak,
  syntaxHighlight,
  fenbianlv
} from '@/utils/index'
import type { IAxios } from 'axios'
import { TDateTimeType } from '@/types/components/hgdateindex'
import { TSysoperatelog, TSysoperatelogQueryInfo } from '@/types/views/system/sysoperatelog'
import { getSysoperatelogPageListApi } from '@/api/system/sysoperatelog'
import { useRouter } from 'vue-router'
const router = useRouter()
defineOptions({
  name: 'SysOperateLog'
})
/** 表格高度 */
const tableheight = ref(0)
/** 每页选择下拉 */
const pageSizes = ref<number[]>([10, 20, 30, 40])
/** 数据总条数 */
const pageTotal = ref<number>(0)
/** 日志列表 */
const sysoperatelogList = ref<TSysoperatelog[]>([])
/** 查询对象 */
const queryInfo = ref<TSysoperatelogQueryInfo>({
  sort: 'soperatetime',
  order: 'desc',
  queryKey: '',
  startTime: '',
  endTime: '',
  slogtype: '',
  page: 1,
  pageSize: 20
})
/** 日志类型下拉 */
const slogTypeCombo = ['新增', '修改', '删除']

const dialogVisible = ref(false)

const logForm = ref<TSysoperatelog>({})
const logForm_new = ref([])
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
  const data: TSysoperatelogQueryInfo = queryInfo.value
  getSysoperatelogPageListApi(data).then(({ obj, total }: IAxios<TSysoperatelog[]>) => {
    sysoperatelogList.value = obj
    pageTotal.value = total
  })
}
const handleView = (row: any, index: number) => {
  dialogVisible.value = true
  logForm.value = { ...row }
  if (typeof logForm.value?.snewvalue === 'string') {
    logForm_new.value = JSON.parse(logForm.value.snewvalue)
    logForm_new.value = syntaxHighlight(logForm_new.value)
  }
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
    queryInfo.value.order = 'soperatetime'
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
 * 上一篇
 */
const upstoryinfo = () => {
  let row
  for (let i = 0; i < sysoperatelogList.value.length; i++) {
    if (sysoperatelogList.value[i].id === logForm.value.id) {
      if (i === 0) {
        ElMessage({
          showClose: true,
          message: '已经是本页第一篇！',
          type: 'warning'
        })
        return
      } else {
        row = sysoperatelogList.value[i - 1]
      }
    }
  }
  handleView(row, 0)
}
/**
 * 下一篇
 */
const nextstoryinfo = () => {
  let row
  for (let i = 0; i < sysoperatelogList.value.length; i++) {
    if (sysoperatelogList.value[i].id === logForm.value.id) {
      if (i === sysoperatelogList.value.length - 1) {
        ElMessage({
          showClose: true,
          message: '已经是本页最后一篇！',
          type: 'warning'
        })
        return
      } else {
        row = sysoperatelogList.value[i + 1]
      }
    }
  }
  handleView(row, 0)
}
/**
 * 页面跳转
 */
const showhelp = () => {
  const to = '/template/Help?name=' + 'sysoperatelog'
  const routeData = router.resolve(to)
  window.open(routeData.href, '_blank')
}
</script>

<style scoped>
.pre_box {
  max-height: 300px;
  background-color: #f2f6fc;
  overflow-y: auto;
  width: 96%;
  color: black;
}
.pre_box_1 {
  max-height: 300px;
  background-color: #409eff;
  overflow-y: auto;
  width: 100%;
  color: #fff;
  font-size: 14px;
}
</style>
