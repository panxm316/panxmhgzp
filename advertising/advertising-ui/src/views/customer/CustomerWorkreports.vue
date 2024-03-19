<!--
 * @Author: wanghl
 * @Date: 2024-03-07 16:08:07
 * @LastEditTime: 2024-03-12 10:29:59
 * @LastEditors: wanghl
 * @Description: 客户工作报告
-->
<template>
  <div>
    <!-- 客户工作报告组件 -->
    <el-dialog
      v-model="dialogVisible"
      :title="'客户服务详情'"
      :width="fenbianlv() > 1440 ? '80%' : '94%'"
      align-center
      :destroy-on-close="true"
      @close="handleDetailCancel"
    >
      <el-tabs v-model="activeName" class="demo-tabs" style="margin-top: -30px">
        <el-tab-pane label="客户工作报告" name="MyAchievement">
          <div class="table_box">
            <HgDateIndex :initdate="timedata" @onresponse="ontime"></HgDateIndex>
            <el-select
              v-model="queryWorkReport.iworktype"
              placeholder="请选择类型"
              style="width: 140px; margin: 0 5px"
              clearable
              @change="getWorkReportList"
              @clear="getWorkReportList"
            >
              <!-- <el-option label="全部类型" value=""></el-option> -->
              <el-option
                v-for="t in workReportTypeList"
                :key="t.id"
                :label="t.name"
                :value="t.id"
              ></el-option>
            </el-select>
            <el-input
              v-model="queryWorkReport.queryKey"
              placeholder="请输入：工作内容，审批人，部门等关键字"
              style="width: 300px; margin-right: 5px"
              clearable
              @clear="getWorkReportList"
            ></el-input>
            <el-button type="primary" icon="Search" @click="getWorkReportList">搜索</el-button>
            <el-table
              ref="dataTableRef"
              :data="workReportList"
              highlight-current-row
              :height="tableheight"
              :header-cell-style="tableHeaderColor"
              border
              style="margin-top: 10px"
            >
              <el-table-column label="部门" prop="deptname" min-width="160" show-overflow-tooltip />
              <el-table-column
                label="人员名称"
                prop="employname"
                width="120"
                show-overflow-tooltip
              />
              <el-table-column label="开始日期" prop="dstartdate" width="100">
                <template #default="scope">
                  <span>{{ dayjs(scope.row.dstartdate).format('YYYY-MM-DD') }}</span>
                </template>
              </el-table-column>
              <el-table-column label="结束日期" prop="denddate" width="100">
                <template #default="scope">
                  <span>{{ dayjs(scope.row.denddate).format('YYYY-MM-DD') }}</span>
                </template>
              </el-table-column>
              <el-table-column label="工作方式" prop="sworkmode" width="100" align="center" />
              <el-table-column label="报告类型" prop="iworktype" width="100" align="center">
                <template #default="scope">
                  {{ workReportTypeList.find((t) => t.id === scope.row.iworktype)?.name }}
                </template>
              </el-table-column>
              <!-- <el-table-column
            label="客户名称"
            prop="customername"
            min-width="160"
            show-overflow-tooltip
          /> -->
              <el-table-column
                label="工作内容"
                prop="sworkcontent"
                width="300"
                show-overflow-tooltip
              />
              <el-table-column label="工作文件" min-width="360">
                <template #default="scope">
                  <el-link
                    v-for="file in scope.row.filelist"
                    :key="file.id"
                    :underline="false"
                    type="primary"
                    size="small"
                    style="font-size: 12px"
                    :icon="file.sfiletype === 'Photo' ? 'Picture' : 'Document'"
                    @click="previewFile(file)"
                    >{{ file.soriginalfile }}</el-link
                  >
                </template>
              </el-table-column>
              <el-table-column
                label="服务反馈"
                prop="sfeedback"
                width="120"
                show-overflow-tooltip
              />
              <el-table-column label="工作计划" prop="splan" width="160" show-overflow-tooltip />
              <el-table-column
                label="工作难点"
                prop="squestions"
                width="160"
                show-overflow-tooltip
              />
              <el-table-column label="创建时间" prop="dcreatedate" width="160" />
              <!-- <el-table-column label="审批状态" prop="iapprovestatus" width="100" align="center">
            <template #default="scope">
              {{ approveStatusList.find((t) => t.id === scope.row.iapprovestatus)?.name }}
            </template>
          </el-table-column> -->
              <el-table-column label="审批人名称" prop="scheckername" width="120" />
              <el-table-column label="审批日期" prop="dcheckdate" width="160" />
              <el-table-column
                label="审批意见"
                prop="scheckopinions"
                width="160"
                show-overflow-tooltip
              />
              <!-- <el-table-column
            label="操作"
            align="left"
            width="200"
            class-name="small-padding fixed-width"
            fixed="right"
          >
            <template #default="scope">
              <el-button
                v-if="user?.userid === scope.row.employid"
                link
                style="margin-left: 10px"
                type="success"
                icon="Edit"
                size="small"
                title="修改"
                @click="onEdit(scope.row)"
                >修改</el-button
              >
            </template>
          </el-table-column> -->
            </el-table>
            <el-pagination
              v-model:current-page="queryWorkReport.page"
              class="pagination"
              style="margin-left: 10px; width: 100%"
              :page-sizes="pageSizes"
              :page-size="queryWorkReport.pageSize"
              small
              background
              layout="total, sizes, prev, pager, next, jumper"
              :total="pageTotal"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
            />
          </div>
        </el-tab-pane>
        <el-tab-pane label="客户服务详情" name="MyTaskStat">
          <!-- <HgMyTaskStat v-if="activeName === 'MyTaskStat'"></HgMyTaskStat> -->
          <CustomerService v-if="activeName === 'MyTaskStat'" :customerid="customerid" />
          <!--  -->
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { computTableHeight, formatDate, formatMoney, tableHeaderColor, fenbianlv } from '@/utils'
import { dayjs } from 'element-plus'
import { TDateTimeType, TDateType } from '@/types/components/hgdateindex'
import { getCustomerWorkReportPageListApi } from '@/api/business/workreport'
import { IAxios } from 'axios'
import useUserStore from '@/store/modules/user'
import {
  IWorkReportFilesDTO,
  IWorkReportsDTO,
  TWorkReportQuery,
  TpICustomerWorkReportQuery
} from '@/types/views/business/workreports'
import { EWorkReportType } from '@/types/common/enumindex'
import { IDataCombo } from '@/types/common/DataCombo'
import { getEnumCombo } from '@/api/common/index'
const global = getCurrentInstance()?.appContext.config.globalProperties
/** 工作报告类型下拉列表 */
const workReportTypeList: IDataCombo[] = getEnumCombo(EWorkReportType)
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
const queryWorkReport = ref<TpICustomerWorkReportQuery>({
  sort: 'dcreatedate',
  order: 'desc',
  page: 1,
  pageSize: 20,
  startTime: '',
  endTime: '',
  cacheDTOKey: '', // 业务对象缓存key
  customerid: '', // 客户id
  iworktype: '', // 报告类型(0-日报、1-周报、2-月报、3-年报、4-客户服务）
  loginUserId: user.userid, // 登录人id
  queryKey: '' // 查询关键字
})
/** 列表对象 */
const workReportList = ref<IWorkReportsDTO[]>([])
/** 组件传值---------------------------------------- */
const dialogVisible = ref(false)
import { defineExpose } from 'vue'
/** 根据合同号获取订单详情 */
const view = (row: string) => {
  dialogVisible.value = true
  queryWorkReport.value.customerid = row
  customerid.value = row
  activeName.value = 'MyAchievement'
  getWorkReportList()
}
defineExpose({ view })
/** 组件传值---------------------------------------- */
/** 服务详情查询------------------------------------ */
const activeName = ref('MyAchievement')
import CustomerService from './CustomerService.vue'
/** 客户id */
const customerid = ref('')
/** 服务详情查询------------------------------------ */

/** 查询列表 */
const getWorkReportList = () => {
  getCustomerWorkReportPageListApi(queryWorkReport.value).then(
    ({ obj, success, msg, total }: IAxios) => {
      workReportList.value = obj
      pageTotal.value = total
    }
  )
}
onMounted(() => {
  nextTick(() => {
    /**
     * 计算表格高度 - 50为页面上方导航条Navbar的高度
     */
    tableheight.value = computTableHeight() - 200
    activeName.value = 'MyAchievement'
  })
})
/**
 * 改变页码总数时
 */
const handleSizeChange = (val: number) => {
  queryWorkReport.value.pageSize = val
  getWorkReportList()
}
/**
 * 改变页数时
 * @param val
 */
const handleCurrentChange = (val: number) => {
  queryWorkReport.value.page = val
  getWorkReportList()
}
const dialogEditVisible = ref(false)
const bcheckflag = ref(false)
/**
 * 编辑按钮事件
 * @param row
 */
// const onEdit = (row: IWorkReportsDTO) => {
//   dialogEditVisible.value = true
//   workReportForm.value = { ...row }
// }
/**
 * 时间选择
 * @param val
 */
const ontime = (val: TDateTimeType) => {
  let timev = false
  if (queryWorkReport.value.startTime) {
    timev = true
  }
  queryWorkReport.value.startTime = val.startTime
  queryWorkReport.value.endTime = val.endTime
  if (timev) {
    getWorkReportList()
  }
}
/**
 * 关闭弹窗
 */
const handleDetailCancel = () => {
  dialogVisible.value = false
  queryWorkReport.value = {
    sort: 'dcreatedate',
    order: 'desc',
    page: 1,
    pageSize: 20,
    startTime: '',
    endTime: '',
    cacheDTOKey: '', // 业务对象缓存key
    customerid: '', // 客户id
    iworktype: '', // 报告类型(0-日报、1-周报、2-月报、3-年报、4-客户服务）
    loginUserId: user.userid, // 登录人id
    queryKey: '' // 查询关键字
  }
}
/**
 * 预览上传的文件
 * @param file
 */
const previewFile = (file: IWorkReportFilesDTO) => {
  console.log(file)
  if (file.sfiletype === 'Photo') {
    window.open(file.durl)
  } else if (file.sfiletype === 'Office') {
    const url = `${global?.officePreviewUrl}?src=${file.durl}`
    window.open(url)
  }
}
</script>

<style scoped lang="scss">
.el-tabs__header {
  border-bottom: 5px solid #f8f8f9 !important;
}
</style>
