<!--
 * @Author: lhl
 * @Date: 2023-11-06
 * @LastEditTime: 2024-02-20 09:31:56
 * @LastEditors: lhl
 * @Description: 任务设置
-->

<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex">
        <el-button type="primary" icon="Plus" @click="handleAddDepartment">新增部门任务</el-button>
        <el-button type="primary" icon="Plus" @click="handleAddEmployee">新增人员任务</el-button>
        <el-button type="primary" :disabled="isUpdateButton" @click="handleUpdate"
          >批量更新</el-button
        >
        <el-button type="danger" :disabled="isUpdateButton" @click="handleBatchDelete"
          >批量删除</el-button
        >
        <el-button type="primary" :disabled="isCopyButtonEnable()" @click="handleBatchCopy"
          >批量复制</el-button
        >

        <el-input
          v-model="queryParams.queryKey"
          placeholder="支持部门、人员名称模糊查询"
          clearable
          style="width: 240px"
          @clear="handleQuery"
        />
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-tooltip
          class="item"
          effect="dark"
          :content="showSearch ? '隐藏非常用' : '显示非常用'"
          placement="top"
        >
          <el-button
            circle
            style="position: static"
            :icon="showSearch == false ? 'ArrowRightBold' : 'ArrowDownBold'"
            @click="toggleSearch()"
          />
        </el-tooltip>
      </div>
      <div v-show="showSearch" class="showSearch">
        <HgDateRange :initdate="timedata" @onresponse="ontime"></HgDateRange>
        <el-select
          v-model="queryParams.sTaskType"
          placeholder="请选择任务类型"
          style="width: 100px; margin-right: 5px"
          @change="handleQuery"
          @clear="handleQuery"
        >
          <el-option
            v-for="item in baseTask.TaskType"
            :key="item.id"
            :label="item.value"
            :value="item.id"
          ></el-option>
        </el-select>
        <el-select
          v-model="queryParams.sTaskCategory"
          placeholder="请选择任务类别"
          style="width: 100px; margin-right: 5px"
          @change="handleQuery"
          @clear="handleQuery"
        >
          <el-option
            v-for="item in baseTask.TaskCategory"
            :key="item.id"
            :label="item.value"
            :value="item.id"
          ></el-option>
        </el-select>
        <el-select v-model="mediaId" placeholder="请选择媒体" clearable @change="handleMedia">
          <el-option
            v-for="item in mediaCombo"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </div>
    </div>
    <!--
      查询列表
    -->
    <div class="table_box">
      <el-table
        ref="dataTableRef"
        :data="taskReportList"
        highlight-current-row
        :height="tableheight"
        :header-cell-style="tableHeaderColor"
        border
        @selection-change="handleSelectionChange"
        @sort-change="changeSort"
      >
        <el-table-column
          type="selection"
          prop="ischeck"
          :width="50"
          align="center"
        ></el-table-column>
        <el-table-column type="index" label="序号" :index="table_index" width="60" align="center">
        </el-table-column>
        <el-table-column
          label="部门名称"
          prop="deptname"
          min-width="160"
          sortable
          show-overflow-tooltip
        />
        <el-table-column
          label="人员名称"
          prop="employname"
          min-width="160"
          sortable
          show-overflow-tooltip
        />
        <el-table-column
          label="媒体名称"
          prop="medianame"
          min-width="160"
          sortable
          show-overflow-tooltip
        />
        <el-table-column label="任务类别" prop="stasktype" min-width="160" show-overflow-tooltip>
          <template #default="scope">
            <span v-if="parseInt(scope.row.stasktype) === ETaskCategory.年度" style="color: #e6a23c"
              >年度</span
            >
            <span v-if="parseInt(scope.row.stasktype) === ETaskCategory.月度" style="color: #409eff"
              >月度</span
            >
          </template>
        </el-table-column>
        <el-table-column
          label="任务类型"
          prop="spersonaltype"
          min-width="160"
          show-overflow-tooltip
        >
          <template #default="scope">
            <span v-if="parseInt(scope.row.spersonaltype) === ETaskType.部门" style="color: #e6a23c"
              >部门</span
            >
            <span v-if="parseInt(scope.row.spersonaltype) === ETaskType.人员" style="color: #409eff"
              >人员</span
            >
          </template>
        </el-table-column>
        <el-table-column label="任务日期" prop="staskdate" min-width="160" show-overflow-tooltip />
        <el-table-column
          label="任务金额(万元)"
          prop="ntaskamount"
          min-width="140"
          sortable
          show-overflow-tooltip
          align="right"
        >
          <template #default="scope">
            <span>{{ formatMoney(scope.row.ntaskamount, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="备注" prop="sremark" min-width="160" show-overflow-tooltip />
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
              title="修改"
              @click="onEdit(scope.row)"
              >修改</el-button
            >
            <el-button
              link
              style="margin-left: 10px"
              type="danger"
              icon="Edit"
              size="small"
              title="删除"
              @click="onDelete(scope.row)"
              >删除</el-button
            >
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
    <!--
      新增部门任务窗口
    -->
    <el-dialog
      v-model="dialogAddDepartmentTaskVisible"
      :title="getAddTaskTitle()"
      :width="800"
      append-to-body
      :destroy-on-close="true"
    >
      <div>
        <el-row :gutter="20">
          <el-col v-if="taskQuotaForm.sTaskType == '0'" :span="10">
            <div>
              <HgZtree
                style="width: 120px"
                :scopeflag="EHgDeptZtreeUrl.DEPT_GETDEPTTREE"
                :treeheight="300"
                :expandall="true"
                :checkboxtype="true"
                @selectionztree="onSelectionDepart"
              >
              </HgZtree>
            </div>
          </el-col>
          <el-col v-if="taskQuotaForm.sTaskType == '1'" :span="10">
            <div style="height: 300px">
              <HgZtree
                style="width: 120px"
                :scopeflag="EHgDeptZtreeUrl.EMP_GETDEPTEMPLOY"
                :treeheight="300"
                :expandall="true"
                :checkboxtype="true"
                @selectionztree="onSelectionDepart"
              >
              </HgZtree>
            </div>
          </el-col>
          <el-col :span="14">
            <el-form
              ref="taskQuotaFormRef"
              :model="taskQuotaForm"
              :rules="rules"
              label-width="120px"
              class="demo-workReportForm"
              status-icon
            >
              <el-form-item label="时间类型" prop="sCategoryType">
                <el-radio-group v-model="taskQuotaForm.sCategoryType" @change="onChangeType">
                  <el-radio
                    v-for="worktype in baseTask.TaskTime"
                    :key="worktype.id"
                    :label="worktype.id"
                    >{{ worktype.value }}</el-radio
                  >
                </el-radio-group>
              </el-form-item>
              <el-form-item label="年度范围" prop="sStartYear">
                <HgDateRange :initdate="timedata" @onresponse="onAddTime"></HgDateRange>
              </el-form-item>
              <el-form-item label="任务额度" prop="dQuota">
                <el-input-number
                  v-model="taskQuotaForm.dQuota"
                  placeholder="请输入任务额度"
                  :precision="4"
                  :controls="false"
                  :min="0.0001"
                  :max="1000000"
                  style="width: 240px"
                /><span style="margin-left: 20px">万元</span>
              </el-form-item>
              <el-form-item label="媒体">
                <el-select
                  v-model="dlgmediaId"
                  placeholder="请选择媒体"
                  clearable
                  style="width: 295px"
                  @change="handleMediaChange"
                >
                  <el-option
                    v-for="item in mediaCombo"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  />
                </el-select>
              </el-form-item>

              <el-form-item label="备注" prop="remarks">
                <el-input
                  v-model="taskQuotaForm.remarks"
                  :rows="3"
                  type="textarea"
                  class="inputwindht"
                  resize="none"
                  placeholder="请输入..."
                  style="width: 295px"
                />
              </el-form-item>

              <el-form-item>
                <div style="text-align: right; margin-top: 60px; width: 100%">
                  <el-button
                    type="primary"
                    icon="Check"
                    @click="saveTaskQuotaForm(taskQuotaFormRef)"
                    >保存</el-button
                  >
                  <el-button icon="Close" @click="dialogAddDepartmentTaskVisible = false"
                    >取消</el-button
                  >
                </div>
              </el-form-item>
            </el-form>
          </el-col>
        </el-row>
      </div>
    </el-dialog>
    <!--
      编辑人员额度对话框
    -->
    <el-dialog
      v-model="dialogUpdateVisible"
      title="编辑额度"
      :width="700"
      append-to-body
      :destroy-on-close="true"
    >
      <div>
        <el-row>
          <el-col :span="22">
            <el-form
              ref="taskQuotaBodyFormRef"
              :model="taskQuotaBodyForm"
              label-width="120px"
              class="demo-workReportForm"
              status-icon
            >
              <el-form-item label="任务类型">
                <span v-if="taskQuotaBodyForm.spersonaltype == '0'">部门</span>
                <span v-if="taskQuotaBodyForm.spersonaltype == '1'">人员</span>
              </el-form-item>
              <el-form-item label="时间类型">
                <span v-if="taskQuotaBodyForm.stasktype == '0'">年度</span>
                <span v-if="taskQuotaBodyForm.stasktype == '1'">月度</span>
              </el-form-item>
              <el-form-item label="名称">
                <span v-if="taskQuotaBodyForm.spersonaltype == '0'">{{
                  taskQuotaBodyForm.deptname
                }}</span>
                <span v-if="taskQuotaBodyForm.spersonaltype == '1'">{{
                  taskQuotaBodyForm.employname
                }}</span>
              </el-form-item>
              <el-form-item label="媒体">
                <span>{{ taskQuotaBodyForm.medianame }}</span>
              </el-form-item>
              <el-form-item label="任务期间">
                <span>{{ taskQuotaBodyForm.staskdate }}</span>
              </el-form-item>
              <el-form-item label="任务额度" prop="ntaskamount">
                <el-input-number
                  v-model="taskQuotaBodyForm.ntaskamount"
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
                  v-model="taskQuotaBodyForm.sremark"
                  :rows="3"
                  type="textarea"
                  class="inputwindht"
                  resize="none"
                  placeholder="请输入..."
                />
              </el-form-item>
              <el-form-item>
                <div style="text-align: right; margin-top: 60px; width: 100%">
                  <el-button type="primary" icon="Check" @click="updateTaskQuota()">保存</el-button>
                  <el-button icon="Close" @click="dialogUpdateVisible = false">取消</el-button>
                </div>
              </el-form-item>
            </el-form>
          </el-col>
        </el-row>
      </div>
    </el-dialog>

    <!--
      批量更新对话框
    -->
    <el-dialog
      v-model="dialogBatchUpdateVisible"
      title="批量更新任务额度"
      :width="800"
      append-to-body
      :destroy-on-close="true"
    >
      <div>
        <el-row>
          <el-col :span="24">
            <el-form
              ref="taskQuotaBodyFormRef"
              :model="taskQuotaBodyForm"
              label-width="120px"
              class="demo-workReportForm"
              status-icon
            >
              <el-form-item label="任务额度" prop="ntaskamount">
                <el-input-number
                  v-model="taskQuotaBodyForm.ntaskamount"
                  placeholder="请输入任务额度"
                  :controls="false"
                  :precision="4"
                  :min="0.0001"
                  :max="1000000"
                  style="width: 200px"
                /><span style="margin-left: 20px">万元</span>
              </el-form-item>
              <el-form-item label="备注" prop="sremark">
                <el-input
                  v-model="taskQuotaBodyForm.sremark"
                  :rows="3"
                  type="textarea"
                  class="inputwindht"
                  resize="none"
                  placeholder="请输入..."
                />
              </el-form-item>
              <el-form-item>
                <div style="text-align: center; margin-top: 60px">
                  <el-button type="primary" icon="Check" @click="batchUpdateTaskQuota()"
                    >保存</el-button
                  >
                  <el-button icon="Close" @click="dialogBatchUpdateVisible = false">取消</el-button>
                </div>
              </el-form-item>
            </el-form>
          </el-col>
        </el-row>
      </div>
    </el-dialog>

    <!--
      批量复制对话框
    -->
    <el-dialog
      v-model="dialogBatchCopyVisible"
      title="批量复制"
      :width="500"
      append-to-body
      :destroy-on-close="true"
    >
      <div>
        <el-row>
          <el-col :span="24">
            <el-form label-width="120px" class="demo-workReportForm" status-icon>
              <el-form-item label="复制年度">
                <el-date-picker
                  v-model="copyYear"
                  :editable="false"
                  type="year"
                  placeholder="选择年度"
                  :clearable="false"
                  style="width: 130px; margin-right: 5px"
                >
                </el-date-picker>
              </el-form-item>

              <el-form-item>
                <div style="text-align: center; margin-top: 60px">
                  <el-button type="primary" icon="Check" @click="updateBatchTaskQuota()"
                    >复制</el-button
                  >
                  <el-button icon="Close" @click="dialogBatchCopyVisible = false">取消</el-button>
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
import useUserStore from '@/store/modules/user'
import baseTask from '@/types/views/business/tasks'
import { ETaskType, ETaskCategory, EHgDeptZtreeUrl } from '@/types/common/enumindex'
import {
  ITaskReportQuery,
  ITaskReportsDTO,
  ITaskQuotaAddDTO,
  ITaskQuotaUpdateDTO
} from '@/types/views/business/tasks'
import { TSelectZtree } from '@/types/common'
import { computTableHeight, required, tableHeaderColor, formatMoney } from '@/utils'
import { IAxios } from 'axios'
import {
  getTaskQuotaPageListtApi,
  saveTaskQuotaApi,
  updateTaskQuotaApi,
  deleteTaskQuotaApi,
  batchCopyTaskQuotaApi
} from '@/api/business/tasks'
import { TDateTimeType } from '@/types/components/hgdateindex'
import { FormInstance, FormRules } from 'element-plus'
import { IDataCombo } from '@/types/common/DataCombo'
import { getMediaDataComboApi } from '@/api/media/media'
import dayjs from 'dayjs'
import { truncate } from 'lodash'

/** 列表对象 */
const taskReportList = ref<ITaskReportsDTO[]>([])
/** 表格高度 */
const tableheight = ref(0)
/** 每页选择下拉 */
const pageSizes = ref<number[]>([10, 20, 30, 40])
/** 查询记录总条数 */
const total = ref(0)
/** 新增部门任务窗口显示状态 */
const dialogAddDepartmentTaskVisible = ref(false)
/** 任务额度编辑窗口 */
const dialogUpdateVisible = ref(false)
/** 批量更新任务额度编辑窗口 */
const dialogBatchUpdateVisible = ref(false)
/** 批量复制窗口 */
const dialogBatchCopyVisible = ref(false)
/** 新增任务额度表单对象 */
const taskQuotaFormRef = ref<FormInstance>()
/** 媒体下拉列表 */
const mediaCombo = ref<IDataCombo[]>([])
/** 查询关键字 */
const queryKey = ref('')
/** 复制年度 */
const copyYear = ref<Date | string>('')
/** 新增任务额度请求对象 */
const taskQuotaForm = reactive<ITaskQuotaAddDTO>({
  sTaskType: '0',
  sCategoryType: ETaskCategory.年度.toString(),
  sStartYear: '',
  sEndYear: '',
  remarks: '',
  idList: '',
  mediaId: '',
  dQuota: 1
})
/** 任务额度实体对象 */
const taskQuotaBodyForm = ref<ITaskReportsDTO>({
  stasktype: '0',
  ntaskamount: 1,
  employname: '',
  deptname: '',
  staskdate: '',
  sremark: '',
  medianame: '',
  spersonaltype: '0',
  mediaid: ''
})
/** 批量更新任务额度请求对象 */
const taskQuotaUpdateForm = reactive<ITaskQuotaUpdateDTO>({
  dQuota: 0,
  sremark: '',
  idList: []
})

/** 媒体 */
const mediaId = ref('')

/** 媒体 */
const dlgmediaId = ref('')

/**
 * 选中数据存放
 * @protected
 * @return array
 */
let multipleSelection: Array<any> = []

/** 是否禁用批量更新按钮 */
const isUpdateButton = ref(true)
/** 显示隐藏非常用**/
const showSearch = ref(false)
/**
 * 校验设置参数
 */
const rules = reactive<FormRules<ITaskQuotaAddDTO>>({
  sCategoryType: [
    { validator: required, required: true, message: '请选择时间类型', trigger: 'blur' }
  ],
  dQuota: [{ required: true, message: '请输入任务额度', trigger: 'blur' }],
  sStartYear: [{ required: true, message: '起始年度和终止年度不能为空', trigger: 'change' }],
  sEndYear: [{ required: true, message: '终止年度不能为空', trigger: 'change' }]
})
/** 显示非常用**/
const toggleSearch = () => {
  showSearch.value = !showSearch.value
  nextTick(function () {
    tableheight.value = computTableHeight()
  })
}
/**
 * 获取起始年度
 * @param val
 */
const getStratTear = () => {
  const year = new Date().getFullYear()
  return dayjs(new Date(year + '-01-01')).format('YYYY-MM-DD')
}

/**
 * 获取结束年度
 * @param val
 */
const getEndTear = () => {
  const year = new Date().getFullYear()
  return dayjs(new Date(year + '-12-31')).format('YYYY-MM-DD')
}

/**
 * 初始化默认年度
 */
const initDate = () => {
  var startTime = new Date().toDateString()
  var endTime = new Date().toDateString()
  var startYear = new Date(startTime).getFullYear()
  var endYear = new Date(endTime).getFullYear()
  taskQuotaForm.sStartYear = startYear.toString()
  taskQuotaForm.sEndYear = endYear.toString()
}

/**
 * 初始化默认复制年度
 */
const initCopyDate = () => {
  var year = parseInt(String(queryParams.startTime))
  // 复制年度递增一年
  year += 1
  copyYear.value = new Date(year.toString() + '-01-01')
}

const initDialog = () => {
  initDate()
  taskQuotaForm.sCategoryType = '0'
  taskQuotaForm.dQuota = 1
  taskQuotaForm.idList = []
  taskQuotaForm.mediaId = ''
  taskQuotaForm.remarks = ''
  dlgmediaId.value = ''
}

/** 查询条件 */
const queryParams = reactive<ITaskReportQuery>({
  page: 0,
  pageSize: 20,
  sTaskType: '0',
  sTaskCategory: '1',
  startTime: getStratTear(),
  endTime: getEndTear(),
  medidId: '',
  querykey: '',
  sortType: 0,
  sortRule: 1
})

/** 初始化时间 */
const timedata = reactive<TDateTimeType>({
  startTime: new Date().toDateString(),
  endTime: new Date().toDateString()
})

onMounted(() => {
  getMediaDataCombo()
  handleQuery()
  /**
   * 计算表格高度
   */
  tableheight.value = computTableHeight()
  initDate()
})

/**
 * 新增部门任务
 */
const handleAddDepartment = () => {
  initDialog()
  taskQuotaForm.sTaskType = '0'
  dialogAddDepartmentTaskVisible.value = true
}

/**
 * 新增人员任务
 */
const handleAddEmployee = () => {
  initDialog()
  taskQuotaForm.sTaskType = '1'
  dialogAddDepartmentTaskVisible.value = true
}

/**
 * 批量更新
 */
const handleUpdate = () => {
  taskQuotaBodyForm.value.ntaskamount = 1.0
  taskQuotaBodyForm.value.sremark = ''
  dialogBatchUpdateVisible.value = true
}

/**
 * 批量复制
 */
const handleBatchCopy = () => {
  initCopyDate()
  dialogBatchCopyVisible.value = true
}

/**
 * 批量删除
 */
const handleBatchDelete = () => {
  taskQuotaUpdateForm.idList = []
  for (var i = 0; i < multipleSelection.length; i++) {
    taskQuotaUpdateForm.idList.push(multipleSelection[i].id)
  }
  ElMessageBox.confirm('是否删除选择的数据？', '提示', {
    type: 'warning'
  })
    .then(() => {
      deleteTaskQuotaApi(taskQuotaUpdateForm)
        .then((res: IAxios) => {
          if (res.success) {
            ElMessage.success('删除成功!')
            handleQuery()
          }
        })
        .catch(() => {})
    })
    .catch(() => {})
}

/**
 * 查询
 */
const handleQuery = () => {
  getTaskQuotaPageListtApi(queryParams)
    .then((res: IAxios<ITaskReportsDTO[]>) => {
      if (res.success) {
        console.log(res.obj)
        taskReportList.value = res.obj
        total.value = res.total
      }
    })
    .finally(() => {})
}

/**
 * 改变页码总数调用
 */
const handleSizeChange = (val: number) => {
  queryParams.pageSize = val
  handleQuery()
}
/**
 * 改变页数调用
 * @param val
 */
const handleCurrentChange = (val: number) => {
  queryParams.page = val
  handleQuery()
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
 * 时间选择
 * @param val
 */
const onAddTime = (val: TDateTimeType) => {
  var startYear = new Date(val.startTime).getFullYear()
  var endYear = new Date(val.endTime).getFullYear()
  taskQuotaForm.sStartYear = startYear.toString()
  taskQuotaForm.sEndYear = endYear.toString()
}

/**
 * 选择人员
 * @param val
 */
const onSelectionEmp = (val: TSelectZtree[]) => {
  console.log('人员树选择')
  const employid = val.map((item) => item.id).join(',')
  console.log(employid)
}

/**
 * 选择部门
 * @param val
 */
const onSelectionDepart = (val: TSelectZtree[]) => {
  const departid = val.map((item) => item.id).join(',')
  taskQuotaForm.idList = departid
}

const table_index = (val: number) => {
  // eslint-disable-next-line eqeqeq
  const pagenum = queryParams.pageSize! * (queryParams.page == 0 ? 0 : queryParams.page! - 1) + 1
  return val + pagenum
}

/**
 * 任务额度时间类型调整
 */
const onChangeType = () => {}

/**
 * 编辑保存
 * @param formEl
 */
const saveTaskQuotaForm = async (formEl: FormInstance | undefined) => {
  if (!formEl) return
  await formEl.validate((valid, fields) => {
    if (valid) {
      if (verifyTaskQuotaForm()) {
        saveTaskQuotaApi(taskQuotaForm)
          .then((res: IAxios) => {
            if (res.success) {
              ElMessage.success(res.msg)
              dialogAddDepartmentTaskVisible.value = false
              handleQuery()
            }
          })
          .catch(() => {})
      }
    }
  })
}

/**
 * 验证数据是否符合需求
 */
const verifyTaskQuotaForm = () => {
  var startYear = taskQuotaForm.sStartYear!
  var endYear = taskQuotaForm.sEndYear!
  if (endYear < startYear) {
    ElMessage.error('起始年度不能大于终止年度')
    return false
  }
  if (taskQuotaForm.idList.length <= 0) {
    ElMessage.error('部门或人员不能为空，请选择部门或人员')
    return false
  }
  return true
}

/**
 * 更新
 */
const updateTaskQuota = async () => {
  taskQuotaUpdateForm.dQuota = taskQuotaBodyForm.value.ntaskamount
  taskQuotaUpdateForm.sremark = taskQuotaBodyForm.value.sremark
  taskQuotaUpdateForm.idList = []
  taskQuotaUpdateForm.idList.push(taskQuotaBodyForm.value.id)
  updateTaskQuotaApi(taskQuotaUpdateForm)
    .then((res: IAxios) => {
      if (res.success) {
        ElMessage.success(res.msg)
        dialogUpdateVisible.value = false
        handleQuery()
      }
    })
    .catch(() => {})
}

/**
 * 批量更新
 */
const batchUpdateTaskQuota = async () => {
  taskQuotaUpdateForm.dQuota = taskQuotaBodyForm.value.ntaskamount
  taskQuotaUpdateForm.sremark = taskQuotaBodyForm.value.sremark
  taskQuotaUpdateForm.idList = []
  for (var i = 0; i < multipleSelection.length; i++) {
    taskQuotaUpdateForm.idList.push(multipleSelection[i].id)
  }
  updateTaskQuotaApi(taskQuotaUpdateForm)
    .then((res: IAxios) => {
      if (res.success) {
        ElMessage.success(res.msg)
        dialogBatchUpdateVisible.value = false
        handleQuery()
      }
    })
    .catch(() => {})
}
/**
 * 批量复制
 */
const updateBatchTaskQuota = async () => {
  var startYear = new Date(copyYear.value).getFullYear()
  taskQuotaUpdateForm.sremark = startYear.toString()
  taskQuotaUpdateForm.idList = []
  for (var i = 0; i < multipleSelection.length; i++) {
    taskQuotaUpdateForm.idList.push(multipleSelection[i].id)
  }
  batchCopyTaskQuotaApi(taskQuotaUpdateForm)
    .then((res: IAxios) => {
      if (res.success) {
        ElMessage.success(res.msg)
        dialogBatchCopyVisible.value = false
        handleQuery()
      }
    })
    .catch(() => {})
}

/**
 * 删除按钮事件
 * @param row
 */
const onDelete = (row: ITaskReportsDTO | undefined) => {
  taskQuotaUpdateForm.idList = []
  taskQuotaUpdateForm.idList.push(row?.id)
  ElMessageBox.confirm('是否删除选择的数据？', '提示', {
    type: 'warning'
  })
    .then(() => {
      deleteTaskQuotaApi(taskQuotaUpdateForm)
        .then((res: IAxios) => {
          if (res.success) {
            ElMessage.success('删除成功!')
            handleQuery()
          }
        })
        .catch(() => {})
    })
    .catch(() => {})
}

/**
 * 获取媒体下拉列表
 */
const getMediaDataCombo = () => {
  getMediaDataComboApi().then((res: IAxios<IDataCombo[]>) => {
    if (res.success) {
      mediaCombo.value = res.obj
      console.log('媒体')
      console.log(mediaCombo.value)
    }
  })
}

/**
 * 媒体事件
 */
const handleMedia = () => {
  // eslint-disable-next-line no-self-compare
  queryParams.medidId = mediaId.value
  handleQuery()
}

/**
 * 媒体事件
 */
const handleMediaChange = () => {
  taskQuotaForm.mediaId = dlgmediaId.value
  handleQuery()
}

/**
 * 获取添加任务额度对话框标题
 */
const getAddTaskTitle = () => {
  // eslint-disable-next-line eqeqeq
  if (taskQuotaForm.sTaskType == '0') {
    return '新增部门任务额度'
  } else {
    return '新增人员任务额度'
  }
}

/**
 * 编辑按钮事件
 * @param row
 */
const onEdit = (row: ITaskReportsDTO) => {
  taskQuotaBodyForm.value = { ...row }
  dialogUpdateVisible.value = true
}

/**
 * 批量复制按钮是否允许
 */
const isCopyButtonEnable = () => {
  if (!isUpdateButton.value) {
    var startyear = parseInt(String(queryParams.startTime))
    var endyear = parseInt(String(queryParams.endTime))
    if (startyear === endyear) {
      return false
    }
  }
  return true
}
/**
 * 排序事件
 */
const changeSort = (val: any) => {
  console.log(val.order)
  console.log(val.prop)
  if (val.prop === 'deptname') {
    queryParams.sortType = 1
  } else if (val.prop === 'employname') {
    queryParams.sortType = 2
  } else if (val.prop === 'medianame') {
    queryParams.sortType = 3
  }
  if (val.order === 'descending') {
    queryParams.sortRule = 1
  }
  if (val.order === 'ascending') {
    queryParams.sortRule = 0
  }
  handleQuery()
}
/**
 * 检查框选择
 * @param row,index
 */
const handleSelectionChange = (val: any) => {
  multipleSelection = val
  if (multipleSelection.length > 0) {
    isUpdateButton.value = false
  } else {
    isUpdateButton.value = true
  }

  for (var i = 0; i < multipleSelection.length; i++) {
    console.log(multipleSelection[i].id)
  }
}
</script>

<style lang="scss" scoped></style>
