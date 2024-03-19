<!--
 * @Author: suny
 * @Date: 2023-12-19 11:01:31
 * @LastEditTime: 2024-03-13 18:52:04
 * @LastEditors: suny
 * @Description: 广告资源
-->
<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex">
        <el-button type="primary" icon="Plus" @click="handleAdd">新增</el-button>
        <HgDateIndex :initdate="timedata" @onresponse="ontime"></HgDateIndex>
        <el-select
          v-model="queryParams.iapprovestatus"
          placeholder="请选择审核状态"
          clearable
          style="width: 100px; margin-right: 5px"
          @change="handleQuery"
          @clear="handleQuery"
        >
          <el-option
            v-for="t in approveStatusList"
            :key="t.id"
            :label="t.name"
            :value="t.id"
          ></el-option>
        </el-select>
        <HgZtreeSelect
          v-if="user?.blead"
          :scopeflag="EHgDeptZtreeUrl.EMP_GETDEPTEMPLOYPERSON"
          :selectids="queryParams.employid ? [queryParams.employid] : []"
          style="width: 100px"
          :filterable="true"
          :treeparams="{ bIgnorePermissions: true }"
          @selectionztree="onSelectionEmp"
        ></HgZtreeSelect>
        <el-input
          v-model="queryParams.queryKey"
          clearable
          placeholder="请输入客户或者标题关键字"
          class="input-with-select"
          style="width: 200px"
          @keyup.enter="handleQuery"
          @clear="handleQuery"
        >
        </el-input>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
      </div>
    </div>
    <div class="table_box">
      <el-table
        ref="dataTableRef"
        :data="adResourcApplicationList"
        highlight-current-row
        :height="tableheight"
        :header-cell-style="tableHeaderColor"
        border
      >
        <el-table-column label="部门" prop="deptname" min-width="100" show-overflow-tooltip />
        <el-table-column label="人员名称" prop="employname" width="120" show-overflow-tooltip />
        <el-table-column label="客户类型" prop="icusttype" width="100" align="center">
          <template #default="scope">
            <span v-if="scope.row.icusttype === ECustomerType.直接客户">直接客户</span>
            <span v-if="scope.row.icusttype === ECustomerType.代理公司">代理公司</span>
            <span v-if="scope.row.icusttype === ECustomerType.内容生产方">内容生产方</span>
          </template>
        </el-table-column>
        <el-table-column
          label="客户名称"
          prop="customername"
          min-width="160"
          show-overflow-tooltip
        />
        <el-table-column label="申请时间" prop="dapplytime" width="160" />
        <el-table-column label="广告标题" prop="sadtitle" width="160" show-overflow-tooltip />
        <el-table-column label="一审意见" prop="sfirstopinion" width="160" show-overflow-tooltip />
        <el-table-column label="开始日期" prop="dstartdate" width="100">
          <template #default="scope">
            <span>{{ dayjs(scope.row.dstartdate).format('YYYY-MM-DD') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="结束日期" prop="denddate" width="100">
          <template #default="scope">
            <span>{{
              scope.row.denddate !== null ? dayjs(scope.row.denddate).format('YYYY-MM-DD') : ''
            }}</span>
          </template>
        </el-table-column>
        <el-table-column label="审批状态" prop="iapprovestatus" width="100" align="center">
          <template #default="scope">
            <span v-if="scope.row.iapprovestatus === EApproveStatus.待审" style="color: #e6a23c"
              >待审</span
            >
            <span v-if="scope.row.iapprovestatus === EApproveStatus.在审" style="color: #409eff"
              >在审</span
            >
            <span v-if="scope.row.iapprovestatus === EApproveStatus.通过" style="color: #67c23a"
              >通过</span
            >
            <span v-if="scope.row.iapprovestatus === EApproveStatus.否决" style="color: #f56c6c"
              >否决</span
            >
          </template>
        </el-table-column>
        <el-table-column label="广告内容" prop="sadcontent" width="160" show-overflow-tooltip />
        <el-table-column label="快速上版" prop="bquickly" width="160" align="center">
          <template #default="scope">
            <el-checkbox v-model="scope.row.bquickly" disabled></el-checkbox>
          </template>
        </el-table-column>
        <el-table-column
          label="审批意见"
          prop="sapprovalopinions"
          width="220"
          show-overflow-tooltip
        />
        <el-table-column
          label="操作"
          align="left"
          width="240"
          class-name="small-padding fixed-width"
          fixed="right"
        >
          <template #default="scope">
            <el-button
              v-if="
                user?.userid === scope.row.employid &&
                scope.row.iapprovestatus !== EApproveStatus.在审 &&
                scope.row.iapprovestatus !== EApproveStatus.通过
              "
              link
              type="success"
              icon="Edit"
              size="small"
              title="修改"
              @click="onEdit(scope.row)"
              >修改
            </el-button>
            <el-button
              v-if="
                scope.row.iapprovestatus !== EApproveStatus.在审 &&
                scope.row.iapprovestatus !== EApproveStatus.通过
              "
              link
              type="primary"
              icon="Top"
              size="small"
              title="提交审核"
              @click="onCheckFlow(scope.row)"
              >提交
            </el-button>
            <el-button
              v-if="!scope.row.bquickly && scope.row.iapprovestatus === EApproveStatus.通过"
              link
              type="primary"
              icon="Top"
              size="small"
              title="广告预定"
              @click="onCheckTworder(scope.row)"
              >预定
            </el-button>
            <el-button
              v-if="
                scope.row.iapprovestatus !== EApproveStatus.在审 &&
                scope.row.iapprovestatus !== EApproveStatus.通过
              "
              link
              type="danger"
              icon="Delete"
              size="small"
              title="删除"
              @click="onDelete(scope.row)"
              >删除
            </el-button>
            <el-button
              link
              type="primary"
              icon="view"
              size="small"
              title="查看详情"
              @click="onDetail(scope.row)"
              >详情
            </el-button>
            <!-- <el-button
              v-if="scope.row.iapprovestatus !== EApproveStatus.待审"
              link
              type="text"
              icon="RefreshRight"
              size="small"
              title="历史详情"
              @click="onHistory(scope.row)"
              >历史
            </el-button> -->
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
    <!-- 提交审核 -->
    <el-dialog
      v-model="dialogToCheckVisible"
      title="提交审批"
      width="800"
      align-center
      :close-on-click-modal="false"
      :destroy-on-close="true"
    >
      <el-row :gutter="50">
        <el-col :span="24">
          <el-form ref="flowRef" label-width="140px" class="demo-workReportForm" status-icon>
            <el-form-item label="选择审核流程" prop="flowid">
              <el-select
                v-model="adResourceApplicationForm.flowid"
                placeholder="请选择审核流程"
                clearable
                style="width: 100%"
              >
                <el-option
                  v-for="item in flowTypeList"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
            <div style="width: 100%; text-align: right">
              <el-button type="primary" icon="Check" @click="onToCheck">提交</el-button>
              <el-button icon="Close" @click="dialogToCheckVisible = false">取消</el-button>
            </div>
          </el-form>
        </el-col>
      </el-row>
    </el-dialog>
    <!-- 审核历史详情 -->
    <el-dialog
      v-model="dialogHistoryVisible"
      title="广告资源审核历史详情"
      width="800"
      align-center
      :close-on-click-modal="false"
      :destroy-on-close="true"
    >
      <flow-node-format
        ref="flowFormatRef"
        :disable-select="true"
        :form-data="currentData.formData"
        :process-instance-id="currentData.processInstanceId"
        :flow-id="currentData.flowId"
        :customer-process-instance="historyList"
      ></flow-node-format>
      <div v-if="historyList.length >= 2" style="width: 100%; text-align: left; padding-left: 50px">
        <li v-for="item in historyList" :key="item.flowId">
          <el-link v-if="item.result === false" type="danger" @click="Seelistdetail(item)"
            >{{ item.createTime }}提交审核
          </el-link>
          <el-link v-if="item.result === true" type="success" @click="Seelistdetail(item)"
            >{{ item.createTime }}提交审核
          </el-link>
          <el-link v-if="item.result === null" type="primary" @click="Seelistdetail(item)"
            >{{ item.createTime }}提交审核
          </el-link>
        </li>
      </div>
    </el-dialog>
    <!-- 详情 -->
    <el-dialog
      v-model="dialogDetailVisible"
      title="广告资源申请表详情"
      :width="adResourceApplicationForm.iapprovestatus !== EApproveStatus.待审 ? '1200' : '1000'"
      align-center
      :close-on-click-modal="false"
      :destroy-on-close="true"
    >
      <HgAdSourceDetail
        :id="adResourceApplicationForm.id"
        :showflag="adResourceApplicationForm.iapprovestatus !== EApproveStatus.待审 ? true : false"
      ></HgAdSourceDetail>
    </el-dialog>
    <!--  新增或者修改的dialog  -->
    <ad-resource-application-dialog ref="dialogRef" @onCloseDialog="() => handleQuery()" />
    <!-- 订单预定 -->
    <el-dialog
      v-model="dialogVisible"
      title="编辑订单"
      align-center
      :close-on-click-modal="false"
      :width="fenbianlv() > 1440 ? '80%' : '94%'"
      @close="handleCancel"
    >
      <!-- /* 编辑 */ -->
      <AdTworder
        style="width: 100%"
        :data="''"
        :i-ad-resource-application="IAdResourceApplicationDTOData"
        :redetdata="true"
        :bottomshow="true"
        :isbu="0"
        @closedialogVisible="closedialogVisible"
      ></AdTworder>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import AdResourceApplicationDialog from './AdResourceApplicationDialog.vue'
import HgAdSourceDetail from '@/components/HgAdSourceDetail/index.vue'
import { onMounted, reactive, ref } from 'vue'
import { IAxios } from 'axios'
import { TDateTimeType, TDateType } from '@/types/components/hgdateindex'
import { computTableHeight, tableHeaderColor, fenbianlv } from '@/utils'
import {
  deleteAdResourceApplicationByIdApi,
  getAdResourceApplicationPageListApi,
  getProcessInstanceByIdApi,
  updateCheckApi
} from '@/api/ad/adresourceapplication'
import { EApproveStatus, ECustomerType, EFlowType, EHgDeptZtreeUrl } from '@/types/common/enumindex'
import useUserStore from '@/store/modules/user'
import { getEnumCombo } from '@/api/common/index'
import { IDataCombo } from '@/types/common/DataCombo'
import { TSelectZtree } from '@/types/common'
import { dayjs, FormInstance } from 'element-plus'
import {
  IAdResourceApplicationDTO,
  ResourceApplicationDialogMode,
  TAdResourceApplicationQuery
} from '@/types/views/ad/adresourceapplication'
import { Tadindustrylist, TypeCurrentData } from '@/types/components/hgindustry'
import { getFlowlistComboByFlowTypeApi } from '@/api/flow'
import FlowNodeFormat from '@/components/Flow/FlowNodeFormatData.vue'
import { useRouter } from 'vue-router'
import AdTworder from './AdTworder.vue'

const router = useRouter()
const user = useUserStore().user
const total = ref(0)
/** 表格高度 */
const tableheight = ref(0)
/** 预定窗口 */
const dialogVisible = ref(false)
/** 预定时候传值 */
const IAdResourceApplicationDTOData = ref<IAdResourceApplicationDTO>()
/** 提交审核窗口显示状态 */
const dialogToCheckVisible = ref(false)
/** 查看详情窗口显示状态 */
const dialogDetailVisible = ref(false)
/** 历史记录窗口显示状态 */
const dialogHistoryVisible = ref(false)
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
const adResourceApplicationInfo: IAdResourceApplicationDTO = {
  id: '',
  icusttype: ECustomerType.直接客户,
  deptid: user!.deptid!.toString(),
  deptname: user!.deptname!,
  employid: user!.userid,
  employname: user!.username,
  dstartdate: dayjs().format('YYYY-MM-DD'),
  denddate: '',
  sadtitle: '',
  sadcontent: '',
  sremark: '',
  customerid: '',
  customername: '',
  bquickly: false,
  iapprovestatus: EApproveStatus.待审,
  filelist: []
}
/** 编辑实体 */
const adResourceApplicationForm = ref<IAdResourceApplicationDTO>({ ...adResourceApplicationInfo })
/** 审批状态下拉列表 */
const approveStatusList: IDataCombo[] = getEnumCombo(EApproveStatus)
/** 查询条件 */
const queryParams = reactive<TAdResourceApplicationQuery>({
  sort: 'dapplytime',
  order: 'desc',
  page: 1,
  pageSize: 20,
  startTime: '',
  endTime: '',
  employid: '',
  icusttype: '',
  iapprovestatus: ''
})

/** Form选中的行 */
const multipleSelection = ref<IAdResourceApplicationDTO[]>([])

/** 列表对象 */
const adResourcApplicationList = ref<IAdResourceApplicationDTO[]>([])
/** 审核列表 */
const checkList = ref<IAdResourceApplicationDTO[]>([])
/** 流程类型列表 */
const flowTypeList = ref<Tadindustrylist[]>([])
/** 当前数据历史列表 */
const historyList = ref<TypeCurrentData[]>([])
/** 当前历史详情 */
const currentData = ref<TypeCurrentData>({
  flowId: '',
  processInstanceId: '',
  formData: {}
})
const dialogRef = ref()

onMounted(() => {
  handleQuery()
  getFlowlistCombo()
  /**
   * 计算表格高度
   */
  tableheight.value = computTableHeight()
})
/**
 * 获取审批流程下拉列表
 */
const getFlowlistCombo = () => {
  getFlowlistComboByFlowTypeApi(EFlowType.资源审批流程).then((res: IAxios<Tadindustrylist[]>) => {
    if (res.success) {
      flowTypeList.value = res.obj
    }
  })
}

/**
 * 查询
 */
const handleQuery = () => {
  getAdResourceApplicationPageListApi(queryParams)
    .then((res: IAxios<IAdResourceApplicationDTO[]>) => {
      if (res.success) {
        adResourcApplicationList.value = res.obj
        total.value = res.total
      }
    })
    .finally(() => {})
}
/**
 * 新增
 */
const handleAdd = () => {
  dialogRef.value.init(ResourceApplicationDialogMode.Add, flowTypeList.value)
  checkList.value = []
}
/**
 * 提交审核按钮事件
 */
const onCheckFlow = (row: IAdResourceApplicationDTO) => {
  adResourceApplicationForm.value = { ...row }
  if (flowTypeList.value.length === 1) {
    adResourceApplicationForm.value.flowid = flowTypeList.value[0].id
    onToCheck()
  } else {
    dialogToCheckVisible.value = true
  }
}
/**
 * 提交预定按钮事件
 */
const onCheckTworder = (row: IAdResourceApplicationDTO) => {
  // const to = '/ad/adtworder?Resourceid=' + row.id
  // router.push(to)
  dialogVisible.value = true
  IAdResourceApplicationDTOData.value = row
}
/**
 * 提交审核
 * @param row
 */
const onToCheck = () => {
  ElMessageBox.confirm('确定提交审核吗？提交后将不能进行修改', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(() => {
      const ids = []
      ids.push(adResourceApplicationForm.value.id)
      const data = {
        ids: ids.join(','),
        flowid: adResourceApplicationForm.value.flowid as string
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
const onEdit = (row: IAdResourceApplicationDTO) => {
  dialogRef.value.init(ResourceApplicationDialogMode.Edit, flowTypeList.value, row)
  checkList.value = []
  if (row.iapprovestatus !== EApproveStatus.待审 && row.iapprovestatus !== EApproveStatus.在审) {
    const checkinfo = {
      iapprovestatus: row.iapprovestatus,
      sapprovalopinions: row.sapprovalopinions
    } as IAdResourceApplicationDTO
    checkList.value.push(checkinfo)
  }
}
/**
 * 删除按钮事件
 * @param row
 */
const onDelete = (row: IAdResourceApplicationDTO | undefined) => {
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
    if (row.iapprovestatus !== EApproveStatus.待审 && row.iapprovestatus !== EApproveStatus.否决) {
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
      deleteAdResourceApplicationByIdApi(data)
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
 * 查看详情按钮事件
 * @param row
 */
const onDetail = (row: IAdResourceApplicationDTO) => {
  dialogDetailVisible.value = true
  adResourceApplicationForm.value = { ...row }
}
/**
 * 查看历史按钮事件
 * @param row
 */
const onHistory = (row: IAdResourceApplicationDTO) => {
  const data = {
    id: row.id as string
  }
  getProcessInstanceByIdApi(data).then((res: IAxios<TypeCurrentData[]>) => {
    if (res.success) {
      historyList.value = res.obj
      dialogHistoryVisible.value = true
      Seelistdetail(historyList.value[0])
    }
  })
}
/**
 * 查看每条历史详情
 * @param val
 */
const Seelistdetail = (val: TypeCurrentData) => {
  currentData.value.processInstanceId = val.processInstanceId
  currentData.value.flowId = val.flowId
  currentData.value.formData = val.formData
}
/**
 * 提交审核意见 (先注释)
 */
// const saveCheckInfo = () => {
//   if (adResourceApplicationForm.value.sapprovalopinions === '') {
//     ElMessage.warning('审核意见不允许为空')
//     return
//   }
//   adResourceApplicationForm.value.iapprovestatus = iapprovestatus.value
//   saveCheckInfoApi(adResourceApplicationForm.value)
//     .then((res: IAxios) => {
//       if (res.success) {
//         ElMessage.success('提交成功')
//         handleQuery()
//         adResourceApplicationFormRef.value?.resetFields()
//         dialogEditVisible.value = false
//       }
//     })
//     .catch(() => {})
// }
/**
 * 选择人员
 * @param val
 */
const onSelectionEmp = (val: TSelectZtree[]) => {
  const employid = val.map((item) => item.id).join(',')
  queryParams.employid = employid
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
/**
 * 取消
 */
const handleCancel = () => {
  dialogVisible.value = false
}
/**
 * 保存信息关闭订单弹窗
 */
const closedialogVisible = (val: string) => {
  handleQuery()
  dialogVisible.value = false
}
</script>

<style scoped></style>
