<!--
 * @Author: lhl
 * @Date: 2023-12-25
 * @Description: 任务进度汇总 前端视图页面
-->
<template>
  <div class="app-container">
    <div class="search_box">
      <el-select
        v-model="deporemploy"
        style="width: 100px; margin-right: 5px"
        @change="handleDepORemploy($event)"
      >
        <el-option
          v-for="item in baseTask.TaskType"
          :key="item.id"
          :label="item.value"
          :value="item.id"
        ></el-option>
      </el-select>
      <el-select
        v-if="showdept === 0"
        v-model="deplevel"
        style="width: 100px; margin-right: 5px"
        clearable
        @change="handleQuery"
      >
        <el-option
          v-for="item in baseTask.DepLevel"
          :key="item.id"
          :label="item.value"
          :value="item.id"
        ></el-option>
      </el-select>

      <el-select
        v-model="datetype"
        style="width: 100px; margin-right: 5px"
        @change="handleDateType($event)"
      >
        <el-option
          v-for="item in baseTask.DateType"
          :key="item.id"
          :label="item.value"
          :value="item.id"
        ></el-option>
      </el-select>
      <el-date-picker
        v-if="showyear === 0"
        v-model="dateYear"
        :editable="false"
        type="year"
        placeholder="选择年度"
        :clearable="false"
        style="width: 130px; margin-right: 5px"
        @change="changeyear"
      >
      </el-date-picker>
      <el-date-picker
        v-if="showyear === 1"
        v-model="dateMonth"
        :editable="false"
        type="month"
        placeholder="选择月度"
        :clearable="false"
        style="width: 130px; margin-right: 5px"
        @change="changemonth"
      >
      </el-date-picker>
      <el-select v-model="mediaId" placeholder="请选择媒体" clearable @change="handleMedia($event)">
        <el-option v-for="item in mediaCombo" :key="item.id" :label="item.name" :value="item.id" />
      </el-select>
      <el-checkbox v-model="orderpublish" @change="handleQuery">仅已刊发</el-checkbox>
    </div>
    <!--
      查询列表
    -->
    <div class="table_box">
      <el-table
        :data="dataList"
        highlight-current-row
        :height="tableheight"
        :header-cell-style="tableHeaderColor"
        border
      >
        <el-table-column type="index" label="序号" :index="table_index" width="60" align="center">
        </el-table-column>
        <el-table-column :label="getLabel(1)" :prop="getProp(0)" min-width="160" align="center" />
        <el-table-column :label="getLabel(2)" :prop="getProp(1)" min-width="160" align="center" />
        <el-table-column
          v-if="queryvo.mediaId != ''"
          label="媒体"
          prop="medianame"
          min-width="160"
          align="center"
        />
        <el-table-column :label="getLabel(0)" prop="countdate" min-width="160" align="center" />
        <el-table-column label="签订金额(元)" prop="amountreceivable" min-width="160" align="right">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.amountreceivable, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="已收金额(元)" prop="amountreceived" min-width="160" align="right">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.amountreceived, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="欠款金额(元)" prop="amountarrearage" min-width="160" align="right">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.amountarrearage, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="业绩金额(元)"
          prop="amountachievement"
          min-width="160"
          align="right"
        >
          <template #default="scope">
            <span>{{ formatMoney(scope.row.amountachievement, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="任务额度(万元)" prop="ntaskamount" min-width="160" align="right">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.ntaskamount, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="任务描述" prop="sremark" min-width="160" align="center" />
        <el-table-column label="完成情况" prop="ratio" min-width="300" align="center" />
        <el-table-column
          label="操作"
          align="left"
          width="200"
          class-name="small-padding fixed-width"
          fixed="right"
        >
          <template #default="scope">
            <el-button
              link
              style="margin-left: 10px"
              type="success"
              icon="Edit"
              size="small"
              title="调整额度"
              @click="adjustTask(scope.row)"
              >调整额度</el-button
            >
            <el-button
              link
              style="margin-left: 10px"
              type="success"
              icon="Edit"
              size="small"
              title="发送消息"
              @click="sendMessage(scope.row)"
              >发送消息</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        v-model:current-page="queryvo.pageNum"
        class="pagination"
        style="margin-left: 10px; width: 100%"
        :page-sizes="pageSizes"
        :page-size="queryvo.pageSize"
        small
        background
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
    <el-dialog
      v-model="dialogUpdateVisible"
      :title="dgltitle"
      :width="700"
      append-to-body
      :destroy-on-close="true"
    >
      <div>
        <el-row>
          <el-col :span="22">
            <el-form
              ref="taskQuotaFormRef"
              :model="taskQuotaBody"
              label-width="120px"
              :rules="taskrules"
              class="demo-workReportForm"
              status-icon
            >
              <el-form-item label="任务类型">
                <span v-if="taskQuotaBody.spersonaltype == '0'">部门</span>
                <span v-if="taskQuotaBody.spersonaltype == '1'">人员</span>
              </el-form-item>
              <el-form-item label="时间类型">
                <span v-if="taskQuotaBody.stasktype == '0'">年度</span>
                <span v-if="taskQuotaBody.stasktype == '1'">月度</span>
              </el-form-item>
              <el-form-item label="名称">
                <span v-if="taskQuotaBody.spersonaltype == '0'">{{ taskQuotaBody.deptname }}</span>
                <span v-if="taskQuotaBody.spersonaltype == '1'">{{
                  taskQuotaBody.employname
                }}</span>
              </el-form-item>
              <el-form-item label="媒体">
                <span>{{ taskQuotaBody.medianame }}</span>
              </el-form-item>
              <el-form-item label="任务期间">
                <span>{{ taskQuotaBody.staskdate }}</span>
              </el-form-item>
              <el-form-item label="任务额度" prop="ntaskamount">
                <el-input-number
                  v-model="taskQuotaBody.ntaskamount"
                  placeholder="请输入任务额度"
                  :precision="4"
                  :controls="false"
                  :min="0.0001"
                  :max="1000000"
                  style="width: 200px"
                /><span style="margin-left: 20px">万元</span>
              </el-form-item>
              <el-form-item label="备注" prop="sremark">
                <el-input
                  v-model="taskQuotaBody.sremark"
                  :rows="3"
                  type="textarea"
                  class="inputwindht"
                  resize="none"
                  placeholder="请输入..."
                />
              </el-form-item>
              <el-form-item>
                <div style="text-align: right; margin-top: 60px; width: 100%">
                  <el-button type="primary" icon="Check" @click="updateTaskQuota(taskQuotaFormRef)"
                    >保存</el-button
                  >
                  <el-button icon="Close" @click="dialogUpdateVisible = false">取消</el-button>
                </div>
              </el-form-item>
            </el-form>
          </el-col>
        </el-row>
      </div>
    </el-dialog>
    <!--
      发送消息通知
    -->
    <el-dialog
      v-model="sendMessageVisible"
      title="发送消息通知"
      :width="600"
      append-to-body
      :destroy-on-close="true"
    >
      <div>
        <el-row>
          <el-col :span="24">
            <el-form
              ref="sendMessageFormRef"
              :model="taskMessageBody"
              label-width="120px"
              :rules="messagerules"
              class="demo-workReportForm"
              status-icon
            >
              <el-form-item label="消息接收者">
                <span>{{ taskMessageBody.departName }}</span>
              </el-form-item>
              <el-form-item label="消息标题" prop="title">
                <el-input v-model="taskMessageBody.title" class="inputwindht" />
              </el-form-item>
              <el-form-item label="消息内容" prop="content">
                <el-input
                  v-model="taskMessageBody.content"
                  :rows="3"
                  type="textarea"
                  class="inputwindht"
                  resize="none"
                  placeholder="请输入..."
                />
              </el-form-item>
              <el-form-item>
                <div style="text-align: center; margin-top: 60px">
                  <el-button
                    type="primary"
                    icon="Check"
                    @click="sendTaskMessage(sendMessageFormRef)"
                    >发送消息</el-button
                  >
                  <el-button icon="Close" @click="sendMessageVisible = false">取消</el-button>
                </div>
              </el-form-item>
            </el-form>
          </el-col>
        </el-row>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import moment from 'moment'
import useUserStore from '@/store/modules/user'
import { IDataCombo } from '@/types/common/DataCombo'
import { FormInstance, FormRules } from 'element-plus'
import baseTask from '@/types/views/business/tasks'
import { getMediaDataComboApi } from '@/api/media/media'
import {
  getDepartmentTaskCountListApi,
  getEmployeTaskCountListApi,
  sendTwtaskMessageApi
} from '@/api/business/taskcount'
import { getTwtaskBodyApi, addTwtaskApi, updateTwtaskApi } from '@/api/business/tasks'
import { TwtaskDTO, TwtaskMessageDTO } from '@/types/views/business/tasks'
import { IAxios } from 'axios'
import { computTableHeight, required, tableHeaderColor, formatMoney } from '@/utils'
const userStore = useUserStore()
defineOptions({
  name: 'TaskCount'
})
onMounted(() => {
  initTime()
  getMediaDataCombo()
  handleQuery()
  tableheight.value = computTableHeight()
})

/** 部门/人员选项 */
const deporemploy = ref('0')
/** 年度/月度选项 */
const datetype = ref('0')
/** 部门级别 */
const deplevel = ref('2')
/** 部门/人员开关 */
const showdept = ref(0)
/** 年度 */
const dateYear = ref<Date | string>('')
/** 月度 */
const dateMonth = ref<Date | string>('')
/** 年度/月度开关 */
const showyear = ref(0)
/** 媒体 */
const mediaId = ref('')
/** 刊发 */
const orderpublish = ref(false)
/** 媒体下拉列表 */
const mediaCombo = ref<IDataCombo[]>([])
/** 起始日期 */
const startTime = ref('')
/** 列表 */
const dataList = ref<any>([])
/** 结束日期 */
const endTime = ref('')
/** 查询记录总条数 */
const total = ref(0)
/** 额度调整标题 */
const dgltitle = ref('添加额度')
/** 每页选择下拉 */
const pageSizes = ref<number[]>([10, 20, 30, 40])
/** 表格高度 */
const tableheight = ref(0)
/** 发送消息请求对象 */
const taskMessageBody = ref<TwtaskMessageDTO>({
  messagetype: '0',
  departid: '',
  emplyeid: '',
  title: '',
  content: '',
  departName: ''
})
/** 任务额度实体对象 */
const taskQuotaBody = ref<TwtaskDTO>({
  id: '',
  operatorid: '',
  operatorname: '',
  stasktype: '',
  spersonaltype: '',
  deptid: '',
  deptname: '',
  employid: '',
  employname: '',
  mediaid: '',
  medianame: '',
  staskdate: '',
  ntaskamount: 0,
  sremark: ''
})
/** 任务额度调整窗口 */
const dialogUpdateVisible = ref(false)
/** 发送消息通知窗口 */
const sendMessageVisible = ref(false)
/** 请求对象 */
const queryvo = ref({
  mediaId: '',
  dateType: '0',
  stratTime: '',
  endTime: '',
  pageNum: 0,
  pageSize: 20,
  publishstatus: '',
  depLevel: '2'
})
/** 任务额度记录请求对象 */
const querytaskvo = ref({
  taskid: ''
})
/**
 * 调整额度校验参数
 */
const taskrules = reactive<FormRules<TwtaskDTO>>({
  ntaskamount: [{ required: true, message: '请输入任务额度', trigger: 'blur' }]
})
/**
 * 发送消息校验参数
 */
const messagerules = reactive<FormRules<TwtaskMessageDTO>>({
  title: [{ required: true, message: '请输入消息标题', trigger: 'blur' }],
  content: [{ required: true, message: '请输入消息内容', trigger: 'blur' }]
})
/** 调整任务额度表单对象 */
const taskQuotaFormRef = ref<FormInstance>()
/** 发送通知表单对象 */
const sendMessageFormRef = ref<FormInstance>()
/**
 * 查询
 */
const handleQuery = () => {
  if (showdept.value === 0) {
    handleDepartQuery()
  } else if (showdept.value === 1) {
    handleEmployeQuery()
  }
}
/**
 * 部门/人员选择事件
 */
const handleDepORemploy = (event: any) => {
  if (event === '0') {
    showdept.value = 0
  } else {
    showdept.value = 1
  }
  handleQuery()
}
/**
 * 年度/人员选择事件
 */
const handleDateType = (event: any) => {
  if (event === '0') {
    dateYear.value = new Date()
    showyear.value = 0
    initTime()
  } else {
    showyear.value = 1
    dateMonth.value = new Date()
    initMonth()
  }
  handleQuery()
}
/**
 * 媒体事件
 */
const handleMedia = (event: any) => {
  handleQuery()
}
/**
 * 获取媒体下拉列表
 */
const getMediaDataCombo = () => {
  getMediaDataComboApi().then((res: IAxios<IDataCombo[]>) => {
    if (res.success) {
      mediaCombo.value = res.obj
    }
  })
}
/**
 * 年度
 */
const changeyear = (event: any) => {
  var year = new Date(dateYear.value).getFullYear()
  startTime.value = year + '-01-01'
  endTime.value = year + '-12-31'
  handleQuery()
}
/**
 * 月度
 */
const changemonth = (event: any) => {
  var year = new Date(dateMonth.value).getFullYear()
  var month = new Date(dateMonth.value).getMonth() + 1
  const m = moment(dateMonth.value)
  const curDays = getMonthDays(m) // 当月天数

  if (month < 9) {
    startTime.value = year.toString() + '-0' + month.toString() + '-01'
    endTime.value = year.toString() + '-0' + month.toString() + '-' + curDays
  } else {
    startTime.value = year.toString() + '-' + month.toString() + '-01'
    endTime.value = year.toString() + '-' + month.toString() + '-' + curDays
  }
  handleQuery()
}
/**
 * 获取月份天数
 */
const getMonthDays = (momentObj: any) => {
  return momentObj.daysInMonth()
}
/**
 * 获取序号
 */
const table_index = (val: number) => {
  // eslint-disable-next-line eqeqeq
  const pagenum =
    queryvo.value.pageSize! * (queryvo.value.pageNum === 0 ? 0 : queryvo.value.pageNum! - 1) + 1
  return val + pagenum
}
/**
 * 调整额度
 */
const adjustTask = async (row: any) => {
  if (row.taskid === 0) {
    if (showdept.value === 0) {
      taskQuotaBody.value.spersonaltype = '0'
      taskQuotaBody.value.deptname = row.departmentname
    } else {
      taskQuotaBody.value.spersonaltype = '1'
      taskQuotaBody.value.employname = row.employename
    }
    if (showyear.value === 0) {
      taskQuotaBody.value.stasktype = '0'
    } else {
      taskQuotaBody.value.stasktype = '1'
    }
    taskQuotaBody.value.id = 0
    if (row.deptid != null) {
      taskQuotaBody.value.deptid = row.deptid
    } else {
      taskQuotaBody.value.deptid = '-1'
    }
    taskQuotaBody.value.employid = row.employid
    taskQuotaBody.value.staskdate = row.countdate
    // eslint-disable-next-line eqeqeq
    if (row.mediaid != 0) {
      taskQuotaBody.value.mediaid = row.mediaid
      taskQuotaBody.value.medianame = row.medianame
    }
    dgltitle.value = '添加额度'
    dialogUpdateVisible.value = true
    console.log('新建任务额度')
  } else {
    querytaskvo.value.taskid = row.taskid
    getTwtaskBodyApi(querytaskvo.value).then((res) => {
      if (res.success) {
        taskQuotaBody.value = res.obj
        dgltitle.value = '调整额度'
        dialogUpdateVisible.value = true
        console.log(taskQuotaBody.value)
      }
    })
  }
}
/**
 * 发送消息
 */
const sendMessage = async (row: any) => {
  if (showdept.value === 0) {
    taskMessageBody.value.messagetype = '0'
    taskMessageBody.value.departid = row.departid
    taskMessageBody.value.departName = row.departmentname
  } else {
    taskMessageBody.value.messagetype = '1'
    taskMessageBody.value.emplyeid = row.employid
    taskMessageBody.value.departName = row.employename
  }
  sendMessageVisible.value = true
}
/**
 * 初始化时间
 */
const initTime = () => {
  dateYear.value = new Date()
  var year = new Date().getFullYear()
  startTime.value = year + '-01-01'
  endTime.value = year + '-12-31'
}
/**
 * 初始化月度
 */
const initMonth = () => {
  var year = new Date().getFullYear()
  var month = new Date().getMonth() + 1
  const m = moment(dateMonth.value)
  const curDays = getMonthDays(m) // 当月天数
  if (month < 9) {
    startTime.value = year.toString() + '-0' + month.toString() + '-01'
    endTime.value = year.toString() + '-0' + month.toString() + '-' + curDays
  } else {
    startTime.value = year.toString() + '-' + month.toString() + '-01'
    endTime.value = year.toString() + '-' + month.toString() + '-' + curDays
  }
}
/**
 * 获取lable
 */
const getLabel = (val: any) => {
  if (val === 0) {
    if (datetype.value === '0') return '时间(年度)'
    if (datetype.value === '1') return '时间(月度)'
  }
  if (val === 1) {
    if (showdept.value === 0) return '上级部门'
    if (showdept.value === 1) return '部门'
  }
  if (val === 2) {
    if (showdept.value === 0) return '部门'
    if (showdept.value === 1) return '人员'
  }
}
/**
 * 改变页码总数调用
 */
const handleSizeChange = (val: number) => {
  queryvo.value.pageSize = val
  handleQuery()
}
/**
 * 改变页数调用
 * @param val
 */
const handleCurrentChange = (val: number) => {
  queryvo.value.pageNum = val
  handleQuery()
}
/**
 * 部门汇总
 */
const handleDepartQuery = () => {
  queryvo.value.mediaId = mediaId.value
  queryvo.value.stratTime = startTime.value
  queryvo.value.endTime = endTime.value
  queryvo.value.depLevel = deplevel.value
  queryvo.value.dateType = datetype.value
  if (orderpublish.value) queryvo.value.publishstatus = '1'
  else {
    queryvo.value.publishstatus = ''
  }
  getDepartmentTaskCountListApi(queryvo.value).then((res) => {
    if (res.success) {
      dataList.value = res.obj
      total.value = res.total
      console.log(dataList.value)
    }
  })
}
/**
 * 人员汇总
 */
const handleEmployeQuery = () => {
  queryvo.value.mediaId = mediaId.value
  queryvo.value.stratTime = startTime.value
  queryvo.value.endTime = endTime.value
  queryvo.value.depLevel = deplevel.value
  queryvo.value.dateType = datetype.value
  if (orderpublish.value) queryvo.value.publishstatus = '1'
  else {
    queryvo.value.publishstatus = ''
  }
  getEmployeTaskCountListApi(queryvo.value).then((res) => {
    if (res.success) {
      dataList.value = res.obj
      total.value = res.total
      console.log(dataList.value)
    }
  })
}
/**
 * 获取prop
 */
const getProp = (val: any) => {
  if (val === 0) {
    if (showdept.value === 0) return 'superiorname'
    if (showdept.value === 1) return 'departmentname'
  }
  if (val === 1) {
    if (showdept.value === 0) return 'departmentname'
    if (showdept.value === 1) return 'employename'
  }
}
/**
 * 调整额度
 */
const updateTaskQuota = async (formEl: FormInstance | undefined) => {
  if (!formEl) return
  await formEl.validate((valid, fields) => {
    if (valid) {
      if (taskQuotaBody.value.id === 0) {
        addTwtaskApi(taskQuotaBody.value).then((res: IAxios) => {
          if (res.success) {
            dialogUpdateVisible.value = false
            handleQuery()
            ElMessage.success(res.msg)
          }
        })
      } else {
        updateTwtaskApi(taskQuotaBody.value).then((res: IAxios) => {
          if (res.success) {
            dialogUpdateVisible.value = false
            handleQuery()
            ElMessage.success(res.msg)
          }
        })
      }
    }
  })
}
/**
 * 发送消息
 */
const sendTaskMessage = async (formEl: FormInstance | undefined) => {
  if (!formEl) return
  await formEl.validate((valid, fields) => {
    if (valid) {
      sendTwtaskMessageApi(taskMessageBody.value).then((res: IAxios) => {
        if (res.success) {
          sendMessageVisible.value = false
          ElMessage.success(res.msg)
        }
      })
    }
  })
}
</script>

<style scoped>
.text {
  font-size: 14px;
}

.item {
  margin-bottom: 0px;
}

.box-card {
  width: 280px;
  max-height: 600px;
  overflow-y: auto;
}
</style>
