<!--
 * @Author: lhl
 * @Date: 2024-01-16
 * @Description: 报表配置
-->
<template>
  <div class="app-container">
    <div class="table_box">
      <!--
      工具条
     -->
      <div v-if="showtype === 0" class="search_box">
        <el-select
          v-model="reportform"
          placeholder="请选择支持的报表"
          style="width: 300px; margin-right: 5px"
          @change="handleReportForm"
        >
          <el-option
            v-for="item in baseTask.ReportForms"
            :key="item.id"
            :label="item.value"
            :value="item.id"
          ></el-option>
        </el-select>
        <el-button type="primary" @click="addReportSet">新增配置</el-button>
      </div>
      <!--
      汇总明细工具条
     -->
      <div v-if="showtype === 1" class="search_box">
        <el-select
          v-model="reportform"
          placeholder="请选择支持的报表"
          :disabled="true"
          style="width: 300px; margin-right: 5px"
          @change="handleReportForm"
        >
          <el-option
            v-for="item in baseTask.ReportForms"
            :key="item.id"
            :label="item.value"
            :value="item.id"
          ></el-option>
        </el-select>
        <el-button type="primary" @click="addSumItem">添加汇总项</el-button>
        <el-button type="primary" @click="backtoreport">返回</el-button>
      </div>

      <!--
      配置记录列表
     -->
      <div v-if="showtype === 0" class="table_box">
        <el-table
          :data="reportFormList"
          highlight-current-row
          :height="tableheight"
          :header-cell-style="tableHeaderColor"
          border
        >
          <el-table-column type="index" label="序号" width="60" align="center"></el-table-column>
          <el-table-column
            label="配置名称"
            prop="reportname"
            min-width="260"
            show-overflow-tooltip
          />
          <el-table-column label="汇总类型" prop="reporttype" min-width="120" align="center">
            <template #default="scope">
              <span v-if="scope.row.reporttype === '0'">部门汇总</span>
              <span v-if="scope.row.reporttype === '1'">业务汇总</span>
              <span v-if="scope.row.reporttype === '2'">部门/业务汇总</span>
              <span v-if="scope.row.reporttype === '3'">年度/业务汇总</span>
            </template>
          </el-table-column>
          <el-table-column
            label="配置人"
            prop="createempname"
            min-width="120"
            show-overflow-tooltip
          />
          <el-table-column label="状态" prop="buse" min-width="100" align="center">
            <template #default="scope">
              <span v-if="scope.row.buse === true">启用</span>
              <span v-if="scope.row.buse === false">禁用</span>
            </template>
          </el-table-column>
          <el-table-column label="说明" prop="sdescription" min-width="260" show-overflow-tooltip />
          <el-table-column label="配置日期" prop="dcreatetime" min-width="100" align="center">
            <template #default="scope">
              <span>{{ dayjs(scope.row.dcreatetime).format('YYYY-MM-DD') }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="操作"
            align="left"
            width="200"
            class-name="small-padding fixed-width"
            fixed="right"
          >
            <template #default="scope">
              <el-button
                v-if="scope.row.buse === false"
                link
                style="margin-left: 10px"
                type="success"
                icon="Edit"
                size="small"
                title="启用"
                @click="onEnable(scope.row)"
              >
                <span v-if="scope.row.buse === false">启用</span>
              </el-button>
              <el-button
                link
                style="margin-left: 10px"
                type="success"
                icon="Edit"
                size="small"
                title="编辑"
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
              <el-button
                link
                style="margin-left: 10px"
                type="success"
                icon="Edit"
                size="small"
                title="配置汇总项"
                @click="onSetSum(scope.row)"
                >配置汇总项</el-button
              >
            </template>
          </el-table-column>
        </el-table>
      </div>
      <!--
      汇总记录列表
     -->
      <div v-if="showtype === 1" class="table_box">
        <el-table
          :data="reportItemList"
          highlight-current-row
          :height="tableheight"
          :header-cell-style="tableHeaderColor"
          border
        >
          <el-table-column type="index" label="序号" width="60" align="center"></el-table-column>
          <el-table-column
            label="汇总项名称"
            prop="sheadername"
            min-width="160"
            show-overflow-tooltip
          />
          <el-table-column
            label="部门/业务"
            prop="scolumnname"
            min-width="400"
            show-overflow-tooltip
          />
          <el-table-column label="排列序号" prop="isort" min-width="80" show-overflow-tooltip />
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
                title="编辑"
                @click="onEditItem(scope.row)"
              >
                编辑
              </el-button>
              <el-button
                link
                style="margin-left: 10px"
                type="success"
                icon="Edit"
                size="small"
                title="删除"
                @click="onDelItem(scope.row)"
                >删除</el-button
              >
              <el-button
                v-if="scope.row.reporttype === '部门'"
                link
                style="margin-left: 10px"
                type="success"
                icon="Edit"
                size="small"
                title="选择部门"
                @click="addDepartment(scope.row)"
                >选择部门</el-button
              >
              <el-button
                v-if="scope.row.reporttype === '业务'"
                link
                style="margin-left: 10px"
                type="success"
                icon="Edit"
                size="small"
                title="选择业务"
                @click="addMedia(scope.row)"
                >选择业务</el-button
              >
            </template>
          </el-table-column>
        </el-table>
      </div>
      <!--
      配置记录编辑对话框
     -->
      <el-dialog
        v-model="dialoReportFormVisible"
        title="新增配置记录"
        :width="700"
        append-to-body
        :destroy-on-close="true"
      >
        <el-form
          ref="ReportFormGroupDTORef"
          :model="ReportFormGroupDTO"
          label-width="120px"
          :rules="rules"
          class="demo-workReportForm"
          status-icon
        >
          <el-form-item label="汇总类型" prop="reporttype">
            <el-select
              v-model="reporTypeV"
              placeholder="汇总类型"
              style="width: 160px; margin-right: 5px"
              @change="handleFormType"
            >
              <el-option
                v-for="item in baseTask.ReportFormType"
                :key="item.id"
                :label="item.value"
                :value="item.id"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="配置名称" prop="reportname">
            <el-input v-model="ReportFormGroupDTO.reportname" />
          </el-form-item>
          <el-form-item label="说明" prop="sdescription">
            <el-input
              v-model="ReportFormGroupDTO.sdescription"
              :rows="3"
              type="textarea"
              class="inputwindht"
              resize="none"
              placeholder="请输入..."
            />
          </el-form-item>
          <el-form-item>
            <div style="text-align: right; margin-top: 60px; width: 100%">
              <el-button
                type="primary"
                icon="Check"
                @click="saveReportFormGroup(ReportFormGroupDTORef)"
                >保存</el-button
              >
              <el-button icon="Close" @click="dialoReportFormVisible = false">取消</el-button>
            </div>
          </el-form-item>
        </el-form>
      </el-dialog>
      <!--
      配置部门或区域
     -->
      <el-dialog
        v-model="dialogSetDepartmentVisible"
        title="选择部门"
        :width="700"
        append-to-body
        :destroy-on-close="true"
      >
        <HgZtree
          style="width: 120px"
          :scopeflag="EHgDeptZtreeUrl.DEPT_GETDEPTTREE"
          :treeheight="300"
          :expandall="true"
          :checkboxtype="true"
          @selectionztree="onSelectionDepart"
        >
        </HgZtree>
        <div style="text-align: right; margin-top: 60px; width: 100%">
          <el-button type="primary" icon="Check" @click="addDepartmentSelect()">确定</el-button>
          <el-button icon="Close" @click="dialogSetDepartmentVisible = false">取消</el-button>
        </div>
      </el-dialog>
      <!--
      添加汇总项
     -->
      <el-dialog
        v-model="dialogAddSumItemVisible"
        title="添加汇总项"
        :width="500"
        append-to-body
        :destroy-on-close="true"
      >
        <el-form
          ref="ReportmodelItemDTORef"
          :model="ReportmodelItemDTO"
          label-width="120px"
          :rules="sumrules"
          class="demo-workReportForm"
          status-icon
        >
          <el-form-item label="汇总类型" prop="reporttype">
            <el-select
              v-model="ReportmodelItemDTO.reporttype"
              placeholder="汇总类型"
              style="width: 160px; margin-right: 5px"
              @change="handleItemForm"
            >
              <el-option
                v-for="item in baseTask.ReportObjectType"
                :key="item.value"
                :label="item.value"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item
            v-if="ReportmodelItemDTO.reporttype != '公司'"
            label="汇总项名称"
            prop="sheadername"
          >
            <el-input v-model="ReportmodelItemDTO.sheadername" />
          </el-form-item>
          <el-form-item
            v-if="ReportmodelItemDTO.reporttype === '公司'"
            label="公司名称"
            prop="reporttype"
          >
            <el-select
              v-model="reporCompany"
              placeholder="请选择公司"
              style="width: 160px; margin-right: 5px"
              @change="handleFormCompany"
            >
              <el-option
                v-for="item in companyList"
                :key="item.id"
                :label="item.sname"
                :value="item.sname"
              ></el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="排列序号" prop="isort">
            <el-input-number
              v-model="ReportmodelItemDTO.isort"
              controls-position="right"
              :min="1"
              :max="100"
            ></el-input-number>
          </el-form-item>

          <el-form-item>
            <div style="text-align: right; margin-top: 60px; width: 100%">
              <el-button type="primary" icon="Check" @click="saveReportItem(ReportmodelItemDTORef)"
                >保存</el-button
              >
              <el-button icon="Close" @click="dialogAddSumItemVisible = false">取消</el-button>
            </div>
          </el-form-item>
        </el-form>
      </el-dialog>
      <!--
      选择业务
     -->
      <el-dialog
        v-model="dialogSetMediaVisible"
        title="选择业务"
        :width="500"
        append-to-body
        :destroy-on-close="true"
      >
        <el-select
          v-model="mediaType"
          placeholder="请选择业务类型"
          style="width: 100%; margin-right: 5px"
          @change="handlMedia"
        >
          <el-option
            v-for="item in baseTask.MediaType"
            :key="item.id"
            :label="item.value"
            :value="item.id"
          ></el-option>
        </el-select>
        <div style="margin-top: 30px; width: 100%">
          <HgZtree
            v-if="mediaType === '0'"
            :scopeflag="EHgDeptZtreeUrl.MEDIA_GETSYSMEDIATREE"
            :treeheight="350"
            :expandall="true"
            :checkboxtype="true"
            @selectionztree="onSelectionDepart"
          >
          </HgZtree>
          <HgZtree
            v-if="mediaType === '1'"
            :scopeflag="EHgDeptZtreeUrl.MEDIA_GETSYSMEDIAFLOADTREE"
            :treeheight="350"
            :expandall="true"
            :checkboxtype="true"
            @selectionztree="onSelectionDepart"
          >
          </HgZtree>
          <HgZtree
            v-if="mediaType === '2'"
            :scopeflag="EHgDeptZtreeUrl.MEDIA_GETSYSMEDIAFLOADAREAVERTREE"
            :treeheight="350"
            :expandall="true"
            :checkboxtype="true"
            @selectionztree="onSelectionDepart"
          >
          </HgZtree>
        </div>
        <div style="text-align: right; margin-top: 60px; width: 100%">
          <el-button type="primary" icon="Check" @click="addMediaSelect()">保存</el-button>
          <el-button icon="Close" @click="dialogSetMediaVisible = false">取消</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>
<script setup lang="ts">
import { IReportSumDTO } from '@/types/views/statistic/reportforms'
import baseTask from '@/types/views/statistic/reportforms'
import { IReportFormGroupDTO, IReportmodelItemDTO } from '@/types/views/statistic/reportforms'
import { FormInstance, FormRules, dayjs } from 'element-plus'
import {
  saveReportFormGroupApi,
  getReportFormListApi,
  updateReportFormGroupApi,
  deleteReportFormGroupApi,
  enableReportFormApi,
  saveReportmodelItemApi,
  updateReportmodelItemApi,
  getReportSumItemListApi,
  deleteReportmodelItemApi,
  addSumObjectApi
} from '@/api/statistic/reportformset'
import { getBusinessentityComboApi } from '@/api/finance/businessentity'
import { IAxios } from 'axios'
import { computTableHeight, required, tableHeaderColor, formatMoney } from '@/utils'
import useUserStore from '@/store/modules/user'
import { EHgDeptZtreeUrl } from '@/types/common/enumindex'
import { TSelectZtree } from '@/types/common'
const userStore = useUserStore()
/** 配置记录对象 */
const ReportFormGroupDTO = reactive<IReportFormGroupDTO>({
  id: '',
  buse: false,
  reportname: '',
  applyto: '',
  sdescription: '',
  reporttype: '0',
  createempid: userStore.user?.userid.toString(),
  createempname: userStore.user?.username.toString(),
  dcreatetime: new Date()
})
/** 汇总项记录对象 */
const ReportmodelItemDTO = reactive<IReportmodelItemDTO>({
  id: '',
  reportmodelgroupid: '',
  sheadername: '',
  reporttype: '部门',
  idepttype: 0,
  scolumnname: '',
  ilevel: 0,
  snames: '',
  sids: '',
  isort: 0
})
/** 汇总业务对象 */
const ReportSumDTO = reactive<IReportSumDTO>({
  reportGroupItemId: '',
  reporttype: '0',
  idepttype: 0,
  sids: '',
  snames: ''
})
/** 显示内容类型 */
const showtype = ref(0)
/** 表格高度 */
const tableheight = ref(0)
const ReportFormGroupDTORef = ref<FormInstance>()
const ReportmodelItemDTORef = ref<FormInstance>()
/** 报表选择项 */
const reportform = ref('0')
/** 报表类型选择项 */
const reporTypeV = ref('0')
/** 公司选择项 */
const reporCompany = ref('')
/** 列表对象 */
const reportFormList = ref<IReportFormGroupDTO[]>([])
/** 汇总明细列表对象 */
const reportItemList = ref<IReportmodelItemDTO[]>([])
/** 部门ID */
const departmentIDList = ref('')
/** 公司列表 */
const companyList = ref<any>([])

/**
 * 校验设置参数
 */
const rules = reactive<FormRules<IReportFormGroupDTO>>({
  reportname: [{ required: true, message: '请输入配置名称', trigger: 'blur' }],
  reporttype: [{ required: true, message: '汇总类型不能为空', trigger: 'change' }]
})
/**
 * 汇总项校验设置参数
 */
const sumrules = reactive<FormRules<IReportmodelItemDTO>>({
  sheadername: [{ required: true, message: '请输入汇总项名称', trigger: 'blur' }]
})

onMounted(() => {
  getReportFormList()
  /**
   * 计算表格高度
   */
  tableheight.value = computTableHeight()
  getCompany()
})
/** 配置记录编辑窗口 */
const dialoReportFormVisible = ref(false)
/** 配置部门编辑窗口 */
const dialogSetDepartmentVisible = ref(false)
/** 添加汇总项窗口 */
const dialogAddSumItemVisible = ref(false)
/** 业务选择窗口 */
const dialogSetMediaVisible = ref(false)
/** 添加汇总项窗口 */
const reporGroupId = ref('')
/** 汇总类型（dept/media） */
const reporTypeData = ref('')
/** 媒体类型（媒体/叠次/版本） */
const mediaType = ref('0')
/** 启用请求对象 */
const enableVO = ref({
  reportId: '',
  reportFormId: ''
})
/**
 * 新增配置
 */
const addReportSet = () => {
  ReportFormGroupDTO.id = ''
  ReportFormGroupDTO.reportname = ''
  ReportFormGroupDTO.sdescription = ''
  ReportFormGroupDTO.reporttype = '0'
  dialoReportFormVisible.value = true
}
/**
 * 保存配置记录
 */
const saveReportFormGroup = async (formEl: FormInstance | undefined) => {
  if (!formEl) return
  await formEl.validate((valid, fields) => {
    if (valid) {
      if (ReportFormGroupDTO.id === '') {
        ReportFormGroupDTO.applyto = reportform.value
        saveReportFormGroupApi(ReportFormGroupDTO)
          .then((res: IAxios) => {
            if (res.success) {
              getReportFormList()
              ElMessage.success('保存成功')
              dialoReportFormVisible.value = false
            }
          })
          .catch(() => {})
      } else {
        updateReportFormGroupApi(ReportFormGroupDTO)
          .then((res: IAxios) => {
            if (res.success) {
              ElMessage.success('更新保存成功')
              getReportFormList()
              dialoReportFormVisible.value = false
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
 * 编辑汇总项
 */
const saveReportItem = async (formEl: FormInstance | undefined) => {
  if (!formEl) return
  await formEl.validate((valid, fields) => {
    if (valid) {
      if (ReportmodelItemDTO.id === '') {
        ReportmodelItemDTO.idepttype = 0
        ReportmodelItemDTO.reportmodelgroupid = reporGroupId.value
        saveReportmodelItemApi(ReportmodelItemDTO)
          .then((res: IAxios) => {
            if (res.success) {
              ElMessage.success('保存成功')
              querySumItemList(reporTypeData.value)
              dialogAddSumItemVisible.value = false
            }
          })
          .catch(() => {})
      } else {
        updateReportmodelItemApi(ReportmodelItemDTO)
          .then((res: IAxios) => {
            if (res.success) {
              ElMessage.success('更新保存成功')
              querySumItemList(reporTypeData.value)
              dialogAddSumItemVisible.value = false
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
 * 汇总类型选择
 */
const handleFormType = () => {
  ReportFormGroupDTO.reporttype = reporTypeV.value
}

/**
 * 汇总类型选择
 */
const handleFormCompany = (event: any) => {
  ReportmodelItemDTO.sheadername = reporCompany.value
  var list = companyList.value
  for (var i = 0; i < list.length; i++) {
    if (list[i].sname === event) {
      ReportmodelItemDTO.sids = list[i].id
      return
    }
  }
}
const handleItemForm = () => {
  ReportmodelItemDTO.sheadername = ''
}
/**
 * 查询配置记录
 */
const getReportFormList = () => {
  getReportFormListApi({ reportFormId: reportform.value }).then((res: any) => {
    if (res.success) {
      reportFormList.value = res.obj
    }
  })
}
/**
 * 报表选择处理
 */
const handleReportForm = () => {
  getReportFormList()
}
/**
 * 编辑按钮事件
 * @param row
 */
const onEdit = (row: IReportFormGroupDTO) => {
  ReportFormGroupDTO.buse = row.buse
  ReportFormGroupDTO.applyto = row.applyto
  ReportFormGroupDTO.createempid = row.createempid
  ReportFormGroupDTO.createempname = row.createempname
  ReportFormGroupDTO.id = row.id
  ReportFormGroupDTO.reportname = row.reportname
  ReportFormGroupDTO.sdescription = row.sdescription
  ReportFormGroupDTO.reporttype = row.reporttype
  dialoReportFormVisible.value = true
}
/**
 * 删除按钮事件
 * @param row
 */
const onDelete = (row: IReportFormGroupDTO) => {
  ElMessageBox.confirm('是否删除选择的配置记录？', '提示', {
    type: 'warning'
  })
    .then(() => {
      deleteReportFormGroupApi(row)
        .then((res: IAxios) => {
          if (res.success) {
            ElMessage.success('删除成功!')
            getReportFormList()
          }
        })
        .catch(() => {})
    })
    .catch(() => {})
}

/**
 * 启用按钮事件
 * @param row
 */
const onEnable = (row: IReportFormGroupDTO) => {
  enableVO.value.reportId = row.id
  enableVO.value.reportFormId = row.applyto
  ElMessageBox.confirm('是否启用该配置记录？', '提示', {
    type: 'warning'
  })
    .then(() => {
      enableReportFormApi(enableVO.value)
        .then((res: IAxios) => {
          if (res.success) {
            ElMessage.success('启用成功!')
            getReportFormList()
          }
        })
        .catch(() => {})
    })
    .catch(() => {})
}
/**
 * 配置汇总项事件
 * @param row
 */
const onSetSum = (row: IReportFormGroupDTO) => {
  reporGroupId.value = row.id.toString()
  reporTypeData.value = ''
  querySumItemList(reporTypeData.value)
  showtype.value = 1
}
/**
 * 配置业务事件
 * @param row
 */
const onSetMedia = (row: IReportFormGroupDTO) => {
  reporGroupId.value = row.id.toString()
  reporTypeData.value = ''
  querySumItemList(reporTypeData.value)
  showtype.value = 1
}
/**
 * 返回事件
 * @param row
 */
const backtoreport = () => {
  showtype.value = 0
}
/**
 * 选择部门
 * @param val
 */
const onSelectionDepart = (val: TSelectZtree[]) => {
  const departid = val.map((item) => item.id).join(',')
  departmentIDList.value = departid
}
/**
 * 添加汇总事件
 * @param
 */
const addSumItem = () => {
  ReportmodelItemDTO.id = ''
  ReportmodelItemDTO.sheadername = ''
  ReportmodelItemDTO.isort = reportItemList.value.length + 1
  dialogAddSumItemVisible.value = true
}

/**
 * 查询汇总项记录
 */
const querySumItemList = (retype: string) => {
  console.log(reporGroupId.value)
  getReportSumItemListApi({ reportGroupId: reporGroupId.value, reportType: retype }).then(
    (res: any) => {
      if (res.success) {
        reportItemList.value = res.obj
      }
    }
  )
}
/**
 * 编辑汇总项按钮事件
 * @param row
 */
const onEditItem = (row: IReportmodelItemDTO) => {
  ReportmodelItemDTO.sheadername = row.sheadername
  ReportmodelItemDTO.isort = row.isort
  ReportmodelItemDTO.id = row.id
  ReportmodelItemDTO.reporttype = row.reporttype
  ReportmodelItemDTO.idepttype = row.idepttype
  ReportmodelItemDTO.reportmodelgroupid = row.reportmodelgroupid
  ReportmodelItemDTO.sids = row.sids
  ReportmodelItemDTO.scolumnname = row.scolumnname
  ReportmodelItemDTO.ilevel = row.ilevel
  ReportmodelItemDTO.snames = row.snames
  dialogAddSumItemVisible.value = true
}
/**
 * 删除汇总项按钮事件
 * @param row
 */
const onDelItem = (row: IReportmodelItemDTO) => {
  ElMessageBox.confirm('是否删除该汇总项？', '提示', {
    type: 'warning'
  })
    .then(() => {
      deleteReportmodelItemApi(row)
        .then((res: IAxios) => {
          if (res.success) {
            ElMessage.success('删除成功!')
            querySumItemList(reporTypeData.value)
          }
        })
        .catch(() => {})
    })
    .catch(() => {})
}
/**
 * 添加部门事件
 * @param
 */
const addDepartment = (row: IReportFormGroupDTO) => {
  ReportSumDTO.reportGroupItemId = row.id
  ReportSumDTO.idepttype = 0
  departmentIDList.value = ''
  dialogSetDepartmentVisible.value = true
}
/**
 * 添加业务事件
 * @param
 */
const addMedia = (row: IReportFormGroupDTO) => {
  ReportSumDTO.reportGroupItemId = row.id
  departmentIDList.value = ''
  dialogSetMediaVisible.value = true
}
/**
 * 添加部门
 * @param
 */
const addDepartmentSelect = () => {
  if (departmentIDList.value === '') {
    ElMessage.info('请选择部门')
    return
  }
  ReportSumDTO.sids = departmentIDList.value
  addSumObjectApi(ReportSumDTO).then((res: any) => {
    if (res.success) {
      querySumItemList(reporTypeData.value)
      dialogSetDepartmentVisible.value = false
    }
  })
}
/**
 * 选择业务
 * @param
 */
const addMediaSelect = () => {
  if (departmentIDList.value === '') {
    ElMessage.info('请选择业务')
    return
  }
  ReportSumDTO.sids = departmentIDList.value
  ReportSumDTO.snames = mediaType.value
  addSumObjectApi(ReportSumDTO).then((res: any) => {
    if (res.success) {
      querySumItemList(reporTypeData.value)
      dialogSetMediaVisible.value = false
    }
  })
}
/**
 * 业务类型选择处理
 */
const handlMedia = () => {
  getReportFormList()
}
/**
 * 选择公司
 * @param
 */
const getCompany = () => {
  getBusinessentityComboApi().then((res: any) => {
    if (res.success) {
      companyList.value = res.obj
    }
  })
}
</script>
