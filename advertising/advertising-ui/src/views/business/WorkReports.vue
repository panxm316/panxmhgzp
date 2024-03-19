<!--
 * @Author: suny
 * @Date: 2023-10-30 17:17:47
 * @LastEditTime: 2024-03-18 13:03:43
 * @LastEditors: suny
 * @Description: 工作报告管理
-->
<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex">
        <el-button type="primary" icon="Plus" @click="handleAdd">新增</el-button>
        <HgDateIndex :initdate="timedata" @onresponse="ontime"></HgDateIndex>
        <HgZtreeSelect
          v-if="user?.blead"
          style="width: 120px"
          :scopeflag="EHgDeptZtreeUrl.EMP_GETDEPTEMPLOY"
          :selectids="queryParams.employid ? [queryParams.employid] : []"
          @selectionztree="onSelectionEmp"
        ></HgZtreeSelect>
        <el-select
          v-model="queryParams.iworktype"
          placeholder="请选择报告类型"
          style="width: 100px; margin-right: 5px"
          clearable
          @change="handleQuery"
          @clear="handleQuery"
        >
          <!-- <el-option label="全部类型" value=""></el-option> -->
          <el-option
            v-for="t in workReportTypeList"
            :key="t.id"
            :label="t.name"
            :value="t.id"
          ></el-option>
        </el-select>
        <el-select
          v-model="queryParams.iapprovestatus"
          placeholder="请选择审核状态"
          clearable
          style="width: 100px; margin-right: 5px"
          @change="handleQuery"
          @clear="handleQuery"
        >
          <!-- <el-option label="全部状态" value=""></el-option> -->
          <el-option
            v-for="t in approveStatusList"
            :key="t.id"
            :label="t.name"
            :value="t.id"
          ></el-option>
        </el-select>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
      </div>
    </div>
    <div class="table_box">
      <el-table
        ref="dataTableRef"
        :data="workReportList"
        highlight-current-row
        :height="tableheight"
        :header-cell-style="tableHeaderColor"
        border
      >
        <el-table-column label="部门" prop="deptname" min-width="160" show-overflow-tooltip />
        <el-table-column label="人员名称" prop="employname" width="120" show-overflow-tooltip />
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
        <el-table-column
          label="客户名称"
          prop="customername"
          min-width="160"
          show-overflow-tooltip
        />
        <el-table-column label="工作内容" prop="sworkcontent" width="160" show-overflow-tooltip>
          <template #default="scope">
            <el-button type="primary" link size="small" @click="onDetail(scope.row)">
              {{ scope.row.sworkcontent }}</el-button
            >
          </template>
        </el-table-column>
        <el-table-column label="服务反馈" prop="sfeedback" width="120" show-overflow-tooltip />
        <el-table-column label="工作计划" prop="splan" width="160" show-overflow-tooltip />
        <el-table-column label="工作难点" prop="squestions" width="160" show-overflow-tooltip />
        <el-table-column label="创建时间" prop="dcreatedate" width="160" />
        <el-table-column label="审批状态" prop="iapprovestatus" width="100" align="center">
          <template #default="scope">
            {{ approveStatusList.find((t) => t.id === scope.row.iapprovestatus)?.name }}
          </template>
        </el-table-column>
        <el-table-column label="审批人名称" prop="scheckername" width="120" />
        <el-table-column label="审批日期" prop="dcheckdate" width="160" />
        <el-table-column label="审批意见" prop="scheckopinions" width="160" show-overflow-tooltip />
        <el-table-column
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
              @click="onEdit(scope.row, true)"
              >修改</el-button
            >
            <el-button
              v-if="
                (scope.row.iapprovestatus === EApproveStatus.待审 ||
                  scope.row.iapprovestatus === EApproveStatus.否决) &&
                user?.userid === scope.row.employid
              "
              link
              type="primary"
              icon="Top"
              size="small"
              title="提交审核"
              @click="onToCheck(scope.row)"
              >提交</el-button
            >
            <el-button
              v-if="scope.row.iapprovestatus === EApproveStatus.在审 && user?.blead"
              link
              type="success"
              icon="Check"
              size="small"
              title="审批"
              @click="onCheck(scope.row)"
              >审批</el-button
            >
            <el-button
              v-if="
                (scope.row.iapprovestatus === EApproveStatus.待审 ||
                  scope.row.iapprovestatus === EApproveStatus.否决) &&
                user?.userid === scope.row.employid
              "
              link
              type="danger"
              icon="Delete"
              size="small"
              title="删除"
              @click="onDelete(scope.row)"
              >删除</el-button
            >
            <!-- <el-button
              link
              style="margin-left: 10px"
              type="success"
              icon="View"
              size="small"
              title="详情"
              @click="onDetail(scope.row)"
              >详情</el-button
            > -->
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
    <!-- 编辑窗口 -->
    <el-dialog
      v-model="dialogEditVisible"
      title="工作报告"
      :width="bcheckflag ? 1100 : 800"
      append-to-body
      :destroy-on-close="true"
    >
      <div>
        <el-row>
          <el-col :span="bcheckflag ? 16 : 24">
            <el-form
              ref="workReportFormRef"
              :model="workReportForm"
              :rules="rules"
              label-width="140px"
              class="demo-workReportForm"
              :disabled="bDetailFlag"
              status-icon
            >
              <el-form-item label="报告类型">
                <el-radio-group
                  v-model="workReportForm.iworktype"
                  :disabled="
                    workReportForm.iapprovestatus !== EApproveStatus.待审 &&
                    workReportForm.iapprovestatus !== EApproveStatus.否决
                  "
                  @change="onChangeType"
                >
                  <el-radio
                    v-for="worktype in workReportTypeList"
                    :key="worktype.id"
                    :label="worktype.id"
                    >{{ worktype.name }}</el-radio
                  >
                </el-radio-group>
              </el-form-item>
              <el-form-item label="工作方式" prop="sworkmode">
                <el-select
                  v-model="workReportForm.sworkmode"
                  :disabled="
                    workReportForm.iapprovestatus !== EApproveStatus.待审 &&
                    workReportForm.iapprovestatus !== EApproveStatus.否决
                  "
                  filterable
                  allow-create
                  default-first-option
                  :reserve-keyword="false"
                  placeholder="请选择工作方式"
                  class="inputwindht"
                >
                  <el-option
                    v-for="item in workModeList"
                    :key="item.name"
                    :label="item.name"
                    :value="item.name"
                  />
                </el-select>
              </el-form-item>
              <el-form-item label="日期范围" required>
                <el-row :span="24">
                  <el-col :span="11">
                    <el-form-item prop="dstartdate">
                      <el-date-picker
                        v-model="workReportForm.dstartdate"
                        :disabled="
                          workReportForm.iapprovestatus !== EApproveStatus.待审 &&
                          workReportForm.iapprovestatus !== EApproveStatus.否决
                        "
                        type="date"
                        placeholder="开始日期"
                        :clearable="false"
                        value-format="YYYY-MM-DD"
                        style="width: 262px"
                      />
                    </el-form-item>
                  </el-col>
                  <el-col class="text-center" :span="2">至</el-col>
                  <el-col :span="11">
                    <el-form-item prop="denddate">
                      <el-date-picker
                        v-model="workReportForm.denddate"
                        :disabled="
                          workReportForm.iapprovestatus !== EApproveStatus.待审 &&
                          workReportForm.iapprovestatus !== EApproveStatus.否决
                        "
                        type="date"
                        placeholder="结束日期"
                        :clearable="false"
                        value-format="YYYY-MM-DD"
                        style="width: 262px"
                      />
                    </el-form-item>
                  </el-col>
                </el-row>
              </el-form-item>
              <el-form-item label="客户选择" prop="customerid">
                <el-row style="width: 100%">
                  <el-col :span="23">
                    <el-input
                      v-model="workReportForm.customername"
                      placeholder="请选择客户信息"
                      class="inputwindht"
                      :disabled="user?.userid !== workReportForm.employid"
                      @input="workReportForm.customerid = ''"
                    >
                      <template #append>
                        <el-button
                          icon="Search"
                          circle
                          title="选择客户"
                          @click="dialogCustomerVisible = true"
                        />
                      </template>
                      <template #suffix>
                        <el-button
                          v-show="workReportForm.customername"
                          link
                          icon="CircleClose"
                          @click="SeeCustomerdelete()"
                        ></el-button>
                      </template>
                    </el-input>
                  </el-col>
                </el-row>
              </el-form-item>
              <el-form-item label="工作内容" prop="sworkcontent">
                <el-input
                  v-model="workReportForm.sworkcontent"
                  :disabled="
                    workReportForm.iapprovestatus !== EApproveStatus.待审 &&
                    workReportForm.iapprovestatus !== EApproveStatus.否决
                  "
                  :rows="3"
                  type="textarea"
                  class="inputwindht"
                  resize="none"
                  placeholder="请输入..."
                />
              </el-form-item>
              <el-form-item label="服务反馈" prop="sfeedback">
                <el-input
                  v-model="workReportForm.sfeedback"
                  :disabled="
                    workReportForm.iapprovestatus !== EApproveStatus.待审 &&
                    workReportForm.iapprovestatus !== EApproveStatus.否决
                  "
                  :rows="3"
                  type="textarea"
                  resize="none"
                  class="inputwindht"
                  placeholder="请输入..."
                />
              </el-form-item>
              <el-form-item label="工作计划" prop="splan">
                <el-input
                  v-model="workReportForm.splan"
                  :disabled="
                    workReportForm.iapprovestatus !== EApproveStatus.待审 &&
                    workReportForm.iapprovestatus !== EApproveStatus.否决
                  "
                  :rows="3"
                  type="textarea"
                  resize="none"
                  class="inputwindht"
                  placeholder="请输入..."
                />
              </el-form-item>
              <el-form-item label="工作难点" prop="squestions">
                <el-input
                  v-model="workReportForm.squestions"
                  :disabled="
                    workReportForm.iapprovestatus !== EApproveStatus.待审 &&
                    workReportForm.iapprovestatus !== EApproveStatus.否决
                  "
                  :rows="3"
                  type="textarea"
                  resize="none"
                  class="inputwindht"
                  placeholder="请输入..."
                />
              </el-form-item>
              <el-form-item label="文件上传">
                <el-row style="width: 100%">
                  <el-col
                    v-if="
                      (workReportForm.iapprovestatus === EApproveStatus.待审 ||
                        workReportForm.iapprovestatus === EApproveStatus.否决) &&
                      !bDetailFlag
                    "
                    :span="24"
                  >
                    <HgFileUpload
                      :server="hgfileuploadparam.server"
                      :accept="hgfileuploadparam.accept"
                      :multiple="true"
                      :storytypes="hgfileuploadparam.storytypes"
                      @getupfile="getUpFile"
                    ></HgFileUpload>
                  </el-col>
                  <el-col :span="24">
                    <div v-for="(file, index) in workReportForm.filelist" :key="file.sfileid">
                      <div v-if="!file.bdelete">
                        <el-link type="primary" :underline="false" @click="previewFile(file)">
                          <el-icon><Document /></el-icon> {{ file.soriginalfile }}</el-link
                        >
                        <el-button
                          v-if="
                            (workReportForm.iapprovestatus === EApproveStatus.待审 ||
                              workReportForm.iapprovestatus === EApproveStatus.否决) &&
                            !bDetailFlag
                          "
                          link
                          type="danger"
                          icon="CircleClose"
                          @click="onUpfileDel(index)"
                        ></el-button>
                      </div>
                    </div>
                  </el-col>
                </el-row>
              </el-form-item>
              <el-form-item v-if="!bcheckflag && !bDetailFlag">
                <div style="position: absolute; right: 5px">
                  <el-button
                    type="primary"
                    icon="Check"
                    @click="saveWorkReportForm(workReportFormRef)"
                    >保存</el-button
                  >
                  <el-button icon="Close" @click="dialogEditVisible = false">取消</el-button>
                </div>
              </el-form-item>
              <el-form-item v-if="checkList.length > 0" label="审核意见">
                <el-table
                  ref="dataTableRef"
                  :data="checkList"
                  highlight-current-row
                  border
                  style="max-height: 400px; overflow-y: auto"
                >
                  <el-table-column
                    label="审核人"
                    prop="scheckername"
                    min-width="160"
                    show-overflow-tooltip
                  />
                  <el-table-column label="审批日期" prop="dcheckdate" width="100">
                    <template #default="scope">
                      <span>{{ dayjs(scope.row.dstartdate).format('YYYY-MM-DD') }}</span>
                    </template>
                  </el-table-column>
                  <el-table-column
                    label="审批状态"
                    prop="iapprovestatus"
                    width="100"
                    align="center"
                  >
                    <template #default="scope">
                      {{ approveStatusList.find((t) => t.id === scope.row.iapprovestatus)?.name }}
                    </template>
                  </el-table-column>
                  <el-table-column
                    label="审批意见"
                    prop="scheckopinions"
                    min-width="160"
                    show-overflow-tooltip
                  />
                </el-table>
              </el-form-item>
            </el-form>
            <!-- 选择客户 -->
            <el-dialog
              v-model="dialogCustomerVisible"
              title="选择客户"
              width="1000"
              align-center
              :close-on-click-modal="false"
              :destroy-on-close="true"
            >
              <el-row :gutter="50">
                <el-col :span="24">
                  <HgSalesCustomer @selectiondata="selectCustomer"></HgSalesCustomer>
                </el-col>
              </el-row>
            </el-dialog>
          </el-col>
          <el-col v-if="bcheckflag" :span="8">
            <div style="position: absolute; right: 5px; bottom: 60px; width: 35%">
              <div style="padding: 10px">
                <el-select
                  v-model="iapprovestatus"
                  placeholder="请选择审核状态"
                  style="width: 100%; margin-right: 5px"
                >
                  <el-option
                    :key="EApproveStatus.通过"
                    :label="EApproveStatus[EApproveStatus.通过]"
                    :value="EApproveStatus.通过"
                  ></el-option>
                  <el-option
                    :key="EApproveStatus.否决"
                    :label="EApproveStatus[EApproveStatus.否决]"
                    :value="EApproveStatus.否决"
                  ></el-option>
                </el-select>
              </div>
              <div style="padding: 10px">
                <el-input
                  v-model="workReportForm.scheckopinions"
                  placeholder="请填写审核意见"
                  :rows="6"
                  type="textarea"
                  resize="none"
                  style="width: 100%"
                />
              </div>
              <div style="position: absolute; right: 5px; padding: 10px">
                <el-button type="primary" icon="Check" @click="saveCheckInfo()">提交</el-button>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { onMounted, reactive, ref } from 'vue'
import { IAxios } from 'axios'
import { TDateTimeType, TDateType } from '@/types/components/hgdateindex'
import { THgFileUploadParam, TUpFile } from '@/types/components/hgfileupload'
import { computTableHeight, required, tableHeaderColor, formatDate } from '@/utils'
import {
  deleteWorkReportByIdApi,
  getWorkReportPageListApi,
  saveCheckInfoApi,
  saveWorkReportApi,
  updateCheckApi,
  updateWorkReportApi
} from '@/api/business/workreport'
import {
  EApproveStatus,
  EHgDeptZtreeUrl,
  EWorkMode,
  EWorkReportType
} from '@/types/common/enumindex'
import useUserStore from '@/store/modules/user'
import { getEnumCombo } from '@/api/common/index'
import { IDataCombo } from '@/types/common/DataCombo'
import { TSelectZtree } from '@/types/common'
import { FormInstance, FormRules, dayjs } from 'element-plus'
import { getMonthDates, getWeekDates, getYearDates } from '@/utils/getdate'
import { TAdCustomer } from '@/types/views/customer'
import {
  IWorkReportFilesDTO,
  IWorkReportsDTO,
  TWorkReportQuery
} from '@/types/views/business/workreports'

const global = getCurrentInstance()?.appContext.config.globalProperties
const user = useUserStore().user
const workReportFormRef = ref<FormInstance>()
const total = ref(0)
/** 表格高度 */
const tableheight = ref(0)
/** 选择客户显示状态 */
const dialogCustomerVisible = ref(false)
/** 编辑窗口显示状态 */
const dialogEditVisible = ref(false)
/** 是否审核 */
const bcheckflag = ref(false)
/** 审核状态：通过/否决 */
const iapprovestatus = ref(EApproveStatus.通过)
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
/** 审批状态下拉列表 */
const approveStatusList: IDataCombo[] = getEnumCombo(EApproveStatus)
/** 工作报告类型下拉列表 */
const workReportTypeList: IDataCombo[] = getEnumCombo(EWorkReportType)
/** 工作方式下拉列表 */
const workModeList: IDataCombo[] = getEnumCombo(EWorkMode)
/** 查询条件 */
const queryParams = reactive<TWorkReportQuery>({
  page: 1,
  pageSize: 20,
  startTime: '',
  endTime: '',
  employid: '',
  employname: '',
  iworktype: '',
  iapprovestatus: ''
})
const iworktype = ref(0)
const WorkReportInfo: IWorkReportsDTO = {
  id: '',
  iworktype: EWorkReportType.日报,
  deptid: user?.deptid?.toString(),
  deptname: user?.deptname,
  employid: user?.userid,
  employname: user?.username,
  dstartdate: dayjs().format('YYYY-MM-DD'),
  denddate: dayjs().format('YYYY-MM-DD'),
  sworkmode: '',
  sworkcontent: '',
  sfeedback: '',
  splan: '',
  squestions: '',
  customerid: '',
  customername: '',
  iapprovestatus: EApproveStatus.待审,
  filelist: []
}
/** 编辑实体 */
const workReportForm = ref<IWorkReportsDTO>({ ...WorkReportInfo })
/** 上传的文件列表 */
// const uploadFileList = ref<TUpFile[]>([])
/**
 * 校验设置参数
 */
const rules = reactive<FormRules<IWorkReportsDTO>>({
  sworkcontent: [
    { validator: required, required: true, message: '请填写工作内容', trigger: 'blur' }
  ],
  sworkmode: [
    { validator: required, required: true, message: '请选择工作方式', trigger: 'change' }
  ],
  dstartdate: [{ required: true, message: '开始日期不能为空', trigger: 'change' }],
  denddate: [{ required: true, message: '结束日期不能为空', trigger: 'change' }]
})
/** Form选中的行 */
const multipleSelection = ref<IWorkReportsDTO[]>([])
/** 上传组件参数 */
const hgfileuploadparam = ref<THgFileUploadParam>({
  server: import.meta.env.VITE_APP_BASE_API + '/common/UploadFile/importFile',
  multiple: true,
  filenumlimit: 20,
  storytypes: []
})
/** 是否查看详情 */
const bDetailFlag = ref(false)

/** 列表对象 */
const workReportList = ref<IWorkReportsDTO[]>([])
/** 审核列表 */
const checkList = ref<IWorkReportsDTO[]>([])
onMounted(() => {
  console.log(workModeList)
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
  getWorkReportPageListApi(queryParams)
    .then((res: IAxios<IWorkReportsDTO[]>) => {
      if (res.success) {
        // console.log(user?.userid)
        // console.log(res.obj[0].employid)
        // console.log(user?.userid === res.obj[0].employid)
        workReportList.value = res.obj
        total.value = res.total
      }
    })
    .finally(() => {})
}
/**
 * 新增
 */
const handleAdd = () => {
  resetForm(workReportFormRef.value)
  bcheckflag.value = false
  workReportForm.value = { ...WorkReportInfo }
  dialogEditVisible.value = true
  checkList.value = []
}
/**
 * 提交审核
 * @param row
 */
const onToCheck = (row: IWorkReportsDTO) => {
  ElMessageBox.confirm('确定提交审核吗？提交后将不能进行修改', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(() => {
      const ids = []
      ids.push(row.id)
      const data = {
        ids: ids.join(',')
      }
      updateCheckApi(data).then((res: IAxios) => {
        if (res.success) {
          ElMessage.success('操作成功')
          handleQuery()
        }
      })
    })
    .catch(() => {})
}
/**
 * 编辑按钮事件
 * @param row
 */
const onEdit = (row: IWorkReportsDTO, bedit: boolean) => {
  dialogEditVisible.value = true
  bDetailFlag.value = !bedit
  bcheckflag.value = false
  workReportForm.value = { ...row }
  checkList.value = []
  if (row.iapprovestatus !== EApproveStatus.待审 && row.iapprovestatus !== EApproveStatus.在审) {
    const checkinfo = {
      scheckername: row.scheckername,
      dcheckdate: row.dcheckdate,
      iapprovestatus: row.iapprovestatus,
      scheckopinions: row.scheckopinions
    } as IWorkReportsDTO
    checkList.value.push(checkinfo)
  }
}
/**
 * 查看详情
 * @param row
 */
const onDetail = (row: IWorkReportsDTO) => {
  bcheckflag.value = false
  onEdit(row, false)
}
/**
 * 删除按钮事件
 * @param row
 */
const onDelete = (row: IWorkReportsDTO | undefined) => {
  const ids: string[] = []
  if (row === undefined) {
    multipleSelection.value.forEach((item) => {
      if (item.iapprovestatus !== EApproveStatus.待审) {
        ElMessage.warning('只允许删除未提交审核的数据')
        return
      }
      ids.push(item.id?.toString() as string)
    })
  } else {
    if (row.iapprovestatus !== EApproveStatus.待审) {
      ElMessage.warning('只允许删除未提交审核的数据')
      return
    }
    ids.push(row.id?.toString() as string)
  }

  if (ids.length === 0) {
    ElMessage.warning('请选择要删除的数据')
    return
  }
  const data = {
    ids: ids.join(',')
  }
  ElMessageBox.confirm('是否删除选择的数据？', '提示', {
    type: 'warning'
  })
    .then(() => {
      deleteWorkReportByIdApi(data)
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
 * 审核按钮事件
 * @param row
 */
const onCheck = (row: IWorkReportsDTO) => {
  dialogEditVisible.value = true
  bcheckflag.value = true
  bDetailFlag.value = false
  workReportForm.value = { ...row }
  checkList.value = []
}
/**
 * 选择人员
 * @param val
 */
const onSelectionEmp = (val: TSelectZtree[]) => {
  const employid = val.map((item) => item.id).join(',')
  queryParams.employid = employid
  queryParams.employname = val.map((item) => item.name).join(',')
}
/**
 * 工作报告类型改变时时间调整
 */
const onChangeType = () => {
  let dateList: {
    start: Date
    end: Date
  }
  switch (workReportForm.value.iworktype) {
    case EWorkReportType.日报:
    case EWorkReportType.客户服务:
      workReportForm.value.dstartdate = dayjs().format('YYYY-MM-DD')
      workReportForm.value.denddate = dayjs().format('YYYY-MM-DD')
      break
    case EWorkReportType.周报:
      dateList = getWeekDates()
      workReportForm.value.dstartdate = dayjs(dateList.start).format('YYYY-MM-DD')
      workReportForm.value.denddate = dayjs(dateList.end).format('YYYY-MM-DD')
      break
    case EWorkReportType.月报:
      dateList = getMonthDates()
      workReportForm.value.dstartdate = dayjs(dateList.start).format('YYYY-MM-DD')
      workReportForm.value.denddate = dayjs(dateList.end).format('YYYY-MM-DD')
      break
    case EWorkReportType.年报:
      dateList = getYearDates()
      workReportForm.value.dstartdate = dayjs(dateList.start).format('YYYY-MM-DD')
      workReportForm.value.denddate = dayjs(dateList.end).format('YYYY-MM-DD')
      break
  }
}
/**
 * 获取上传后返回的文件列表
 * @param res
 */
const getUpFile = (res: IAxios<TUpFile>) => {
  console.log(res.success)
  if (res.success) {
    const index = workReportForm.value.filelist!.findIndex((file) => {
      return file.sfileid === res.obj.sfileid
    })
    if (index === undefined || index === null || index === -1) {
      // uploadFileList.value.push(res.obj)
      const workreport = res.obj as IWorkReportFilesDTO
      workreport.bdelete = false
      workReportForm.value.filelist?.push(workreport)
    } else {
      workReportForm.value.filelist![index].bdelete = false
    }
  }
}
/**
 * 删除上传的文件，如果是新上传文件则直接删除，已经存入数据库中的则修改删除标记，用户数据库文件删除
 * @param index
 */
const onUpfileDel = (index: number) => {
  console.log(index)
  if (workReportForm.value.filelist![index].id === undefined) {
    workReportForm.value.filelist!.splice(index, 1)
  } else {
    workReportForm.value.filelist![index].bdelete = true
  }
}
/**
 * 编辑保存
 * @param formEl
 */
const saveWorkReportForm = async (formEl: FormInstance | undefined) => {
  if (!formEl) return
  await formEl.validate((valid, fields) => {
    if (valid) {
      if (workReportForm.value.id === '') {
        saveWorkReportApi(workReportForm.value)
          .then((res: IAxios) => {
            if (res.success) {
              ElMessage.success('保存成功')
              handleQuery()
              workReportFormRef.value?.resetFields()
              dialogEditVisible.value = false
              workReportForm.value.filelist = []
            }
          })
          .catch(() => {})
      } else {
        updateWorkReportApi(workReportForm.value)
          .then((res: IAxios) => {
            if (res.success) {
              ElMessage.success('更新保存成功')
              handleQuery()
              workReportFormRef.value?.resetFields()
              dialogEditVisible.value = false
              workReportForm.value.filelist = []
            }
          })
          .catch(() => {})
      }
    } else {
      console.log('error submit!', fields)
    }
  })
}
/**
 * 重置form表单
 * @param formEl
 */
const resetForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.resetFields()
}
/**
 * 提交审核意见
 */
const saveCheckInfo = () => {
  if (workReportForm.value.scheckopinions === '') {
    ElMessage.warning('审核意见不允许为空')
    return
  }
  workReportForm.value.iapprovestatus = iapprovestatus.value
  saveCheckInfoApi(workReportForm.value)
    .then((res: IAxios) => {
      if (res.success) {
        ElMessage.success('提交成功')
        handleQuery()
        workReportFormRef.value?.resetFields()
        dialogEditVisible.value = false
      }
    })
    .catch(() => {})
}
/**
 * 选择客户信息后返回接收
 * @param val
 */
const selectCustomer = (val: TAdCustomer) => {
  workReportForm.value.customerid = val.id
  workReportForm.value.customername = val.sname
  dialogCustomerVisible.value = false
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
/** 修改时删除客户列表信息 */
const SeeCustomerdelete = () => {
  workReportForm.value.customername = ''
  workReportForm.value.customerid = ''
}
</script>

<style lang="scss" scoped>
.inputwindht {
  width: 540px;
}
</style>
